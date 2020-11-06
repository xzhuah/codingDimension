package nodes.hotinfoNode.models;


import nodes.hotinfoNode.constants.KeyValueConstant;

import java.util.Objects;

/**
 * Created by Xinyu Zhu on 6/30/2020, 5:39 AM
 */
public class RankingRuleVO {

    private KeyValueConstant.TypeCode rankType;

    public RankingRuleVO(KeyValueConstant.TypeCode rankType) {
        this.rankType = rankType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RankingRuleVO that = (RankingRuleVO) o;
        return rankType == that.rankType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rankType);
    }


    public KeyValueConstant.TypeCode getRankType() {
        return rankType;
    }

    public void setRankType(KeyValueConstant.TypeCode rankType) {
        this.rankType = rankType;
    }


    @Override
    public String toString() {
        return "RankingRuleVO{" +
                "rankType=" + rankType +
                '}';
    }

    public String getUrl() {
        return "https://www.bilibili.com/v/popular/rank/" + this.rankType.getCode();
    }
}
