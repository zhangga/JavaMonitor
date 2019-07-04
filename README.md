# JavaMonitor
面向Java服务的性能监测工具  
非侵入式  
只需要将monitor.jar和要监控的项目一起启动即可。
## 部署说明
### monitor.jar包的使用说明，在monitor项目下。
### monitor_server的使用说明
可以自己部署监控服务器，也可以使用已搭建好的服务: **monitor.server=192.144.167.243:10007**  
自己搭建的话，将server项目检出，修改配置文件application.properties中的mysql配置，使用maven打包出jar或war包都可，然后启动脚本：nohup java -jar monitor_server.war > ./monitor_server.log &  
### 数据可视化使用superset
自己搭建server的话，也需要搭建superset。
