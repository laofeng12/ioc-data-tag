<template>
  <div class="table-container">
    <div class="header">
      <div class="left">
        <router-link class="return" to="/cooperationModel">
          <i class="el-icon-arrow-left"></i>
        </router-link>
        <div class="name">
          <div class="img"></div>
          <div class="text">{{modelName}}</div>
        </div>
      </div>
      <!--<div class="right rightBtn">-->
      <!--<el-button class="button2" type="primary" size="small">保存打标</el-button>-->
      <!--</div>-->
    </div>
    <div class="content">
      <div class="components">
        <div class="contentTitle">{{modelName}}</div>
        <div class="newTable  daList">
          <el-table ref="multipleTable" :data="tableData" border stripe tooltip-effect="dark"
                    style="width: 100%;text-align: center"
                    :header-cell-style="{background:'#f0f2f5'}" :height="tableHeight">
            <template slot="empty">
              <div v-if="Loading">
                <div v-loading="saveLoading2"></div>
              </div>
              <div v-else>暂无数据</div>
            </template>
            <el-table-column v-for="(item,index) in headData" :key="index" :prop="item.sourceCol" min-width="300">
              <template slot="header" slot-scope="scope">
                <span>{{item.showCol}}</span>
                <i v-if="item.isMarking==1&&item.isCooField==1" class="el-icon-position  iconLogo"
                   @click="dataMaking(item)"></i>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <el-dialog class="creat" title="数据打标" :visible.sync="setTagsDialog" width="800px" center
               :modal-append-to-body="false" :close-on-click-modal="false"
               @close="$emit('update:show', false)" @open="init">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="选择标签组:" prop="tagTeam">
          <el-col :span="9">
            <el-select v-model="ruleForm.tagTeam" filterable placeholder="请选择标签组" size="small" @change="chooseTagTeam">
              <el-option
                v-for="item in tagTeamList"
                :key="item.id"
                :label="item.tagsName"
                :value="item.id">
              </el-option>
            </el-select>
          </el-col>
          <!--<el-col :span="9">-->
            <!--<el-link type="primary" :underline="false" @click="editLabelgroup">编辑标签组</el-link>-->
          <!--</el-col>-->
        </el-form-item>
        <el-form-item label="选择标签层:" prop="tagLev">
          <el-col :span="11">
            <div class="allTree">
              <div class="sel">
                <el-input style="width: 215px"
                          size="small"
                          placeholder="请选择"
                          v-model="ruleForm.tagLev">
                  <i slot="suffix" class="el-input__icon el-icon-arrow-down" @click="showTree()"></i>
                </el-input>
                <div class="treeBoder" v-show="showLevTree">
                  <el-input size="small" v-model="filterText" placeholder="请输入关键字查询" style="margin-bottom:10px;"/>
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
            <el-select v-model="ruleForm.tagSet" filterable placeholder="请选择" size="small">
              <el-option v-for="(item ,index) in tagSetList" :key="item.id" :label="item.tagName" :value="item.id">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="9">
            <el-row>
              <el-button type="primary" size="small" :disabled="ruleForm.tagSet===''" @click="handleMark">添加自动打标
              </el-button>
              <el-button type="primary" size="small" :disabled="ruleForm.tagSet===''" @click="selfMark">添加人工打标</el-button>
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
              <el-card class="box-card">
                <!--人工打标结构-->
                <div class="card-handle" v-if="item.isHandle===0">
                  <i class="el-icon-circle-close deleteContent" @click="delSelfMark(index)"></i>
                  <el-input style="width:100px" size="small" v-model="item.tagSetName" placeholder="请输入内容"
                            readonly></el-input>
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
                    <el-input style="width:100px" size="small" v-model="item.tagSetName" placeholder="请输入内容"
                              readonly></el-input>
                  </div>
                  <div class="chinese">{{item.sourceCol}}</div>
                  <div class="self-mark-choose-box">
                    <div class="chooseNum" @click="showSelf(item,$event)">
                      <span>已选</span>
                      <span class="num">{{item.checkList.length}}</span>
                      <span>条</span>
                    </div>
                    <div class="self-mark-list" v-show="item.showSelfMark">
                      <el-input
                        size="small"
                        placeholder="请输入内容"
                        v-model="searchWord">
                        <i slot="suffix" class="el-input__icon el-icon-search" @click="search"></i>
                      </el-input>
                      <div class="checkIt">
                        <div class="checkOne">
                          <el-checkbox-group v-model="item.checkList" @change="checkMarkChange(item)">
                            <el-checkbox v-for="(colItem,cIndex) in  colList" :key='cIndex'
                                         :label="colItem.markName"></el-checkbox>
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
</template>

