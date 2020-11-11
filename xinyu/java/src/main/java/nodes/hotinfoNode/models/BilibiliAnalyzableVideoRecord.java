package nodes.hotinfoNode.models;

import com.google.gson.JsonObject;
import common.time.TimeClient;
import common.time.TimeConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nodes.hotinfoNode.utils.Converter;
import org.bson.types.ObjectId;

import java.text.ParseException;

@Data
@ToString(exclude = "id")
@NoArgsConstructor
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

}