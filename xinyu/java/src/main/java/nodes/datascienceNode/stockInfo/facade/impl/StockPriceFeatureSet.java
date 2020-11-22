package nodes.datascienceNode.stockInfo.facade.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import nodes.featureEngineeringNode.facade.Feature;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/21, 19:44
 * nodes.datascienceNode.stockInfo.facade.impl in codingDimensionTemplate
 */
public class StockPriceFeatureSet implements Feature<List<StockDailyRecordPOJO>, JsonArray> {
    List<StockNdayAvgPriceFeature> nDayAvgS;

    public StockPriceFeatureSet() {
        nDayAvgS = new ArrayList<>();
        nDayAvgS.add(new StockNdayAvgPriceFeature(5));
        nDayAvgS.add(new StockNdayAvgPriceFeature(10));
        nDayAvgS.add(new StockNdayAvgPriceFeature(20));
        nDayAvgS.add(new StockNdayAvgPriceFeature(30));
        nDayAvgS.add(new StockNdayAvgPriceFeature(60));
    }

    @Override
    public JsonArray extractForInstance(List<StockDailyRecordPOJO> target) {

        List<List<Double>> featureSetResult = new ArrayList<>(nDayAvgS.size());
        for (StockNdayAvgPriceFeature stockNdayAvgPriceFeature : nDayAvgS) {
            featureSetResult.add(stockNdayAvgPriceFeature.extractForInstance(target));
        }
        JsonArray resultArray = new JsonArray();
        Gson gson = new GsonBuilder().create();
        for (int i = 0; i < target.size(); i++) {
            JsonObject obj = gson.toJsonTree(target.get(i)).getAsJsonObject();
            obj.remove("id");
            for (int j = 0; j < featureSetResult.size(); j++) {
                obj.addProperty(nDayAvgS.get(j).getFeatureName(), featureSetResult.get(j).get(i));
            }
            resultArray.add(obj);
        }

        return resultArray;
    }
}
