package nodes.datascienceNode.stockInfo.facade.impl;

import com.google.inject.Singleton;
import nodes.featureEngineeringNode.facade.ComparableFeature;
import nodes.stockinfoNode.models.StockCompanyPOJO;

/**
 * Created by Xinyu Zhu on 2020/11/14, 21:41
 * nodes.datascienceNode.stockInfo.facade.impl in codingDimensionTemplate
 */
@Singleton
public class MarketPerEmployeeFeature implements ComparableFeature<StockCompanyPOJO, Double> {

    @Override
    public Double extractForInstance(StockCompanyPOJO companyPOJO) {
        if (companyPOJO.getEmployee() == 0) {
            return 0.0;
        }
        return companyPOJO.getMarket() * 1.0 / (companyPOJO.getEmployee() * 1.0);
    }
}
