package nodes.stockinfoNode.models;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Xinyu Zhu on 2020/12/2, 23:46
 * nodes.stockinfoNode.models in codingDimensionTemplate
 */
@Data
@NoArgsConstructor
public class ChineseStockCompanyPOJO extends StockCompanyPOJO{
    @Override
    public int getEmployee() {
        throw new RuntimeException("Field Not Support in ChineseStockCompanyPOJO");
    }

    @Override
    public long getMarket() {
        throw new RuntimeException("Field Not Support in ChineseStockCompanyPOJO");
    }

    @Override
    public String getExchange() {
        throw new RuntimeException("Field Not Support in ChineseStockCompanyPOJO");
    }

    @Override
    public String getSector() {
        throw new RuntimeException("Field Not Support in ChineseStockCompanyPOJO");
    }

    @Override
    public void setMarket(long market) {
        throw new RuntimeException("Field Not Support in ChineseStockCompanyPOJO");
    }

    @Override
    public void setEmployee(int employee) {
        throw new RuntimeException("Field Not Support in ChineseStockCompanyPOJO");
    }

    @Override
    public void setExchange(String exchange) {
        throw new RuntimeException("Field Not Support in ChineseStockCompanyPOJO");
    }

    @Override
    public void setSector(String sector) {
        throw new RuntimeException("Field Not Support in ChineseStockCompanyPOJO");
    }
}
