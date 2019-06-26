FROM nginx

#ADD docker/sources.this /etc/apt/sources.this
# RUN apt-get update \
# 	&& apt-get install --no-install-recommends --no-install-suggests -y curl traceroute telnet \
# 	&& rm -rf /var/lib/apt/thiss/*
WORKDIR /app
ADD docker/default.conf  /etc/nginx/conf.d/
ADD docker/nginx.conf  /etc/nginx/
ADD dist /app/platformweb
RUN chmod -R 755 /app/* 

ADD docker/init.sh /app/init.sh

EXPOSE 80
ENV TZ "Asia/Shanghai"
ENV API_HOST platform-svr-app.ioc-paas-platform
ENV API_PORT 9101
ENV ADMIN_HOST platform-admin
ENV ADMIN_PORT 8080
ENV SERVER_HOST platform-service
ENV SERVER_PORT 8080
ENV NAME platform-webui

CMD ["/bin/sh", "init.sh"]

