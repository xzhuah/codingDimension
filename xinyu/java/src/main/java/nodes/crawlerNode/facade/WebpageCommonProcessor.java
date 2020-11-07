package nodes.crawlerNode.facade;

import common.io.web.ResponseProcessor;
import common.io.web.models.ResponseProcessResult;
import org.apache.http.client.methods.CloseableHttpResponse;

/**
 * Created by Xinyu Zhu on 2020/11/7, 12:11
 * nodes.crawlerNode.facade in codingDimensionTemplate
 */
public class WebpageCommonProcessor implements ResponseProcessor {

    private static WebpageCommonProcessor instance = null;

    private WebpageCommonProcessor() {
    }

    public static ResponseProcessor getInstance() {
        if (instance == null) {
            synchronized (WebpageCommonProcessor.class) {
                if (instance == null) {
                    instance = new WebpageCommonProcessor();
                }
            }
        }
        return instance;
    }

    @Override
    public ResponseProcessResult process(CloseableHttpResponse response) throws Exception {
        // TODO
        return null;
    }
}
