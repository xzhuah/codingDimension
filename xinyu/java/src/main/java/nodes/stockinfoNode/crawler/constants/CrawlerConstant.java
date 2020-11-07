package nodes.stockinfoNode.crawler.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Xinyu Zhu on 2020/11/6, 23:54
 * nodes.stockinfoNode.crawler.constants in codingDimensionTemplate
 */
public class CrawlerConstant {

    public static final Map<String, String> DEFAULT_HEADER = new HashMap<>() {
        {
            put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
        }
    };
    public static final String ALPHAVANTAGE_API_ENDPOINT = "https://www.alphavantage.co/query";
}
