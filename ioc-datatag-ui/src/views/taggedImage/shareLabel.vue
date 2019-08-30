<template>
  <div class="app-container">
    <div class="actionBar">
        <el-input
          class="zxinp tagSelect"
          size="small"
          clearable
          placeholder="请输入内容"
          prefix-icon="el-icon-search"
          v-model="input2">
        </el-input>
        <el-button class="zxlistBtn" size="small" type="primary" @click="shareQuery">查询</el-button>
        <el-button size="small" type="primary" @click="createLabel">我的标签组</el-button>
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
          <el-table-column prop="tagsName" label="标签组名称"></el-table-column>
          <el-table-column prop="source" label="使用热度" >
            <template slot-scope="scope">
              <div class="gress">
                <div class="gressPercentage"><el-progress :percentage="scope.row.percentage" :show-text="false" :color="customColorMethod"></el-progress></div>
                <div>{{scope.row.popularity}}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="level1" label="单位" ></el-table-column>
          <el-table-column prop="synopsis" label="标签组简介"></el-table-column>
          <el-table-column prop="people" label="共享人/更新时间" >
            <template slot-scope="scope">
              <div>
                <div>{{scope.row.shareUserName}}</div>
                <div>{{scope.row.modifyTime}}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="125px">
            <template slot-scope="props" class="caozuo">
              <el-tooltip class="item" effect="dark" content="查看" placement="top">
              <span class="operationIcona">
                <router-link :to="`lookTree/${props.row.tagsId}/${props.row.tagsName}`">
                  <i class="el-icon-view iconLogo"></i>
                </router-link>
              </span>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="选用标签组" placement="top">
              <span class="operationIcona">
                <i class="el-icon-position iconLogo" @click="Selection(props.row.tagsId,props.row.tagsName)"></i>
              </span>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
        <element-pagination :pageSize="size"  :total="totalnum"  @handleCurrentChange="handleCurrentChange" @sureClick="goPage"></element-pagination>
      </div>
      <el-dialog class="creat" title="选用标签组" :visible.sync="labelDialog" width="530px" center :close-on-click-modal="false"
                 @close="closelabelDialog">
        <div class="del-dialog-cnt">
          <el-form label-width="80px">
            <el-form-item >您正在选用{{this.selectiontagsName}}，是否确认选用？</el-form-item>
          </el-form>
        </div>
        <div slot="footer" class="dialog-footer device">
          <div>
            <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading" @click="sureOk">确定选用</el-button>
            <el-button size="small"  class="queryBtn" @click="cancelNo">取消</el-button>
          </div>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
  import {shareSearch,changeSelection} from '@/api/shareLabel.js'
  import ElementPagination from '@/components/ElementPagination'
  export default {
    components: {ElementPagination},
    name: "tagManage",
    data() {
      return {
        page:0,
        size:10,
        totalnum:0,
        input2: '',
        Loading:true,
        saveLoading2:true,
        shareDialog:false,
        saveLoading:false,
        labelDialog:false,
        selectionId:'',
        selectiontagsName:'',
        percentage: 0,
        ztableShowList:[],
      }
    },
    methods: {
      customColorMethod(percentage) {
        if (percentage < 25) {
          return '#909399';
        } else if (percentage < 75) {
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
        this.$router.push('tagManage')
      },
      async handleSearch(){
        try{
          const search = await shareSearch({
            page:this.page,
            searchKey:this.input2,
            size:this.size
          })
          this.ztableShowList = search.content?search.content:[]
          this.totalnum = search.totalElements
          if(search.content&&search.content.length > 0){
            search.content.forEach(item => {
              if(item.popularityLevel == 0){
                item.popularityLevel = 0
              }else if(item.popularityLevel == 1){
                item.popularityLevel = 25
              }else if(item.popularityLevel == 2){
                item.popularityLevel = 50
              }else if(item.popularityLevel == 3){
                item.popularityLevel = 75
              }else{
                item.popularityLevel = 100
              }
            })
          }else {
            this.Loading = false
          }
        }catch (e) {
          console.log(e);
          this.Loading = false
        }
      },
      handleCurrentChange(page){
        this.page = page-1
        this.handleSearch()
      },
      shareQuery(){
        this.page = 0
        this.handleSearch()
      },
      goPage(){},
      Selection(id,name){
        this.labelDialog = true
        this.selectionId = id
        this.selectiontagsName = name
      },
      closelabelDialog(){
        this.labelDialog = false
      },
      async sureOk(){
        this.saveLoading = true
        try{
          const res = await changeSelection({
            id: this.selectionId
          })
          this.$message({
            message: res.message,
            type: 'success'
          });
          this.saveLoading = false
          this.labelDialog = false
          this.$router.push('/tagManage')
        }catch (e) {
          this.saveLoading = false
          console.log(e);
        }
      },
      cancelNo(){
        this.labelDialog = false
      }
    },
    created() {
      this.handleSearch()
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
