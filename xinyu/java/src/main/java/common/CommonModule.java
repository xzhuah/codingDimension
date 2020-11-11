package common;

import com.google.gson.JsonObject;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
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

    }
}
