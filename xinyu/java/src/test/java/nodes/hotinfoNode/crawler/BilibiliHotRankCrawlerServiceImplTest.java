package nodes.hotinfoNode.crawler;

import nodes.NodeModule;
import nodes.hotinfoNode.models.RankingRuleVO;
import nodes.hotinfoNode.models.VideoRecordVO;
import nodes.hotinfoNode.utils.EnumUtils;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by Xinyu Zhu on 2020/11/4, 21:13
 * nodes.hotinfoNode.crawler.impls in AllInOne
 */
public class BilibiliHotRankCrawlerServiceImplTest {
    BilibiliHotRankCrawlerService bilibiliHotRankCrawlerService = NodeModule.getGlobalInjector().getInstance(BilibiliHotRankCrawlerService.class);

    @Test
    public void test() throws Exception {
        Map<RankingRuleVO, List<VideoRecordVO>> result = bilibiliHotRankCrawlerService.achieveRecords(
                EnumUtils.getAllPossibleRankingRule().subList(0, 1)
        );
        System.out.println(result);

        bilibiliHotRankCrawlerService.stop();
    }
}