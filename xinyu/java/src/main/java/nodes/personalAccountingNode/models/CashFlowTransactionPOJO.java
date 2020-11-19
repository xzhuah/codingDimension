package nodes.personalAccountingNode.models;

import common.io.database.mongodb.BaseMongoPOJO;
import lombok.Data;

/**
 * Created by Xinyu Zhu on 2020/11/18, 21:21
 * nodes.personalAccountingNode.models in codingDimensionTemplate
 */
@Data
public class CashFlowTransactionPOJO extends BaseMongoPOJO {
    protected String account;
    protected double amount;
    protected long time;
}
