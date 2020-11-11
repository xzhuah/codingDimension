package nodes.stockinfoNode.models;

import lombok.Data;
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
    private ObjectId id;

    private String symbol;
    private String name;
    private String exchange;
    private String sector;
    private String industry;
    private String country;
}
