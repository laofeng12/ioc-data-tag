<template>
  <div class='app-container'>
    <div class="result">
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
    <div class="back">
      <!--<el-button size="small" @click="goback" v-if="contentArr != ''">返回</el-button>-->
      <el-button size="small" @click="goback">返回</el-button>
    </div>
    <div class="queryCard">
      <el-card class="box-card card2" v-for="(item,index) in contentArr" :key="index">
        <div class="numId">
          <span>ID：</span>
          <span>{{item.id}}</span>
        </div>
        <div class="numidContent">{{item.title}}</div>
        <div class="peopleContent">
          <el-tag class="people" v-for="(name,key) in item.lists" :key="key">{{name}}</el-tag>
          <el-tag class="people">....</el-tag>
        </div>
        <div>
          <el-button type="text" @click="lookDetail(item.id,item.tableName)">查看详情</el-button>
        </div>
      </el-card>
    </div>

  </div>
</template>

<script>
  import {mapActions, mapState, mapGetters} from 'vuex'
  export default {
    components: {},
    name: "queryImage",
    data() {
      return {
        input2: ''
      }
    },
    methods: {
      ...mapActions('tagPanel', ['getimageList','getarrList']),
      queryDetail() {
        this.getimageList(this.input2)
      },
      lookDetail(detailId,tableName) {
        localStorage.setItem('pKey',detailId)
        localStorage.setItem('tableName',tableName)
        this.getarrList({
          pKey:detailId,
          tableName:tableName
        })
        this.$router.push({
          path:'/detailImage',
          query:{
            detailId:detailId,
            id:this.input2
          }
        })
      },
      goback() {
        this.$router.go(-1)
      },
    },
    created() {
      this.input2 = this.$route.query.id
      this.queryDetail()
    },
    computed: {
      ...mapState('tagPanel', ['contentArr','listArr'])
    },
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
    font-size: 18px;
    font-weight: 700;
    color: #FF0000;
    margin-right: 20px;
  }

  .result {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }

  .queryCard {
    margin-top: 30px;
  }

  .numId {
    font-size: 20px;
    color: #333333;
  }

  .numidContent {
    font-size: 14px;
    color: #333333;
    margin-top: 10px;
  }

  .people {
    background-color: rgba(0, 204, 204, 1);
    color: #fff;
    margin-right: 10px;
    font-size: 12px;
  }

  .peopleContent {
    margin-top: 10px;
  }

  .card2 {
    margin-bottom: 10px;
  }

  .back {
    /*text-align: center;*/
    /*margin-top: 20px;*/
    display: flex;
    justify-content: flex-end;
    margin-top: -32px;
  }
</style>
