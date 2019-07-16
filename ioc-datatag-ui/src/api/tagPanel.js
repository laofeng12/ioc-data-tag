import {fetch} from '@/utils/fetch'
/***
 * 仪表盘growthOne
 * @param params
 * @returns {*}
 */
export function growthOne (params) {
  return fetch({
    url: '/datatag/statistic/datasetgrowth',
    method: 'get',
    params
  })
}

/***
 * 仪表盘growthTwo
 * @param params
 * @returns {*}
 */
export function growthTwo (params) {
  return fetch({
    url: '/datatag/statistic/getTagThanDataSet',
    method: 'get',
    params
  })
}

/***
 * 仪表盘growthThree
 * @param params
 * @returns {*}
 */
export function growthThree (params) {
  return fetch({
    url: '/datatag/statistic/getLastMonthTagSum',
    method: 'get',
    params
  })
}

/***
 * 仪表盘标签变化图
 * @param params
 * @returns {*}
 */
export function labelChange (params) {
  return fetch({
    url: '/datatag/statistic/getMonthlyLabelChanges',
    method: 'get',
    params
  })
}

/***
 * 仪表盘标签labbel
 * @param params
 * @returns {*}
 */
export function labelNum (params) {
  return fetch({
    url: '/datatag/statistic/getAllYearMonth',
    method: 'get',
    params
  })
}

/***
 * 仪表盘热门标签
 * @param params
 * @returns {*}
 */
export function getToday (params) {
  return fetch({
    url: '/datatag/statistic/getsamedayhottags',
    method: 'get',
    params
  })
}

export function getYesterday (params) {
  return fetch({
    url: '/datatag/statistic/getyesterdayhottags',
    method: 'get',
    params
  })
}

export function getWeek (params) {
  return fetch({
    url: '/datatag/statistic/getlastweekhottags',
    method: 'get',
    params
  })
}


export function getMonth (params) {
  return fetch({
    url: '/datatag/statistic/getlastmonthhottags',
    method: 'get',
    params
  })
}

export function getYear (params) {
  return fetch({
    url: '/datatag/statistic/getlastyearhottags',
    method: 'get',
    params
  })
}
