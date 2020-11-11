package nodes.hotinfoNode.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import nodes.hotinfoNode.constants.KeyValueConstant;

/**
 * Created by Xinyu Zhu on 6/30/2020, 5:39 AM
 */
@Data
@AllArgsConstructor
public class RankingRuleVO {

    private KeyValueConstant.TypeCode rankType;

    public String getUrl() {
        return "https://www.bilibili.com/v/popular/rank/" + this.rankType.getCode();
    }
}
