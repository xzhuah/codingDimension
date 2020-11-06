package common.io.database.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoIterable;

/**
 * Created by Xinyu Zhu on 2020/11/3, 19:28
 * common.io.database.mongodb in AllInOne
 *
 * These files are written according to this documentation: https://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/connect-to-mongodb/
 */
public class MongoDBConnection {
    public static void main(String[] args) {

        //MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://host1:27017"));

        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

        MongoIterable<String> databaseNames = mongoClient.listDatabaseNames();
        for (String string : databaseNames) {
            System.out.println(string);
        }

        mongoClient.close();
    }
}
