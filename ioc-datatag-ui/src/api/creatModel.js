import { fetch } from '@/utils/fetch'
// 标签管理--资源数列表
export function getOneZtreeData (params) {
  return fetch({
    url: '/pds/datalake/dataLake/allResourceTree',
    method: 'get',
    params
  })
}

// 标签管理--资源数列表
export function getChildZtreeData (orgId,databaseType) {
  return fetch({
    url: `/pds/datalake/dataLake/dataLakeResourceTree/${orgId}-${databaseType}`,
    method: 'get'
  })
}

// 标签管理--查询资源目录表列表
export function getResourceListData (orgId,type,databaseType) {
  return fetch({
    url: `/pds/datalake/dataLake/resourceList/${orgId}-${type}-${databaseType}`,
    method: 'get'
  })
}


