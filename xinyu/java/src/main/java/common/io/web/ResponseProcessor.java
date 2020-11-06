package common.io.web;

import common.io.web.models.ResponseProcessResult;
import org.apache.http.client.methods.CloseableHttpResponse;


/**
 * Created by Xinyu Zhu on 7/1/2020, 9:04 AM
 *common.io.web in AllInOne
 */
public interface ResponseProcessor {
    ResponseProcessResult process(CloseableHttpResponse response) throws Exception;
}
