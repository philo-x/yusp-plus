#!/usr/bin/env bash

export NAME_SERVER=`awk '/^nameserver/{print $2}' /etc/resolv.conf`

sed -i  "s#{NAME_SERVER}#${NAME_SERVER}#g" /etc/nginx/conf.d/default.conf
