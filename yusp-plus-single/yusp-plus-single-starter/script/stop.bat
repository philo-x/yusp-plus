@echo off
chcp 65001
set program=../lib/yusp-plus-single-starter.jar
echo program: %program%
for /f "usebackq tokens=1-2" %%a in (`jps -l ^| findstr %program%`) do (
	set pid=%%a
	set image_name=%%b
)
if not defined pid (echo process %program% does not exists) else (
	echo prepare to kill %image_name%
	echo start kill %pid% ...
	rem 根据进程ID，kill进程
	taskkill /f /pid %pid%
)
echo yusp-plus-single is stopped
pause