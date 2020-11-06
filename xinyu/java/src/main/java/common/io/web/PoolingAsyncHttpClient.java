package common.io.web;

import common.io.web.models.ResponseProcessResult;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by Xinyu Zhu on 7/1/2020, 8:20 AM
 * common.io.web in AllInOne
 */
public interface PoolingAsyncHttpClient {
    void addRequestToPool(String tag, HttpUriRequest httpRequest);
    List<Future<ResponseProcessResult>> startProcessing(String tag);
    void finish();
}
