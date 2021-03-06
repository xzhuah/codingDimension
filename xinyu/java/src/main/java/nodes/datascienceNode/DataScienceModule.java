package nodes.datascienceNode;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import nodes.datascienceNode.stockInfo.StockFilterService;
import nodes.datascienceNode.stockInfo.StockPriceInfoJsonify;
import nodes.datascienceNode.stockInfo.impl.StockCompanyAnalysisImpl;
import nodes.datascienceNode.stockInfo.impl.StockFilterServiceImpl;
import nodes.datascienceNode.stockInfo.impl.StockPriceAnalysisImpl;
import nodes.datascienceNode.stockInfo.impl.StockPriceInfoJsonifyImpl;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/14, 19:20
 * nodes.datascienceNode in codingDimensionTemplate
 */
public class DataScienceModule extends AbstractModule {

    @Override
    protected void configure() {
        super.configure();

        // For stockInfo
        bind(new TypeLiteral<FeatureAnalysis<StockCompanyPOJO, String>>() {
        }).to(StockCompanyAnalysisImpl.class);
        bind(new TypeLiteral<FeatureAnalysis<List<StockDailyRecordPOJO>, String>>() {
        }).to(StockPriceAnalysisImpl.class);
        bind(StockPriceInfoJsonify.class).to(StockPriceInfoJsonifyImpl.class);
        bind(StockFilterService.class).to(StockFilterServiceImpl.class);
    }
}
