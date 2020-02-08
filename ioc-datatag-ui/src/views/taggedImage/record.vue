<template>
  <div class="app-container">
    <div class="clearfix">
      <div class="searchBar">
        <el-input
          class="zxinp tagSelect"
          size="small"
          clearable
          placeholder="请输入内容"
          prefix-icon="el-icon-search"
          @keyup.enter.native="shareQuery"
          v-model="input2">
        </el-input>
        <el-button class="zxlistBtn" size="small" type="primary" @click="shareQuery">查询</el-button>
      </div>
      <div class="actionBar">
        <el-button size="small" type="primary" @click="goBack">返回</el-button>
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
          <el-table-column prop="tagName" label="标签组名称">{{tagName}}</el-table-column>
          <el-table-column prop="chooseOrgNmae" label="选用部门"></el-table-column>
          <el-table-column prop="chooseUserName" label="选用人"></el-table-column>
          <el-table-column prop="chooseTime" label="选用时间"></el-table-column>
        </el-table>
        <!--<element-pagination :pageSize="size"  :total="totalnum"  @handleCurrentChange="handleCurrentChange" @sureClick="goPage"></element-pagination>-->
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
import { recordSearch } from '@/api/shareLabel.js'
import ElementPagination from '@/components/ElementPagination'

export default {
  components: { ElementPagination },
  name: 'tagManage',
  data () {
    return {
      page: 0,
      size: 10,
      totalnum: 0,
      tableId:'',
      input2: '',
      tagName:'',
      Loading: true,
      saveLoading2: true,
      ztableShowList: []
    }
  },
  methods: {
    goBack () {
      this.$router.push('shareLabel')
    },
    async handleSearch () {
      try {
        const search = await recordSearch({
          page: this.page,
          keyword: this.input2,
          size: this.size,
          eq_copiedTagg: this.tableId
        })
        this.ztableShowList = search.rows ? search.rows : []
        this.totalnum = search.total
        if (search.rows && search.rows.length > 0) {
        } else {
          this.Loading = false
        }
      } catch (e) {
        console.log(e)
        this.Loading = false
      }
    },
    handleCurrentChange (page) {
      this.page = page - 1
      this.handleSearch()
    },
    handleSizeChange (size) {
      this.size = size
      this.handleSearch()
    },
    shareQuery () {
      this.page = 0
      this.handleSearch()
    },
    goPage () {
    }
  },
  created () {
    this.tableId = this.$route.query.id
    this.tagName = this.$route.query.name
    this.handleSearch()
  },
  computed: {},
  watch: {},
  mounted () {
  }
}
</script>

<style scoped>
  .zxinp {
    width: 240px;
  }

  .zxlistBtn {
    margin-right: 10px;
  }

  .actionBar {
    float: right;
  }

  .searchBar {
    float: left;
  }

  .tagSelect {
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
    cursor: pointer;
    margin-right: 16px;
    color: #0486FE;
  }

  .area, .dateInp {
    width: 360px;
  }

  .clearfix:after {
    content: "";
    display: block;
    clear: both;
  }
</style>
