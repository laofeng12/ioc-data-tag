<template>
  <div class="treeContainer">
    <div class="treelabel">
      <div class="labelTitle">
        <div class="labelName">标签树</div>
        <div><i class="el-icon-plus add" @click="openOne"></i></div>
      </div>
      <el-input
        class="elInput"
        size="small"
        placeholder="输入关键字进行过滤"
        v-model="filterText">
      </el-input>
      <div class="treeTop">
        <el-tree
          class="filter-tree"
          :data="data"
          node-key="id"
          :props="defaultProps"
          default-expand-all
          :filter-node-method="filterNode"
          :expand-on-click-node="false"
          ref="tree">
        <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <!--<el-button-->
          <!--type="text"-->
          <!--size="mini"-->
          <!--@click="() => append(data)">-->
          <el-button
            class="addBtn"
            type="text"
            size="mini"
            @click="addTwo">
            <!--Append-->
            <i class="el-icon-plus"></i>
          </el-button>
          <el-button
            type="text"
            size="mini"
            @click="() => remove(node, data)">
            <!--Delete-->
            <i class="el-icon-delete"></i>
          </el-button>
        </span>
      </span>
        </el-tree>
      </div>

    </div>

    <div class="tableContent">
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
          <el-table-column prop="name" label="标签名称">
            <template slot-scope="scope">
              <span>教育体系标签</span>
            </template>
          </el-table-column>
          <el-table-column prop="people" label="创建时间">2019/4/19 16:12:13</el-table-column>
          <el-table-column prop="introduction" label="标签说明">这是教育体系标签简介</el-table-column>
          <el-table-column label="操作" width="180px">
            <template slot-scope="props" class="caozuo">
              <el-tooltip class="item" effect="dark" content="编辑" placement="top">
              <span class="operationIcona">
                  <i class="el-icon-edit-outline iconLogo" @click="editLabel"></i>
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
    </div>

    <el-dialog class="creat" title="添加顶级标签" :visible.sync="labelDialog" width="530px" center
               :close-on-click-modal="false"
               @close="closedialogOne">
      <div class="del-dialog-cnt">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
          <el-form-item label="标签名称:" prop="name" class="nameOne">
            <el-input v-model="ruleForm.name"></el-input>
          </el-form-item>
          <el-form-item label="标签简介:" prop="textarea" class="nameOne">
            <el-input
              class="area"
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 4}"
              placeholder="请输入内容"
              v-model="ruleForm.textarea">
            </el-input>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer device">
        <div>
          <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading">确认添加</el-button>
        </div>
      </div>
    </el-dialog>


    <el-dialog class="creat" title="添加下级标签" :visible.sync="labelDialog2" width="530px" center
               :close-on-click-modal="false"
               @close="closedialogTwo">
      <div class="del-dialog-cnt">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
          <el-form-item label="标签名称:" prop="name" class="nameOne">
            <el-input v-model="ruleForm.name"></el-input>
          </el-form-item>
          <el-form-item label="标签简介:" prop="textarea" class="nameOne">
            <el-input
              class="area"
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 4}"
              placeholder="请输入内容"
              v-model="ruleForm.textarea">
            </el-input>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer device">
        <div>
          <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading">确认添加</el-button>
        </div>
      </div>
    </el-dialog>


    <el-dialog class="creat" title="编辑标签" :visible.sync="editDialog" width="530px" center :close-on-click-modal="false"
               @close="closeEdit">
      <div class="del-dialog-cnt">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
          <el-form-item label="标签名称:" prop="name" class="nameOne">
            <el-input v-model="ruleForm.name"></el-input>
          </el-form-item>
          <el-form-item label="标签简介:" prop="textarea2" class="nameOne">
            <el-input
              class="area"
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 4}"
              placeholder="请输入内容"
              v-model="ruleForm.textarea2">
            </el-input>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer device">
        <div>
          <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading">确认修改</el-button>
        </div>
      </div>
    </el-dialog>

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
          <el-button size="small" type="primary" class="queryBtn" :loading="deleteLoading" >确定删除</el-button>
          <el-button size="small" type="primary" class="queryBtn" >取消</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "treeLabel"
  }
