<template>
  <div class="app-container">
    <div class="actionBar">
      <el-input
        class="zxinp moduleOne"
        size="small"
        clearable
        placeholder="请输入内容"
        prefix-icon="el-icon-search"
        @keyup.enter.native="modelQuery"
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
      <el-button size="small" type="primary" @click="modelQuery">查询</el-button>
      <el-button class="zxlistBtn" size="small" type="primary" @click="createLabel">创建标签组</el-button>
      <router-link to="/lableImage">
        <el-button size="small" type="primary">我的模型</el-button>
      </router-link>
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
          <el-table-column prop="runState" label="状态">
            <template slot-scope="scope">
              <div class="state">
                <div class="stateName" :style="stateColor(scope.row.runState)">{{scope.row.runState}}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="completeTime" label="完成时间">-</el-table-column>
          <el-table-column prop="people" label="发起人/发起时间">
            <template slot-scope="scope">
              <div>
                <div>{{scope.row.createUserName}}</div>
                <div>{{scope.row.createTime}}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="60px">
            <template slot-scope="props" class="caozuo">
              <el-tooltip class="item" effect="dark" content="打标" placement="top">
                <span class="operationIcona">
                    <i class="el-icon-price-tag iconLogo" @click="marking(props.row.taggingModelId,props.row.modelName)"></i>
                </span>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!--<element-pagination :pageSize="size"  :total="totalnum"  @handleCurrentChange="handleCurrentChange" @sureClick="goPage"></element-pagination>-->
      <element-pagination
        :pageSize="size"
        :currentPage="page+1"
        :total="totalnum"
        @handleSizeChange="handleSizeChange"
        @handleCurrentChange="handleCurrentChange"
      ></element-pagination>
    </div>
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
              v-model.trim="ruleForm.tagsName" style="width: 360px">
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
  </div>
</template>

<script>
  import {getcooperationList,cooperationQuery} from '@/api/cooperation.js'
  import { getDtTagGroupData } from '@/api/tagManage'
  import ElementPagination from '@/components/ElementPagination'
  export default {
    components: {ElementPagination},
    name: "cooperationModel",
    data() {
      return {
        page: 0,
        size: 10,
        totalnum: 0,
        input2: '',
        Loading: true,
        saveLoading2: true,
        saveLoading: false,
        labelcreatDialog: false,
        creatsaveLoading: false,
        value: '',
        options: [
          {
            value: '',
            label: '全部'
          }, {
            value: '0',
            label: '进行中'
          }, {
            value: '1',
            label: '已完成'
          }
        ],
        ztableShowList: [],
        ruleForm: {
          tagsName: '',
          synopsis: ''
        },
        rules: {
          labelName: [
            {required: true, message: '请填写名称', trigger: 'blur'}
          ],
          tagsName: [
            {required: true, message: '请填写名称', trigger: 'blur'}
          ],
        },
      }
    },
    methods: {
      stateColor(name) {
        if (name == '已完成'){
          return "color:#999999";
        } else if (name == '进行中') {
          return "color:#00CC33";
        } else {
          return "color:#FF3333";
        }
      },
      marking(id,name) {
        this.$router.push({
          path:'/marking',
          query:{
            id:id,
            modelName:name
          }
        })
      },
      createLabel() {
        this.labelcreatDialog = true
      },
      closeCreat() {
        this.$refs.ruleForm.resetFields();
        this.labelcreatDialog = false
      },
      cancleCreat() {
        this.$refs.ruleForm.resetFields();
        this.labelcreatDialog = false
      },
      async modelQuery() {
        const params = {
          eq_cooUser:'',
          eq_taggmId:'',
          keyWord:this.input2,
          runState:this.value,
          page:this.page,
          size:this.size
        }
        try{
          const resQuery = await cooperationQuery(params)
          if(resQuery.rows && resQuery.rows.length > 0){
            resQuery.rows.forEach(item => {
              if (item.runState == 0) {
                item.runState = '进行中'
              } else {
                item.runState = '已完成'
              }
            })
            this.ztableShowList = resQuery.rows
            this.totalnum = resQuery.total
          }else{
            this.ztableShowList = []
            this.Loading = false
          }

        }catch (e) {

        }

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
                this.$router.push('/labelcreatTree/' + data.id +'/'+ data.tagsName)
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
      handleCurrentChange(page){
        this.page = page-1
        this.modelQuery()
      },
      handleSizeChange (size) {
        this.size = size
        this.modelQuery()
      },
      goPage(){},
    },
    created() {
      // this.getList()
      this.modelQuery()
    },
    computed: {},
    watch: {},
    mounted() {
    }
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
    margin-left: 10px;
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
</style>
