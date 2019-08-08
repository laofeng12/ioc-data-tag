<template>
  <div class="aside treeCode">
    <el-input placeholder="输入关键词搜索" v-model="filterText" class="search"   size="small" suffix-icon="el-icon-search"></el-input>
    <div   class="tree-box">
      <el-tree icon-class="el-icon-folder" class="tree" :props="props" :filter-node-method="filterNode" ref="tree"  :load="loadNode" lazy>
        <span class="custom-tree-node" slot-scope="{ node, data }">
        <span class="cus-node-title">{{ data.orgName }}</span>
          <el-button  class="set-btn" type="text" size="mini" :disabled="routerName==='editModel'" v-if="data.isTable===true"  @click.stop="setTags(0,node,data)">
            <i class="el-icon-setting"></i>
          </el-button>
        </span>
      </el-tree>
    </div>
    <!--字段设置-->
    <el-dialog class="creat" title="字段设置"  :visible.sync="colSetDialog" width="720px" center :modal-append-to-body="false" :close-on-click-modal="false"
               @close="close"  @open="init">
      <div class="col-set-box">
        <el-container class="">
          <el-aside width="250px" class="left">
            <h3>选择字段</h3>
            <el-input placeholder="输入关键词搜索列表" v-model="searchText" size="small" suffix-icon="el-icon-search"></el-input>
            <div class="h4">
              <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">{{resourceName}}</el-checkbox>
            </div>
            <ul>
              <li v-for="(item,index) in columnData"  >
                <el-checkbox-group v-model="checkedCols" @change="handleCheckedColsChange">
                  <el-checkbox  :label="item.name":key="item.id" >
                    <span class="col-name-box">
                      <span v-if="item.type==='string'" class="blue">Str.</span>
                      <span v-else-if="item.type==='number'" class="green">No.</span>
                      <span v-else="item.type==='date'" class="orange">Date.</span>
                      <span class="col-name" :title="item.name">
                    {{item.name}}
                    </span>
                    </span>
                  </el-checkbox>
                </el-checkbox-group>
              </li>
            </ul>
          </el-aside>
          <div class="right">
            <h3>已择字段</h3>
            <div>
              <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
                <el-form-item label="选择主键"  prop="pkey">
                  <el-select v-model="ruleForm.pkey" filterable placeholder="请选择主键" size="small">
                    <el-option
                      v-for="(item,index) in tableData"
                      :key="item.id"
                      :label="item.name"
                      :value="item.name">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-form>

              <el-table class="my-table"  border style="width: 100%" height="300"
                :data="tableData"
                tooltip-effect="dark">
                <el-table-column width="40"  align="center">
                  <template slot="header" slot-scope="scope">
                    <i class="el-icon-delete"></i>
                  </template>
                  <template slot-scope="scope">
                    <el-button  v-if="scope.row.name===ruleForm.pkey"  class="set-btn" type="text" :disabled="true">
                      <i  class="el-icon-delete"></i>
                    </el-button>
                    <el-button  v-else class="set-btn" type="text">
                      <i  class="el-icon-delete" @click="delCol(scope.$index)"></i>
                    </el-button>

                  </template>
                </el-table-column>
                <el-table-column
                  label="排序" width="60">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.colSort" placeholder="请输入内容"></el-input>
                  </template>
                </el-table-column>
                <el-table-column
                  label="字段"  prop="name">
                </el-table-column>
                <el-table-column
                  prop="type"
                  label="类型">
                </el-table-column>
                <el-table-column
                  label="选择打标字段" width="110">
                  <template slot-scope="scope">
                    <el-checkbox  v-model="scope.row.isMarking===1" v-if="scope.row.name===ruleForm.pkey" disabled></el-checkbox>
                    <el-checkbox  v-model="scope.row.isMarking===1" v-else @change="getCheckChange(scope.row,$event)"></el-checkbox>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-container>
      </div>
      <div slot="footer" class="dialog-footer device">
        <div>
          <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading" @click="setCols">确认选择</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {getOneZtreeData,getChildZtreeData,getResourceListData,getResourceInfoData,setColsData,getModelData} from '@/api/creatModel'
