package common.io.web.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import common.io.web.PoolingAsyncHttpClient;
import common.io.web.ResponseProcessor;
import common.io.web.SyncHttpClient;
import common.io.web.constants.ValueConstant;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.*;
import java.util.concurrent.*;


/**
 * Created by Xinyu Zhu on 7/1/2020, 10:24 AM
 * common.io.web.impl in AllInOne
 * tag -> [request, request, ...]
 */
public class PoolingAsyncHttpClientImpl<T, E> implements PoolingAsyncHttpClient<T, E> {
    private final SyncHttpClient<T> syncHttpClient;
    private final ExecutorService executorService;
    private final Map<E, Deque<HttpUriRequest>> httpRequests;


    public PoolingAsyncHttpClientImpl(ResponseProcessor<T> responseProcessor) {
        syncHttpClient = new SyncHttpClientImpl<T>(responseProcessor);

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(ValueConstant.FormatTemplate.HTTPCLIENT_POOL.getValue()).build();

        executorService = new ThreadPoolExecutor(ValueConstant.ThreadPoolParam.CORE_POOL_SIZE.getValue(),
                ValueConstant.ThreadPoolParam.MAXIMUM_POOL_SIZE.getValue(),
                ValueConstant.ThreadPoolParam.KEEP_ALIVE_TIME_SECOND.getValue(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(ValueConstant.ThreadPoolParam.BLOCK_QUEUE_CAPACITY.getValue()),
                namedThreadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy());

        httpRequests = new ConcurrentHashMap<>();
    }

    @Override
    public void addRequestToPool(E tag, HttpUriRequest httpRequest) {
        if (!httpRequests.containsKey(tag)) {
            httpRequests.putIfAbsent(tag, new LinkedList<>());
        }
        httpRequests.get(tag).addLast(httpRequest);
    }

    @Override
    public List<Future<Optional<T>>> startProcessing(E tag) {
        List<Future<Optional<T>>> currentResult = new ArrayList<>();
        Deque<HttpUriRequest> currentReceivedRequest = httpRequests.get(tag);
        if (null == currentReceivedRequest) {
            return currentResult;
        }
        synchronized (httpRequests) {
            // Other thread can't access to httpRequests to modify the deque while the deque is being read here
            while (!currentReceivedRequest.isEmpty()) {
                HttpUriRequest nextRequest = currentReceivedRequest.pollFirst();
                currentResult.add(executorService.submit(new ProcessRequestCallable(nextRequest)));
            }
        }
        return currentResult;
    }

    @Override
    public void finish() {
        this.executorService.shutdown();
    }

    private class ProcessRequestCallable implements Callable<Optional<T>> {
        private final HttpUriRequest httpUriRequest;

        public ProcessRequestCallable(HttpUriRequest httpUriRequest) {
            this.httpUriRequest = httpUriRequest;
        }

        @Override
        public Optional<T> call() throws Exception {
            return PoolingAsyncHttpClientImpl.this.syncHttpClient.processRequest(this.httpUriRequest);
        }
    }
}
