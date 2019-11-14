<template>
  <!--<div class="table-container">-->
  <div>
    <div class="header">
      <div class="left">
          <span class="return">
            <i class="el-icon-arrow-left" @click="goback"></i>
          </span>
        <div class="name">
          <div class="img"></div>
          <div class="text">{{titleName}}</div>
        </div>
      </div>
    </div>
    <div class="content">
      <div class="components">
        <div class="contentD">
          <p class="numDetail"><span>{{pKey}}</span><span>的画像详情</span></p>
          <div class="allContent">
            <div class="myCon">
              <div class="information">
                <img class="iconA" src="../../assets/img/icon_corner1.png" height="16" width="16"/>
                <img class="iconB" src="../../assets/img/icon_corner2.png" height="16" width="16"/>
                <img class="iconC" src="../../assets/img/icon_corner3.png" height="16" width="16"/>
                <img class="iconD" src="../../assets/img/icon_corner4.png" height="16" width="16"/>
                <div>
                  <img class="inforImage" src="../../assets/img/icon_default.png" height="54" width="54"/>
                </div>
                <div class="inforLabel">
                  <span class="labelName">ID:<span>{{pKey}}</span></span>
                  <span class="labelName" v-for="(item,index) in leftListarr" :key="index">{{item}}</span>
                </div>
              </div>
            </div>
            <div class="arrow">
              <img src="../../assets/img/icon_arrow.png" height="36" width="40"/></div>
          </div>
          <div class="quan">
            <hadleCircle></hadleCircle>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {getImagelist} from '@/api/lableImage.js'
  import hadleCircle from '@/components/panel/zxcircle'

  export default {
    name: 'lookImagedetail',
    components: {
      hadleCircle
    },
    data() {
      return {
        pKey: '',
        tableName: '',
        titleName: '',
        leftListarr: [],
        rightListarr: []
      }
    },
    watch: {},
    created() {
      this.pKey = this.$route.query.pKey
      this.tableName = this.$route.query.tableName
      this.titleName = this.$route.query.titleName
      this.getpictureList()
    },
    mounted() {

    },
    methods: {
      // 返回
      goback() {
        this.$router.go(-1)
      },
      // 获取显示数据信息
      async getpictureList() {
        try {
          const resDetail = await getImagelist({
            pKey: this.pKey,
            tableName: this.tableName
          })
          this.rightListarr = resDetail.data.mapLists // 右边
          this.leftListarr = resDetail.data.mapProperty  // 左边
        } catch (e) {

        }
      }
    }
  }
</script>

