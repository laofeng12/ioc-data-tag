<template>
  <div class="chart">
    <div id="myChart" :style="{width: '100%', height: '300px'}"></div>
  </div>
</template>

<script>
  import echarts from 'echarts'

  export default {
    name: 'chart',
    data() {
      return {}
    },
    mounted() {
      this.drawLine()
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
              data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri'],
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
                "show": false
              }
            },

          ],
          series: [
            {
              name: '直接访问',
              type: 'bar',
              barWidth: '60%',
              data: [0, 52, 200, 334, 390, 330, 220, 10, 52, 200, 334, 390],
              markLine: {
                symbol: ['none', 'none'], //标示线，虚线
                itemStyle: {
                  normal: {
                    lineStyle:
                      {
                        type: 'dotted',
                      },
                    label:
                      {
                        show: false,
                      }
                  }
                },
                data: [{
                  yAxis: 100
                }, {
                  yAxis: 200
                }, {
                  yAxis: 300
                }, {
                  yAxis: 400
                }]
              }
            }
          ]
        };
        // 为echarts对象加载数据
        myChart.setOption(option)
      }
    }
  }
</script>

<style scoped>
  .chart {
    width: 100%;
  }
</style>
