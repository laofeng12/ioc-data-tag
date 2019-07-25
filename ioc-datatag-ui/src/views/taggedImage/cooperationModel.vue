<template>
  <div class="app-container">
    <div class="actionBar">
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
        <router-link  to="/tree">
          <el-button  class="zxlistBtn" size="small" type="primary">创建标签</el-button>
        </router-link>
        <router-link  to="/lableImage">
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
              <div>
                <div>数据搬运工</div>
                <div>2019/4/19  16:12:13</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="efficiency" label="完成效率"></el-table-column>
          <el-table-column prop="state" label="状态" >
            <template slot-scope="scope">
              <div class="state">
                <div class="stateName" :style="stateColor(scope.row.state)">{{scope.row.state}}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="60px">
            <template slot-scope="props" class="caozuo">
              <el-tooltip class="item" effect="dark" content="打标" placement="top">
                <span class="operationIcona">
                    <i class="el-icon-position iconLogo" @click="marking"></i>
                </span>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapActions, mapState, mapGetters} from 'vuex'
  export default {
    components: {

    },
    name: "cooperationModel",
    data() {
      return {
        input2: '',
        Loading:true,
        saveLoading2:true,
        saveLoading:false,
        value:'',
        options:[
          {
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
          }
        ],
        ztableShowList:[{
          name:'教育',
          overTime:'2019/4/19  16:17:22',
          efficiency:'优',
          people:'数据搬运工',
          time:'2019/4/19  16:17:22',
          introduction:'动画的很多好很多',
          state:'进行中',
        },{
          name:'教育22',
          overTime:'2019/4/19  16:17:22',
          efficiency:'-',
          people:'数据搬运工22',
          time:'2019/4/19  16:17:22',
          introduction:'动画的很多好很多22',
          state:'已完成',
        },{
          name:'教育22',
          overTime:'2019/4/19  16:17:22',
          efficiency:'-',
          people:'数据搬运工22',
          time:'2019/4/19  16:17:22',
          introduction:'动画的很多好很多22',
          state:'已超时',
        }],
        ruleForm:{
          name:'',
          introduction:'',
          date:''
        },
      }
    },
    methods: {
      stateColor(name) {
        if (name == '进行中') {
          return "color:#999999";
        } else if (name == '已完成') {
          return "color:#00CC33";
        } else {
          return "color:#FF3333";
        }
      },
      createModel(){
        this.$router.push('creatModel')
      },
      marking(){
        this.$router.push('marking')
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
    margin-left: 10px;
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
</style>
