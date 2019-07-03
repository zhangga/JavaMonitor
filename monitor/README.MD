# 使用说明
### 使用maven的install构建出jar包，
### 将构建出的 **monitor-xx.jar** 和 **lib/javassist-xx-GA.jar** 一起放到要监控的服务器目录下，**注意：** 这两个jar包放同目录下。
### 启动脚本：java -javaagent:目录\monitor-1.0.jar -cp 自己项目的cp配置 自己项目的启动类