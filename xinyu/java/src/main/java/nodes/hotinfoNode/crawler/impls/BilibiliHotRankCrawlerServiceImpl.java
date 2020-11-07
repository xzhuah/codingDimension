package nodes.hotinfoNode.crawler.impls;


import common.io.web.PoolingAsyncHttpClient;
import common.io.web.impl.PoolingAsyncHttpClientImpl;
import common.io.web.models.ResponseProcessResult;
import nodes.hotinfoNode.crawler.BilibiliHotRankCrawlerService;
import nodes.hotinfoNode.crawler.facade.ResponseToRankListProcessor;
import nodes.hotinfoNode.models.RankingRuleVO;
import nodes.hotinfoNode.models.VideoRecordListVO;
import nodes.hotinfoNode.models.VideoRecordVO;
import nodes.hotinfoNode.utils.Converter;
import nodes.hotinfoNode.utils.EnumUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Xinyu Zhu on 7/1/2020, 12:08 PM
 * xinyu.hotinfo.biz.bilibili.impls in HotInfo
 */
public class BilibiliHotRankCrawlerServiceImpl implements BilibiliHotRankCrawlerService {
    private PoolingAsyncHttpClient poolingAsyncHttpClient;
    private AtomicInteger counter;

    public BilibiliHotRankCrawlerServiceImpl() {
        poolingAsyncHttpClient = new PoolingAsyncHttpClientImpl(ResponseToRankListProcessor.getInstance());
        counter = new AtomicInteger(0);
    }

    @Override
    public Map<RankingRuleVO, List<VideoRecordVO>> achieveRecords(List<RankingRuleVO> rankingRules) throws Exception {
        String tag = String.valueOf(counter.getAndAdd(1));
        Map<RankingRuleVO, List<VideoRecordVO>> result = new HashMap<>();

        for (RankingRuleVO rankingRuleVO : rankingRules) {
            try {
                poolingAsyncHttpClient.addRequestToPool(tag, Converter.toGetHttpUriRequest(rankingRuleVO.getUrl()));
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        List<Future<ResponseProcessResult>> rawResult = poolingAsyncHttpClient.startProcessing(tag);

        // ensure two size are equal
        if (rawResult.size() != rankingRules.size()) {
            throw new Exception("REQUEST_RESPONSE_MISMATCH_EXCEPTION");
        }


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
        poolingAsyncHttpClient.finish();
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
