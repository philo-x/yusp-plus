# FROM dev-apaas-harbor-app.mis.bcs/kylin_base_images/kylin-openjdk:20220330
# 更换成支持 tar 命令的基础镜像包
FROM dev-apaas-harbor-app.mis.bcs/kylin_base_images/openjdk17:20231018
#创建放置程序包的目录
RUN mkdir -p /app
ADD ./yusp-plus-single/yusp-plus-single-starter/target/yusp-plus-single-starter.jar /app/
WORKDIR /app
#USER bcsappadmin
#程序启动命令和启动参数
CMD ["sh", "-c", "cd /app; java ${JAVA_OPTS} -jar /app/yusp-plus-single-starter.jar ${RUN_ARGS}"]
