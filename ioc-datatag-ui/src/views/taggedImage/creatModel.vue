<template>
  <div class="table-container">
    <div class="header">
      <div class="left">
        <router-link class="return" to="/lableImage">
          <i class="el-icon-arrow-left"></i>
        </router-link>
        <div class="name">
          <div class="img"></div>
          <div class="text">
            <div class="zedit" v-if="routerName==='editModel'">
              <!--<el-input v-show="!showBtn" size="small" v-model="runModelname" placeholder="请输入内容"-->
              <!--@blur="getsaveName"></el-input>-->
              <el-input v-show="!showBtn" size="small" clearable v-model.trim="runModelname" placeholder="请输入模型名称"
                        maxlength="25"
                        show-word-limit></el-input>
              <span v-show="showBtn">模型名称：{{runModelname}}</span>
              <div class="editTwo">
                <i v-show="showBtn" class="el-icon-edit" @click="rename"></i>
                <el-button v-show="!showBtn" size="mini" type="primary" @click="getsaveName">确定</el-button>
              </div>
            </div>
            <span v-else> 模型名称：未命名</span>
          </div>
        </div>
      </div>
      <div class="right" v-show="routerName==='editModel'">
        <el-button class="button" type="primary" size="small" @click="saveAsto">另存模型</el-button>
        <el-button class="button" type="primary" size="small" @click="runModel">模型调度</el-button>
        <!--<el-button class="button" type="primary" size="small" @click="saveModel">保存模型</el-button>-->
      </div>
    </div>
    <div class="allContent">
      <div class="content">
        <!--左边数据树目录结构-->
        <Aside :modelData="modelData" ref="tree" @commit="getModelDatalist"/>
        <div class="components" v-if="routerName==='creatModel'">
          <div class="top">
            <div class="headLeft">字段设置</div>
            <div class="right rightNew2">
              <span class="cooperation">协作人员:</span>
              <img class="imgPeople" src="../../assets/img/icon_default.png" height="18" width="18"/>
              <span class="multiply">x</span>
              <span class="num">{{peopleLength+1}}</span>
              <span class="handlePeople"><i class="el-icon-arrow-down" @click="showIt"></i></span>
            </div>
            <div class="card" v-show="show">
              <div class="clearfix">
                <div class="peopleList">成员列表</div>
                <div class="addPeople"><i class="el-icon-plus" @click="addPeople"></i></div>
              </div>
              <div class="peopleContent">
                <div class="contentA clearfix">
                  <div>
                    <img class="imgPeople2" src="../../assets/img/Uadmin.png" height="16" width="16"/>
                    <img class="imgPeople2" src="../../assets/img/u163.png" height="25" width="25"/></div>
                  <div class="listName" :title="this.$store.state.user.userInfo.userName">
                    {{this.$store.state.user.userInfo.userName}}
                  </div>
                </div>
                <div class="contentB clearfix" v-for="(item,index) in showPeoplelist" :key="index">
                  <div><img class="imgPeople2" src="../../assets/img/u163.png" height="25" width="25"/></div>
                  <div class="listName" :title="item.cooUserName">{{item.cooUserName}}</div>
                  <div class="head"><i class="el-icon-delete" @click="deleteList(item.id)"></i></div>
                </div>
              </div>
            </div>
          </div>
          <div v-if="routerName==='editModel'">
            <EditTable ref="ztable" :tableData="modelTableData" :modelId="taggingModelId"
                       @commit2="getModelDatalist"></EditTable>
          </div>

        </div>
        <div class="components" v-if="routerName==='editModel'">
          <div class="top">
            <div class="left">
              {{modelData.resourceName}}
              <el-button v-if="routerName==='editModel'" class="set-btn btnMargin" type="text" size="mini"
                         @click.stop="editSetTags(1)">
                <i class="el-icon-setting"></i>
              </el-button>
            </div>
            <div class="right rightNew">
              <span class="cooperation">协作人员:</span>
              <img class="imgPeople" src="../../assets/img/icon_default.png" height="18" width="18"/>
              <span class="multiply">x</span>
              <span class="num">{{peopleLength+1}}</span>
              <span class="handlePeople"><i class="el-icon-arrow-down" @click="showIt"></i></span>
            </div>
            <div class="card" v-show="show">
              <div class="clearfix">
                <div class="peopleList">成员列表</div>
                <div class="addPeople"><i class="el-icon-plus" @click="addPeople"></i></div>
              </div>
              <div class="peopleContent">
                <div class="contentA clearfix">
                  <div>
                    <img class="imgPeople2" src="../../assets/img/Uadmin.png" height="16" width="16"/>
                    <img class="imgPeople2" src="../../assets/img/u163.png" height="25" width="25"/></div>
                  <div class="listName" :title="this.$store.state.user.userInfo.userName">
                    {{this.$store.state.user.userInfo.userName}}
                  </div>
                </div>
                <div class="contentB clearfix" v-for="(item,index) in showPeoplelist" :key="index">
                  <div><img class="imgPeople2" src="../../assets/img/u163.png" height="25" width="25"/></div>
                  <div class="listName" :title="item.cooUserName">{{item.cooUserName}}</div>
                  <div class="head"><i class="el-icon-delete" @click="deleteList(item.id)"></i></div>
                </div>
              </div>
            </div>
          </div>
          <div v-if="routerName==='editModel'">
            <EditTable ref="ztable" :tableData="modelTableData" :modelId="taggingModelId"
                       @commit2="getModelDatalist"></EditTable>
          </div>

        </div>

      </div>
    </div>
    <!--另存-->
    <el-dialog class="creat" title="另存模型" :visible.sync="editDialog" width="530px" center :close-on-click-modal="false"
               @close="closeSaveas">
      <div class="del-dialog-cnt">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
          <el-form-item label="模型名称:" prop="name" class="nameOne">
            <el-input v-model="ruleForm.name" maxlength="25" show-word-limit placeholder="请输入模型名称"></el-input>
          </el-form-item>
          <el-form-item label="模型简介:" prop="textarea2" class="nameOne">
            <el-input
              class="area"
              type="textarea"
              maxlength="100"
              show-word-limit
              :autosize="{ minRows: 2, maxRows: 4}"
              placeholder="请输入模型简介"
              v-model="ruleForm.textarea2">
            </el-input>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer device">
        <div>
          <el-button size="small" type="primary" class="queryBtn" :loading="saveasLoading" @click="saveAsmodel">确认另存
          </el-button>
        </div>
      </div>
    </el-dialog>
    <!--另存 end-->
    <!--运行-->
    <el-dialog class="creat" title="模型调度" :visible.sync="runDialog" width="530px" center :close-on-click-modal="false"
               @close="closeRun">
      <div class="del-dialog-cnt">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px">
          <el-form-item label="模型名称:" prop="zmodelName" class="nameOne">{{runModelname}}</el-form-item>
          <el-form-item label="运行开始时间:" prop="date" class="nameOne" v-if="this.ruleForm.region != 6">
            <el-date-picker
              size="small"
              class="dateInp"
              value-format="yyyy-MM-dd HH:mm:ss"
              format="yyyy-MM-dd HH:mm:ss"
              v-model="ruleForm.date"
              type="datetime"
              :picker-options="pickerOptions"
              placeholder="选择日期时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="运行时间周期:" prop="region" class="nameOne">
            <el-select class="controlChoose" size="small" v-model="ruleForm.region" placeholder="请选择">
              <el-option
                v-for="item in options2"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer device">
        <div>
          <el-button size="small" type="primary" class="queryBtn" :loading="savedispatchLoading" @click="goDispatchto">
            确定调度
          </el-button>
        </div>
      </div>
    </el-dialog>
    <!--运行 end-->
    <!--协作添加-->
    <el-dialog class="creat addCreat" title="添加成员" :visible.sync="addSetDialog" width="1100px" center
               :modal-append-to-body="false" :close-on-click-modal="false"
               @close="close">
      <div class="col-set-box">
        <el-container class="">
          <el-aside width="250px" class="left">
            <h3>选择协作用户</h3>
            <el-input placeholder="输入关键词搜索列表" clearable v-model.trim="searchText" size="small"
                      prefix-icon="el-icon-search"
            ></el-input>
            <div class="allPeople">
              <div class="userContent clearfix" v-for="(item,index) in list" :key="index">
                <div class="peopleName" ref="people" :title="item.FULLNAME">{{item.FULLNAME}}</div>
                <div><i class="el-icon-circle-plus-outline addIcon" @click="getaddPeople(item.USERID)"></i></div>
              </div>
            </div>
          </el-aside>
          <el-aside width="250px" class="left">
            <h3>已选用户</h3>
            <el-input placeholder="输入关键词搜索列表" clearable v-model.trim="searchText2" size="small"
                      prefix-icon="el-icon-search"></el-input>
            <div class="allPeople">
              <div class="userContent clearfix" v-for="(item,index) in list2" :key="index"
                   :class="{zxhh:changeRed == index}">
                <div class="peopleName" :title="item.cooUserName" @click="markingPeople(item.cooUser,item.id,index)">
                  {{item.cooUserName}}
                </div>
                <div><i class="el-icon-delete addIcon" @click="deleteList(item.id,index)"></i></div>
              </div>
            </div>
          </el-aside>
          <div class="right2">
            <h3>选择协作打标字段</h3>
            <div class="marking">
              <el-table class="my-table tableHeight" ref="multipleTable" :data="tableData" border
                        tooltip-effect="dark"
                        style="width: 100%;"
                        :header-cell-style="{background:'#f0f2f5'}">
                <el-table-column label="字段" prop="showCol"></el-table-column>
                <el-table-column prop="sourceDataType" label="类型" width="100"></el-table-column>
                <el-table-column
                  label="选择标签组">
                  <template slot-scope="scope">
                    <el-checkbox v-show="false"></el-checkbox>
                    <el-select v-if="scope.row.useTagGroup == '123947334341780'" class="controlChoose2" size="small"
                               v-model="scope.row.useTagGroup" placeholder="请选择"
                               @change="chooseSelect(scope.row)">
                      <el-option
                        v-for="item in options3"
                        :key="item.id"
                        :label="item.tagsName"
                        :value="item.id">
                      </el-option>
                    </el-select>
                    <el-select v-else class="controlChoose2" size="small" v-model="scope.row.useTagGroup"
                               placeholder="请选择"
                               @change="chooseSelect(scope.row)"
                               :disabled="(!helpId || (scope.row.cooUser && scope.row.cooUser !== helpId))">
                      <el-option
                        v-for="item in options3"
                        :key="item.id"
                        :label="item.tagsName"
                        :value="item.id">
                      </el-option>
                    </el-select>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div slot="footer" class="dialog-footer device">
              <div>
                <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading" @click="getdosave">确认添加
                </el-button>
              </div>
            </div>
          </div>
        </el-container>
      </div>
    </el-dialog>
    <!--协作添加 end-->
  </div>
