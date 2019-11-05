<template>
  <div class="app-container">
    <div class="actionBar">
      <el-input
        class="zxinp moduleOne"
        size="small"
        clearable
        placeholder="请输入模型名称"
        @keyup.enter.native="modelQuery"
        prefix-icon="el-icon-search"
        v-model="input2">
      </el-input>
      <el-select class="tagSelect" size="small" v-model="value" placeholder="请选择">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-button class="zxlistBtn" size="small" type="primary" @click="modelQuery">查询</el-button>
      <el-button size="small" type="primary" @click="createLabel">创建标签组</el-button>
      <el-button size="small" type="primary" @click="createModel">创建模型</el-button>
      <el-button size="small" type="primary" @click="cooperationModel">协作模型</el-button>
      <el-button size="small" type="primary" @click="down">下载管理</el-button>
    </div>
    <div class="tableBar">
      <div class="newTable  daList">
        <el-table ref="multipleTable" :data="ztableShowList" border stripe tooltip-effect="dark"
                  style="width: 100%;text-align: center"
                  :header-cell-style="{background:'#f0f2f5'}">
          <template slot="empty">
            <div v-if="Loading">
              <div v-loading="saveLoading2"></div>
            </div>
            <div v-else>暂无数据</div>
          </template>
          <el-table-column prop="modelName" label="模型名称"></el-table-column>
          <el-table-column prop="createUserName" label="创建者"></el-table-column>
          <el-table-column prop="state" label="状态">
            <template slot-scope="scope">
              <div class="state" v-if="scope.row.runState !== '运行错误'">
                <div class="spot" :style="spotColor(scope.row.runState)"></div>
                <div class="stateName" :style="stateColor(scope.row.runState)">{{scope.row.runState}}</div>
              </div>
              <div class="state errorState" v-else @click="handleError(scope.row.taggingModelId)">
                <div class="spot" :style="spotColor(scope.row.runState)"></div>
                <div class="stateName" :style="stateColor(scope.row.runState)">{{scope.row.runState}}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="runResult" label="调度总量/成功数量"></el-table-column>
          <!--<el-table-column prop="people" label="修改人/修改时间">-->
            <!--<template slot-scope="scope">-->
              <!--<div>-->
                <!--<div>{{scope.row.modifyUserName}}</div>-->
                <!--<div>{{scope.row.modifyTime}}</div>-->
              <!--</div>-->
            <!--</template>-->
          <!--</el-table-column>-->
          <el-table-column prop="modifyUserName" label="修改人"></el-table-column>
          <el-table-column prop="modifyTime" label="修改时间"></el-table-column>
          <el-table-column label="操作" width="260px">
            <template slot-scope="scope" class="caozuo">
              <el-tooltip class="item" effect="dark" content="调度" placement="top">
                <span class="operationIcona">
                    <i class="el-icon-s-operation iconLogo"
                       @click="handleControl(scope.row.modelName,scope.row.taggingModelId)"></i>
                </span>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="编辑" placement="top">
                  <span class="operationIcona">
                    <router-link :to="{ path: `editModel/${scope.row.taggingModelId}`}">
                      <i class="el-icon-edit-outline iconLogo"></i>
                    </router-link>
                  </span>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="画像" placement="top">
                <span class="operationIcona">
                    <!--<router-link :to="`/ImageDetail/${scope.row.taggingModelId}/${scope.row.modelName}`">-->
                  <!--<i class="el-icon-user iconLogo"></i>-->
                  <!--</router-link>-->
                 <i class="el-icon-user iconLogo"
                    @click="lookImage(scope.row,scope.row.taggingModelId,scope.row.modelName)"></i>
                </span>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="导出数据" placement="top">
                <span class="operationIcona">
                    <i class="el-icon-download iconLogo" @click="download(scope.row.taggingModelId,scope.row.runResult)"></i>
                </span>
              </el-tooltip>

              <el-tooltip class="item" effect="dark" content="删除" placement="top">
              <span class="operationIcona">
                <i class="el-icon-delete iconLogo"
                   @click="handleDelete(scope.row.modelName,scope.row.taggingModelId)"></i>
              </span>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
        <!--<element-pagination :pageSize="size" :total="totalnum" @handleCurrentChange="handleCurrentChange"-->
        <!--@sureClick="goPage"></element-pagination>-->
        <element-pagination
          :pageSize="size"
          :currentPage="page+1"
          :total="totalnum"
          @handleSizeChange="handleSizeChange"
          @handleCurrentChange="handleCurrentChange"
        ></element-pagination>
      </div>
      <el-dialog class="creat" title="模型调度" :visible.sync="controlDialog" width="530px" center
                 :close-on-click-modal="false"
                 @close="closeControl">
        <div class="del-dialog-cnt">
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px">
            <el-form-item label="模型名称:" prop="name" class="nameOne">{{dispatchName}}</el-form-item>
            <el-form-item label="调度开始时间:" prop="date" class="nameOne">
              <el-date-picker
                size="small"
                class="dateInp"
                v-model="ruleForm.date"
                value-format="yyyy-MM-dd HH:mm:ss"
                format="yyyy-MM-dd HH:mm:ss"
                type="datetime"
                :picker-options="pickerOptions"
                placeholder="选择日期时间">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="调度时间周期:" prop="region" class="nameOne">
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
            <el-button size="small" type="primary" class="queryBtn" :loading="dispatchLoading" @click="sureDispatch">
              确定调度
            </el-button>
          </div>
        </div>
      </el-dialog>
      <!--下载-->
      <el-dialog class="creat" title="导出数据" :visible.sync="downloadDialog" width="530px" center
                 :close-on-click-modal="false"
                 @close="closeDownload">
        <div class="del-dialog-cnt">
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px">
            <el-form-item label="导出数据范围:" prop="download" class="nameOne">
              <el-select class="controlChoose" size="small" v-model="exportValue" placeholder="请选择">
                <el-option
                  v-for="item in options3"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item v-show="exportValue === '1'" label="导出数据数量:" prop="exportNum" class="controlChoose">
              <el-input
                class="controlChoose"
                size="small"
                placeholder="请输入需要导出的数据条目数量"
                v-model="exportNum">
              </el-input>
            </el-form-item>

          </el-form>
        </div>
        <div slot="footer" class="dialog-footer device">
          <div>
            <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading" @click="handleExport">确定导出</el-button>
            <el-button size="small" type="primary" class="queryBtn" @click="cancleExport">取消</el-button>
          </div>
        </div>
      </el-dialog>
      <el-dialog class="creat" title="删除提示" :visible.sync="deleteDialog" width="530px" center
                 :close-on-click-modal="false"
                 @close="closedelete">
        <div class="del-dialog-cnt">
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" style="text-align: center">
            <el-form-item>您正在删除{{this.deleteName}}，是否确认删除？</el-form-item>
          </el-form>
        </div>
        <div slot="footer" class="dialog-footer device">
          <div>
            <el-button size="small" type="primary" class="queryBtn" :loading="deleteLoading" @click="sureDelete">确定删除
            </el-button>
            <el-button size="small" type="primary" class="queryBtn" @click="cancelDelete">取消</el-button>
          </div>
        </div>
      </el-dialog>
      <el-dialog class="creat" title="创建标签组" :visible.sync="labelcreatDialog" width="530px" center
                 :close-on-click-modal="false"
                 @close="closeCreat">
        <div class="del-dialog-cnt">
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
            <el-form-item label="标签组名称:" prop="tagsName" class="nameOne">
              <el-input
                class="zxinp moduleOne"
                size="small"
                maxlength="25"
                show-word-limit
                placeholder="请输入标签组名称"
                v-model="ruleForm.tagsName" style="width: 360px">
              </el-input>
            </el-form-item>
            <el-form-item label="标签组简介:" prop="synopsis" class="nameOne">
              <el-input
                class="area"
                type="textarea"
                maxlength="100"
                show-word-limit
                :autosize="{ minRows: 2, maxRows: 4}"
                placeholder="请输入标签组简介"
                v-model="ruleForm.synopsis">
              </el-input>
            </el-form-item>
          </el-form>
        </div>
        <div slot="footer" class="dialog-footer device">
          <div>
            <el-button size="small" plain class="btn-group" @click="cancleCreat">取消</el-button>
            <el-button size="small" type="primary" class="queryBtn" :loading="creatsaveLoading" @click="sureCreat">
              确认并编辑
            </el-button>
          </div>
        </div>
      </el-dialog>
      <!--查看错误日志-->
      <el-dialog class="creat" title="错误日志" :visible.sync="errorDialog" width="600px" center
                 :close-on-click-modal="false">
        <div class="del-dialog-cnt">
          <div class="errorContent">{{errorContent}}</div>
        </div>
        <div slot="footer" class="dialog-footer device">
          <div>
            <el-button size="small" type="primary" class="queryBtn" @click="cancelError">关闭</el-button>
          </div>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
  import {getmodelList, getDispatch, getDelete, getDispatchdetail,startDown,getError} from '@/api/lableImage.js'
  import {getDtTagGroupData} from '@/api/tagManage'
  import ElementPagination from '@/components/ElementPagination'
  import DMSocket from '@/utils/DMSocket'
  import createSocket from '@/utils/web'

  export default {
    components: {ElementPagination},
    name: "tagManage",
    data() {
      return {
        errorContent:'',
        lockReconnect:false,
        webUserId: '',
        downloadId:'',
        page: 0,
        size: 10,
        totalnum: 0,
        like_modelName: '',
        eq_runState: '',
        dispatchName: '',
        dispatchId: '',
        deleteName: '',
        deleteId: '',
        input2: '',
        errorDialog:false,
        Loading: true,
        saveLoading2: true,
        controlDialog: false,
        downloadDialog: false,
        deleteDialog: false,
        dispatchLoading: false,
        deleteLoading: false,
        saveLoading: false,
        labelcreatDialog: false,
        creatsaveLoading: false,
        percentage: 30,
        value1: '',
        textarea: '',
        WebSocket: null,
        options: [{
          value: '',
          label: '全部'
        }, {
          value: '0',
          label: '未开始'
        }, {
          value: '1',
          label: '等待运行'
        }, {
          value: '2',
          label: '运行中'
        }, {
          value: '3',
          label: '运行成功'
        }, {
          value: '4',
          label: '运行错误'
        }, {
          value: '-1',
          label: '运行结束'
        }],
        options2: [{
          value: '0',
          label: '停止运行'
        }, {
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
        options3: [{
          value: '0',
          label: '导出全部数据'
        }, {
          value: '1',
          label: '导出部分数据'
        }],
        exportValue: '0',
        exportNum: '',
        value: '',
        ztableShowList: [],
        timer: '',
        ruleForm: {
          region: null,
          date: null,
          tagsName: '',
          synopsis: ''
        },
        rules: {
          date: [
            {required: true, message: '请选择时间', trigger: 'blur'}
          ],
          region: [
            {required: true, message: '请选择周期', trigger: 'change'}
          ],
          labelName: [
            {required: true, message: '请填写名称', trigger: 'blur'}
          ],
          tagsName: [
            {required: true, message: '请填写名称', trigger: 'blur'}
          ]
        },
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() <= Date.now() - 8.64e7
          }
        },
      }
    },
    methods: {
      stateColor(runState) {
        if (runState == '未开始' || runState == '等待运行' || runState == '运行结束') {
          return "color:#909399;";
        } else if (runState == '运行中' || runState == '运行成功') {
          return "color:#67c23a";
        } else {
          return "color:#ee0320";
        }
      },
      spotColor(runState) {
        if (runState == '未开始' || runState == '等待运行' || runState == '运行结束') {
          return "backgroundColor:#909399";
        } else if (runState == '运行中' || runState == '运行成功') {
          return "backgroundColor:#67c23a";
        } else {
          return "backgroundColor:#ee0320";
        }
      },
      async handleControl(name, id) {
        this.controlDialog = true
        this.dispatchName = name
        this.dispatchId = id
        try {
          const resOk = await getDispatchdetail({
            taggingModelId: id
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
      closeControl() {
        this.controlDialog = false
        this.$refs.ruleForm.resetFields()
      },
      download(id,num) {
        if(num === '0/0'){
          this.$message.error('调度成功数量为0，无法导出！');
          return
        }else {
          this.downloadDialog = true
          this.downloadId = id
        }
      },
      closeDownload() {
        this.downloadDialog = false
        this.exportNum = ''
      },
      cancleExport(){
        this.downloadDialog = false
        this.exportNum = ''
      },
      handleDelete(name, id) {
        this.deleteDialog = true
        this.deleteName = name
        this.deleteId = id
      },
      closedelete() {
        this.deleteDialog = false
      },
      cancelDelete() {
        this.deleteDialog = false
      },
      createLabel() {
        this.labelcreatDialog = true
      },
      createModel() {
        this.$router.push('creatModel')
      },
      cooperationModel() {
        this.$router.push('cooperationModel')
      },
      down() {
        this.$router.push('download')
      },
      async handleExport(){
        this.saveLoading = true
          if((this.exportValue == 1 && this.exportNum != '') || this.exportValue == 0){
            const params = {
              number:this.exportNum,
              taggingModelId:this.downloadId
            }
            try{
              const res = await startDown(params)
              this.$message({
                message: res.message,
                type: 'success'
              });
              this.saveLoading = false
              this.downloadDialog = false
              this.$router.push('download')
            }catch (e) {
              console.log(e);
            }
          }else{
            this.$message.error('请输入需要导出的数据条目数量');
            this.saveLoading = false
          }
      },
      // 定时器
      setTimer() {
        setInterval(() => {
          this.datamodelList()
        }, 5000)
      },
      async datamodelList() {
        const params = {
          eq_runState: this.value,
          ge_startTime: '',
          le_startTime: '',
          like_modelName: this.input2,
          page: this.page,
          size: this.size
        }
        try {
          const modelList = await getmodelList(params)
          if (modelList.rows && modelList.rows.length > 0) {
            this.ztableShowList = modelList.rows
            this.totalnum = modelList.total
            modelList.rows.map(item => {
              if (item.runState == 0) {
                item.runState = '未开始'
              }
              if (item.runState == 1) {
                item.runState = '等待运行'
                this.getsocket()
              }
              if (item.runState == 2) {
                item.runState = '运行中'
              }
              if (item.runState == 3) {
                item.runState = '运行成功'
              }
              if (item.runState == 4) {
                item.runState = '运行错误'
              }
              if (item.runState == -1) {
                item.runState = '运行结束'
              }
            })
            // this.getsocket()
          } else {
            this.ztableShowList = []
            this.Loading = false
            this.totalnum = 0
          }
        } catch (e) {
          console.log(e);
        }
      },
      handleCurrentChange(page) {
        this.page = page - 1
        this.datamodelList()
      },
      handleSizeChange(size) {
        this.size = size
        this.datamodelList()
      },
      goPage() {
      },
      modelQuery() {
        this.page = 0
        this.datamodelList()
      },
      sureDispatch() {
        const param = {
          "cycleEnum": this.ruleForm.region,
          "id": this.dispatchId,
          "startTime": this.ruleForm.date
        }
        this.dispatchLoading = true
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            try {
              const remindTime = this.ruleForm.date
              const str = remindTime.toString()
              const str2 = str.replace('/-/g', '/')
              const oldTime = new Date(str2).getTime()
              if (oldTime <= new Date().getTime()) {
                this.$message.error('运行开始时间不能小于当前时间!')
                this.dispatchLoading = false
                return
              }
              const res = await getDispatch(param)
              this.$message({
                message: res.message,
                type: 'success'
              });
              this.dispatchLoading = false
              this.controlDialog = false
              this.datamodelList()

            } catch (e) {
              console.log(e);
              this.dispatchLoading = false
            }
          } else {
            this.dispatchLoading = false
          }
        });

      },
      async sureDelete() {
        const res = await getDelete({
          id: this.deleteId
        })
        this.$message({
          message: res.message,
          type: 'success'
        });
        this.deleteDialog = false
        this.datamodelList()
      },
      closeCreat() {
        this.$refs.ruleForm.resetFields();
        this.labelcreatDialog = false
      },
      cancleCreat() {
        this.$refs.ruleForm.resetFields();
        this.labelcreatDialog = false
      },
      sureCreat() {
        try {
          this.creatsaveLoading = true
          this.$refs.ruleForm.validate(async (valid) => {
            if (valid) {
              try {
                const data = await getDtTagGroupData({
                  id: '',
                  isNew: true,
                  isShare: '',
                  synopsis: this.ruleForm.synopsis,
                  tagsName: this.ruleForm.tagsName
                })
                this.creatsaveLoading = false
                this.$router.push('/tree/' + data.id + '/' + data.tagsName)
              } catch (e) {
                this.creatsaveLoading = false
                console.log(e);
              }
            } else {
              this.creatsaveLoading = false
            }
          });
        } catch (e) {
          this.creatsaveLoading = false
          console.log(e);
        }
      },
      lookImage(row, id, name) {
        if (row.runState != '运行成功') {
          this.$message.error('请先进行数据调度成功之后再查看画像！');
        } else {
          this.$router.push({
            path: '/ImageDetail',
            query: {
              detailId: id,
              imageName: name
            }
          })
        }
      },
      // zhujianxiong
      getsocket() {
        const url = '/datatag/websocket/server/' + this.webUserId
        try {
          //方法调用
          const handler = (evt, ws) => {
            //evt 是 websockett数据
            var obj = JSON.parse(evt.data)
            if(obj.datas != "-1"){
              this.datamodelList()
              wssCenter.close();  //断开连接
            }
          }
          const wssCenter = createSocket(url, handler,this.webUserId)
        } catch (e) {
          console.log(e)
          this.reconnect(url);
        }
      },
      // 查看错误日记
      async handleError(taggingModelId){
        this.errorDialog = true
        const res = await getError(taggingModelId)
        this.errorContent = res
      },
      cancelError(){
        this.errorDialog = false
      }
    },
    created() {
      this.webUserId = this.$store.state.user.userInfo.userId
      this.datamodelList()
    },
    computed: {},
    watch: {},
    mounted() {}
  }
</script>

<style scoped>
  .zxinp {
    width: 240px;
  }

  .zxlistBtn {
    margin-right: 10px;
  }

  .actionBar {
    display: flex;
    justify-content: flex-end;
  }

  .tagSelect {
    margin-left: 10px;
    margin-right: 10px;
  }

  .iconLogo {
    font-size: 18px;
    margin-left: 24px;
    cursor: pointer;
  }

  .controlChoose, .dateInp {
    width: 340px;
  }

  .spot {
    width: 10px;
    height: 10px;
    background: #00CC33;
    border-radius: 50%;
    margin-right: 5px;
    margin-top: -1px;
  }

  .state {
    display: flex;
    align-items: center;
  }
  .errorState{
    cursor: pointer;
  }
  .stateName {
    color: #00CC33;
  }
  .errorContent{
    width: 100%;
    height: 300px;
    overflow-y: auto;
    padding: 12px;
    background-color: rgba(51, 51, 51, 1);
    color: #ffffff;
    line-height: 25px;
  }
  .area {
    width: 360px;
  }
</style>
