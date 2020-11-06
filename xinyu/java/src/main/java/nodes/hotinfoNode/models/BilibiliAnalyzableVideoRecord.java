package nodes.hotinfoNode.models;

import com.google.gson.JsonObject;
import common.time.TimeClient;
import common.time.TimeConstant;
import nodes.hotinfoNode.utils.Converter;
import org.bson.types.ObjectId;

import java.text.ParseException;

public class BilibiliAnalyzableVideoRecord {

    private ObjectId id;

    private String videoAddress;
    private String title;

    private String description;
    private String author;

    private String rankType;

    private Integer rank;
    private Integer score;

    private Integer like;
    private Integer play;
    private Integer comment;

    private Long recordTime;

    public BilibiliAnalyzableVideoRecord() {

    }

    public BilibiliAnalyzableVideoRecord(final JsonObject jsonObject) {

        videoAddress = jsonObject.get("地址").getAsString();
        title = jsonObject.get("标题").getAsString();
        description = jsonObject.get("描述").getAsString();
        author = jsonObject.get("作者").getAsString();
        rankType = jsonObject.get("排名类别").getAsString();
        try {
            rank = jsonObject.get("排名").getAsInt();
        } catch (Exception e) {
            rank = -1;
        }
        try {
            score = jsonObject.get("分数").getAsInt();
        } catch (Exception e) {
            score = -1;
        }
        try {
            like = Converter.toInteger(jsonObject.get("点赞量").getAsString());
        } catch (Exception e) {
            like = -1;
        }
        try {
            play = Converter.toInteger(jsonObject.get("播放数").getAsString());
        } catch (Exception e) {
            play = -1;
        }
        try {
            comment = Converter.toInteger(jsonObject.get("评论数").getAsString());
        } catch (Exception e) {
            comment = -1;
        }

        try {
            recordTime = TimeClient.stringToTimestamp(jsonObject.get("统计时间").getAsString(), TimeConstant.dateTimeFormat);
        } catch (ParseException e) {
            // e.printStackTrace();
            recordTime = 0L;
        }
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getVideoAddress() {
        return videoAddress;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getRankType() {
        return rankType;
    }

    public Integer getRank() {
        return rank;
    }

    public Integer getScore() {
        return score;
    }

    public Integer getLike() {
        return like;
    }

    public Integer getPlay() {
        return play;
    }

    public Integer getComment() {
        return comment;
    }

    public Long getRecordTime() {
        return recordTime;
    }

    public void setVideoAddress(String videoAddress) {
        this.videoAddress = videoAddress;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setRankType(String rankType) {
        this.rankType = rankType;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public void setPlay(Integer play) {
        this.play = play;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public void setRecordTime(Long recordTime) {
        this.recordTime = recordTime;
    }

    @Override
    public String toString() {
        return "BilibiliAnalyzableVideoRecord{" +
                "videoAddress='" + videoAddress + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", rankType='" + rankType + '\'' +
                ", rank=" + rank +
                ", score=" + score +
                ", like=" + like +
                ", play=" + play +
                ", comment=" + comment +
                ", recordTime=" + recordTime +
                '}';
    }
}