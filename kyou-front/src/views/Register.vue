<template>
    <el-form class="form-container" ref="ruleFormRef" :rules="rules" :model="form">
      <!-- 用户名输入项 -->
      <el-form-item label="用户名" prop="username" class="in">
        <el-input v-model.trim="form.username" clearable placeholder="请输入用户名"></el-input>
      </el-form-item>
  
      <!-- 密码输入项 -->
      <el-form-item label="密码" prop="password" class="in">
        <el-input type="password" v-model.trim="form.password" clearable placeholder="请输入密码"></el-input>
      </el-form-item>
  
      <el-form-item label="邮箱" prop="email" class="in">
        <el-input v-model.trim="form.email" clearable placeholder="请输入邮箱账号"></el-input>
      </el-form-item>
      <el-form-item class="submit">
        <el-button type="primary" size="large" @click.prevent="submitForm(ruleFormRef)">提交</el-button>
        <el-button type="warning" size="large" class="reset" @click.prevent="resetForm(ruleFormRef)">重置</el-button>
      </el-form-item>
      <div class="info">
        <a class="backHome" @click="backFront">回到kyou博客</a>
      </div>
    </el-form>
  </template>
  <script>
  import { defineComponent,ref } from 'vue'
  import router from '@/router'
  import { ElMessage } from 'element-plus'
  import {register} from '@/api/back/user.js'
  export default defineComponent({
    name:"Register",
    setup() {
      const backFront=()=>{
        router.push({name:"front"})
      }
      const form=ref({
          username: '',
          password: '',
          email: ''
        })
       const rules= ref({
          // 用户名验证规则
          username: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
          ],
          // 密码验证规则
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            { min: 6, max: 16, message: '长度在6到16个字符', trigger: 'blur' }
          ],
          email: [
            { required: true, message: '请输入邮箱账号', trigger: 'blur' },
            {
              pattern: /^\w{5,16}@.{2,8}\.[a-zA-Z]{1,4}$/,
              message: '邮箱格式不正确',
              trigger: 'blur'
            }
          ]
        }  )
const ruleFormRef=ref(null);
const submitForm = async (formEl) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      if(!/^\w{4,16}$/.test(form.value.username)){
        ElMessage('请输入4到16位字母数字的用户名！');
        return;
      }
      if(!/^\w{6,16}$/.test(form.value.password)){
        ElMessage('请输入6-16位包含数字字母的复杂的密码！');
        return;
      }
      if(!/^\w{5,16}@.{2,8}\.[a-zA-Z]{1,4}$/.test(form.value.email)){
        ElMessage('邮箱格式错误！');
        return;
      }
      //开始注册
      register(form.value).then(res=>{
        if(res.code==200){
          ElMessage('注册成功！');
          router.replace({name:'login'});
        }else{
          ElMessage(res.msg);
        }
      })

    } else {
      ElMessage('输入参数不正确');
    }
  })
}
const resetForm = (formEl) => {
  if (!formEl) return
  formEl.resetFields()
}
     return {
      form,rules,ruleFormRef,resetForm,
      backFront,submitForm,
     }   
    },
  })
  </script>
  
  <style lang="css" scoped>
  .form-container {
    background-color: antiquewhite;
    max-width: 600px;
    width: 500px;
    position: fixed;
    left: 38%;
    top:30%;
    height: 400px;
    margin: auto;
    padding: 75px;
    border-radius: 14px;
    /* background: linear-gradient(to bottom, #f7f8fb, #f1f2f6); */
    box-shadow:
     -1px -1px 0 #e1e1e1,
      2px 2px 0 #ffffff,
      3px 3px 7px rgba(0,0,0,.25);
  
  }
  .in{
    width: 360px;
    height: 50px;
    
  }
  .submit{
    margin-left: 100px;
    margin-top:50px;
  }
  .reset{
    margin-left: 51px;
  }
  .info{
    width: 100px;
    position: absolute;
    margin-left: -40px;
  }
  .backHome{
    font-size: medium;
    font-weight: 500;
    color: black;
    cursor: pointer;
    margin-left:-10px;
  }
  .backHome:hover{
    color:black;
  }
  </style>