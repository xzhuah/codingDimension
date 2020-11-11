package common.io.file;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Test;

import java.io.IOException;
import java.util.List;


/**
 * Created by Xinyu Zhu on 2020/11/3, 22:33
 * common.io.file in AllInOne
 */
public class JsonFileClientTest {

    @Test
    public void test() throws IOException {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", "xinyu");
        obj.addProperty("age", 23);

        JsonArray array = new JsonArray();
        array.add(obj);
        array.add(obj);

        JsonFileClient.appendFile("test.json", array);

        List<JsonElement> result = JsonFileClient.readFile("test.json");
        System.out.println(result);
    }

}