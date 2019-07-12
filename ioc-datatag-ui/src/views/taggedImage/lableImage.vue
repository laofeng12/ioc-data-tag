<template>
  <div class="app-container">
    <div class="actionBar">
      <div>
        <el-input
          class="zxinp moduleOne"
          size="small"
          placeholder="请输入内容"
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
        <el-button class="zxlistBtn" size="small" type="primary">查询</el-button>
      </div>

      <div>
        <el-button size="small" type="primary" @click="createLabel">创建标签</el-button>
        <el-button size="small" type="primary" @click="createModel">创建模型</el-button>
        <el-button size="small" type="primary" @click="cooperationModel">协作模型</el-button>
        <!--<el-button size="small" type="primary" >下载管理</el-button>-->
      </div>

    </div>
    <div class="tableBar">
      <div class="newTable  daList">
        <el-table ref="multipleTable" :data="ztableShowList" border stripe tooltip-effect="dark"
                  style="width: 100%;text-align: center"
                  :header-cell-style="{background:'#f0f2f5'}">
          <template slot="empty">
            <div v-if="Loading">
              <div v-loading="saveLoading2" ></div>
            </div>
            <div v-else>暂无数据</div>
          </template>
          <el-table-column prop="name" label="名称">
            <template slot-scope="scope">
              <span>教育体系标签</span>
            </template>
          </el-table-column>
          <el-table-column prop="creator" label="创建者"></el-table-column>
          <el-table-column prop="people" label="修改人/修改时间" >
            <template slot-scope="scope">
              <div style="text-align: center">
                <div>数据搬运工</div>
                <div>2019/4/19  16:12:13</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="state" label="状态" >
            <template slot-scope="scope">
              <div class="state">
                <div class="spot"></div>
                <div class="stateName">{{scope.row.state}}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="260px">
            <template slot-scope="props" class="caozuo">
              <el-tooltip class="item" effect="dark" content="调度" placement="top">
                <span class="operationIcona">
                    <i class="el-icon-s-operation iconLogo" @click="handleControl"></i>
                </span>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="编辑" placement="top">
                  <span class="operationIcona">
                    <!--<router-link :to="`dataManage/datasetEdit/${scope.row.dsDataSetId}`">-->
                    <router-link :to="`/modelEdit/123`">
                      <i class="el-icon-edit-outline iconLogo" ></i>
                    </router-link>
                  </span>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="画像" placement="top">
                <span class="operationIcona">
                    <router-link :to="`/ImageDetail/456`">
                      <i class="el-icon-user iconLogo" ></i>
                    </router-link>
                </span>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="导出数据" placement="top">
                <span class="operationIcona">
                    <i class="el-icon-download iconLogo" @click="download"></i>
                </span>
              </el-tooltip>

              <el-tooltip class="item" effect="dark" content="删除" placement="top">
              <span class="operationIcona">
                <i class="el-icon-delete iconLogo" @click="handleDelete"></i>
              </span>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-dialog class="creat" title="模型调度" :visible.sync="controlDialog" width="530px" center :close-on-click-modal="false"
                 @close="closeControl">
        <div class="del-dialog-cnt">
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px">
            <el-form-item label="模型名称:" prop="name" class="nameOne">教育体系标签</el-form-item>
            <el-form-item label="调度开始时间:" prop="date" class="nameOne">
              <el-date-picker
                size="small"
                class="dateInp"
                v-model="value1"
                type="datetime"
                placeholder="选择日期时间">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="调度时间周期:" prop="date" class="nameOne">
              <el-select class="controlChoose" size="small" v-model="value" placeholder="请选择">
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
            <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading" >确定调度</el-button>
          </div>
        </div>
      </el-dialog>
      <!--下载-->
      <el-dialog class="creat" title="下载数据" :visible.sync="downloadDialog" width="530px" center :close-on-click-modal="false"
                 @close="closeDownload">
        <div class="del-dialog-cnt">
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px">
            <el-form-item label="下载数据范围:" prop="date" class="nameOne">
              <el-select class="controlChoose" size="small" v-model="value" placeholder="请选择">
                <el-option
                  v-for="item in options2"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="调度开始时间:" prop="date" class="nameOne">
              <el-date-picker
                size="small"
                class="dateInp"
                v-model="value1"
                type="datetimerange"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-form-item>

          </el-form>
        </div>
        <div slot="footer" class="dialog-footer device">
          <div>
            <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading" >确定下载</el-button>
          </div>
        </div>
      </el-dialog>
      <!--删除-->
      <el-dialog class="creat" title="删除提示" :visible.sync="deleteDialog" width="530px" center :close-on-click-modal="false"
                 @close="closedelete">
        <div class="del-dialog-cnt">
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px">
            <el-form-item >
              您正在删除南城区高考成绩数据，是否确认删除？
            </el-form-item>
          </el-form>
        </div>
        <div slot="footer" class="dialog-footer device">
          <div>
            <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading" >确定删除</el-button>
            <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading" >取消</el-button>
          </div>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
  import {mapActions, mapState, mapGetters} from 'vuex'
  export default {
    components: {

    },
    name: "tagManage",
    data() {
      return {
        input2: '',
        Loading:true,
        saveLoading2:true,
        controlDialog:false,
        downloadDialog:false,
        deleteDialog:false,
        saveLoading:false,
        percentage: 30,
        value1: '',
        textarea: '',
        options: [{
          value: '选项1',
          label: '全部'
        }, {
          value: '选项2',
          label: '运行中'
        }, {
          value: '选项3',
          label: '运行出错'
        },{
          value: '选项4',
          label: '运行结束'
        }],
        options2: [{
          value: '选项1',
          label: '停止运行'
        }, {
          value: '选项2',
          label: '运行一次'
        }],
        value: '',
        ztableShowList:[{
          name:'教育',
          creator:'大雄',
          people:'数据搬运工',
          time:'2019/4/19  16:17:22',
          introduction:'动画的很多好很多',
          state:'运行中',
        },{
          name:'教育22',
          creator:'大雄',
          people:'数据搬运工22',
          time:'2019/4/19  16:17:22',
          introduction:'动画的很多好很多22',
          state:'运行中',
        }],
        ruleForm:{
          name:'',
          introduction:'',
          date:''
        },
        rules: {
          // date: [
          //   {required: true, message: '请选择时间',trigger: 'change'}
          // ]
        },
      }
    },
    methods: {
      customColorMethod(percentage) {
        if (percentage < 30) {
          return '#909399';
        } else if (percentage < 70) {
          return '#e6a23c';
        } else {
          return '#67c23a';
        }
      },
      handleControl(){
        this.controlDialog = true
      },
      closeControl(){
        this.controlDialog = false
        this.$refs.ruleForm.resetFields()
      },
      download(){
        this.downloadDialog = true
      },
      closeDownload(){
        this.downloadDialog = false
        this.$refs.ruleForm.resetFields()
      },
      handleDelete(){
        this.deleteDialog = true
      },
      closedelete(){
        this.deleteDialog = false
      },
      createLabel(){
        this.$router.push('tree')
      },
      createModel(){
        this.$router.push('creatModel')
      },
      cooperationModel(){
        this.$router.push('cooperationModel')
      }
    },
    created() {
    },
    computed: {

    },
    watch: {

    },
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
  .actionBar{
    display: flex;
    justify-content: flex-end;
  }
  .tagSelect{
    margin-left: 10px;
    margin-right: 10px;
  }
  .iconLogo {
    font-size: 18px;
    margin-left: 24px;
    cursor: pointer;
  }
  .controlChoose,.dateInp{
    width: 340px;
  }
.spot{
  width: 10px;
  height: 10px;
  background: #00CC33;
  border-radius: 50%;
  margin-right: 5px;
  margin-top: -1px;
}
  .state{
    display: flex;
    align-items: center;
  }
  .stateName{
    color: #00CC33;
  }
</style>
