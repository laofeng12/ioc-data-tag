import {fetch} from "../utils/fetch";

/**
 *
 * @param params
 */
export function shareSearch(params) {
  return fetch({
    url:'/datatag/tagmanage/shareDtTagGroup',
    method:'get',
    params
  })
}

/**
 *
 * @param data
 */
export function changeSelection(data) {
  return fetch({
    url:'/datatag/tagmanage/shareDtTagGroup',
    method:'post',
    contentType: 'application/x-www-form-urlencoded',
    data
  })
}

/**
 *
 * @param params
 */
export function lookTree(id) {
  return fetch({
    url:'/datatag/tagmanage/dtTag/'+id,
    method:'get'
  })
}

/**
 *
 * @param params
 */
export function looktreeTable(id) {
  return fetch({
    url:'/datatag/tagmanage/dtTag/table/'+id,
    method:'get'
  })
}

