<template>
  <!--<div class='app-container'>-->
  <div class=''>
    <div class="result">
      <!--<div class="queryTitle">画像查询</div>-->
      <div class="queryIt">
        <div class="nameImage">画像查询</div>
        <div class="queryInput">
          <el-input
            class="inputBtn moduleOne"
            size="small"
            clearable
            placeholder="请输入内容"
            prefix-icon="el-icon-search"
            @keyup.enter.native="queryDetail"
            v-model.trim="input2">
          </el-input>
          <el-button class="zxlistBtn" size="small" type="primary" @click="queryDetail">查询</el-button>
        </div>
      </div>
    </div>
    <div class="back">
      <!--<el-button size="small" @click="goback" v-if="contentArr != ''">返回</el-button>-->
      <!--<el-button size="small" @click="goback">返回</el-button>-->
    </div>
    <div class="queryCard" v-if="contentArr != ''">
      <div class="cart_list">
        <el-row :gutter="24">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-for="(item, index) in contentArr" :key="index">
            <el-card>
              <div class="contents">
                <img src="@/assets/img/icon_default.png" alt="">
                <el-form>
                  <div class="numId">
                    <span>ID：</span>
                    <span>{{item.id}}</span>
                  </div>
                  <div class="idContent">{{item.title}}</div>
                  <div class="peopleContent">
                    <span class="people peopleHeight" v-for="(name,key) in item.lists" :key="key">{{name}}</span>
                  </div>
                </el-form>
              </div>
              <div class="bottoms">
                <el-button style="padding: 3px 0;font-size:15px;" type="text"
                           @click="lookDetail(item.id,item.tableName)">
                  <i class="ch-button-icon el-icon-ch-view_s"></i>查看详情
                </el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>
    <div v-else>
      <img class="noImage" src="../../assets/img/007.png" height="144" width="160"/>
    </div>

  </div>
</template>

<script>
  import {mapActions, mapState, mapGetters} from 'vuex'

  export default {
    components: {},
    name: "queryImage",
    data() {
      return {
        input2: ''
      }
    },
    methods: {
      ...mapActions('tagPanel', ['getimageList', 'getarrList']),
      // 画像根据输入内容查询
      queryDetail() {
        if (this.input2) {
          this.getimageList(this.input2)
        } else {
          this.$message({
            message: '请填写查询信息',
            type: 'warning'
          });
        }
      },
      /**
       * 画像查询详情
       * @param detailId
       * @param tableName
       */
      lookDetail(detailId, tableName) {
        localStorage.setItem('pKey', detailId)
        localStorage.setItem('tableName', tableName)
        this.getarrList({
          pKey: detailId,
          tableName: tableName
        })
        this.$router.push({
          path: '/detailImage',
          query: {
            detailId: detailId,
            id: this.input2
          }
        })
      },
    },
    created() {
      this.input2 = this.$route.query.id
      this.queryDetail()
    },
    computed: {
      ...mapState('tagPanel', ['contentArr', 'listArr'])
    },
    watch: {},
    mounted() {}
  }
</script>

<style lang="stylus" scoped>
  .inputBtn {
    width: 482px;
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
    background-color: #FFF;
    padding: 16px 16px 24px 16px;
  }

  .queryCard {
    margin-top: 34px;
  }

  .numId {
    font-size: 20px;
    color: #333333;
  }

  .idContent {
    font-size: 14px;
    color: #333333;
    margin-top: 10px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .people {
    color: #0486FE;
    background: #F4FAFF;
    border: 1px solid #0486FE;
    margin-right: 10px;
    font-size: 12px;
  }

  .peopleContent {
    margin-top: 20px;
    display: flex;
    flex-wrap: wrap;
    height: 100px;
    overflow: hidden;
  }

  .card2 {
    margin-bottom: 10px;
  }

  .back {
    display: flex;
    justify-content: flex-end;
    margin-top: -32px;
  }

  .noImage {
    margin-top: 60px;
    margin-left: 180px;
  }

  .nameImage {
    font-size: 16px;
    color: #262626;
    font-family: PingFangSC-Medium;
    margin-left: -4px;
  }

  .queryInput {
    display: flex;
    margin-left: 8px;
    margin-top: 16px;
  }

  .cart_list {
    >>> .el-form-item__content {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    >>> .el-card__body {
      padding 0 !important
      height: 248px;
      position: relative;
    }
    .contents {
      padding 20px
      display flex
      img {
        flex 0 0 50px
        width 50px
        height 50px
        border-radius 50%
        background-size cover
      }
      .el-form {
        margin-left 15px
        flex 1
        .el-form-item {
          margin-bottom 0
          >>> .el-form-item__label {
            color: rgba(0, 0, 0, 0.45)
            font-size 14px
          }
          >>> .el-form-item__content {
            color: rgba(0, 0, 0, 0.45)
            font-size 14px
          }
        }
        .tit_head {
          >>> .el-form-item__content {
            font-size 16px
            color: rgba(0, 0, 0, 0.85) !important
          }
        }
      }
    }
    .bottoms {
      height 50px
      background: #F7F9FA
      border: 1px solid #E9E9E9
      border-radius: 0 0 2px 2px
      display flex
      padding 10px 0
      position: absolute
      width: 100%
      bottom: 0px
      .el-button {
        flex 1
        text-align center
        color: rgba(0, 0, 0, 0.45)
        border-radius 0
        color #0486FE
      }
      >>> .subChecked {
        color #989898
      }
      >>> .nosubChecked {
        color #0486FE
      }
    }
  }

  .cart_list .el-form {
    padding: 0px !important;
  }

  .peopleHeight {
    height: 22px;
    padding: 4px 10px;
    margin-bottom: 12px;
  }

  .cart_list >>> .el-card.is-always-shadow {
    box-shadow: 0 0px 0px 0 transparent !important
  }

  .cart_list >>> .el-card {
    border: none !important
    border-radius: 0px !important
  }
</style>
