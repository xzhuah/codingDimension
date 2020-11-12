package common.io.database.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import common.io.database.mongodb.utils.Converter;
import org.bson.Document;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/3, 19:38
 * common.io.database.mongodb in AllInOne
 * <p>
 * This interface aims at providing handy and basic database operation
 */
public interface MongoDBClient {
    // Info about database
    MongoIterable<String> getAllDatabaseNameAsMongoIterable();

    default List<String> getAllDatabaseName() {
        return Converter.toList(getAllDatabaseNameAsMongoIterable());
    }

    MongoIterable<String> getAllCollectionNameAsMongoIterable(String databaseName);

    default List<String> getAllCollection(String databaseName) {
        return Converter.toList(getAllCollectionNameAsMongoIterable(databaseName));
    }

    MongoDatabase getDatabase(String databaseName);

    MongoCollection<Document> getCollection(String databaseName, String collectionName);

    void close();
}
