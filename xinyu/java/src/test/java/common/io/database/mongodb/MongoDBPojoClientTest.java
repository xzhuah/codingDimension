package common.io.database.mongodb;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import common.io.database.mongodb.MongoDBPojoClient;
import common.io.database.mongodb.models.Address;
import common.io.database.mongodb.models.Person;
import common.io.database.mongodb.utils.Converter;
import nodes.NodeModule;
import org.bson.Document;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Xinyu Zhu on 2020/11/4, 22:08
 * common.io.database.mongodb.impl in AllInOne
 */
public class MongoDBPojoClientTest {
    MongoDBPojoClient mongoDBPojoClient = NodeModule.getGlobalInjector().getInstance(MongoDBPojoClient.class);

    @Test
    public void getAllDatabaseName() {
        System.out.println(mongoDBPojoClient.getAllDatabaseName());
    }

    @Test
    public void insetGeneral() {
        MongoCollection<Document> collection = mongoDBPojoClient.getCollection("test", "test_collection");
        collection.deleteMany(eq("name", "Café Con Leche"));

        Document document = new Document("name", "Café Con Leche")
                .append("contact", new Document("phone", "228-555-0149")
                        .append("email", "cafeconleche@example.com")
                        .append("location", Arrays.asList(-73.92502, 40.8279556)))
                .append("stars", 3)
                .append("categories", Arrays.asList("Bakery", "Coffee", "Pastries"));

        collection.insertOne(document);

        FindIterable<Document> result = collection.find(eq("name", "Café Con Leche"));
        List<Document> resultInList = Converter.toList(result);
        assertEquals(resultInList.size(), 1);
        assertEquals(3, (int) resultInList.get(0).getInteger("stars"));
        assertEquals(((List<Double>) ((Document) resultInList.get(0).get("contact")).get("location")).get(0), Double.valueOf(-73.92502));
    }

    @Test
    public void insertObject() {
        MongoCollection<Person> collection = mongoDBPojoClient.getCollection("test", Person.TABLE_NAME, Person.class);

        collection.deleteMany(eq("name", "Ada Byron"));

        Person ada = new Person("Ada Byron", 20, new Address("St James Square", "London", "W1"));
        collection.insertOne(ada);

        Person somebody = collection.find(eq("address.city", "London")).first();
        assertEquals(somebody.getName(), "Ada Byron");
    }

}