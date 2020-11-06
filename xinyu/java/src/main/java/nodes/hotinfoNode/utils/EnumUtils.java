package nodes.hotinfoNode.utils;


import nodes.hotinfoNode.constants.KeyValueConstant;
import nodes.hotinfoNode.constants.ValueConstant;
import nodes.hotinfoNode.models.RankingRuleVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Xinyu Zhu on 6/30/2020, 6:28 AM
 */
public class EnumUtils {
    public static Map<String, String> keyValueEnumToMap(KeyValueConstant[] keyValueConstants, boolean reverseKeyValue) {
        Map<String, String> result = new HashMap<>();
        if (!reverseKeyValue) {
            for (KeyValueConstant keyValue : keyValueConstants) {
                result.put(keyValue.getKey().toString(), keyValue.getValue().toString());
            }
        } else {
            for (KeyValueConstant keyValue : keyValueConstants) {
                result.put(keyValue.getValue().toString(), keyValue.getKey().toString());
            }
        }
        return result;
    }

    public static List<RankingRuleVO> getAllPossibleRankingRule() {
        List<RankingRuleVO> rankingRules = new ArrayList<>();
        for (KeyValueConstant.TypeCode typeCode : KeyValueConstant.TypeCode.values()) {
            rankingRules.add(new RankingRuleVO(typeCode));
        }
        return rankingRules;
    }

    public static int getRandomCrawlerWaitTime() {
        return ThreadLocalRandom.current().nextInt(ValueConstant.CrawlerParameter.MINMUM_WAIT_TIME.getValue(), ValueConstant.CrawlerParameter.MAXIMUM_WAIT_TIME.getValue() + 1);
    }

    public static int getRandomCrawlerProcessRequest() {
        return ThreadLocalRandom.current().nextInt(ValueConstant.CrawlerParameter.MINIMUM_CONCURRENT_REQUEST.getValue(), ValueConstant.CrawlerParameter.MAXIMUM_CONCURRENT_REQUEST.getValue() + 1);
    }

}