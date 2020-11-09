package nodes.stockinfoNode.crawler.facade;

import com.google.gson.JsonObject;
import common.io.web.ResponseProcessor;
import common.io.web.models.ResponseProcessResult;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.utils.Converter;
import org.apache.http.client.methods.CloseableHttpResponse;

/**
 * Created by Xinyu Zhu on 2020/11/8, 22:04
 * nodes.stockinfoNode.crawler.facade in codingDimensionTemplate
 */
public class CompanyInfoProcessor implements ResponseProcessor {

    private static ResponseProcessor instance = null;

    private CompanyInfoProcessor() {
    }

    public static ResponseProcessor getInstance() {
        if (instance == null) {
            synchronized (DailyPriceProcessor.class) {
                if (instance == null) {
                    instance = new CompanyInfoProcessor();
                }
            }
        }
        return instance;
    }

    private static StockCompanyPOJO toStockCompanyPOJO(JsonObject jsonObject) {
        StockCompanyPOJO stockCompanyPOJO = new StockCompanyPOJO();
        stockCompanyPOJO.setSymbol(jsonObject.get("Symbol").getAsString());
        try {
            stockCompanyPOJO.setExchange(jsonObject.get("Exchange").getAsString());
        } catch (Exception e) {
            // It may not have this field;
        }
        try {
            stockCompanyPOJO.setIndustry(jsonObject.get("Industry").getAsString());
        } catch (Exception e) {
            // It may not have this field;
        }
        try {
            stockCompanyPOJO.setName(jsonObject.get("Name").getAsString());
        } catch (Exception e) {
            // It may not have this field;
        }
        try {
            stockCompanyPOJO.setSector(jsonObject.get("Sector").getAsString());
        } catch (Exception e) {
            // It may not have this field;
        }
        try {
            stockCompanyPOJO.setCountry(jsonObject.get("Country").getAsString());
        } catch (Exception e) {
            // It may not have this field;
        }
        return stockCompanyPOJO;
    }


    @Override
    public ResponseProcessResult process(CloseableHttpResponse response, String url) throws Exception {
        JsonObject jsonObject = Converter.toJsonObject(response);
        return toStockCompanyPOJO(jsonObject);
    }
}
