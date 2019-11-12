<template>
  <!--<div class="app-container">-->
  <div class="">
    <div class="content">
      <div class="dataChange">
        <div class="changeOne">
          <div class="distance">
            <div class="title">使用数据集</div>
            <div class="name">上个月的统计数据</div>
          </div>
          <div>
            <span class="num numA">{{growth.growthOne}}</span>
          </div>
        </div>
        <div class="changeTwo">
          <div class="distance">
            <div class="title">使用标签/使用数据集</div>
            <div class="name">上个月的统计数据</div>
          </div>
          <div>
            <span class="num numB">{{growth.growthTwo}}</span>
          </div>
        </div>
        <div class="changeThree">
          <div class="distance">
            <div class="title">新增唯一标签</div>
            <div class="name">上个月的统计数据</div>
          </div>
          <div>
            <span class="num numC">{{growth.growthThree}}</span>
          </div>
        </div>
      </div>
      <div class="histogram">
        <div class="histogramTag">
          <div class="labelchangeTitle">标签变化</div>
          <div class="labelTag">
            <div class="labelchangeOne clearfix">
              <div class="labelName">全部标签</div>
              <div class="labelNum">{{num.numOne}}</div>
            </div>
            <div class="labelchangeTwo clearfix">
              <div class="labelName">过去一年</div>
              <div class="labelNum">{{num.numThree}}</div>
            </div>
            <div class="labelchangeThree clearfix">
              <div class="labelName">过去一个月</div>
              <div class="labelNum">{{num.numTwo}}</div>
            </div>
          </div>
        </div>
        <div class="barGraph">
          <chart></chart>
        </div>
      </div>
      <div class="label">
        <div class="labelchangeTitle">热门标签</div>
        <div class="hotLabel">
            <div class="hotOne hotOneleft">
              <div class="barGraphOne">
                <div class="titleName">今天</div>
                <div class="modelLabel">
                  <div class="contentSpacing" v-for="(item,index) in tablelistOne" :key="index"><span class="titleContent">{{item}}</span></div>
                </div>
              </div>
            </div>
          <div class="hotOne">
            <div class="barGraphOne">
              <div class="titleName">昨天</div>
              <div class="modelLabel">
                <div class="contentSpacing" v-for="(item,index) in tablelistTwo" :key="index"><span class="titleContent">{{item}}</span></div>
              </div>
            </div>
          </div>
          <div class="hotOne">
            <div class="barGraphOne">
              <div class="titleName">最近一个星期</div>
              <div class="modelLabel">
                <div class="contentSpacing" v-for="(item,index) in tablelistThree" :key="index"><span class="titleContent">{{item}}</span></div>
              </div>
            </div>
          </div>
          <div class="hotOne">
            <div class="barGraphOne">
              <div class="titleName">最近一个月</div>
              <div class="modelLabel">
                <div class="contentSpacing" v-for="(item,index) in tablelistFouth" :key="index"><span class="titleContent">{{item}}</span></div>
              </div>
            </div>
          </div>
          <div class="hotOne hotOneright">
            <div class="barGraphOne">
              <div class="titleName">最近一年</div>
              <div class="modelLabel">
                <div class="contentSpacing" v-for="(item,index) in tablelistFifth" :key="index"><span class="titleContent">{{item}}</span></div>
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
  import chart from '@/components/panel/chart'
  import {labelNum,getToday,getYesterday,getWeek,getMonth,getYear} from '@/api/tagPanel.js'
  export default {
    components: {
      chart
    },
    name: 'tagPanel',
    data() {
      return {
        tablelistOne:[], // 今天
        tablelistTwo:[], // 昨天
        tablelistThree:[], // 最近一个星期
        tablelistFouth:[], // 最近一个月
        tablelistFifth:[], // 最近一年
        num:{
          numOne:'',
          numTwo:'',
          numThree:''
        }
      }
    },
    methods: {
      ...mapActions('tagPanel', ['getgrowthOne','getgrowthTwo','getgrowthThree','getlabelChange']),
      async labelNum(){
        try{
          const res = await labelNum()
          this.num.numOne = res.data.ALL_TAG_NB  //全部标签
          this.num.numTwo = res.data.MONTH_TAG_NB //过去一个月
          this.num.numThree = res.data.YEAR_TAG_NB //过去一年
        }catch (e) {
          console.log(e);
        }
      },
      async labelDate(){
        try{
          const todayList = await getToday()
          const yesterdayList = await getYesterday()
          const weekList = await getWeek()
          const monthList = await getMonth()
          const yearList = await getYear()
          this.tablelistOne = todayList.data  // 今天
          this.tablelistTwo = yesterdayList.data  // 昨天
          this.tablelistThree = weekList.data  // 最近一个星期
          this.tablelistFouth = monthList.data // 最近一个月
          this.tablelistFifth = yearList.data // 最近一年
        }catch (e) {
          console.log(e);
        }
      }
    },
    async created() {
      await this.getgrowthOne()
      await this.getgrowthTwo()
      await this.getgrowthThree()
      this.labelNum()
      this.labelDate()
    },
    computed: {
      ...mapState('tagPanel', ['growth','labelChange']),
    },
    mounted() {

    },
  }
