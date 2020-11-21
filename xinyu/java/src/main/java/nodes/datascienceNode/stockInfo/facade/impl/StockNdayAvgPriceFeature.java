package nodes.datascienceNode.stockInfo.facade.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Setter;
import nodes.datascienceNode.common.utils.Algorithms;
import nodes.datascienceNode.stockInfo.utils.Converter;
import nodes.featureEngineeringNode.facade.Feature;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/21, 1:57
 * nodes.datascienceNode.stockInfo.facade.impl in codingDimensionTemplate
 */
@Setter
public class StockNdayAvgPriceFeature implements Feature<List<StockDailyRecordPOJO>, JsonArray> {
    private int nDay;

    public StockNdayAvgPriceFeature(int nDay) {
        this.nDay = nDay;
    }

    @Override
    public JsonArray extractForInstance(List<StockDailyRecordPOJO> target) {
        List<Double> dailyAvg = Converter.toDailyAvgPriceList(target);
        List<Double> result = Algorithms.slidingWindowAvg(Algorithms.listPadding(dailyAvg,nDay - 1,  Algorithms.PaddingPolicy.LEFT), nDay);
        JsonArray resultArray = new JsonArray();
        Gson gson = new GsonBuilder().create();
        for (int i = 0; i < target.size(); i++) {
            JsonObject obj = gson.toJsonTree(target.get(i)).getAsJsonObject();
            obj.addProperty(nDay + " avg", result.get(i));
            obj.remove("id");
            resultArray.add(obj);
        }

        return resultArray;
    }

    @Override
    public String getFeatureName() {
       return String.format("%s day avg price", this.nDay);
    }
}
