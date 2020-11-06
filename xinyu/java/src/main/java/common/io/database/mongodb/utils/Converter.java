package common.io.database.mongodb.utils;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoIterable;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Xinyu Zhu on 2020/11/3, 20:45
 * common.io.database.mongodb.utils in AllInOne
 */
public class Converter {
    public static List<String> toList(MongoIterable<String> mongoIterable) {
        if (null == mongoIterable) {
            return null;
        }
        List<String> resultList = new ArrayList<>();
        mongoIterable.forEach((Consumer<String>) resultList::add);
        return resultList;
    }

    public static List<Document> toList(FindIterable<Document> findIterable) {
        if (null == findIterable) {
            return null;
        }
        List<Document> resultList = new ArrayList<>();
        findIterable.forEach((Consumer<Document>) resultList::add);
        return resultList;
    }
}
