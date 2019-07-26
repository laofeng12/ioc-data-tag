<template>
  <div class="aside">
    <el-input placeholder="输入关键词搜索" v-model="filterText" class="search"   size="small" suffix-icon="el-icon-search"></el-input>
    <div   class="tree">
      <el-tree icon-class="el-icon-folder"  :props="props" :filter-node-method="filterNode" ref="tree"  :load="loadNode" lazy>
        <span class="custom-tree-node" slot-scope="{ node, data }">
        <span class="cus-node-title">{{ data.orgName }}</span>
          <el-button  class="set-btn" type="text" size="mini" v-if="node.level>1"  @click.stop="setTags(node,data)">
            <i class="el-icon-setting"></i>
          </el-button>
        </span>
      </el-tree>
    </div>
    <!--字段设置-->
    <el-dialog class="creat" title="字段设置"  :visible.sync="colSetDialog" width="630px" center :modal-append-to-body="false" :close-on-click-modal="false"
               @close="close">
      <div class="col-set-box">
        <el-container class="">
          <el-aside width="200px" class="left">
            <h3>选择字段</h3>
            <el-input placeholder="输入关键词搜索列表" v-model="searchText" size="small" suffix-icon="el-icon-search"></el-input>
            <div class="h4">
              <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">东城区高考成绩数据</el-checkbox>
            </div>
            <ul>
              <li v-for="city in cities">
                <el-checkbox-group v-model="checkedCities" @change="handleCheckedCitiesChange">
                  <el-checkbox  :label="city" :key="city">{{city}}</el-checkbox>
                </el-checkbox-group>
              </li>
            </ul>
          </el-aside>
          <div class="right">
            <h3>已择字段</h3>
            <div>
              选择主键
              <el-select v-model="value" filterable placeholder="请选择" size="small">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
              <el-table class="my-table"  border style="width: 100%"
                :data="tableData"
                tooltip-effect="dark">
                <el-table-column width="40"  align="center">
                  <template slot="header" slot-scope="scope">
                    <i class="el-icon-delete"></i>
                  </template>
                  <template slot-scope="scope">
                    <i class="el-icon-delete"></i>
                  </template>
                </el-table-column>
                <el-table-column
                  label="字段"  prop="colName">
                </el-table-column>
                <el-table-column
                  prop="colNameType"
                  label="类型"
                  width="100">
                </el-table-column>
                <el-table-column
                  label="选择打标字段" width="120">
                  <template slot-scope="scope">
                    <el-checkbox></el-checkbox>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-container>
      </div>
      <div slot="footer" class="dialog-footer device">
        <div>
          <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading">确认选择</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {getOneZtreeData,getChildZtreeData,getResourceListData} from '@/api/creatModel'