const colsOptions = [];
export default {
  name: 'datasetAside',
  props: {
    modelData: {
      type: Object,
      default: Object
    }
  },
  data () {
    return {
      sortNum:'',
      isNew:true,
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
      checkAll: false,
      checkedCols: [],
      cols: colsOptions,
      isIndeterminate: true,
      tableData: [{
        colName: '2016-05-03',
        colNameType: '王小虎'
      }],
      resData:[],
      treeData:[],
      multipleSelection: [],
      resourceName:'',
      resourceId:0,
      resourceType:0,
      columnData:[],
      checkTags:false,
      ruleForm: {
        pkey: '',
      },
      rules: {
        pkey: [
          { required: true, message: '请选择主键', trigger: 'change' }
        ]
      },
      taggingModelId:0,
      routerName:'creatModel'
    }
  },
  watch: {
    '$route' (to, from) {
      this.routerName = to.name
      if (to.name === 'editModel') {
        this.isNew=false
      }
    },
    filterText(val) {
      this.$refs.tree.filter(val)
    },
    modelData: function (newVal,oldVal) {
        this.modelData=newVal
      //console.log('新数据', this.modelData)
    },
    searchText(val) {
      this.columnData=this.fuzzyQuery(this.columnData,val)
    },
  },
  methods: {
    //初始化弹窗清空数据
    init(){
      this.searchText=''
      this.checkAll=false
      this.columnData=[]
      this.isIndeterminate=false
      this.tableData=[]
      this.ruleForm.pkey=''
      this.checkedCols=[]
      this.$nextTick(() => {
        this.$refs['ruleForm'].clearValidate()
      });
    },
    /**
     * 使用indexof方法实现模糊查询
     * @param  {Array}  list     进行查询的数组
     * @param  {String} keyWord  查询的关键词
     * @return {Array}           查询的结果
     */
    fuzzyQuery(list, keyWord) {
      const arr = []
      list.map(item => {
        if (item.name &&
          (item.name.toLowerCase().includes(keyWord) ||
            item.name.toUpperCase().includes(keyWord))) {
          arr.push(item)
        }
      })
      return arr
    },
    //全选
    handleCheckAllChange(val) {
      this.checkedCols = val ? colsOptions : [];
      this.isIndeterminate = false;
      console.log(this.checkAll)
      if(this.checkAll===true){
        this.tableData= this.columnData
        this.tableData.forEach((item,index)=>{
          //item.isChecked=false
          item.isMarking=0
        })
      }else {
        this.tableData=[]
      }
    },
    handleCheckedColsChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.cols.length;
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.cols.length;
      //console.log(this.checkedCols)
      this.tableData=[]
      console.log(value)
      this.checkedCols.forEach((citem)=>{
       this.columnData.map((item,index)=>{
         item.isMarking=0
         item.colSort=''
         if(item.name == citem){
           this.tableData.push(item)
         }
        })
      })
        this.tableData.forEach((item,index)=>{
          if(item.colSort===''){
            item.colSort=index+1
          }
          })

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
          //console.log(node.data)
      }
    },
    //获取4级树子节点
    getChildTreeData(data,resolve){
     // console.log('点击当前的数据',data)
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
    //对字段进行选择确认,type=0,新建模型，type=1编辑模型
   async setTags(type,node,data){
     // console.log(data)
      this.colSetDialog=true
     let colsData={}
     if(type===0){
        //新建模型字段确认获取数据
        colsData=await getResourceInfoData(data.resourceId,data.type)
     }else {
       //编辑模型字段确认获取数据
        const resourceId=this.modelData.resourceId
        const type=this.modelData.resourceType
        colsData=await getResourceInfoData(resourceId,type)
     }
     this.columnData=colsData.data.column
     this.resourceName=colsData.data.resourceName
     this.resourceId=colsData.data.resourceId
     this.resourceType=colsData.data.type
     this.tableData=[]
     this.columnData.forEach((item,inde)=>{
       colsOptions.push(item.name)
     })
     if(this.routerName==='editModel'){
        //获取主键值
       this.ruleForm.pkey=this.modelData.pkey
       //获取选中的字段，从接口来
       this.modelData.colList.forEach((item,index)=>{
         this.checkedCols.push(item.sourceCol)
         this.tableData.push({name:item.sourceCol,isMarking:item.isMarking,
           colId:item.colId,type:item.sourceDataType,colSort:item.colSort})
       })
     }
    },
    // 获取1-3级树数据
    async getOneZtreeData() {
      try {
        const data = await getOneZtreeData()
        //console.log(data)
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
        let allData=[]
        //获取资源树
      // console.log('data',data)
        if(data.hasOwnProperty('orgId')){
          const treeData = await getChildZtreeData(data.orgId,data.databaseType)
          this.treeData=treeData.data
        }
      //  console.log('获取树treeData',this.treeData)
        //获取资源树里面表
        if(data.hasOwnProperty('orgId')) {
          const resData = await getResourceListData(data.orgId, data.type, data.databaseType)
          const resAlldata = resData.data
          //console.log('资源树所有值',resAlldata)
          resAlldata.forEach((item,index)=>{
            this.resData.push({orgName:item.resourceName,resourceId:item.resourceId,type:data.type,isTable:item.isTable})
          })
          //console.log('resData',this.resData)
          allData=this.treeData.concat(this.resData)
        }

       // console.log('allData',allData)
         resolve(allData)

      } catch (e) {

      }
    },
    //确认选择
    setCols(){
     // console.log('resourceId',this.resourceId)
     // console.log('pkey',this.pkey)
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          const colList=[]
          this.tableData.map((item,index)=>{
            colList.push({sourceCol:item.name,
              sourceDataType:item.type,
              isMarking:item.isMarking,colId:item.colId,
              colSort:item.colSort,
              taggingModelId:this.modelData.taggingModelId
            })
          })
          console.log('colList',colList)
          if(colList.length===1){
            this.$message({
              message: '单个字段的数据集不能进行数据打标，请选择其它数据集',
              type: 'error'
            });
          }else {
            if(this.routerName==='editModel'){
              this.isNew=false
              this.taggingModelId=this.modelData.taggingModelId
            }else {
              this.taggingModelId=''
            }

            const params={
              'taggingModelId':this.taggingModelId,
              'resourceId':this.resourceId,
              'resourceName': this.resourceName,
              'modelName':this.resourceName,
              'resourceType':this.resourceType,
              'pkey':this.ruleForm.pkey,
              'isNew':this.isNew,
              'colList':colList
            }
            //debugger
            this.setColData(params)
          }

        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    // 获取所有树表的数据
    async   setColData(params) {
        const data=await setColsData(params)
          this.colSetDialog=false
          this.taggingModelId=data.taggingModelId
          const Id= this.taggingModelId
          this.$message({
                showClose: true,
                message:data.message ,
                duration:2000,
                type: 'success'
              })
          if (this.routerName === 'creatModel') {
            this.$router.push({path: `editModel/${Id}`})
          }

    },
    //选择打标字段
    getCheckChange(row,$event){
     // console.log(row)
    if(row.isMarking===0){
        row.isMarking=1
      }else {
        row.isMarking=0
      }
    },
    delCol(index){
      this.checkedCols.forEach((name,cindex)=>{
          if(name===this.tableData[index].name){
            this.checkedCols.splice(cindex,1)
          }
      })
      this.tableData.splice(index,1)
    }
  },
  created() {
    this.getOneZtreeData()

  },
  mounted(){
    this.routerName=this.$route.name
  }
}
</script>

<style scoped lang="scss">
 .col-name-box{
   display: flex;
   .col-name{
     width: 150px;
     overflow: hidden;
     text-overflow: ellipsis;
   }
  }
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
    .tree-box{
/*      overflow-y: hidden;
      overflow-x: scroll;
      width:80px;
      height: 500px;*/
    }
    .tree{
    height: calc(100vh - 130px);
      overflow: auto;
/*      min-width: 100%;
      display:inline-block !important;*/
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
/*      max-height: 350px;
      overflow: hidden;*/
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
        font-weight: bold;
      }
      ul{
        margin: 0;
        padding: 0;
        li{
          margin: 0;
          padding: 5px 10px;
          border: 1px solid #eee;
          border-top: none;
          .blue{
            color:#0486fe;
          }
          .green{
            color: green;
          }
          .orange{
            color: orange;
          }
        }
      }
    }
    .right{
      min-width: 400px;
      padding: 0 10px;
    }
    .my-table{
 /*     .el-icon-delete{
        cursor: pointer;
      }*/
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
