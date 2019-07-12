<template>
  <div>
    <!--<div class="con" :style="`width: ${tableBoxWidth}px; height: ${tableBoxHeight}px`">-->
      <!--<div class="showCon" :style="`width: ${tableWidth}px; height: ${tableHeight}px`">-->
        <!--<el-table :data="tableData">-->
          <!--<el-table-column v-for="(item, index) in theadData" :key="index" :property="`column${index+1}`"-->
                           <!--:label="item.label" min-width="149" align="center"></el-table-column>-->
        <!--</el-table>-->
        <div class="newTable  daList">
          <el-table ref="multipleTable" :data="ztableShowList" border stripe tooltip-effect="dark"
                    style="width: 100%;text-align: center"
                    :header-cell-style="{background:'#f0f2f5'}">
            <template slot="empty">
              <div v-if="Loading">
                <div v-loading="saveLoading2" ></div>
              </div>
              <div v-else>暂无数据</div>
            </template>
            <el-table-column prop="name" label="名称">
              <template slot-scope="scope">
                <span>教育体系标签</span>
              </template>
            </el-table-column>
            <el-table-column prop="creator" label="创建者"></el-table-column>
            <el-table-column prop="people" label="修改人/修改时间" >
                  <div>数据搬运工</div>
            </el-table-column>
            <el-table-column prop="state" label="状态" >
              <template slot-scope="scope">
                <div class="state">
                  <div class="spot"></div>
                  <div class="stateName">{{scope.row.state}}</div>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    <!--</div>-->
  <!--</div>-->
</template>

<script>
export default {
  name: 'datasetEditable',
  props: {
    tableData: {
      type: Array,
      required: true
    },
    theadData: {
      type: Array,
      required: true
    }
  },
  data () {
    return {
      tableBoxWidth: 800,
      tableBoxHeight: 700,
      tableWidth: 800,
      tableHeight: 700,
      Loading:true,
      saveLoading2:true,
      ztableShowList:[{
        name:'教育',
        creator:'大雄',
        people:'数据搬运工',
        time:'2019/4/19  16:17:22',
        introduction:'动画的很多好很多',
        state:'运行中',
      },{
        name:'教育22',
        creator:'大雄',
        people:'数据搬运工22',
        time:'2019/4/19  16:17:22',
        introduction:'动画的很多好很多22',
        state:'运行中',
      }],
    }
  },
  watch: {
    theadData (val) {
      this.tableWidth = val.length * 140
    }
  },
  created () {
    this.tableBoxWidth = document.body.offsetWidth - 280
    this.tableBoxHeight = document.body.offsetHeight - 120
    this.tableWidth = this.theadData.length * 149
    this.tableHeight = this.tableData.length * 48
  },
  mounted () {
    // this.tableWidth = this.theadData
  },
  methods: {}
}
</script>

<style scoped lang="scss">
  .con {
    overflow: auto;
  }
</style>
