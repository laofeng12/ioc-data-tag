<template>
  <el-dialog class="creat" title="数据打标" :visible.sync="setTagsDialog" width="630px" center :modal-append-to-body="false"
             :close-on-click-modal="false"
             @close="close">
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="选择标签组" prop="region">
        <el-col :span="11">
          <el-select v-model="ruleForm.tagTeam" filterable placeholder="请选择" size="small">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="11">
          <!--<el-link type="primary" :underline="false">编辑标签组</el-link>-->
          <el-button type="text">编辑标签组</el-button>
        </el-col>
      </el-form-item>
      <el-form-item label="选择标签层" prop="region">
        <el-col :span="11">
          <el-select v-model="ruleForm.tagTeam" filterable placeholder="请选择" size="small">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-col>
      </el-form-item>
      <el-form-item label="打标设置" prop="region">
        <el-col :span="11">
          <el-select v-model="ruleForm.tagTeam" filterable placeholder="请选择" size="small">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="11">
          <el-row>
            <el-button type="primary" size="small">自动打标</el-button>
            <el-button type="primary" size="small">人工打标</el-button>
          </el-row>
        </el-col>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer device">
      <el-button size="small" type="primary" class="queryBtn" :loading="saveLoading">确认打标</el-button>
    </div>
  </el-dialog>

</template>

<script>
  export default {
    name: "MarkDialog"
  }
</script>

<style scoped>

</style>
