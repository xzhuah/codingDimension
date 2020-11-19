package common.io.database.mongodb;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bson.types.ObjectId;

/**
 * Created by Xinyu Zhu on 2020/11/18, 21:32
 * common.io.database.mongodb in codingDimensionTemplate
 */
@Data
@ToString(exclude = "id")
public class BaseMongoPOJO {
    @EqualsAndHashCode.Exclude protected ObjectId id;
}
