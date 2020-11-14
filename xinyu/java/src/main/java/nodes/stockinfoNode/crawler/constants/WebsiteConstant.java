package nodes.stockinfoNode.crawler.constants;

import common.io.file.PlaintextClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/6, 23:54
 * nodes.stockinfoNode.crawler.constants in codingDimensionTemplate
 */
public class WebsiteConstant {

    public static final String ALPHAVANTAGE_API_ENDPOINT = "https://www.alphavantage.co/query";

    public static final String SYMBOL_QUERY_ENDPOINT = "https://api.iextrading.com/1.0/ref-data/symbols";

    public static final long COOL_DOWN_TIME = 15000;

    private static final String keyFile = "stockinfoKey.pass";
    private static final String stockFile = "SP500.pass";

    public static String LOAD_KEY;
    public static List<String> SP_500_SYMBOL = new ArrayList<>(520);

    static {
        try {
            LOAD_KEY = PlaintextClient.readFile(keyFile);
        } catch (IOException e) {
            LOAD_KEY = "demo";
        }
        LOAD_KEY = LOAD_KEY.trim();
    }

    static {
        try {
            String stocks = PlaintextClient.readFile(stockFile);
            Collections.addAll(SP_500_SYMBOL, stocks.split(","));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
