<template>
  <div class="app-container">
    <treeLabel></treeLabel>
    <!--<div class="back">-->
      <!--<el-button @click="goback">返回</el-button>-->
    <!--</div>-->
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
.back{
  text-align: center;
  margin-top: 30px;
}
</style>
