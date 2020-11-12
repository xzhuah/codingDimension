package nodes.stockinfoNode.crawler;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Created by Xinyu Zhu on 2020/11/7, 2:07
 * nodes.stockinfoNode.crawler in codingDimensionTemplate
 */
public interface AlphavantageCrawler<T> {
    void addSymbolToQueue(String symbol) throws Exception;

    default void addSymbolsToQueue(Collection<String> symbols) {
        for (String symbol : symbols) {
            try {
                addSymbolToQueue(symbol);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Skipped " + symbol);
            }
        }
    }

    Future<Optional<T>> getResultFuture(String symbol) throws Exception;

    void shutDown();
}
