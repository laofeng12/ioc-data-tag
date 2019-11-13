<template>
  <div>
    <div class="treeContainer">
      <div class="treelabel treeBel temporary">
        <div class="labelTitle">
          <div class="labelName"><i class="el-icon-folder iconLabel"></i>{{groupName}}</div>
          <div v-show="topLabel"><i class="el-icon-plus add" @click="openOne"></i></div>
        </div>
        <el-input
          class="elInput"
          size="small"
          clearable
          placeholder="输入关键字进行过滤"
          v-model="filterText">
        </el-input>
        <div class="treeTop" :style="{height:taHeight}">
          <el-tree
            class="filter-tree"
            :data="data"
            node-key="id"
            :props="defaultProps"
            default-expand-all
            :filter-node-method="filterNode"
            :expand-on-click-node="false"
            :highlight-current="true"
            @node-click="handleNodeClick"
            ref="tree">
        <span class="custom-tree-node" slot-scope="{ node, data }">
        <span class="labelLength" :title="node.label">{{ node.label }}</span>
        <span v-show="addLabel">
          <el-button class="addBtn" type="text" size="mini" @click.stop="addTwo(node,data)" :disabled="node.level>2">
            <!--Append-->
            <i class="el-icon-plus"></i>
          </el-button>
          <el-button type="text" size="mini" @click.stop="remove(node, data)">
            <!--Delete-->
            <i class="el-icon-delete"></i>
          </el-button>
        </span>
      </span>
          </el-tree>
        </div>

      </div>

      <div class="tableContent">
        <el-form :model="ruleForm2" :rules="rules" ref="ruleForm2" label-width="100px" v-if="showContent">
          <el-form-item label="标签名称:" prop="name2" class="nameOne">
            <el-input v-model="ruleForm2.name2" maxlength="25" show-word-limit :disabled="lookContent"
                      placeholder="请输入标签名称"></el-input>
          </el-form-item>
          <el-form-item label="标签简介:" prop="textarea2" class="nameOne">
            <el-input
              class="area"
              type="textarea"
              :autosize="{ minRows: 8, maxRows: 4}"
              placeholder="请输入标签简介"
              maxlength="100"
              show-word-limit
              :disabled="lookContent"
              v-model="ruleForm2.textarea2">
            </el-input>
          </el-form-item>
          <el-form-item style="text-align: center">
            <el-button v-show="showBtn" size="small" type="primary" :loading="editLoading"
                       @click="sureShare(ruleForm.deteleId)">保存
            </el-button>
            <el-button v-show="showBtn" size="small" @click="handleDelete(ruleForm2.name2,ruleForm.deteleId)">删除
            </el-button>
            <el-button size="small" @click="goback">返回</el-button>
          </el-form-item>
        </el-form>
        <div class="noImage" v-else>
          <img src="../../assets/img/002.png" height="144" width="160"/></div>
      </div>
      <el-dialog class="creat" title="添加顶级标签" :visible.sync="labelDialog" width="530px" center
                 :close-on-click-modal="false"
                 @close="closedialogOne">
        <div class="del-dialog-cnt">
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
            <el-form-item label="标签名称:" prop="name" class="nameOne">
              <el-input v-model="ruleForm.name" maxlength="25" show-word-limit placeholder="请输入标签名称"></el-input>
            </el-form-item>
            <el-form-item label="标签简介:" prop="textarea" class="nameOne">
              <el-input
                class="area"
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 4}"
                placeholder="请输入标签简介"
                maxlength="100"
                show-word-limit
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
              <el-input v-model="ruleForm.name" maxlength="25" show-word-limit placeholder="请输入标签名称"></el-input>
            </el-form-item>
            <el-form-item label="标签简介:" prop="textarea" class="nameOne">
              <el-input
                class="area"
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 4}"
                placeholder="请输入标签简介"
                maxlength="100"
                show-word-limit
                v-model="ruleForm.textarea">
              </el-input>
            </el-form-item>
          </el-form>
        </div>
        <div slot="footer" class="dialog-footer device">
          <div>
            <el-button size="small" plain class="btn-group" @click="cancelOne()">取消</el-button>
            <el-button size="small" type="primary" class="queryBtn" :loading="addLoading"
                       @click="addNext(ruleForm.name,ruleForm.textarea)">添加
            </el-button>
          </div>
        </div>
      </el-dialog>
      <!--删除开始-->
      <el-dialog class="creat" title="删除提示" :visible.sync="deleteDialog" width="530px" center
                 :close-on-click-modal="false"
                 @close="closedelete">
        <div class="del-dialog-cnt">
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" style="text-align: center">
            <el-form-item>您正在删除{{this.tagName}}标签，使用过该标签的模型会停止运行，是否确认删除？</el-form-item>
          </el-form>
        </div>
        <div slot="footer" class="dialog-footer device">
          <div>
            <el-button size="small" type="primary" class="queryBtn" :loading="deleteLoading"
                       @click="delTree(delTreeId)">
              确定删除
            </el-button>
            <el-button size="small" @click="cancelDelete">取消</el-button>
          </div>
        </div>
      </el-dialog>
      <!--删除结束 end-->
    </div>
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
        lookContent: false,
        showContent: true,
        showBtn: true,
        groupName: '',
        tabHeight: '',
        tagName: '',
        delTreeId: 0,
        lvl: 1,
        id: 0,
        tagId: 0,
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
        addLoading: false,
        editLoading: false,
        percentage: 30,
        addLabel: true,
        topLabel: true,
        labelEdit: true,
        treeID: '',
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
        parentList: [],
        showList: [],
        ruleForm: {
          name: '',
          textarea: '',
          deteleId: ''
        },
        ruleForm2: {
          name2: '',
          textarea2: ''
        },
        rules: {
          name: [
            {required: true, message: '请填写', trigger: 'blur'}
          ],
          name2: [
            {required: true, message: '请填写', trigger: 'blur'}, {trigger: 'change'}
          ],
          textarea: [
            {required: true, message: '请填写', trigger: 'blur'}
          ],
          textarea2: [
            {required: true, message: '请填写', trigger: 'blur'}, {trigger: 'change'}
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
    computed: {
      taHeight: function () {
        return (document.body.clientHeight - 300) + "px"
      }
    },
    methods: {
      // 编辑标签确认
      async sureShare(id) {
        this.editLoading = true
        const tagsId = Number(this.$route.params.tagsId)
        this.$refs.ruleForm2.validate(async (valid) => {
          if (valid) {
            try {
              const params = {
                id: id,
                isNew: false,
                synopsis: this.ruleForm2.textarea2,
                tagName: this.ruleForm2.name2,
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
                // this.sharelookTree()
                try {
                  const res = await lookTree(this.$route.params.tagsId)
                  this.data = res.childrenNode
                  if (res.childrenNode && res.childrenNode.length > 0) {
                    const treeTable = await looktreeTable(res.childrenNode[0].id)
                  } else {
                    this.showContent = false
                    this.ruleForm2.name2 = ''
                    this.ruleForm2.textarea2 = ''
                  }
                } catch (e) {
                  console.log(e);
                }
                this.editDialog = false
                this.editLoading = false
                // this.$refs.ruleForm.resetFields()
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
      /**
       * 标签树添加顶级标签
       * @param tagsName
       * @param synopsis
       */
      add(tagsName, synopsis) {
        this.saveLoading = true
        this.$refs.ruleForm.validate((valid) => {
          if (valid) {
            try {
              this.lvl = 1
              this.getDtTagData(null, synopsis, tagsName)
              this.labelDialog = false
              this.saveLoading = false
              this.$refs.ruleForm.resetFields()
              this.showContent = true
            } catch (e) {
              this.saveLoading = false
              console.log(e);
            }
          } else {
            this.saveLoading = false
          }
        });
      },
      /**
       * 标签树添加下级
       * @param tagsName
       * @param synopsis
       */
      addNext(tagsName, synopsis) {
        this.addLoading = true
        this.$refs.ruleForm.validate((valid) => {
          if (valid) {
            try {
              this.parentList = []
              this.getDtTagData(this.tagId, synopsis, tagsName)
              this.labelDialog2 = false
              this.addLoading = false
              this.$refs.ruleForm.resetFields()
            } catch (e) {
              this.addLoading = false
              console.log(e);
            }
          } else {
            this.addLoading = false
          }
        });
      },
      // 关闭
      closeShare2() {
        this.editDialog = false
        this.$refs.ruleForm.resetFields()
      },
      // 弹框取消操作
      cancel() {
        this.labelDialog = false
        this.$refs.ruleForm.resetFields()
      },
      // 取消添加下级
      cancelOne() {
        this.labelDialog2 = false
        this.$refs.ruleForm.resetFields()

      },
      filterNode(value, data) {
        if (!value) return true;
        return data.tagName.indexOf(value) !== -1;
      },
      // 获取标签信息
      async handleNodeClick(data) {
        this.ruleForm2.name2 = data.tagName
        this.ruleForm2.textarea2 = data.synopsis
        this.ruleForm.deteleId = data.id
        this.parentList = []
        const treeTable = await looktreeTable(data.id)
        this.showList = treeTable.childrenTag
        this.parentList.push(treeTable.parentTag)
        if (this.showList == '') {
          this.Loading2 = false
        }
      },
      // 增加
      append(data) {
        const newChild = {id: id++, label: 'testtest', children: []};
        if (!data.children) {
          this.$set(data, 'children', []);
        }
        data.children.push(newChild);
      },
      // 删除
      remove(node, data) {
        this.deleteDialog = true
        this.tagName = data.tagName
        this.delTreeId = data.id
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
      // 新建下级标签
      addTwo(node, data) {
        //console.log('新建下级标签',node,data)
        this.lvl = node.level + 1
        this.tagId = data.id
        this.labelDialog2 = true
      },
      // 关闭一级标签弹框
      closedialogOne() {
        this.labelDialog = false
        this.$refs.ruleForm.resetFields()
      },
      // 关闭二级标签弹框
      closedialogTwo() {
        this.labelDialog2 = false
        this.$refs.ruleForm.resetFields()
      },
      // 取消编辑
      closeEdit() {
        this.editDialog = false
      },
      // 关闭删除弹框
      closedelete() {
        this.deleteDialog = false
      },
      // 删除操作
      handleDelete(name, id) {
        this.tagName = name
        this.delTreeId = id
        this.deleteDialog = true
      },
      // 确定删除
      async delTree(id) {
        this.parentList = []
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
      cancelDelete() {
        this.deleteDialog = false
      },
      /**
       * 标签管理标签树新建树节点
       * @param tagId
       * @param synopsis
       * @param tagsName
       * @returns {Promise<void>}
       */
      async getDtTagData(tagId, synopsis, tagsName) {
        const tagsId = Number(this.$route.params.tagsId)
        const params = {
          lvl: this.lvl,
          preaTagId: tagId,
          isNew: true,
          synopsis: synopsis,
          tagName: tagsName,
          tagsId: tagsId, // 标签组id
        }
        try {
          const data = await getDtTagData(params)
          this.sharelookTree()

        } catch (e) {

        }
      },
      // 获取数据列表数据
      async sharelookTree() {
        try {
          const res = await lookTree(this.$route.params.tagsId)
          this.data = res.childrenNode
          if (res.childrenNode && res.childrenNode.length > 0) {
            const treeTable = await looktreeTable(res.childrenNode[0].id)
            this.ruleForm2.name2 = treeTable.parentTag.tagName
            this.ruleForm2.textarea2 = treeTable.parentTag.synopsis
            this.ruleForm.deteleId = treeTable.parentTag.id
          } else {
            this.showContent = false
            this.ruleForm2.name2 = ''
            this.ruleForm2.textarea2 = ''
          }
        } catch (e) {
          console.log(e);
        }
      },
      // 返回
      goback() {
        this.$router.go(-1)
      }
    },
    created() {
      if (this.$route.name == 'lookTree') {
        this.sharelookTree()
        this.addLabel = false
        this.topLabel = false
        this.labelEdit = false
        this.showBtn = false
        this.lookContent = true
      } else if (this.$route.name == 'editTree') {
        this.sharelookTree()
      } else if (this.$route.name == 'labelcreatTree') {
        this.sharelookTree()
      } else if (this.$route.name == 'tree') {
        this.sharelookTree()
      } else {
      }
      this.groupName = this.$route.params.tagsName
    }
  };
</script>

<style scoped>
  .treeContainer {
    width: 100%;
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
    border: 1px solid #dcdfe6;
  }

  .treelabel {
    width: 320px;
    margin-top: 20px;
  }

  .tableContent {
    width: 81%;
    margin-top: 20px;
    padding: 0px 16px;
    padding-top: 20px;
  }

  .labelTitle {
    /*width: 320px;*/
    width: 318px;
    height: 48px;
    font-size: 14px;
    color: #606266;
    background: rgb(240, 242, 245);
  }

  .labelName {
    float: left;
    line-height: 48px;
    padding-left: 15px;
    width: 200px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
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
    overflow-y: auto;
  }

  .addBtn {
    margin-right: 5px;
  }

  .secondTable >>> .el-table__header-wrapper {
    display: none;
  }

  .iconLabel {
    padding-right: 5px;
  }

  .noImage {
    text-align: center;
    margin-top: 20%;
  }

  .labelLength {
    display: inline-block;
    width: 180px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
</style>
