package nodes.datascienceNode;

import com.google.inject.AbstractModule;
import nodes.datascienceNode.stockInfo.StockAnalysis;

/**
 * Created by Xinyu Zhu on 2020/11/14, 19:20
 * nodes.datascienceNode in codingDimensionTemplate
 */
public class DataScienceModule extends AbstractModule {

    @Override
    protected void configure() {
        super.configure();
        bind(StockAnalysis.class);
    }
}
