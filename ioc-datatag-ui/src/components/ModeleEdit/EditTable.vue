<template>
  <div class="table-box">
    <div class="showCon">
      <el-table border class="my-table" :data="tableData" style="width: 100%;" :height="tableHeight">
        <el-table-column v-for="(item,index) in theadData" :prop="item.sourceCol" :key="index" min-width="300">
          <template slot="header" slot-scope="scope">
            <el-dropdown @command="handleCommandTags($event,item)"
                         v-if="theadData[index].isMarking===true || theadData[index].isMarking==1">
              <span class="el-dropdown-link">
                {{item.showCol}} <i class="el-icon-setting btnMargin"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="0" icon="el-icon-document-copy">克隆字段</el-dropdown-item>
                <el-dropdown-item command="1" icon="el-icon-price-tag">数据打标</el-dropdown-item>
                <el-dropdown-item command="2" icon="el-icon-close">清除字段</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <span v-else>
            {{item.showCol}}
          </span>
          </template>
        </el-table-column>
      </el-table>
      <!--    <element-pagination :pageSize="size" :total="totalnum" @handleCurrentChange="handleCurrentChange"
                              @sureClick="goPage"></element-pagination>-->
      <!--字段设置-->
      <el-dialog class="creat" title="数据打标" :visible.sync="setTagsDialog" width="800px" center
                 :modal-append-to-body="false" :close-on-click-modal="false"
                 @close="$emit('update:show', false)" @open="init">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="选择标签组:" prop="tagTeam">
            <el-col :span="9">
              <el-select v-model="ruleForm.tagTeam" filterable placeholder="请选择标签组" size="small"
                         @change="chooseTagTeam">
                <el-option
                  v-for="item in tagTeamList"
                  :key="item.id"
                  :label="item.tagsName"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="9">
              <el-link type="primary" :underline="false" @click="editLabelgroup">编辑标签组</el-link>
            </el-col>
          </el-form-item>
          <el-form-item label="选择标签层:" prop="tagLev">
            <el-col :span="7.5">
              <div class="allTree">
                <div class="sel">
                  <el-input size="small"
                            placeholder="请输入内容"
                            v-model="ruleForm.tagLev">
                    <i slot="suffix" class="el-input__icon el-icon-arrow-down" @click="showTree()"></i>
                  </el-input>
                  <div class="treeBoder" v-show="showLevTree">
                    <el-input size="small" v-model="ruleForm.tag" placeholder="请输入关键字查询" style="margin-bottom:10px;"/>
                    <el-tree
                      :data="treeLevdata"
                      :props="defaultProps"
                      node-key="id"
                      ref="treeForm"
                      show-checkbox
                      default-expand-all
                      check-strictly
                      :filter-node-method="filterNode"
                      @check-change="handleClick"
                      @node-click="nodeClick">
                      <span class="slot-t-node span-ellipsis" slot-scope="{ node, data }">
                      <i :class="{ 'fa fa-folder': !node.expanded, 'fa fa-folder-open':node.expanded}"
                         style="color: #fcd568;"/>
                        <span :title="data.tagName">{{ data.tagName }}</span>
                      </span>
                    </el-tree>
                  </div>
                </div>
              </div>
            </el-col>
          </el-form-item>
          <el-form-item label="打标设置:" prop="tagSet">
            <el-col :span="9">
              <el-select v-model="ruleForm.tagSet" filterable placeholder="请选择" size="small" @change="getLabel">
                <el-option v-for="(item ,index) in tagSetList" :key="item.id" :label="item.tagName" :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="10">
              <el-row>
                <el-button type="primary" size="small" :disabled="ruleForm.tagSet===''" @click="handleMark">添加自动打标
                </el-button>
                <el-button type="primary" size="small" :disabled="ruleForm.tagSet===''" @click="selfMark">添加人工打标
                </el-button>
              </el-row>
            </el-col>
          </el-form-item>
          <div class="lookContent">
            <div class="contentTop" v-show="isHandle===0">
              <div class="connect-smbol-box">
                <div class="topOne" :class="{'no-arrow':conditionSetting.length===0}"
                     v-for="(item,index) in connectSymbolList"
                     :key="'con'+index" @click="chooseConnectSymbo(item)">{{item.codename}}
                </div>
              </div>
              <div class="count-smbol-box">
                <div class="topOne"
                     v-for="(item,index) in countSymbolList"
                     :key="'count'+index" @click="chooseCountSymbol(item)">{{item.codename}}
                </div>
              </div>
            </div>
            <!--自动打标不可操作-->
            <div class="contentTop" v-show="isHandle===1">
              <div class="connect-smbol-box">
                <div class="topOne" :class="{'no-arrow':isHandle===1}"
                     v-for="(item,index) in connectSymbolList"
                     :key="'con'+index">{{item.codename}}
                </div>
              </div>
              <div class="count-smbol-box">
                <div class="topOne" :class="{'no-arrow':isHandle===1}"
                     v-for="(item,index) in countSymbolList"
                     :key="'count'+index">{{item.codename}}
                </div>
              </div>
            </div>
            <!--自动打标不可操作-->
            <div class="makingContent">
              <!--打标开始-->
              <div class="card" v-for="(item,index) in selfMarkList" :key="index" @click="chooseMark(item,index)"
                   :class="{acitve:curIndex===index}">
                <el-card class="box-card" :class="{borderColor:changeRed == index+1}">
                  <!--人工打标结构-->
                  <div class="card-handle" v-if="item.isHandle===0">
                    <i class="el-icon-circle-close deleteContent" @click="delSelfMark(index)"></i>
                    <div class="labelCard" :title="item.tagSetName">{{item.tagSetName}}</div>
                    <!--<el-input style="width:100px" size="small" v-model="item.tagSetName" placeholder="请输入内容"-->
                              <!--readonly></el-input>-->
                    <span class="chinese">{{item.sourceCol}}</span>
                    <div class="conditions">
                      <div class="condition" v-for="(conItem,conIndex) in item.conditionSetting" :key="'con'+conIndex">
                        <div class="count-symbol" v-if="conItem.isConnectSymbol===0">
                          <span class="smbol-len" v-show="conItem.symbol.length>2">{{conItem.symbol}}</span>
                          <span class="smbol" v-show="conItem.symbol.length<3">{{conItem.symbol}}</span>
                          <el-input style="width:100px" size="small" v-model="conItem.theValues"
                                    placeholder="请输入内容"></el-input>
                        </div>
                        <div class="connect-symbol" v-else>
                          <span class="smbol">{{conItem.symbol}}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                  <!--自动打标结构-->
                  <div class="card2" v-else>
                    <div><i class="el-icon-circle-close deleteContent" @click="delSelfMark(index)"></i></div>
                    <div>
                      <div class="labelCard" :title="item.tagSetName">{{item.tagSetName}}</div>
                      <!--<el-input style="width:200px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" size="small" v-model="item.tagSetName" placeholder="请输入内容" readonly></el-input>-->
                    </div>
                    <div class="chinese">{{item.sourceCol}}</div>
                    <div class="self-mark-choose-box">
                      <div class="chooseNum" @click="showSelf(item,$event)">
                        <span>已选</span>
                        <span class="num">{{item.checkList.length}}</span>
                        <span>条</span>
                        <i class="el-icon-caret-top" v-if="item.showSelfMark==true"></i>
                        <i class="el-icon-caret-bottom" v-else></i>
                      </div>
                      <div class="self-mark-list" v-show="item.showSelfMark">
                        <el-input
                          size="small"
                          placeholder="请输入内容"
                          v-model="searchWord">
                          <i slot="suffix" class="el-input__icon el-icon-search" @click="search"></i>
                        </el-input>
                        <div class="checkIt">
                          <div class="checkOne" v-for="(colItem,cIndex) in listCheck">
                            <el-checkbox-group v-model="item.checkList" @change="checkMarkChange(item)">
                              <el-checkbox :key='cIndex' :label="colItem.markName"></el-checkbox>
                            </el-checkbox-group>
                          </div>
                        </div>
                      </div>
                    </div>

                  </div>

                </el-card>
              </div>
              <!--打标结束-->
            </div>
          </div>

        </el-form>
        <div slot="footer" class="dialog-footer device">
          <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading" @click="saveMark">确认打标
          </el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
  import ElementPagination from '@/components/ElementPagination'
  import {mapState} from 'vuex'
  import {
    getMyTagGroupData,
    getTagLevData,
    getHistoryColData,
    saveMarkData,
    delCol,
    cloneCol,
    getSymolsData
  } from '@/api/creatModel'

  export default {
    components: {ElementPagination},
    name: 'datasetEditable',
    props: {
      // theadData: {
      //   type: Array,
      //   default: Array
      // },
      modelId: {
        type: String,
        default: ''
      },
      tableData: {
        type: Array,
        default: Array
      }
    },
    data() {
      return {
        changeRed:-1,
        znumber:'',
        ztheadData: '',
        tableHeight: '',
        totalnum: 20,
        size: 10,
        curIndex: 0,
        connectSymbolList: [],
        countSymbolList: [],
        mineStatus: '',
        mineStatusValue: [],
        treeLevdata: [],
        defaultProps: {
          children: 'childrenNode', // 子集的属性
          label: 'tagName',
          disabled: 'notLeafParent'// 是否可以选择
        },
        saveLoading: false,
        ruleForm: {
          tagTeam: '',
          tagLev: '',
          tagSet: '',
          tag: ''
        },
        rules: {
          tagTeam: [
            {required: true, message: '请选择标签组', trigger: 'change'}
          ],
          tagLev: [
            {required: true, message: '请选择标签层', trigger: 'change'}
          ],
          tagSet: [
            {required: true, message: '请选择', trigger: 'change'}
          ]
        },
        options: [],
        tagTeamList: [],
        tagSetList: [],
        setTagsDialog: false,
        searchKeyword: '',
        colId: 0,
        filterText: '',
        check: '',
        changevalue: '',
        showLevTree: false,
        showSelfMark: false,
        valuesType: '',
        sourceCol: '',//打标字段名称
        searchWord: '',// 搜索自动打标值
        colList: [],//对应字段列内容
        checkList: [],
        selfMarkList: [],//所有自动打标数据
        condition: [],
        conditionSetting: [],
        isHandle: 1,
        radio: '1',
        childrenNode: [],
        chooseTagTeamid: '',
        chooseTagTeamname: ''
      }
    },
    watch: {
      'connectSymbolList': {
        handler: function (newValue, oldValue) {
          this.connectSymbolList = newValue
        }
      },
      'countSymbolList': {
        handler: function (newValue, oldValue) {
          this.countSymbolList = newValue
        }
      },
      'ruleForm.tag': {
        handler: function (newValue, oldValue) {
          this.$refs.treeForm.filter(newValue)
        }
      },
      'tagSetList': {
        handler: function (newValue, oldValue) {
          this.tagSetList = newValue
        },
        deep: true
      },
      'tableData': {
        handler: function (newValue, oldValue) {
          this.tableData = newValue
          this.$set(this.tableData)
        },
        deep: true
      },
      // 'this.theadData': {
      //   handler: function (newValue, oldValue) {
      //     // this.theadData = newValue
      //     // this.$set(this.theadData)
      //   },
      //   deep: true
      // },
    },
    created() {
      //获取标签组
      this.getMyTagGroupList()
      this.getConnectList('dt.tag.conditions.connect')
      this.getCountList('dt.tag.conditions.noconnect')
      this.tableHeight = document.body.clientHeight - 137

    },
    mounted() {
    },
    methods: {
      getLabel(){},
      init() {
        this.ruleForm.tagTeam = ''
        this.treeLevdata = []
        this.selfMarkList = []
        this.ruleForm.tagLev = ''
        this.ruleForm.tagSet = ''
        this.$nextTick(() => {
          this.$refs['ruleForm'].clearValidate()
        });
      },
      //点击分页跳转
      handleCurrentChange(page) {
        this.page = page - 1
        //this.getTagsData()
      },
      //点击分页确认
      goPage() {
      },
      //深度克隆
      deepClone(obj) {
        let newObj = Array.isArray(obj) ? [] : {}

        if (obj && typeof obj === 'object') {
          for (let key in obj) {
            if (obj.hasOwnProperty(key)) {
              newObj[key] = (obj && typeof obj[key] === 'object') ? this.deepClone(obj[key]) : obj[key]
            }
          }
        }
        return newObj
      },
      //选择标签层拿树
      nodeClick(data, checked, node) {
      },
      handleClick(data, checked, node) {
        if (checked === true) {
          this.tagSetList = []
          this.checkedId = data.id;
          this.$refs.treeForm.setCheckedKeys([data.id]);
          if (data.childrenNode) {
            data.childrenNode.map(item => {
              if (item.leafParent == false) {
                this.tagSetList.push(item)
              }
            })
          }
          this.ruleForm.tagLev = data.tagName
          this.showLevTree = false
        } else {
          if (this.checkedId == data.id) {
            this.$refs.treeForm.setCheckedKeys([data.id]);
          }
        }
      },
      //选择连接符号
      chooseConnectSymbo(item) {
        const conditionObj = {
          isConnectSymbol: 1,//是否有连接符号
          symbol: item.codename,//连接符名或者>,=
          theValues: '',
          valuesType: this.valuesType,
        }
        const consLen = this.selfMarkList[this.curIndex].conditionSetting.length
        if (consLen === 1) {
          this.selfMarkList[this.curIndex].conditionSetting.push(conditionObj)
        } else if (consLen === 2) {
          this.selfMarkList[this.curIndex].conditionSetting.splice(1, 1)
          this.selfMarkList[this.curIndex].conditionSetting.push(conditionObj)
        }
        this.conditionSetting = this.selfMarkList[this.curIndex].conditionSetting
      },
      //选择运算符号
      chooseCountSymbol(item) {
        const conditionObj = {
          isConnectSymbol: 0,//是否有连接符号
          symbol: item.codename,//连接符名或者>,=
          theValues: '',
          valuesType: this.valuesType,
        }
        //console.log('this.selfMarkList',this.selfMarkList)
        const consLen = this.selfMarkList[this.curIndex].conditionSetting.length
        if (consLen === 0) {
          this.selfMarkList[this.curIndex].conditionSetting.push(conditionObj)
        } else if (consLen === 1) {
          this.selfMarkList[this.curIndex].conditionSetting = []
          this.selfMarkList[this.curIndex].conditionSetting.push(conditionObj)
        } else if (consLen === 2) {
          this.selfMarkList[this.curIndex].conditionSetting.push(conditionObj)
        }
        this.conditionSetting = this.selfMarkList[this.curIndex].conditionSetting

      },
      closePanel() {
        this.refs['ruleForm'].resetFields()
        this.ruleForm.tagTeam = ''
        this.selfMarkList = []
      },
      //点击自动打标按钮
      selfMark() {
        let tagSetName = ''
        this.tagSetList.forEach((item) => {
          if (item.id === this.ruleForm.tagSet) {
            return tagSetName = item.tagName
          }
        })
        const conditionSetting = [{
          isConnectSymbol: 0,//是否有连接符号
          symbol: 'IN',
          theValues: '',
          valuesType: this.valuesType,
        }]
        const markObj = {
          tagId: this.ruleForm.tagSet,
          tagSetName: tagSetName,
          sourceCol: this.sourceCol,
          checkList: [],
          showSelfMark: false,
          isHandle: 1,//自动打标
          conditionSetting: conditionSetting
        }
        this.selfMarkList.push(markObj)
        this.curIndex = this.selfMarkList.length - 1
        this.isHandle = 1
      },
      //人工打标
      handleMark() {
        let tagSetName = ''
        //console.log('this.ruleForm.tagSet',this.ruleForm.tagSet)
        //console.log('this.tagSetList', this.tagSetList)
        this.tagSetList.forEach((item) => {
          if (item.id === this.ruleForm.tagSet) {
            return tagSetName = item.tagName
          }
        })
        const markObj = {
          tagId: this.ruleForm.tagSet,
          tagSetName: tagSetName,
          sourceCol: this.sourceCol,
          checkList: [],
          showSelfMark: false,
          isHandle: 0,//人工打标
          conditionSetting: []
        }
        this.selfMarkList.push(markObj)
        this.curIndex = this.selfMarkList.length - 1
        this.isHandle = 0
      },
      //选中自动打标内容
      checkMarkChange(item) {
        // console.log(value)
        // console.log(item)
        this.checkList = item.checkList
        item.conditionSetting[0].theValues = this.checkList.join(",")
      },
      //显示标签层
      showTree() {
        this.showLevTree = !this.showLevTree
      },
      unique(arr) {
        const res = new Map()
        return arr.filter((markName) => !res.has(markName) && res.set(markName, 1))
      },
      //显示自动打标内容

      showSelf(itemObj) {
        itemObj.showSelfMark = !itemObj.showSelfMark
        this.colList = []
        const name = this.sourceCol
        this.tableData.forEach((item, index) => {
          //console.log(item[name])
          const markName = item[name]
          this.colList.push({markName: markName})

        })
        const obj = {}
        //数组去重
        this.colList = this.colList.reduce((item, next) => {
          obj[next.markName] ? '' : obj[next.markName] = true && item.push(next)
          return item
        }, [])
      },
      delSelfMark(index) {
        // console.log(index)
        this.selfMarkList.splice(index, 1)
      },
      search() {
        // console.log("查询");
      },
      ///过滤树
      filterNode(value, data) {
        if (!value) return true;
        return data.tagName.indexOf(value) !== -1;
      },
      colSetTags(data) {
        this.setTagsDialog = true
        //console.log(colId)
        this.valuesType = data.sourceDataType
        this.sourceCol = data.sourceCol
        this.colId = data.colId
        this.getHistoryColList(data.colId)
      },
      //下拉菜单处理
      handleCommandTags(dropIndex, data) {
        // console.log('下拉菜单数据',data)
        switch (dropIndex) {
          case '0': // 克隆字段
            this.cloneCol(data)
            break
          case '1': // 字段打标
            this.colSetTags(data)
            break
          case '2': // 清除字段
            this.delCol(data)
        }
      },
      // 清除字段
      async delCol(data) {
        const params = {
          id: data.colId
        }
        try {
          const data = await delCol(params)
          this.$message.success(data.message)
          this.$parent.getModelList(this.modelId)
          this.$parent.getModelColsList(this.modelId, 0, 100, 1)
          this.$emit('commit2')
        } catch (e) {

        }
      },
      // 克隆字段
      async cloneCol(data) {
        const params = {
          colId: data.colId
        }
        try {
          const data = await cloneCol(params)
          this.$message.success(data.message)
          this.$parent.getModelList(this.modelId)
          this.$parent.getModelColsList(this.modelId, 0, 100, 1)
          this.$emit('commit2')
        } catch (e) {

        }
      },
      // 连接
      async getConnectList(codetype) {
        const params = {
          codetype: codetype
        }
        try {
          const data = await getSymolsData(params)
          this.connectSymbolList = data.rows
        } catch (e) {
        }
      },
      // 计算符合
      async getCountList(codetype) {
        const params = {
          codetype: codetype
        }
        try {
          const data = await getSymolsData(params)
          this.countSymbolList = data.rows
        } catch (e) {
        }
      },
      //选择标签组
      chooseTagTeam(id) {
        this.chooseTagTeamid = id
        this.getTagLevList(id)
        this.tagTeamList.forEach(item => {
          if (item.id == id) {
            this.chooseTagTeamname = item.tagsName
          }
        })
      },
      // 编辑标签组
      editLabelgroup() {
        if (this.chooseTagTeamid) {
          this.$router.push({
            path: '/editTree/' + this.chooseTagTeamid + '/' + this.chooseTagTeamname,
          })
        } else {
          this.$message.error('请先选择标签组！');
        }
      },

      //关闭打标
      close() {
        this.showSelfMark = false
        this.colList = []
      },
      // 选择标签组
      async getMyTagGroupList() {
        try {
          const params = {
            keyword: '',
            page: 0,
            size: 200
          }
          const data = await getMyTagGroupData(params)
          // console.log('选择标签组', data.rows)
          this.tagTeamList = data.rows
        } catch (e) {

        }
      },
      // 标签层数据
      async getTagLevList(id) {
        try {
          const data = await getTagLevData(id)
          // console.log('选择标签层', data.childrenNode)
          this.treeLevdata = data.childrenNode
        } catch (e) {

        }
      },
      // 查询打标历史接口
      async getHistoryColList(colId) {
        const params = {
          colId: colId
        }
        try {
          const data = await getHistoryColData(params)
          // console.log('打标历史接口data', data)
          //被选标签组
          this.ruleForm.tagTeam = data.selectTagGroup.id
          this.chooseTagTeam(data.selectTagGroup.id)
          //标签层数树
          this.getTagLevList(this.ruleForm.tagTeam)
          //选择标签层
          this.ruleForm.tagLev = data.selectTags.tagName
          // 打标设置
          // this.ruleForm.tagSet = data.selectTag.id
          //历史数据
          this.$refs.treeForm.setCheckedKeys([data.selectTags.id]);
          const obj = {
            tagName: data.selectTag.tagName,
            id: data.selectTag.id
          }
          this.tagSetList.push(obj)
          //打标相关字段
          this.selfMarkList = this.deepClone(data.condtion)
          this.selfMarkList.map((item, index) => {
            item.showSelfMark = false
            item.checkList = item.conditionSetting[0].theValues.split(',')
            item.tagSetName = item.tagName
          })
          this.curIndex = this.selfMarkList.length - 1
          //console.log('this.selfMarkList',this.selfMarkList)
          this.conditionSetting = this.selfMarkList[this.curIndex].conditionSetting
          // console.log('this.conditionSetting',this.conditionSetting)
        } catch (e) {

        }
      },
      //确认打标按钮
      saveMark() {
        this.saveLoading = true
        this.$refs.ruleForm.validate((valid) => {
          if (valid) {
            this.getSaveMarkList()
            this.saveLoading = false
          } else {
            this.saveLoading = false
          }
        });
      },
      //打标确认保存
      async getSaveMarkList() {
        // console.log('this.selfMarkList',this.selfMarkList)
        //console.log('this.valuesType',this.valuesType)
          let conditions = this.deepClone(this.selfMarkList)
          conditions.forEach((obj, index) => {
            delete obj.checkList
            delete obj.sourceCol
            delete obj.tagSetName
            delete obj.showSelfMark
            obj.colId = this.colId
            return obj
          })
          const params = {
            colId: this.colId,
            condtion: conditions
          }
          try{
            const data = await saveMarkData(params)
              this.$message({
                showClose: true,
                message: '打标成功',
                duration: 2000,
                type: 'success'
              })
              this.setTagsDialog = false
              this.selfMarkList = []
          }catch (e) {
            console.log('e',e);
            this.changeRed = e.data
          }

      },
      //选中要打标条件修改
      chooseMark(item, index) {
        this.curIndex = index
        //console.log('选中要打标的项',item)
        this.isHandle = item.isHandle
        this.conditionSetting = item.conditionSetting
      }
    },
    computed: {
      ...mapState({
        theadData: state => state.image.tableArr
      }),
      listCheck() {
        const arr = []
        this.colList.map(item => {
          if (item.markName &&
            (item.markName.toLowerCase().includes(this.searchWord) ||
              item.markName.toUpperCase().includes(this.searchWord))) {
            arr.push(item)
          }
        })
        return arr
      },
    },
  }
