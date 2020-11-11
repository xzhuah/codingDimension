package common.io.web;

import org.apache.http.client.methods.CloseableHttpResponse;

import java.util.Optional;


/**
 * Created by Xinyu Zhu on 7/1/2020, 9:04 AM
 * common.io.web in AllInOne
 */
public interface ResponseProcessor<T> {
    // url field is the url for the response, it provide some context information so that we can encode more complex
    // Info into the result, we can add more context information in the future when needed
    Optional<T> process(CloseableHttpResponse response, String url) throws Exception;
}
