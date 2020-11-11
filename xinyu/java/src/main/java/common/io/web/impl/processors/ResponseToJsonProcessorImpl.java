package common.io.web.impl.processors;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.io.web.ResponseProcessor;
import common.io.web.constants.ValueConstant;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.Optional;


/**
 * Created by Xinyu Zhu on 2020/11/1, 20:26
 * common.io.web.impl in AllInOne
 */
public class ResponseToJsonProcessorImpl implements ResponseProcessor<JsonObject> {
    private static ResponseProcessor<JsonObject> instance = null;

    private ResponseToJsonProcessorImpl() {
    }

    public static ResponseProcessor<JsonObject> getInstance() {
        if (instance == null) {
            synchronized (ResponseToJsonProcessorImpl.class) {
                if (instance == null) {
                    instance = new ResponseToJsonProcessorImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public Optional<JsonObject> process(CloseableHttpResponse response, String url) throws Exception {
        HttpEntity entity = response.getEntity();
        String msg;
        msg = EntityUtils.toString(entity, ValueConstant.Encoding.UTF_8.getValue());
        JsonObject jsonObject = JsonParser.parseString(msg).getAsJsonObject();
        response.close();
        return Optional.of(jsonObject);
    }
}
