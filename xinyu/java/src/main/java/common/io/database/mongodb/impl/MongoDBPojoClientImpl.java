package common.io.database.mongodb.impl;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import common.io.database.mongodb.MongoDBPojoClient;
import common.io.database.mongodb.constants.DBConstant;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

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
    private final MongoClient mongoClient;

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
    public MongoIterable<String> getAllCollectionNameAsMongoIterable(String databaseName) {
        return mongoClient.getDatabase(databaseName).listCollectionNames();
    }

    @Override
    public MongoDatabase getDatabase(String databaseName) {
        return mongoClient.getDatabase(databaseName);
    }

    @Override
    public <T> MongoCollection<T> getCollection(String databaseName, String collectionName, Class<T> cls) {
        return mongoClient.getDatabase(databaseName).getCollection(collectionName, cls);
    }


    @Override
    public void close() {
        mongoClient.close();
    }
}
