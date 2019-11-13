<template>
  <div>
    <div v-show="this.routerName === 'creatModel'" class="newAside">
      <div class="asideTwo">
        <el-input placeholder="输入关键词搜索" v-model="filterText" class="search" size="small"
                  suffix-icon="el-icon-search"></el-input>
        <div class="tree-box treeCode">
          <el-tree class="tree" :props="props" :highlight-current="true"
                   :filter-node-method="filterNode"
                   ref="tree"
                   node-key="id"
                   @node-click="handleNodeClick"
                   :load="loadNode" lazy>
            <div class="custom-tree-node" slot-scope="{ node, data }">
              <i v-if="data.isTable===true" class="el-icon-coin iconImg"></i>
              <div class="cus-node-title" :title="data.orgName">{{ data.orgName }}</div>
            </div>
          </el-tree>
        </div>
      </div>
      <div class="asideTwo treeCode" style="height:calc(70vh);">
        <div class="col-set-box">
          <el-container class="">
            <el-aside width="250px" class="leftNew">
              <el-tabs v-model="activeName" @tab-click="handleClick">
                <el-tab-pane label="可用字段" name="first">
                  <el-input placeholder="输入关键词搜索列表" v-model.trim="searchText" size="small"
                            suffix-icon="el-icon-search"></el-input>
                  <div class="h4" v-show="list.length > 0">
                    <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">
                      {{resourceName}}
                    </el-checkbox>
                  </div>
                  <ul class="contentNumheight">
                    <li v-for="(item,index) in list">
                      <el-checkbox-group v-model="checkedCols" @change="handleCheckedColsChange">
                        <el-checkbox :label="item.definition" :key="item.definition">
                        <span class="col-name-box">
                          <span v-if="item.type==='string'" class="blue">Str.</span>
                          <span v-else-if="item.type==='number'" class="green">No.</span>
                          <span v-else="item.type==='date'" class="orange">Date.</span>
                          <span class="col-name" :title="item.definition">{{item.definition}}</span>
                        </span>
                        </el-checkbox>
                      </el-checkbox-group>
                    </li>
                  </ul>
                </el-tab-pane>
                <el-tab-pane label="全部字段" name="second" class="">
                  <el-input placeholder="输入关键词搜索列表" v-model.trim="searchText" size="small"
                            suffix-icon="el-icon-search"></el-input>
                  <div class="h4">
                    <span class="allNametitle">{{resourceName}}</span>
                  </div>
                  <ul class="contentNumheight">
                    <li v-for="(item,index) in allList">
                      <div class="col-name-box">
                        <div v-if="item.type==='string'" class="blue">Str.</div>
                        <div v-else-if="item.type==='number'" class="green">No.</div>
                        <div v-else="item.type==='date'" class="orange">Date.</div>
                        <div class="col-name" :title="item.definition">{{item.definition}}</div>
                        <div class="power"><span>{{powerName}}</span></div>
                      </div>
                    </li>
                  </ul>
                  <!--<div class="subscribeData" v-if="this.resourceType == 0" @click="getLink">订阅数据</div>-->
                </el-tab-pane>
              </el-tabs>
            </el-aside>

            <div class="formClass">
              <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
                <el-form-item label="选择主键" prop="pkey">
                  <el-select v-model="ruleForm.pkey" filterable placeholder="请选择主键" size="small" @change="changeSel">
                    <el-option
                      v-for="(item,index) in myData"
                      :key="item.sourceColId"
                      :label="item.definition"
                      :value="item.definition">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-form>

              <el-table class="my-table tableNew" border :style="contentStyleObj"
                        :data="tableData"
                        tooltip-effect="dark">
                <el-table-column width="60" align="center">
                  <template slot="header" slot-scope="scope">
                    <i class="el-icon-delete"></i>
                  </template>
                  <template slot-scope="scope">
                    <el-button v-if="scope.row.definition == ruleForm.pkey" class="set-btn" type="text"
                               :disabled="true">
                      <i class="el-icon-delete"></i>
                    </el-button>
                    <el-button v-else class="set-btn" type="text">
                      <i class="el-icon-delete" @click="delCol(scope.$index)"></i>
                    </el-button>

                  </template>
                </el-table-column>
                <el-table-column
                  label="排序" width="60">
                  <template slot-scope="scope">
                    <div v-model="scope.row.colSort">{{scope.row.colSort}}</div>
                  </template>
                </el-table-column>
                <el-table-column label="字段" prop="definition"></el-table-column>
                <el-table-column prop="type" label="类型">
                </el-table-column>
                <el-table-column
                  label="选择打标字段">
                  <template slot-scope="scope">
                    <el-checkbox v-if="scope.row.definition===ruleForm.pkey || ruleForm.pkey==''"
                                 disabled></el-checkbox>
                    <el-checkbox v-else @change="getCheckChange(scope.row,$event)"
                                 v-model="scope.row.isMarking"></el-checkbox>
                  </template>
                </el-table-column>
              </el-table>
            </div>

          </el-container>
        </div>
      </div>
    </div>
    <div class="btnNew" v-show="this.routerName === 'creatModel'">
      <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading" @click="setCols">确认选择
      </el-button>
    </div>
    <!--字段设置-->
    <el-dialog class="creat" title="字段设置" :visible.sync="colSetDialog" width="800px" center
               :modal-append-to-body="false" :close-on-click-modal="false"
               @close="close" @open="init">
      <div class="col-set-box">
        <el-container class="">
          <el-aside width="250px" class="left">
            <el-tabs v-model="activeName" @tab-click="handleClick">
              <el-tab-pane label="可用字段" name="first">
                <el-input placeholder="输入关键词搜索列表" v-model.trim="searchText" size="small"
                          suffix-icon="el-icon-search"></el-input>
                <div class="h4">
                  <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">
                    {{resourceName}}
                  </el-checkbox>
                </div>
                <ul class="contentNum">
                  <li v-for="(item,index) in list">
                    <el-checkbox-group v-model="checkedCols" @change="handleCheckedColsChange">
                      <el-checkbox :label="item.definition" :key="item.definition">
                        <span class="col-name-box">
                          <span v-if="item.type==='string'" class="blue">Str.</span>
                          <span v-else-if="item.type==='number'" class="green">No.</span>
                          <span v-else="item.type==='date'" class="orange">Date.</span>
                          <span class="col-name" :title="item.definition">{{item.definition}}</span>
                        </span>
                      </el-checkbox>
                    </el-checkbox-group>
                  </li>
                </ul>
              </el-tab-pane>
              <el-tab-pane label="全部字段" name="second" class="">
                <el-input placeholder="输入关键词搜索列表" v-model.trim="searchText" size="small"
                          suffix-icon="el-icon-search"></el-input>
                <div class="h4">
                  <span class="allNametitle">{{resourceName}}</span>
                </div>
                <ul class="contentNum">
                  <li v-for="(item,index) in allList">
                    <div class="col-name-box">
                      <div v-if="item.type==='string'" class="blue">Str.</div>
                      <div v-else-if="item.type==='number'" class="green">No.</div>
                      <div v-else="item.type==='date'" class="orange">Date.</div>
                      <div class="col-name" :title="item.definition">{{item.definition}}</div>
                      <div class="power"><span>{{powerName}}</span></div>
                    </div>
                  </li>
                </ul>
                <div class="subscribeData" v-if="this.resourceType == 0" @click="getLink">订阅数据</div>
              </el-tab-pane>
            </el-tabs>
          </el-aside>
          <div class="right" style="width: 100%;">
            <h3>已择字段</h3>
            <div class="formClass">
              <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
                <el-form-item label="选择主键" prop="pkey">
                  <el-select v-model="ruleForm.pkey" filterable placeholder="请选择主键" size="small" @change="changeSel">
                    <el-option
                      v-for="(item,index) in myData"
                      :key="item.sourceColId"
                      :label="item.definition"
                      :value="item.definition">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-form>

              <el-table class="my-table" border style="width: 100%" height="300"
                        :data="tableData"
                        tooltip-effect="dark">
                <el-table-column width="40" align="center">
                  <template slot="header" slot-scope="scope">
                    <i class="el-icon-delete"></i>
                  </template>
                  <template slot-scope="scope">
                    <el-button v-if="scope.row.definition == ruleForm.pkey" class="set-btn" type="text"
                               :disabled="true">
                      <i class="el-icon-delete"></i>
                    </el-button>
                    <el-button v-else class="set-btn" type="text">
                      <i class="el-icon-delete" @click="delCol(scope.$index)"></i>
                    </el-button>

                  </template>
                </el-table-column>
                <el-table-column
                  label="排序" width="50">
                  <template slot-scope="scope">
                    <div v-model="scope.row.colSort">{{scope.row.colSort}}</div>
                  </template>
                </el-table-column>
                <el-table-column label="字段" prop="definition"></el-table-column>
                <el-table-column width="80" prop="type" label="类型">
                </el-table-column>
                <el-table-column
                  label="选择打标字段" width="110">
                  <template slot-scope="scope">
                    <el-checkbox v-if="scope.row.definition===ruleForm.pkey || ruleForm.pkey==''"
                                 disabled></el-checkbox>
                    <el-checkbox v-else @change="getCheckChange(scope.row,$event)"
                                 v-model="scope.row.isMarking"></el-checkbox>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-container>
      </div>
      <div slot="footer" class="dialog-footer device">
        <div>
          <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading" @click="setCols">确认选择
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    getOneZtreeData,
    getChildZtreeData,
    getResourceListData,
    getResourceInfoData,
    setColsData,
    getModelData,
    getdatalakeLink
  } from '@/api/creatModel'

  let colsOptions = [];
  export default {
    name: 'datasetAside',
    props: {
      modelData: {
        type: Object,
        default: Object
      }
    },
    data() {
      return {
        contentStyleObj: {
          width: ''
        },
        chooseBox: [],
        powerName: '',
        activeName: 'first',
        openScope: '',
        modelId: '',
        sortNum: '',
        isNew: true,
        dataLakeDirectoryTree: [],
        dataSetDirectoryTree: [],
        dataLakeDirectoryName: '',
        dataSetDirectoryName: '',
        saveLoading: false,
        oneNodeData: [],
        colSetDialog: false,
        filterText: '',
        props: {
          label: 'orgName',
          children: 'children',
          isLeaf: 'leaf',
        },
        searchText: '',
        checkAll: false,
        checkedCols: [],
        checkIt: [],
        cols: colsOptions,
        isIndeterminate: true,
        tableData: [],
        totableData: [],
        resData: [],
        treeData: [],
        multipleSelection: [],
        resourceName: '',
        resourceId: 0,
        resourceType: 0,
        columnData: [],
        allcolumnData: [],
        myData: [],
        editData: [],
        checkTags: false,
        ruleForm: {
          pkey: '',
        },
        rules: {
          pkey: [
            {required: true, message: '请选择主键', trigger: 'change'}
          ]
        },
        taggingModelId: 0,
        routerName: 'creatModel'
      }
    },
    watch: {
      '$route'(to, from) {
        this.routerName = to.name
        if (to.name === 'editModel') {
          this.isNew = false
        }
      },
      filterText(val) {
        this.$refs.tree.filter(val)
      },
      modelData: function (newVal, oldVal) {
        this.modelData.colList.forEach((item, index) => {
          if (item.isMarking == 0) {
            item.isMarking = false
          } else {
            item.isMarking = true
          }
        })
        this.modelData = newVal
      },
    },
    methods: {
      // 初始化弹窗清空数据
      init() {
        this.searchText = ''
        this.checkAll = false
        this.columnData = []
        this.isIndeterminate = false
        this.tableData = []
        this.ruleForm.pkey = ''
        this.checkedCols = []
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
          if (item.definition &&
            (item.definition.toLowerCase().includes(keyWord) ||
              item.definition.toUpperCase().includes(keyWord))) {
            arr.push(item)
          }
        })
        return arr
      },
      // 标签全选操作
      handleCheckAllChange(val) {
        this.tableData = []
        this.myData = []
        this.editData = []
        this.checkedCols = val ? colsOptions : [];
        this.isIndeterminate = false;
        if (this.checkAll === true) {
          this.columnData.map(item => {
            item.colSort = ''
          })
          this.tableData = this.columnData
          this.tableData.forEach((item, index) => {
            item.isMarking = false
            if (item.colSort === '') {
              item.colSort = index + 1
            }
          })
        } else {
          this.tableData = []
        }
        if (val) {
          this.checkedCols.forEach((citem) => {
            this.columnData.map((item, index) => {
              if (item.definition == citem) {
                this.editData.push(item)
              }
            })
          })
          if (this.resourceType == 0) {
            this.editData.forEach((item, index) => {
              if (item.isPrimaryKey == 1) {
                this.myData.push(item)
              }
            })
          } else {
            this.myData = this.editData
          }
        } else {
          this.myData = []
          this.ruleForm.pkey = ''
          this.$refs.ruleForm.resetFields()
        }
        // 编辑
        if (this.routerName === 'editModel') {
          this.modelData.colList.forEach(item => {
            this.tableData.map(newItem => {
              if (item.isMarking == true && newItem.definition == item.showCol) {
                newItem.isMarking = true
              }
              if (item.sourceColId == newItem.id) {
                Object.assign(newItem, {colId: item.colId})
              }
            })
          })
        }
      },
      handleCheckedColsChange(value) {
        let checkedCount = value.length;
        this.checkAll = checkedCount === this.cols.length;
        this.isIndeterminate = checkedCount > 0 && checkedCount < this.cols.length;
        // this.tableData = []
        this.myData = []
        // 左边勾选的项 this.checkedCols
        // 左边全部字段项 this.columnData
        this.tableData = this.tableData.filter(({definition}) => this.checkedCols.some(citem => citem === definition)) // 删除
        // 本宝宝
        this.checkedCols.forEach((citem) => {
          if ((this.tableData.length === 0 || this.tableData.every(({definition}) => definition !== citem))) {
            const item_a = this.columnData.find(({definition}) => definition === citem)
            if (item_a) {
              item_a.isMarking = false
              item_a.colSort = ''
              this.tableData.push(JSON.parse(JSON.stringify(item_a)))
            }
          }
        })
        // this.checkedCols.forEach((citem) => {
        //   this.columnData.map((item) => {
        //     if (item.definition == citem && (this.tableData.length === 0 || this.tableData.every(({definition}) => definition !== citem))) {
        //       console.log('===============', this.tableData.some(({definition}) => definition !== citem), this.tableData, citem)
        //       item.isMarking = false
        //       item.colSort = ''
        //       this.tableData.push(item)
        //     }
        //   })
        // })
        // console.log('排序', this.tableData)
        this.tableData.forEach((item, index) => {
          if (item.colSort === '') {
            item.colSort = index + 1
          }
        })
        // 下拉框填写内容
        if (this.resourceType == 0) {
          this.tableData.forEach((item, index) => {
            if (item.isPrimaryKey == 1) {
              this.myData.push(item)
            }
          })
        } else {
          this.myData = this.tableData
        }
        // 清空
        if (value == '') {
          this.myData = []
          this.ruleForm.pkey = ''
          this.$refs.ruleForm.resetFields()
        }
        // 编辑
        if (this.routerName === 'editModel') {
          this.modelData.colList.forEach(item => {
            this.tableData.map(newItem => {
              if (item.isMarking == true && newItem.definition == item.showCol) {
                newItem.isMarking = true
              }
              if (item.sourceColId == newItem.id) {
                Object.assign(newItem, {colId: item.colId})
              }
              if (newItem.definition == item.showCol) {
                newItem.definition = item.showCol
              }
            })
          })
        }
      },
      close() {
      },
      // 树过滤
      filterNode(value, data) {
        if (!value) return true;
        return data.orgName.indexOf(value) !== -1
      },
      // 对字段进行选择确认,type=0,新建模型，type=1编辑模型
      async handleNodeClick(data) {
        this.myData = []
        this.editData = []
        colsOptions = []
        // this.colSetDialog = true
        let colsData = {}
        let allcolsData = {}
        if (this.routerName === 'creatModel' && data.isTable === true) {
          // this.colSetDialog = true
          // 新建模型字段确认获取数据
          colsData = await getResourceInfoData(data.resourceId, data.type, 1)  // 有权限的字段
          allcolsData = await getResourceInfoData(data.resourceId, data.type, 0)  // 全部的字段
          // 全部字段
          this.allcolumnData = allcolsData.data.column
          // 有权限字段
          this.columnData = colsData.data.column
          this.resourceName = colsData.data.resourceName
          this.resourceId = colsData.data.resourceId
          this.resourceType = colsData.data.type   // 0数据湖 1自建目录
          // this.tableData = []
          this.columnData.forEach((item, index) => {
            colsOptions.push(item.definition)
          })
          this.cols = colsOptions
          // 全部的字段权限显示
          this.allcolumnData.forEach((item, index) => {
            if (item.viewable == false) {
              this.powerName = '无权限'
              return
            } else if (item.decryption == false) {
              this.powerName = '加密'
              return
            } else if (item.sensitived == false) {
              this.powerName = '脱敏'
              return
            } else {
              this.powerName = ''
            }
          })
        }
        if (this.routerName === 'editModel') {
          this.colSetDialog = true
          // 编辑模型字段确认获取数据
          const resourceId = this.modelData.resourceId
          const type = this.modelData.resourceType
          colsData = await getResourceInfoData(resourceId, type, 1)  // 有权限的字段
          allcolsData = await getResourceInfoData(resourceId, type, 0)  // 全部的字段
          // 全部字段
          this.allcolumnData = allcolsData.data.column
          // 有权限字段
          this.columnData = colsData.data.column
          this.resourceName = colsData.data.resourceName
          this.resourceId = colsData.data.resourceId
          this.resourceType = colsData.data.type   // 0数据湖 1自建目录
          // this.tableData = []
          this.columnData.forEach((item, index) => {
            colsOptions.push(item.definition)
          })
          this.cols = colsOptions
          // 全部的字段权限显示
          this.allcolumnData.forEach((item, index) => {
            if (item.viewable == false) {
              this.powerName = '无权限'
              return
            } else if (item.decryption == false) {
              this.powerName = '加密'
              return
            } else if (item.sensitived == false) {
              this.powerName = '脱敏'
              return
            } else {
              this.powerName = ''
            }
          })
        }
        if (this.routerName === 'editModel') {
          // 获取主键值
          this.ruleForm.pkey = this.modelData.pkey
          // 获取选中的字段，从接口来
          this.modelData.colList.forEach((item, index) => {
            this.checkedCols.push(item.showCol)
            this.tableData.push({
              definition: item.showCol, isMarking: item.isMarking,
              colId: item.colId, type: item.sourceDataType, colSort: item.colSort, sourceColtion: item.sourceCol
            })
          })
          // 编辑下拉
          this.checkedCols.forEach((citem) => {
            this.columnData.map((item, index) => {
              if (item.definition == citem) {
                this.editData.push(item)
              }
            })
          })
          if (this.resourceType == 0) {
            this.editData.forEach((item, index) => {
              if (item.isPrimaryKey == 1) {
                this.myData.push(item)
              }
            })
          } else {
            this.myData = this.editData
          }
        }
      },
      // 获取1-3级树数据
      async getOneZtreeData(resolve) {
        try {
          const {data} = await getOneZtreeData()
          this.dataLakeDirectoryName = data.dataLakeDirectoryName
          this.dataSetDirectoryName = data.dataSetDirectoryName
          this.dataLakeDirectoryTree = data.dataLakeDirectoryTree
          this.dataSetDirectoryTree = data.dataSetDirectoryTree
          // resolve([{ orgName: this.dataSetDirectoryName, id: 1 }])
          // resolve([{orgName:this.dataSetDirectoryTree}])
          resolve(this.dataSetDirectoryTree)
        } catch (e) {
          console.log('error', e)
        }
      },
      // 加载树节点
      async loadNode(node, resolve) {
        if (node.level === 0) {
          this.getOneZtreeData(resolve)
        } else if (node.level === 1) {
          this.getChildTreeData(node.data, resolve)
        } else {
          return resolve([])
        }
      },
      // 获取4级树数据
      async getChildtreeData(data, resolve) {
        try {
          let allData = []
          // 获取资源树
          if (data.hasOwnProperty('orgId')) {
            const treeData = await getChildZtreeData(data.orgId, data.databaseType)
            this.treeData = treeData.data
          }
          // 获取资源树里面表
          if (data.hasOwnProperty('orgId')) {
            this.resData = []
            const resData = await getResourceListData(data.orgId, data.type, data.databaseType)
            if (resData) {
              resData.data.map(item => {
                Object.assign(item, {leaf: true})
              })
            }
            // 状态
            if (resData.openScope == '1') {
              this.openScope = '全部对外公开'
            } else if (resData.openScope == '2') {
              this.openScope = '全部对内公开'
            } else if (resData.openScope == '3') {
              this.openScope = '部分对内公开'
            } else {
              this.openScope = '不公开'
            }
            const resAlldata = resData.data
            // console.log('资源树所有值',resAlldata)
            resAlldata.forEach((item, index) => {
              this.resData.push({
                orgName: item.resourceName,
                resourceId: item.resourceId,
                type: data.type,
                isTable: item.isTable,
                leaf: true
              })
            })
            allData = this.treeData.concat(this.resData)
          }
          resolve(allData)
        } catch (e) {
          resolve([])
        }
      },
      // 获取3级树子节点
      getThreeChild(id, resolve) {
        // 这里可以替换成向后台发起的请求修改data,为了演示我用的是写死的数据,获取到data后,resolve出去就好了
        if (id === '1') {
          const data = this.dataLakeDirectoryTree
          resolve(data)
        } else if (id == '2') {
          const data = this.dataSetDirectoryTree
          resolve(data)
        }
      },
      // 获取4级树子节点
      getChildTreeData(data, resolve) {
        // console.log('点击当前的数据',data)
        this.getChildtreeData(data, resolve)
      },
      // 确认选择
      setCols() {
        this.$refs['ruleForm'].validate((valid) => {
          if (valid) {
            this.saveLoading = true
            const colList = []
            if (this.routerName === 'editModel') {
              this.tableData.map((item, index) => {
                this.modelData.colList.forEach(modelitem => {
                  if (item.definition == modelitem.showCol) {
                    item.definition = modelitem.sourceCol
                  }
                })
              })
            }
            this.tableData.map((item, index) => {
              colList.push({
                sourceCol: item.definition,
                sourceDataType: item.type,
                isMarking: item.isMarking ? 1 : 0,
                colId: item.colId,
                colSort: item.colSort,
                taggingModelId: this.modelData.taggingModelId,
                sourceColId: item.id
              })
            })
            if (colList.length === 1) {
              this.$message({
                message: '单个字段的数据集不能进行数据打标，请选择其它数据集',
                type: 'error'
              });
              this.saveLoading = false
            } else {
              if (this.routerName === 'editModel') {
                this.isNew = false
                this.taggingModelId = this.modelData.taggingModelId
              } else {
                this.taggingModelId = ''
              }
              const params = {
                'taggingModelId': this.taggingModelId,
                'resourceId': this.resourceId,
                'resourceName': this.resourceName,
                'modelName': this.resourceName,
                'resourceType': this.resourceType,
                'pkey': this.ruleForm.pkey,
                'isNew': this.isNew,
                'colList': colList
              }
              this.setColData(params)
              this.saveLoading = false
            }
          } else {
            console.log('error submit!!');
            this.saveLoading = false
            return false;
          }
        });
      },
      // 获取所有树表的数据
      async setColData(params) {
        const data = await setColsData(params)
        this.colSetDialog = false
        this.taggingModelId = data.taggingModelId
        const Id = this.taggingModelId
        this.$message({
          showClose: true,
          message: data.message,
          duration: 2000,
          type: 'success'
        })
        if (data.message == '保存成功') {
          this.$emit('commit')
          this.$parent.getModelList(this.taggingModelId)
          this.$parent.getModelColsList(this.taggingModelId, 0, 100, 1)
          if (this.routerName === 'creatModel') {
            this.$router.push({path: `editModel/${Id}`})
          }
        }

      },
      // 选择打标字段
      getCheckChange(row, $event) {
        this.tableData = JSON.parse(JSON.stringify(this.tableData))
      },
      delCol(index) {
        this.checkedCols.forEach((name, cindex) => {
          if (name === this.tableData[index].definition) {
            this.checkedCols.splice(cindex, 1)
          }
        })
        this.tableData.splice(index, 1)
      },
      changeSel() {
        this.tableData.map(item => {
          if (item.definition == this.ruleForm.pkey) {
            item.isMarking = false
          }
        })
      },
      handleClick(tab, event) {
        // console.log(tab, event);
      },
      async getLink() {
        try {
          const link = await getdatalakeLink(this.resourceId)
          if (link.data.code == 200) {
            this.$message({
              message: link.data.message,
              type: 'success'
            });
          } else if (link.data.code == 307) {
            window.location = link.data.dataLakeApplyPageUrl
          } else {

          }
        } catch (e) {
          console.log(e);
        }


      },
      getHeight() {
        this.contentStyleObj.width = window.innerWidth - 614 + 'px';
      }
    },
    created() {
      window.addEventListener('resize', this.getHeight);
      this.getHeight()
      // this.getOneZtreeData()
      if (this.$route.name == 'editModel') {
        this.modelId = this.$route.params.id
      }
    },
    mounted() {
      this.routerName = this.$route.name
    },
    computed: {
      list() {
        const arr = []
        this.columnData.map(item => {
          if (item.definition &&
            (item.definition.toLowerCase().includes(this.searchText) ||
              item.definition.toUpperCase().includes(this.searchText))) {
            arr.push(item)
          }
        })
        return arr
      },
      allList() {
        const arr = []
        this.allcolumnData.map(item => {
          if (item.definition &&
            (item.definition.toLowerCase().includes(this.searchText) ||
              item.definition.toUpperCase().includes(this.searchText))) {
            arr.push(item)
          }
        })
        return arr
      },
    }
  }
