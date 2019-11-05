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
                <el-tooltip class="item" effect="dark" content="打标" placement="top">
                <i v-if="item.isMarking==1&&item.isCooField==1" class="el-icon-price-tag  iconLogo"
                   @click="dataMaking(item)"></i>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <el-dialog class="creat" title="数据打标" :visible.sync="setTagsDialog" width="900px" left
               :modal-append-to-body="false" :close-on-click-modal="false"
               @close="closeSettags" @open="init">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
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
            <el-row class="autoBtn">
              <el-button type="primary" size="small" @click="selfMark">添加手动</el-button>
              <el-button type="primary" size="small" @click="handleMark">添加自动</el-button>
            </el-row>
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
            <el-row class="autoBtn">
              <el-button type="primary" size="small" @click="selfMark">添加手动</el-button>
              <el-button type="primary" size="small" @click="handleMark">添加自动</el-button>
            </el-row>
          </div>
          <!--自动打标不可操作-->
          <div class="makingContent">
            <!--打标开始-->
            <div class="card" v-for="(item,index) in selfMarkList" :key="index" @click="chooseMark(item,index)"
                 :class="{acitve:curIndex===index}">
              <el-card class="box-card" :class="{borderColor:changeRed == index+1}">
                <!--自动打标结构-->
                <div class="card-handle" v-if="item.isHandle===0">
                  <div class="auto">自动</div>
                  <el-cascader ref="myCascader"  v-model="item.tagId" size="small"
                               :options="arrtest" :props="defaultParams" :clearable="true":show-all-levels="false"></el-cascader>
                  <span class="chinese">{{item.sourceCol}}</span>
                  <div class="conditions">
                    <div class="condition" v-for="(conItem,conIndex) in item.conditionSetting" :key="'con'+conIndex">
                      <div class="count-symbol" v-if="conItem.isConnectSymbol===0">
                        <span class="smbol-len" v-show="conItem.symbol.length>2">{{conItem.symbol}}</span>
                        <span class="smbol" v-show="conItem.symbol.length<3">{{conItem.symbol}}</span>
                        <el-input style="width:100px" size="small" v-model="conItem.theValues" clearable
                                  placeholder="请输入内容"></el-input>
                      </div>
                      <div class="connect-symbol" v-else>
                        <span class="smbol">{{conItem.symbol}}</span>
                      </div>
                    </div>
                  </div>
                  <div class="autoDelete" @click="delSelfMark(index)">删除</div>
                </div>
                <!--手动打标结构-->
                <div class="card2" v-else>
                  <div>
                    <div class="auto">手动</div>
                  </div>
                  <div>
                    <el-cascader ref="myCascader"  v-model="item.tagId" size="small"
                                 :options="arrtest" :props="defaultParams" :clearable="true":show-all-levels="false"></el-cascader>
                  </div>
                  <div class="chinese">{{item.sourceCol}}</div>
                  <div class="self-mark-choose-box">
                    <div class="chooseNum" @click="showSelf(item,$event)" id="handle">
                      <span>已选</span>
                      <span class="num">{{item.checkList.length}}</span>
                      <!--<span class="num">{{checkList.length}}</span>-->
                      <span>条</span>
                      <i class="el-icon-caret-top" v-if="item.showSelfMark==true"></i>
                      <i class="el-icon-caret-bottom" v-else></i>
                    </div>

                    <div class="self-mark-list" v-show="item.showSelfMark">
                      <el-input
                        size="small"
                        clearable
                        placeholder="输入关键字搜索"
                        v-model="searchWord">
                        <i slot="suffix" class="el-input__icon el-icon-search" @click="search"></i>
                      </el-input>
                      <div class="checkIt">
                        <div class="checkOne" v-for="(colItem,cIndex) in listCheck">
                          <!--<el-checkbox-group v-model="item.checkList" @change="checkMarkChange(item)">-->
                          <!--<el-checkbox :key='cIndex' :label="colItem.markName"><span class="col-name" :title="colItem.markName">{{colItem.markName}}</span></el-checkbox>-->
                          <!--</el-checkbox-group>-->
                          <el-checkbox @change="checkMarkChange(item)" v-model="item.checkList" :key='cIndex'
                                       :label="colItem.markName"><span class="col-name" :title="colItem.markName">{{colItem.markName}}</span>
                          </el-checkbox>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="autoDelete" @click="delSelfMark(index)">删除</div>
                </div>

              </el-card>
            </div>
            <!--打标结束-->
          </div>
        </div>

      </el-form>
      <div slot="footer" class="dialog-footer device" style="text-align: center">
        <el-button size="small" class="queryBtn cancleBtn" :loading="saveLoading" @click="canselMark">取消</el-button>
        <el-button size="small" type="primary" class="queryBtn  sureBtn" :loading="saveLoading" @click="saveMark">确定
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
    getModelColsData,
    getListalldata
  } from '@/api/creatModel'

  export default {
    name: 'marking',
    data() {
      return {
        defaultParams: {},
        arrtest: [],
        changeRed:-1,
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
      //获取标签组
      this.allData()
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
      // 取消
      canselMark() {
        this.setTagsDialog = false
      },
      getLabel(){},
      closeSettags(){},
      closeControl() {
        this.makingDialog = false
        this.$refs.ruleForm.resetFields()
      },
      dataMaking(row) {
        this.setTagsDialog = true
        this.valuesType = row.sourceDataType
        this.fieldId = row.colId
        this.colId = row.colId
        this.sourceCol = row.sourceCol
        //获取标签组
        // this.getMyTagGroupList(row.colId)
        this.getHistoryColList(row.colId)
      },
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      search() {
        // console.log("查询");
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
          if(item.codename==='('){
            this.selfMarkList[this.curIndex].conditionSetting.push(conditionObj)
          }else {
            this.selfMarkList[this.curIndex].conditionSetting.splice(1, 1)
            this.selfMarkList[this.curIndex].conditionSetting.push(conditionObj)
          }

        }else{
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
        const consLen = this.selfMarkList[this.curIndex].conditionSetting.length
        if (consLen === 0) {
          this.selfMarkList[this.curIndex].conditionSetting.push(conditionObj)
        } else if (consLen === 1) {
          this.selfMarkList[this.curIndex].conditionSetting = []
          this.selfMarkList[this.curIndex].conditionSetting.push(conditionObj)
        } else if (consLen === 2) {
          this.selfMarkList[this.curIndex].conditionSetting.push(conditionObj)
        }else {
          this.selfMarkList[this.curIndex].conditionSetting.push(conditionObj)
        }
        this.conditionSetting = this.selfMarkList[this.curIndex].conditionSetting

      },
      closePanel() {
        this.refs['ruleForm'].resetFields()
        this.ruleForm.tagTeam = ''
        this.selfMarkList = []
      },
      //点击人工打标按钮
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
          isHandle: 1,//人工打标
          conditionSetting: conditionSetting
        }
        this.selfMarkList.push(markObj)
        this.curIndex = this.selfMarkList.length - 1
        this.isHandle = 1
      },
      //自动打标
      handleMark() {
        let tagSetName = ''
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
          isHandle: 0,//自动打标
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
        // item.checkList = this.checkList
        item.showSelfMark = false
        this.showSelf(item)
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
        this.changeRed = -1
      },
      search() {
        // console.log("查询");
      },
      ///过滤树
      filterNode(value, data) {
        if (!value) return true;
        return data.tagName.indexOf(value) !== -1;
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
      //关闭打标
      close() {
        this.showSelfMark = false
        this.colList = []
      },
      // 查询打标历史接口
      async getHistoryColList(colId) {
        const params = {
          colId: colId
        }
        try {
          const data = await getHistoryColData(params)
          //打标相关字段  this.checkList
          data.condtion.forEach(item =>{
            item.conditionSetting.map(itemIndex =>{
              if(itemIndex.theValues == null){
                itemIndex.theValues = ''
              }
            })

          })
          this.selfMarkList = this.deepClone(data.condtion)
          this.selfMarkList.map((item, index) => {
            const arrPath = item.idPath.split(',')
            item.showSelfMark = false
            item.checkList = item.conditionSetting[0].theValues.split(',')
            item.tagId = arrPath
          })
          this.curIndex = this.selfMarkList.length - 1
          this.conditionSetting = this.selfMarkList[this.curIndex].conditionSetting
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
        this.selfMarkList.map(item =>{
          const arrId = item.tagId.pop()
          item.tagId = arrId
        })
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
        try {
          const data = await saveMarkData(params)
          this.$message({
            showClose: true,
            message: '打标成功',
            duration: 2000,
            type: 'success'
          })
          this.setTagsDialog = false
          this.selfMarkList = []
          this.changeRed = -1
        } catch (e) {
          console.log('e', e);
          this.changeRed = e.data
        }
      },
      //选中要打标条件修改
      chooseMark(item, index) {
        this.curIndex = index
        //console.log('选中要打标的项',item)
        this.isHandle = item.isHandle
        this.conditionSetting = item.conditionSetting
      },
      // 打标所有的数据
      async allData(){
        const resData = await getListalldata()
        this.arrtest = resData
      }
    },
    computed: {
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
    color: #409eff;
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
    border-radius: 4px;
    background: #F3F3F3;
  }

  .contentTop {
    display: flex;
    border-bottom: 1px solid #dcdfe6;
    position: relative;
    background-color: #fff;
    .connect-smbol-box {
      display: flex;
    }
    .count-smbol-box {
      display: flex;
    }
  }

  .topOne {
    width: 40px;
    height: 40px;
    text-align: center;
    line-height: 40px;
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
    min-height: 200px;
    max-height: 400px;
    overflow: auto;
    padding-bottom: 10px;
    padding: 0px 10px;
    background: #F3F3F3;
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
    padding: 8px 0px;
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
    display: -webkit-box;
    align-items: center; /*垂直居中*/
    overflow: auto;
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
.borderColor{
  border: 1px solid #ee0320;
}
  .clearfix:after {
    content: '';
    display: block;
    clear: both;
  }
</style>
<style>
  .creat .el-dialog__title {
    font-family: PingFangSC-Medium;
    font-size: 16px;
    color: #262626;
  }

  .creat .el-dialog__body {
    padding: 18px 20px !important;
  }

  .creat .el-button {
    padding: 0 16px !important;
    font-family: PingFangSC-Regular;
    font-size: 14px;
    color: #0486FE;
    background-color: #fff !important;
  }

  .creat .sureBtn {
    color: #fff;
    background-color: #0486fe !important;
  }

  .creat .cancleBtn {
    border: 1px solid #0486FE;
  }

  .creat .card-handle {
    min-height: 50px;
  }

  .creat .el-card__body {
    padding: 6px 70px 3px 9px !important;
  }

  .auto {
    font-family: PingFangSC-Regular;
    font-size: 14px;
    color: #999999;
    margin-right: 14px;
  }

  .autoDelete {
    font-family: PingFangSC-Regular;
    font-size: 14px;
    color: #FF7374;
    position: absolute;
    right: 20px;
    line-height: 50px;
  }

  .autoBtn {
    position: absolute;
    right: 11px;
    top: 7px;
  }

  .autoBtn .el-button {
    height: 26px !important;
    line-height: 26px !important;
  }
</style>
