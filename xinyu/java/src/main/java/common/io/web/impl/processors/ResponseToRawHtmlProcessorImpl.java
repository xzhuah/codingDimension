package common.io.web.impl.processors;

import com.google.inject.Singleton;
import common.io.web.constants.ValueConstant;
import common.io.web.facade.ResponseProcessor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.Optional;


/**
 * Created by Xinyu Zhu on 7/1/2020, 9:09 AM
 * common.io.web.impl in AllInOne
 * Read out raw html content from the response
 */
@Singleton
public class ResponseToRawHtmlProcessorImpl implements ResponseProcessor<String> {

    @Override
    public Optional<String> process(CloseableHttpResponse response, String url) throws Exception {
        HttpEntity entity = response.getEntity();
        String msg = "";
        msg = EntityUtils.toString(entity, ValueConstant.Encoding.UTF_8.getValue());
        response.close();
        return Optional.of(msg);
    }
}
