package common.io.database.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
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

    List<String> getAllDatabaseName();

    MongoIterable<String> getAllCollectionNameAsMongoIterable();

    List<String> getAllCollection();

    MongoDatabase getCurrentDatabase();

    /*
            This method will create database if the database does not exist
         */
    MongoDBClient setCurrentDatabase(String databaseName);

    MongoDatabase setAndGetDatabase(String databaseName);

    MongoCollection getCurrentCollection();

    /*
            This method will create collection if the collection does not exist in current database
         */
    MongoDBClient setCurrentCollection(String collectionName);

    MongoCollection setAndGetCurrentCollection(String collectionName);

    MongoDBClient insert(Document document);

    MongoDBClient insert(List<Document> documents);

    void close();
}
