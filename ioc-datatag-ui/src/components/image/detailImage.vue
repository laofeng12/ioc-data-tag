<template>
  <!--<div class="app-container">-->
  <div class="">
    <div class="result">
      <div class="queryIt">
        <div class="nameImage">画像查询</div>
        <div class="queryInput">
          <el-input
            class="inputBtn moduleOne"
            size="small"
            clearable
            placeholder="请输入内容"
            prefix-icon="el-icon-search"
            @keyup.enter.native="goback"
            v-model.trim="input2">
          </el-input>
          <el-button class="zxlistBtn" size="small" type="primary" @click="goback">查询</el-button>
        </div>
      </div>
    </div>
    <!--详情开始-->
    <div class="contentD">
      <p class="numDetail"><span>{{titleId}}</span><span>的画像详情</span></p>
      <div class="allContent">
        <div class="myCon">
          <div class="information">
            <img class="iconA" src="../../assets/img/icon_corner1.png" height="16" width="16"/>
            <img class="iconB" src="../../assets/img/icon_corner2.png" height="16" width="16"/>
            <img class="iconC" src="../../assets/img/icon_corner3.png" height="16" width="16"/>
            <img class="iconD" src="../../assets/img/icon_corner4.png" height="16" width="16"/>
            <div>
              <img class="infoImage" src="../../assets/img/icon_default.png" height="54" width="54"/>
            </div>
            <div class="infoLabel">
              <span class="labelName">ID:<span>{{titleId}}</span></span>
              <span class="labelName" v-for="(item,index) in listArr.property" :key="index">{{item}}</span>
            </div>
          </div>
        </div>
        <div class="arrow">
          <img src="../../assets/img/icon_arrow.png" height="36" width="40"/></div>
      </div>
      <div class="circular">
        <hadleCircle></hadleCircle>
      </div>
    </div>
    <!--详情结束 end-->
  </div>
</template>
<script>
  import {mapActions, mapState, mapGetters} from 'vuex'
  import hadleCircle from '@/components/panel/zxcircle'

  export default {
    name: "detailImage",
    components: {
      hadleCircle
    },
    data() {
      return {
        input2: '', // 输入id
        titleId: '', // 详情ID
        arr: []
      }
    },
    methods: {
      ...mapActions('tagPanel', ['getimageList', 'getarrList']),
      goback() {
        this.$router.go(-1)
      }
    },
    created() {
      this.titleId = this.$route.query.detailId  // 详情ID
      this.input2 = this.$route.query.id      // 输入id
      const pKey = localStorage.getItem('pKey')
      const tableName = localStorage.getItem('tableName')
      this.getarrList({
        pKey: pKey,
        tableName: tableName
      })
    },
    computed: {
      ...mapState({
        ...mapState('tagPanel', ['contentArr', 'listArr'])
      })
    },
    watch: {},
    mounted() {

    }
  }
</script>

<style scoped>
  .numDetail {
    font-size: 16px;
    color: #333333;
    padding-left: 16px;
    padding-top: 18px;
  }

  .inputBtn {
    width: 482px;
  }

  .result {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    background-color: #FFF;
    padding: 16px 16px 24px 16px;
  }

  .back {
    text-align: center;
    margin-top: 20px;
  }

  .nameImage {
    font-size: 16px;
    color: #262626;
    font-family: PingFangSC-Medium;
    margin-left: -4px;
  }

  .queryInput {
    display: flex;
    margin-left: 8px;
    margin-top: 16px;
  }

  .contentD {
    background-color: #fff;
    min-height: calc(100vh - 265px);
    margin-top: 22px;
  }

  .myCon {
    text-align: center;
    padding: 0px 50px;
    margin-top: 40px;
  }

  .information {
    position: relative;
    display: inline-block;
    background-color: #fff;
    min-width: 320px;
    min-height: 154px;
    margin: 0 auto;
    box-shadow: 0 0 50px 0 rgba(4, 134, 254, 0.1);
  }

  .infoLabel {
    margin-top: 20px;
    padding: 0px 16px;
  }

  .infoImage {
    margin-top: 20px;
  }

  .labelName {
    font-family: PingFangSC-Regular;
    font-size: 14px;
    color: #0486FE;
    padding: 8px 16px;
    background: #F4FAFF;
    border: 1px solid #0486FE;
    border-radius: 18px;
    margin: 0px 8px;
    display: inline-block;
    margin-bottom: 20px;
  }

  .iconA {
    position: absolute;
    left: 0px;
    top: 0px;
  }

  .iconB {
    position: absolute;
    right: 0px;
    top: 0px;
  }

  .iconC {
    position: absolute;
    left: 0px;
    bottom: 0px;
  }

  .iconD {
    position: absolute;
    right: 0px;
    bottom: 0px;
  }

  .arrow {
    text-align: center;
    margin-bottom: 40px;
  }

  .circular {
    text-align: center;
    width: 100%;
  }
</style>
