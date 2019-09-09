<template>
  <div class="app-container">
    <div class="result">
      <!--<div class="queryTitle">画像查询</div>-->
      <div class="queryIt">
        <el-input
          class="zxinp moduleOne"
          size="small"
          placeholder="请输入内容"
          prefix-icon="el-icon-search"
          v-model="input2">
        </el-input>
        <el-button class="zxlistBtn" size="small" type="primary" @click="goback">画像查询</el-button>
      </div>
    </div>
    <div>
      <p class="numDetail"><span>{{titleId}}</span><span>的画像详情</span></p>
      <div class="circularContent">
        <div class="circular">
          <div class="circularOne">
            <div v-for="(item,index) in listArr.property" :key="index">
              <el-button class="peopleDetail" size="mini" round><span>ID：</span><span>{{titleId}}</span></el-button>
              <el-button :class='"peopleDetail"+(2+index)' size="mini" round>{{item}}</el-button>
              <!--<el-button class="peopleDetail" size="mini" round><span>ID：</span><span>1234</span></el-button>-->
              <!--<el-button class="peopleDetail2" size="mini" round>张晓非</el-button>-->
              <!--<el-button class="peopleDetail3" size="mini" round>女</el-button>-->
              <!--<el-button class="peopleDetail4" size="mini" round>28岁</el-button>-->
              <!--<el-button class="peopleDetail5" size="mini" round><span>ID：</span><span>1234</span></el-button>-->
              <!--<el-button class="peopleDetail6" size="mini" round>张晓非</el-button>-->
              <!--<el-button class="peopleDetail7" size="mini" round>女</el-button>-->
              <!--<el-button class="peopleDetail8" size="mini" round>28岁</el-button>-->
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
            <div v-for="(item,index) in listArr.lists" :key="'info2-'+index">
              <el-button :class='"contentDetai"+(1+index)' size="mini" round v-if="index < 8">{{item}}</el-button>
              <!--<el-button class="contentDetai1" size="mini" round>美容顾问</el-button>-->
              <!--<el-button class="contentDetai2" size="mini" round>租房</el-button>-->
              <!--<el-button class="contentDetai3" size="mini" round>工作5年</el-button>-->
              <!--<el-button class="contentDetai4" size="mini" round>乐观派</el-button>-->
              <!--<el-button class="contentDetai5" size="mini" round>月收入2000元</el-button>-->
              <!--<el-button class="contentDetai6" size="mini" round>未婚</el-button>-->
              <!--<el-button class="contentDetai7" size="mini" round>单身</el-button>-->
            </div>
            <!--zeng-->
            <div style="position:absolute;left: 580px;top:0px">
              <div v-for="(item,index) in listArr.lists" :key="'info2-'+index">
              <el-button :class='"contentDetai"+(1+index)' size="mini" round  v-if="7 < index && index < 15">{{item}}</el-button>
            </div>
            </div>
            <div style="position:absolute;left: 800px;top:0px">
              <div v-for="(item,index) in listArr.lists" :key="'info2-'+index">
              <el-button :class='"contentDetai"+(1+index)' size="mini" round v-if="14 < index && index < 22">{{item}}</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  </div>
</template>

<script>
  import {mapActions, mapState, mapGetters} from 'vuex'

  export default {
    name: "detailImage",
    data() {
      return {
        input2: '',
        titleId:'',
        arr:[]
      }
    },
    methods: {
      ...mapActions('tagPanel', ['getimageList', 'getarrList']),
      goback() {
        this.$router.go(-1)
      }
    },
    created() {
      this.titleId = this.$route.query.detailId
      this.input2 = this.$route.query.id
      const pKey = localStorage.getItem('pKey')
      const tableName = localStorage.getItem('tableName')
      this.getarrList({
        pKey:pKey,
        tableName:tableName
      })
    },
    computed: {
      ...mapState({
        ...mapState('tagPanel', ['contentArr', 'listArr'])
      })
    },
    watch: {},
    mounted() {}
  }
</script>

<style scoped>
  .numDetail {
    font-size: 16px;
    color: #333333;
    margin-top: 30px;
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
    padding-top: 50px;
    /*margin-bottom: 135px;*/
    /*margin-left: 80px;*/
    padding-left: 80px;
    padding-right: 80px;
    overflow-x: auto;
    padding-bottom: 50px;
  }

  .peopleDetail, .peopleDetail2, .peopleDetail3, .peopleDetail4,.peopleDetail5,.peopleDetail6,.peopleDetail7,.peopleDetail8,.peopleDetail9,.peopleDetail10 {
    color: #00CCCC;
    background-color: #fff;
    border: 1px solid #fff;
    box-shadow: 0 0 0 6px #e9fcff30;
    position: absolute;
    padding: 7px 27px;
    width: 200px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    left: -266px;
  }

  .peopleDetail {
    top: 30px;
  }

  .peopleDetail2 {
    top: 80px;
  }

  .peopleDetail3 {
    top: 130px;
  }

  .peopleDetail4 {
    top: 180px;
  }
  .peopleDetail5 {
    top: 230px;
  }
  .peopleDetail6 {
    top: 280px;
  }
  .peopleDetail7 {
    top: 330px;
  }
  .peopleDetail8 {
    top: 380px;
  }
  .peopleDetail9 {
    top: 430px;
  }
  .peopleDetail10 {
    top: 480px;
  }

  .contentDetai1 {
    color: #fff;
    background-color: rgba(0, 204, 204, 1);
    position: absolute;
    top: 15px;
    right: -135px;
    border: 1px solid #fff;
  }

  .contentDetai2 {
    color: #fff;
    background-color: rgba(255, 0, 51, 1);
    position: absolute;
    top: 54px;
    right: -210px;
    border: 1px solid #fff;
  }

  .contentDetai3 {
    color: #fff;
    background-color: rgba(90, 225, 192, 1);
    position: absolute;
    top: 93px;
    right: -180px;
    border: 1px solid #fff;
  }

  .contentDetai4 {
    color: #fff;
    background-color: rgba(90, 225, 192, 1);
    position: absolute;
    top: 132px;
    right: -235px;
    border: 1px solid #fff;
  }

  .contentDetai5 {
    color: #fff;
    background-color: rgba(0, 204, 204, 1);
    position: absolute;
    top: 170px;
    right: -197px;
    border: 1px solid #fff;
  }

  .contentDetai6 {
    color: #fff;
    background-color: rgb(152, 139, 139);
    position: absolute;
    top: 208px;
    right: -245px;
    border: 1px solid #fff;
  }

  .contentDetai7 {
    color: #fff;
    background-color: rgba(90, 225, 192, 1);
    position: absolute;
    top: 246px;
    right: -175px;
    border: 1px solid #fff;
  }

  .contentDetai1, .contentDetai2, .contentDetai3, .contentDetai4, .contentDetai5, .contentDetai6, .contentDetai7 {
    padding: 10px 27px;
    width: 200px;
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
</style>
