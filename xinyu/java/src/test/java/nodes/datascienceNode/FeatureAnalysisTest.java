package nodes.datascienceNode;

import com.google.inject.Key;
import nodes.NodeModule;
import nodes.datascienceNode.stockInfo.facade.impl.EmployeeNumFeature;
import nodes.datascienceNode.stockInfo.facade.impl.MarketFeature;
import nodes.datascienceNode.stockInfo.facade.impl.MarketPerEmployeeFeature;
import nodes.datascienceNode.stockInfo.facade.impl.StockExpectedReturnFeature;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Xinyu Zhu on 2020/11/15, 4:53
 * nodes.datascienceNode in codingDimensionTemplate
 */
public class FeatureAnalysisTest {

    FeatureAnalysis<StockCompanyPOJO, String> companyAnalyser = NodeModule.getGlobalInjector().getInstance(new Key<>() {
    });
    FeatureAnalysis<List<StockDailyRecordPOJO>, String> priceAnalyser = NodeModule.getGlobalInjector().getInstance(new Key<>() {
    });

    @Test
    public void printCompanyReport() {
        companyAnalyser.setFeature(new MarketFeature(),
                new EmployeeNumFeature(),
                new MarketPerEmployeeFeature());

        companyAnalyser.printReportForTarget();
    }

    @Test
    public void printPriceReport() {
        int sampleDuration = 15;
        priceAnalyser.setFeature(new StockExpectedReturnFeature(sampleDuration, 0.01),
                new StockExpectedReturnFeature(sampleDuration, 0.015),
                new StockExpectedReturnFeature(sampleDuration, 0.02),
                new StockExpectedReturnFeature(sampleDuration, 0.025),
                new StockExpectedReturnFeature(sampleDuration, 0.03),
                new StockExpectedReturnFeature(sampleDuration, 0.035),
                new StockExpectedReturnFeature(sampleDuration, 0.04));

        priceAnalyser.printReportForTarget(List.of("VMW", "MSFT", "AMZN", "GOOG", "BABA"));

    }
}