</script>

<style scoped lang="scss">
  .col-name-box {
    display: flex;
    .col-name {
      width: 100px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }

  .newAside {
    display: flex;
  }

  .asideTwo {
    width: 250px;
    flex-shrink: 0;
    /*position: fixed;*/
    padding: 10px;
    /*margin-top: 40px;*/
    box-shadow: 5px 0 10px 0 rgba(0, 0, 0, 0.05);
    margin-left: 16px;
    box-sizing: border-box;
    z-index: 2;
    .tree {
      height: calc(75vh - 116px);
      overflow: auto;
    }
    .search {
      margin-bottom: 20px;
    }
    .cus-node-title {
      display: inline-block;
      max-width: 120px;
      overflow: hidden;
      text-overflow: ellipsis;
      color: #606266;
      font-size: 14px;
    }
    .custom-tree-node {
      display: flex;
      align-items: baseline;
    }
  }

  .aside {
    width: 250px;
    flex-shrink: 0;
    position: fixed;
    padding: 10px;
    /*margin-top: 40px;*/
    box-shadow: 5px 0 10px 0 rgba(0, 0, 0, 0.05);
    margin-left: 16px;
    box-sizing: border-box;
    z-index: 2;
    .tree {
      /*height: calc(75vh - 80px);*/
      height: calc(75vh - 116px);
      overflow: auto;
    }
    .search {
      margin-bottom: 20px;
    }
    .cus-node-title {
      display: inline-block;
      max-width: 120px;
      overflow: hidden;
      text-overflow: ellipsis;
      color: #606266;
      font-size: 14px;
    }
    .custom-tree-node {
      display: flex;
      align-items: baseline;
    }
  }

  .col-set-box {
    .left {
      border-right: 1px solid #eee;
      padding: 0 10px;
      color: #000000;
    }
    .leftNew {
      padding: 0 10px;
      color: #000000;
    }
    h3 {
      font-weight: normal;
      font-size: 14px;
      line-height: 32px;
      border-bottom: 1px solid #eee;
    }
    .h4 {
      margin-top: 10px;
      padding: 5px 10px;
      background-color: #f4f9fb;
      border: 1px solid #eee;
      font-weight: bold;
      .allNametitle {
        padding-left: 10px;
        font-size: 14px;
        color: #606266;
        font-weight: 500;
      }
    }
    ul {
      margin: 0;
      padding: 0;
      li {
        margin: 0;
        padding: 5px 10px;
        border: 1px solid #eee;
        border-top: none;
        .blue {
          color: #0486fe;
        }
        .green {
          color: green;
        }
        .orange {
          color: orange;
        }
      }
    }
  }

  .right {
    min-width: 400px;
    padding: 0 10px;
  }

  .rightNew {
    padding-left: 24px;
  }

  .tableNew {
    height: calc(58vh);
    overflow: auto;
  }

  .contentNum {
    height: 300px;
    overflow: auto;
  }

  .contentNumheight {
    height: calc(66vh - 140px);
    overflow: auto;
  }

  .treeCode {
    left: 282px;
    /*height: calc(68vh);*/
  }

  .btnMargin {
    margin-left: 5px;
  }

  .subscribeData {
    font-size: 14px;
    text-align: center;
    height: 30px;
    line-height: 30px;
    color: #fff;
    background-color: #0486fe;
    border-color: #0486fe;
    cursor: pointer;
  }

  .power {
    color: #b1b1b1;
  }

  .iconImg {
    font-size: 16px;
    color: #1296DB;
    margin-right: 5px;
  }

  .creatTable {
    height: calc(75vh - 80px);
    overflow-y: auto;
  }

  .btnNew {
    position: absolute;
    bottom: 24px;
    text-align: center;
    left: 0px;
    right: 0px;
    z-index: 9;
  }

  .formClass {
    margin-left: 20px;
  }

  .formClass .el-form {
    background-color: transparent !important;
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
  tr:hover > td
    background-color: #f4f9fb;
</style>
