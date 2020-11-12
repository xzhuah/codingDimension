package common.io.database.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import common.io.database.mongodb.utils.Converter;
import org.bson.Document;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/10, 22:10
 * common.io.database.mongodb in codingDimensionTemplate
 */
public interface MongoDBPojoClient {
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

    default MongoCollection<Document> getCollection(String databaseName, String collectionName) {
        return getCollection(databaseName, collectionName, Document.class);
    }

    <T> MongoCollection<T> getCollection(String databaseName, String collectionName, Class<T> cls);

    void close();
}
