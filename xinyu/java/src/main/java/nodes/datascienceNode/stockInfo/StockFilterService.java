package nodes.datascienceNode.stockInfo;

import java.util.Map;

/**
 * Created by Xinyu Zhu on 2020/11/29, 0:44
 * nodes.datascienceNode.stockInfo in codingDimensionTemplate
 */
public interface StockFilterService {


    Map<String, Double> filterGoldenCrossStock(int n, int m);
}
