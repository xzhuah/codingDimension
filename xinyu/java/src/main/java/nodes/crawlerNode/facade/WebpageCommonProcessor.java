package nodes.crawlerNode.facade;

import common.io.web.ResponseProcessor;
import common.io.web.constants.ValueConstant;
import common.io.web.models.ResponseProcessResult;
import nodes.crawlerNode.models.WebpageCommonInfo;
import nodes.crawlerNode.utils.Converter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

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
    public ResponseProcessResult process(CloseableHttpResponse response, String url) throws Exception {
        HttpEntity entity = response.getEntity();
        String msg = null;
        try {
            msg = EntityUtils.toString(entity, ValueConstant.Encoding.UTF_8.getValue());
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        // Obtained document from website content
        Document document = Jsoup.parse(msg);
        WebpageCommonInfo webpageCommonInfo = Converter.toWebpageCommonInfo(document, url);
        return webpageCommonInfo;
    }
}
