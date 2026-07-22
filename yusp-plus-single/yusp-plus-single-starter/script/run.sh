#!/bin/bash

usage="Usage: yusp-plus-single.sh (start|stop|status) <yusp-plus-single> "

if [ $# -le 1 ]; then
    echo $usage
    exit 1
fi

startStop=$1
shift
command=$1
shift

BIN_DIR=`dirname $0`
BIN_DIR=`cd "${BIN_DIR}"; pwd`

YUSP_PLUS_HOME=${BIN_DIR}/..

export YUSP_PLUS_HOME_PID_DIR=$YUSP_PLUS_HOME/pid
export YUSP_PLUS_HOME_LOG_DIR=$YUSP_PLUS_HOME/logs
export YUSP_PLUS_HOME_CONF_DIR=$YUSP_PLUS_HOME/conf
export YUSP_PLUS_HOME_LIB_JARS=$YUSP_PLUS_HOME/lib/*
export STOP_TIMEOUT=5

log=$YUSP_PLUS_HOME_LOG_DIR/yusp-plus-single.out
pid=$YUSP_PLUS_HOME_PID_DIR/yusp-plus-single.pid

if [ ! -d "$YUSP_PLUS_HOME_LOG_DIR" ]; then
   mkdir $YUSP_PLUS_HOME_LOG_DIR
fi

if [ "$command" = "yusp-plus-single" ]; then
  HEAP_INITIAL_SIZE=100m
  HEAP_MAX_SIZE=100m
  HEAP_NEW_GENERATION__SIZE=100m
  CONFIG_FILE="-Dspring.config.location=$YUSP_PLUS_HOME_CONF_DIR/"
  CLASS_LIB=yusp-plus-single-starter.jar
else
  echo "Error: No command named \`$command' was found."
  exit 1
fi

export YUSP_PLUS_SINGLE_OPTS="-server -Xms$HEAP_INITIAL_SIZE -Xmx$HEAP_MAX_SIZE -Xmn$HEAP_NEW_GENERATION__SIZE -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m  -Xss512k -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+PrintGCDetails  -XX:+HeapDumpOnOutOfMemoryError  -XX:HeapDumpPath=dump.hprof"

case $startStop in
  (start)
    [ -w "${YUSP_PLUS_HOME_PID_DIR}" ] ||  mkdir -p "$YUSP_PLUS_HOME_PID_DIR"

    if [ -f $pid ]; then
      if kill -0 `cat $pid` > /dev/null 2>&1; then
        echo $command running as process `cat $pid`.  Stop it first.
        exit 1
      fi
    fi

    echo "begin $startStop $command ...... "

    exec_command="$CONFIG_FILE $YUSP_PLUS_SINGLE_OPTS  -jar  $YUSP_PLUS_HOME/lib/$CLASS_LIB --application.file.local-disk-path=$USEDEPLOY_BASIC_EXECEL_HOME --use.basicDir=$YUSP_PLUS_HOME_CAP_DIR"

    echo "nohup java $exec_command > $log 2>&1 &"
    nohup java $exec_command > $log 2>&1 &
    echo $! > $pid
    echo "$command start，the detail result to see log file -> $log"
    ;;

  (stop)

      if [ -f $pid ]; then
        TARGET_PID=`cat $pid`
        if kill -0 $TARGET_PID > /dev/null 2>&1; then
          echo stopping $command
          kill $TARGET_PID
          sleep $STOP_TIMEOUT
          if kill -0 $TARGET_PID > /dev/null 2>&1; then
            echo "$command did not stop gracefully after $STOP_TIMEOUT seconds: killing with kill -9"
            kill -9 $TARGET_PID
          fi
        else
          echo no $command to stop
        fi
        rm -f $pid
      else
        echo no $command to stop
      fi
      ;;

  (status)
    # more details about the status can be added later
    serverCount=`ps -ef |grep "$CLASS_LIB" |grep -v "grep" |wc -l`
    state="STOP"
    #  font color - red
    state="[ \033[1;31m $state \033[0m ]"
    if [[ $serverCount -gt 0 ]];then
      state="RUNNING"
      # font color - green
      state="[ \033[1;32m $state \033[0m ]"
    fi
    echo -e "$command  $state"
    ;;

  (*)
    echo $usage
    exit 1
    ;;

esac