</script>

<style scoped>
  .content {
    width: 100%;
    height: 100%;
    text-align: center;
  }

  .dataChange {
    display: flex;
    justify-content: space-between;
    background-color: #fff;
    padding: 24px;
  }

  .changeOne, .changeTwo, .changeThree {
    display: flex;
    align-items: center;
    width: 100%;
    height: 120px;
    justify-content: center;
    background: #FFFFFF;
    border: 1px solid #D9D9D9;
  }

  .title {
    font-size: 16px;
    color: #ffffff;
    padding-bottom: 8px;
    font-family: PingFangSC-Regular;
    color: #262626;
  }

  .name {
    font-family: PingFangSC-Regular;
    font-size: 14px;
    color: #999999;
  }

  .num {
    font-size: 26px;
    color: #ffffff;
    margin-left: 24px;
  }
  .numA{
    color: #FFCC33;
  }
  .numB{
    color: #6699FD;
  }
  .numC{
    color: #2CC048;
  }

  .histogram{
    display: flex;
  }
  .label{
    text-align: left;
  }
  .histogram, .label {
    width: 100%;
    margin-top: 16px;
    background-color: #fff;
    padding: 24px;
  }
.histogramTag{
  font-family: PingFangSC-Medium;
  font-size: 16px;
  color: #262626;
  text-align: left;
}

.labelTag{
  width: 240px;
  height: 208px;
  background: #F6F8FA;
  padding: 24px;
  margin-top: 16px;
}

.hotLabel{
  width: 100%;
  margin-top: 16px;
  display: flex;
}
.hotOneleft{
  margin-left: 0px !important;
}
.hotOneright{
  margin-right: 0px !important;
}
.hotOne{
  width: 345px;
  min-height: 208px;
  background: #F6F8FA;
  margin: 0px 12px;
}
  .barGraph {
    width: 100%;
  }

  .labelchangeTitle {
    font-family: PingFangSC-Medium;
    font-size: 16px;
    color: #262626;
    margin-left: -8px;
    margin-top: -8px;
  }

  .labelName{
    font-family: PingFangSC-Regular;
    font-size: 14px;
    color: #303133;
    margin-bottom: 20px;
    float: left;
  }
  .labelNum{
    float: right;
    font-family: PingFangSC-Regular;
    font-size: 14px;
    color: #0486FE;
  }

  .titleName {
    font-family: PingFangSC-Regular;
    font-size: 14px;
    color: #303133;
    text-align: center;
    padding-top: 14px;
    margin-bottom: 40px;
  }

  .titleContent {
    font-family: PingFangSC-Regular;
    font-size: 12px;
    color: #0486FE;
    background: #F4FAFF;
    border: 1px solid #0486FE;
    padding: 4px 14px;
  }
  .modelLabel{
    padding: 0px 14px;
  }
  .contentSpacing {
    margin-bottom: 12px;
    display: inline-block;
    margin-left: 16px;
  }
  .clearfix:after{
    content: '';
    display: block;
    clear: both;
  }

</style>
