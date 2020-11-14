package nodes.hotinfoNode.crawler.facade.impl;

import com.google.inject.Singleton;
import common.io.web.facade.ResponseProcessor;
import common.io.web.constants.ValueConstant;
import nodes.hotinfoNode.models.VideoRecordVO;
import nodes.hotinfoNode.utils.Converter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Xinyu Zhu on 7/1/2020, 12:09 PM
 * xinyu.hotinfo.biz.bilibili.facade in HotInfo
 */
@Singleton
public class ResponseToRankListProcessor implements ResponseProcessor<List<VideoRecordVO>> {

    @Override
    public Optional<List<VideoRecordVO>> process(CloseableHttpResponse response, String url) throws Exception {
        HttpEntity entity = response.getEntity();
        String msg = null;
        try {
            msg = EntityUtils.toString(entity, ValueConstant.Encoding.UTF_8.getValue());
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            response.close();
        }

        List<VideoRecordVO> result = new ArrayList<>(100);
        Document document = Jsoup.parse(msg);
        Element rankList = document.selectFirst("ul.rank-list");

        if (null == rankList) {
            return Optional.of(result);
        }


        Elements rankRecords = rankList.select("li.rank-item");
        for (Element rankRecord : rankRecords) {
            VideoRecordVO record = Converter.toVideoRecordVO(rankRecord);
            result.add(record);
        }
        return Optional.of(result);
    }
}