</template>

<script>
  import EditTable from '../../components/ModeleEdit/EditTable'
  import Aside from '../../components/ModeleEdit/Aside'
  import {
    choosePeople,
    getPeople,
    addPeople,
    deletePeople,
    markingCheck,
    labelGroup,
    dosave,
    getModelData,
    getModelColsData,
    saveName,
    saveAs,
    goDispatch,
    getmodelDispatchdetail
  } from '@/api/creatModel'

  export default {
    name: 'creatModel',
    data() {
      return {
        sureIt: 0,
        userIdarr: [],
        groupId: [],
        selectD: 0,
        openScope: '',
        showBtn: true,
        headColList: [], // 打标字段头部数据
        modelData: {}, // 获取模型数据
        taggingModelId: 0, // 模型id
        routerName: 'creatModel',
        runModelname: '未命名',
        show: false,
        editDialog: false,
        saveLoading: false,
        saveasLoading: false,
        savedispatchLoading: false,
        runDialog: false,
        saveDialog: false,
        value1: '',
        value: '',
        search: '',
        chooseList: [],
        showPeoplelist: [],
        arrlist: [],
        peopleLength: 0,
        modeId: 1640046, // 模型ID
        ruleForm: {
          name: '',
          textarea2: '',
          modelName: '',
          date: null,
          region: null
        },
        runModelname: '',
        searchText: '',
        searchText2: '',
        isIndeterminate: true,
        addSetDialog: false,
        modelTableData: [],
        options3: [],
        selectVal: '',
        changeRed: -3,
        startDisable: true,
        helpId: '',  // 协作用户id
        cooId: '',
        zcolId: '',
        options2: [{
          value: '0',
          label: '停止运行'
        }, {
          value: '6',
          label: '立即执行'
        },{
          value: '1',
          label: '运行一次'
        }, {
          value: '2',
          label: '每天一次'
        }, {
          value: '3',
          label: '每周一次'
        }, {
          value: '4',
          label: '每月一次'
        }, {
          value: '5',
          label: '每年一次'
        }],
        rules: {
          name: [
            {required: true, message: '请填写', trigger: 'blur'}
          ],
          textarea2: [
            {required: true, message: '请填写', trigger: 'blur'}
          ],
          date: [{required: true, message: '请选择时间', trigger: 'blur'}],
          region: [{required: true, message: '请选择时间周期', trigger: 'change'}]
        },
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() <= Date.now() - 8.64e7
          }
        },
        tableData: []
      }
    },
    components: {EditTable, Aside},
    watch: {
      'headColList': {
        handler: function (newValue, oldValue) {
          this.headColList = newValue
        },
        deep: true
      },
      '$route'(to, from) {
        this.routerName = to.name
        if (to.name === 'editModel') {
          this.taggingModelId = to.params.id
          this.getModelList(this.taggingModelId)
          this.getModelColsList(this.taggingModelId, 0, 100, 1)
        }
      }
    },
    mounted() {
    },
    methods: {
      rename() {
        this.showBtn = false
      },
      // type=0,新增，type=1编辑
      editSetTags(type) {
        // 调用子组件里面的方法
        // this.$refs['tree'].setTags(type)
        // this.$refs['tree'].handleNodeClick()
        this.$refs.tree.handleNodeClick()
      },
      // 获取模型数据
      async getModelList(modelId) {
        try {
          const params = {
            taggingModelId: modelId
          }
          const data = await getModelData(params)
          this.modelData = data
          this.modelName = data.resourceName
          this.runModelname = data.modelName
          this.headColList = data.colList
          this.$store.dispatch('getlistArr', data.colList)
          this.getModelColsList(this.taggingModelId, 0, 100, 1)
        } catch (e) {
        }
      },
      getModelDatalist() {
        if (this.$route.name == 'editModel') {
          this.getModelList(this.$route.params.id)
        } else {
          return
        }
      },
      // 协作人员添加已选用户
      addPeople() {
        if (this.taggingModelId) {
          this.addSetDialog = true
          this.cooperatioUser()
          this.markingTable()
          this.getpeopleList()
          this.show = false
        } else {
          this.$message.error('请先进行字段设置操作！');
        }

      },
      async close() {
        this.addSetDialog = false
        // 关闭操作去掉已选用户
        this.groupId = []
        this.tableData.map(item => item.useTagGroup).every(async _item => {
          if (_item != null) {
            this.groupId.push(_item)
          }
        })
        if (this.groupId == '') {
          let obj = ''
          this.showPeoplelist.map(item => {
            this.userIdarr.push(item.id)
            obj += item.id + ','
          })
          if (obj.length > 0) {
            obj = obj.substr(0, obj.length - 1);
          }
          if (obj) {
            try {
              const res = await deletePeople({
                ids: obj
              })
              this.getpeopleList()
              this.markingTable()
            } catch (e) {
              console.log(e);
            }
          } else {
            return
          }

        }
      },
      // 获取模型数据
      async getModelColsList(modelId, page, size, type) {
        // console.log(modelId,page,size)
        try {
          // 表格数据
          const data = await getModelColsData(modelId, page, size, type)
          this.modelTableData = data.data.content
        } catch (e) {

        }
      },
      // 显示协作人员列表
      showIt() {
        this.show = !this.show
      },
      saveAsto() {
        this.editDialog = true
      },
      closeSaveas() {
        this.editDialog = false
        this.$refs.ruleForm.resetFields()
      },
      // 模型调度操作
      async runModel() {
        this.runDialog = true
        try {
          const resOk = await getmodelDispatchdetail({
            taggingModelId: this.taggingModelId
          })
          this.options2.forEach(item => {
            if (resOk.cycleEnum == item.value) {
              resOk.cycleEnum = item.value
            }
          })
          this.ruleForm.date = resOk.startTime
          this.ruleForm.region = resOk.cycleEnum
        } catch (e) {
          console.log(e);
        }
      },
      closeRun() {
        this.runDialog = false
        this.$refs.ruleForm.resetFields()
      },
      saveModel() {
        this.saveDialog = true
      },
      closeSave() {
        this.saveDialog = false
      },
      // 选择协作用户列表
      async cooperatioUser() {
        const userId = this.$store.state.user.userInfo.userId
        try {
          const user = await choosePeople(userId)
          this.chooseList = user.data
        } catch (e) {
          console.log(e);
        }

      },
      // 成员列表
      async getpeopleList() {
        const params = {
          eq_createUser: '',
          eq_taggmId: this.taggingModelId,  // 模型ID
          page: '',
          size: 1000
        }
        try {
          const peopleList = await getPeople(params)
          if (peopleList.rows && peopleList.rows.length >= 0) {
            this.showPeoplelist = peopleList.rows
            this.peopleLength = peopleList.rows.length
          }
        } catch (e) {
          console.log(e);
        }
      },
      // 单个协作成员添加
      async getaddPeople(userId) {
        const param = {
          "cooUser": userId,
          "id": 0,
          "taggmId": this.taggingModelId,  // 模型ID
        }
        try {
          const addRes = await addPeople(param)
          this.getpeopleList()
        } catch (e) {
          console.log(e);
        }
      },
      // 删除操作
      async deleteList(id, index) {
        try {
          const res = await deletePeople({
            id: id,
            ids: ''
          })
          this.$message({
            message: res.message,
            type: 'success'
          });
          if (this.changeRed === index) {
            this.changeRed = -3
          }
          this.getpeopleList()
          this.markingTable()
        } catch (e) {
          console.log(e);
        }
      },
      // 点击用户操作
      async markingPeople(zuserid, id, index) {
        this.changeRed = index
        this.helpId = zuserid
        this.cooId = id
        this.startDisable = false
      },
      // table列表数据获取
      async markingTable() {
        const userId = this.$store.state.user.userInfo.userId
        this.tableData = []
        try {
          const markingRes = await markingCheck({
            modelId: this.taggingModelId,  // 模型ID
            userId: userId
          })
          if (markingRes.rows && markingRes.rows.length > 0) {
            markingRes.rows.map(item => {
              if (item.isMarking == 1) {
                item.ischecked = false
                item.select = ''      // 标签组id
                item.helpuserId = ''  // 协作用户id
                item.ztagColName = ''  // 打标字段
                if (item.isCooField == 0) {
                  item.isCooField = false  // checkbox 为false
                } else {
                  item.isCooField = true
                }
                this.tableData.push(item)
              }
            })
          }
        } catch (e) {

        }
      },
      // 我的标签组下拉
      async groupList() {
        const params = {
          eq_isShare: '',
          keyword: '',
          page: '',
          size: ''
        }
        try {
          const groupRes = await labelGroup(params)
          if (groupRes.rows && groupRes.rows.length > 0) {
            const arr = [{
              code: "",
              createTime: "",
              createUser: "",
              id: 123947334341780,
              isDeleted: 1,
              isNew: "",
              isShare: 0,
              message: "",
              modifyTime: "",
              percentage: "",
              popularity: "",
              popularityLevel: "",
              synopsis: "",
              tagsName: "请选择"
            }]
            this.options3 = arr.concat(groupRes.rows)
          }
        } catch (e) {
          console.log(e)
        }
      },
      // checkbox
      getRow(row) {
        if (row.isCooField == false) {
          row.isCooField = true
          if (row.isCooField = true) {
            row.cooUser = this.helpId
          }
        } else {
          row.isCooField = false
          row.helpuserId = ''
        }
      },
      // 下拉选中
      async chooseSelect(row, item) {
        row.cooUser = this.helpId
        row.id = this.cooId
        row.tagColId = row.colId
        let arrRow = []
        arrRow.push(row)
        if (row.useTagGroup == '123947334341780') {
          row.isDeleted = 1
          const tmp = arrRow.filter(item => item.useTagGroup).map(({id, cooFieldId, showCol, useTagGroup, isCooField, cooUser, tagColId, isDeleted}) => {
            return {
              "cooId": id, // id
              "id": cooFieldId,  // cooFieldId
              "tagColName": showCol, // 打标字段
              useTagGroup,  // 标签组ID
              isCooField,// 是否选中
              cooUser, // 协作用户ID
              tagColId: tagColId,
              isDelete: isDeleted
            }
          })
          tmp.forEach(item => {
            if (item.useTagGroup) {
              item.isCooField = true
            }
          })
          let obj_user = []
          this.showPeoplelist.forEach(item_c => {
            tmp.map(item_d => {
              if (item_c.cooUser == item_d.cooUser) {
                obj_user.push(item_c)
                item_c.cooTagcolLimitList = []
                item_c.cooTagcolLimitList.push(item_d)
              }
            })
          })
          try {
            await dosave(obj_user)
            this.markingTable()
          } catch (e) {
            console.log(e);
          }
        }
      },
      // 数据保存操作
      async getdosave() {
        this.saveLoading = true
        const tmp = this.tableData.filter(item => item.useTagGroup).map(({id, cooFieldId, showCol, useTagGroup, isCooField, cooUser, tagColId, isDeleted}) => {
          return {
            "cooId": id, // id
            "id": cooFieldId,  //  cooFieldId
            "tagColName": showCol, // 打标字段
            useTagGroup,  // 标签组ID
            isCooField,// 是否选中
            cooUser, // 协作用户ID
            tagColId: tagColId,
            isDelete: isDeleted
          }
        })
        if (tmp.length == this.tableData.length) {
          for (let data of tmp) {
            if (data.useTagGroup && data.useTagGroup == '123947334341780') {
              this.$message.error('请选择标签组！');
              this.saveLoading = false
              return
            } else {

            }
          }
          tmp.forEach(item => {
            if (item.useTagGroup && item.useTagGroup != '123947334341780') {
              item.isCooField = true
            }
          })
          const arrList = [...this.showPeoplelist]
          const arr = []
          let obj = []
          tmp.map(item_a => {
            arr.push(item_a.cooUser)
          })
          let uniqueArr = [...new Set(arr)]
          // 根据用户的id刷选用户
          arrList.forEach(item_b => {
            uniqueArr.map(item_c => {
              if (item_b.cooUser == item_c) {
                item_b.cooTagcolLimitList = []
                obj.push(item_b)
              }
            })
          })
          tmp.forEach((item, index) => {
            arrList.map((citem, cindex) => {
              if (item.cooUser == citem.cooUser) {
                citem.cooTagcolLimitList.push(item)
              }
            })
          })
          try {
            const saveRes = await dosave(obj)
            this.$message({
              message: saveRes.message,
              type: 'success'
            });
            this.saveLoading = false
            this.addSetDialog = false
            this.sureIt = 1
          } catch (e) {
            this.saveLoading = false
          }
        } else {
          this.$message.error('请选择用户和标签组！');
          this.saveLoading = false
        }

      },
      // 模型名称保存
      async getsaveName() {
        try {
          if (this.runModelname) {
            const resName = await saveName({
              taggingModelId: this.taggingModelId,
              modelName: this.runModelname
            })
            this.$message({
              message: resName.message,
              type: 'success'
            });
            this.showBtn = true
          } else {
            this.$message({
              message: '请输入模型名称',
              type: 'warning'
            });
          }

        } catch (e) {
          console.log(e);
        }

      },
      // 另存模型
      async saveAsmodel() {
        this.saveasLoading = true
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            try {
              const res = await saveAs({
                taggingModelId: this.taggingModelId,
                "modelDesc": this.ruleForm.textarea2,
                "modelName": this.ruleForm.name
              })
              this.$message({
                message: res.message,
                type: 'success'
              });
              this.saveasLoading = false
              this.editDialog = false
              this.$router.push('/lableImage')
            } catch (e) {
              console.log(e);
              this.saveasLoading = false
            }
          } else {
            this.saveasLoading = false
          }
        });

      },
      // 确定调度
      async goDispatchto() {
        this.savedispatchLoading = true
        if (this.ruleForm.region === '6') {
          this.$refs['ruleForm'].clearValidate()
          try {
            const param = {
              "cycleEnum": this.ruleForm.region,
              "id": this.taggingModelId,
              "startTime": this.ruleForm.date
            }
            const resto = await goDispatch(param)
            this.$message({
              message: resto.message,
              type: 'success'
            });
            this.savedispatchLoading = false
            this.runDialog = false
            this.$router.push('/lableImage')
          } catch (e) {
            console.log(e);
            this.savedispatchLoading = false
          }
        }else {
          this.$refs.ruleForm.validate(async (valid) => {
            if (valid) {
              try {
                const param = {
                  "cycleEnum": this.ruleForm.region,
                  "id": this.taggingModelId,
                  "startTime": this.ruleForm.date
                }
                const remindTime = this.ruleForm.date
                const str = remindTime.toString()
                const str2 = str.replace('/-/g', '/')
                const oldTime = new Date(str2).getTime()
                if (oldTime <= new Date().getTime()) {
                  this.$message.error('运行开始时间不能小于当前时间!')
                  this.savedispatchLoading = false
                  return
                }
                const resto = await goDispatch(param)
                this.$message({
                  message: resto.message,
                  type: 'success'
                });
                this.savedispatchLoading = false
                this.runDialog = false
                this.$router.push('/lableImage')
              } catch (e) {
                console.log(e);
                this.savedispatchLoading = false
              }
            } else {
              this.savedispatchLoading = false
            }
          });
        }
      }
    },
    created() {
      this.routerName = this.$route.name
      this.taggingModelId = this.$route.params.id
      if (this.routerName === 'creatModel') {
      } else {
        //进入编辑模型或者打标界面
        //获取模型数据
        this.getModelList(this.taggingModelId)
        // this.getModelColsList(this.taggingModelId, 0, 100, 1)
        this.getpeopleList()
      }
      this.groupList()
    },
    computed: {
      list() {
        let arr = [];
        this.chooseList.map(item => {
          if (item.FULLNAME.indexOf(this.searchText) >= 0) {
            arr.push(item)
          }
        })
        return arr
      },
      list2() {
        let arr2 = [];
        this.showPeoplelist.map(item => {
          if (item.cooUserName.indexOf(this.searchText2) >= 0) {
            arr2.push(item)
          }
        })
        return arr2
      }
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
    .right {
      display: flex;
      align-items: center;
      padding-right: 20px;
      box-sizing: border-box;
      .img {
        width: 25px;
        height: 25px;
        margin-right: 20px;
        background: url("../../assets/img/save.png");
        background-repeat: no-repeat;
        background-size: 100% 100%;
      }
    }
  }

  .content {
    display: flex;
    height: calc(100vh - 189px);
    padding-top: 40px;
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
      .top {
        height: 45px;
        line-height: 45px;
      }
      .search {
        margin-bottom: 20px;
      }
      .con {
        color: #cccccc;
        .box {
          margin-bottom: 25px;
          .title {
            color: #ffffff;
          }
          .line {
            display: flex;
            justify-content: space-between;
            height: 30px;
            line-height: 30px;
            position: relative;
            .type {
              width: 20%;
            }
            .name {
              flex-shrink: 0;
              width: 60%;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
            .icon {
              cursor: pointer;
            }
            .menu {
              width: 150px;
              position: absolute;
              right: -170px;
              top: -50px;
              background: #ffffff;
              color: #333;
              font-size: 14px;
              box-shadow: #999 0 0 4px;
              padding: 10px;
              border-radius: 5px;
              box-sizing: border-box;
              z-index: 2;
              cursor: pointer;
              .menu-con {
                position: relative;
                .menu-line {
                  display: flex;
                  height: 30px;
                  line-height: 30px;
                  .menu-icon {
                    width: 25px;
                    height: 25px;
                    color: #999;
                    font-size: 18px;
                    margin-right: 5px;
                    background-repeat: no-repeat;
                    background-size: 100% 100%;
                  }
                  .menu-name {
                    flex-grow: 1;
                    border-bottom: 1px #ddd solid;
                  }
                }
                .del {
                  width: 15px;
                  height: 15px;
                  position: absolute;
                  top: -10px;
                  right: 0;
                }
              }
            }
          }
        }
      }
    }
    .components {
      width: 100%;
      position: absolute;
      top: 60px;
      bottom: 0;
      padding: 10px 10px 10px 260px;
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

  .cooperation, .multiply, .num {
    font-size: 12px;
    color: #999999;
    margin-left: 4px;
  }

  .cooperation {
    margin-right: 5px;
  }

  .iconPeople {
    font-size: 15px;
    margin-right: 2px;
    margin-top: -5px;
  }

  .handlePeople {
    font-size: 15px;
    margin-left: 5px;
  }

  .right {
    display: flex;
    align-items: center;
  }

  .rightNew {
    border: 1px solid #D9D9D9;
    padding: 2px 2px;
  }

  .rightNew2 {
    padding: 4px;
    border: 1px solid #D9D9D9;
  }

  .card {
    width: 200px;
    border: 1px solid #dedede;
    position: absolute;
    right: 15px;
    z-index: 33;
    margin-top: 35px;
    background-color: #fff;
    border-radius: 8px;
  }

  .peopleList, .addPeople {
    font-size: 14px;
    margin-top: 10px;
  }

  .peopleList {
    float: left;
    margin-left: 15px;
  }

  .addPeople {
    float: right;
    margin-right: 15px;
  }

  .peopleContent {
    margin-top: 15px;
    height: 280px;
    overflow-y: auto;
  }

  .imgPeople2, .listName {
    float: left;
  }

  .imgPeople2 {
    margin-top: 6px;
    margin-left: 8px;
  }

  .listName {
    font-size: 12px;
    width: 105px;
    margin-left: 8px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .head {
    font-size: 14px;
    float: right;
    margin-right: 11px;
  }

  .contentA {
    height: 40px;
    line-height: 40px;
  }

  .dateInp, .controlChoose {
    width: 340px;
  }

  .clearfix:after {
    content: '';
    display: block;
    clear: both;
  }

  .addCreat {
    .col-set-box {
      .left {
        border-right: 1px solid #eee;
        padding: 0 10px;
        color: #000000;
      }
      h3 {
        font-weight: normal;
        font-size: 14px;
        line-height: 32px;
        border-bottom: 1px solid #eee;
      }
    }
    .right2 {
      width: 100%;
      padding: 0 10px;
    }
    .my-table {
      i {
        cursor: pointer;
      }
    }
    .my-table > > >
    thead {
      color: #222222;
      font-size: 14px;
    }

    th {
      background-color: #f4f9fb;
      padding: 5px 0;
    }
    // hover
    tr:hover > td {
      background-color: #f4f9fb;
    }
  }

  .controlChoose2 {
    width: 180px !important;
  }

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
    .right {
      display: flex;
      align-items: center;
      padding-right: 20px;
      box-sizing: border-box;
      .img {
        width: 25px;
        height: 25px;
        margin-right: 20px;
        background: url("../../assets/img/save.png");
        background-repeat: no-repeat;
        background-size: 100% 100%;
      }
    }
  }

  .content {
    display: flex;
    height: calc(100vh - 189px);
    padding-top: 40px;
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
      .top {
        height: 45px;
        line-height: 45px;
      }
      .search {
        margin-bottom: 20px;
      }

    }
    .components {
      width: 100%;
      position: absolute;
      top: 0px;
      bottom: 0;
      padding: 10px 24px 10px 24px;
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

        }
      }
    }
  }

  .cooperation, .multiply, .num {
    font-size: 12px;
    color: #999999;
    margin-left: 4px;
  }

  .cooperation {
    margin-right: 5px;
  }

  .iconPeople {
    font-size: 15px;
    margin-right: 2px;
    margin-top: -5px;
  }

  .handlePeople {
    font-size: 15px;
    margin-left: 5px;
  }

  .right {
    display: flex;
    align-items: center;
  }

  .card {
    width: 220px;
    border: 1px solid #dedede;
    position: absolute;
    right: 15px;
    z-index: 33;
    margin-top: 35px;
    background-color: #fff;
    border-radius: 8px;
  }

  .peopleList, .addPeople {
    font-size: 14px;
    margin-top: 10px;
  }

  .peopleList {
    float: left;
    margin-left: 15px;
  }

  .addPeople {
    float: right;
    margin-right: 15px;
  }

  .peopleContent {
    margin-top: 15px;
    height: 280px;
    overflow-y: auto;
  }

  .imgPeople2, .listName {
    float: left;
  }

  .imgPeople2 {
    margin-top: 6px;
    margin-left: 8px;
  }

  .listName {
    font-size: 12px;
    width: 105px;
    margin-left: 8px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .head {
    font-size: 14px;
    float: right;
    margin-right: 11px;
  }

  .contentA, .contentB {
    height: 40px;
    line-height: 40px;
  }

  .contentB {
    margin-left: 24px;
  }

  .dateInp, .controlChoose {
    width: 340px;
  }

  .allPeople {
    height: 300px;
    overflow: auto;
  }

  .userContent {
    margin-top: 10px;
    padding: 5px 10px;
    background-color: rgba(242, 242, 242, 1);
    border-radius: 3px;
  }

  .peopleName {
    width: 170px;
    font-size: 14px;
    color: #606266;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    float: left;
    cursor: pointer;
  }

  .addIcon {
    font-size: 16px;
    float: right;
    cursor: pointer;
  }

  .clearfix:after {
    content: '';
    display: block;
    clear: both;
  }

  .addCreat {
    .col-set-box {
      .left {
        border-right: 1px solid #eee;
        padding: 0 10px;
        color: #000000;
      }
      h3 {
        font-weight: normal;
        font-size: 14px;
        line-height: 32px;
        border-bottom: 1px solid #eee;
      }
    }
    .right2 {
      width: 100%;
      padding: 0 10px;
    }
    .my-table {
      i {
        cursor: pointer;
      }
    }
    .my-table > > >
    thead {
      color: #222222;
      font-size: 14px;
    }

    th {
      background-color: #f4f9fb;
      padding: 5px 0;
    }
    // hover
    tr:hover > td {
      background-color: #f4f9fb;
    }
  }

  .controlChoose2 {
    width: 180px !important;
  }

  .zxhh {
    background-color: #58ea6a;
  }

  .btnMargin {
    margin-left: 5px;
  }

  .zedit {
    display: flex;
    align-items: center;
  }

  .editTwo {
    margin-left: 15px;
    cursor: pointer;
  }

  .device {
    text-align: center;
    margin-top: 35px;
  }

  .table-container {
    width: 100%;
    height: 100%;
    background-color: #F2F2F2;
  }

  .allContent {
    // width: calc(100vw - 48px);
    height: calc(100vh - 108px);
    background-color: #fff;
    margin: 24px;
    position: relative;
    overflow-y: auto;
  }

  .headLeft {
    font-family: PingFangSC-Medium;
    font-size: 16px;
    color: #262626;
  }

  .clearfix:after {
    content: '';
    display: block;
    clear: both;
  }
</style>
