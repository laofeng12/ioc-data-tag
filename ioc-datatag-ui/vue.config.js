const baseUrl = process.env.NODE_ENV === 'production' ? '/datatag/' : '/datatag/'
module.exports = {
  lintOnSave: false,
  publicPath: baseUrl,
  css: {
    loaderOptions: {
      sass: {
        data: `$baseUrl: "${baseUrl}";`
      }
    }
  },
  devServer: {
    proxy: {
      '/admin': {
        // target: 'http://47.107.56.144:8081',
        // target: 'http://19.104.40.36:31880', // 政务内网
        target: 'http://183.6.55.26:31075', // 公司测试环境
        changeOrigin: true
      },
      '/pds': {
        target: 'http://183.6.55.26:31013', // 公司测试环境
        // target: 'http://192.168.4.209:8082', // zuoye
        changeOrigin: true
      },
      '/ljdp': {
        target: 'http://183.6.55.26:31075', // 临时上传文件
        changeOrigin: true
      },
      '/framework': {
        target: 'http://183.6.55.26:31075', //
        changeOrigin: true
      },
    }
  },
  chainWebpack: config => {
    // 一个规则里的 基础Loader
    // svg是个基础loader
    const svgRule = config.module.rule('svg')

    // 清除已有的所有 loader。
    // 如果你不这样做，接下来的 loader 会附加在该规则现有的 loader 之后。
    svgRule.uses.clear()

    // 添加要替换的 loader
    svgRule
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
  }
}
