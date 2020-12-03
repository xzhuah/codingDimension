# Created by Xinyu Zhu on 2020/12/2, 21:13

import pymongo
from common.annotations.annotation import pre_check

db_client = None


def lazy_init():
    global db_client
    if db_client is None:
        db_client = pymongo.MongoClient("mongodb://localhost:27017/")


@pre_check(lazy_init)
def get_all_db_name():
    return db_client.list_database_names()


@pre_check(lazy_init)
def get_all_collection_name(db_name):
    return db_client[db_name].list_collection_names()


@pre_check(lazy_init)
def get_db(db_name):
    return db_client[db_name]


@pre_check(lazy_init)
def get_collection(db_name, collection_name):
    return db_client[db_name][collection_name]


def new_db(db_name):
    return pymongo.MongoClient("mongodb://localhost:27017/")[db_name]


def new_collection(db_name, collection_name):
    return pymongo.MongoClient("mongodb://localhost:27017/")[db_name][collection_name]

