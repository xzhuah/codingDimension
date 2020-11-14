package nodes.hotinfoNode;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import common.io.web.facade.ResponseProcessor;
import nodes.crawlerNode.AutoCoolDownCrawler;
import nodes.hotinfoNode.constants.ValueConstant;
import nodes.hotinfoNode.crawler.BilibiliHotRankCrawlerService;
import nodes.hotinfoNode.crawler.facade.impl.ResponseToRankListProcessor;
import nodes.hotinfoNode.crawler.impls.BilibiliHotRankCrawlerServiceImpl;
import nodes.hotinfoNode.impls.BilibiliInfoManagerImpl;
import nodes.hotinfoNode.models.VideoRecordVO;

import java.util.List;

public class HotInfoModule extends AbstractModule {

    @Override
    protected void configure() {
        ResponseToRankListProcessor responseToRankListProcessor = new ResponseToRankListProcessor();

        bind(new TypeLiteral<ResponseProcessor<List<VideoRecordVO>>>() {
        }).toInstance(responseToRankListProcessor);

        bind(new TypeLiteral<AutoCoolDownCrawler<List<VideoRecordVO>>>() {
        }).toInstance(new AutoCoolDownCrawler<>(responseToRankListProcessor, ValueConstant.CrawlerParameter.MINMUM_WAIT_TIME.getValue(), ValueConstant.CrawlerParameter.MAXIMUM_WAIT_TIME.getValue(), 0));

        bind(BilibiliHotRankCrawlerService.class).to(BilibiliHotRankCrawlerServiceImpl.class);

        bind(BilibiliInfoManager.class).to(BilibiliInfoManagerImpl.class);
    }
}