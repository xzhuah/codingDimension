package nodes.datascienceNode.stockInfo;

import com.google.gson.JsonArray;

/**
 * Created by Xinyu Zhu on 2020/11/21, 20:08
 * nodes.datascienceNode.stockInfo in codingDimensionTemplate
 */
public interface StockPriceInfoJsonify {
    JsonArray outputNdayData(int n);
}
