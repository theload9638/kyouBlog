<template>
   <el-form
    ref="ruleFormRef"
    :model="info"
    :rules="rules"
    label-width="120px"
    class="demo-ruleForm"
    size="large"
    status-icon
  >
    <el-form-item label="原密码" prop="curPwd">
      <el-input class="pwd" v-model="info.curPwd" type="password" />
    </el-form-item>
    <el-form-item label="新密码" prop="nextPwd">
      <el-input  class="pwd"  v-model="info.nextPwd"  type="password"/>
    </el-form-item>
    <el-form-item label="确认密码" prop="checkPwd">
      <el-input class="pwd" v-model="info.checkPwd" type="password" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm(ruleFormRef)">
        保存
      </el-button>
      <el-button @click="resetForm(ruleFormRef)">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script >
import { defineComponent,reactive,ref } from 'vue'
import { ElMessage } from 'element-plus'
import {updPwd} from '@/api/back/user.js'
export default defineComponent({
    name:"UpdatePwd",
    setup() {
        const info=ref({
            curPwd:'',
            nextPwd:'',
            checkPwd:''
        })
        const ruleFormRef=ref(null);
        //重置
        const resetForm = (formEl) => {
  if (!formEl) return
  formEl.resetFields()
}
//保存
const submitForm = async (formEl) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
        if (!/^\w{6,16}$/.test(info.value.curPwd)) {
            ElMessage.error('密码长度在6到16位')
        return;
      } 
      if (!/^\w{6,16}$/.test(info.value.nextPwd) &&
       !/^\w{6,16}$/.test(info.value.checkPwd) &&info.value.checkPwd!=info.value.nextPwd) {
            ElMessage.error('密码长度在6到16位，密码不一致')
        return;
      } 
      updPwd(info.value).then(res=>{
        ElMessage.error(res.msg);
      })
    } else {
    }
  })
}
//
const rules = reactive({
    curPwd: [
            { required: true, message: '请输入原密码', trigger: 'blur' },
            { min: 6, max: 16, message: '长度在6到16个字符', trigger: 'blur' }
          ],
    nextPwd: [
            { required: true, message: '请输入新密码', trigger: 'blur' },
            { min: 6, max: 16, message: '长度在6到16个字符', trigger: 'blur' }
          ],
    checkPwd: [
            { required: true, message: '请输入确认密码', trigger: 'blur' },
            { min: 6, max: 16, message: '长度在6到16个字符', trigger: 'blur' }
          ],
})
        return {
            ruleFormRef,resetForm,submitForm,
            info,rules

        }
    },
})
</script>


<style scoped>
.pwd{
    width: 600px;
}
.demo-ruleForm{
    background-color: lightgoldenrodyellow;
}
</style>