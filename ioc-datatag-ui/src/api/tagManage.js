import { fetch } from '@/utils/fetch'
// 标签管理--表格列表
export function getTagsData (params) {
  return fetch({
    url: '/datatag/tagmanage/myDtTagGroup/search',
    method: 'get',
    params
  })
}
