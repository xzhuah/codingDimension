package nodes.stockinfoNode;

import com.google.gson.JsonArray;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import common.io.web.ResponseProcessor;
import common.io.web.impl.processors.ResponseToJsonArrayProcessorImpl;
import nodes.crawlerNode.BaseCrawler;
import nodes.stockinfoNode.crawler.AlphavantageCrawler;
import nodes.stockinfoNode.crawler.StockSymbolCrawler;
import nodes.stockinfoNode.crawler.facade.CompanyInfoProcessor;
import nodes.stockinfoNode.crawler.facade.DailyPriceProcessor;
import nodes.stockinfoNode.crawler.impls.AlphavantageCrawlerImpl;
import nodes.stockinfoNode.crawler.impls.AlphavantageSymbolCrawlerImpl;
import nodes.stockinfoNode.crawler.impls.StockSymbolCrawlerImpl;
import nodes.stockinfoNode.impls.DeltaDelayPriceAutoUpdaterImpl;
import nodes.stockinfoNode.impls.StockPriceServiceImpl;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;
import nodes.stockinfoNode.querier.StockPriceDBService;
import nodes.stockinfoNode.querier.impls.StockPriceDBServiceImpl;

import java.util.List;

public class StockInfoModule extends AbstractModule {

    @Override
    protected void configure() {
        CompanyInfoProcessor companyInfoProcessor = new CompanyInfoProcessor();
        DailyPriceProcessor dailyPriceProcessor = new DailyPriceProcessor();
        ResponseProcessor<JsonArray> jsonArrayResponseProcessor = new ResponseToJsonArrayProcessorImpl();

        bind(new TypeLiteral<ResponseProcessor<StockCompanyPOJO>>() {
        }).toInstance(companyInfoProcessor);

        bind(new TypeLiteral<BaseCrawler<StockCompanyPOJO>>() {
        }).toInstance(new BaseCrawler<>(companyInfoProcessor));

        bind(new TypeLiteral<ResponseProcessor<List<StockDailyRecordPOJO>>>() {
        }).toInstance(dailyPriceProcessor);

        bind(new TypeLiteral<BaseCrawler<List<StockDailyRecordPOJO>>>() {
        }).toInstance(new BaseCrawler<>(dailyPriceProcessor));

        bind(new TypeLiteral<BaseCrawler<JsonArray>>() {
        }).toInstance(new BaseCrawler<>(jsonArrayResponseProcessor));

        bind(new TypeLiteral<AlphavantageCrawler<StockCompanyPOJO>>(){}).to(AlphavantageSymbolCrawlerImpl.class);
        bind(new TypeLiteral<AlphavantageCrawler<List<StockDailyRecordPOJO>>>(){}).to(AlphavantageCrawlerImpl.class);
        bind(StockSymbolCrawler.class).to(StockSymbolCrawlerImpl.class);

        bind(StockPriceDBService.class).to(StockPriceDBServiceImpl.class);
        bind(PriceAutoUpdater.class).to(DeltaDelayPriceAutoUpdaterImpl.class);
        bind(StockPriceService.class).to(StockPriceServiceImpl.class);
    }
}