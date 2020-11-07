package nodes.hotinfoNode.crawler.impls;


import common.io.web.models.ResponseProcessResult;
import nodes.crawlerNode.BaseCrawler;
import nodes.crawlerNode.constants.CrawlerConstant;
import nodes.hotinfoNode.crawler.BilibiliHotRankCrawlerService;
import nodes.hotinfoNode.crawler.facade.ResponseToRankListProcessor;
import nodes.hotinfoNode.models.RankingRuleVO;
import nodes.hotinfoNode.models.VideoRecordListVO;
import nodes.hotinfoNode.models.VideoRecordVO;
import nodes.hotinfoNode.utils.EnumUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static common.utils.ConditionChecker.checkStatus;

/**
 * Created by Xinyu Zhu on 7/1/2020, 12:08 PM
 * xinyu.hotinfo.biz.bilibili.impls in HotInfo
 */
public class BilibiliHotRankCrawlerServiceImpl implements BilibiliHotRankCrawlerService {
    private final AtomicInteger counter;
    private BaseCrawler crawler;

    public BilibiliHotRankCrawlerServiceImpl() {
        crawler = new BaseCrawler(ResponseToRankListProcessor.getInstance());
        counter = new AtomicInteger(0);
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
        List<Future<ResponseProcessResult>> rawResult = crawler.getResultFuture(tag);

        // ensure two size are equal
        checkStatus(rawResult.size() == rankingRules.size(), "REQUEST_RESPONSE_MISMATCH_EXCEPTION");

        for (int i = 0; i < rawResult.size(); i++) {
            Future<ResponseProcessResult> future = rawResult.get(i);
            VideoRecordListVO records = null;
            try {
                records = (VideoRecordListVO) future.get();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Failed for Request: " + rankingRules + " keep processing remaining");
                continue;
            }

            result.put(rankingRules.get(i), records.getVideoRecords());
        }

        return result;
    }

    @Override
    public void stop() {
        crawler.shutDown();
    }

    public static void main(String[] args) throws Exception {
        BilibiliHotRankCrawlerService bilibiliHotRankCrawlerService = new BilibiliHotRankCrawlerServiceImpl();
        Map<RankingRuleVO, List<VideoRecordVO>> result = bilibiliHotRankCrawlerService.achieveRecords(
                EnumUtils.getAllPossibleRankingRule().subList(0, 1)
        );
        System.out.println(result);

        bilibiliHotRankCrawlerService.stop();
    }
}
