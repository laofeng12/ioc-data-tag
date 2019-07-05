<template>
  <ch-login :bgData="bgData" :loading="loading" :login="login"></ch-login>
</template>

<script>
export default {
  data () {
    return {
      bgData: [
        { title: '方法 · 路径 · 目标', describe: '数据驱动服务效率提升', src: require(`@/assets/img/bitmap_1.png`) },
        { title: '数据智能化', describe: '数据驱动服务效率提升', src: require(`@/assets/img/bitmap_3.png`) },
        { title: '多业务协同', describe: '数据驱动服务效率提升', src: require(`@/assets/img/bitmap_2.png`) }
      ],
      loading: false
    }
  },
  methods: {
    login (data) {
      this.loading = true
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
