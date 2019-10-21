<template>
  <ch-login :bgData="bgData" :loading="loading" :login="login" :sliderInfos="sliderInfos" :autoplay="10000" :portalInfo="portalInfo"></ch-login>
</template>

<script>
  export default {
    data () {
      return {
        bgData: [
          { src: require(`@/assets/img/bitmap_0.png`) },
          { src: require(`@/assets/img/bitmap_1.png`) },
          { src: require(`@/assets/img/bitmap_2.png`) }
        ],
        sliderInfos: [
          { title: '数据挖掘', describe: '发现数据更大的价值' },
          { title: '数据碰撞', describe: '让数据跟数据产生生态化反' },
          { title: '标签和画像', describe: '全方位洞察数据价值' },
          { title: 'BI分析', describe: '让数据产生决策辅助支持' },
          { title: '可视化组件', describe: '自己的展示自己定' }
        ],
        loading: false,
        portalInfo: { logo: require('@/assets/img/logo.png'), portalName: '大数据服务平台' }
      }
    },
    methods: {
      login (data) {
        this.loading = true
        console.log('data',data)
        this.$store.dispatch('Login', data).then((res) => {
          this.$message({
            type: 'success',
            duration: 500,
            message: '登录成功'
          })
          setTimeout(() => {
            this.$router.push({ path: '/' })
          }, 500)
        }).finally(() => {
          this.loading = false
        })
      }
    }
  }
</script>
