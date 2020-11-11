package common.io.database.mongodb.impl;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import common.io.database.mongodb.MongoDBClient;
import common.io.database.mongodb.MongoDBPojoClient;
import common.io.database.mongodb.constants.DBConstant;
import common.io.database.mongodb.utils.Converter;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Created by Xinyu Zhu on 2020/11/3, 19:41
 * common.io.database.mongodb.impl in AllInOne
 * <p>
 * The PojoClient support POJO object: https://mongodb.github.io/mongo-java-driver/3.5/driver/getting-started/quick-start-pojo/
 * Start the local mongoDB instance first before using this client
 */
public class MongoDBPojoClientImpl implements MongoDBPojoClient {
    MongoClient mongoClient;
    MongoDatabase currentDatabase;
    MongoCollection currentCollection;

    /**
     * Set up connection with the default setting
     */
    public MongoDBPojoClientImpl() {
        this(DBConstant.DEFAULT_HOST_ADDRESS, DBConstant.DEFAULT_PORT);
    }

    /**
     * Set up connection with specific host and port
     *
     * @param host host address
     * @param port port number
     */
    public MongoDBPojoClientImpl(String host, int port) {
        this(String.format("%s:%d", host, port));
    }

    /**
     * Set up connection with a connectionUrl
     * I haven't encounter the need of remote connection, so the credential staff is not implemented yet
     *
     * @param connectionUrl a string in this kind of format "hostname:27017"
     */
    public MongoDBPojoClientImpl(String connectionUrl) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build();
        mongoClient = new MongoClient(connectionUrl, mongoClientOptions);
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
    public MongoDBPojoClient setCurrentDatabase(String databaseName) {
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
    public MongoDBPojoClient setCurrentCollection(String collectionName) {
        this.currentCollection = currentDatabase.getCollection(collectionName);
        return this;
    }

    @Override
    public MongoDBPojoClient setCurrentCollection(String collectionName, Class cls) {
        this.currentCollection = currentDatabase.getCollection(collectionName, cls);
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

    @Override
    public MongoCollection setAndGetCurrentCollection(String collectionName, Class cls) {
        setCurrentCollection(collectionName, cls);
        return currentCollection;
    }

    // TODO provide index API: I don't want to implement those index related API since I think it would be better to
    // manage index manually from UI. The program seldom know when is the best time to create index

    // TODO I don't provide Query/Update API since this process requires complex Query logic which is a part of business
    // Logic

    @Override
    public MongoDBPojoClient insert(Object pojoObject, Class cls) {
        synchronized (this) {
            currentCollection = currentCollection.withDocumentClass(cls);
            currentCollection.insertOne(pojoObject);
        }
        return this;
    }

    @Override
    public MongoDBPojoClient insert(List<Object> pojoObjects, Class cls) {
        synchronized (this) {
            currentCollection = currentCollection.withDocumentClass(cls);
            currentCollection.insertMany(pojoObjects);
        }
        return this;
    }

    @Override
    public void close() {
        mongoClient.close();
    }
}
