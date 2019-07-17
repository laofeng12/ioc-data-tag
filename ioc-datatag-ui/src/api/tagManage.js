import { fetch } from '@/utils/fetch'
// 标签管理--表格列表
export function getTagsData (params) {
  return fetch({
    url: '/datatag/tagmanage/myDtTagGroup/search',
    method: 'get',
    params
  })
}
// 标签管理--共享
export function getDtTagGroupData (data) {
  return fetch({
    url: '/datatag/tagmanage/myDtTagGroup',
    method: 'post',
    data
  })
}

// 标签管理--删除
export function delTagGroup (id) {
  return fetch({
    url: `/datatag/tagmanage/myDtTagGroup?id=${id}`,
    method: 'delete'
  })
}

