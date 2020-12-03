import tushare as ts

from common.annotations.annotation import pre_check
from common.io.database.mongodb.MongoDBClient import get_collection
from common.io.file.ApiKeyClient import get_key
from common.time.TimeClient import get_current_timestamp_in_millisecond, timestamp_in_millisecond_to_str, \
    NUMERICAL_DATE_FORMAT, str_to_timestamp_in_millisecond

api_entry = None
company_info_collection = None
stock_price_collection = None


def lazy_init():
    global api_entry, company_info_collection, stock_price_collection
    if api_entry is None:
        ts.set_token(get_key("tuSharePro"))
        api_entry = ts.pro_api()
    if company_info_collection is None:
        company_info_collection = get_collection("stock_data", "company_info_china")
    if stock_price_collection is None:
        stock_price_collection = get_collection("stock_data", "daily_price_china")


@pre_check(lazy_init)
def update_company_info():
    code_to_name = {}
    hs300 = ts.get_hs300s()
    zz500 = ts.get_zz500s()
    for _, row in hs300.iterrows():
        code_to_name[row["code"]] = row["name"]
    for _, row in zz500.iterrows():
        code_to_name[row["code"]] = row["name"]
    print("已更新沪深300和中证500指数列表共", code_to_name.__len__(), "只股票")

    stock_info_list = []
    company_info = api_entry.stock_basic(exchange='', list_status='L', fields='ts_code, symbol,area,industry')
    for _, row in company_info.iterrows():
        if row["symbol"] in code_to_name:
            stock_info_record = {
                "ts_code": row["ts_code"],
                "symbol": row["symbol"],
                "name": code_to_name[row["symbol"]],
                "area": row["area"],
                "industry": row["industry"]
            }
            stock_info_list.append(stock_info_record)

    print("已更新股票信息共", stock_info_list.__len__(), "条")

    if len(stock_info_list) == len(stock_info_list):
        # 如果全部成功获取, 可清空原先的信息
        company_info_collection.delete_many({})
    # 插入最新信息
    company_info_collection.insert_many(stock_info_list)

    return


@pre_check(lazy_init)
def update_stock_price_info(day_to_update=5):
    """
    :param day_to_update: 指定更新最近多少天的数据
    :return:
    """
    api_upper_limit_per_request = 5000
    buffer_day = 3
    stock_per_request = api_upper_limit_per_request // day_to_update
    stock_per_request = min(stock_per_request, 99)

    all_company = company_info_collection.find({})

    alreay_update_set = set()
    for ts_code in stock_price_collection.find({"time": {"$gte": get_current_timestamp_in_millisecond() - 3600 * 24 * 1000 * buffer_day}}, {"_id": 0, "ts_code": 1}):
        alreay_update_set.add(ts_code["ts_code"])
    print(len(alreay_update_set))

    company_need_update = []
    buffer = list()
    for company in all_company:
        if company["ts_code"] in alreay_update_set:
            continue
        if len(buffer) < stock_per_request:
            buffer.append(company["ts_code"])
        else:
            company_need_update.append(buffer)
            buffer = list()
            buffer.append(company["ts_code"])

    if len(buffer) > 0:
        company_need_update.append(buffer)

    print(company_need_update)
    print(company_need_update.__len__())

    start_date = timestamp_in_millisecond_to_str(
        get_current_timestamp_in_millisecond() - 3600 * 24 * 1000 * day_to_update, NUMERICAL_DATE_FORMAT)

    for sub_company_list in company_need_update:
        if len(sub_company_list) == 0:
            continue
        query_key = ",".join(sub_company_list)
        print("正在更新", query_key)
        data = api_entry.query('daily', ts_code=query_key, start_date=start_date)
        valid_record_counter = 0
        for _, row in data.iterrows():
            price_record = {
                "ts_code": row["ts_code"],
                "time": str_to_timestamp_in_millisecond(str(row["trade_date"]), NUMERICAL_DATE_FORMAT),
                "open": float(row["open"]),
                "high": float(row["high"]),
                "low": float(row["low"]),
                "close": float(row["close"]),
                # 成交量 （手）
                "volume": float(row["vol"]),
                # 成交额 （千元）
                "amount": float(row["amount"]),
            }
            try:
                stock_price_collection.insert_one(price_record)
                valid_record_counter += 1
            except Exception as e:
                pass
        print(query_key, "更新完毕共", valid_record_counter, "条有效记录")

    return


if __name__ == '__main__':
    update_stock_price_info(5000)
