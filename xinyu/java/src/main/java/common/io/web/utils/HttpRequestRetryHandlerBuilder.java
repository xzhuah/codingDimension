package common.io.web.utils;

import common.io.web.constants.ValueConstant;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

/**
 * Created by Xinyu Zhu on 7/1/2020, 10:05 AM
 * common.io.web.utils in AllInOne
 */
public class HttpRequestRetryHandlerBuilder {

    public static HttpRequestRetryHandler getDefaultHandler() {
        return (exception, executionCount, context) -> {
            if (executionCount >= ValueConstant.ConnectionNumber.MAX_RETRY.getValue()) {
                // retry no more than a limited number of times
                return false;
            }
            if (exception instanceof NoHttpResponseException) {
                // server not responding, retry
                return true;
            }
            if (exception instanceof SSLHandshakeException) {
                // SSL Exception, fail directly
                return false;
            }
            if (exception instanceof InterruptedIOException) {
                // timeout
                return false;
            }
            if (exception instanceof UnknownHostException) {
                // unknow host
                return false;
            }
            if (exception instanceof SSLException) {
                // SSL Exception, fail directly
                return false;
            }
            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();

            if (!(request instanceof HttpEntityEnclosingRequest)) {
                return true;
            }
            return false;
        };
    }
}
