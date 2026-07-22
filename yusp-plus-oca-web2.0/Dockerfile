FROM dev-apaas-harbor-app.mis.bcs/kylin_base_images/nginx27:20250725-01

#将dist目录内容复制到nginx容器html内部
COPY yusp-plus-oca-web2.0/dist /usr/share/nginx/html/
#COPY custom /usr/share/nginx/html/custom
#COPY libs /usr/share/nginx/html/libs
#COPY mocks /usr/share/nginx/html/mocks
#COPY pages /usr/share/nginx/html/pages
#COPY themes /usr/share/nginx/html/themes
#COPY *.* /usr/share/nginx/html/
COPY yusp-plus-oca-web2.0/default.conf /etc/nginx/conf.d/
#COPY yusp-plus-oca-web2.0/dns.sh /docker-entrypoint.d/dns.sh
#RUN chmod +x /docker-entrypoint.d/dns.sh

#EXPOSE 80
