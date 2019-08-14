<template>
  <div class="app-container">
    <div>
      <div class="queryTitle">画像查询</div>
      <div class="queryIt">
        <el-input
          class="zxinp moduleOne"
          size="small"
          placeholder="请输入需要查询的ID"
          prefix-icon="el-icon-search"
          v-model="input2">
        </el-input>
        <el-button class="zxlistBtn" size="small" type="primary" @click="queryDetail">查询</el-button>
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
    width: 500px;
  }

  .zxlistBtn {
    margin-left: 10px;
  }

  .queryTitle {
    font-size: 24px;
    font-weight: 700;
    color: #FF0000;
    text-align: center;
    margin-top: 194px;
  }

  .queryIt {
    display: flex;
    justify-content: center;
    margin-top: 25px;
  }
</style>
