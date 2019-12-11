<template>
  <div class="app-container">
    <div>
      <!--<div class="queryTitle">画像查询</div>-->
      <div class="queryIt">
        <div class="name">画像查询</div>
        <div class="queryInp">
          <el-input
            class="zxinp moduleOne"
            size="small"
            clearable
            placeholder="请输入需要查询的画像主键"
            prefix-icon="el-icon-search"
            @keyup.enter.native="queryDetail"
            v-model.trim="input2">
          </el-input>
          <el-button class="zxlistBtn" size="small" type="primary" @click="queryDetail">查询</el-button>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
  export default {
    components: {},
    name: "portraitQuery",
    data() {
      return {
        input2: '',
      }
    },
    methods: {
      queryDetail(){
        if(this.input2){
          this.$router.push({
            path:'/queryImage',
            query:{
              id:this.input2 ,
            }
          })
        }else{
          this.$message({
            message: '请填写查询信息',
            type: 'warning'
          });
        }
      }
    },
    created() {},
    computed: {},
    watch: {},
    mounted() {
    }
  }
</script>

<style scoped>
  .zxinp {
    /*width: 500px;*/
    width: 482px;
  }
  .queryTitle {
    font-size: 24px;
    font-weight: 700;
    color: #FF0000;
    text-align: center;
    margin-top: 194px;
  }
  .name{
    font-size: 36px;
    color: #262626;
    letter-spacing: 30px;
    font-family: PingFangSC-Medium;
  }
  .queryIt {
    text-align: center;
    margin-top: 200px;
  }
  .queryInp{
    margin-top: 24px;
  }
</style>
