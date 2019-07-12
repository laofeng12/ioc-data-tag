<template>
  <div class="table-container">
    <div class="header">
      <div class="left">
        <router-link class="return" to="/cooperationModel">
          <i class="el-icon-arrow-left"></i>
        </router-link>
        <div class="name">
          <div class="img"></div>
          <div class="text">南城区高考成绩数据</div>
        </div>
      </div>
      <div class="right rightBtn">
        <el-button class="button2" type="primary" size="small">保存打标</el-button>
      </div>
    </div>
    <div class="content">
      <div class="components">
        <div class="contentTitle">南城区高考成绩数据</div>
        <div class="newTable  daList">
          <el-table ref="multipleTable" :data="tableData" border stripe tooltip-effect="dark"
                    style="width: 100%;text-align: center"
                    :header-cell-style="{background:'#f0f2f5'}">
            <el-table-column label="Date" prop="date"></el-table-column>
            <el-table-column label="Date" prop="date"></el-table-column>
            <el-table-column label="Date" prop="date"></el-table-column>
            <el-table-column prop="name" width="200px" class="clearfix">
              <template slot="header" slot-scope="scope">
                <span>名字</span>
                <i class="el-icon-position  iconLogo" @click="dataMaking"></i>
              </template>
            </el-table-column>
            <el-table-column prop="name" width="200px" class="clearfix">
              <template slot="header" slot-scope="scope">
                <span>名字2</span>
                <i class="el-icon-position  iconLogo"></i>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <el-dialog class="creat" title="数据打标" :visible.sync="makingDialog" width="650px" center
               :close-on-click-modal="false"
               @close="closeControl">
      <div class="del-dialog-cnt">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px">
          <el-form-item label="选择标签组:" prop="value" class="nameOne">
            <el-select v-model="value" size="small" filterable placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
            <span class="makingEdit">编辑标签组</span>
          </el-form-item>
          <el-form-item label="选择标签层:" prop="value" class="nameOne">
            <div class="allTree">
              <div class="sel">
                <el-input
                  size="small"
                  placeholder="请输入内容"
                  v-model="filterText">
                  <i slot="suffix" class="el-input__icon el-icon-arrow-down" @click="test"></i>
                </el-input>
                <div class="treeBoder" v-show="show">
                  <el-tree
                    class="filter-tree"
                    show-checkbox
                    :data="data"
                    :props="defaultProps"
                    default-expand-all
                    :filter-node-method="filterNode"
                    ref="tree">
                  </el-tree>
                </div>
              </div>
            </div>
          </el-form-item>
          <el-form-item label="打标设置:" prop="value" class="nameOne">
            <el-select v-model="value" size="small" filterable placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
            <el-button class="dataBtn" size="small">自动打标</el-button>
            <el-button class="dataBtn" size="small">人工打标</el-button>
          </el-form-item>

        </el-form>
      </div>
      <div class="lookContent">
        <div class="contentTop">
          <div class="topOne">AND</div>
          <div class="topOne">OR</div>
          <div class="topOne">LIKE</div>
          <div class="topOne">NOT</div>
          <div class="topOne">(</div>
          <div class="topOne">)</div>
          <div class="topOne"><</div>
          <div class="topOne">></div>
          <div class="topOne">=</div>
          <div class="topOne">≠</div>
          <div class="topOne">≤</div>
          <div class="topOne">≥</div>
          <div class="topOne">+</div>
        </div>
        <div class="makingContent">
          <div class="card">
            <el-card class="box-card">
            <i class="el-icon-circle-close deleteContent"></i>
            <el-input style="width:100px" size="small" v-model="input2" placeholder="请输入内容"></el-input>
            <span class="chinese">CHINESE</span>
            <el-input style="width:100px" size="small" v-model="input" placeholder="请输入内容"></el-input>
            <span class="chinese2">AND</span>
            <el-input style="width:100px" size="small" v-model="input" placeholder="请输入内容"></el-input>
            </el-card>
          </div>
          <div class="card">
            <el-card class="box-card">
              <i class="el-icon-circle-close deleteContent"></i>
              <el-input style="width:100px" size="small" v-model="input2" placeholder="请输入内容"></el-input>
              <span class="chinese">CHINESE</span>
              <el-input style="width:100px" size="small" v-model="input" placeholder="请输入内容"></el-input>
              <span class="chinese2">AND</span>
              <el-input style="width:100px" size="small" v-model="input" placeholder="请输入内容"></el-input>
            </el-card>
          </div>
          <div class="card ">
            <el-card class="box-card">
              <div class="card2">
                <div><i class="el-icon-circle-close deleteContent"></i></div>
                <div><el-input style="width:100px" size="small" v-model="input2" placeholder="请输入内容"></el-input></div>
                <div class="chinese">CHINESE</div>
                <div class="chooseNum" @click="test2">
                  <span>已选</span>
                  <span class="num">17</span>
                  <span>条</span>
                </div>
              </div>
                <div class="treeBoder2" v-show="show2">
                   <el-input
                      style="width: 195px;"
                      size="small"
                      placeholder="请输入内容"
                      v-model="filterText">
                  <i slot="suffix" class="el-input__icon el-icon-search" @click="search"></i>
                  </el-input>
                  <div class="checkIt">
                    <div class="checkOne"><el-checkbox v-model="check">1</el-checkbox></div>
                    <div class="checkOne"><el-checkbox v-model="check">2</el-checkbox></div>
                    <div class="checkOne"><el-checkbox v-model="check">3</el-checkbox></div>
                    <div class="checkOne"><el-checkbox v-model="check">4</el-checkbox></div>
                    <div class="checkOne"><el-checkbox v-model="check">5</el-checkbox></div>
                    <div class="checkOne"><el-checkbox v-model="check">6</el-checkbox></div>
                    <div class="checkOne"><el-checkbox v-model="check">7</el-checkbox></div>
                    <div class="checkOne"><el-checkbox v-model="check">8</el-checkbox></div>
                  </div>
                </div>
            </el-card>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer device">
        <div>
          <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading">确定打标</el-button>
        </div>
      </div>
    </el-dialog>

  </div>
