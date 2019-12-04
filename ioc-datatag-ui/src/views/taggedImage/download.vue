<template>
  <div class="app-container">
    <div class="clearfix">
      <div class="searchBar">
        <el-input
          class="zxinp moduleOne"
          size="small"
          clearable
          placeholder="请输入查询名称"
          prefix-icon="el-icon-search"
          @keyup.enter.native="getQuireData"
          v-model="input2">
        </el-input>
        <el-button class="zxlistBtn" size="small" type="primary" @click="getQuireData">查询</el-button>
      </div>
      <div class="actionBar">
        <el-button size="small" type="primary" @click="goback">返回</el-button>
      </div>
    </div>
    <div class="tableBar">
      <div class="newTable  daList">
        <el-table ref="multipleTable" :data="ztableShowList" border stripe tooltip-effect="dark"
                  style="width: 100%;text-align: center"
                  :header-cell-style="{background:'#f0f2f5'}">
          <template slot="empty">
            <div v-if="Loading">
              <div v-loading="saveLoading2"></div>
            </div>
            <div v-else>暂无数据</div>
          </template>
          <el-table-column prop="bname" label="模型名称"></el-table-column>
          <el-table-column prop="downloadNum" label="下载数据量"></el-table-column>
          <el-table-column prop="createTime" label="下载时间"></el-table-column>
          <el-table-column prop="fileSize" label="文件大小"></el-table-column>
          <el-table-column prop="speedOfProgress" label="打包进度">
            <template slot-scope="scope">
              <div class="gress">
                <div class="gressPercentage">
                  <el-progress :percentage="scope.row.speedOfProgress"
                               :color="customColorMethod" :format="format"></el-progress>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180px">
            <template slot-scope="scope">
              <div>
                <div v-if="scope.row.speedOfProgress == '100'" class="export"
                     @click="exportData(scope.row.bname,scope.row.bid)">导出到本地
                </div>
                <div v-else>-</div>
              </div>
            </template>
          </el-table-column>
        </el-table>
        <element-pagination
          :pageSize="size"
          :currentPage="page+1"
          :total="totalnum"
          @handleSizeChange="handleSizeChange"
          @handleCurrentChange="handleCurrentChange"
        ></element-pagination>
      </div>
    </div>
  </div>
</template>

<script>
  import ElementPagination from '@/components/ElementPagination'
  import {getdownList, exportList} from '@/api/down'

  export default {
    components: {ElementPagination},
    name: "download",
    data() {
      return {
        timer: null,
        labelId: 0,
        isShare: false,
        labelName: '',
        totalnum: 0,
        eq_isShare: '',
        keyword: '',
        page: 0,
        size: 10,
        input2: '',
        tagName: '',
        delTreeId: '',
        Loading: true,
        saveLoading2: true,
        shareDialog: false,
        saveLoading: false,
        percentage: '',
        value1: '',
        textarea: '',
        ztableShowList: [],
      }
    },
    methods: {
      customColorMethod(percentage) {
        if (percentage < 25) {
          return '#909399';
        } else if (percentage < 75) {
          return '#e6a23c';
        } else {
          return '#67c23a';
        }
      },
      format(percentage) {
        return percentage === 100 ? '完成' : `${percentage}%`;
      },
      goback() {
        this.$router.go(-1)
      },
      // 我的标签列表数据
      async gettableList() {
        const params = {
          like_bname: this.input2,
          page: this.page,
          size: this.size
        }
        try {
          const data = await getdownList(params)
          if (data.rows && data.rows.length > 0) {
            data.rows.map(item => {
              item.speedOfProgress = JSON.parse(item.speedOfProgress)
            })
            this.ztableShowList = data.rows
            this.totalnum = data.total
          } else {
            this.ztableShowList = []
            this.Loading = false
            this.totalnum = 0
          }

        } catch (e) {

        }
      },
      // 查询
      getQuireData() {
        this.page = 0
        this.gettableList()
      },
      // 点击分页跳转
      handleCurrentChange(page) {
        this.page = page - 1
        this.gettableList()
      },
      handleSizeChange(size) {
        this.size = size
        this.gettableList()
      },
      // 点击分页确认
      goPage() {
      },
      /**
       * 数据导出
       * @param name
       * @param taggingModelId
       * @returns {Promise<void>}
       */
      async exportData(name, taggingModelId) {
        try {
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = '/datatagweb/datatag/dowload/downloadQueue/dowloadToLocal/' + taggingModelId
          link.setAttribute('download', name + '.zip')
          document.body.appendChild(link)
          link.click()
          this.$message({
            message: '导出成功',
            type: 'success'
          })
        } catch (e) {
          this.$message.error('导出失败,请重新操作！');
        }
      },
      Timer() {
        this.timer = setInterval(() => {
          this.gettableList()
        }, 8000)
      },
    },
    destroyed() {
      clearInterval(this.timer)
    },
    created() {
      this.gettableList()
      this.Timer()
    },
    computed: {},
    watch: {},
    mounted() {
    }
  }
</script>

<style scoped>
  .zxinp {
    width: 240px;
  }

  .zxlistBtn {
    margin-left: 10px;
  }

  .actionBar {
    float: right;
  }

  .searchBar {
    float: left;
  }

  .tagSelect {
    margin-left: 10px;
    margin-right: 10px;
  }

  .gress {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-around;
  }

  .gressPercentage {
    width: 60%;
  }

  .export {
    cursor: pointer;
    color: #0479e5;
  }

  .iconLogo {
    font-size: 18px;
    margin-left: 24px;
    cursor: pointer;
  }

  .area, .dateInp {
    width: 360px;
  }
</style>
