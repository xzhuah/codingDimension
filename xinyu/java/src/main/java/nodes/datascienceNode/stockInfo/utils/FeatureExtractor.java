package nodes.datascienceNode.stockInfo.utils;

import nodes.stockinfoNode.models.StockCompanyPOJO;

/**
 * Created by Xinyu Zhu on 2020/11/14, 20:26
 * nodes.datascienceNode.stockInfo.utils in codingDimensionTemplate
 */
public class FeatureExtractor {
    public static double getMarketPerEmployee(StockCompanyPOJO companyPOJO) {
        return companyPOJO.getMarket() * 1.0 / (companyPOJO.getEmployee() * 1.0);
    }
}
