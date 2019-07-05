import { request, requestJsonHttpCode, requestUpload } from '@/utils/request'

// 字典查询
export async function getBaseData (params) {
  return request({
    url: '/framework/sys/code/list2',
    method: 'GET',
    params
  })
}

// 上传文件
export async function uploadFile (data) {
  return requestUpload({
    url: '/ljdp/attach/memory/upload.act',
    method: 'post',
    data
  })
}
/**
 * 获取镜像列表
 * @param {object} params
 */
export async function getImagesList (params) {
  return requestJsonHttpCode({
    url: '/api/v1/images',
    method: 'GET',
    params
  })
}
