package common.io.web.models;

import com.google.gson.JsonObject;

/**
 * Created by Xinyu Zhu on 2020/11/1, 20:30
 * common.io.web.models in AllInOne
 */
public class WebpageJsonDTO implements ResponseProcessResult {
    private JsonObject jsonObject;

    public WebpageJsonDTO(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public String toString() {
        return "WebpageJsonDTO{" +
                "jsonObject=" + jsonObject +
                '}';
    }
}
