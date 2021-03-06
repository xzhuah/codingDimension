package nodes.stockinfoNode;

import com.google.gson.JsonArray;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import common.io.web.facade.ResponseProcessor;
import common.io.web.impl.processors.ResponseToJsonArrayProcessorImpl;
import nodes.crawlerNode.AutoCoolDownCrawler;
import nodes.crawlerNode.BaseCrawler;
import nodes.stockinfoNode.crawler.AlphavantageCrawler;
import nodes.stockinfoNode.crawler.StockSymbolCrawler;
import nodes.stockinfoNode.crawler.facade.impl.CompanyInfoProcessor;
import nodes.stockinfoNode.crawler.facade.impl.DailyPriceProcessor;
import nodes.stockinfoNode.crawler.impls.AlphavantageCrawlerImpl;
import nodes.stockinfoNode.crawler.impls.AlphavantageSymbolCrawlerImpl;
import nodes.stockinfoNode.crawler.impls.StockSymbolCrawlerImpl;
import nodes.stockinfoNode.db.ChineseStockInfoDBService;
import nodes.stockinfoNode.db.PriceAutoUpdater;
import nodes.stockinfoNode.db.StockInfoDBService;
import nodes.stockinfoNode.db.StockSymbolUpdater;
import nodes.stockinfoNode.db.impls.ChineseStockInfoDBServiceImpl;
import nodes.stockinfoNode.db.impls.DeltaDelayPriceAutoUpdaterImpl;
import nodes.stockinfoNode.db.impls.StockInfoDBServiceImpl;
import nodes.stockinfoNode.db.impls.StockSymbolUpdaterImpl;
import nodes.stockinfoNode.impls.StockInfoServiceImpl;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.List;

import static nodes.stockinfoNode.crawler.constants.WebsiteConstant.COOL_DOWN_TIME_MAX;
import static nodes.stockinfoNode.crawler.constants.WebsiteConstant.COOL_DOWN_TIME_MIN;

public class StockInfoModule extends AbstractModule {

    @Override
    protected void configure() {
        CompanyInfoProcessor companyInfoProcessor = new CompanyInfoProcessor();
        DailyPriceProcessor dailyPriceProcessor = new DailyPriceProcessor();
        ResponseProcessor<JsonArray> jsonArrayResponseProcessor = new ResponseToJsonArrayProcessorImpl();

        bind(new TypeLiteral<ResponseProcessor<StockCompanyPOJO>>() {
        }).toInstance(companyInfoProcessor);

        bind(new TypeLiteral<AutoCoolDownCrawler<StockCompanyPOJO>>() {
        }).toInstance(new AutoCoolDownCrawler<>(companyInfoProcessor, COOL_DOWN_TIME_MIN, COOL_DOWN_TIME_MAX, 0));

        bind(new TypeLiteral<ResponseProcessor<List<StockDailyRecordPOJO>>>() {
        }).toInstance(dailyPriceProcessor);

        bind(new TypeLiteral<AutoCoolDownCrawler<List<StockDailyRecordPOJO>>>() {
        }).toInstance(new AutoCoolDownCrawler<>(dailyPriceProcessor, COOL_DOWN_TIME_MIN, COOL_DOWN_TIME_MAX, 0));

        bind(new TypeLiteral<BaseCrawler<JsonArray>>() {
        }).toInstance(new BaseCrawler<>(jsonArrayResponseProcessor));

        bind(new TypeLiteral<AlphavantageCrawler<StockCompanyPOJO>>() {
        }).to(AlphavantageSymbolCrawlerImpl.class);
        bind(new TypeLiteral<AlphavantageCrawler<List<StockDailyRecordPOJO>>>() {
        }).to(AlphavantageCrawlerImpl.class);
        bind(StockSymbolCrawler.class).to(StockSymbolCrawlerImpl.class);

        bind(StockInfoDBService.class).to(StockInfoDBServiceImpl.class);
        bind(ChineseStockInfoDBService.class).to(ChineseStockInfoDBServiceImpl.class);

        bind(PriceAutoUpdater.class).to(DeltaDelayPriceAutoUpdaterImpl.class);
        bind(StockSymbolUpdater.class).to(StockSymbolUpdaterImpl.class);
        bind(StockInfoService.class).to(StockInfoServiceImpl.class);
    }
}