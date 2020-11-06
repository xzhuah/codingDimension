package common.io.web.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.io.web.ResponseProcessor;
import common.io.web.constants.ValueConstant;
import common.io.web.models.ResponseProcessResult;
import common.io.web.models.WebpageJsonDTO;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;


/**
 * Created by Xinyu Zhu on 2020/11/1, 20:26
 * common.io.web.impl in AllInOne
 */
public class ResponseToJsonProcessorImpl implements ResponseProcessor {
    private static ResponseProcessor instance = null;

    private ResponseToJsonProcessorImpl() {
    }

    public static ResponseProcessor getInstance() {
        if (instance == null) {
            synchronized (ResponseToRawHtmlProcessorImpl.class) {
                if (instance == null) {
                    instance = new ResponseToJsonProcessorImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public ResponseProcessResult process(CloseableHttpResponse response) throws Exception {
        HttpEntity entity = response.getEntity();
        String msg = "";
        msg = EntityUtils.toString(entity, ValueConstant.Encoding.UTF_8.getValue());
        JsonObject jsonObject = new JsonParser().parse(msg).getAsJsonObject();
        return new WebpageJsonDTO(jsonObject);
    }
}
