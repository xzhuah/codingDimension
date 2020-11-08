package nodes.hotinfoNode.crawler;


import nodes.hotinfoNode.models.RankingRuleVO;
import nodes.hotinfoNode.models.VideoRecordVO;

import java.util.List;
import java.util.Map;

/**
 * Created by Xinyu Zhu on 7/1/2020, 12:08 PM
 */
public interface BilibiliHotRankCrawlerService {
    Map<RankingRuleVO, List<VideoRecordVO>> achieveRecords(List<RankingRuleVO> rankingRules) throws Exception;

    void stop();
}
