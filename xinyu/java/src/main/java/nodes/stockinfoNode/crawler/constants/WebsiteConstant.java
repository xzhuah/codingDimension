package nodes.stockinfoNode.crawler.constants;

import common.io.file.PlaintextClient;

import java.io.IOException;

/**
 * Created by Xinyu Zhu on 2020/11/6, 23:54
 * nodes.stockinfoNode.crawler.constants in codingDimensionTemplate
 */
public class WebsiteConstant {

    public static final String ALPHAVANTAGE_API_ENDPOINT = "https://www.alphavantage.co/query";

    public static final String SYMBOL_QUERY_ENDPOINT = "https://api.iextrading.com/1.0/ref-data/symbols";

    public static final long COOL_DOWN_TIME = 15000;

    private static final String keyFile = "stockinfoKey.pass";

    public static String loadKey;

    static {
        try {
            loadKey = PlaintextClient.readFile(keyFile);
        } catch (
                IOException e) {
            loadKey = "demo";
        }
        loadKey = loadKey.trim();
    }
}
