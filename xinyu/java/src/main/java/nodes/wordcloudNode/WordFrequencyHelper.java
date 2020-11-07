package nodes.wordcloudNode;

import com.kennycason.kumo.WordFrequency;

import java.io.IOException;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/6, 22:16
 * nodes.wordcloudNode in codingDimensionTemplate
 *
 * This helper class interface helps to count word frequency, generate List<WordFrequency> which can be directly
 * used to generate word cloud
 */
public interface WordFrequencyHelper {
    List<WordFrequency> getWordFrequencyFromString(String string);

    List<WordFrequency> getWordFrequencyFromFile(String filename) throws IOException;

    // How many result in list do you want
    void setWordFrequenciesToReturn(int wordFrequenciesToReturn);

    // Min word length to be count as a word
    void setMinWordLength(int length);

    // Max word length to be count as a word
    void setMaxWordLength(int length);

    void setStopword(List<String> stopword);
}
