package nodes.crawlerNode.impl;

import com.google.inject.Inject;
import com.google.inject.Key;
import common.io.web.facade.ResponseProcessor;
import nodes.NodeModule;
import nodes.crawlerNode.BaseCrawler;
import nodes.crawlerNode.models.WebpageCommonInfo;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Created by Xinyu Zhu on 2020/11/7, 23:52
 * nodes.crawlerNode.impl in codingDimensionTemplate
 */
public class WebpageCommonInfoCrawler extends BaseCrawler<WebpageCommonInfo> {

    @Inject
    public WebpageCommonInfoCrawler(ResponseProcessor<WebpageCommonInfo> webpageCommonProcessor) {
        super(webpageCommonProcessor);
    }

    public static void main(String[] args) throws Exception {

        BaseCrawler<WebpageCommonInfo> WebpageCommonInfoCrawler = NodeModule.getGlobalInjector().getInstance(new Key<>() {
        });

        String url = "https://www.linkedin.com/school/stanford-university/people/";
        WebpageCommonInfoCrawler.addJobToQueue(url);
        List<Future<Optional<WebpageCommonInfo>>> resultFuture = WebpageCommonInfoCrawler.getResultFuture(url, null);
        WebpageCommonInfo webpageCommonInfo;
        if (!resultFuture.get(0).get().isPresent()) {
            System.out.println("Failed to fetch the result");
            WebpageCommonInfoCrawler.shutDown();
        }
        webpageCommonInfo = resultFuture.get(0).get().get();
        System.out.println(webpageCommonInfo.getWebpageContent());
        System.out.println(webpageCommonInfo.getWebpageLinks());
        System.out.println(webpageCommonInfo.getWebpageTitles());

        WebpageCommonInfoCrawler.shutDown();
    }
}
