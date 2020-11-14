package nodes.stockinfoNode.db;

import nodes.stockinfoNode.models.StockCompanyPOJO;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/11, 20:47
 * nodes.stockinfoNode in codingDimensionTemplate
 *
 * This interface provide all function to update stock price to database
 * You can make a full update for all companies, or a partial update for a specific company
 * You can check whether an update is needed for each company, or get a list of company that might need update
 */
public interface PriceAutoUpdater {

    // automatically update for all companies (for only those out of dated)
    default void fullUpdate() {
        update(getOutOfDateCompany());
    }

    default void update(StockCompanyPOJO company) {
        update(List.of(company));
    }

    // force update a list company, don't check out of date
    void update(List<StockCompanyPOJO> companies);

    // check whether a company is out of date
    boolean isOutOfDate(StockCompanyPOJO company);

    // return a list of company that are out of date
    List<StockCompanyPOJO> getOutOfDateCompany();

}
