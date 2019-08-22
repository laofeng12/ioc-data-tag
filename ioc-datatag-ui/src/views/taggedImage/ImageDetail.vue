<template>
  <div class="table-container">
    <div class="header">
      <div class="left">
        <router-link class="return" to="/lableImage">
          <i class="el-icon-arrow-left"></i>
        </router-link>
        <div class="name">
          <div class="img"></div>
          <div class="text">{{tagName}}</div>
        </div>
      </div>
    </div>
    <div class="content">
      <div class="components">
        <div class="newTable  daList  imageTable" v-if="ztableShowList != ''">
          <el-table ref="multipleTable" :data="ztableShowList" border stripe tooltip-effect="dark"
                    style="width: 100%;text-align: center"
                    :header-cell-style="{background:'#f0f2f5'}">
            <template slot="empty">
              <div v-if="Loading">
                <div v-loading="saveLoading2"></div>
              </div>
              <div v-else>暂无数据</div>
            </template>
            <!--<el-table-column label="操作" width="100px">-->
              <!--<template slot-scope="props" class="caozuo">-->
                <!--<span class="operationIcona  look" @click="lookImage(props.row)">查看画像</span>-->
              <!--</template>-->
            <!--</el-table-column>-->
            <el-table-column :label="item" v-for="(item,index) in theadData" :key="index"
                             :prop="item" min-width="300" v-if="index <1">
            <template slot-scope="props" class="caozuo">
              <span class="operationIcona  look" @click="lookImage(props.row)">{{props.row[item]}}</span>
            </template>
            </el-table-column>
            <el-table-column :label="item" v-for="(item,index) in theadData" :key="index"
                             :prop="item" min-width="300" v-if="index > 0"></el-table-column>
          </el-table>
          <element-pagination :pageSize="size" :total="totalnum" @handleCurrentChange="handleCurrentChange"
                              @sureClick="goPage"></element-pagination>
        </div>
        <div v-else class="topImage">
          <img src="../../assets/img/001.png" height="144" width="160"/></div>
      </div>
    </div>
  </div>
</template>

<script>
  import ElementPagination from '@/components/ElementPagination'
  import {getlist, getImagelist} from '@/api/lableImage.js'

  export default {
    components: {ElementPagination},
    name: 'modelEdit',
    data() {
      return {
        page: 0,
        size: 15,
        totalnum: 0,
        doFalse: false,
        Loading: true,
        saveLoading2: true,
        ztableShowList: [],
        headData: [],
        tableData: [],
        theadData: [],
        dimensionList: [],
        measureList: [],
        componentsSwitch: true,
        tagId: '',
        tagName: '',
        updateAsideForce: true,
        pKey: '',
        tableName: ''
      }
    },
    watch: {},
    created() {
      this.tagId = this.$route.params.id
      this.tagName = this.$route.params.name
      this.getList()
    },
    mounted() {

    },
    methods: {
      async getList() {
        const params = {
          page: this.page,
          size: this.size,
          taggingModelId: this.tagId
        }
        try {
          //theadData
          const resList = await getlist(params)
          if ((resList.data.result.content && resList.data.result.content.length > 0) && (resList.data.cols && resList.data.cols.length > 0)) {
            this.ztableShowList = resList.data.result.content
            this.theadData = resList.data.cols
            this.doFalse = true
            this.pKey = resList.data.pKey
            this.tableName = resList.data.tableName
          } else {
            this.ztableShowList = []
            this.theadData = []
            this.doFalse = false
            // this.$message.error('请先进行数据调度');
          }

          this.totalnum = resList.data.result.totalElements
        } catch (e) {

        }
      },
      handleCurrentChange(page) {
        this.page = page - 1
        this.getList()
      },
      goPage() {
      },
      // 查看画像
      async lookImage(row) {
        const obj = row
        // console.log('44',row[this.pKey])
        this.$router.push({
          path: '/lookImagedetail',
          query: {
            pKey: row[this.pKey],
            tableName: this.tableName,
            titleName: this.tagName
          }
        })
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

  .look {
    color: #169BD5;
    cursor: pointer;
  }

  .topImage {
    text-align: center;
    position: relative;
    top: 50%;
    transform: translateY(-50%);
  }
.newTable{
  padding: 0px 15px;
}
  .clearfix:after {
    content: '';
    display: block;
    clear: both;
  }


</style>
