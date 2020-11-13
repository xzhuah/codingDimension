package nodes.crawlerNode;

import common.io.web.ResponseProcessor;
import nodes.crawlerNode.constants.CrawlerConstant;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Xinyu Zhu on 2020/11/12, 23:43
 * nodes.crawlerNode in codingDimensionTemplate
 * <p>
 * This crawler provides cool down functions on BaseCrawler, means it will block itself for a while if a certain
 * condition is meet
 */
public class AutoCoolDownCrawler<T> extends BaseCrawler<T> {

    private long coolDownMinTime;
    private long coolDownMaxTime;
    private int coolDownInterval;

    private int requestTimeRecord;
    private int lastRequestTime;

    /**
     * 这个爬虫在执行一定次数的请求后会自动冷却一段时间, 防止被网站限流
     *
     * @param responseProcessor responseProcessor
     * @param coolDownMinTime   每次冷却的时间下限(毫秒)
     * @param coolDownMaxTime   每次冷却的时间上限(毫秒)
     * @param coolDownInterval  每隔多少次请求, 才需要执行冷却操作
     */
    public AutoCoolDownCrawler(ResponseProcessor<T> responseProcessor, long coolDownMinTime, long coolDownMaxTime, int coolDownInterval) {
        super(responseProcessor);
        this.coolDownMinTime = coolDownMinTime;
        this.coolDownMaxTime = coolDownMaxTime;
        this.coolDownInterval = coolDownInterval;
        requestTimeRecord = 0;
        lastRequestTime = 0;
    }

    public AutoCoolDownCrawler(ResponseProcessor<T> responseProcessor) {
        this(responseProcessor, CrawlerConstant.DEFAULT_COOL_DOWN_MIN_TIME, CrawlerConstant.DEFAULT_COOL_DOWN_MAX_TIME, CrawlerConstant.DEFAULT_COOL_DOWN_INTERVAL);
    }

    @Override
    protected List<Future<Optional<T>>> getResultFuture(String tag, String url, Map<String, String> params) {
        requestTimeRecord += 1;
        if (needCoolDown()) {
            lastRequestTime = requestTimeRecord;
            coolDown();
        }
        return super.getResultFuture(tag, url, params);
    }

    private boolean needCoolDown() {
        return requestTimeRecord - lastRequestTime > coolDownInterval;
    }

    private long getRandomCoolDownTime() {
        return ThreadLocalRandom.current().nextLong(this.coolDownMinTime, this.coolDownMaxTime);
    }

    private void coolDown() {
        try {
            Thread.sleep(getRandomCoolDownTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public long getCoolDownMinTime() {
        return coolDownMinTime;
    }

    public void setCoolDownMinTime(long coolDownMinTime) {
        this.coolDownMinTime = coolDownMinTime;
    }

    public long getCoolDownMaxTime() {
        return coolDownMaxTime;
    }

    public void setCoolDownMaxTime(long coolDownMaxTime) {
        this.coolDownMaxTime = coolDownMaxTime;
    }

    public int getCoolDownInterval() {
        return coolDownInterval;
    }

    public void setCoolDownInterval(int coolDownInterval) {
        this.coolDownInterval = coolDownInterval;
    }
}
