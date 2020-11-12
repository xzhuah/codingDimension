package nodes.stockinfoNode.impls;

import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import nodes.stockinfoNode.PriceAutoUpdater;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.querier.StockPriceDBService;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Xinyu Zhu on 2020/11/6, 23:50
 * nodes.stockinfoNode.impls in codingDimensionTemplate
 */
public class ZeroDelayPriceAutoUpdaterImpl implements PriceAutoUpdater {

    private final StockPriceDBService stockPriceDBService;

    @Inject
    private ZeroDelayPriceAutoUpdaterImpl(StockPriceDBService stockPriceDBService) {
        this.stockPriceDBService = stockPriceDBService;
        System.out.println("ZeroDelay");
    }


    @Override
    public void update(List<StockCompanyPOJO> companies) {

    }

    @Override
    public boolean isOutOfDate(StockCompanyPOJO company) {
        return false;
    }

    @Override
    public List<StockCompanyPOJO> getOutOfDateCompany() {
        return null;
    }

}
