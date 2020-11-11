package nodes.hotinfoNode.crawler.impls;

import com.google.inject.Inject;
import nodes.NodeModule;
import nodes.crawlerNode.BaseCrawler;
import nodes.crawlerNode.constants.CrawlerConstant;
import nodes.hotinfoNode.crawler.BilibiliHotRankCrawlerService;
import nodes.hotinfoNode.crawler.facade.ResponseToRankListProcessor;
import nodes.hotinfoNode.models.RankingRuleVO;
import nodes.hotinfoNode.models.VideoRecordVO;
import nodes.hotinfoNode.utils.EnumUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static common.utils.ConditionChecker.checkStatus;

/**
 * Created by Xinyu Zhu on 7/1/2020, 12:08 PM
 * xinyu.hotinfo.biz.bilibili.impls in HotInfo
 */
public class BilibiliHotRankCrawlerServiceImpl implements BilibiliHotRankCrawlerService {
    private final AtomicInteger counter;
    private final BaseCrawler<List<VideoRecordVO>> crawler;

    @Inject
    public BilibiliHotRankCrawlerServiceImpl(BaseCrawler<List<VideoRecordVO>> crawler) {
        this.crawler = crawler;
        this.counter = new AtomicInteger(0);
    }

    public static void main(String[] args) throws Exception {
        BilibiliHotRankCrawlerService bilibiliHotRankCrawlerService = NodeModule.getGlobalInjector().getInstance(BilibiliHotRankCrawlerService.class);

        // BilibiliHotRankCrawlerService bilibiliHotRankCrawlerService = new BilibiliHotRankCrawlerServiceImpl();
        Map<RankingRuleVO, List<VideoRecordVO>> result = bilibiliHotRankCrawlerService.achieveRecords(
                EnumUtils.getAllPossibleRankingRule().subList(0, 1)
        );
        System.out.println(result);

        bilibiliHotRankCrawlerService.stop();
    }

    @Override
    public Map<RankingRuleVO, List<VideoRecordVO>> achieveRecords(List<RankingRuleVO> rankingRules) throws Exception {
        String tag = String.valueOf(counter.getAndAdd(1));
        Map<RankingRuleVO, List<VideoRecordVO>> result = new HashMap<>();

        for (RankingRuleVO rankingRuleVO : rankingRules) {
            try {
                crawler.addJobToQueue(tag, rankingRuleVO.getUrl(), null, CrawlerConstant.DEFAULT_HEADER);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        List<Future<Optional<List<VideoRecordVO>>>> rawResult = crawler.getResultFuture(tag);

        // ensure two size are equal
        checkStatus(rawResult.size() == rankingRules.size(), "REQUEST_RESPONSE_MISMATCH_EXCEPTION");

        for (int i = 0; i < rawResult.size(); i++) {
            Future<Optional<List<VideoRecordVO>>> future = rawResult.get(i);
            List<VideoRecordVO> records = null;
            try {
                records = future.get().get();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Failed for Request: " + rankingRules + " keep processing remaining");
                continue;
            }

            result.put(rankingRules.get(i), records);
        }

        return result;
    }

    @Override
    public void stop() {
        crawler.shutDown();
    }
}
