package nodes.stockinfoNode.models;

import lombok.Data;
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
    private ObjectId id;

    // These two are primary key
    private String stockSymbol;
    private long time;

    // These are other data
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;
    private double adjustedClose;
    private double dividend;
    private double splitCoefficient;
}
