package nodes.crawlerNode.apps.leetCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2021/12/14, 23:34
 * nodes.crawlerNode.apps in codingDimensionTemplate
 */
public class LeetCodeCrawler {
    private static final String ROOT = "https://leetcode.com/problemset/all/?page=";
    private static final int MAX_PAGE = 43;

    private static List<String> getAllRoots() {
        List<String> allRootUrls = new ArrayList<>(MAX_PAGE);
        for (int i = 1; i < MAX_PAGE + 1; i++) {
            allRootUrls.add(ROOT + i);
        }
        return allRootUrls;
    }
}