</template>

<script>

  export default {
    name: 'marking',
    data() {
      return {
        show:false,
        show2:false,
        saveLoading:false,
        Loading: true,
        saveLoading2: true,
        makingDialog: false,
        tableBoxWidth: 800,
        tableBoxHeight: 800,
        tableWidth: 800,
        tableHeight: 800,
        ruleForm:{
          labelOne:'',
          filterText:''
        },
        input2:'不及格',
        input:'<30',
        check:'',
        changevalue:'',
        filterText:'',
        options: [{
          value: '选项1',
          label: '黄金糕'
        }, {
          value: '选项2',
          label: '双皮奶'
        }, {
          value: '选项3',
          label: '蚵仔煎'
        }, {
          value: '选项4',
          label: '龙须面'
        }, {
          value: '选项5',
          label: '北京烤鸭'
        }],
        value: '',
        tableData: [{
          date: '2016-05-02',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-04',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1517 弄'
        }, {
          date: '2016-05-01',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1519 弄'
        }, {
          date: '2016-05-03',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1516 弄'
        }],
        theadData: [],
        data: [{
          id: 1,
          label: '一级 1',
          children: [{
            id: 4,
            label: '二级 1-1',
            children: [{
              id: 9,
              label: '三级 1-1-1'
            }, {
              id: 10,
              label: '三级 1-1-2'
            }]
          }]
        }, {
          id: 2,
          label: '一级 2',
          children: [{
            id: 5,
            label: '二级 2-1'
          }, {
            id: 6,
            label: '二级 2-2'
          }]
        }, {
          id: 3,
          label: '一级 3',
          children: [{
            id: 7,
            label: '二级 3-1'
          }, {
            id: 8,
            label: '二级 3-2'
          }]
        }],
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        rules: {
          // date: [
          //   {required: true, message: '请选择时间',trigger: 'change'}
          // ]
        },
      }
    },
    watch: {
      theadData: function (val) {
        this.tableWidth = val.length * 149
      },
      filterText(val) {
        this.$refs.tree.filter(val);
      }
    },
    created() {
      this.datasetId = this.$route.params.id
    },
    mounted() {

    },
    methods: {
      closeControl() {
        this.makingDialog = false
        this.$refs.ruleForm.resetFields()
      },
      dataMaking(){
        this.makingDialog = true
      },
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      test(){
        this.show = !this.show
      },
      test2(){
        this.show2 = !this.show2
      },
      search(){
        console.log("查询");
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
    margin-left: 20px;
  }

  .daList {
    margin-top: -20px;
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
.makingEdit{
  font-size: 14px;
  margin-left: 12px;
  color:#00CCCC;
  cursor: pointer;
}

  /**/
  .sel{
    width: 215px;
    height: 32px;
  }
  .selIcon{
    height: 32px;
    line-height: 32px;
    margin-left: 9px;
  }
  .zxIcon{
    margin-left: 8px;
    line-height: 37px;
  }
  .treeBoder,.treeBoder2{
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    padding: 15px 10px;
    background-color: #fff;
    position: relative;
    z-index: 55;
    overflow: auto;
  }
  .treeBoder2{
    width: 218px;
    position: absolute;
    right: 175px;
    margin-top: 10px;
  }
  .dataBtn{
    margin-left: 10px;
    background-color: #00CCCC;
    border-radius: 4px;
    color: #fff;
    font-size: 12px;
  }
  .lookContent{
    border:1px solid #dcdfe6;
    padding: 0px 10px;
    border-radius: 4px;
  }
  .contentTop{
    display: flex;
    border-bottom: 1px solid #dcdfe6;
  }
  .topOne{
    width: 40px;
    height: 32px;
    text-align: center;
    line-height: 32px;
    font-size: 12px;
    color:#00CCCC;
    cursor: pointer;
  }
  .deleteContent{
    font-size: 18px;
    padding: 0px 10px 0px 0px;
    color:#00CCCC;
  }
  .chinese,.chinese2{
    font-size: 14px;
    padding: 0px 10px;
  }
  .chinese2{
    color:#00CCCC;
  }
  .makingContent{
    margin-top: 20px;
  }
  .card{
    margin-bottom: 10px;
  }
  .chooseNum{
    width: 200px;
    height: 32px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 14px;
  }
  .num{
    color:#00CCCC;
    padding: 0px 3px;
  }
  .card2{
    display: flex;
    align-items: center;
  }
  .checkIt{
    padding: 10px;
  }
  .checkOne{
    padding-top: 5px;
  }
  .clearfix:after {
    content: '';
    display: block;
    clear: both;
  }
</style>
