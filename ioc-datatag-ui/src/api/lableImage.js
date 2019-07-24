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
