package common.io.web.impl;


import common.io.web.ResponseProcessor;
import common.io.web.SyncHttpClient;
import common.io.web.constants.ValueConstant;
import common.io.web.models.ResponseProcessResult;
import common.io.web.utils.HttpRequestRetryHandlerBuilder;
import org.apache.http.ParseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;


import java.io.IOException;

/**
 * Created by Xinyu Zhu on 11/3/2020, 8:42 AM
 * common.io.web.impl in AllInOne
 */
public class SyncHttpClientImpl implements SyncHttpClient {

    private PoolingHttpClientConnectionManager connManager;
    private CloseableHttpClient httpclient;
    private ResponseProcessor responseProcessor;

    public SyncHttpClientImpl() {
        this(ResponseToRawHtmlProcessorImpl.getInstance());
    }

    public SyncHttpClientImpl(ResponseProcessor responseProcessor) {
        this.connManager = new PoolingHttpClientConnectionManager();
        // Set max connection number
        connManager.setMaxTotal(ValueConstant.ConnectionNumber.MAX_TOTAL_CONNECTION.getValue());
        // Set max connection number per route
        connManager.setDefaultMaxPerRoute(ValueConstant.ConnectionNumber.MAX_CONNECTION_PER_ROUTE.getValue());

        // Create request configuration
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(ValueConstant.Timeout.DEFAULT_CONNECTION_REQUEST_TIMEOUT.getValue())
                .setConnectTimeout(ValueConstant.Timeout.DEFAULT_CONNECTION_TIMEOUT.getValue())
                .setSocketTimeout(ValueConstant.Timeout.DEFAULT_SOCKET_TIMEOUT.getValue())
                .setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
                .build();


        HttpRequestRetryHandler retry = HttpRequestRetryHandlerBuilder.getDefaultHandler();

        // create httpClient
        this.httpclient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(retry)
                .setConnectionManager(connManager)
                .build();

        this.responseProcessor = responseProcessor;
    }

    @Override
    public ResponseProcessResult processRequest(HttpUriRequest request) throws Exception {
        CloseableHttpResponse response = null;
        try {
            response = this.httpclient.execute(request);
            return this.responseProcessor.process(response);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            if (null != response) {
                try {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new Exception("SyncHttpClientImpl.processRequest: failed to process result: " + request);
    }
}
