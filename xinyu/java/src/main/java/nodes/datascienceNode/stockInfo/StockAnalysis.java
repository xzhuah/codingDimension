package nodes.datascienceNode.stockInfo;

import com.google.inject.Inject;
import nodes.NodeModule;
import nodes.datascienceNode.stockInfo.facade.impl.EmployeeNumFeature;
import nodes.datascienceNode.stockInfo.facade.impl.MarketFeature;
import nodes.datascienceNode.stockInfo.facade.impl.MarketPerEmployeeFeature;
import nodes.datascienceNode.stockInfo.facade.impl.StockExpectedReturnFeature;
import nodes.datascienceNode.stockInfo.models.StockReturnIndicator;
import nodes.featureEngineeringNode.BaseFeatureGroup;
import nodes.featureEngineeringNode.facade.ComparableFeature;
import nodes.featureEngineeringNode.facade.Feature;
import nodes.stockinfoNode.StockInfoService;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/14, 19:19
 * nodes.datascienceNode.stockInfo in codingDimensionTemplate
 */
public class StockAnalysis {
    private final StockInfoService stockInfoService;

    // Used to sort company report
    private final ComparableFeature<StockCompanyPOJO, Double> marketPerEmployeeFeature;
    private final BaseFeatureGroup<StockCompanyPOJO> companyFeatureGroup;


    // Used to sort price report
    private final ComparableFeature<List<StockDailyRecordPOJO>, StockReturnIndicator> returnFeatureMain;
    private final BaseFeatureGroup<List<StockDailyRecordPOJO>> priceFeatureGroup;

    @Inject
    public StockAnalysis(StockInfoService stockInfoService,
                         BaseFeatureGroup<StockCompanyPOJO> companyFeatureGroup,
                         BaseFeatureGroup<List<StockDailyRecordPOJO>> priceFeatureGroup) {
        this.stockInfoService = stockInfoService;
        this.stockInfoService.setAutoUpdate(false);

        this.companyFeatureGroup = companyFeatureGroup;
        this.priceFeatureGroup = priceFeatureGroup;

        // This is the preferred way to use feature and feature group
        this.marketPerEmployeeFeature = new MarketPerEmployeeFeature();
        this.companyFeatureGroup.addFeature(this.marketPerEmployeeFeature);
        this.companyFeatureGroup.addFeature(new MarketFeature());
        this.companyFeatureGroup.addFeature(new EmployeeNumFeature());


        int sampleDuration = 30;
        this.returnFeatureMain = new StockExpectedReturnFeature(sampleDuration, 0.01);
        this.priceFeatureGroup.addFeature(this.returnFeatureMain);
        this.priceFeatureGroup.addFeature(new StockExpectedReturnFeature(sampleDuration, 0.02));
        this.priceFeatureGroup.addFeature(new StockExpectedReturnFeature(sampleDuration, 0.03));
        this.priceFeatureGroup.addFeature(new StockExpectedReturnFeature(sampleDuration, 0.04));
        this.priceFeatureGroup.addFeature(new StockExpectedReturnFeature(sampleDuration, 0.05));
        this.priceFeatureGroup.addFeature(new StockExpectedReturnFeature(sampleDuration, 0.06));
        this.priceFeatureGroup.addFeature(new StockExpectedReturnFeature(sampleDuration, 0.07));
        this.priceFeatureGroup.addFeature(new StockExpectedReturnFeature(sampleDuration, 0.08));
    }

    public void analysis() {
        List<String> allSymbol = this.stockInfoService.getAllSymbols();
        this.printReportForCompanies(allSymbol);
        this.printPriceReportForCompanies(allSymbol);
    }

    public void printPriceReportForCompanies(List<String> companySymbol) {
        // Convert to real data
        List<List<StockDailyRecordPOJO>> dailyPriceData = new ArrayList<>();
        for (String company : companySymbol) {
            dailyPriceData.add(stockInfoService.getSortedPriceForSymbol(company));
        }
        // sort by feature value
        dailyPriceData = this.returnFeatureMain.sortInstanceWithFeature(dailyPriceData, true);

        // Generate report based on sorted feature
        List<String> fields = priceFeatureGroup.getFeatureNameAsList();
        List<List<String>> values = priceFeatureGroup.getFeatureValueAsString(dailyPriceData);
        System.out.println("Symbol\t" + String.join("\t", fields));
        for (int i = 0; i < values.size(); i++) {
            System.out.println(dailyPriceData.get(i).get(0).getSymbol() + "\t" + String.join("\t", values.get(i)));
        }
    }

    public void printReportForCompanies(List<String> companys) {
        // Convert to real data
        List<StockCompanyPOJO> companyPOJOS = stockInfoService.sortCompanyByMarket(companys);

        // sort by feature value
        companyPOJOS = this.marketPerEmployeeFeature.sortInstanceWithFeature(companyPOJOS, true);

        // Generate report based on sorted feature
        List<String> fields = companyFeatureGroup.getFeatureNameAsList();
        List<List<String>> values = companyFeatureGroup.getFeatureValueAsString(companyPOJOS);
        System.out.println("Symbol\t" + String.join("\t", fields));
        for (int i = 0; i < companyPOJOS.size(); i++) {
            System.out.println(companyPOJOS.get(i).getSymbol() + "\t" + String.join("\t", values.get(i)));
        }
    }


    public static void main(String[] args) {
        StockAnalysis stockAnalysis = NodeModule.getGlobalInjector().getInstance(StockAnalysis.class);
        stockAnalysis.analysis();
    }

}
