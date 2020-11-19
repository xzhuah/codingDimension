package nodes.stockinfoNode.models;

import common.io.database.mongodb.BaseMongoPOJO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by Xinyu Zhu on 2020/11/6, 23:36
 * nodes.stockinfoNode.models in codingDimensionTemplate
 */
@Data
@NoArgsConstructor
public class StockDailyRecordPOJO extends BaseMongoPOJO {

    // These two are primary key
    private String symbol;
    private long time;

    // These are other data
    @EqualsAndHashCode.Exclude private double open;
    @EqualsAndHashCode.Exclude private double high;
    @EqualsAndHashCode.Exclude private double low;
    @EqualsAndHashCode.Exclude private double close;
    @EqualsAndHashCode.Exclude private double volume;
    @EqualsAndHashCode.Exclude private double adjustedClose;
    @EqualsAndHashCode.Exclude private double dividend;
    @EqualsAndHashCode.Exclude private double splitCoefficient;
}
