<template>
  <div class="table-container">
    <div class="header">
      <div class="left">
        <router-link class="return" to="/lableImage">
          <i class="el-icon-arrow-left"></i>
        </router-link>
        <div class="name">
          <div class="img"></div>
          <div class="text">南城区高考成绩数据</div>
        </div>
      </div>
    </div>
    <div class="content">
      <div class="components">
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
            <el-table-column label="操作" width="100px">
              <template slot-scope="props" class="caozuo">
              <span class="operationIcona  look">查看画像</span>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="名称">
              <template slot-scope="scope">
                <span>教育体系标签</span>
              </template>
            </el-table-column>
            <el-table-column prop="creator" label="创建者"></el-table-column>
            <el-table-column prop="people" label="修改人" >
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
    </div>



  </div>
</template>

<script>

  export default {
    name: 'modelEdit',
    data () {
      return {
        Loading:true,
        saveLoading2:true,
        tableBoxWidth: 800,
        tableBoxHeight: 800,
        tableWidth: 800,
        tableHeight: 800,
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

        tableData: [],
        theadData: [],
        dimensionList: [],
        measureList: [],
        componentsSwitch: true,
        datasetId: '',
        updateAsideForce: true
      }
    },
    watch: {
      theadData: function (val) {
        this.tableWidth = val.length * 149
      }
    },
    created () {
      this.datasetId = this.$route.params.id
    },
    mounted () {

    },
    methods: {


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
  .content {
    display: flex;
    .components {
      width: 100%;
      position: absolute;
      top: 60px;
      bottom: 0;
      box-sizing: border-box;
      overflow-x: hidden;
      .top {
        display: flex;
        justify-content: space-between;
        .left {
          display: flex;
          justify-content: space-around;
          align-items: center;
          font-size: 14px;
          .box {
            width: 50%;
            cursor: pointer;
            .icon {
              width: 25px;
              height: 25px;
              margin: 0 auto;
              background-size: 100% 100%;
              background-repeat: no-repeat;
            }
          }
          .right-line {
            border-right: 1px #ddd solid;
          }
        }
      }
    }
  }
  .look{
    color: #169BD5;
    cursor: pointer;
  }
  .clearfix:after{
    content: '';
    display: block;
    clear: both;
  }
</style>
