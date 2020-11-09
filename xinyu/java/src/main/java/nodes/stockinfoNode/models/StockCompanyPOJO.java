package nodes.stockinfoNode.models;

import common.io.web.models.ResponseProcessResult;
import org.bson.types.ObjectId;

/**
 * Created by Xinyu Zhu on 2020/11/8, 21:31
 * nodes.stockinfoNode.models in codingDimensionTemplate
 */
public class StockCompanyPOJO implements ResponseProcessResult {
    // This is POJO indicator: real primary key
    private ObjectId id;

    private String symbol;
    private String name;
    private String exchange;
    private String sector;
    private String industry;
    private String country;

    public StockCompanyPOJO() {

    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "StockCompanyPOJO{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", exchange='" + exchange + '\'' +
                ", sector='" + sector + '\'' +
                ", industry='" + industry + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
