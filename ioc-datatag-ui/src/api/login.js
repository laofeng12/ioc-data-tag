import { request, requestJsonHttpCode } from '@/utils/request'

/**
 * 登陆admin项目
 * @param {string} userAccount
 * @param {string} userPwd
 */
export function login (userAccount, userPwd) {
  return request({
    url: '/admin/user/sysUser/login',
    method: 'post',
    data: {
      userAccount,
      userPwd
    }
  })
}

/**
 * 登陆api/v1项目
 * @param {string} userAccount
 * @param {string} userPwd
 */
export function loginApiV1 (userAccount, userPwd) {
  return requestJsonHttpCode({
    url: '/api/v1/account/login',
    method: 'post',
    data: {
      username: userAccount,
      password: userPwd
    }
  })
}

/**
 * 退出，暂时没用
 */
// export function logout () {
//   return request({
//     url: '',
//     method: 'post'
//   })
// }

export function logout () {
  return request({
    url: '/admin/user/OA/loginOut',
    method: 'post'
  })
}
