package nodes.datascienceNode.stockInfo;

import common.time.TimeInterval;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/15, 16:43
 * nodes.datascienceNode.stockInfo in codingDimensionTemplate
 */
public interface StockTimeSeriesFeatureAnalysis<T> extends StockFeatureAnalysis<T> {

    public void printReportForTarget(TimeInterval timeInterval);

    public void printReportForTarget(List<String> companySymbol, TimeInterval timeInterval, boolean sortByPrimaryFeature);

    default void printReportForTarget(List<String> companySymbol, boolean sortByPrimaryFeature) {
        printReportForTarget(companySymbol, TimeInterval.getUpToNowInterval(), sortByPrimaryFeature);
    }
}
