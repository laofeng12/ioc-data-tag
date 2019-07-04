import { request, requestJson } from '@/utils/request'

// api凭证列表
export function getApiCredential (params) {
  return request({
    url: '/certificate/api/spApiCredential/search',
    method: 'GET',
    params
  })
}

// 创建api凭证
export function addApiCredential (data) {
  return request({
    url: '/certificate/api/spApiCredential',
    method: 'post',
    contentType: 'application/json',
    data
  })
}

// 删除api凭证
export function delApiCredential (data) {
  return request({
    url: '/certificate/api/spApiCredential/delete',
    method: 'post',
    contentType: 'application/json',
    data
  })
}

// api凭证详情
export function getApiCredentialDetail (id) {
  return request({
    url: '/certificate/api/spApiCredential/' + id,
    method: 'GET'
  })
}

// 获取sso凭证列表
export function getServiceCredential (params) {
  return request({
    url: '/certificate/services/spServiceCredential/search',
    method: 'GET',
    params
  })
}

// 修改sso凭证
export function editServiceCredential (data) {
  return requestJson({
    url: '/certificate/services/spServiceCredential',
    method: 'post',
    contentType: 'application/json',
    data
  })
}

// 删除sso凭证
export function delServiceCredential (data) {
  return request({
    url: '/certificate/services/spServiceCredential/delete',
    method: 'post',
    contentType: 'application/json',
    data
  })
}
