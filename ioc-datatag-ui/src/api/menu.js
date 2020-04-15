import { request } from '@/utils/request'

/**
 * 获取菜单列表
 * @param {object} params
 */
// export function getMenuList (params) {
//   return request({
//     url: '/admin/res/sysRes/myResources',
//     method: 'get',
//     params
//   })
// }
export function getMenuList (params) {
  return request({
    url: '/admin/res/sysRes/v2/myResources?systemids=10000082780009',
    method: 'get',
    params
  })
}
