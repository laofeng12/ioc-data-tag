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
