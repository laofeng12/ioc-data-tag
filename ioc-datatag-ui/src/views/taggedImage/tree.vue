<template>
  <!--<div class="app-container">创建标签</div>-->
  <div class="app-container">
    <treeLabel></treeLabel>
    <div style="text-align:center">
      <el-button @click="goback">返回</el-button>
      <el-button type="primary" @click="handleSave">保存标签组</el-button>
    </div>
    <el-dialog class="creat" title="保存标签组" :visible.sync="saveDialog" width="530px" center :close-on-click-modal="false"
               @close="closeSaveas">
      <div class="del-dialog-cnt">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
          <el-form-item label="标签组名称:" prop="name" class="nameOne">
            <el-input v-model="ruleForm.name"></el-input>
          </el-form-item>
          <el-form-item label="标签组简介:" prop="textarea2" class="nameOne">
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
          <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading">确认保存</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import treeLabel from '@/components/treeLabel/treeLabel'

  export default {
    components: {
      treeLabel
    },
    name: 'tree',
    data() {
      return {
        saveDialog:false,
        saveLoading:false,
        ruleForm:{
          name:'',
          textarea2:''
        },
        rules: {
          name: [
            {required: true, message: '请填写',trigger: 'blur'}
          ]
        },
      }
    },
    mounted() {

    },
    methods: {
      closeSaveas(){
        this.saveDialog = false
      },
      handleSave(){
        this.saveDialog = true
      },
      goback(){
        this.$router.push('lableImage')
      }
    }
  }
</script>

<style scoped>

</style>
