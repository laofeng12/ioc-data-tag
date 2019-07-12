<template>
  <div class="app-container">
    <div v-show="query">
      <div class="queryTitle">画像查询</div>
      <div class="queryIt">
        <el-input
          class="zxinp moduleOne"
          size="small"
          placeholder="请输入内容"
          prefix-icon="el-icon-search"
          v-model="input2">
        </el-input>
        <el-button class="zxlistBtn" size="small" type="primary" @click="queryDetail">查询</el-button>
      </div>
    </div>
    <queryImage v-show="queryImage"></queryImage>
  </div>
</template>

<script>
  import {mapActions, mapState, mapGetters} from 'vuex'
  import queryImage from '@/components/image/queryImage'

  export default {
    components: {
      queryImage
    },
    name: "portraitQuery",
    data() {
      return {
        input2: '',
        query: true,
        queryImage:false
      }
    },
    methods: {
      queryDetail(){
        this.queryImage = true
        this.query = false
      }
    },
    created() {
    },
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
