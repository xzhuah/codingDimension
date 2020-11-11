package common.io.database.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.Document;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/10, 22:10
 * common.io.database.mongodb in codingDimensionTemplate
 */
public interface MongoDBPojoClient {
    // Info about database
    MongoIterable<String> getAllDatabaseNameAsMongoIterable();

    List<String> getAllDatabaseName();

    MongoIterable<String> getAllCollectionNameAsMongoIterable();

    List<String> getAllCollection();

    /*
            This method will create database if the database does not exist
         */
    MongoDBPojoClient setCurrentDatabase(String databaseName);

    MongoDatabase getCurrentDatabase();

    MongoDatabase setAndGetDatabase(String databaseName);

    /*
            This method will create collection if the collection does not exist in current database
         */
    MongoDBPojoClient setCurrentCollection(String collectionName);

    MongoDBPojoClient setCurrentCollection(String collectionName, Class cls);

    MongoCollection getCurrentCollection();

    MongoCollection setAndGetCurrentCollection(String collectionName);

    MongoCollection setAndGetCurrentCollection(String collectionName, Class cls);

    default MongoDBPojoClient insert(Document document) {
        return insert(document, Document.class);
    }

    default MongoDBPojoClient insert(List<Document> documents) {
        return insert(documents, Document.class);
    }

    MongoDBPojoClient insert(Object pojoObject, Class cls);

    MongoDBPojoClient insert(List<Object> pojoObjects, Class cls);

    void close();
}
