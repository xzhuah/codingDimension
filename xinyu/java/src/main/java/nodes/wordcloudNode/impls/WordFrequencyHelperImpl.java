package nodes.wordcloudNode.impls;

import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import nodes.wordcloudNode.WordFrequencyHelper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/6, 22:18
 * nodes.wordcloudNode.impls in codingDimensionTemplate
 */
public class WordFrequencyHelperImpl implements WordFrequencyHelper {
    FrequencyAnalyzer frequencyAnalyzer;

    public WordFrequencyHelperImpl() {
        frequencyAnalyzer = new FrequencyAnalyzer();

        // Provide default setting
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(2);

        // Support both Chinese and English
        frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());
    }

    public WordFrequencyHelperImpl(FrequencyAnalyzer frequencyAnalyzer) {
        this.frequencyAnalyzer = frequencyAnalyzer;
    }

    // How many result in list do you want
    public void setWordFrequenciesToReturn(int wordFrequenciesToReturn) {
        frequencyAnalyzer.setWordFrequenciesToReturn(wordFrequenciesToReturn);
    }

    // Min word length to be count as a word
    public void setMinWordLength(int length) {
        frequencyAnalyzer.setMinWordLength(length);
    }

    // Max word length to be count as a word
    public void setMaxWordLength(int length) {
        frequencyAnalyzer.setMaxWordLength(length);
    }

    public void setStopword(List<String> stopword) {
        frequencyAnalyzer.setStopWords(stopword);
    }

    // functional method
    public List<WordFrequency> getWordFrequencyFromString(String string) {
        return frequencyAnalyzer.load(Arrays.asList(string.split(" ")));
    }

    public List<WordFrequency> getWordFrequencyFromFile(String filename) throws IOException {
        return frequencyAnalyzer.load(filename);
    }

}
