package nodes.datascienceNode.stockInfo.impl;

import com.google.inject.Inject;
import nodes.NodeModule;
import nodes.datascienceNode.stockInfo.StockFeatureAnalysis;
import nodes.datascienceNode.stockInfo.constants.Config;
import nodes.datascienceNode.stockInfo.facade.impl.EmployeeNumFeature;
import nodes.datascienceNode.stockInfo.facade.impl.MarketFeature;
import nodes.datascienceNode.stockInfo.facade.impl.MarketPerEmployeeFeature;
import nodes.datascienceNode.stockInfo.facade.impl.featureGroup.StockCompanyFeatureGroup;
import nodes.datascienceNode.stockInfo.utils.ReportPrinter;
import nodes.featureEngineeringNode.BaseFeatureGroup;
import nodes.featureEngineeringNode.facade.ComparableFeature;
import nodes.featureEngineeringNode.facade.Feature;
import nodes.stockinfoNode.StockInfoService;
import nodes.stockinfoNode.models.StockCompanyPOJO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Xinyu Zhu on 2020/11/14, 19:19
 * nodes.datascienceNode.stockInfo in codingDimensionTemplate
 */
public class StockCompanyAnalysisImpl implements StockFeatureAnalysis<StockCompanyPOJO> {
    private final StockInfoService stockInfoService;

    // Used to sort company report
    private ComparableFeature<StockCompanyPOJO, ? extends Comparable> companyPrimaryFeature;
    private BaseFeatureGroup<StockCompanyPOJO> featureGroup;

    @Inject
    public StockCompanyAnalysisImpl(StockInfoService stockInfoService) {
        this.stockInfoService = stockInfoService;
        this.stockInfoService.setAutoUpdate(Config.AUTO_UPDATE);

        this.featureGroup = new StockCompanyFeatureGroup();

        // This is the preferred way to use feature and feature group
        this.companyPrimaryFeature = new MarketPerEmployeeFeature();
        this.featureGroup.addFeature(this.companyPrimaryFeature);
        this.featureGroup.addFeature(new MarketFeature());
        this.featureGroup.addFeature(new EmployeeNumFeature());
    }

    @Override
    public void setFeature(ComparableFeature<StockCompanyPOJO, ? extends Comparable> primaryCompanyFeature, List<Feature<StockCompanyPOJO, ?>> otherCompanyFeature) {
        this.companyPrimaryFeature = primaryCompanyFeature;
        this.featureGroup.clearFeature();
        this.featureGroup.addFeature(this.companyPrimaryFeature);
        for (Feature<StockCompanyPOJO, ?> otherFeature : otherCompanyFeature) {
            this.featureGroup.addFeature(otherFeature);
        }
    }

    @Override
    public void printReportForTarget(boolean sortByPrimaryFeature) {
        printReportForTarget(this.stockInfoService.getAllSymbols(), sortByPrimaryFeature);
    }

    @Override
    public void printReportForTarget(List<String> companies, boolean sortByPrimaryFeature) {
        companies = this.stockInfoService.filterSymbols(companies);
        // Convert to real data
        List<StockCompanyPOJO> companyPOJOS = stockInfoService.sortCompanyByMarket(companies);

        if (sortByPrimaryFeature) {
            // sort by feature value
            companyPOJOS = this.companyPrimaryFeature.sortInstanceWithFeature(companyPOJOS, true);
        }

        // Generate report based on sorted feature
        List<String> fields = featureGroup.getFeatureNameAsList();
        List<List<String>> values = featureGroup.getFeatureValueAsString(companyPOJOS);

        ReportPrinter.printReport(fields,
                companyPOJOS.stream().map(companyPOJO -> companyPOJO.getSymbol()).collect(Collectors.toList()),
                values, "Symbol", true);

    }


    public static void main(String[] args) {
        StockCompanyAnalysisImpl stockCompanyAnalysisImpl = NodeModule.getGlobalInjector().getInstance(StockCompanyAnalysisImpl.class);

        stockCompanyAnalysisImpl.setFeature(new MarketFeature(),
                new EmployeeNumFeature(),
                new MarketPerEmployeeFeature());

        stockCompanyAnalysisImpl.printReportForTarget();
    }

}
