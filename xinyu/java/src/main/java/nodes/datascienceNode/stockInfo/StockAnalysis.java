package nodes.datascienceNode.stockInfo;

import com.google.inject.Inject;
import nodes.NodeModule;
import nodes.datascienceNode.stockInfo.facade.impl.annota.MarketPerEmployee;
import nodes.featureEngineeringNode.BaseFeatureGroup;
import nodes.featureEngineeringNode.facade.Feature;
import nodes.stockinfoNode.StockInfoService;
import nodes.stockinfoNode.models.StockCompanyPOJO;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/14, 19:19
 * nodes.datascienceNode.stockInfo in codingDimensionTemplate
 */
public class StockAnalysis {
    private final StockInfoService stockInfoService;
    private final Feature<StockCompanyPOJO, Double> marketPerEmployeeFeature;
    private final BaseFeatureGroup<StockCompanyPOJO> companyFeatureGroup;

    @Inject
    public StockAnalysis(StockInfoService stockInfoService,
                         BaseFeatureGroup<StockCompanyPOJO> companyFeatureGroup,
                         @MarketPerEmployee Feature<StockCompanyPOJO, Double> marketPerEmployeeFeature) {
        this.stockInfoService = stockInfoService;
        this.stockInfoService.setAutoUpdate(false);

        this.companyFeatureGroup = companyFeatureGroup;
        this.marketPerEmployeeFeature = marketPerEmployeeFeature;

        // This is the preferred way to use feature group
        this.companyFeatureGroup.addFeature(this.marketPerEmployeeFeature);
    }

    public void analysis() {

        List<StockCompanyPOJO> allCompany = this.stockInfoService.sortCompanyByMarket();


        List<StockCompanyPOJO> sortedCompany = marketPerEmployeeFeature.sortInstanceWithFeature(allCompany);
        List<Double> featureValue = marketPerEmployeeFeature.extractForInstance(sortedCompany);

        for (int i = featureValue.size() - 1; i >= 0; i--) {
            System.out.println(marketPerEmployeeFeature.getFeatureName() + ": " + featureValue.get(i));
            System.out.println(sortedCompany.get(i));
        }
    }

    public void printReportForCompanies(List<StockCompanyPOJO> companyPOJOS) {
        List<String> fields = companyFeatureGroup.getFeatureNameAsList();
        List<List<String>> values = companyFeatureGroup.getFeatureValueAsString(companyPOJOS);
        System.out.println("Symbol\t" + String.join("\t", fields));
        for (int i = 0; i < companyPOJOS.size(); i++) {
            System.out.println(companyPOJOS.get(i).getSymbol() + "\t" + String.join("\t", values.get(i)));
        }

    }

    public static void main(String[] args) {
        StockAnalysis stockAnalysis = NodeModule.getGlobalInjector().getInstance(StockAnalysis.class);

        //System.out.println(stockAnalysis.marketPerEmployeeFeature.isComparable());

        List<StockCompanyPOJO> allCompany = stockAnalysis.stockInfoService.sortCompanyByMarket();

        List<StockCompanyPOJO> sortedCompany = stockAnalysis.marketPerEmployeeFeature.sortInstanceWithFeature(allCompany, true);

        stockAnalysis.printReportForCompanies(sortedCompany);
    }

}