<style scoped lang="scss">
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 60px;
    color: #ffffff;
    background: rgba(22, 38, 59, 1);
    .left {
      display: flex;
      align-items: center;
      .return {
        width: 60px;
        border-right: 1px solid #999;
        text-align: center;
        font-size: 30px;
        margin-right: 20px;
      }
      .name {
        display: flex;
        text-align: center;
        .img {
          width: 25px;
          height: 25px;
          margin-right: 10px;
          background: url("../../assets/img/u71.png");
          background-repeat: no-repeat;
          background-size: 100% 100%;
        }
        .text {
          line-height: 25px;
        }
      }
    }
  }

  .clearfix:after {
    content: '';
    display: block;
    clear: both;
  }

  .numDetail {
    font-size: 16px;
    color: #333333;
    margin-top: 50px;
    margin-left: 50px;
  }

  .circular {
    margin-left: 245px;
  }

  .circularOne {
    width: 350px;
    height: 350px;
    border-radius: 50%;
    background-color: #fff;
    box-shadow: 0 0 0 20px #e9fcff30;
    position: relative;
  }

  .circularTwo {
    width: 250px;
    height: 250px;
    border: 2px dotted #00CCCC;
    border-radius: 50%;
    position: relative;
    top: 50px;
    left: 50px;
  }

  .circularThree {
    width: 150px;
    height: 150px;
    border-radius: 50%;
    position: relative;
    top: 50px;
    left: 50px;
    background-color: #F5F5F5;
  }

  .dataImage {
    text-align: center;
    line-height: 150px;
    color: #00CCCC;
  }

  .circulartwoSmall {
    width: 45px;
    height: 45px;
    border: 2px dotted #00CCCC;
    border-radius: 50%;
    position: absolute;
    top: 30px;
    background-color: #fff;
  }

  .circulartwoSmall2 {
    width: 30px;
    height: 30px;
    border-radius: 50%;
    position: absolute;
    background-color: rgba(0, 204, 204, 1);
    left: 5px;
    top: 5px;
  }

  .line {
    position: absolute;
    top: -8px;
    left: -113px;
  }

  .circularContent {
    padding-top: 120px;
    /*margin-bottom: 135px;*/
    /*margin-left: 80px;*/
    padding-left: 80px;
    padding-right: 80px;
    overflow-x: auto;
    padding-bottom: 50px;
  }

  .peopleDetailid, .peopleDetail1, .peopleDetail2, .peopleDetail3, .peopleDetail4, .peopleDetail5, .peopleDetail6, .peopleDetail7, .peopleDetail8, .peopleDetail9, .peopleDetail10 {
    color: #00CCCC;
    background-color: #fff;
    border: 1px solid #fff;
    box-shadow: 0 0 0 6px #e9fcff30;
    position: absolute;
    padding: 7px 27px;
    width: 220px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    left: -286px;
  }

  .peopleDetailid {
    top: 30px;
  }

  .peopleDetail1 {
    top: 80px;
  }

  .peopleDetail2 {
    top: 130px;
  }

  .peopleDetail3 {
    top: 180px;
  }

  .peopleDetail4 {
    top: 230px;
  }

  .peopleDetail5 {
    top: 280px;
  }

  .peopleDetail6 {
    top: 330px;
  }

  .peopleDetail7 {
    top: 380px;
  }

  .peopleDetail8 {
    top: 430px;
  }

  .peopleDetail9 {
    top: 480px;
  }

  .peopleDetail10 {
    top: 530px;
  }

  .contentDetai1 {
    color: #fff;
    background-color: rgba(0, 204, 204, 1);
    position: absolute;
    top: 15px;
    right: -170px;
    border: 1px solid #fff;
  }

  .contentDetai2 {
    color: #fff;
    background-color: rgba(255, 0, 51, 1);
    position: absolute;
    top: 45px;
    right: -252px;
    border: 1px solid #fff;
  }

  .contentDetai3 {
    color: #fff;
    background-color: rgba(90, 225, 192, 1);
    position: absolute;
    top: 75px;
    right: -206px;
    border: 1px solid #fff;
  }

  .contentDetai4 {
    color: #fff;
    background-color: rgba(90, 225, 192, 1);
    position: absolute;
    top: 105px;
    right: -292px;
    border: 1px solid #fff;
  }

  .contentDetai5 {
    color: #fff;
    background-color: rgba(0, 204, 204, 1);
    position: absolute;
    top: 135px;
    right: -226px;
    border: 1px solid #fff;
  }

  .contentDetai6 {
    color: #fff;
    background-color: #F58687;
    position: absolute;
    top: 165px;
    right: -265px;
    border: 1px solid #fff;
  }

  .contentDetai7 {
    color: #fff;
    background-color: #c594c0;
    position: absolute;
    top: 195px;
    right: -223px;
    border: 1px solid #fff;
  }

  .contentDetai8 {
    color: #fff;
    background-color: #bbd6cf;
    position: absolute;
    top: 225px;
    right: -261px;
    border: 1px solid #fff;
  }

  .contentDetai9 {
    color: #fff;
    background-color: rgba(90, 225, 192, 1);
    position: absolute;
    top: 252px;
    right: -180px;
    border: 1px solid #fff;
  }

  .contentDetai1, .contentDetai2, .contentDetai3, .contentDetai4, .contentDetai5, .contentDetai6, .contentDetai7 {
    padding: 7px 27px;
    width: 220px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

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

  .back {
    text-align: center;
    margin-top: 20px;
  }

  .peopleDetailid {

  }
</style>
<style scoped>
  .numDetail {
    font-size: 16px;
    color: #333333;
    padding-left: 16px;
    padding-top: 18px;
  }

  .zxinp {
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

  .allContent {
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

  .inforLabel {
    margin-top: 20px;
    padding: 0px 16px;
  }

  .inforImage {
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

  .quan {
    text-align: center;
    width: 100%;
  }
</style>
