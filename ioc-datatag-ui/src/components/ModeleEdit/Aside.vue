<template>
  <div class="aside">
    <el-input class="search" placeholder="请输入内容" v-model="search" size="small">
      <i slot="prefix" class="el-input__icon el-icon-search"></i>
    </el-input>
    <!--<div class="con">-->
      <!--<div class="box">-->
        <!--<div class="title">维度</div>-->
        <!--<div class="list">-->
          <!--<div class="line" v-for="(item, index) in dimensionList" :key="index">-->
            <!--<div class="type">{{item.columnType}}</div>-->
            <!--<div class="name">{{item.columnName}}</div>-->
            <!--<div class="icon" @click="showMenu('dimension', index)"><i class="el-icon-setting"></i></div>-->
            <!--<div class="menu" v-if="item.show">-->
              <!--<div class="menu-con">-->
                <!--<div class="menu-line" v-for="(menuItem, menuIndex) in asideMenu" :key="menuIndex"-->
                     <!--@click="menuFunc('dimension', menuItem.name, index)">-->
                  <!--<div class="menu-icon">-->
                    <!--<i :class="`${menuItem.icon}`"></i>-->
                  <!--</div>-->
                  <!--<div class="menu-name">{{menuItem.name}}</div>-->
                <!--</div>-->
                <!--<div class="del" @click="closeMenu">-->
                  <!--<i class="el-icon-close"></i>-->
                <!--</div>-->
              <!--</div>-->
            <!--</div>-->
          <!--</div>-->
        <!--</div>-->
      <!--</div>-->
      <!--<div class="box">-->
        <!--<div class="title">度量</div>-->
        <!--<div class="list">-->
          <!--<div class="line" v-for="(item, index) in measureList" :key="index">-->
            <!--<div class="type">{{item.columnType}}</div>-->
            <!--<div class="name">{{item.columnName}}</div>-->
            <!--<div class="icon" @click="showMenu('measure', index)"><i class="el-icon-setting"></i></div>-->
            <!--<div class="menu" v-if="item.show">-->
              <!--<div class="menu-con">-->
                <!--<div class="menu-line" v-for="(menuItem, menuIndex) in asideMenu" :key="menuIndex"-->
                     <!--@click="menuFunc('measure', menuItem.name, index)">-->
                  <!--<div class="menu-icon">-->
                    <!--<i :class="`${menuItem.icon}`"></i>-->
                  <!--</div>-->
                  <!--<div class="menu-name">{{menuItem.name}}</div>-->
                <!--</div>-->
                <!--<div class="del" @click="closeMenu">-->
                  <!--<i class="el-icon-close"></i>-->
                <!--</div>-->
              <!--</div>-->
            <!--</div>-->
          <!--</div>-->
        <!--</div>-->
      <!--</div>-->
    <!--</div>-->
  </div>
</template>

