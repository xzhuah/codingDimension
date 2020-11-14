package common.io.web.impl.processors;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.inject.Singleton;
import common.io.web.facade.ResponseProcessor;
import common.io.web.constants.ValueConstant;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.Optional;

@Singleton
public class ResponseToJsonArrayProcessorImpl implements ResponseProcessor<JsonArray> {

    @Override
    public Optional<JsonArray> process(CloseableHttpResponse response, String url) throws Exception {
        HttpEntity entity = response.getEntity();
        String msg;
        msg = EntityUtils.toString(entity, ValueConstant.Encoding.UTF_8.getValue());
        JsonArray jsonObject = JsonParser.parseString(msg).getAsJsonArray();
        response.close();
        return Optional.of(jsonObject);
    }
}