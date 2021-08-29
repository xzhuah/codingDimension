# Created by Xinyu Zhu on 2021/8/29, 0:11
def check_state(expression, error_message=""):
    if not expression:
        raise Exception(error_message)
