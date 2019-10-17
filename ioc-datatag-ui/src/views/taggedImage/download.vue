<template>
  <div class="app-container">
    <div class="actionBar">
      <el-input
        class="zxinp moduleOne"
        size="small"
        clearable
        placeholder="请输入内容"
        prefix-icon="el-icon-search"
        @keyup.enter.native="getQuireData"
        v-model="input2">
      </el-input>
      <el-button class="zxlistBtn" size="small" type="primary" @click="getQuireData">查询</el-button>
      <el-button size="small" type="primary" @click="goback">返回</el-button>
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
          <el-table-column prop="tagsName" label="名称"></el-table-column>
          <el-table-column prop="isShare" label="下载数据量"></el-table-column>
          <el-table-column prop="modifyTime" label="下载时间"></el-table-column>
          <el-table-column prop="synopsis" label="文件大小"></el-table-column>
          <el-table-column prop="source" label="打包进度">
            <template slot-scope="scope">
              <div class="gress">
                <div class="gressPercentage">
                  <el-progress :percentage="scope.row.percentage" :show-text="false"
                               :color="customColorMethod"></el-progress>
                </div>
                <div>{{scope.row.popularity}}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180px">
            <template slot-scope="scope">
              <div>
                <div v-if="scope.row.completeTime">{{scope.row.completeTime}}</div>
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
        labelId: 0,
        isShare: false,
        labelName: '',
        totalnum: 0,
        eq_isShare: '',
        keyword: '',
        page: 0,
        size: 10,
        input2: '',
        tagName:'',
        delTreeId:'',
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
      goback(){
        this.$router.go(-1)
      },
      // 我的标签列表数据
      async getTagsData() {
        const params = {
          eq_isShare: this.eq_isShare,
          keyword: this.keyword,
          page: this.page,
          size: this.size
        }
        try {
          const data = await getTagsData(params)
          if (data.rows && data.rows.length > 0) {
            this.ztableShowList = data.rows
            this.totalnum = data.total
            data.rows.map(item => {
              if (item.popularityLevel == 0) {
                item.popularityLevel = 0
              }
              if (item.popularityLevel == 1) {
                item.popularityLevel = 25
              }
              if (item.popularityLevel == 2) {
                item.popularityLevel = 50
              }
              if (item.popularityLevel == 3) {
                item.popularityLevel = 75
              }
              if (item.popularityLevel == 4) {
                item.popularityLevel = 100
              }
            })
          } else {
            this.ztableShowList = []
            this.Loading = false
            this.totalnum = 0
          }

        } catch (e) {

        }
      },
      //查询
      getQuireData() {
        this.page = 0
        this.eq_isShare = this.value
        this.keyword = this.input2
        this.getTagsData()
      },
      //点击分页跳转
      handleCurrentChange(page) {
        this.page = page - 1
        this.getTagsData()
      },
      handleSizeChange (size) {
        this.size = size
        this.getTagsData()
      },
      //点击分页确认
      goPage() {},
    },
    created() {
      this.getTagsData()
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
    display: flex;
    justify-content: flex-end;
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

  .iconLogo {
    font-size: 18px;
    margin-left: 24px;
    cursor: pointer;
  }

  .area, .dateInp {
    width: 360px;
  }
</style>
