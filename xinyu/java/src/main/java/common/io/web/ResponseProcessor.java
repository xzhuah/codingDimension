package common.io.web;

import common.io.web.models.ResponseProcessResult;
import org.apache.http.client.methods.CloseableHttpResponse;


/**
 * Created by Xinyu Zhu on 7/1/2020, 9:04 AM
 * common.io.web in AllInOne
 */
public interface ResponseProcessor {
    // url field is the url for the response, it provide some context information so that we can encode more complex
    // Info into the result, we can add more context information in the future when needed
    ResponseProcessResult process(CloseableHttpResponse response, String url) throws Exception;
}
