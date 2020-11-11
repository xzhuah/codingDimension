package nodes.hotinfoNode;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import common.io.web.ResponseProcessor;
import nodes.crawlerNode.BaseCrawler;
import nodes.hotinfoNode.crawler.BilibiliHotRankCrawlerService;
import nodes.hotinfoNode.crawler.facade.ResponseToRankListProcessor;
import nodes.hotinfoNode.crawler.impls.BilibiliHotRankCrawlerServiceImpl;
import nodes.hotinfoNode.models.VideoRecordVO;

import java.util.List;

public class HotInfoModule extends AbstractModule {

    @Override
    protected void configure() {
        ResponseToRankListProcessor responseToRankListProcessor = new ResponseToRankListProcessor();

        bind(new TypeLiteral<ResponseProcessor<List<VideoRecordVO>>>() {
        }).toInstance(responseToRankListProcessor);

        bind(new TypeLiteral<BaseCrawler<List<VideoRecordVO>>>() {
        }).toInstance(new BaseCrawler<>(responseToRankListProcessor));

        bind(BilibiliHotRankCrawlerService.class).to(BilibiliHotRankCrawlerServiceImpl.class);


    }
}