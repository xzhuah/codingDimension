# Created by Xinyu Zhu on 1/22/2023, 4:20 PM

import win32gui
import win32con
import sys

sys.path.append("C:/myC/Personal/codingDimension/xinyu/python")
from node.clipboardNode.winPathAutoConvert import start_listen


# win32gui.Shell_NotifyIcon(NIM_ADD ,n) 这是最终的要使用的函数

# 参数需要一个结构体notify_id,
# notify结构体需要一个hwnd,
# hwnd是一个处理消息的窗口,
# 窗口需要用createwindow创建,
# 创建前需要注册,都是c语言的规则,但是我不太懂.
# 注册前要定义处理事件消息的函数notify
# 消息是相当于发生的事件,比如新建,销毁,鼠标滑过啥的

# 注册时要定义处理消息的函数notify

def main():
    print("start my own function")
    start_listen()


def notify(hwnd, msg, wparam, lparam):
    '''消息处理函数
        窗口事件入口,所有事件都是通过本函数进来处理
        msg是最主要的识别符.比如自定义鼠标事件发送WM_USER + 20过来,就会收到msg==WM_USER + 20的信息,也就是1044,自定义消息符只能定义在1044-ox7fff,wparam, lparam是msg带过来的孩子,有时也不带孩子来.hwnd不用解释了,句柄,交给系统后,这个句柄是变化的.所以经常都要传一传.
        '''

    print("notify", msg, type(msg))
    if msg == 799:
        main()
    if lparam == win32con.WM_LBUTTONDBLCLK:  # 双击左键
        print("双击左键", msg)

        # Terminate the app.
        nid = (hwnd, 0)
        win32gui.Shell_NotifyIcon(win32gui.NIM_DELETE, nid)
        win32gui.PostQuitMessage(0)
        sys.exit()
        # exit(1)
    elif lparam == win32con.WM_RBUTTONUP:  # 右键弹起
        print("右键弹起", msg)
        pass
        # s.show_menu()
    elif lparam == win32con.WM_LBUTTONUP:  # 左键弹起
        print("左键弹起", msg)
        pass
        # s.root.deiconify()

    return True


if __name__ == '__main__':
    # 准备注册
    wc = win32gui.WNDCLASS()  # winclass在win32中是结构体,win32gui库中,被封装成了一个类.
    # 通过重写属性实现这个结构,我是这么理解的,不一定对,暂时也不需要对。

    wc.hInstance = win32gui.GetModuleHandle(None)
    # 参数为字符串，可使用相对路径指向一个DLL或EXE文件。省略扩展名则默认为DLL
    # 如果参数为NULL，返回创建调用进程的文件句柄。

    wc.lpszClassName = "测试"  # window名称
    wc.style = win32con.CS_VREDRAW | win32con.CS_HREDRAW
    # WNDCLASS的style 属性,wincalss,具体属性值查后面推荐的网站.

    wc.lpfnWndProc = notify  # 指定消息处理函数

    # 注册wc类,用于随后在CreateWindow中使用
    classAtom = win32gui.RegisterClass(wc)

    # 创建窗口,获取hwnd
    hwnd = win32gui.CreateWindow(classAtom,  # 之前创注册的类,这里是一个int,相当于一个指针,
                                 "tst2",  # 这个名称相当于是实例的名称,虽然之前的结构体,也就是类中也有一个名称.
                                 win32con.WS_OVERLAPPEDWINDOW,  # 类的style是cs头字,对象style是ws头字.不一样,
                                 win32con.CW_USEDEFAULT,  # 窗口的x坐标,这个代表默认值,如果设置默认,y坐标就会被忽略.
                                 win32con.CW_USEDEFAULT,  # 窗口的y坐标,因为x已经设置了默认,所以这里设置什么都可以.,y坐标就会被忽略.
                                 win32con.CW_USEDEFAULT,  # 窗口的宽度
                                 win32con.CW_USEDEFAULT,  # 窗口的宽度
                                 None,  # 指定父窗口句柄,如果创建的是子窗口才需要提供,否者设置为none或0
                                 None,  # 指定菜单句柄,也可以设置为0
                                 None,  # 指定关联模块句柄,一般是本模块,就是本程序,本进程的句柄.后面处理某些消息可能会用到
                                 None  # 这个是创建多文档窗口时必须指向的一个结构体.首先要了解多文档窗口。
                                 )

    # 准备notify_id结构体
    notify_id = (hwnd,  # 就是刚创建的窗口的句柄
                 0,  # 托盘图标ID,应该是根据图标资源来的,如果资源是一个表,这个才起作用.
                 win32gui.NIF_ICON | win32gui.NIF_MESSAGE | win32gui.NIF_TIP,
                 # 托盘图标可以使用的功能的标识,ICON,MESSAGE,TIP就是下面三个参数.上文表示先表态说,ICON,MESSAGE,TIP三个成员有效。NIF_INFO不知道是什么。
                 win32con.WM_USER + 20,  # 上文NIF_MESSAGE中的messagg功能，鼠标事件发生在图标上时，向窗口通知此消息
                 # WM_USER值为1024,小于1024是window保留值,1024到Ox7ffff(32767)是程序可用的范围,
                 # 大于7fff--bfff 是预留给系统的,大于bfff--ffff是程序可用的字符串消息.更大范围是预留给windows将来使用.
                 # 这些值不能用于定义整个应用程序中有意义的消息，因为某些预定义的窗口类已经定义了此范围内的值。
                 # 例如，诸如BUTTON，EDIT，LISTBOX和COMBOBOX之类的预定义的控制类可以使用这些值。
                 # 该范围内的消息不应发送到其他应用程序，除非应用程序被设计为交换消息并且将相同的含义附加到消息号。

                 win32gui.LoadIcon(0, win32con.IDI_APPLICATION),  # 图标资源句柄.
                 "Python 后台系统助手(双击退出)"  # 需要上面申明NIF_TIP有效才行
                 )

    win32gui.Shell_NotifyIcon(0, notify_id)  # 0就是NIM_ADD值,可以看包,新建修改删除,对应012

    # 如果没有下面的句子,上面也能成功,但是一闪而过,因为窗口,需要一直刷新,需要循环....但凡是窗体,都有一个大循环.还有什么消息循环.
    # 想想一下, 执行完上面的句子,程序就该结束了,程序结束,窗口就不存在了, 所以应该有一个循环句子,类似tk也是这样的.
    # win32gui.UpdateWindow(hwnd)
    win32gui.PumpMessages()
