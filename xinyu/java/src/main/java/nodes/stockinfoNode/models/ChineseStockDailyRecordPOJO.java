package nodes.stockinfoNode.models;

import common.io.database.mongodb.BaseMongoPOJO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by Xinyu Zhu on 2020/12/2, 23:31
 * nodes.stockinfoNode.models in codingDimensionTemplate
 */
@Data
@NoArgsConstructor
public class ChineseStockDailyRecordPOJO extends BaseMongoPOJO {

    // These two are primary key
    private String ts_code;
    private long time;

    // 成交额 （千元）
    @EqualsAndHashCode.Exclude
    private double amount;

    // These are other data
    @EqualsAndHashCode.Exclude
    private double open;
    @EqualsAndHashCode.Exclude
    private double high;
    @EqualsAndHashCode.Exclude
    private double low;
    @EqualsAndHashCode.Exclude
    private double close;
    @EqualsAndHashCode.Exclude
    private double volume;

}