const cityOptions = ['上海', '北京', '广州', '深圳'];
export default {
  name: 'datasetAside',
  props: {
  },
  data () {
    return {
      dataLakeDirectoryTree:[],
      dataSetDirectoryTree:[],
      dataLakeDirectoryName:'',
      dataSetDirectoryName:'',
      saveLoading:false,
      oneNodeData:[],
      colSetDialog:false,
      filterText:'',
      props: {
        label: 'orgName',
        children: 'child',
        isLeaf: 'leaf'
      },
      searchText:'',
      checkList: ['选中且禁用','复选框 A'],
      checkAll: false,
      checkedCities: [],
      cities: cityOptions,
      isIndeterminate: true,
      options: [{
        value: '选项1',
        label: '黄金糕'
      }, {
        value: '选项2',
        label: '双皮奶'
      }, {
        value: '选项3',
        label: '蚵仔煎'
      }, {
        value: '选项4',
        label: '龙须面'
      }, {
        value: '选项5',
        label: '北京烤鸭'
      }],
      value: '',
      tableData: [{
        colName: '2016-05-03',
        colNameType: '王小虎'
      }],
      resData:[],
      treeData:[],
      multipleSelection: []
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    }
  },
  methods: {
    //全选
    handleCheckAllChange(val) {
      this.checkedCities = val ? cityOptions : [];
      this.isIndeterminate = false;
    },
    handleCheckedCitiesChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.cities.length;
      console.log(this.checkAll)
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.cities.length;
      console.log(this.checkedCities)
    },
    close(){

    },
    //树过滤
    filterNode(value, data) {
      //console.log(data)
      if (!value) return true;
      return data.orgName.indexOf(value) !== -1
    },
    //加载树节点
    loadNode(node, resolve) {
      //console.log(resolve)
      if (node.level === 0) {
        return resolve([{ orgName: '数据目录' }])
      }
      else if(node.level === 1){
        return resolve([{orgName: this.dataLakeDirectoryName,id:'1'},{orgName: this.dataSetDirectoryName,id:'2'}])
      }
      else if(node.level===2) {
        this.getThreeChild(node.data.id, resolve)
      }
      else {
       // this.getTreeChild(node.data,resolve)
        this.getChildTreeData(node.data,resolve)
      }
    },
    //获取4级树子节点
    getChildTreeData(data,resolve){
      console.log('点击当前的数据',data)
      this.getChildtreeData(data,resolve)
    },
    //获取3级树子节点
    getThreeChild(id, resolve) {
//  这里可以替换成向后台发起的请求修改data,为了演示我用的是写死的数据,获取到data后,resolve出去就好了
      if (id === '1') {
        const data=this.dataLakeDirectoryTree
        resolve(data)
      } else if(id=='2') {
        const data =this.dataSetDirectoryTree
        resolve(data)
      }
    },
    //对字段进行选择确认
    setTags(node,data){
     console.log(data)
      this.colSetDialog=true
    },
    // 获取1-3级树数据
    async getOneZtreeData() {
      try {
        const data = await getOneZtreeData()
        console.log(data)
        this.oneNodeData=data.data
        this.dataLakeDirectoryName=this.oneNodeData.dataLakeDirectoryName
        this.dataSetDirectoryName=this.oneNodeData.dataSetDirectoryName
        this.dataLakeDirectoryTree=this.oneNodeData.dataLakeDirectoryTree
        this.dataSetDirectoryTree=this.oneNodeData.dataSetDirectoryTree
      } catch (e) {

      }
    },
    // 获取4级树数据
    async getChildtreeData(data,resolve) {
      try {
        //获取资源树
       const treeData = await getChildZtreeData(data.orgId,data.databaseType)
        this.treeData=treeData.data
      //  console.log('获取树treeData',this.treeData)
        //获取资源树里面表
        const resData = await getResourceListData(data.orgId,data.type,data.databaseType)
        const resAlldata=resData.data
       // console.log('资源树所有值',resAlldata)
        resAlldata.forEach((item,index)=>{
          this.resData.push({orgName:item.resourceName,resourceId:item.resourceId})
        })
        //console.log('resData',this.resData)
        const allData=this.treeData.concat(this.resData)
        console.log('allData',allData)
         resolve(allData)
        if(this.resData.length===0){
         resolve(this.treeData)
        }

      } catch (e) {

      }
    },
    // 获取所有树表的数据
    async getResourceListData(orgId,type,databaseType) {
      try {


      } catch (e) {

      }
    }
  },
  created() {
    this.getOneZtreeData()
  },
}
</script>

<style scoped lang="scss">
  .aside {
    width: 250px;
    flex-shrink: 0;
    position: fixed;
    top: 60px;
    bottom: 0;
    left: 0;
    color: #ffffff;
    background: rgba(62, 71, 96, 1);
    padding: 10px;
    box-sizing: border-box;
    z-index: 2;
    .tree{
      height: calc(100vh - 130px);
      overflow: auto;
    }
    .search {
      margin-bottom: 20px;
    }
    .cus-node-title {
      display:inline-block;
      max-width: 150px;
      overflow: hidden;
      text-overflow: ellipsis;
      margin-top: 11px;
      color: #606266;
      font-size: 14px;
    }
    .col-set-box{
      .left{
        border-right: 1px solid #eee;
        padding: 0 10px;
        color: #000000;
      }
      h3{
        font-weight: normal;
        font-size: 14px;
        line-height: 32px;
        border-bottom: 1px solid #eee;
      }
      .h4{
        margin-top:10px;
        padding: 5px 10px;
        background-color:#f4f9fb;
        border: 1px solid #eee;
      }
      ul{
        margin: 0;
        padding: 0;
        li{
          margin: 0;
          padding: 5px 10px;
          border: 1px solid #eee;
          border-top: none;
        }
      }
    }
    .right{
      min-width: 400px;
      padding: 0 10px;
    }
    .my-table{
      i{
        cursor: pointer;
      }
    }
  }
</style>
<style lang="stylus" scoped>
  .my-table >>>
  thead
    color: #222222;
    font-size 14px
    th
      background-color: #f4f9fb
      padding: 5px 0

  // hover
  tr:hover>td
    background-color:  #f4f9fb;
</style>
