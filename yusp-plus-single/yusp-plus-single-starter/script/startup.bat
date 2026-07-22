@echo off
chcp 65001
set conf=-Dspring.config.location=../conf/
set startfile=../lib/yusp-plus-single-starter.jar
set opts=-server -Xms100m -Xmx100m -Xmn100m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m  -Xss512k -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+PrintGCDetails  -XX:+HeapDumpOnOutOfMemoryError  -XX:HeapDumpPath=dump.hprof

echo find yusp-plus-single to kill before start.
call stop.bat

start javaw -Dfile.encoding=utf-8 %conf% %opts% -jar %startfile% 1>>../logs/yusp-plus-single.log 2>&1 &
echo ------ begin start yusp-plus-single, see: ../logs/yusp-plus-single.log. ------
pause