import { request } from '@/utils/request'

/**
 * 获取用户列表
 * @param {object} params
 */
export function getUserList (params) {
  return request({
    url: '/admin/user/sysUser/search',
    method: 'get',
    params
  })
}

/**
 * 获取用户信息
 * @param {number} userId
 */
export function getUserInfo (userId) {
  return request({
    url: `/admin/user/sysUser/${userId}`,
    method: 'get'
  })
}

/**
 * 删除用户
 * @param {number} data
 */
export function deleteUser (data) {
  return request({
    url: '/admin/addNewUser',
    method: 'post',
    data
  })
}

/**
 * 获取用户拥有的角色列表
 * @param {number} params
 */
export function getUserRoleList (params) {
  return request({
    url: '/admin/user/sysUserRole/search',
    method: 'get',
    params
  })
}

/**
 * 删除用户角色
 * @param {object} data
 */
export function deleteUserRole (data) {
  return request({
    url: '/admin/user/sysUserRole/delete',
    method: 'post',
    data
  })
}

/**
 * 为用户添加角色
 * @param {object} data
 */
export function addSysUserRole (data) {
  return request({
    url: '/admin/user/sysUserRole',
    method: 'post',
    data
  })
}
