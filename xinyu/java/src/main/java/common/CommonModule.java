package common;

import com.google.gson.JsonObject;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import common.io.database.mongodb.MongoDBClient;
import common.io.database.mongodb.MongoDBPojoClient;
import common.io.database.mongodb.impl.MongoDBClientImpl;
import common.io.database.mongodb.impl.MongoDBPojoClientImpl;
import common.io.web.ResponseProcessor;
import common.io.web.impl.processors.ResponseToJsonProcessorImpl;
import common.io.web.impl.processors.ResponseToRawHtmlProcessorImpl;

/**
 * Created by Xinyu Zhu on 2020/11/9, 21:12
 * common in codingDimensionTemplate
 */
public class CommonModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(new TypeLiteral<ResponseProcessor<String>>() {
        }).to(new TypeLiteral<ResponseToRawHtmlProcessorImpl>() {
        });

        bind(new TypeLiteral<ResponseProcessor<JsonObject>>() {
        }).to(new TypeLiteral<ResponseToJsonProcessorImpl>() {
        });

        try {
            bind(MongoDBClient.class).toConstructor(MongoDBClientImpl.class.getConstructor());
        } catch (NoSuchMethodException e) {
            // Actually the same
            bind(MongoDBClient.class).to(MongoDBClientImpl.class);
        }

        try {
            bind(MongoDBPojoClient.class).toConstructor(MongoDBPojoClientImpl.class.getConstructor());
        } catch (NoSuchMethodException e) {
            // Actually the same
            bind(MongoDBPojoClient.class).to(MongoDBPojoClientImpl.class);
        }

    }
}