</script>

<script>
  let id = 1000;
  export default {
    name: "treeLabel",
    data() {
      return {
        labelDialog: false,
        labelDialog2: false,
        editDialog: false,
        deleteDialog:false,
        deleteLoading:false,
        filterText: '',
        Loading: true,
        saveLoading2: true,
        shareDialog: false,
        saveLoading: false,
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
        ztableShowList: [{
          name: '教育',
          people: '数据搬运工',
          time: '2019/4/19  16:17:22',
          introduction: '动画的很多好很多',
          share: '已共享',
        }, {
          name: '教育22',
          people: '数据搬运工22',
          time: '2019/4/19  16:17:22',
          introduction: '动画的很多好很多22',
          share: '已共享',
        }],
        ruleForm: {
          name: '',
          introduction: '',
          textarea: '',
          textarea2: ''
        },
        rules: {
          name: [
            {required: true, message: '请填写', trigger: 'blur'}
          ],
          textarea: [
            {required: true, message: '请填写', trigger: 'blur'}
          ]
        },
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
        }
      };
    },
    watch: {
      filterText(val) {
        this.$refs.tree.filter(val);
      }
    },
    methods: {
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      append(data) {
        const newChild = {id: id++, label: 'testtest', children: []};
        if (!data.children) {
          this.$set(data, 'children', []);
        }
        data.children.push(newChild);
      },

      remove(node, data) {
        const parent = node.parent;
        const children = parent.data.children || parent.data;
        const index = children.findIndex(d => d.id === data.id);
        children.splice(index, 1);
      },
      renderContent(h, {node, data, store}) {
        return (
          '<span class="custom-tree-node">' +
          '<span>{node.label}</span>' +
          '<span>' +
          '<el-button size="mini" type="text" on-click={ () => this.append(data) }>Append</el-button>' +
          '<el-button size="mini" type="text" on-click={ () => this.remove(node, data) }>Delete</el-button>' +
          '</span>' +
          '</span>');
      },
      openOne() {
        this.labelDialog = true
      },
      addTwo() {
        this.labelDialog2 = true
      },
      closedialogOne() {
        this.labelDialog = false
      },
      closedialogTwo() {
        this.labelDialog2 = false
      },
      editLabel() {
        this.editDialog = true
      },
      closeEdit() {
        this.editDialog = false
      },
      closedelete(){
        this.deleteDialog = false
      },
      handleDelete(){
        this.deleteDialog = true
      }
    },
  };
</script>

<style scoped>
  .treeContainer {
    width: 100%;
    height: 700px;
    display: flex;
  }

  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }

  .treelabel, .tableContent {
    float: left;
  }

  .treelabel {
    width: 320px;
  }

  .tableContent {
    width: 81%;
  }

  .labelTitle {
    width: 320px;
    height: 48px;
    font-size: 14px;
    color: #606266;
    background: rgb(240, 242, 245);
    margin-top: 20px;
  }

  .labelName {
    float: left;
    line-height: 48px;
    margin-left: 15px;
  }

  .add {
    float: right;
    line-height: 48px;
    margin-right: 15px;
  }

  .elInput {
    width: 300px;
    margin-left: 10px;
    margin-top: 10px;
  }

  .treeTop {
    margin-top: 20px;
    padding-left: 5px;
    padding-right: 5px;
    height: 565px;
    overflow-y: auto;
  }

  .iconLogo {
    font-size: 18px;
    margin-left: 25px;
    cursor: pointer;
  }

  .newTable {
    height: 590px;
    overflow-y: auto;
  }
  .addBtn{
    margin-right: 5px;
  }
</style>
