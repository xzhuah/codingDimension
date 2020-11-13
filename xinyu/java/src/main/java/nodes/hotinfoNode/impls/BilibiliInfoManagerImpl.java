package nodes.hotinfoNode.impls;

import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import common.io.database.mongodb.MongoDBPojoClient;
import nodes.hotinfoNode.BilibiliInfoManager;
import nodes.hotinfoNode.constants.ValueConstant;
import nodes.hotinfoNode.crawler.BilibiliHotRankCrawlerService;
import nodes.hotinfoNode.models.BilibiliAnalyzableVideoRecord;
import nodes.hotinfoNode.models.RankingRuleVO;
import nodes.hotinfoNode.models.VideoRecordVO;
import nodes.hotinfoNode.utils.Converter;
import nodes.hotinfoNode.utils.EnumUtils;

import java.util.List;
import java.util.Map;

public class BilibiliInfoManagerImpl implements BilibiliInfoManager {
    private final BilibiliHotRankCrawlerService bilibiliHotRankCrawlerService;
    private final MongoDBPojoClient mongoDBPojoClient;


    private final MongoCollection<BilibiliAnalyzableVideoRecord> mongoCollection;

    @Inject
    public BilibiliInfoManagerImpl(BilibiliHotRankCrawlerService bilibiliHotRankCrawlerService, MongoDBPojoClient mongoDBPojoClient) {
        this.bilibiliHotRankCrawlerService = bilibiliHotRankCrawlerService;
        this.mongoDBPojoClient = mongoDBPojoClient;

        this.mongoCollection = mongoDBPojoClient.getCollection(ValueConstant.Path.DATABASE_NAME.getValue(),
                ValueConstant.Path.COLLECTION_NAME.getValue(), BilibiliAnalyzableVideoRecord.class);
    }

    public void collectLatestHotRanking() {
        List<RankingRuleVO> allRankingRules = EnumUtils.getAllPossibleRankingRule();

        long collectTime = System.currentTimeMillis();
        int start = 0;

        while (start < allRankingRules.size()) {
            int nextRequestNum = EnumUtils.getRandomCrawlerProcessRequest();
            int end = Math.min(start + nextRequestNum, allRankingRules.size());

            Map<RankingRuleVO, List<VideoRecordVO>> allResult;

            try {
                allResult = bilibiliHotRankCrawlerService.achieveRecords(allRankingRules.subList(start, end));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            List<BilibiliAnalyzableVideoRecord> recordList = Converter.toBilibiliAnalyzableVideoRecordList(allResult, collectTime);

            // insertMany will stopped when encounter duplication, set insertOption orderd false doesn't work
            for (BilibiliAnalyzableVideoRecord bilibiliAnalyzableVideoRecord : recordList) {
                try {
                    mongoCollection.insertOne(bilibiliAnalyzableVideoRecord);
                } catch (Exception duplicateException) {
                    try {
                        // update duplication
                        mongoCollection.findOneAndDelete(Converter.toPrimaryFilter(bilibiliAnalyzableVideoRecord));
                        mongoCollection.insertOne(bilibiliAnalyzableVideoRecord);
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }

                }
            }

            start = end;

            System.out.println("Processed: " + end + "/" + allRankingRules.size() + " Time elapsed: " + (System.currentTimeMillis() - collectTime) / 1000 + " seconds");
        }


    }

    public void exit() {
        bilibiliHotRankCrawlerService.stop();
        mongoDBPojoClient.close();
    }
}