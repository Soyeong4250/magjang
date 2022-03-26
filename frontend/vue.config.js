// const path = require('path');
module.exports = {
  publicPath: process.env.BASE_URL,
  assetsDir: process.env.BASE_URL, 
  devServer: {
    // contentBase: path.join(__dirname, ''), // 설정하면 url(/src/assets") 경로 사용 가능 
    
    // Vue3 관련 설정 파일
    https: true,
    port: 443,
    open: true,
    proxy: {
      '/account': {
        target: 'http://localhost:8080/'
      },
    },
    historyApiFallback: true,
    hot: true
  },
  // css: {
  //   requireModuleExtension: false // import 시에 경로에 .module 포함 안해도 됨.
  // },
  // transpileDependencies: [
  //   'element-plus'
  // ],
  // lintOnSave: false,
  // outputDir: '/var/www/html/dist',

  
   
}