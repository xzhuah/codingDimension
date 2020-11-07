package nodes.crawlerNode;

import common.io.web.PoolingAsyncHttpClient;
import common.io.web.ResponseProcessor;
import common.io.web.impl.PoolingAsyncHttpClientImpl;
import common.io.web.models.ResponseProcessResult;
import common.io.web.utils.RequestBuilder;
import nodes.crawlerNode.constants.CrawlerConstant;
import nodes.crawlerNode.constants.CrawlerStatus;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Future;

import static common.utils.ConditionChecker.checkStatus;

/**
 * Created by Xinyu Zhu on 2020/11/7, 11:45
 * nodes.crawlerNode.impl in codingDimensionTemplate
 * <p>
 * You can inherit it, you can inject it, very handy
 * <p>
 * If you used tag to mark requests, you must use the same tag to fetch those result, the crawler won't memorize
 * which result belongs to which url + params combination
 * <p>
 * If you didn't use tag to mark request, the crawler will memorize which result belongs to which
 * url + params combination, so you need to provide url + params to fetch the result
 */
public class BaseCrawler {
    PoolingAsyncHttpClient poolingAsyncHttpClient;
    private CrawlerStatus currentStatus;


    public BaseCrawler(ResponseProcessor responseProcessor) {
        poolingAsyncHttpClient = new PoolingAsyncHttpClientImpl(responseProcessor);
        currentStatus = CrawlerStatus.RUNNING_JOB_ASYNC;
    }

    // For lazy me :)
    public synchronized void addJobToQueue(String url) throws Exception {
        // In this case, tag is just the url
        addJobToQueue(null, url, null, CrawlerConstant.DEFAULT_HEADER);
    }

    // For lazy me :)
    public synchronized void addJobToQueue(String url, Map<String, String> params) throws Exception {
        addJobToQueue(null, url, params, CrawlerConstant.DEFAULT_HEADER);
    }

    // params, header can be null
    public synchronized void addJobToQueue(String url, Map<String, String> params, Map<String, String> header) throws Exception {
        addJobToQueue(null, url, params, header);
    }

    // params, header can be null
    // You can use the same tag for multiple different request, is up to you, we return all result under that tag when you
    // ask for it, if you provide a null tag, we will create a unique tag for you based on url and params so that you can
    // later find what you want
    public synchronized void addJobToQueue(String tag, String url, Map<String, String> params, Map<String, String> header) throws Exception {
        checkStatus(canAcceptNewJob(), "Crawler can't accept new Job! crawler status: " + currentStatus);
        if (null == tag) {
            tag = getUniqueTag(url, params);
        }
        poolingAsyncHttpClient.addRequestToPool(tag, RequestBuilder.buildHttpGet(url, params, header));
    }

    // To get a result, you must have added it, otherwise you can an empty list.
    // For lazy me :)
    public List<Future<ResponseProcessResult>> getResultFuture(String tag) {
        return getResultFuture(tag, null, null);
    }

    public List<Future<ResponseProcessResult>> getResultFuture(String url, Map<String, String> params) {
        return getResultFuture(null, url, params);
    }

    private List<Future<ResponseProcessResult>> getResultFuture(String tag, String url, Map<String, String> params) {
        checkStatus(canReturnResult(), "Crawler can't return result! crawler status: " + currentStatus);
        if (null == tag) {
            checkStatus(url != null, "Invalid params, tag and url are null!");
            tag = getUniqueTag(url, params);
        } else {
            checkStatus((url == null && params == null) || tag == getUniqueTag(url, params),
                    "Your tag is inconsistant with your url and params: " + tag + ", " + url + ", " + params);
        }
        return poolingAsyncHttpClient.startProcessing(tag);
    }

    public boolean canAcceptNewJob() {
        return this.currentStatus == CrawlerStatus.ACCEPTING_JOB || this.currentStatus == CrawlerStatus.RUNNING_JOB_ASYNC;
    }

    public boolean canReturnResult() {
        return this.currentStatus == CrawlerStatus.FINISHED || this.currentStatus == CrawlerStatus.RUNNING_JOB_ASYNC;
    }

    public void shutDown() {
        this.currentStatus = CrawlerStatus.KILLED;
        this.poolingAsyncHttpClient.finish();
    }

    // We don't consider header, we assum content are determined by url and params
    public static String getUniqueTag(String url, Map<String, String> params) {
        StringBuilder tagBuilder = new StringBuilder(url);

        if (null != params) {
            SortedSet<String> keys = new TreeSet<>(params.keySet());
            keys.stream().forEach(key -> {
                tagBuilder.append(":").append(key).append(":").append(params.get(key));
            });
        }
        return tagBuilder.toString();
    }
}
