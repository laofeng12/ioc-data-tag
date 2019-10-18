import {fetch} from "../utils/fetch";

/**
 *
 * @param params
 */
export function getmodelList(params) {
  return fetch({
    url:'/datatag/tagmodel/dtTaggingModel/search',
    method:'get',
    params
  })
}


/**
 *
 * @param data
 */
export function getDispatch(data) {
  return fetch({
    url:'/datatag/tagmodel/dtTaggingModel/Dispatch',
    method:'post',
    data
  })
}

/**
 *
 * @param params
 */
export function getDelete(params) {
  return fetch({
    url:'/datatag/tagmodel/dtTaggingModel',
    method:'delete',
    params
  })
}

/**
 *
 * @param data
 */
export function getImage(id) {
  return fetch ({
    url:'/datatag/portrayal/search/'+id,
    method:'get'
  })
}

/**
 *
 * @param data
 */
// export function getImagelist(detailId) {
//   return fetch ({
//     url:'/datatag/portrayal/getCoolDetail/'+detailId,
//     method:'get'
//   })
// }

/**
 *
 * @param data
 */
export function getImagelist({tableName,pKey}) {
  return fetch ({
    url:`datatag/portrayal/getCoolDetail/${tableName}/${pKey}`,
    method:'get'
  })
}

/**
 * 获取调度详情
 * @param detailId
 */
export function getDispatchdetail(params) {
  return fetch ({
    url:'/datatag/tagmodel/dtTaggingModel/Dispatch',
    method:'get',
    params
  })
}


/**
 *查询模型打标结果列表
 * @param data
 */
export function getlist({taggingModelId,page,size}) {
  return fetch ({
    url:`/datatag/tagmodel/dtTaggingModel/${taggingModelId}/${page}/${size}/`,
    method:'get'
  })
}

/**
 *开始导出
 * @param data
 */
export function startDown(params) {
  return fetch ({
    url:"/datatag/tagmodel/dtTaggingModel/beginDowload",
    method:'get',
    params
  })
}
