package common.io.file;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 * Created by Xinyu Zhu on 2020/11/29, 19:04
 * common.io.file in codingDimensionTemplate
 * <p>
 * 为整个系统提供统一的密钥读取功能
 */
public class APIKeyClient {
    private static final String API_KEY_FILE = "apiKey.pass";
    private static JsonObject API_KEY;

    static {
        try {
            String content = PlaintextClient.readFile(API_KEY_FILE);
            API_KEY = new Gson().fromJson(content, JsonObject.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getApiKey(String key) {
        JsonElement result = API_KEY.get(key);
        if (null == result) {
            return null;
        } else {
            return result.getAsString();
        }

    }

    public static void main(String[] args) {
        System.out.println(getApiKey("test1"));
    }
}
