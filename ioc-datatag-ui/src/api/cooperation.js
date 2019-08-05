import {fetch} from "../utils/fetch";

export function getcooperationList(data) {
  return fetch({
    url:'/datatag/tagcol/dtCooperation/searchcool',
    method:'post',
    data
  })
}


