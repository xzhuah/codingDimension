package nodes.datascienceNode.stockInfo.facade.impl;

import nodes.featureEngineeringNode.facade.ComparableFeature;
import nodes.stockinfoNode.models.StockCompanyPOJO;

/**
 * Created by Xinyu Zhu on 2020/11/14, 21:41
 * nodes.datascienceNode.stockInfo.facade.impl in codingDimensionTemplate
 */
public class MarketPerEmployeeFeature implements ComparableFeature<StockCompanyPOJO, Double> {

    @Override
    public Double extractForInstance(StockCompanyPOJO companyPOJO) {
        return companyPOJO.getMarket() * 1.0 / (companyPOJO.getEmployee() * 1.0);
    }
}
