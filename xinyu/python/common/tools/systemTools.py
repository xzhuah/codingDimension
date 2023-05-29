# Created by Xinyu Zhu on 1/27/2023, 11:34 PM
from pyspectator.processor import Cpu
import winshell
import time


def structured_storage(filename: str):
    return winshell.structured_storage(filename)




if __name__ == '__main__':
    cpu = Cpu(monitoring_latency=1)
    with cpu:
        while True:
            print(f'Temp: {cpu.temperature} °C')
            time.sleep(2)