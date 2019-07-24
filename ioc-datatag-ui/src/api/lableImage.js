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
    // contentType: 'application/x-www-form-urlencoded',
    data
  })
}
