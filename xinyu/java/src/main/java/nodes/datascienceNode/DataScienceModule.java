package nodes.datascienceNode;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import nodes.datascienceNode.stockInfo.StockAnalysis;
import nodes.datascienceNode.stockInfo.facade.impl.MarketPerEmployeeFeature;
import nodes.datascienceNode.stockInfo.facade.impl.StockCompanyFeatureGroup;
import nodes.datascienceNode.stockInfo.facade.impl.annota.MarketPerEmployee;
import nodes.featureEngineeringNode.BaseFeatureGroup;
import nodes.featureEngineeringNode.facade.Feature;
import nodes.stockinfoNode.models.StockCompanyPOJO;

/**
 * Created by Xinyu Zhu on 2020/11/14, 19:20
 * nodes.datascienceNode in codingDimensionTemplate
 */
public class DataScienceModule extends AbstractModule {

    @Override
    protected void configure() {
        super.configure();

        // For stockInfo
        bind(StockAnalysis.class);
        bind(new TypeLiteral<Feature<StockCompanyPOJO, Double>>() {
        }).annotatedWith(MarketPerEmployee.class).to(MarketPerEmployeeFeature.class);

        bind(new TypeLiteral<BaseFeatureGroup<StockCompanyPOJO>>(){}).to(StockCompanyFeatureGroup.class);


    }
}
