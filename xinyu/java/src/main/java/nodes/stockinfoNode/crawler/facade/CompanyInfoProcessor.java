package nodes.stockinfoNode.crawler.facade;

import com.google.gson.JsonObject;
import com.google.inject.Singleton;
import common.io.web.ResponseProcessor;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.utils.Converter;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.util.Optional;

/**
 * Created by Xinyu Zhu on 2020/11/8, 22:04
 * nodes.stockinfoNode.crawler.facade in codingDimensionTemplate
 */
@Singleton
public class CompanyInfoProcessor implements ResponseProcessor<StockCompanyPOJO> {

    private static StockCompanyPOJO toStockCompanyPOJO(JsonObject jsonObject) {
        StockCompanyPOJO stockCompanyPOJO = new StockCompanyPOJO();
        try {
            stockCompanyPOJO.setSymbol(jsonObject.get("Symbol").getAsString());
        } catch (Exception e) {
            // return null when parsing failed
            return null;
        }
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
    public Optional<StockCompanyPOJO> process(CloseableHttpResponse response, String url) throws Exception {
        JsonObject jsonObject = Converter.toJsonObject(response);
        return Optional.ofNullable(toStockCompanyPOJO(jsonObject));
    }
}
