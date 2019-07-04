#!/usr/bin/env bash
echo "## build代码"
rm -rf dist
npm run build

currenttime=$(date "+%Y%m%d%H%M%S")
echo "## build ui镜像，版本=${currenttime}"
docker rmi harbor.node02.paas/ioc-paas/platform-webui:latest

docker build . -t harbor.node02.paas/ioc-paas/platform-webui:${currenttime}
docker tag harbor.node02.paas/ioc-paas/platform-webui:${currenttime} harbor.node02.paas/ioc-paas/platform-webui:latest

docker login -u admin -p Harbor@admin2019 harbor.node02.paas
docker push harbor.node02.paas/ioc-paas/platform-webui:${currenttime}
docker push harbor.node02.paas/ioc-paas/platform-webui:latest

echo "## kubernetes 滚动更新"
oc set image deployment/platform-webui platform-webui=harbor.node02.paas/ioc-paas/platform-webui:${currenttime} -n ioc-platform

oc rollout status deployment/platform-webui