package nodes.stockinfoNode.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;

/**
 * Created by Xinyu Zhu on 2020/11/6, 23:36
 * nodes.stockinfoNode.models in codingDimensionTemplate
 */
@Data
@ToString(exclude = "id")
@NoArgsConstructor
public class StockDailyRecordPOJO {

    // This is POJO indicator: real primary key
    @EqualsAndHashCode.Exclude private ObjectId id;

    // These two are primary key
    private String stockSymbol;
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
