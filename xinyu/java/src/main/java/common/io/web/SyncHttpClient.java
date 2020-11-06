package common.io.web;

import common.io.web.models.ResponseProcessResult;
import common.io.web.utils.RequestBuilder;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.Map;

/**
 * Created by Xinyu Zhu on 7/1/2020, 8:24 AM
 * common.io.web in AllInOne
 */
public interface SyncHttpClient {
    ResponseProcessResult processRequest(HttpUriRequest request) throws Exception;

    default ResponseProcessResult doPost(String url, Map<String, String> params, Map<String, String> header) throws Exception {
        return processRequest(RequestBuilder.buildHttpPost(url, params, header));
    }

    default ResponseProcessResult doGet(String url, Map<String, String> params, Map<String, String> header) throws Exception {
        return processRequest(RequestBuilder.buildHttpGet(url, params, header));
    }
}
