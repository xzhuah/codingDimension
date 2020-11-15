package nodes.datascienceNode.stockInfo;

import com.google.inject.Inject;
import nodes.NodeModule;
import nodes.datascienceNode.stockInfo.facade.impl.annota.MarketPerEmployee;
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

    @Inject
    public StockAnalysis(StockInfoService stockInfoService,
                         @MarketPerEmployee Feature<StockCompanyPOJO, Double> marketPerEmployeeFeature) {
        this.stockInfoService = stockInfoService;
        this.stockInfoService.setAutoUpdate(false);

        this.marketPerEmployeeFeature = marketPerEmployeeFeature;
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

    public static void main(String[] args) {
        StockAnalysis stockAnalysis = NodeModule.getGlobalInjector().getInstance(StockAnalysis.class);
        stockAnalysis.analysis();
    }

}
