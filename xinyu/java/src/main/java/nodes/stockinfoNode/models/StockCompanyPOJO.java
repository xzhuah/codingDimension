package nodes.stockinfoNode.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;

/**
 * Created by Xinyu Zhu on 2020/11/8, 21:31
 * nodes.stockinfoNode.models in codingDimensionTemplate
 */
@Data
@ToString(exclude = "id")
@NoArgsConstructor
public class StockCompanyPOJO {
    // This is POJO indicator: real primary key
    @EqualsAndHashCode.Exclude private ObjectId id;

    // Primary key
    private String symbol;

    @EqualsAndHashCode.Exclude private String name;
    @EqualsAndHashCode.Exclude private String exchange;
    @EqualsAndHashCode.Exclude private String sector;
    @EqualsAndHashCode.Exclude private String industry;
    @EqualsAndHashCode.Exclude private String country;

    @EqualsAndHashCode.Exclude private int employee;
    @EqualsAndHashCode.Exclude private long market;

}
