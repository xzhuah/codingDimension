package nodes.stockinfoNode.crawler.facade.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Singleton;
import common.io.web.facade.ResponseProcessor;
import common.time.TimeClient;
import common.time.TimeConstant;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;
import nodes.stockinfoNode.utils.Converter;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.util.*;

@Singleton
public class DailyPriceProcessor implements ResponseProcessor<List<StockDailyRecordPOJO>> {

    private static StockDailyRecordPOJO toDailyPriceData(JsonObject dailyData, String dayString, String symbol) throws ParseException, java.text.ParseException {
        StockDailyRecordPOJO stockDailyRecordPOJO = new StockDailyRecordPOJO();
        stockDailyRecordPOJO.setOpen(dailyData.get("1. open").getAsDouble());
        stockDailyRecordPOJO.setHigh(dailyData.get("2. high").getAsDouble());
        stockDailyRecordPOJO.setLow(dailyData.get("3. low").getAsDouble());
        stockDailyRecordPOJO.setClose(dailyData.get("4. close").getAsDouble());
        stockDailyRecordPOJO.setAdjustedClose(dailyData.get("5. adjusted close").getAsDouble());
        stockDailyRecordPOJO.setVolume(dailyData.get("6. volume").getAsDouble());
        stockDailyRecordPOJO.setDividend(dailyData.get("7. dividend amount").getAsDouble());
        stockDailyRecordPOJO.setSplitCoefficient(dailyData.get("8. split coefficient").getAsDouble());
        stockDailyRecordPOJO.setTime(TimeClient.stringToTimestamp(dayString, TimeConstant.dateOnlyFormat));
        stockDailyRecordPOJO.setSymbol(symbol);
        return stockDailyRecordPOJO;
    }

    public Optional<List<StockDailyRecordPOJO>> process(CloseableHttpResponse response, String url) throws Exception {

        JsonObject jsonObject = Converter.toJsonObject(response);

        try {
            JsonObject dailyPriceObject = jsonObject.get("Time Series (Daily)").getAsJsonObject();
            String symbol = jsonObject.get("Meta Data").getAsJsonObject().get("2. Symbol").getAsString();

            Set<Map.Entry<String, JsonElement>> dailyEntry = dailyPriceObject.entrySet();

            List<StockDailyRecordPOJO> priceDataList = new ArrayList<>(100);

            for (Map.Entry<String, JsonElement> entry : dailyEntry) {
                String dayString = entry.getKey();
                JsonObject dailyData = dailyPriceObject.get(dayString).getAsJsonObject();
                priceDataList.add(toDailyPriceData(dailyData, dayString, symbol));
            }
            return Optional.of(priceDataList);
        } catch (Exception e) {
            // Return Empty List if encounter error
            System.err.println("Encountered invalid response: " + response);
            return Optional.of(new ArrayList<>(0));
        } finally {
            response.close();
        }
    }
}
