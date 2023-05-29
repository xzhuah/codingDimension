# Created by Xinyu Zhu on 2/28/2023, 10:38 PM
from enum import Enum


class Resource(Enum):
    COIN = 1
    DIAMOND = 2

    LINGLI = 3
    LEVEL=4


class ResourceBatch:
    def __init__(self, resource={}):
        self.resource = resource

    def put(self, resource: Resource, amount: int):
        self.resource[resource] = amount

    def add(self, resource: Resource, amount: int):
        if resource not in self.resource:
            self.resource[resource] = amount
        else:
            self.resource[resource] += amount

    def __repr__(self):
        return str(self.resource)


class TradingRule:
    def __init__(self, cost: list, gain: list, name: str):
        self.cost = cost
        self.gain = gain
        self.tags = {"name": name}

    def tag(self, key, value):
        self.tags[key] = value

    def name(self, name):
        self.tags["name"] = name

    def __repr__(self):
        result = self.tags["name"] + ":" + str(self.cost) + " -> " + str(self.gain)
        if len(self.tags) > 1:
            result += "\n" + str(self.tags)
        return result


class TradingRules:
    def __init__(self):
        self.all_rules = []
        self.all_rules.append(
            TradingRule([ResourceBatch({Resource.DIAMOND: 20})], [ResourceBatch({Resource.COIN: 159840 * 1.4})],
                        "女神供奉20"))
        self.all_rules.append(
            TradingRule([ResourceBatch({Resource.DIAMOND: 50})], [ResourceBatch({Resource.COIN: 399600 * 1.4})],
                        "女神供奉50"))

        self.all_rules.append(
            TradingRule([ResourceBatch({Resource.COIN: 1220000, Resource.LINGLI: 76555})], [ResourceBatch({Resource.COIN: 399600 * 1.4})],
                        "女神升级1"))

    def __repr__(self):
        return str(self.all_rules)


if __name__ == '__main__':
    trading_rules = TradingRules()
    print(trading_rules)
