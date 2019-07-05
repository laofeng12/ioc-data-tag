<template>
  <section class="w-carousel-wrapper">
    <nav class="nav-bar">
      <div class="plaform-name">大数据服务平台</div>
      <div class="login-btn-wrap">
        <router-link to="/login">
          <el-button type="primary" class="login-btn">登陆</el-button>
        </router-link>
      </div>
    </nav>
    <div class="w-bg-wrapper">
      <el-carousel :height="carouselHeight" arrow="never">
        <el-carousel-item v-for="(item, index) in bgData" :key="index" :class="{'out-bg': !!item.title}">
          <div class="describe-wrap" v-if="item.title">
            <h3 class="title">{{item.title}}</h3>
            <p class="describe">{{item.describe}}</p>
          </div>
          <div class="w-bg" :class="`w-bg-${index + 1}`"></div>
        </el-carousel-item>
      </el-carousel>
    </div>
  </section>
</template>

<script>
export default {
  data () {
    return {
      bgData: [
        {},
        { title: '多业务协同', describe: '让数据来驱动服务效率的提升' },
        { title: '数据智能化', describe: '让数据来驱动服务效率的提升' }
      ],
      carouselHeight: 'calc(100vh - 72px)'
    }
  },
  mounted () {
    this.setCarouseHeigth()
  },
  methods: {
    setCarouseHeigth () {
      const screenWidth = window.screen.width
      if (screenWidth <= 1600) {
        this.carouselHeight = 'calc(100vh - 54px)'
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.w-carousel-wrapper {
  height: 100%;
  .nav-bar {
    position: fixed;
    top: 0;
    left: 0;
    height: 72px;
    width: 100%;
    line-height: 72px;
    background: #033F60;
    display: flex;
    justify-content: space-between;
    .plaform-name {
      padding-left: 76px;
      font-size: 20px;
      color: #ffffff;
      background: url($baseUrl + '/img/logo.png') no-repeat 40px/26px 26px;
    }
    .login-btn-wrap {
      margin-right: 40px;
      .login-btn {
        line-height: 20px;
        padding: 5px 20px;
      }
    }
  }
  .w-bg-wrapper {
    height: 100%;
    background: #022539;
    padding-top: 72px;
    .describe-wrap {
      color: #ffffff;
      text-align: center;
      margin-top: 120px;
      .title {
        font-size: 40px;
        margin: 0;
        font-family: PingFangSC-Medium;
      }
      .describe {
        font-size: 20px;
        padding-left: 20px;
        margin-top: 16px;
        font-family: PingFangSC-Regular;
        letter-spacing: 20px;
      }
    }
    .out-bg {
      background: url($baseUrl + '/img/bitmap_bg.png') no-repeat center 172px/1270px 744px;
    }
    .w-bg {
      width: 100%;
      height: 100%;
    }
    .w-bg-1 {
      background: url($baseUrl + '/img/bitmap_1.png') no-repeat center/1270px 744px;
    }
    .w-bg-2 {
      background: url($baseUrl + '/img/bitmap_2.png') no-repeat center 87px/845px 555px;
      // background-image: url($baseUrl + '/img/bitmap_2.png');
    }
    .w-bg-3 {
      background: url($baseUrl + '/img/bitmap_3.png') no-repeat center 87px/865px 530px;
      // background-image: url($baseUrl + '/img/bitmap_3.png');
    }

  }
  @media screen and (max-width: 1600px) {
    .nav-bar {
      height: 54px;
      line-height: 54px;
      .plaform-name {
        background-position: 30px center;
        background-size: 20px 20px;
        font-size: 16px;
        padding-left: 57px;
      }
      .login-btn-wrap {
        margin-right: 30px;
        .login-btn {
          line-height: 15px;
          padding: 4px 15px;
          font-size: 14px;
        }
      }
    }

    .w-bg-wrapper {
      padding-top: 54px;
      .describe-wrap {
        margin-top: 90px;
        .title {
          font-size: 30px;
        }
        .describe {
          font-size: 16px;
          padding-left: 15px;
          margin-top: 12px;
          letter-spacing: 15px;
        }
      }
    }
    .w-bg-wrapper {
      .out-bg {
        background-position: center 102px;
        background-size: 1016px 595px;
      }
      .w-bg-1 {
        background-size: 1270px 744px;
      }
      .w-bg-2 {
        background-position: center 65px;
        background-size: 633px 416px;
      }
      .w-bg-3 {
        background-position: center 65px;
        background-size: 648px 397px;
      }
    }
  }
}
</style>
