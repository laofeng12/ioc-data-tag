<template>
  <!--<div class="app-container">标签仪表盘</div>-->
  <div class="app-container">
    <div class="content">
      <div class="dataChange">
        <div class="changeOne">
          <div class="distance">
            <div class="title">数据集</div>
            <div>
              <span><i class="el-icon-plus iconSize"></i></span>
              <span class="num">12</span>
              <span class="percentage">%</span>
            </div>
            <div class="name">上个月的统计数据</div>
          </div>
        </div>
        <div class="changeTwo">
          <div class="distance">
            <div class="title">标签/数据集</div>
            <div>
              <span class="num">4.2</span>
            </div>
            <div class="name">上个月的统计数据</div>
          </div>
        </div>
        <div class="changeThree">
          <div class="distance">
            <div class="title">唯一标签</div>
            <div>
              <span class="num">198</span>
            </div>
            <div class="name">上个月的统计数据</div>
          </div>
        </div>
      </div>
      <div class="histogram">
        <div class="tag">
          <div class="labelchangeTitle">标签变化</div>
          <div class="labelchangeOne">
            <span>全部标签：</span>
            <span>30134</span>
          </div>
          <div class="labelchangeTwo">
            <span>过去一年：</span>
            <span>16785</span>
          </div>
          <div class="labelchangeThree">
            <span>过去一个月：</span>
            <span>1534</span>
          </div>
        </div>
        <div class=" barGraph">
          <chart></chart>
        </div>
      </div>
      <div class="label">
        <div class="tag">
          <div class="labelchangeTitle">热门标签</div>
        </div>
        <div class=" barGraph">
          <div class="barGraphOne">
            <div class="titleName">今天</div>
            <div class="contentBtn">
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
            </div>
          </div>
          <div class="barGraphOne">
            <div class="titleName">昨天</div>
            <div class="contentBtn">
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
            </div>
          </div>
          <div class="barGraphOne">
            <div class="titleName">最后一个星期</div>
            <div class="contentBtn">
              <div class="contentSpacing"><span class="titleContent">主要按钮333</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
            </div>
          </div>
          <div class="barGraphOne">
            <div class="titleName">最后一个月</div>
            <div class="contentBtn">
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
            </div>
          </div>
          <div class="barGraphOne">
            <div class="titleName">最后一年</div>
            <div class="contentBtn">
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
              <div class="contentSpacing"><span class="titleContent">主要按钮</span></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import chart from '@/components/panel/chart'

  export default {
    components: {
      chart
    },
    name: 'tagPanel',
    data() {
      return {
        tablelist:[]
      }
    },
    mounted() {

    },
    methods: {

    }
  }
</script>

<style scoped>
  .content {
    width: 100%;
    height: 100%;
    padding-left: 40px;
    padding-right: 40px;
    text-align: center;
  }

  .dataChange {
    display: flex;
    justify-content: space-between;
  }

  .changeOne, .changeTwo, .changeThree {
    display: flex;
    align-items: center;
    width: 300px;
    height: 160px;
  }

  .changeOne {
    background-color: rgba(255, 204, 51, 1);
  }

  .changeTwo {
    background-color: rgba(102, 153, 255, 1);
  }

  .changeThree {
    background-color: rgba(43, 192, 72, 1);
  }

  .title {
    font-size: 16px;
    color: #ffffff;
  }

  .iconSize, .name {
    font-size: 16px;
    color: #ffffff;
  }

  .num, .percentage {
    font-size: 26px;
    color: #ffffff;
  }

  .distance {
    line-height: 40px;
    width: 300px;
  }

  .histogram, .label {
    width: 100%;
    display: flex;
    margin-top: 50px;
  }

  .tag {
    width: 15%;
    text-align: left;
    font-size: 14px;
    color: #a7a7a7;
  }

  .barGraph {
    width: 85%;
    display: flex;
  }

  .labelchangeTitle {
    font-size: 16px;
    color: #333333;
  }

  .labelchangeTitle, .labelchangeOne, .labelchangeTwo, .labelchangeThree {
    margin-top: 8px;
  }

  .barGraphOne {
    width: 20%;
  }

  .titleName {
    font-size: 14px;
    color: #a7a7a7;
    margin-top: 5px;
  }

  .titleContent {
    text-align: center;
    font-size: 12px;
    background-color: rgba(0, 204, 204, 1);
    color: #fff;
    padding: 4px 6px;
    border-radius: 4px;
  }

  .contentSpacing {
    margin-top: 8px;
  }

  .contentBtn {
    padding-bottom: 10px;
  }
</style>
