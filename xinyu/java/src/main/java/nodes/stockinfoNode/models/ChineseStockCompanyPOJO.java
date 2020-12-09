package nodes.stockinfoNode.models;

import common.io.database.mongodb.BaseMongoPOJO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by Xinyu Zhu on 2020/12/2, 23:46
 * nodes.stockinfoNode.models in codingDimensionTemplate
 */
@Data
@NoArgsConstructor
public class ChineseStockCompanyPOJO extends BaseMongoPOJO {
    // primary key
    private String ts_code;

    @EqualsAndHashCode.Exclude
    private String symbol;
    @EqualsAndHashCode.Exclude
    private String name;
    @EqualsAndHashCode.Exclude
    private String area;
    @EqualsAndHashCode.Exclude
    private String industry;
}
