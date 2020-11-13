package nodes.stockinfoNode.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.io.web.constants.ValueConstant;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by Xinyu Zhu on 2020/11/8, 21:45
 * nodes.stockinfoNode.utils in codingDimensionTemplate
 */
public class Converter {
    public static Bson toPrimaryFilter(StockCompanyPOJO stockCompanyPOJO) {
        return new Document("symbol", stockCompanyPOJO.getSymbol());

    }

    public static StockCompanyPOJO toStockCompanyPOJO(String symbol) {
        StockCompanyPOJO stockCompanyPOJO = new StockCompanyPOJO();
        stockCompanyPOJO.setSymbol(symbol);
        return stockCompanyPOJO;
    }

    public static Bson toPrimaryFilter(StockDailyRecordPOJO stockCompanyPOJO) {
        return new Document("symbol", stockCompanyPOJO.getSymbol())
                .append("time", stockCompanyPOJO.getTime());
    }

    public static Bson getTimeFilterForStockDailyRecord(long startTime, long endTime) {
        return and(gte("time", startTime), lt("time", endTime));
    }

    public static JsonObject toJsonObject(CloseableHttpResponse closeableHttpResponse) throws IOException {
        HttpEntity entity = closeableHttpResponse.getEntity();
        String msg = "";
        msg = EntityUtils.toString(entity, ValueConstant.Encoding.UTF_8.getValue());
        JsonElement element = JsonParser.parseString(msg);
        JsonObject jsonObject = element.getAsJsonObject();
        return jsonObject;
    }
}
