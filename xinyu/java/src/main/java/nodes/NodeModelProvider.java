package nodes;

import common.framework.BaseModelProvider;
import nodes.stockinfoNode.StockInfoModelProvider;

/**
 * Created by Xinyu Zhu on 2020/12/7, 22:02
 * nodes in codingDimensionTemplate
 */
public class NodeModelProvider extends BaseModelProvider {
    @Override
    protected void configure() {
        super.configure();
        install(new StockInfoModelProvider());
    }
}
