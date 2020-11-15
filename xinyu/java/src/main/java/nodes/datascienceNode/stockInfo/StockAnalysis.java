package nodes.datascienceNode.stockInfo;

import com.google.inject.Inject;
import nodes.NodeModule;
import nodes.datascienceNode.stockInfo.utils.FeatureExtractor;
import nodes.stockinfoNode.StockInfoService;
import nodes.stockinfoNode.models.StockCompanyPOJO;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/14, 19:19
 * nodes.datascienceNode.stockInfo in codingDimensionTemplate
 */
public class StockAnalysis {
    private final StockInfoService stockInfoService;

    @Inject
    public StockAnalysis(StockInfoService stockInfoService) {
        this.stockInfoService = stockInfoService;
        this.stockInfoService.setAutoUpdate(false);
    }

    public void analysis() {
        System.out.println("Hello");
        List<StockCompanyPOJO> allCompany = this.stockInfoService.sortCompanyByMarket();
        allCompany.sort((company1, company2) -> {
            double diffInMarketPerEmployee = FeatureExtractor.getMarketPerEmployee(company1) - FeatureExtractor.getMarketPerEmployee(company2);
            if (diffInMarketPerEmployee > 0) {
                return 1;
            } else if (diffInMarketPerEmployee == 0) {
                return 0;
            } else {
                return -1;
            }
        });
    }

    public static void main(String[] args) {
        StockAnalysis stockAnalysis = NodeModule.getGlobalInjector().getInstance(StockAnalysis.class);
        stockAnalysis.analysis();
    }

}