<script>
export default {
  name: 'datasetAside',
  props: {
    dimensionList: {
      type: Array,
      required: true
    },
    measureList: {
      type: Array,
      required: true
    }
  },
  data () {
    return {
      search: '',
      asideMenu: [
        // { icon: 'el-icon-edit', name: '编辑' },
        { icon: 'el-icon-tickets', name: '克隆维度' },
        { icon: 'el-icon-close', name: '删除' },
        // { icon: 'el-icon-plus', name: '新建维度' },
        { icon: 'el-icon-sort-up', name: '上移' },
        { icon: 'el-icon-sort-down', name: '下移' },
        { icon: 'el-icon-sort', name: '转换度量' }
      ]
    }
  },
  methods: {
    showMenu (type, index) {
      this.dimensionList.forEach(item => { item.show = false })
      this.measureList.forEach(item => { item.show = false })
      if (type === 'measure') {
        this.measureList[index].show = true
        this.$emit('updateAside', 'measure', this.measureList)
      } else {
        this.dimensionList[index].show = true
        this.$emit('updateAside', 'dimension', this.dimensionList)
      }
    },
    closeMenu () {
      this.dimensionList.forEach(item => { item.show = false })
      this.measureList.forEach(item => { item.show = false })
      this.$emit('updateAside', 'measure', this.measureList)
      this.$emit('updateAside', 'dimension', this.dimensionList)
    },
    menuFunc (type, name, index) {
      switch (name) {
        // case '编辑':
        //   this.menuEditName(type, index)
        //   return false
        case '克隆维度':
          this.menuClone(type, index)
          return false
        case '克隆度量':
          this.menuClone(type, index)
          return false
        case '删除':
          this.menuDelete(type, index)
          return false
        // case '新建':
        //   this.menuEditName(type, index)
        //   return false
        case '上移':
          this.menuMoveUp(type, index)
          return false
        case '下移':
          this.menuMoveDown(type, index)
          return false
        case '转换度量':
          this.menuChange(type, index)
          return false
        case '转换维度':
          this.menuChange(type, index)
          return false
      }
    },
    // menuEditName (type, index) {
    //   console.info('type', type)
    //   console.info('index', index)
    // },
    menuDelete (type, index) {
      if (type === 'measure') {
        this.measureList.splice(index, 1)
      } else {
        this.dimensionList.splice(index, 1)
      }
    },
    menuClone (type, index) {
      if (type === 'measure') {
        const newItem = Object.assign({}, this.measureList[index], { columnName: this.measureList[index].columnName, show: false })
        this.measureList.splice(index + 1, 0, newItem)
        this.$emit('updateAside', 'measure', this.measureList)
      } else {
        const newItem = Object.assign({}, this.dimensionList[index], { columnName: this.dimensionList[index].columnName, show: false })
        this.dimensionList.splice(index + 1, 0, newItem)
        this.$emit('updateAside', 'dimension', this.dimensionList)
      }
    },
    menuMoveUp (type, index) {
      this.closeMenu()
      if (index === 0) return this.$message.error('已经到顶了！')
      if (type === 'measure') {
        this.measureList.splice(index - 1, 0, this.measureList[index])
        this.measureList.splice(index + 1, 1)
        this.$emit('updateAside', 'measure', this.measureList)
      } else {
        // if (index === this.dimensionList.length) return this.$message.error('已经到顶了！')
        this.dimensionList.splice(index - 1, 0, this.dimensionList[index])
        this.dimensionList.splice(index + 1, 1)
        this.$emit('updateAside', 'dimension', this.dimensionList)
      }
    },
    menuMoveDown (type, index) {
      this.closeMenu()
      if (type === 'measure') {
        if (index === this.measureList.length - 1) return this.$message.error('已经到底了！')
        this.measureList.splice(index + 1, 0, this.measureList[index])
        this.measureList.splice(index, 1)
        this.$emit('updateAside', 'measure', this.measureList)
      } else {
        if (index === this.dimensionList.length - 1) return this.$message.error('已经到底了！')
        this.dimensionList.splice(index + 1, 0, this.dimensionList[index])
        this.dimensionList.splice(index, 1)
        this.$emit('updateAside', 'dimension', this.dimensionList)
      }
    },
    menuChange (type, index) {
      if (type === 'measure') {
        this.dimensionList.push(this.measureList[index])
        this.measureList.splice(index, 1)
        this.$emit('updateAside', 'measure', this.measureList)
        this.$emit('updateAside', 'dimension', this.dimensionList)
      } else {
        this.measureList.push(this.dimensionList[index])
        this.dimensionList.splice(index, 1)
        this.$emit('updateAside', 'measure', this.measureList)
        this.$emit('updateAside', 'dimension', this.dimensionList)
      }
    }
  }
}
</script>

<style scoped lang="scss">
  .aside {
    width: 250px;
    flex-shrink: 0;
    position: fixed;
    top: 60px;
    bottom: 0;
    left: 0;
    color: #ffffff;
    background: rgba(62, 71, 96, 1);
    padding: 10px;
    box-sizing: border-box;
    z-index: 2;
    .top {
      height: 45px;
      line-height: 45px;
    }
    .search {
      margin-bottom: 20px;
    }
    .con {
      color: #cccccc;
      .box {
        margin-bottom: 25px;
        .title {
          color: #ffffff;
        }
        .line {
          display: flex;
          justify-content: space-between;
          height: 30px;
          line-height: 30px;
          position: relative;
          .type {
            width: 20%;
          }
          .name {
            flex-shrink: 0;
            width: 60%;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
          .icon {
            cursor: pointer;
          }
          .menu {
            width: 150px;
            position: absolute;
            right: -170px;
            top: -150px;
            background: #ffffff;
            color: #333;
            font-size: 14px;
            box-shadow: #999 0 0 4px;
            padding: 10px;
            border-radius: 5px;
            box-sizing: border-box;
            z-index: 2;
            cursor: pointer;
            .menu-con {
              position: relative;
              .menu-line {
                display: flex;
                height: 30px;
                line-height: 30px;
                .menu-icon {
                  width: 25px;
                  height: 25px;
                  color: #999;
                  font-size: 18px;
                  margin-right: 5px;
                  background-repeat: no-repeat;
                  background-size: 100% 100%;
                }
                .menu-name {
                  flex-grow: 1;
                  border-bottom: 1px #ddd solid;
                }
              }
              .del {
                width: 15px;
                height: 15px;
                position: absolute;
                top: -10px;
                right: 0;
              }
            }
          }
        }
      }
    }
  }
</style>
