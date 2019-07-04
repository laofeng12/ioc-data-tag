export function statusPhaseFilter (value) {
  let label = ''
  switch (value) {
    case 0:
      label = '处理成功'
      break
    case 1:
      label = '正在等待'
      break
    case 2:
      label = '处理错误'
      break
    default:
      break
  }
  return label
}
