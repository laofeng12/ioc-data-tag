<template>
  <div class="app-container">
    <div class="clearfix">
      <div class="searchBar">
        <el-input
          class="zxinp moduleOne"
          size="small"
          clearable
          placeholder="请输入内容"
          prefix-icon="el-icon-search"
          @keyup.enter.native="getQuireData"
          v-model.trim="input2">
        </el-input>
        <el-select class="tagSelect" size="small" v-model="value" placeholder="请选择">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
        <el-button class="zxlistBtn" size="small" type="primary" @click="getQuireData">查询</el-button>
      </div>
      <div class="actionBar">
        <el-button size="small" type="primary" @click="createLabel">创建标签组</el-button>
        <el-button size="small" type="primary" @click="shareLabel">共享标签组</el-button>
      </div>
    </div>
    <div class="tableBar">
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
          <el-table-column prop="tagsName" label="标签组名称"></el-table-column>
          <el-table-column prop="source" label="选用热度">
            <template slot-scope="scope">
              <div class="gress">
                <div class="gressPercentage">
                  <!--{{scope.row.percentage}}-->
                  <!--<el-progress :percentage="scope.row.popularityLevel" :show-text="false"-->
                  <!--:color="customColorMethod"></el-progress>-->
                  <el-progress :percentage="scope.row.percentage" :show-text="false"
                               :color="customColorMethod"></el-progress>
                </div>
                <div>{{scope.row.popularity}}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="isShare" label="共享状态">
            <template slot-scope="scope">
              <!--<div v-if="scope.row.isShare===0">未共享</div>-->
              <!--<div v-if="scope.row.isShare===1">已共享</div>-->
              <div>
                <span @click='shareStatus(!scope.row.isShare,scope.row)'
                      :class='
                      ["share-btn",scope.row.isShare?"share":"no-share"]'>{{scope.row.isShare ? '已共享': '未共享'}}
                </span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="synopsis" label="标签组简介"></el-table-column>
          <el-table-column prop="modifyTime" label="修改时间"></el-table-column>
          <el-table-column label="操作" width="180px">
            <template slot-scope="{row,$index}" class="caozuo">
              <el-tooltip class="item" effect="dark" content="设置" placement="top">
                <span class="operationIcona">
                  <!--el-icon-document-->
                  <!--<i class="el-icon-setting iconLogo" @click="handleShare(row,$index)"></i>-->
                  <i class="el-icon-document iconLogo" @click="handleShare(row,$index)"></i>
                </span>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="编辑" placement="top">
                <router-link :to="`editTree/${row.id}/${row.tagsName}`">
                  <i class="el-icon-edit-outline iconLogo"></i>
                </router-link>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="删除" placement="top">
              <span class="operationIcona">
                <!--<i class="el-icon-delete iconLogo" @click="delTag(row.id)"></i>-->
                 <i class="el-icon-delete iconLogo" @click="delTaglabel(row)"></i>
              </span>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
        <!--<element-pagination :pageSize="size" :total="totalnum" @handleCurrentChange="handleCurrentChange"-->
        <!--@sureClick="goPage"></element-pagination>-->
        <element-pagination
          :pageSize="size"
          :currentPage="page+1"
          :total="totalnum"
          @handleSizeChange="handleSizeChange"
          @handleCurrentChange="handleCurrentChange"
        ></element-pagination>
      </div>
      <el-dialog class="creat" title="标签组设置" :visible.sync="shareDialog" width="530px" center
                 :close-on-click-modal="false"
                 @close="closeShare">
        <div class="del-dialog-cnt">
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
            <el-form-item label="标签组名称:" prop="labelName" class="nameOne">
              <el-input
                maxlength="25"
                show-word-limit
                class="zxinp moduleOne"
                size="small"
                placeholder="请输入内容"
                v-model.trim="ruleForm.labelName" style="width: 360px">
              </el-input>
            </el-form-item>
            <el-form-item label="标签组简介:" prop="textarea" class="nameOne">
              <el-input
                class="area"
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 4}"
                placeholder="请输入内容"
                maxlength="100"
                show-word-limit
                v-model.trim="ruleForm.textarea">
              </el-input>
            </el-form-item>
            <!--<el-form-item label="是否共享:" prop="introduction" class="nameOne">-->
            <!--<el-switch-->
            <!--v-model="isShare"-->
            <!--active-text="共享"-->
            <!--inactive-text="不共享">-->
            <!--</el-switch>-->
            <!--</el-form-item>-->
          </el-form>
        </div>
        <div slot="footer" class="dialog-footer device">
          <div>
            <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading" @click="sureShare">确定
            </el-button>
            <el-button size="small" plain class="btn-group" @click="closeShare2">取消</el-button>
          </div>
        </div>
      </el-dialog>
      <el-dialog class="creat" title="创建标签组" :visible.sync="labelcreatDialog" width="530px" center
                 :close-on-click-modal="false"
                 @close="closeCreat">
        <div class="del-dialog-cnt">
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
            <el-form-item label="标签组名称:" prop="tagsName" class="nameOne">
              <el-input
                maxlength="25"
                show-word-limit
                class="zxinp moduleOne"
                size="small"
                placeholder="请输入标签组名称"
                v-model.trim="ruleForm.tagsName" style="width: 360px">
              </el-input>
            </el-form-item>
            <el-form-item label="标签组简介:" prop="synopsis" class="nameOne">
              <el-input
                class="area"
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 4}"
                placeholder="请输入标签组简介"
                maxlength="100"
                show-word-limit
                v-model.trim="ruleForm.synopsis">
              </el-input>
            </el-form-item>
          </el-form>
        </div>
        <div slot="footer" class="dialog-footer device">
          <div>
            <el-button size="small" plain class="btn-group" @click="cancleCreat">取消</el-button>
            <el-button size="small" type="primary" class="queryBtn" :loading="creatsaveLoading" @click="sureCreat">
              确认并编辑
            </el-button>
          </div>
        </div>
      </el-dialog>

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
            <el-button size="small" type="primary" class="queryBtn" :loading="deleteLoading" @click="delTag(delTreeId)">
              确定删除
            </el-button>
            <el-button size="small" @click="cancelDelete">取消</el-button>
          </div>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { mapActions, mapState, mapGetters } from 'vuex'
