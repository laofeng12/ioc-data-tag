import { request, requestJsonHttpCode } from '@/utils/request'

// fetch
export function getRoleList (params) {
  return request({
    url: '/admin/role/sysRole/search',
    method: 'get',
    params
  })
}

// add || edit
export function editRole (data) {
  return request({
    url: '/admin/role/sysRole',
    method: 'post',
    contentType: 'application/json',
    data
  })
}

// delete
export function deleteRole (data) {
  return request({
    url: '/admin/role/sysRole/delete',
    method: 'post',
    contentType: 'application/json',
    data
  })
}

// get tree
export function jurisdictionList (params) {
  return request({
    url: '/admin/res/sysRes/antTreeByRole',
    method: 'get',
    params
  })
}

// save tree
export function saveTreeList (data) {
  return request({
    url: '/admin/role/sysRole/updateRes',
    method: 'POST',
    contentType: 'application/json',
    data
  })
}

// getResourseTree
export function getResourseTree () {
  return request({
    url: '/admin/res/sysRes/doZTree',
    method: 'get'
  })
}

// getResourseTree(new)
export function getNewResourseTree () {
  return request({
    url: '/admin/res/sysRes/doZTreeToElement',
    method: 'get'
  })
}

// getResourseTreeDetail
export function getResourseTreeDetail (id) {
  return request({
    url: '/admin/res/sysRes/' + id,
    method: 'get'
  })
}

// saveResource
export function saveResourseTree (data, isNew) {
  return request({
    url: '/admin/res/sysRes?isNew=' + isNew,
    method: 'post',
    contentType: 'application/json',
    data
  })
}

// deleteResource
export function deleteResourseTree (data) {
  return request({
    url: '/admin/res/sysRes/delete',
    method: 'post',
    contentType: 'application/json',
    data
  })
}

export function addRoleUser (data) {
  return request({
    url: '/admin/user/sysUserRole',
    method: 'post',
    data
  })
}

// 获取组织机构树列表
export function getMechanismTree (params) {
  return requestJsonHttpCode({
    url: '/admin/org/sysOrg/doZTreeToElement',
    method: 'get',
    params
  })
}

// 获取组织机构树列表详情
export function getMechanismTreeDetail (id) {
  return requestJsonHttpCode({
    url: '/admin/org/sysOrg/' + id,
    method: 'get'
  })
}