</script>

<style scoped lang="scss">
  .my-table {
    .el-icon-setting {
      color: #409eff;
      cursor: pointer;
    }
  }

  label {
    font-weight: normal;
  }

  .allTree {
    position: relative;
    i {
      cursor: pointer;
    }

  }

  .filter-tree {
    height: 200px;
    overflow: auto;
  }

  .treeBoder {
    width: 100%;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    padding: 15px 10px;
    background-color: #fff;
    position: absolute;
    z-index: 55;
    width: 215px;
    padding-top: 3px;
    overflow: auto;
  }

  .span-ellipsis {
    width: 100%;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    display: block;
  }

  .lookContent {
    border: 1px solid #dcdfe6;
    padding: 0px 10px;
    border-radius: 4px;
  }

  .contentTop {
    display: flex;
    border-bottom: 1px solid #dcdfe6;
    .connect-smbol-box {
      display: flex;
    }
    .count-smbol-box {
      display: flex;
    }
  }

  .topOne {
    width: 40px;
    height: 32px;
    text-align: center;
    line-height: 32px;
    font-size: 12px;
    color: #0486fe;
    cursor: pointer;
    &.no-arrow {
      cursor: not-allowed;
      color: #cccccc;
    }
  }

  .deleteContent {
    font-size: 18px;
    padding: 0px 10px 0px 0px;
    color: #0486fe;
  }

  .chinese, .chinese2 {
    font-size: 14px;
    padding: 0px 10px;
  }

  .chinese2 {
    color: #0486fe;
  }

  .makingContent {
    margin-top: 20px;
    min-height: 100px;
    /*max-height: 300px;*/
    /*overflow: auto;*/
    padding-bottom: 10px;
  }

  .card {
    margin-bottom: 10px;
    cursor: pointer;
    position: relative;
  }

  .chooseNum {
    width: 200px;
    height: 32px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 14px;
    cursor: pointer;
  }

  .no-handle {
    .no-arrow {
      color: #cccccc;
    }
  }

  .self-mark-choose-box {
    position: absolute;
    right: 100px;

  }

  .self-mark-list {
    width: 200px;
    position: absolute;
    right: 0px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    padding: 5px 10px;
    background-color: #fff;
    z-index: 9999;
  }

  .num {
    color: #0486fe;
    padding: 0px 3px;
  }

  .card2 {
    display: flex;
    align-items: center;
  }

  .checkIt {
    padding: 10px;
    height: 130px;
    overflow: auto;

  }

  .checkOne {
    padding-top: 5px;
  }

  .clearfix:after {
    content: '';
    display: block;
    clear: both;
  }

  .deleteContent {
    cursor: pointer;
  }

  .card-handle {
    display: flex;
    align-items: center; /*垂直居中*/
  }

  .conditions {
    display: flex;
    align-items: center; /*垂直居中*/
    .condition {
      position: relative;
      .connect-symbol {
        .smbol {
          color: #409EFF;
          margin: 0 20px;
        }
      }
      .count-symbol {
        .smbol-len {
          margin-left: 10px;
          margin-right: 5px;
        }
        .smbol {
          position: absolute;
          z-index: 8;
          top: 8px;
          left: 2px;
        }
      }

    }
  }

  .table-box {
    overflow: auto;
  }

  .btnMargin {
    margin-left: 5px;
  }

  .labelCard {
    width: 100px;
    height: 32px;
    line-height: 32px;
    background-color: #fff;
    background-image: none;
    border-radius: 4px;
    border: 1px solid #dcdfe6;
    color:#606266;
    padding:0 15px;
    font-size: 12px;
    overflow: hidden;
    text-overflow:ellipsis;
    white-space:nowrap;
  }
  .borderColor{
    border: 1px solid #ee0320;
  }
</style>
