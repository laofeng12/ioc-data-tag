#!/usr/bin/env bash
echo "## build代码"
rm -rf dist
npm run build

echo "## build ui镜像"
docker rmi harbor.alltosea.com:88/ioc-paas/platform-webui:latest

docker build . -t harbor.alltosea.com:88/ioc-paas/platform-webui:latest

docker login -u admin -p Ch#123456 harbor.alltosea.com:88
docker push harbor.alltosea.com:88/ioc-paas/platform-webui:latest