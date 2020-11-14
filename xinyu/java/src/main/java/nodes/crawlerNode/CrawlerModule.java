package nodes.crawlerNode;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import common.io.web.facade.ResponseProcessor;
import nodes.crawlerNode.facade.impl.WebpageCommonProcessor;
import nodes.crawlerNode.impl.WebpageCommonInfoCrawler;
import nodes.crawlerNode.models.WebpageCommonInfo;

/**
 * Created by Xinyu Zhu on 2020/11/11, 1:19
 * nodes.crawlerNode in codingDimensionTemplate
 */
public class CrawlerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<ResponseProcessor<WebpageCommonInfo>>() {
        }).to(new TypeLiteral<WebpageCommonProcessor>() {
        });

        bind(new TypeLiteral<BaseCrawler<WebpageCommonInfo>>() {
        }).to(new TypeLiteral<WebpageCommonInfoCrawler>() {
        });
    }
}
