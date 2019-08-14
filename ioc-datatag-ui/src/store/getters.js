const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  userInfo: state => state.user.userInfo,
  name: state => state.user.name,
  roles: state => state.user.roles,
  menuList: state => state.app.menuList,
  apiv1Token: state => state.user.apiv1Token,
  imagesList: state => state.dictionary.imagesList,
  catalogmenuList: state => state.app.catalogmenuList,
  imageId: state => state.tagPanel.listArr.id,
  imageName: state => state.tagPanel.listArr.tableName,
}
export default getters
