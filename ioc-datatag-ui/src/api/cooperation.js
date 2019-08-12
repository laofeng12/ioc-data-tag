import {fetch} from "../utils/fetch";

export function getcooperationList(data) {
  return fetch({
    url:'/datatag/tagcol/dtCooperation/searchcool',
    method:'post',
    data
  })
}

export function cooperationQuery(params){
  return fetch({
    url:'/datatag/tagcol/dtCooperation/pagesearch',
    method:'get',
    params
  })
}

/**
 * 协作字段记录
 * @param data
 */
export function getSearchcoofield(data) {
  return fetch({
    url:'/datatag/tagcol/dtCooperation/searchcoofield',
    method:'post',
    contentType: 'application/x-www-form-urlencoded',
    data
  })
}


/**
 * 协作字段标签组
 * @param data
 */
export function getTaggroup(data) {
  return fetch({
    url:'/datatag/tagcol/dtCooperation/taggroup',
    method:'post',
    contentType: 'application/x-www-form-urlencoded',
    data
  })
}
