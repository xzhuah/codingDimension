package nodes.personalAccountingNode.models;

import lombok.Data;

/**
 * Created by Xinyu Zhu on 2020/11/18, 21:22
 * nodes.personalAccountingNode.models in codingDimensionTemplate
 */
@Data
public class StockTransactionPOJO extends CashFlowTransactionPOJO{
    // actually will be the line number
    protected int primaryId;
    private double shares;
}
