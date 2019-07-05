#! /bin/sh
sed -i "s%API_HOST%$API_HOST%g" /etc/nginx/conf.d/default.conf
sed -i "s%API_PORT%$API_PORT%g" /etc/nginx/conf.d/default.conf
sed -i "s%ADMIN_HOST%$ADMIN_HOST%g" /etc/nginx/conf.d/default.conf
sed -i "s%ADMIN_PORT%$ADMIN_PORT%g" /etc/nginx/conf.d/default.conf
sed -i "s%SERVER_HOST%$SERVER_HOST%g" /etc/nginx/conf.d/default.conf
sed -i "s%SERVER_PORT%$SERVER_PORT%g" /etc/nginx/conf.d/default.conf
nginx -g 'daemon off;'
