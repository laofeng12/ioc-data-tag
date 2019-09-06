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
        <div>
          <p class="numDetail"><span>{{titleName}}</span><span>的画像详情</span></p>
          <div class="circularContent">
            <div class="circular">
              <div class="circularOne">
                <div>
                  <el-button class="peopleDetailid" size="mini" round><span>ID：</span><span>{{pKey}}</span></el-button>
                  <el-button v-for="(item,key,index) in leftListarr" :class='"peopleDetail"+(1+index)' size="mini" round
                             :title="`${key}：${item}`">{{key}}：{{item}}
                  </el-button>
                  <!--<el-button class="peopleDetailid" size="mini" round><span>ID：</span><span>2222</span></el-button>-->
                  <!--<el-button class="peopleDetail1" size="mini" round>33</el-button>-->
                  <!--<el-button class="peopleDetail2" size="mini" round>tb_0_MODULE_CREATETIME：33</el-button>-->
                  <!--<el-button class="peopleDetail2" size="mini" round>tb_0_MODULE_CREATETIME：张晓非</el-button>-->
                  <!--<el-button class="peopleDetail3" size="mini" round>tb_0_MODULE_CREATETIME：女</el-button>-->
                  <!--<el-button class="peopleDetail4" size="mini" round>tb_0_MODULE_CREATETIME：28岁</el-button>-->
                </div>
                <div class="circularTwo">
                  <img class="line" src="../../assets/img/line.png" height="44" width="120"/>
                  <div class="circulartwoSmall">
                    <div class="circulartwoSmall2"></div>
                  </div>
                  <div class="circularThree">
                    <div class="dataImage">数据画像</div>
                  </div>
                </div>
                <div class="content">
                  <div style="position:absolute;left: 360px;top:0px">
                    <el-button v-for="(item,key,index) in rightListarr" :key="'info2-'+index"
                               :class='"contentDetai"+(1+index)' size="mini" round v-if="index < 10"
                               :title="`${key}：${item}`">{{key}}：{{item}}
                    </el-button>
                    <!--<el-button class="contentDetai1" size="mini" round>tb_0_MODULE_CODE：美容顾问美容顾问</el-button>-->
                    <!--<el-button class="contentDetai2" size="mini" round>tb_0_MODULE_CODE：租房</el-button>-->
                    <!--<el-button class="contentDetai3" size="mini" round>tb_0_MODULE_CODE：工作5年</el-button>-->
                    <!--<el-button class="contentDetai4" size="mini" round>tb_0_MODULE_CODE：乐观派</el-button>-->
                    <!--<el-button class="contentDetai5" size="mini" round>tb_0_MODULE_CODE：月收入2000元</el-button>-->
                    <!--<el-button class="contentDetai6" size="mini" round>tb_0_MODULE_CODE：未婚6</el-button>-->
                    <!--<el-button class="contentDetai7" size="mini" round>tb_0_MODULE_CODE：未婚7</el-button>-->
                    <!--<el-button class="contentDetai8" size="mini" round>tb_0_MODULE_CODE：单身8</el-button>-->
                    <!--<el-button class="contentDetai9" size="mini" round>tb_0_MODULE_CODE：单身9</el-button>-->
                  </div>
                  <!--// 增加-->
                  <div style="position:absolute;left: 600px;top:0px">
                    <el-button v-for="(item,key,index) in rightListarr" :key="'info2-'+index"
                               :class='"contentDetai"+(1+index)' size="mini" round v-if="9 < index && index < 20"
                               :title="`${key}：${item}`">{{key}}：{{item}}
                    </el-button>
                  </div>
                  <div style="position:absolute;left: 840px;top:0px">
                    <el-button v-for="(item,key,index) in rightListarr" :key="'info2-'+index"
                               :class='"contentDetai"+(1+index)' size="mini" round v-if="19 < index && index < 30"
                               :title="`${key}：${item}`">{{key}}：{{item}}
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {getImagelist} from '@/api/lableImage.js'

  export default {
    name: 'lookImagedetail',
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
      goback() {
        this.$router.go(-1)
      },
      async getpictureList() {
        try {
          const resDetail = await getImagelist({
            pKey: this.pKey,
            // pKey:'714477082390166',
            tableName: this.tableName
            // tableName:'DT_1643878'
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
