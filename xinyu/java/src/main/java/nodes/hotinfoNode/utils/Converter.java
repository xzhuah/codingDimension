package nodes.hotinfoNode.utils;

import com.google.gson.JsonObject;
import common.io.web.utils.RequestBuilder;
import common.time.TimeClient;
import common.time.TimeConstant;
import nodes.hotinfoNode.constants.WebsiteConstant;
import nodes.hotinfoNode.models.BilibiliAnalyzableVideoRecord;
import nodes.hotinfoNode.models.RankingRuleVO;
import nodes.hotinfoNode.models.VideoRecordVO;
import org.apache.http.client.methods.HttpUriRequest;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Xinyu Zhu on 6/30/2020, 10:20 PM
 */
public class Converter {
    public static HttpUriRequest toGetHttpUriRequest(String url) throws Exception {
        return RequestBuilder.buildHttpGet(url, null, WebsiteConstant.DEFAULT_HEADER);
    }

    public static VideoRecordVO toVideoRecordVO(Element element) throws Exception {
        VideoRecordVO.Builder builder = VideoRecordVO.custom();

        Element numDiv = element.selectFirst("div.num");
        Element imgDiv = element.selectFirst("div.img");
        Element infoDiv = element.selectFirst("div.info");
        Element detailDiv = element.selectFirst("div.detail");
        Element ptsDiv = element.selectFirst("div.pts");

        if (null != numDiv) {
            try {
                builder.setRank(Integer.parseInt(numDiv.text()));
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

        if (null != imgDiv) {
            Element ele = imgDiv.getElementsByTag("a").first();
            if (null != ele) {
                builder.setVideoUrl(ele.attr("href"));
                Element imgUrlEle = ele.getElementsByTag("img").first();
                if (null != imgUrlEle) {
                    builder.setImgUrl(imgUrlEle.attr("src"));
                }
            }
        }

        if (null != infoDiv) {
            Element ele = infoDiv.getElementsByTag("a").first();
            if (null != ele) {
                builder.setTitle(ele.text()).setVideoUrl(ele.attr("href"));
            }
            Element pgcEle = infoDiv.select("div.pgc-info").first();
            if (null != pgcEle) {
                builder.setDescription(pgcEle.text());
            }
        }

        if (null != detailDiv) {
            Elements eles = detailDiv.getElementsByTag("span");
            for (Element ele : eles) {
                Set<String> classname = ele.getElementsByTag("i").first().classNames();
                if (classname.contains("play")) {
                    builder.setPalyCount(ele.text());
                } else if (classname.contains("view")) {
                    builder.setCommentCount(ele.text());
                } else if (classname.contains("author")) {
                    builder.setAuthor(ele.text());
                } else if (classname.contains("fav")) {
                    builder.setLikeCount(ele.text());
                }
            }

        }

        if (null != ptsDiv) {
            Element ele = ptsDiv.getElementsByTag("div").last();
            if (null != ele) {
                try {
                    builder.setScore(Integer.parseInt(ele.text()));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }

        }

        return builder.build();
    }


    public static String toBilibiliVideoId(String videoLink) {
        String[] contents = videoLink.split("/");
        return contents[contents.length - 1];
    }


    public static JsonObject toJsonObject(VideoRecordVO videoRecordVO, RankingRuleVO rankingRuleVO, long createTime) {
        // TODO maybe consider rewrite these field name as configurable parameters
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("排名类别", rankingRuleVO.getRankType().getCode());
        jsonObject.addProperty("标题", videoRecordVO.getTitle());
        jsonObject.addProperty("描述", videoRecordVO.getDescription());
        jsonObject.addProperty("播放数", videoRecordVO.getPalyCount());
        jsonObject.addProperty("评论数", videoRecordVO.getCommentCount());
        jsonObject.addProperty("点赞量", videoRecordVO.getLikeCount());
        jsonObject.addProperty("作者", videoRecordVO.getAuthor());
        // store id only, typical format is https://www.bilibili.com/video/BV1sz4y1X7ys where BV1sz4y1X7ys is the id
        jsonObject.addProperty("地址", toBilibiliVideoId(videoRecordVO.getVideoUrl()));
        jsonObject.addProperty("排名", videoRecordVO.getRank());
        jsonObject.addProperty("分数", videoRecordVO.getScore());
        // Not able to get yet
        // jsonObject.addProperty("封面",videoRecordVO.getImgUrl());

        jsonObject.addProperty("统计时间", TimeClient.timestampToString(createTime, TimeConstant.dateTimeFormat));

        return jsonObject;
    }


    public static JsonObject toJsonObject(BilibiliAnalyzableVideoRecord bilibiliAnalyzableVideoRecord) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("排名类别", bilibiliAnalyzableVideoRecord.getRankType());
        jsonObject.addProperty("标题", bilibiliAnalyzableVideoRecord.getTitle());
        jsonObject.addProperty("描述", bilibiliAnalyzableVideoRecord.getDescription());
        jsonObject.addProperty("播放数", bilibiliAnalyzableVideoRecord.getPlay());
        jsonObject.addProperty("评论数", bilibiliAnalyzableVideoRecord.getComment());
        jsonObject.addProperty("点赞量", bilibiliAnalyzableVideoRecord.getLike());
        jsonObject.addProperty("作者", bilibiliAnalyzableVideoRecord.getAuthor());
        // store id only, typical format is https://www.bilibili.com/video/BV1sz4y1X7ys where BV1sz4y1X7ys is the id
        jsonObject.addProperty("地址", bilibiliAnalyzableVideoRecord.getVideoAddress());
        jsonObject.addProperty("排名", bilibiliAnalyzableVideoRecord.getRank());
        jsonObject.addProperty("分数", bilibiliAnalyzableVideoRecord.getScore());
        // Not able to get yet
        // jsonObject.addProperty("封面",videoRecordVO.getImgUrl());

        jsonObject.addProperty("统计时间", bilibiliAnalyzableVideoRecord.getRecordTime());

        return jsonObject;
    }

    public static BilibiliAnalyzableVideoRecord toBilibiliAnalyzableVideoRecord(VideoRecordVO videoRecordVO, RankingRuleVO rankingRuleVO, long createTime) {
        BilibiliAnalyzableVideoRecord bilibiliAnalyzableVideoRecord = new BilibiliAnalyzableVideoRecord();
        bilibiliAnalyzableVideoRecord.setRankType(rankingRuleVO.getRankType().getCode());
        bilibiliAnalyzableVideoRecord.setTitle(videoRecordVO.getTitle());
        bilibiliAnalyzableVideoRecord.setDescription(videoRecordVO.getDescription());
        try {
            bilibiliAnalyzableVideoRecord.setPlay(toInteger(videoRecordVO.getPalyCount()));
        } catch (Exception e) {
            bilibiliAnalyzableVideoRecord.setPlay(-1);
        }
        try {
            bilibiliAnalyzableVideoRecord.setComment(toInteger(videoRecordVO.getCommentCount()));
        } catch (Exception e) {
            bilibiliAnalyzableVideoRecord.setComment(-1);
        }
        try {
            bilibiliAnalyzableVideoRecord.setLike(toInteger(videoRecordVO.getLikeCount()));
        } catch (Exception e) {
            bilibiliAnalyzableVideoRecord.setLike(-1);
        }
        bilibiliAnalyzableVideoRecord.setAuthor(videoRecordVO.getAuthor());
        bilibiliAnalyzableVideoRecord.setVideoAddress(toBilibiliVideoId(videoRecordVO.getVideoUrl()));
        try {
            bilibiliAnalyzableVideoRecord.setRank(videoRecordVO.getRank());
        } catch (Exception e) {
            bilibiliAnalyzableVideoRecord.setRank(-1);
        }
        try {
            bilibiliAnalyzableVideoRecord.setScore(videoRecordVO.getScore());
        } catch (Exception e) {
            bilibiliAnalyzableVideoRecord.setScore(-1);
        }

        // Record once per day, so keep the timestamp to date level
        try {
            bilibiliAnalyzableVideoRecord.setRecordTime(TimeClient.castToDateTimestamp(createTime));
        } catch (ParseException e) {
            // This will never happen
            e.printStackTrace();
            bilibiliAnalyzableVideoRecord.setRecordTime(System.currentTimeMillis());
        }

        return bilibiliAnalyzableVideoRecord;
    }

    public static List<BilibiliAnalyzableVideoRecord> toBilibiliAnalyzableVideoRecordList(Map<RankingRuleVO, List<VideoRecordVO>> allResult, long createTime) {
        List<BilibiliAnalyzableVideoRecord> allRecord = new ArrayList<>();
        for (Map.Entry<RankingRuleVO, List<VideoRecordVO>> entry : allResult.entrySet()) {
            RankingRuleVO ruleVO = entry.getKey();
            List<VideoRecordVO> videoRecordVOS = entry.getValue();
            for (VideoRecordVO videoRecordVO : videoRecordVOS) {
                allRecord.add(Converter.toBilibiliAnalyzableVideoRecord(videoRecordVO, ruleVO, createTime));
            }
        }
        return allRecord;
    }


    public static List<JsonObject> toJsonObjectList(Map<RankingRuleVO, List<VideoRecordVO>> allResult, long createTime) {
        List<JsonObject> allRecord = new ArrayList<>();
        for (Map.Entry<RankingRuleVO, List<VideoRecordVO>> entry : allResult.entrySet()) {
            RankingRuleVO ruleVO = entry.getKey();
            List<VideoRecordVO> videoRecordVOS = entry.getValue();
            for (VideoRecordVO videoRecordVO : videoRecordVOS) {
                allRecord.add(Converter.toJsonObject(videoRecordVO, ruleVO, createTime));
            }
        }
        return allRecord;
    }

    public static Integer toInteger(String string) {
        if (null == string || string.isEmpty()) {
            return 0;
        } else {
            double number;
            try {
                switch (string.substring(string.length() - 1, string.length())) {
                    case "万":
                        number = Double.parseDouble(string.substring(0, string.length() - 1));
                        number *= 10000.0;
                        break;
                    case "亿":
                        number = Double.parseDouble(string.substring(0, string.length() - 1));
                        number *= 100000000.0;
                        break;
                    default:
                        number = Double.parseDouble(string);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(string + " is unable to be converted to an Integer");
                return 0;
            }
            return (int) number;
        }
    }

}