<script>
  import {getSearchcoofield, getTaggroup} from '@/api/cooperation'
  import {
    getMyTagGroupData,
    getTagLevData,
    getHistoryColData,
    saveMarkData,
    delCol,
    cloneCol,
    getSymolsData,
    getModelColsData
  } from '@/api/creatModel'

  export default {
    name: 'marking',
    data() {
      return {
        Loading: true,
        saveLoading2: true,
        fieldId: '',  // 打标字段ID
        tableHeight: '',
        modeleId: '',
        modelName: '',
        headData: [],
        tableData: [],
        totalnum: 20,
        size: 15,
        curIndex: 0,
        connectSymbolList: [],
        countSymbolList: [],
        mineStatus: '',
        mineStatusValue: [],
        treeLevdata: [],
        defaultProps: {
          children: 'childrenNode', // 子集的属性
          label: 'tagName',
          disabled: 'leaf'// 是否可以选择
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
        chooseTagTeamid: ''
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
      filterText(val) {
        this.$refs.treeForm.filter(val);
      },
      'tagSetList': {
        handler: function (newValue, oldValue) {
          // console.log('tagSetlist',newValue)
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
      'theadData': {
        handler: function (newValue, oldValue) {
          this.theadData = newValue
          this.$set(this.theadData)
        },
        deep: true
      },
    },
    created() {
      this.modeleId = this.$route.query.id
      this.modelName = this.$route.query.modelName
      this.getlistTable()
      this.getModelColsList()
      this.getConnectList('dt.tag.conditions.connect')
      this.getCountList('dt.tag.conditions.noconnect')
      this.tableHeight = document.body.clientHeight - 137
    },
    mounted() {
    },
    methods: {
      closeControl() {
        this.makingDialog = false
        this.$refs.ruleForm.resetFields()
      },
      dataMaking(row) {
        this.setTagsDialog = true
        this.fieldId = row.colId
        this.colId = row.colId
        this.sourceCol = row.sourceCol
        //获取标签组
        this.getMyTagGroupList(row.colId)
      },
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      search() {
        console.log("查询");
      },
      async getlistTable() {
        try {
          const list = await getSearchcoofield({
            modelId: this.modeleId,
            // modelId: '1643371',
            userId: ''
          })
          // console.log('表头list', list);
          this.headData = list.rows
        } catch (e) {
          console.log(e);
        }
      },
      // 列表数据
      async getModelColsList() {  // 1643371
        try {
          const data = await getModelColsData(this.modeleId, 0, 100, 1)
          // console.log('表格数据', data)
          if(data.data.content && data.data.content.length >0){
            this.tableData = data.data.content
          }else{
            this.tableData = []
            this.Loading = false
          }
        } catch (e) {
          console.log(e);
        }
      },

      // dabiao
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
        //console.log(item)
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
        console.log('666',this.sourceCol);
        const name = this.sourceCol
        console.log('999',this.tableData);
        this.tableData.forEach((item, index) => {
          console.log('888',item[name])
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
        this.selfMarkList.splice(index, 1)
      },
      search() {
        // console.log("查询");
      },
      ///过滤树
      filterNode(value, data) {
        if (!value) return true
        return data.tagName.indexOf(value) !== -1
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
      },
      // 编辑标签组
      editLabelgroup() {
        if (this.chooseTagTeamid) {
          this.$router.push({
            path: '/editTree/' + this.chooseTagTeamid,
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
      // 协作选择标签组
      async getMyTagGroupList(fieldId) {
        try {
          const params = {
            colField: fieldId,
            modelId: this.modeleId
          }
          const data = await getTaggroup(params)
          this.tagTeamList = data.rows
        } catch (e) {
          console.log(e);
        }
      },
      // 标签层数据
      async getTagLevList(id) {
        try {
          const data = await getTagLevData(id)
          data.childrenNode.forEach(item => {
            if (item.leaf == true) {
              Object.assign(data.childrenNode, {disabled: true})
            }
          })
          this.treeLevdata = data.childrenNode
        } catch (e) {
          console.log(e);
        }
      },
      // 查询打标历史接口
      async getHistoryColList(colId) {
        // console.log('colId',colId)
        const params = {
          colId: colId
        }
        try {
          const data = await getHistoryColData(params)
          // console.log('打标历史接口data', data)
          //被选标签组
          this.ruleForm.tagTeam = data.selectTagGroup.id
          //标签层数树
          this.getTagLevList(this.ruleForm.tagTeam)
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
        try {
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
          const data = await saveMarkData(params)
          this.$message({
            showClose: true,
            message: '打标成功',
            duration: 2000,
            type: 'success'
          })
          this.setTagsDialog = false
          this.selfMarkList = []

        } catch (e) {

        }
      },
      //选中要打标条件修改
      chooseMark(item, index) {
        this.curIndex = index
        this.isHandle = item.isHandle
        this.conditionSetting = item.conditionSetting
      },
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

  .contentTitle {
    font-size: 14px;
    color: #333333;
    height: 50px;
    line-height: 50px;
    margin-left: 50px;
  }

  .daList {
    margin-top: -20px;
    padding: 0px 20px 0px 25px;
  }

  .rightBtn {
    margin-right: 20px;
  }

  .button2 {
    background-color: rgba(0, 204, 204, 1);
  }

  .iconLogo {
    float: right;
    font-size: 18px;
    margin-right: 10px;
    cursor: pointer;
  }

  .makingEdit {
    font-size: 14px;
    margin-left: 12px;
    color: #00CCCC;
    cursor: pointer;
  }

  /**/
  .sel {
    /*width: 215px;*/
    height: 32px;
  }

  .selIcon {
    height: 32px;
    line-height: 32px;
    margin-left: 9px;
  }

  .zxIcon {
    margin-left: 8px;
    line-height: 37px;
  }

  .treeBoder, .treeBoder2 {
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    padding: 15px 10px;
    background-color: #fff;
    position: relative;
    z-index: 55;
    overflow: auto;
    width: 215px;
    padding-top: 3px;
  }

  .span-ellipsis {
    width: 100%;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    display: block;
  }

  .treeBoder2 {
    width: 218px;
    position: absolute;
    right: 175px;
    margin-top: 10px;
  }

  .dataBtn {
    margin-left: 10px;
    background-color: #00CCCC;
    border-radius: 4px;
    color: #fff;
    font-size: 12px;
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
  }

  .card {
    margin-bottom: 10px;
    cursor: pointer;
    &.acitve {
      /*      box-shadow: 0 2px 12px 0 #0486fe;
            box-shadow: 0 2px 12px 0 #86fe00;*/
      /*  border: 1px solid #0486fe;*/

    }
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
    /*  position: relative;*/
    position: absolute;
    right: 100px;

  }

  .self-mark-list {
    width: 200px;
    position: absolute;
    right: 0px;
    /*  margin-top: 10px;*/
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
      /* display: flex;*/
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

  .clearfix:after {
    content: '';
    display: block;
    clear: both;
  }
</style>
