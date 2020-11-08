package nodes.wordcloudNode;

import nodes.wordcloudNode.constants.StyleConstant;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/8, 0:38
 * nodes.wordcloudNode in codingDimensionTemplate
 */
public interface WordCloudForWebpage {
    void setWordCloudGenerator(WordCloudGenerator wordCloudGenerator);

    default void drawForUrl(String url) throws Exception {
        drawForUrl(url, StyleConstant.DEFAULT_FILE_OUTPUT_PATH, StyleConstant.DEFAULT_WEIGHT_ON_HEADER);
    }

    default void drawForUrl(String url, String outputFile) throws Exception {
        drawForUrl(url, outputFile, StyleConstant.DEFAULT_WEIGHT_ON_HEADER);
    }

    default void drawForUrl(String url, int weightOnHeader) throws Exception {
        drawForUrl(url, StyleConstant.DEFAULT_FILE_OUTPUT_PATH, weightOnHeader);
    }

    void drawForUrl(String url, String outputFile, int weightOnHeader) throws Exception;

    void setStopword(List<String> stopword);

    void shutdown();
}
