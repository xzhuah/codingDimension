package nodes.crawlerNode.models;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/7, 11:45
 * nodes.crawlerNode.models in codingDimensionTemplate
 */
public class WebpageCommonInfo {
    // The url to this page
    private String webPageUrl;

    // All text in header tag, <h1> <h2>...
    private List<String> webpageTitles;

    // All urls in absolute path format
    private List<String> webpageLinks;

    // All visible plain text
    private String webpageContent;

    public WebpageCommonInfo() {

    }

    public String getWebPageUrl() {
        return webPageUrl;
    }

    public void setWebPageUrl(String webPageUrl) {
        this.webPageUrl = webPageUrl;
    }

    public List<String> getWebpageTitles() {
        return webpageTitles;
    }

    public void setWebpageTitles(List<String> webpageTitles) {
        this.webpageTitles = webpageTitles;
    }

    public List<String> getWebpageLinks() {
        return webpageLinks;
    }

    public void setWebpageLinks(List<String> webpageLinks) {
        this.webpageLinks = webpageLinks;
    }

    public String getWebpageContent() {
        return webpageContent;
    }

    public void setWebpageContent(String webpageContent) {
        this.webpageContent = webpageContent;
    }
}
