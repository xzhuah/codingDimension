package nodes.crawlerNode.impl;

import common.io.web.models.ResponseProcessResult;
import nodes.crawlerNode.BaseCrawler;
import nodes.crawlerNode.facade.WebpageCommonProcessor;
import nodes.crawlerNode.models.WebpageCommonInfo;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by Xinyu Zhu on 2020/11/7, 23:52
 * nodes.crawlerNode.impl in codingDimensionTemplate
 */
public class WebpageCommonInfoCrawler extends BaseCrawler {
    public WebpageCommonInfoCrawler() {
        super(WebpageCommonProcessor.getInstance());
    }

    public static void main(String[] args) throws Exception {
        WebpageCommonInfoCrawler WebpageCommonInfoCrawler = new WebpageCommonInfoCrawler();
        String url = "https://www.bilibili.com/";
        WebpageCommonInfoCrawler.addJobToQueue(url);
        List<Future<ResponseProcessResult>> resultFuture = WebpageCommonInfoCrawler.getResultFuture(url, null);
        WebpageCommonInfo webpageCommonInfo = (WebpageCommonInfo) resultFuture.get(0).get();

        System.out.println(webpageCommonInfo.getWebpageContent());
        System.out.println(webpageCommonInfo.getWebpageLinks());
        System.out.println(webpageCommonInfo.getWebpageTitles());

        WebpageCommonInfoCrawler.shutDown();
    }
}
