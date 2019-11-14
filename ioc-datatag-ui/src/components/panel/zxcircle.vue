<template>
  <div class="chart">
    <!--<div id="myCircle" :style="conheight" ></div>-->
    <div id="myCircle" :style="{width: '100%', minHeight: '100px'}" ></div>
  </div>
</template>

<script>
  import {mapActions, mapState, mapGetters} from 'vuex'
  import echarts from 'echarts'

  export default {
    name: 'zxcircle',
    data() {
      return {
        arr: [],
        conheight: {
          height: ''
        }
      }
    },
    mounted() {},
    methods: {
      ...mapActions('tagPanel', ['getimageList', 'getarrList']),
      hadCircle() {
        // 基于准备好的dom，初始化echarts实例
        const myChart = echarts.init(document.getElementById('myCircle'))
        const option = {
          backgroundColor: '#fff',
          // tooltip: {},
          animationDurationUpdate: function (idx) {
            // 越往后的数据延迟越大
            return idx * 100;
          },
          animationEasingUpdate: 'bounceIn',
          color: ['#fff', '#fff', '#fff'],
          series: [{
            type: 'graph',
            layout: 'force',
            force: {
              repulsion: 200,
              edgeLength: 10
            },
            roam: false,
            label: {
              normal: {
                show: true,
                fontSize: 12,
                formatter: val => {
                  const name = val.name
                  const threshold = 7
                  return name.length > threshold ? name.substring(0, threshold) + '\n\n' + name.substring(threshold) : name
                }
              }
            },
            data: this.arr
          }]
        }
        myChart.setOption(option);
        window.onresize = function () {
          myChart.resize();
        }
      },
      // 计算字符长度
      textLength(str) {
        let len = 0;
        for (let a of str) {
          if (/[\u4E00-\u9FA5]/.exec(a)) {
            len += 1;
          } else {
            len += 0.5;
          }
        }
        if (len < 8) {
          const numOne = {
            "name": str,
            "value": 582,
            "symbolSize": 80,
            "draggable": true,
            "itemStyle": {
              "normal": {
                "borderColor": "#69B7FF",
                "borderWidth": 4,
                "color": "#69B7FF"
              }
            }
          }
          this.arr.push(numOne)
        } else {
          const numTwo = {
            "name": str,
            "value": 1484,
            "symbolSize": 100,
            "draggable": true,
            "itemStyle": {
              "normal": {
                "borderColor": "#0486FE",
                "borderWidth": 4,
                "color": "#0486FE"
              }
            }
          }
          this.arr.push(numTwo)
        }
        let obj = 0
        this.arr.forEach(_item => {
          obj = obj + _item.symbolSize
        })
        if (obj < 100) {
          obj = 100
        }
        this.conheight.height = obj + 'px';
        return len;
      },
      async circle() {
        const pKey = localStorage.getItem('pKey')
        const tableName = localStorage.getItem('tableName')
        await this.getarrList({
          pKey: pKey,
          tableName: tableName
        })
        this.listArr.lists.map(item => {
          this.textLength(item)
        })
        await this.hadCircle()
      }
    },
    created() {
      this.circle()
    },
    computed: {
      ...mapState({
        ...mapState('tagPanel', ['contentArr', 'listArr'])
      })
    },
  }
</script>

<style scoped>
  .chart {
    width: 100%;
  }
</style>
