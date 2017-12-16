# linkram

#登录druid监控，账号为root/root
http://localhost:8080/linkram/druid

#Restful
http://localhost:8080/linkram/hello/world/zm

#启动服务命令(到文件路径下)
java -jar com-link-ram-0.0.1-SNAPSHOT.jar

#发送指令rest接口如下，{msg}可以自由替换成其他指令
http://localhost:8080/linkram/instruct/send/{msg}

#TCP端口为8090，与Http端口不能重复。