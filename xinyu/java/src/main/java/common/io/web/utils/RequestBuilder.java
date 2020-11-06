package common.io.web.utils;

import common.io.web.constants.ValueConstant;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Xinyu Zhu on 7/1/2020, 8:33 AM
 * common.io.web.utils in AllInOne
 */
public class RequestBuilder {
    public static HttpGet buildHttpGet(String url, Map<String, String> params, Map<String, String> header) throws Exception {
        URIBuilder builder = null;
        HttpGet httpGet = null;

        builder = new URIBuilder(url);

        if (params != null) {
            for (String key : params.keySet()) {
                builder.addParameter(key, params.get(key));
            }
        }

        httpGet = new HttpGet(builder.build());

        if (header != null) {
            for (String key : header.keySet()) {
                httpGet.addHeader(key, header.get(key));
            }
        }
        return httpGet;
    }

    public static HttpPost buildHttpPost(String url, Map<String, String> params, Map<String, String> header) throws Exception {
        URIBuilder builder = null;
        HttpPost httpPost = null;

        builder = new URIBuilder(url);
        httpPost = new HttpPost(builder.build());


        if (params != null) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                nvps.add(new BasicNameValuePair(key, params.get(key)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, ValueConstant.Encoding.UTF_8.getValue()));

        }
        if (header != null) {
            for (String key : header.keySet()) {
                httpPost.addHeader(key, header.get(key));
            }
        }
        return httpPost;
    }
}
