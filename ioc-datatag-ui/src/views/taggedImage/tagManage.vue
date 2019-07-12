<template>
    <!--<div class="app-container">标签管理</div>-->
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
        <el-button size="small" type="primary" @click="shareLabel">共享标签组</el-button>
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
          <el-table-column prop="people" label="修改人/修改时间" >
            <template slot-scope="scope">
              <div style="text-align: center">
                <div>数据搬运工</div>
                <div>2019/4/19  16:12:13</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="introduction" label="标签组简介">这是教育体系标签简介</el-table-column>
          <el-table-column prop="share" label="共享状态" >已共享</el-table-column>
          <el-table-column prop="source" label="使用热度" >
            <template slot-scope="scope">
              <div class="gress">
                <div class="gressPercentage"><el-progress :percentage="percentage" :show-text="false" :color="customColorMethod"></el-progress></div>
                <div>32211</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180px">
            <template slot-scope="props" class="caozuo">
              <el-tooltip class="item" effect="dark" content="共享" placement="top">
                <span class="operationIcona">
                    <i class="el-icon-share iconLogo" @click="handleShare"></i>
                </span>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="编辑" placement="top">
              <span class="operationIcona">
                  <i class="el-icon-edit-outline iconLogo" ></i>
              </span>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="删除" placement="top">
              <span class="operationIcona">
                <i class="el-icon-delete iconLogo" ></i>
              </span>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-dialog class="creat" title="共享标签组" :visible.sync="shareDialog" width="530px" center :close-on-click-modal="false"
                 @close="closeShare">
        <div class="del-dialog-cnt">
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
            <el-form-item label="标签组名称:" prop="name" class="nameOne">教育体系标签</el-form-item>
            <el-form-item label="标签组简介:" prop="introduction" class="nameOne">
              <el-input
                class="area"
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 4}"
                placeholder="请输入内容"
                v-model="textarea">
              </el-input>
            </el-form-item>
            <el-form-item label="截止日期:" prop="date" class="nameOne">
              <el-date-picker
                class="dateInp"
                v-model="value1"
                type="datetime"
                placeholder="选择日期时间">
              </el-date-picker>
            </el-form-item>
          </el-form>
        </div>
        <div slot="footer" class="dialog-footer device">
          <div>
            <el-button size="small" plain class="btn-group"  @click="closeShare2">关闭</el-button>
            <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading" @click="cancelShare">取消共享</el-button>
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
        shareDialog:false,
        saveLoading:false,
        percentage: 30,
        value1: '',
        textarea: '',
        options: [{
          value: '选项1',
          label: '全部'
        }, {
          value: '选项2',
          label: '未共享'
        }, {
          value: '选项3',
          label: '已共享'
        }],
        value: '',
        ztableShowList:[{
          name:'教育',
          people:'数据搬运工',
          time:'2019/4/19  16:17:22',
          introduction:'动画的很多好很多',
          share:'已共享',
        },{
          name:'教育22',
          people:'数据搬运工22',
          time:'2019/4/19  16:17:22',
          introduction:'动画的很多好很多22',
          share:'已共享',
        }],
        ruleForm:{
          name:'',
          introduction:'',
          date:''
        },
        rules: {
          date: [
            {required: true, message: '请选择时间',trigger: 'change'}
          ]
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
      handleShare(){
        this.shareDialog = true
      },
      closeShare(){
        this.shareDialog = false
        this.$refs.ruleForm.resetFields()
      },
      closeShare2(){
        this.shareDialog = false
        this.$refs.ruleForm.resetFields()
      },
      cancelShare(){
        this.shareDialog = false
        this.$refs.ruleForm.resetFields()
      },
      createLabel(){
        this.$router.push('tree')
      },
      shareLabel(){
        this.$router.push('shareLabel')
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
  .gress{
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-around;
  }
  .gressPercentage{
    width: 60%;
  }
  .iconLogo {
    font-size: 18px;
    margin-left: 24px;
    cursor: pointer;
  }
  .area,.dateInp{
    width: 360px;
  }
</style>
