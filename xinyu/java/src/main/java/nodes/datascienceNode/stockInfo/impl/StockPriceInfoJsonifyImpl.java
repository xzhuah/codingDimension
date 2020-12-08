package nodes.datascienceNode.stockInfo.impl;

import com.google.gson.JsonArray;
import com.google.inject.Inject;
import common.io.file.PlaintextClient;
import common.time.TimeInterval;
import nodes.NodeModule;
import nodes.datascienceNode.stockInfo.StockPriceInfoJsonify;
import nodes.datascienceNode.stockInfo.constants.Config;
import nodes.datascienceNode.stockInfo.facade.impl.StockPriceFeatureSet;
import nodes.stockinfoNode.StockInfoService;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/21, 20:00
 * nodes.datascienceNode.stockInfo.impl in codingDimensionTemplate
 */
public class StockPriceInfoJsonifyImpl implements StockPriceInfoJsonify {
    private final StockInfoService stockInfoService;

    private StockPriceFeatureSet stockPriceFeatureSet;

    @Inject
    public StockPriceInfoJsonifyImpl(StockInfoService stockInfoService) {
        this.stockInfoService = stockInfoService;
        this.stockInfoService.setAutoUpdate(Config.AUTO_UPDATE);

        stockPriceFeatureSet = new StockPriceFeatureSet();
    }


    /**
     * 为股市最近n天的数据计算stockPriceFeatureSet中定义的feature的值
     * 并以JsonArray的形式连同股票原始数据返回, 便于Tableau等可视化软件进行分析
     *
     * @param n
     * @return
     */
    @Override
    public JsonArray outputNdayData(int n) {
        List<String> allSymbols = stockInfoService.getAllSymbols();
        JsonArray result = new JsonArray();
        for (String symbol : allSymbols) {
            List<StockDailyRecordPOJO> sortedPrice = stockInfoService.getSortedPriceForSymbol(symbol, TimeInterval.getUpToNowIntervalWithDuration((long) n * 24L * 3600L * 1000L)).getAllModel(StockDailyRecordPOJO.class).get();
            JsonArray jsonArray = stockPriceFeatureSet.extractForInstance(sortedPrice);
            result.add(jsonArray);
        }
        return result;
    }

    public static void main(String[] args) {
        StockPriceInfoJsonify stockPriceInfoJsonify = NodeModule.getGlobalInjector().getInstance(StockPriceInfoJsonify.class);
        JsonArray result = stockPriceInfoJsonify.outputNdayData(500);
        PlaintextClient.write("500days.json", result.toString());
    }
}
