package nodes.hotinfoNode.crawler.facade;

import common.io.web.ResponseProcessor;
import common.io.web.constants.ValueConstant;
import nodes.hotinfoNode.models.VideoRecordListVO;
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

/**
 * Created by Xinyu Zhu on 7/1/2020, 12:09 PM
 * xinyu.hotinfo.biz.bilibili.facade in HotInfo
 */
public class ResponseToRankListProcessor implements ResponseProcessor {

    private static ResponseProcessor instance = null;

    private ResponseToRankListProcessor() {
    }

    public static ResponseProcessor getInstance() {
        if (instance == null) {
            synchronized (ResponseToRankListProcessor.class) {
                if (instance == null) {
                    instance = new ResponseToRankListProcessor();
                }
            }
        }
        return instance;
    }


    @Override
    public VideoRecordListVO process(CloseableHttpResponse response, String url) throws Exception {
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
            return new VideoRecordListVO(result);
        }


        Elements rankRecords = rankList.select("li.rank-item");
        for (Element rankRecord : rankRecords) {
            VideoRecordVO record = Converter.toVideoRecordVO(rankRecord);
            result.add(record);
        }
        return new VideoRecordListVO(result);
    }
}
