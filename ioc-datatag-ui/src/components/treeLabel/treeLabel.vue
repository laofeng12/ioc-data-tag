<template>
  <div class="treeContainer">
    <div class="treelabel">
      <div class="labelTitle">
        <div class="labelName">标签树</div>
        <div v-show="topaddLabel"><i class="el-icon-plus add" @click="openOne"></i></div>
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
          @node-click="handleNodeClick"
          ref="tree">
        <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span v-show="addLabel">
          <!--<el-button-->
          <!--type="text"-->
          <!--size="mini"-->
          <!--@click="() => append(data)">-->
          <el-button class="addBtn" type="text" size="mini" @click.stop="addTwo(node,data)" :disabled="node.level>2">
            <!--Append-->
            <i class="el-icon-plus"></i>
          </el-button>
          <el-button type="text" size="mini" @click.stop="remove(node, data)" :disabled="node.level>2">
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
        <el-table ref="multipleTable" :data="tableparentList" border stripe tooltip-effect="dark"
                  style="width: 100%;text-align: center"
                  :header-cell-style="{background:'#f0f2f5'}">
          <template slot="empty">
            <div v-if="Loading">
              <div v-loading="saveLoading2"></div>
            </div>
            <div v-else>暂无数据</div>
          </template>
          <el-table-column prop="tagName" label="标签名称"></el-table-column>
          <el-table-column prop="createTime" label="创建时间"></el-table-column>
          <el-table-column prop="synopsis" label="标签简介"></el-table-column>
          <el-table-column label="操作" width="180px" v-if="labelEdit">
            <template slot-scope="props" class="caozuo">
              <el-tooltip class="item" effect="dark" content="编辑" placement="top">
              <span class="operationIcona">
                  <i class="el-icon-edit-outline iconLogo" @click="editLabel(props.row)"></i>
              </span>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="删除" placement="top">
              <span class="operationIcona">
                <i class="el-icon-delete iconLogo" @click="handleDelete(props.row.id)"></i>
              </span>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
        <el-table class="secondTable" ref="multipleTable" :data="ztableShowList" border stripe tooltip-effect="dark"
                  style="width: 100%;text-align: center"
                  :header-cell-style="{background:'#f0f2f5'}">
          <template slot="empty">
            <div v-if="Loading2">
              <div v-loading="saveLoading2"></div>
            </div>
            <div v-else>暂无数据</div>
          </template>
          <el-table-column prop="tagName" label=""></el-table-column>
          <el-table-column prop="createTime" label=""></el-table-column>
          <el-table-column prop="synopsis" label=""></el-table-column>
          <el-table-column label="操作" width="180px" v-if="labelEdit">
            <template slot-scope="props" class="caozuo">
              <el-tooltip class="item" effect="dark" content="编辑" placement="top">
              <span class="operationIcona">
                  <i class="el-icon-edit-outline iconLogo" @click="editLabel(props.row)"></i>
              </span>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="删除" placement="top">
              <span class="operationIcona">
                <i class="el-icon-delete iconLogo" @click="handleDelete(props.row.id)"></i>
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
          <el-button size="small" plain class="btn-group" @click="cancel()">取消</el-button>
          <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading"
                     @click="add(ruleForm.name,ruleForm.textarea)">添加
          </el-button>
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
          <el-button size="small" plain class="btn-group" @click="cancelOne()">取消</el-button>
          <el-button size="small" type="primary" class="queryBtn" :loading="savelabelLoading"
                     @click="addNext(ruleForm.name,ruleForm.textarea)">添加
          </el-button>
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
          <el-button size="small" plain class="btn-group" @click="closeShare2">取消</el-button>
          <el-button size="small" type="primary" class="queryBtn" :loading="editLoading" @click="sureShare">确定
          </el-button>
        </div>
      </div>
    </el-dialog>

    <el-dialog class="creat" title="删除提示" :visible.sync="deleteDialog" width="530px" center
               :close-on-click-modal="false"
               @close="closedelete">
      <div class="del-dialog-cnt">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px">
          <el-form-item>您正在删除南城区高考成绩数据，是否确认删除？</el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer device">
        <div>
          <el-button size="small" type="primary" class="queryBtn" :loading="deleteLoading" @click="delTree(delTreeId)">
            确定删除
          </el-button>
          <el-button size="small" type="primary" class="queryBtn">取消</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {lookTree, looktreeTable} from '@/api/shareLabel.js'
  import {getDtTagData, delTree, delTagGroup} from '@/api/tagManage'

  let id = 1000;
  export default {
    name: "treeLabel",
    data() {
      return {
        delTreeId: 0,
        lvl: 1,
        id: 0,
        preaTagId: 0,
        labelDialog: false,
        labelDialog2: false,
        editDialog: false,
        deleteDialog: false,
        deleteLoading: false,
        filterText: '',
        Loading: true,
        Loading2: true,
        saveLoading2: true,
        shareDialog: false,
        saveLoading: false,
        savelabelLoading: false,
        editLoading:false,
        percentage: 30,
        addLabel: true,
        topaddLabel: true,
        labelEdit: true,
        treeID: '',
        value1: '',
        labelcreatID: '',
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
        tableparentList: [],
        ztableShowList: [],
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
        data: [],
        defaultProps: {
          children: 'childrenNode',
          label: 'tagName'
        }
      };
    },
    watch: {
      filterText(val) {
        this.$refs.tree.filter(val);
      }
    },
    methods: {
      //编辑标签弹窗
      editLabel(row) {
        // console.log(row)
        this.ruleForm.name = row.tagName
        this.ruleForm.textarea2 = row.synopsis
        this.id = row.id
        this.editDialog = true
      },
      // 编辑标签确认
      async sureShare() {
        this.editLoading = true
        const tagsId = Number(this.$route.params.tagsId)
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            try {
              const params = {
                id: this.id,
                isNew: false,
                synopsis: this.ruleForm.textarea2,
                tagName: this.ruleForm.name,
                tagsId: tagsId
              }
              const data = await getDtTagData(params)
              //console.log('编辑成功')
              if (data.message == '修改成功') {
                this.$message({
                  message: '修改成功',
                  duration: 1000,
                  type: 'success'
                })
                this.sharelookTree()
                this.editDialog = false
                this.editLoading = false
                this.$refs.ruleForm.resetFields()
              } else {
                this.editLoading = false
                this.$message.error(data.message)
              }
            } catch (e) {
              this.editLoading = false
            }
          } else {
            this.editLoading = false
          }
        });
      },

      //添加顶级标签
      add(synopsis, tagsName) {
        this.saveLoading = true
        this.$refs.ruleForm.validate((valid) => {
          if (valid) {
            try {
              this.lvl = 1
              this.getDtTagData(null, synopsis, tagsName)
              this.labelDialog = false
              this.saveLoading = false
              this.$refs.ruleForm.resetFields()
            } catch (e) {
              this.saveLoading = false
              console.log(e);
            }
          } else {
            this.saveLoading = false
          }
        });
      },
      //添加下级
      addNext(synopsis, tagsName) {
        this.savelabelLoading = true
        this.$refs.ruleForm.validate((valid) => {
          if (valid) {
            try {
              this.tableparentList = []
              this.getDtTagData(this.preaTagId, synopsis, tagsName)
              this.labelDialog2 = false
              this.savelabelLoading = false
              this.$refs.ruleForm.resetFields()
            } catch (e) {
              this.savelabelLoading = false
              console.log(e);
            }
          } else {
            this.savelabelLoading = false
          }
        });

      },
      closeShare2() {
        this.editDialog = false
        this.$refs.ruleForm.resetFields()
      },
      cancel() {
        this.labelDialog = false
        this.$refs.ruleForm.resetFields()
      },
      //取消添加下级
      cancelOne() {
        this.labelDialog2 = false
        this.$refs.ruleForm.resetFields()

      },
      filterNode(value, data) {
        if (!value) return true;
        return data.tagName.indexOf(value) !== -1;
      },
      async handleNodeClick(data) {
        this.tableparentList = []
        const treeTable = await looktreeTable(data.id)
        this.ztableShowList = treeTable.childrenTag
        this.tableparentList.push(treeTable.parentTag)
        if (this.ztableShowList == '') {
          this.Loading2 = false
        }
      },
      append(data) {
        const newChild = {id: id++, label: 'testtest', children: []};
        if (!data.children) {
          this.$set(data, 'children', []);
        }
        data.children.push(newChild);
      },

      remove(node, data) {
        this.delTree(data.id)
        console.log('remove', data.id)
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
      addTwo(node, data) {
        //console.log('新建下级标签',node,data)
        this.lvl = node.level + 1
        this.preaTagId = data.id
        this.labelDialog2 = true
      },
      closedialogOne() {
        this.labelDialog = false
      },
      closedialogTwo() {
        this.labelDialog2 = false
      },
      closeEdit() {
        this.editDialog = false
      },
      closedelete() {
        this.deleteDialog = false
      },
      handleDelete(id) {
        this.delTreeId = id
        this.deleteDialog = true
      },
      //删除
      async delTree(id) {
        this.tableparentList = []
        try {
          const data = await delTree(id)
          if (data.message == '删除成功') {
            this.$message({
              message: '删除成功',
              duration: 1000,
              type: 'success'
            });
            this.sharelookTree()

          } else {
            this.$message.error(data.message)
          }

        } catch (e) {

        }
        this.deleteDialog = false
      },
      //删除表格对应标签
      async delTag(id) {
        try {
          const data = await delTree(id)
          //console.log(data)
          if (data.message == '删除成功') {
            this.$message({
              message: '删除成功',
              duration: 1000,
              type: 'success'
            });
            this.getTagsData()
          } else {
            this.$message.error(data.message)
          }

        } catch (e) {

        }
      },
      //新建树节点
      async getDtTagData(preaTagId, synopsis, tagsName) {
        const tagsId = Number(this.$route.params.tagsId)
        const params = {
          lvl: this.lvl,
          preaTagId: preaTagId,
          isNew: true,
          synopsis: synopsis,
          tagName: tagsName,
          tagsId: tagsId,//标签组id
        }
        try {
          const data = await getDtTagData(params)
          this.sharelookTree()

        } catch (e) {

        }
      },
      async sharelookTree() {
        try {
          const res = await lookTree(this.$route.params.tagsId)
          this.data = res.childrenNode
          if (res.childrenNode && res.childrenNode.length > 0) {
            const treeTable = await looktreeTable(res.childrenNode[0].id)
            if (treeTable == '') {
              this.Loading = false
            } else {
              this.tableparentList = []
              this.ztableShowList = treeTable.childrenTag ? treeTable.childrenTag : []
              this.tableparentList.push(treeTable.parentTag)
              if (this.ztableShowList == '') {
                this.Loading2 = false
              }
            }
          } else {
            this.Loading = false
            this.Loading2 = false
          }
        } catch (e) {
          console.log(e);
        }
      },
    },
    created() {
      if (this.$route.name == 'lookTree') {
        this.sharelookTree()
        this.addLabel = false
        this.topaddLabel = false
        this.labelEdit = false
      } else if (this.$route.name == 'editTree') {
        this.sharelookTree()
      } else if (this.$route.name == 'labelcreatTree') {
        this.sharelookTree()
      }
      else {
      }
    }
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
    margin-top: 20px;
  }

  .tableContent {
    width: 81%;
    margin-top: 20px;
  }

  .labelTitle {
    width: 320px;
    height: 48px;
    font-size: 14px;
    color: #606266;
    background: rgb(240, 242, 245);
    /*margin-top: 20px;*/
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
    margin-top: -20px;
  }

  .addBtn {
    margin-right: 5px;
  }

  .secondTable >>> .el-table__header-wrapper {
    display: none;
  }
</style>
