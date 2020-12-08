package nodes.datascienceNode.stockInfo.impl;

import com.google.inject.Inject;
import common.time.TimeInterval;
import nodes.NodeModule;
import nodes.datascienceNode.stockInfo.StockFilterService;
import nodes.datascienceNode.stockInfo.facade.impl.DaysOverFeature;
import nodes.datascienceNode.stockInfo.facade.impl.StockNdayAvgPriceSlopeFeature;
import nodes.stockinfoNode.StockInfoService;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/29, 0:44
 * nodes.datascienceNode.stockInfo.impl in codingDimensionTemplate
 * <p>
 * 用于寻找满足特定要求的股票
 */
public class StockFilterServiceImpl implements StockFilterService {

    private final StockInfoService stockInfoService;

    @Inject
    public StockFilterServiceImpl(StockInfoService stockInfoService) {
        this.stockInfoService = stockInfoService;
    }

    // 寻找具备5日均线向上穿过10日均线形成金叉趋势的股票
    // 1. 5日均线在10日均线下方至少n日 -->
    // 2. 最近m日5日均线斜率大于0且大于10日均线斜率
    public List<String> filterGoldenCrossStock(int n, int m) {
        DaysOverFeature daysOverFeature = new DaysOverFeature(5, 10);
        StockNdayAvgPriceSlopeFeature fiveDaySlope = new StockNdayAvgPriceSlopeFeature(5);
        StockNdayAvgPriceSlopeFeature tenDaySlope = new StockNdayAvgPriceSlopeFeature(10);

        List<String> allSymbols = stockInfoService.getAllSymbols();
        List<String> goldenCrossStock = new ArrayList<>();

        long duration = (Math.max(n, m) + 11L) * 2L * 24L * 3600L * 1000L;
        for (String symbol : allSymbols) {
            List<StockDailyRecordPOJO> sortedPrice = stockInfoService.getSortedPriceForSymbol(symbol, TimeInterval.getUpToNowIntervalWithDuration(duration)).getAllModel(StockDailyRecordPOJO.class).get();
            int daysOver = daysOverFeature.extractForInstance(sortedPrice);
            if (daysOver < n) {
                continue;
            }
            List<Double> fiveDaySlopeData = fiveDaySlope.extractForInstance(sortedPrice);

            boolean fiveDaySlopeWithMDayIsPositive = true;
            for (int i = fiveDaySlopeData.size() - 1; i >= fiveDaySlopeData.size() - m; i--) {
                if (fiveDaySlopeData.get(i) <= 0) {
                    fiveDaySlopeWithMDayIsPositive = false;
                    break;
                }
            }
            if (!fiveDaySlopeWithMDayIsPositive) {
                continue;
            }

            boolean fiveDaySlopeWithMDayIsLargerThenTenDays = true;
            List<Double> tenDaySlopeData = tenDaySlope.extractForInstance(sortedPrice);

            for (int i = fiveDaySlopeData.size() - 1; i >= fiveDaySlopeData.size() - m; i--) {
                if (fiveDaySlopeData.get(i) <= tenDaySlopeData.get(i)) {
                    fiveDaySlopeWithMDayIsLargerThenTenDays = false;
                    break;
                }
            }

            if (!fiveDaySlopeWithMDayIsLargerThenTenDays) {
                continue;
            }
            goldenCrossStock.add(symbol);

        }

        return goldenCrossStock;

    }

    public static void main(String[] args) {
        StockFilterService stockFilterService = NodeModule.getGlobalInjector().getInstance(StockFilterService.class);
        List<String> symbols = stockFilterService.filterGoldenCrossStock(3, 2);
        System.out.println(symbols);
    }
}
