package nodes.crawlerNode.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/7, 11:45
 * nodes.crawlerNode.models in codingDimensionTemplate
 */
@Data
@NoArgsConstructor
public class WebpageCommonInfo {
    // The url to this page
    private String webPageUrl;

    // All text in header tag, <h1> <h2>...
    private List<String> webpageTitles;

    // All urls in absolute path format
    private List<String> webpageLinks;

    // All visible plain text
    private String webpageContent;
}
