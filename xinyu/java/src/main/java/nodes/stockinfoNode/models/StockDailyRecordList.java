package nodes.stockinfoNode.models;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/7, 0:20
 * nodes.stockinfoNode.crawler.facade in codingDimensionTemplate
 */
public class StockDailyRecordList {
    private List<StockDailyRecordPOJO> stockDailyRecordPOJOList;

    public StockDailyRecordList() {
        this.stockDailyRecordPOJOList = new ArrayList<>();
    }

    public StockDailyRecordList(List<StockDailyRecordPOJO> stockDailyRecordPOJOList) {
        this.stockDailyRecordPOJOList = stockDailyRecordPOJOList;
    }

    public List<StockDailyRecordPOJO> getStockDailyRecordPOJOList() {
        return stockDailyRecordPOJOList;
    }

    public void setStockDailyRecordPOJOList(List<StockDailyRecordPOJO> stockDailyRecordPOJOList) {
        this.stockDailyRecordPOJOList = stockDailyRecordPOJOList;
    }
}
