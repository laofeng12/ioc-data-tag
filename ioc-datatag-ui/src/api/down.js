import { fetch } from '@/utils/fetch'
// 下载管理--表格列表
export function getdownList (params) {
  return fetch({
    url: '/datatag/dowload/downloadQueue/search',
    method: 'get',
    params
  })
}

/**
 * 导出到本地
 * @param params
 * @returns {*}
 */
export function exportList (params) {
  return fetch({
    url: `/datatag/dowload/downloadQueue/dowloadToLocal/`+params,
    method: 'get'
  })
}

