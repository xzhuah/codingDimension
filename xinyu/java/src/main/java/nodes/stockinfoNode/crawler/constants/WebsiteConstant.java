package nodes.stockinfoNode.crawler.constants;

import common.io.file.PlaintextClient;

import java.io.IOException;

/**
 * Created by Xinyu Zhu on 2020/11/6, 23:54
 * nodes.stockinfoNode.crawler.constants in codingDimensionTemplate
 */
public class WebsiteConstant {

    public static final String ALPHAVANTAGE_API_ENDPOINT = "https://www.alphavantage.co/query";

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
