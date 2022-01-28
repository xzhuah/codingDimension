package nodes.crawlerNode.apps.leetCode.models;

import lombok.Data;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2021/12/14, 23:41
 * nodes.crawlerNode.apps.leetCode.models in codingDimensionTemplate
 */
@Data
public class Problem {
    private int index;
    private int like;
    private int dislike;
    private int discuss;
    private int submission;
    private int accepted;
    private String level;
    private String url;
    private String title;
    private List<String> tags;
    private List<String> similar;
}
