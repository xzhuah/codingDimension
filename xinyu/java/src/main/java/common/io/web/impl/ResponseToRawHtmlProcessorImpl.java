package common.io.web.impl;

import common.io.web.ResponseProcessor;
import common.io.web.constants.ValueConstant;
import common.io.web.models.WebpageRawHtmlDTO;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;


/**
 * Created by Xinyu Zhu on 7/1/2020, 9:09 AM
 * common.io.web.impl in AllInOne
 * Read out raw html content from the response
 */
public class ResponseToRawHtmlProcessorImpl implements ResponseProcessor {

    private static ResponseProcessor instance = null;

    private ResponseToRawHtmlProcessorImpl() {
    }

    public static ResponseProcessor getInstance() {
        if (instance == null) {
            synchronized (ResponseToRawHtmlProcessorImpl.class) {
                if (instance == null) {
                    instance = new ResponseToRawHtmlProcessorImpl();
                }
            }
        }
        return instance;
    }


    @Override
    public WebpageRawHtmlDTO process(CloseableHttpResponse response) throws Exception {
        HttpEntity entity = response.getEntity();
        String msg = "";
        msg = EntityUtils.toString(entity, ValueConstant.Encoding.UTF_8.getValue());

        return new WebpageRawHtmlDTO(msg);
    }
}
