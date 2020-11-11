package nodes.wordcloudNode.impls;

import com.google.inject.Inject;
import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.bg.Background;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.FontScalar;
import com.kennycason.kumo.palette.ColorPalette;
import com.kennycason.kumo.wordstart.WordStartStrategy;
import nodes.NodeModule;
import nodes.wordcloudNode.WordCloudGenerator;
import nodes.wordcloudNode.WordFrequencyHelper;
import nodes.wordcloudNode.constants.StyleConstant;

import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/6, 22:27
 * nodes.wordcloudNode.impls in codingDimensionTemplate
 */
public class WordCloudGeneratorImpl implements WordCloudGenerator {
    private WordFrequencyHelper wordFrequencyHelper;
    private WordCloud wordCloud;
    private String outputFile;

    @Inject
    public WordCloudGeneratorImpl(WordFrequencyHelper wordFrequencyHelper) {
        // You have to change code here to modify the width and height
        this(600, 600, wordFrequencyHelper);
    }

    public WordCloudGeneratorImpl() {
        this(600, 600);
    }

    public WordCloudGeneratorImpl(int width, int height) {
        this(width, height, new WordFrequencyHelperImpl());
    }

    public WordCloudGeneratorImpl(int width, int height, WordFrequencyHelper wordFrequencyHelper) {
        this.wordFrequencyHelper = wordFrequencyHelper;
        wordCloud = new WordCloud(new Dimension(width, height), CollisionMode.PIXEL_PERFECT);
        // This is a font that can support Chinese and English, other font might not be able to support Chinese
        wordCloud.setKumoFont(new KumoFont(new java.awt.Font("STSong-Light", 2, 18)));
        outputFile = "wordCloud.png";
    }

    public static void main(String[] args) throws IOException {
        WordCloudGenerator wordCloudGenerator = NodeModule.getGlobalInjector().getInstance(WordCloudGenerator.class);
        wordCloudGenerator.setBackgroudColor(new Color(0xF1F1F1));
        wordCloudGenerator.setMaxWordToDraw(300);
        wordCloudGenerator.setBackgroudShape(StyleConstant.DEFAULT_CIRCLE_BACKGROUND);
        wordCloudGenerator.setWordColor(StyleConstant.LINEAR_GRADIENT);
        wordCloudGenerator.setWordScalar(StyleConstant.DEFAULT_SQRT_FONT_SCALAR);
        wordCloudGenerator.setWordStartStrategy(StyleConstant.CENTER_WORD);

        wordCloudGenerator.setOutputFile("wordCloudExample.png");
        wordCloudGenerator.drawForFile("wordCloudExample.txt");

    }

    @Override
    public void setWordCloud(WordCloud wordCloud) {
        this.wordCloud = wordCloud;
    }

    @Override
    public void setMaxWordToDraw(int maxWordToDraw) {
        this.wordFrequencyHelper.setWordFrequenciesToReturn(maxWordToDraw);
    }

    @Override
    public void setBackgroudColor(Color color) {
        wordCloud.setBackgroundColor(color);
    }

    @Override
    public void setPadding(int padding) {
        wordCloud.setPadding(padding);
    }

    @Override
    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    // Be careful if you are drawing word cloud for Chinese language, check if your font can support it
    @Override
    public void setFont(Font font) {
        wordCloud.setKumoFont(new KumoFont(font));
    }

    // We provide some pre-set value for these method in constant, we can use them
    @Override
    public void setBackgroudShape(Background background) {
        wordCloud.setBackground(background);
    }

    @Override
    public void setWordColor(ColorPalette colorPalette) {
        wordCloud.setColorPalette(colorPalette);
    }

    @Override
    public void setWordScalar(FontScalar fontScalar) {
        wordCloud.setFontScalar(fontScalar);
    }

    @Override
    public void setWordStartStrategy(WordStartStrategy wordStartStrategy) {
        wordCloud.setWordStartStrategy(wordStartStrategy);
    }

    // These are interfaces
    @Override
    public void drawForText(String text) {
        wordCloud.build(this.wordFrequencyHelper.getWordFrequencyFromString(text));
        wordCloud.writeToFile(this.outputFile);
    }

    @Override
    public void drawForFile(String filename) throws IOException {
        wordCloud.build(this.wordFrequencyHelper.getWordFrequencyFromFile(filename));
        wordCloud.writeToFile(this.outputFile);
    }

    @Override
    public void setStopword(List<String> stopword) {
        this.wordFrequencyHelper.setStopword(stopword);
    }
}
