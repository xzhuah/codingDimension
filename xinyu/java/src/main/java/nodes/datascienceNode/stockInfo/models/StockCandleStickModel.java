package nodes.datascienceNode.stockInfo.models;

import lombok.Data;

/**
 * Created by Xinyu Zhu on 2020/12/8, 20:02
 * nodes.datascienceNode.stockInfo.models in codingDimensionTemplate
 */
@Data
public class StockCandleStickModel {
    private double open;
    private double high;
    private double low;
    private double close;
}
