# Created by Xinyu Zhu on 2020/12/2, 21:04

import time
import threading
from functools import wraps


def profiling(func):
    """
    function with this decorator will print out the running time
    """

    def profiling_wrapper(*args, **kwargs):
        now = time.time()
        result = func(*args, **kwargs)
        duration = time.time() - now
        print("Function", str(func), " took", duration, "seconds")
        return result

    return profiling_wrapper


def simple_thread(func):
    """
    function with this decorator will be run in a new thread
    A thread pool should be used if run in large scale
    """

    def simple_thread_wrapper(*args, **kwargs):
        t = threading.Thread(target=func, args=args, kwargs=kwargs)
        t.start()

    return simple_thread_wrapper


def pre_check(check_func):
    """
    Use it when you want to run some function before your function in order to check some pre condition
    :param check_func:
    :return:
    """
    def decorate(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            check_func()
            return func(*args, **kwargs)

        return wrapper

    return decorate
