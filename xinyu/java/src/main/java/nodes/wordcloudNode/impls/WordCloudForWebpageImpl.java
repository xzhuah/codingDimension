package nodes.wordcloudNode.impls;

import com.google.inject.Inject;
import nodes.NodeModule;
import nodes.crawlerNode.facade.WebpageCommonProcessor;
import nodes.crawlerNode.impl.WebpageCommonInfoCrawler;
import nodes.crawlerNode.models.WebpageCommonInfo;
import nodes.wordcloudNode.WordCloudForWebpage;
import nodes.wordcloudNode.WordCloudGenerator;
import nodes.wordcloudNode.constants.StyleConstant;

import java.util.List;

import static common.utils.ConditionChecker.checkStatus;

/**
 * Created by Xinyu Zhu on 2020/11/8, 0:18
 * nodes.wordcloudNode.integration in codingDimensionTemplate
 */
public class WordCloudForWebpageImpl implements WordCloudForWebpage {

    private WordCloudGenerator wordCloudGenerator;
    private WebpageCommonInfoCrawler webpageCommonInfoCrawler;


    @Inject
    public WordCloudForWebpageImpl(WordCloudGenerator wordCloudGenerator, WebpageCommonInfoCrawler webpageCommonInfoCrawler) {
        this.wordCloudGenerator = wordCloudGenerator;
        this.webpageCommonInfoCrawler = webpageCommonInfoCrawler;
    }

    public static void main(String[] args) throws Exception {
        WordCloudForWebpage wordCloudForWebpage = NodeModule.getGlobalInjector().getInstance(WordCloudForWebpage.class);

        WordCloudGenerator myWordCloudGenerator = NodeModule.getGlobalInjector().getInstance(WordCloudGenerator.class);
        myWordCloudGenerator.setMaxWordToDraw(400);
        myWordCloudGenerator.setPadding(4);
        myWordCloudGenerator.setWordColor(StyleConstant.BLUE_FAMILY);
        myWordCloudGenerator.setWordScalar(StyleConstant.DEFAULT_LINEAR_FONT_SCALAR);
        myWordCloudGenerator.setWordStartStrategy(StyleConstant.CENTER_WORD);
        myWordCloudGenerator.setStopword(StyleConstant.DEFAULT_STOP_WORDS);

        wordCloudForWebpage.setWordCloudGenerator(myWordCloudGenerator);


        //wordCloudForWebpage.drawForUrl("https://www.cnn.com/2020/11/07/politics/transition-biden-coronavirus-task-force/index.html");
        //wordCloudForWebpage.drawForUrl("https://www.bbc.com/zhongwen/simp/world-54858911");
        //wordCloudForWebpage.drawForUrl("https://www.google.com/search?q=google+guice+example&rlz=1C1CHBF_enUS913US913&oq=google&aqs=chrome.0.69i59j69i57j35i39j0i20i131i263i433i457j69i60l2j69i65l2.3527j0j4&sourceid=chrome&ie=UTF-8");
        wordCloudForWebpage.drawForUrl("https://www.bilibili.com/v/popular/rank/all");
        // wordCloudForWebpage.drawForUrl("https://www.alphavantage.co/query?function=OVERVIEW&symbol=IBM&apikey=demo");
        wordCloudForWebpage.shutdown();
    }

    @Override
    public void setWordCloudGenerator(WordCloudGenerator wordCloudGenerator) {
        this.wordCloudGenerator = wordCloudGenerator;
    }

    @Override
    public void drawForUrl(String url, String outputFile, int weightOnHeader) throws Exception {
        checkStatus(weightOnHeader >= 0 && weightOnHeader <= 10,
                "Your weight on header," + weightOnHeader + " exceeds the range 0~10");
        webpageCommonInfoCrawler.addJobToQueue(url);
        WebpageCommonInfo commonInfo = webpageCommonInfoCrawler.getResultFuture(url, null).get(0).get().get();
        StringBuilder webpageContent = new StringBuilder(commonInfo.getWebpageContent());
        wordCloudGenerator.setOutputFile(outputFile);

        commonInfo.getWebpageTitles().forEach(title -> {
            for (int i = 0; i < weightOnHeader - 1; i++) {
                webpageContent.append(" ").append(title);
            }
        });
        wordCloudGenerator.drawForText(webpageContent.toString());
    }

    @Override
    public void setStopword(List<String> stopword) {
        this.wordCloudGenerator.setStopword(stopword);
    }

    @Override
    public void shutdown() {
        this.webpageCommonInfoCrawler.shutDown();
    }


}
