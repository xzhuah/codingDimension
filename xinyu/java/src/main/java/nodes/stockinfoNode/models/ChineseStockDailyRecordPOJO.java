package nodes.stockinfoNode.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by Xinyu Zhu on 2020/12/2, 23:31
 * nodes.stockinfoNode.models in codingDimensionTemplate
 */
@Data
@NoArgsConstructor
public class ChineseStockDailyRecordPOJO extends StockDailyRecordPOJO {

    // These two are primary key
    private String tsCode;

    // 成交额 （千元）
    @EqualsAndHashCode.Exclude
    private double amount;

    @Override
    public void setSplitCoefficient(double splitCoefficient) {
        throw new RuntimeException("Field Not Support in ChineseStockDailyRecordPOJO");
    }

    @Override
    public void setDividend(double dividend) {
        throw new RuntimeException("Field Not Support in ChineseStockDailyRecordPOJO");
    }

    @Override
    public void setAdjustedClose(double adjustedClose) {
        throw new RuntimeException("Field Not Support in ChineseStockDailyRecordPOJO");
    }

    @Override
    public double getSplitCoefficient() {
        throw new RuntimeException("Field Not Support in ChineseStockDailyRecordPOJO");
    }

    @Override
    public double getDividend() {
        throw new RuntimeException("Field Not Support in ChineseStockDailyRecordPOJO");
    }

    @Override
    public double getAdjustedClose() {
        throw new RuntimeException("Field Not Support in ChineseStockDailyRecordPOJO");
    }

    @Override
    public String getSymbol() {
        return tsCode;
    }

    @Override
    public void setSymbol(String symbol) {
        this.tsCode = symbol;
    }
}
