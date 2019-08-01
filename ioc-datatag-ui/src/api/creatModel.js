import {fetch} from '@/utils/fetch'
import {request} from '@/utils/request'

// 标签管理--资源数列表
export function getOneZtreeData(params) {
  return fetch({
    url: '/pds/datalake/dataLake/allResourceTree',
    method: 'get',
    params
  })
}

// 标签管理--资源数列表
export function getChildZtreeData(orgId, databaseType) {
  return fetch({
    url: `/pds/datalake/dataLake/dataLakeResourceTree/${orgId}-${databaseType}`,
    method: 'get'
  })
}

// 标签管理--查询资源目录表列表
export function getResourceListData(orgId, type, databaseType) {
  return fetch({
    url: `/pds/datalake/dataLake/resourceList/${orgId}-${type}-${databaseType}`,
    method: 'get'
  })
}

//选择协作用户
export function choosePeople(userId) {
  return request({
    url: '/admin/org/sysOrg/doUserTopDepartmentUserList/' + userId,
    method: 'get'
  })
}

// 获取协作成员
export function getPeople(params) {
  return fetch({
    url:'/datatag/tagcol/dtCooperation/search',
    method:'get',
    params
  })
}

// add
export function addPeople(data) {
  return fetch({
    url:'/datatag/tagcol/dtCooperation/save',
    method:'post',
    data
  })
}

// 删除
export function deletePeople(params) {
  return fetch({
    url:'/datatag/tagcol/dtCooperation',
    method:'delete',
    params
  })
}

// 打标字段
export function markingCheck(data) {
  return fetch({
    url:'/datatag/tagcol/dtCooperation/searchcoofield',
    contentType: 'application/x-www-form-urlencoded',
    method:'post',
    data
  })
}

// 我的标签组
export function labelGroup(params) {
  return fetch({
    url:'/datatag/tagmanage/myDtTagGroup',
    method:'get',
    params
  })
}

// 保存协作用户
export function dosave(data) {
  return fetch({
    url:'/datatag/tagcol/dtCooperation/dosave',
    method:'post',
    data
  })
}
