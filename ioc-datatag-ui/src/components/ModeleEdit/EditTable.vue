<template>
  <div>
    <el-table border class="my-table"
      :data="tableDataOne"
      style="width: 100%">
      <el-table-column
       v-for="(item,index) in theadDataOne" :prop="item.prop" :key="index">
        <template slot="header" slot-scope="scope">
          <el-dropdown @command="handleCommandTags($event,item)"  v-if="item.isTag===true">
              <span class="el-dropdown-link">
                {{item.lable}} <i class="el-icon-setting"></i>
              </span>
                        <el-dropdown-menu slot="dropdown">
                          <el-dropdown-item command="0" icon="el-icon-document-copy">克隆字段</el-dropdown-item>
                          <el-dropdown-item command="1" icon="el-icon-price-tag">字段打标</el-dropdown-item>
                          <el-dropdown-item command="2" icon="el-icon-close">清除字段</el-dropdown-item>
                        </el-dropdown-menu>
          </el-dropdown>
          <span v-else>
            {{item.lable}}
          </span>
      </template>
      </el-table-column>
    </el-table>
    <!--字段设置-->
    <el-dialog class="creat" title="数据打标"  :visible.sync="setTagsDialog" width="630px" center :modal-append-to-body="false" :close-on-click-modal="false"
               @close="close">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="选择标签组" prop="region">
          <el-col :span="11">
          <el-select v-model="ruleForm.tagTeam" filterable placeholder="请选择" size="small">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
          </el-col>
          <el-col :span="11">
            <el-link type="primary">编辑标签组</el-link>
          </el-col>
        </el-form-item>
        <el-form-item label="选择标签层" prop="region">
          <el-col :span="11">
            <el-select v-model="ruleForm.tagTeam" filterable placeholder="请选择" size="small">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="打标设置" prop="region">
          <el-col :span="11">
            <el-select v-model="ruleForm.tagTeam" filterable placeholder="请选择" size="small">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="11">
            <el-row>
              <el-button type="primary" size="small">自动打标</el-button>
              <el-button type="primary" size="small">人工打标</el-button>
            </el-row>
          </el-col>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer device">
          <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading">确认打标</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
export default {
  name: 'datasetEditable',
  props: {
  },
  data () {
    return {
      saveLoading:false,
      ruleForm:{
        tagTeam:''
      },
      rules:{

      },
      options:[],
      setTagsDialog:false,
      theadDataOne:[{
        prop:'date',
        lable:'日期',
        isTag:true
      },{
        prop:'name',
        lable:'名称',
        isTag:true
      }],
      tableDataOne: [{
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-04',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1517 弄'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1519 弄'
      }, {
        date: '2016-05-03',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1516 弄'
      }]
    }
  },
  watch: {
  },
  created () {
  },
  mounted () {
  },
  methods: {
    //字段打标
    colSetTags(){
      this.setTagsDialog=true
    },
    //下拉菜单处理
    handleCommandTags(dropIndex,data) {
      switch (dropIndex) {
        case '0': // 克隆字段
         // this.renameTreeItem(node, data, 1)
          break
        case '1': // 字段打标
          this.colSetTags()
          break
        case '2': // 清除字段
         // this.moveTreeItem(data, 1)
      }
    },
    //关闭打标
    close(){

    }
  }
}
</script>

<style scoped lang="scss">
.my-table{
  .el-icon-setting{
    color: #409eff;
    cursor: pointer;
  }
}
</style>
