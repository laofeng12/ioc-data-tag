<template>
  <div class="table-box">
    <div class="showCon" @click="hidePanel">
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
      <!--改造-->
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
    getSymolsData,
    getListdata,
    getListalldata
  } from '@/api/creatModel'

  export default {
    components: {ElementPagination},
    name: 'datasetEditable',
    props: {
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
        val: [],
        defaultParams: {},
        arrtest: [],
        mytest: [
          {
            code: '',
            createTime: "2019-09-10 10:22:18",
            createUser: 392846190550001,
            id: 848821386820223,
            isDeleted: 0,
            isNew: '',
            isShare: 0,
            message: '',
            modifyTime: "2019-10-22 17:16:55",
            percentage: 0,
            popularity: 0,
            popularityLevel: '',
            synopsis: "1001010",
            tagName: "测试10",
            label: '测试10',
            value: '848821386820223',
            children: [{
              "value": "848956331340223",
              "tagName": "测试10-2",
              label: '测试10-2',
              "children": [
                {"value": "848956811340223", label: '熟女22', "tagName": "熟女22", "children": undefined},
                {
                  "value": "848956939080223",
                  "label": "剩女",
                  "children": [
                    {"value": "848956811340223", "label": "熟女", "children": undefined}
                  ]
                }, {"value": "849050497670223", "label": "圣女", "children": undefined}, {
                  "value": "849050960050223",
                  "label": "大叔",
                  "children": [{"value": "848956811340223", "label": "熟女", "children": undefined}]
                }]
            }]
          },
          {
            code: '',
            createTime: "2019-09-10 10:22:18",
            createUser: 392846190550001,
            id: 848821386820223,
            isDeleted: 0,
            isNew: '',
            isShare: 0,
            message: '',
            modifyTime: "2019-10-22 17:16:55",
            percentage: 0,
            popularity: 0,
            popularityLevel: '',
            synopsis: "1001010",
            tagName: "没有标签",
            label: '没有标签',
            value: '848821386820223',
            children: []
          }
        ],
        mytestId: ['848821386820223', '848956331340223', '848956811340223'],
        changeRed: -1,
        znumber: '',
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
    },
    created() {
      //获取标签组
      this.allData()
      // this.getMyTagGroupList()
      this.getConnectList('dt.tag.conditions.connect')
      this.getCountList('dt.tag.conditions.noconnect')
      this.tableHeight = document.body.clientHeight - 190
    },
    mounted() {
    },
    methods: {
      getLabel() {
      },
      closeSettags() {
      },
      // 取消
      canselMark() {
        this.setTagsDialog = false
      },
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
          if (item.codename === '(') {
            this.selfMarkList[this.curIndex].conditionSetting.push(conditionObj)
          } else {
            this.selfMarkList[this.curIndex].conditionSetting.splice(1, 1)
            this.selfMarkList[this.curIndex].conditionSetting.push(conditionObj)
          }

        } else {
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
        } else {
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
          tagId: this.ruleForm.tagSet,  // 标签id
          tagSetName: tagSetName,  // 标签名称
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
        // console.log('选中自动打标内容',item);
        this.checkList = item.checkList
        item.conditionSetting[0].theValues = this.checkList.join(",")
        // item.checkList = this.checkList
        item.showSelfMark = false
        this.showSelf(item)
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
      colSetTags(data) {
        this.setTagsDialog = true
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
          case '1': // 数据打标
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
          data.condtion.forEach(item => {
            item.conditionSetting.map(itemIndex => {
              if (itemIndex.theValues == null) {
                itemIndex.theValues = ''
              }
            })

          })
          this.selfMarkList = this.deepClone(data.condtion)
          this.selfMarkList.map((item, index) => {
            const arrPath = item.idPath.split(',')
            item.showSelfMark = false
            item.checkList = item.conditionSetting[0].theValues.split(',')
            // this.checkList = item.conditionSetting[0].theValues.split(',')
            // item.tagSetName = item.tagName
            // item.tagId = ["848842483740223","892854851930228","892854964160228","897353752450009"]
            item.tagId = arrPath
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
        this.selfMarkList.map(item =>{
          if(this.changeRed == -1){
            const arrId = item.tagId.pop()
            item.tagId = arrId
          }else {
            // console.log('item22',item);
            // console.log('item555',item.tagId);
          }
        })
        // console.log('this.valuesType',this.valuesType)
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
      hidePanel(event) {
        let sp2 = document.getElementById("handle");
        if (sp2) {
          if (!sp2.contains(event.target)) {
            this.showLevTree = false;
          }
        }
      },
      // 打标所有的数据
      async allData(){
        const resData = await getListalldata()
        this.arrtest = resData
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
    /*padding: 0px 10px;*/
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
    /*display: flex;*/
    display: -webkit-box;
    align-items: center; /*垂直居中*/
    overflow: auto;
  }

  .conditions {
    /*display: flex;*/
    display: -webkit-box;
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
    color: #606266;
    padding: 0 15px;
    font-size: 12px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .borderColor {
    border: 1px solid #ee0320;
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
