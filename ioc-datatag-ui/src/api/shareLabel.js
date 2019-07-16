import {fetch} from "../utils/fetch";

/**
 *
 * @param params
 */
export function shareSearch(params) {
  return fetch({
    url:'/datatag/tagmanage/shareDtTagGroup/search',
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

