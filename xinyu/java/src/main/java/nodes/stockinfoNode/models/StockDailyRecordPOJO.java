package nodes.stockinfoNode.models;

import org.bson.types.ObjectId;

/**
 * Created by Xinyu Zhu on 2020/11/6, 23:36
 * nodes.stockinfoNode.models in codingDimensionTemplate
 */
public class StockDailyRecordPOJO {

    // This is POJO indicator: real primary key
    private ObjectId id;

    // These two are primary key
    private String stockSymbol;
    private long time;

    // These are other data
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;
    private double adjustedClose;
    private double dividend;
    private double splitCoefficient;

    public StockDailyRecordPOJO() {

    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getAdjustedClose() {
        return adjustedClose;
    }

    public void setAdjustedClose(double adjustedClose) {
        this.adjustedClose = adjustedClose;
    }

    public double getDividend() {
        return dividend;
    }

    public void setDividend(double dividend) {
        this.dividend = dividend;
    }

    public double getSplitCoefficient() {
        return splitCoefficient;
    }

    public void setSplitCoefficient(double splitCoefficient) {
        this.splitCoefficient = splitCoefficient;
    }

    @Override
    public String toString() {
        return "StockDailyRecordPOJO{" +
                "stockSymbol='" + stockSymbol + '\'' +
                ", time=" + time +
                ", start=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                ", adjustedClose=" + adjustedClose +
                ", dividend=" + dividend +
                ", splitCoefficient=" + splitCoefficient +
                '}';
    }
}
