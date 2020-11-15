package nodes.datascienceNode.stockInfo.facade.impl;

import nodes.featureEngineeringNode.facade.ComparableFeature;
import nodes.stockinfoNode.models.StockCompanyPOJO;

/**
 * Created by Xinyu Zhu on 2020/11/15, 3:01
 * nodes.datascienceNode.stockInfo.facade.impl in codingDimensionTemplate
 */
public class MarketFeature implements ComparableFeature<StockCompanyPOJO, Long> {
    @Override
    public Long extractForInstance(StockCompanyPOJO target) {
        return target.getMarket();
    }

    @Override
    public String getFeatureName() {
        return "Market";
    }
}
