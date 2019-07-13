<template>
  <div class="app-container container">
    <p class="numDetail"><span>9527</span><span>的画像详情</span></p>
    <div class="circularContent">
      <div class="circular">
        <div class="circularOne">
          <div>
            <el-button class="peopleDetail" size="mini" round><span>ID：</span><span>7298</span></el-button>
            <el-button class="peopleDetail2" size="mini" round>张晓非</el-button>
            <el-button class="peopleDetail3" size="mini" round>女</el-button>
            <el-button class="peopleDetail4" size="mini" round>28岁</el-button>
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
          <div>
            <el-button class="contentDetail" size="mini" round>美容顾问</el-button>
            <el-button class="contentDetai2" size="mini" round>租房</el-button>
            <el-button class="contentDetai3" size="mini" round>工作5年</el-button>
            <el-button class="contentDetai4" size="mini" round>乐观派</el-button>

            <el-button class="contentDetai5" size="mini" round>月收入2000元</el-button>
            <el-button class="contentDetai6" size="mini" round>未婚</el-button>
            <el-button class="contentDetai7" size="mini" round>单身</el-button>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
  export default {
    name: "detailImage"
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
    margin-top: 50px;
  }

  .peopleDetail, .peopleDetail2, .peopleDetail3, .peopleDetail4 {
    color: #00CCCC;
    background-color: #fff;
    border: 1px solid #fff;
    box-shadow: 0 0 0 6px #e9fcff30;
    position: absolute;
  }

  .peopleDetail {
    top: 30px;
    left: -145px;
  }

  .peopleDetail2 {
    top: 80px;
    left: -145px;
  }

  .peopleDetail3 {
    top: 130px;
    left: -129px;
  }

  .peopleDetail4 {
    top: 180px;
    left: -117px;
  }

  .contentDetail {
    color: #fff;
    background-color: rgba(0, 204, 204, 1);
    position: absolute;
    top: 15px;
    right: 0px;
    border: 1px solid #fff;
  }

  .contentDetai2 {
    color: #fff;
    background-color: rgba(255, 0, 51, 1);
    position: absolute;
    top: 45px;
    right: -135px;
    border: 1px solid #fff;
  }

  .contentDetai3 {
    color: #fff;
    background-color: rgba(90, 225, 192, 1);
    position: absolute;
    top: 75px;
    right: -55px;
    border: 1px solid #fff;
  }

  .contentDetai4 {
    color: #fff;
    background-color: rgba(90, 225, 192, 1);
    position: absolute;
    top: 105px;
    right: -170px;
    border: 1px solid #fff;
  }

  .contentDetai5 {
    color: #fff;
    background-color: rgba(0, 204, 204, 1);
    position: absolute;
    top: 135px;
    right: -76px;
    border: 1px solid #fff;
  }

  .contentDetai6 {
    color: #fff;
    background-color: rgba(255, 0, 51, 1);
    position: absolute;
    top: 195px;
    right: -76px;
    border: 1px solid #fff;
  }

  .contentDetai7 {
    color: #fff;
    background-color: rgba(90, 225, 192, 1);
    position: absolute;
    top: 252px;
    right: -55px;
    border: 1px solid #fff;
  }
</style>
