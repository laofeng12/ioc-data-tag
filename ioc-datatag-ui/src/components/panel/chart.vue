<template>
  <div class="chart">
    <div id="myChart" :style="{width: '100%', height: '256px'}"></div>
  </div>
</template>

<script>
  import echarts from 'echarts'
  import {labelChange} from '@/api/tagPanel.js'
  export default {
    name: 'chart',
    data() {
      return {
        arr:[],
        newArr:[]
      }
    },
    mounted() {

    },
    methods: {
      drawLine() {
        const dom = document.getElementById("myChart");
        const myChart = echarts.init(dom);
        const option = {
          color: ['#3398DB'],
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '2%',
            right: '0%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: [
            {
              type: 'category',
              data: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
              axisTick: {
                alignWithLabel: true
              }
            }
          ],
          yAxis: [
            {
              type: 'value',
              "axisLine": {
                "show": false
              },
              "axisTick": {
                "show": false
              },
              "splitLine": {
                "show": true,
                lineStyle: {
                  color: ['#CCCCCC'],  //网格线
                  width: 1,
                  type:'dashed'
                }
              }
            },

          ],
          series: [
            {
              type: 'bar',
              barWidth: '60%',
              data: this.arr,
            }
          ]
        };
        // 为echarts对象加载数据
        myChart.setOption(option);
        window.onresize = function(){
          myChart.resize();
        }
      },
      async change(){
        try{
          const arr = await labelChange()
          this.arr = arr.data
          await this.drawLine()
        }catch (e) {
          console.log(e);
        }
      }
    },
    created() {
     this.change()
    },
    computed: {
    },
  }
</script>

<style scoped>
  .chart {
    width: 100%;
  }
</style>
