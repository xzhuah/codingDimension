package nodes.datascienceNode.stockInfo.utils;

import nodes.datascienceNode.stockInfo.models.StockCandleStickModel;
import nodes.stockinfoNode.StockInfoModelProvider;
import nodes.stockinfoNode.models.ChineseStockDailyRecordPOJO;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Xinyu Zhu on 2020/11/15, 1:13
 * nodes.datascienceNode.stockInfo.utils in codingDimensionTemplate
 */
public class Converter {
    public static List<Double> toDailyAvgPriceList(List<StockDailyRecordPOJO> sortedDailyPriceRecord) {
        List<Double> dailyAvgPriceList = new ArrayList<>(sortedDailyPriceRecord.size());
        sortedDailyPriceRecord.forEach(dailyRecordPOJO -> dailyAvgPriceList.add(toAvgPrice(dailyRecordPOJO)));
        return dailyAvgPriceList;
    }

    public static double toAvgPrice(StockDailyRecordPOJO dailyRecordPOJO) {
        return (dailyRecordPOJO.getOpen() + dailyRecordPOJO.getHigh() + dailyRecordPOJO.getLow() + dailyRecordPOJO.getClose()) / 4.0;
    }

    public static Optional<StockCandleStickModel> toCandleStickModel(StockInfoModelProvider stockInfoModelProvider) {
        if (stockInfoModelProvider.hasModel(StockDailyRecordPOJO.class)) {
            Optional<StockDailyRecordPOJO> dailyRecordPOJO = stockInfoModelProvider.getModel(StockDailyRecordPOJO.class);
            if (dailyRecordPOJO.isPresent()) {
                StockCandleStickModel stockCandleStickModel = new StockCandleStickModel();
                stockCandleStickModel.setOpen(dailyRecordPOJO.get().getOpen());
                stockCandleStickModel.setHigh(dailyRecordPOJO.get().getHigh());
                stockCandleStickModel.setLow(dailyRecordPOJO.get().getLow());
                stockCandleStickModel.setClose(dailyRecordPOJO.get().getClose());
                return Optional.of(stockCandleStickModel);
            } else {
                return Optional.empty();
            }
        } else if (stockInfoModelProvider.hasModel(ChineseStockDailyRecordPOJO.class)) {
            Optional<ChineseStockDailyRecordPOJO> dailyRecordPOJO = stockInfoModelProvider.getModel(ChineseStockDailyRecordPOJO.class);
            if (dailyRecordPOJO.isPresent()) {
                StockCandleStickModel stockCandleStickModel = new StockCandleStickModel();
                stockCandleStickModel.setOpen(dailyRecordPOJO.get().getOpen());
                stockCandleStickModel.setHigh(dailyRecordPOJO.get().getHigh());
                stockCandleStickModel.setLow(dailyRecordPOJO.get().getLow());
                stockCandleStickModel.setClose(dailyRecordPOJO.get().getClose());
                return Optional.of(stockCandleStickModel);
            } else {
                return Optional.empty();
            }

        } else {
            return Optional.empty();
        }
    }

    public static Optional<List<StockCandleStickModel>> toCandleStickModelList(StockInfoModelProvider stockInfoModelProvider) {
        if (stockInfoModelProvider.hasModel(StockDailyRecordPOJO.class)) {
            Optional<List<StockDailyRecordPOJO>> dailyRecordPOJO = stockInfoModelProvider.getAllModel(StockDailyRecordPOJO.class);
            if (dailyRecordPOJO.isPresent()) {
                List<StockCandleStickModel> result = new ArrayList<>(dailyRecordPOJO.get().size());
                for (StockDailyRecordPOJO stockDailyRecordPOJO : dailyRecordPOJO.get()) {
                    StockCandleStickModel stockCandleStickModel = new StockCandleStickModel();
                    stockCandleStickModel.setOpen(stockDailyRecordPOJO.getOpen());
                    stockCandleStickModel.setHigh(stockDailyRecordPOJO.getHigh());
                    stockCandleStickModel.setLow(stockDailyRecordPOJO.getLow());
                    stockCandleStickModel.setClose(stockDailyRecordPOJO.getClose());
                    result.add(stockCandleStickModel);
                }
                return Optional.of(result);
            } else {
                return Optional.empty();
            }
        } else if (stockInfoModelProvider.hasModel(ChineseStockDailyRecordPOJO.class)) {
            Optional<List<ChineseStockDailyRecordPOJO>> dailyRecordPOJO = stockInfoModelProvider.getAllModel(ChineseStockDailyRecordPOJO.class);
            if (dailyRecordPOJO.isPresent()) {
                List<StockCandleStickModel> result = new ArrayList<>(dailyRecordPOJO.get().size());
                for (ChineseStockDailyRecordPOJO stockDailyRecordPOJO : dailyRecordPOJO.get()) {
                    StockCandleStickModel stockCandleStickModel = new StockCandleStickModel();
                    stockCandleStickModel.setOpen(stockDailyRecordPOJO.getOpen());
                    stockCandleStickModel.setHigh(stockDailyRecordPOJO.getHigh());
                    stockCandleStickModel.setLow(stockDailyRecordPOJO.getLow());
                    stockCandleStickModel.setClose(stockDailyRecordPOJO.getClose());
                    result.add(stockCandleStickModel);
                }
                return Optional.of(result);
            } else {
                return Optional.empty();
            }

        } else {
            return Optional.empty();
        }

    }
}
