<template>
  <div class=''>
    <div class="result">
      <div class="queryTitle">画像查询</div>
      <div class="queryIt">
        <el-input
          class="zxinp moduleOne"
          size="small"
          placeholder="请输入内容"
          prefix-icon="el-icon-search"
          v-model="input2">
        </el-input>
        <el-button class="zxlistBtn" size="small" type="primary">查询</el-button>
      </div>
    </div>

    <div class="queryCard" v-show="image">
      <el-card class="box-card">
        <div class="numId">
          <span>ID：</span>
          <span>9527</span>
        </div>
        <div class="numidContent">南城区公民信息数据</div>
        <div class="peopleContent">
          <el-tag class="people">标签一</el-tag>
          <el-tag class="people">标签一标签一</el-tag>
          <el-tag class="people">标签一</el-tag>
          <el-tag class="people">标签一标签一</el-tag>
          <el-tag class="people">....</el-tag>
        </div>
        <div>
          <el-button type="text" >查看详情</el-button>
        </div>
      </el-card>

      <el-card class="box-card card2">
        <div class="numId">
          <span>ID：</span>
          <span>9527</span>
        </div>
        <div class="numidContent">南城区公民信息数据</div>
        <div class="peopleContent">
          <el-tag class="people">标签一</el-tag>
          <el-tag class="people">标签一标签一</el-tag>
          <el-tag class="people">标签一</el-tag>
          <el-tag class="people">标签一标签一</el-tag>
          <el-tag class="people">....</el-tag>
        </div>
        <div>
          <el-button type="text">查看详情</el-button>
        </div>
      </el-card>
    </div>
    <detailImage v-show="detail"></detailImage>
  </div>
</template>

<script>
  import {mapActions, mapState, mapGetters} from 'vuex'
  import detailImage from '@/components/image/detailImage'

  export default {
    components: {
      detailImage
    },
    name: "queryImage",
    data() {
      return {
        input2: '',
        image: true,
        detail:false
      }
    },
    methods: {

    },
    created() {
    },
    computed: {},
    watch: {},
    mounted() {
    }
  }
</script>

<style scoped>
  .zxinp {
    width: 500px;
  }

  .zxlistBtn {
    margin-left: 10px;
  }

  .queryTitle {
    font-size: 18px;
    font-weight: 700;
    color: #FF0000;
    margin-right: 20px;
  }

  .result {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }

  .queryCard {
    margin-top: 30px;
  }

  .numId {
    font-size: 20px;
    color: #333333;
  }

  .numidContent {
    font-size: 14px;
    color: #333333;
    margin-top: 10px;
  }

  .people {
    background-color: rgba(0, 204, 204, 1);
    color: #fff;
    margin-right: 10px;
    font-size: 12px;
  }

  .peopleContent, .card2 {
    margin-top: 10px;
  }
</style>
