## 微信跳一跳

### 介绍

1. 通过使用Android ddmlib、sdklib 调用adb，执行shell screencap -p 进行手机截图
2. 将截图 pull 到PC后进行分析，找到当前位置和目标位置，计算需跳跃的距离
3. 计算时间，通过adb执行触摸操作


### 使用

1. 安装Android ADB，并将ADB.exe添加到PATH
2. PC使用adb与手机连接，手机打开开发者模式，设置里面开发者选项打开调试模式，可以执cmd行adb devices命令查看是否连接上手机
3. 打开微信跳一跳，开始游戏
4. 启动App main
5. 观察界面显示的找点是否正确或异常，可适当在JumpUtil中对取点颜色和跳跃时间比例进行调整


![image](https://github.com/MeetFuture/weixin-jump-jump/blob/master/tmp/Screen_20180105234426.png?raw=true)