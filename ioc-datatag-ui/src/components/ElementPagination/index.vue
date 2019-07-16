<template>
  <div class="ele-pagination">
    <el-pagination
      background
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="pageSize"
      layout="total, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>
    <el-button type="primary" @click="sureClick()" size="mini" v-show="isSureBtn">确定</el-button>
  </div>
</template>
<script>
export default {
  name: 'index',
  props: {
    isSureBtn: {
      type: Boolean,
      default: true
    },
    total: {
      type: Number,
      default: 40
    },
    pageSize: {
      type: Number,
      default: 10
    },
    currentPage: {
      type: Number,
      default: 1
    }
  },
  methods: {
    handleCurrentChange (page) {
      console.log(`当前页: ${page}`)
      this.$emit('handleCurrentChange', page)
    },
    handleSizeChange (pageSize) {
      console.log(`每页 ${pageSize} 条`)
      this.$emit('handleSizeChange', pageSize)
    },
    sureClick () {
      this.$emit('sureClick')
    }
  }
}
</script>

<style lang="stylus" scoped>
.ele-pagination{
  margin-top:15px;
  text-align: right;
  display: flex;
  justify-content:center
}
</style>