import ElementPagination from '@/components/ElementPagination'
import { getTagsData, getDtTagGroupData, delTagGroup } from '@/api/tagManage'

export default {
  components: { ElementPagination },
  name: 'tagManage',
  data () {
    return {
      labelId: '',
      isShare: false,
      labelName: '',
      totalnum: 0,
      eq_isShare: '',
      keyword: '',
      page: 0,
      size: 10,
      input2: '',
      tagName: '',
      delTreeId: '',
      Loading: true,
      deleteDialog: false,
      deleteLoading: false,
      saveLoading2: true,
      shareDialog: false,
      saveLoading: false,
      labelcreatDialog: false,
      creatsaveLoading: false,
      percentage: '',
      value1: '',
      textarea: '',
      options: [{
        value: '',
        label: '全部'
      }, {
        value: '0',
        label: '未共享'
      }, {
        value: '1',
        label: '已共享'
      }],
      value: '',
      ztableShowList: [],
      ruleForm: {
        labelName: '',
        textarea: '',
        tagsName: '',
        synopsis: ''
      },
      rules: {
        labelName: [
          { required: true, message: '请填写名称', trigger: 'blur' }
        ],
        tagsName: [
          { required: true, message: '请填写名称', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    customColorMethod (percentage) {
      if (percentage < 25) {
        return '#909399'
      } else if (percentage < 75) {
        return '#e6a23c'
      } else {
        return '#67c23a'
      }
    },
    handleShare (row) {
      this.shareDialog = true
      this.ruleForm.labelName = row.tagsName
      this.ruleForm.textarea = row.synopsis
      // if (row.isShare === 0) {
      //   this.isShare = false
      // } else {
      //   this.isShare = true
      // }
      this.labelId = row.id
      // console.log(this.isShare)
    },
    closeShare () {
      this.shareDialog = false
      this.$refs.ruleForm.resetFields()
    },
    closeShare2 () {
      this.shareDialog = false
      this.$refs.ruleForm.resetFields()
    },
    /**
       * 共享
       * @param status
       * @param item
       * @returns {Promise<void>}
       */
    async shareStatus (status, item) {
      this.ruleForm.labelName = item.tagsName
      this.ruleForm.textarea = item.synopsis
      if (item.isShare === 0) {
        this.isShare = false
      } else {
        this.isShare = true
      }
      this.labelId = item.id
      try {
        await this.$confirm(`是否${item.isShare ? '取消' : ''}共享${item.tagsName}`)
        this.getDtTagGroupData('status')
      } catch (err) {
        console.warn(err)
      }
    },
    // 设置确认
    sureShare () {
      this.saveLoading = true
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          try {
            this.getDtTagGroupData('edit')
            this.shareDialog = false
            this.saveLoading = false
          } catch (e) {
            this.saveLoading = false
            console.log(e)
          }
        } else {
          this.saveLoading = false
        }
      })
    },
    createLabel () {
      this.labelcreatDialog = true
    },
    sureCreat () {
      try {
        this.creatsaveLoading = true
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            try {
              const data = await getDtTagGroupData({
                id: '',
                isNew: true,
                isShare: '',
                synopsis: this.ruleForm.synopsis, // 简介
                tagsName: this.ruleForm.tagsName // 名称
              })
              this.creatsaveLoading = false
              this.closeCreat()
              this.$router.push('/labelcreatTree/' + data.id + '/' + data.tagsName)
            } catch (e) {
              this.creatsaveLoading = false
              console.log(e)
            }
          } else {
            this.creatsaveLoading = false
          }
        })
      } catch (e) {
        this.creatsaveLoading = false
        console.log(e)
      }
    },
    closeCreat () {
      this.$refs.ruleForm.resetFields()
      this.labelcreatDialog = false
    },
    cancleCreat () {
      this.$refs.ruleForm.resetFields()
      this.labelcreatDialog = false
    },
    shareLabel () {
      this.$router.push('/shareLabel')
    },
    // 我的标签列表数据
    async getTagsData () {
      const params = {
        eq_isShare: this.eq_isShare,
        keyword: this.keyword,
        page: this.page,
        size: this.size
      }
      try {
        const data = await getTagsData(params)
        if (data.rows && data.rows.length > 0) {
          this.ztableShowList = data.rows
          this.totalnum = data.total
          data.rows.map(item => {
            if (item.popularityLevel == 0) {
              item.popularityLevel = 0
            }
            if (item.popularityLevel == 1) {
              item.popularityLevel = 25
            }
            if (item.popularityLevel == 2) {
              item.popularityLevel = 50
            }
            if (item.popularityLevel == 3) {
              item.popularityLevel = 75
            }
            if (item.popularityLevel == 4) {
              item.popularityLevel = 100
            }
          })
        } else {
          this.ztableShowList = []
          this.Loading = false
          this.totalnum = 0
        }
      } catch (e) {

      }
    },

    // 共享和设置确认
    async getDtTagGroupData (type) {
      if (type === 'status') {
        let isShare = 0
        if (this.isShare === false) {
          isShare = 1
        } else {
          isShare = 0
        }
        let params = {
          id: this.labelId,
          isNew: false,
          isShare: isShare,
          synopsis: this.ruleForm.textarea,
          tagsName: this.ruleForm.labelName
        }
        try {
          const data = await getDtTagGroupData(params)
          this.getQuireData()
          this.$message({
            message: '修改状态成功！',
            type: 'success'
          })
        } catch (e) {
          this.$message.error('修改状态失败，请重试！')
        }
      } else {
        let params = {
          id: this.labelId,
          isNew: false,
          isShare: '',
          synopsis: this.ruleForm.textarea,
          tagsName: this.ruleForm.labelName
        }
        try {
          const data = await getDtTagGroupData(params)
          this.getQuireData()
          this.$message({
            message: '修改信息成功！',
            type: 'success'
          })
        } catch (e) {
          this.$message.error('修改信息失败，请重试！')
        }
      }
    },
    // 查询
    getQuireData () {
      this.page = 0
      this.eq_isShare = this.value
      this.keyword = this.input2
      this.getTagsData()
    },
    // 点击分页跳转
    handleCurrentChange (page) {
      this.page = page - 1
      this.getTagsData()
    },
    handleSizeChange (size) {
      this.size = size
      this.getTagsData()
    },
    // 点击分页确认
    goPage () {

    },
    delTaglabel (row) {
      this.deleteDialog = true
      this.tagName = row.tagsName
      this.delTreeId = row.id
    },
    closedelete () {
      this.deleteDialog = false
    },
    cancelDelete () {
      this.deleteDialog = false
    },
    // 删除
    async delTag (id) {
      try {
        const data = await delTagGroup(id)
        // console.log(data)
        if (data.message == '删除成功') {
          this.$message({
            message: '删除成功',
            duration: 1000,
            type: 'success'
          })
          this.getTagsData()
          this.deleteDialog = false
        } else {
          this.$message.error(data.message)
          this.deleteDialog = false
        }
      } catch (e) {
        this.deleteDialog = false
      }
    }
  },
  created () {
    this.getTagsData()
  },
  activated () {
    this.getTagsData()
  },
  computed: {},
  watch: {},
  mounted () {
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

  .actionBar {
    float: right;
  }

  .searchBar {
    float: left;
  }

  .tagSelect {
    margin-left: 10px;
    margin-right: 10px;
  }

  .gress {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-around;
  }

  .gressPercentage {
    width: 60%;
  }

  .iconLogo {
    font-size: 18px;
    /*margin-left: 24px;*/
    cursor: pointer;
    margin-right: 16px;
    color: #0486FE;
  }

  .area, .dateInp {
    width: 360px;
  }

  .share-btn {
    display: inline-block;
    font-size: 12px;
    width: 60px;
    line-height: 18px;
    text-align: center;
    border-radius: 10px;
    border: 1px solid #0486FE;
    cursor: pointer;
    padding-top: 1px
  }

  .share {
    background: #0486fe;
    color: white;
  }

  .no-share {
    background: white;
    color: #0486FE;
  }

  .clearfix:after {
    content: "";
    display: block;
    clear: both;
  }
</style>
