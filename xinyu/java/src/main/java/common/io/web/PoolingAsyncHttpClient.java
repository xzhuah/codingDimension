package common.io.web;

import org.apache.http.client.methods.HttpUriRequest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Created by Xinyu Zhu on 7/1/2020, 8:20 AM
 * common.io.web in AllInOne
 *
 * We use generic Types to support more process result type T, and tag type E
 */
public interface PoolingAsyncHttpClient<T, E> {
    void addRequestToPool(E tag, HttpUriRequest httpRequest);

    List<Future<Optional<T>>> startProcessing(E tag);

    // shutdown
    void finish();
}
