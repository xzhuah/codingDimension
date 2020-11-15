package nodes.datascienceNode.stockInfo.impl;

import com.google.inject.Inject;
import nodes.NodeModule;
import nodes.datascienceNode.stockInfo.StockFeatureAnalysis;
import nodes.datascienceNode.stockInfo.constants.Config;
import nodes.datascienceNode.stockInfo.facade.impl.StockExpectedReturnFeature;
import nodes.datascienceNode.stockInfo.facade.impl.StockPriceFeatureGroup;
import nodes.featureEngineeringNode.BaseFeatureGroup;
import nodes.featureEngineeringNode.facade.ComparableFeature;
import nodes.featureEngineeringNode.facade.Feature;
import nodes.stockinfoNode.StockInfoService;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/15, 3:58
 * nodes.datascienceNode.stockInfo in codingDimensionTemplate
 */
public class StockPriceAnalysisImpl implements StockFeatureAnalysis<List<StockDailyRecordPOJO>> {
    private final StockInfoService stockInfoService;

    // Used to sort price report
    private ComparableFeature<List<StockDailyRecordPOJO>, ? extends Comparable> primaryFeature;
    private BaseFeatureGroup<List<StockDailyRecordPOJO>> featureGroup;

    @Inject
    public StockPriceAnalysisImpl(StockInfoService stockInfoService) {
        this.stockInfoService = stockInfoService;
        this.stockInfoService.setAutoUpdate(Config.AUTO_UPDATE);

        this.featureGroup = new StockPriceFeatureGroup();

        int sampleDuration = 30;
        this.primaryFeature = new StockExpectedReturnFeature(sampleDuration, 0.01);
        this.featureGroup.addFeature(this.primaryFeature);
        this.featureGroup.addFeature(new StockExpectedReturnFeature(sampleDuration, 0.02));
    }

    @Override
    public void setFeature(ComparableFeature<List<StockDailyRecordPOJO>, ? extends Comparable> primaryPriceFeature, List<Feature<List<StockDailyRecordPOJO>, ?>> otherPriceFeature) {
        this.primaryFeature = primaryPriceFeature;
        this.featureGroup.clearFeature();
        this.featureGroup.addFeature(this.primaryFeature);
        for (Feature<List<StockDailyRecordPOJO>, ?> otherFeature : otherPriceFeature) {
            this.featureGroup.addFeature(otherFeature);
        }
    }

    @Override
    public void printReportForTarget(boolean sortByPrimaryFeature) {
        printReportForTarget(this.stockInfoService.getAllSymbols(), sortByPrimaryFeature);
    }

    @Override
    public void printReportForTarget(List<String> companySymbol, boolean sortByPrimaryFeature) {
        // Convert to real data
        companySymbol = this.stockInfoService.filterSymbols(companySymbol);
        List<List<StockDailyRecordPOJO>> dailyPriceData = new ArrayList<>();
        for (String company : companySymbol) {
            dailyPriceData.add(stockInfoService.getSortedPriceForSymbol(company));
        }
        if (sortByPrimaryFeature) {
            // sort by feature value
            dailyPriceData = this.primaryFeature.sortInstanceWithFeature(dailyPriceData, true);
        }

        // Generate report based on sorted feature
        List<String> fields = featureGroup.getFeatureNameAsList();
        List<List<String>> values = featureGroup.getFeatureValueAsString(dailyPriceData);
        System.out.println("Symbol\t" + String.join("\t", fields));
        for (int i = 0; i < values.size(); i++) {
            System.out.println(dailyPriceData.get(i).get(0).getSymbol() + "\t" + String.join("\t", values.get(i)));
        }
    }

    public static void main(String[] args) {
        StockPriceAnalysisImpl stockPriceAnalysisImpl = NodeModule.getGlobalInjector().getInstance(StockPriceAnalysisImpl.class);
        int sampleDuration = 30;
        stockPriceAnalysisImpl.setFeature(new StockExpectedReturnFeature(sampleDuration, 0.01),
                new StockExpectedReturnFeature(sampleDuration, 0.015),
                new StockExpectedReturnFeature(sampleDuration, 0.02),
                new StockExpectedReturnFeature(sampleDuration, 0.025),
                new StockExpectedReturnFeature(sampleDuration, 0.03),
                new StockExpectedReturnFeature(sampleDuration, 0.035),
                new StockExpectedReturnFeature(sampleDuration, 0.04));

        stockPriceAnalysisImpl.printReportForTarget();
    }
}