package nodes.hotinfoNode.models;

import lombok.Builder;
import lombok.Value;

/**
 * Created by Xinyu Zhu on 6/30/2020, 5:36 AM
 */
@Value
@Builder
public class VideoRecordVO {
    private final String title;
    private final String description;
    private final String palyCount;
    private final String commentCount;
    private final String likeCount;
    private final String author;
    private final String videoUrl;
    private final String imgUrl;
    private final Integer rank;
    private final Integer score;
}
