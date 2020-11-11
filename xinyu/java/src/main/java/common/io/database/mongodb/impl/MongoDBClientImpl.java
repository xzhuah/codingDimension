package common.io.database.mongodb.impl;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import common.io.database.mongodb.MongoDBClient;
import common.io.database.mongodb.constants.DBConstant;
import common.io.database.mongodb.utils.Converter;
import org.bson.Document;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/3, 19:41
 * common.io.database.mongodb.impl in AllInOne
 * <p>
 * Start the local mongoDB instance first before using this client
 */
public class MongoDBClientImpl implements MongoDBClient {
    MongoClient mongoClient;
    MongoDatabase currentDatabase;
    MongoCollection currentCollection;

    /**
     * Set up connection with the default setting
     */
    public MongoDBClientImpl() {
        this(DBConstant.DEFAULT_HOST_ADDRESS, DBConstant.DEFAULT_PORT);
    }

    /**
     * Set up connection with specific host and port
     *
     * @param host host address
     * @param port port number
     */
    public MongoDBClientImpl(String host, int port) {
        this(String.format("mongodb://%s:%d", host, port));
    }

    /**
     * Set up connection with a connectionUrl
     * I haven't encounter the need of remote connection, so the credential staff is not implemented yet
     *
     * @param connectionUrl a string in this kind of format "mongodb://hostname:27017"
     */
    public MongoDBClientImpl(String connectionUrl) {
        mongoClient = new MongoClient(new MongoClientURI(connectionUrl));
    }

    // Info about database
    @Override
    public MongoIterable<String> getAllDatabaseNameAsMongoIterable() {
        return mongoClient.listDatabaseNames();
    }

    @Override
    public List<String> getAllDatabaseName() {
        return Converter.toList(getAllDatabaseNameAsMongoIterable());
    }

    @Override
    public MongoIterable<String> getAllCollectionNameAsMongoIterable() {
        if (null != currentDatabase) {
            return currentDatabase.listCollectionNames();
        } else {
            return null;
        }
    }

    @Override
    public List<String> getAllCollection() {
        return Converter.toList(getAllCollectionNameAsMongoIterable());
    }

    /*
        This method will create database if the database does not exist
     */
    @Override
    public MongoDBClient setCurrentDatabase(String databaseName) {
        this.currentDatabase = mongoClient.getDatabase(databaseName);
        return this;
    }

    @Override
    public MongoDatabase getCurrentDatabase() {
        return this.currentDatabase;
    }

    @Override
    public MongoDatabase setAndGetDatabase(String databaseName) {
        setCurrentDatabase(databaseName);
        return getCurrentDatabase();
    }

    /*
        This method will create collection if the collection does not exist in current database
     */
    @Override
    public MongoDBClient setCurrentCollection(String collectionName) {
        this.currentCollection = currentDatabase.getCollection(collectionName);
        return this;
    }

    @Override
    public MongoCollection getCurrentCollection() {
        return currentCollection;
    }

    @Override
    public MongoCollection setAndGetCurrentCollection(String collectionName) {
        setCurrentCollection(collectionName);
        return currentCollection;
    }

    // TODO provide index API: I don't want to implement those index related API since I think it would be better to
    // manage index manually from UI. The program seldom know when is the best time to create index

    // TODO I don't provide Query/Update API since this process requires complex Query logic which is a part of business
    // Logic

    @Override
    public MongoDBClient insert(Document document) {
        currentCollection.insertOne(document);
        return this;
    }

    @Override
    public MongoDBClient insert(List<Document> documents) {
        currentCollection.insertMany(documents);
        return this;
    }

    @Override
    public void close() {
        mongoClient.close();
    }
}
