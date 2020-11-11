package nodes.wordcloudNode;

import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.bg.Background;
import com.kennycason.kumo.font.scale.FontScalar;
import com.kennycason.kumo.palette.ColorPalette;
import com.kennycason.kumo.wordstart.WordStartStrategy;

import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/6, 23:23
 * nodes.wordcloudNode in codingDimensionTemplate
 */
public interface WordCloudGenerator {
    void setWordCloud(WordCloud wordCloud);

    void setMaxWordToDraw(int maxWordToDraw);

    void setBackgroudColor(Color color);

    void setPadding(int padding);

    void setOutputFile(String outputFile);

    // Be careful if you are drawing word cloud for Chinese language, check if your font can support it
    void setFont(Font font);

    // We provide some pre-set value for these method in constant, we can use them
    void setBackgroudShape(Background background);

    void setWordColor(ColorPalette colorPalette);

    void setWordScalar(FontScalar fontScalar);

    void setWordStartStrategy(WordStartStrategy wordStartStrategy);

    // These are interfaces
    void drawForText(String text);

    void drawForFile(String filename) throws IOException;

    void setStopword(List<String> stopword);
}
