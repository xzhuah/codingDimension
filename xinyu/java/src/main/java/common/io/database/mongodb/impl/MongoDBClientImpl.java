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
    private final MongoClient mongoClient;

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
    public MongoIterable<String> getAllCollectionNameAsMongoIterable(String databaseName) {
        return mongoClient.getDatabase(databaseName).listCollectionNames();
    }

    @Override
    public MongoDatabase getDatabase(String databaseName) {
        return mongoClient.getDatabase(databaseName);
    }

    @Override
    public MongoCollection<Document> getCollection(String databaseName, String collectionName) {
        return mongoClient.getDatabase(databaseName).getCollection(collectionName);
    }

    @Override
    public void close() {
        mongoClient.close();
    }

}
