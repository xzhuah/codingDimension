package nodes.wordcloudNode;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/5, 23:07
 * nodes.wordcloudNode in codingDimensionTemplate
 *
 * This class is wrote according to https://github.com/kennycason/kumo
 */
public class WordCloudGenerator {
    static String example = "Hi. Welcome to On Politics, your guide to the day in national politics. I’m Lisa Lerer, your host.\n" +
            "\n" +
            "Sign up here to get On Politics in your inbox every weekday.\n" +
            "\n" +
            "\n" +
            "ImageProcessing absentee ballots in Atlanta today.\n" +
            "Processing absentee ballots in Atlanta today.Credit...Lynsey Weatherspoon for The New York Times\n" +
            "ATLANTA — What the president of the United States did tonight wasn’t complicated but it was stunning, even after four long years of the politically extraordinary.\n" +
            "\n" +
            "President Trump attacked democracy.\n" +
            "\n" +
            "In his remarks tonight from the White House, Mr. Trump lied about the vote count, smeared his opponents and attempted to undermine the integrity of our electoral system.\n" +
            "\n" +
            "ADVERTISEMENT\n" +
            "\n" +
            "Continue reading the main story\n" +
            "\n" +
            "“If you count the legal votes, I win,” he said, before ticking off a litany of baseless claims about ways his campaign had supposedly been cheated by his opponents, nonpartisan poll workers and a vast conspiracy of technology companies and big business.\n" +
            "\n" +
            "But nothing is “rigged” or “stolen” or “illegal.” No one is “doing a lot of bad things.”\n" +
            "\n" +
            "Donald Trump is simply losing.\n" +
            "\n" +
            "Unlock more free articles.\n" +
            "Create an account or log in\n" +
            "And he’s apparently decided to try and take our system down with him.\n" +
            "\n" +
            "Joe Biden has been cutting into Mr. Trump’s lead, or expanding his own, in three of the four states that will decide the next president: Pennsylvania, Georgia and Nevada. Notably, in the state where Mr. Trump appears to be making gains — Arizona — the president seems to take little issue with the vote count.\n" +
            "\n" +
            "The votes that Mr. Trump calls “late” and “illegal” were postmarked by Election Day, making them valid. In Pennsylvania, the Republican-led state legislature wouldn’t allow poll workers to start counting mail ballots until Election Day. So now, they’re being counted.";
    public static void main(String[] args) throws IOException {
        List<String> text = new ArrayList<>();text.add(example);

//        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
//        frequencyAnalyzer.setWordFrequenciesToReturn(600);
//        frequencyAnalyzer.setMinWordLength(2);
//        frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());
        // For english
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        //final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(text);
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load("my_text_file.txt");
        final Dimension dimension = new Dimension(600, 600);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        wordCloud.setFontScalar(new SqrtFontScalar(12, 45));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("datarank_wordcloud_circle_sqrt_font.png");
    }
}
