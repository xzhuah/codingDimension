package common.io.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xinyu Zhu on 7/2/2020, 10:31 PM
 * common.io.file in AllInOne
 */
public class JsonFileClient {

    public static JsonObject objectToJson(Object obj) {
        Gson gson = new GsonBuilder().create();
        return gson.toJsonTree(obj).getAsJsonObject();
    }

    public static void writeFile(String file, Object object) {
        Gson gson = new GsonBuilder().create();
        PlaintextClient.write(file, gson.toJson(object));

    }


    public static void appendFile(String file, Object object) {
        Gson gson = new GsonBuilder().create();
        PlaintextClient.appendFile(file, System.lineSeparator() + gson.toJson(object));

    }

    public static List<JsonElement> readFile(String file) {
        return readFile(file, System.lineSeparator(), JsonElement.class);
    }

    public static <T> List<T> readFile(String file, Class classOfT) {
        return readFile(file, System.lineSeparator(), classOfT);
    }

    public static <T> List<T> readFile(String file, String spliter, Class classOfT) {
        Gson gson = new GsonBuilder().create();
        List<String> content = PlaintextClient.readFileLines(file, spliter);
        List<T> result = new ArrayList<>();
        for (String c : content) {
            if (null != c && c.length() > 0) {
                try {
                    System.out.println(c);
                    Object ele = gson.fromJson(c, classOfT);
                    result.add((T) ele);
                } catch (Throwable e) {
                    e.printStackTrace();
                    continue;
                }

            }
        }
        return result;
    }
}
