package nodes.stockinfoNode.crawler;

import common.io.web.models.ResponseProcessResult;

import java.util.Collection;
import java.util.concurrent.Future;

/**
 * Created by Xinyu Zhu on 2020/11/7, 2:07
 * nodes.stockinfoNode.crawler in codingDimensionTemplate
 */
public interface AlphavantageCrawler {
    void addSymbolToQueue(String symbol) throws Exception;

    void addSymbolsToQueue(Collection<String> symbols);

    Future<ResponseProcessResult> getResultFuture(String symbol) throws Exception;

    void shutDown();
}
