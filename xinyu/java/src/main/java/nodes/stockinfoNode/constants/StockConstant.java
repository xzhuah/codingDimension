package nodes.stockinfoNode.constants;

/**
 * Created by Xinyu Zhu on 2020/11/6, 23:44
 * nodes.stockinfoNode.constants in codingDimensionTemplate
 */
public class StockConstant {

    public static final String DEFAULT_DATABASE = "stock_data";

    public static final String PRICE_COLLECTION = "daily_price";
    public static final String SYMBOL_COLLECTION = "stock_symbol";

    public static final String CHINESE_PRICE_COLLECTION = "daily_price_china";
    public static final String CHINESE_SYMBOL_COLLECTION = "company_info_china";

    public static final int DEFAULT_DELTA_IN_DAY_FOR_UPDATE = 1;

    // When set OVERRIDE_WHEN_UPDATE to be true, we override all record when encounter duplication, otherwise, ignore
    public static final boolean OVERRIDE_WHEN_UPDATE = false;


}
