<template>
  <div class="app-container">
    <treeLabel></treeLabel>
    <div style="text-align:center">
      <el-button @click="goback">返回</el-button>
    </div>
  </div>
</template>

<script>
  import treeLabel from '@/components/treeLabel/treeLabel'

  export default {
    components: {
      treeLabel
    },
    name: 'tree',
    data() {
      return {
      }
    },
    mounted() {

    },
    methods: {
      goback(){
       // this.$router.push('/lableImage')
        this.$router.go(-1)
      }
    }
  }
</script>

<style scoped>

</style>
