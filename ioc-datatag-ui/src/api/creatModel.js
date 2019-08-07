import { fetch } from '@/utils/fetch'
import {request} from "../utils/request";

// 创建模型--资源树
export function getOneZtreeData (params) {
  return fetch({
    url: '/pds/datalake/dataLake/allResourceTree',
    method: 'get',
    params
  })
}

// 创建模型--资源树多级数据
export function getChildZtreeData (orgId,databaseType) {
  return fetch({
    url: `/pds/datalake/dataLake/dataLakeResourceTree/${orgId}-${databaseType}`,
    method: 'get'
  })
}

// 创建模型--查询资源表列表
export function getResourceListData (orgId,type,databaseType) {
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

// 创建模型--资源表对应字段
export function getResourceInfoData (resourceId,type) {
  return fetch({
    url: `/pds/datalake/dataLake/resourceInfo/${resourceId}-${type}`,
    method: 'get'
  })
}


// 创建模型--字段设置确认选择
export function setColsData (data) {
 // console.log(data)
  return fetch({
    url: '/datatag/tagmodel/dtSetCol/selectCol',
    method: 'post',
    data
  })
}

// 模型-清除字段
export function  delCol( id) {
  return fetch({
    url:'/datatag/tagmodel/dtSetCol/delete',
    method:'delete',
    id
  })
}

// 模型-字段克隆
export function  cloneCol( data) {
  return fetch({
    url:'/datatag/tagmodel/dtSetCol/clone',
    contentType: 'application/x-www-form-urlencoded',
    method:'post',
    data
  })
}

// 获取字典符号
export function getSymolsData(params) {
  return fetch({
    url:'/framework/sys/code/list2',
    method:'get',
    params
  })
}


//获取标签模型数据
export function getModelData (params) {
  return fetch({
    url: '/datatag/tagmodel/dtTaggingModel/getModel',
    method: 'get',
    params
  })
}


export function getModelColsData (taggingModelId,page,size,type) {
  return fetch({
    url: `/datatag/tagmodel/dtTaggingModel/${taggingModelId}/${page}/${size}/${type}`,
    method: 'get'
  })
}
// 打标--选择标签组
export function getMyTagGroupData (params) {
  return fetch({
    url: '/datatag/tagmanage/myDtTagGroup',
    method: 'get',
    params
  })
}

// 打标--选择标签组
export function getTagLevData(id) {
  return fetch({
    url: `/datatag/tagmanage/dtTag/${id}`,
    method: 'get'
  })
}
// 打标--确认打标-查询打标历史接口
export function getHistoryColData(params) {
  return fetch({
    url: '/datatag/tagmodel/dtSetCol/getHistoryCol',
    method: 'get',
    params
  })
}
// 字段打标--打标确认
export function saveMarkData (data) {
  //console.log(data)
  return fetch({
    url: '/datatag/tagmodel/dtSetCol/saveCondition',
    method: 'post',
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

// 名称保存
export function saveName(data) {
  return fetch({
    url:'/datatag/tagmodel/dtTaggingModel/rename',
    method:'post',
    data
  })
}

// 模型另存
export function saveAs(taggingModelId) {
  return fetch({
    url:'/datatag/tagmodel/dtTaggingModel/copy/'+taggingModelId,
    method:'post',
  })
}

// 设置调度
export function goDispatch(data) {
  return fetch({
    url:'/datatag/tagmodel/dtTaggingModel/Dispatch',
    method:'post',
    data
  })
}
/**
 * 获取调度详情
 * @param detailId
 */
export function getmodelDispatchdetail(params) {
  return fetch ({
    url:'/datatag/tagmodel/dtTaggingModel/Dispatch',
    method:'get',
    params
  })
}
