package nodes.datascienceNode.stockInfo.facade.impl;

import nodes.featureEngineeringNode.facade.ComparableFeature;
import nodes.stockinfoNode.models.StockCompanyPOJO;

/**
 * Created by Xinyu Zhu on 2020/11/15, 3:01
 * nodes.datascienceNode.stockInfo.facade.impl in codingDimensionTemplate
 */
public class EmployeeNumFeature implements ComparableFeature<StockCompanyPOJO, Integer> {

    @Override
    public Integer extractForInstance(StockCompanyPOJO target) {
        return target.getEmployee();
    }

    @Override
    public String getFeatureName() {
        return "Employee";
    }
}
