package nodes.stockinfoNode.models;

import common.io.database.mongodb.BaseMongoPOJO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by Xinyu Zhu on 2020/11/8, 21:31
 * nodes.stockinfoNode.models in codingDimensionTemplate
 */
@Data
@NoArgsConstructor
public class StockCompanyPOJO extends BaseMongoPOJO {

    // Primary key
    private String symbol;

    @EqualsAndHashCode.Exclude
    private String name;
    @EqualsAndHashCode.Exclude
    private String exchange;
    @EqualsAndHashCode.Exclude
    private String sector;
    @EqualsAndHashCode.Exclude
    private String industry;
    @EqualsAndHashCode.Exclude
    private String country;

    @EqualsAndHashCode.Exclude
    private int employee;
    @EqualsAndHashCode.Exclude
    private long market;

}
