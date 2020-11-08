package nodes.crawlerNode.utils;

import nodes.crawlerNode.models.WebpageCommonInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/7, 23:22
 * nodes.crawlerNode.utils in codingDimensionTemplate
 */
public class Converter {

    public static WebpageCommonInfo toWebpageCommonInfo(Document document, String url) {
        // In order to get absolute url, we need to set Base uri, tell the document the base uri
        document.setBaseUri(url);
        WebpageCommonInfo webpageCommonInfo = new WebpageCommonInfo();

        webpageCommonInfo.setWebPageUrl(url);
        webpageCommonInfo.setWebpageContent(document.text());
        List<String> allTitle = new ArrayList<>();
        List<String> allUrl = new ArrayList<>();

        allTitle.add(document.title());
        Elements hTags = document.select("h1, h2, h3, h4, h5, h6");
        hTags.forEach(htag -> allTitle.add(htag.text()));

        Elements links = document.select("a[href]");
        Elements media = document.select("[src]");
        Elements imports = document.select("link[href]");

        links.forEach(link -> allUrl.add(link.attr("abs:href")));
        media.forEach(link -> allUrl.add(link.attr("abs:src")));
        imports.forEach(link -> allUrl.add(link.attr("abs:href")));

        webpageCommonInfo.setWebpageTitles(allTitle);
        webpageCommonInfo.setWebpageLinks(allUrl);
        return webpageCommonInfo;
    }
}
