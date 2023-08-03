<template>
  <div @keydown.enter="onSubmit"> 
    <div class="background"></div><!-- 背景容器 -->
    <div class="container"><!-- 登录表单 -->
      <h1>Welcome</h1>
      <form>
        <input type="text" v-model="username" placeholder="用户名或电子邮箱" @blur="checkName" autocomplete="off">
        <span class="nameErr">{{ nameErr }}</span>
        <input type="password" v-model="password"  placeholder="密码" @blur="checkPwd" autocomplete="off">
        <span class="pwdErr">{{ pwdErr }}</span>
        <div class="verifyContainer">
          <input type="text" v-model="verifyCode" name="verifyCode" placeholder="输入验证码" autocomplete="off">
          <img ref="image" src="#" @click="refreshVerifyCode()" class="verifyCodeImg"
               :class="{verifyCodeImgDisabled: verifyCodeDisable }"/>
        </div>
        <span class="codeErr">{{ codeErr }}</span>
        <el-button type="primary" :disabled="isBand" @click="onSubmit" class="loginButton" >Log in</el-button>
        <!-- <el-checkbox label="记住我" class="rememberMe" size="large" v-model="rememberMe" border="border"></el-checkbox> -->
         <span class="loginErr">{{ loginErr }}</span>            
      </form>
      <div class="register">
        <a class="backHome" @click="backFront">返回kyou博客  </a>|
        <a class="forgotPwd">忘记密码?  </a>| 
        <a @click="toRegister" class="registerA">  立刻注册</a>
      </div>
    </div>
  </div>
</template>

<script >
import { defineComponent,ref,
  computed,onMounted,onBeforeUnmount 
} from 'vue'
import {getLoginCode,login} from '@/api/back/user.js'
import router from '@/router';
import {getYoukeId} from '@/hooks/header.js'
export default defineComponent({
  name: 'Login',
  setup(props,context) {
    const username=ref('admin');
    const password=ref('123456');
    const verifyCode=ref('');
    const rememberMe=ref(false);

    //用户名错误
    const nameErr=ref('')
    //密码错误
    const pwdErr=ref('');
    //验证码错误
    const codeErr=ref('')
    //用户名校验
    const checkName=()=>{
      if(username.value==null||username.value==undefined){
        nameErr.value='账号不能为空';
        return 
      }
      if(!username.value.includes('@')){
        //是用户名
        if(!/^\w{4,16}$/.test(username.value)){
          nameErr.value="请输入5到16位数字的用户名！";
          return;
        }
      }else{
        //是邮箱
        if(!/^\w{5,16}@.{2,8}\.[a-zA-Z]{1,4}$/.test(username.value)){
          nameErr.value="邮箱格式错误";
          return;
        }

      }
        nameErr.value='';
    }
    //密码校验
    const checkPwd=()=>{
      if (!/^\w{6,16}$/.test(password.value)) {
        pwdErr.value=("请输入6-16位包含数字字母的复杂的密码");
        return;
      } 
      pwdErr.value='';
    }
    //验证码校验
    const checkCode=()=>{
      if(verifyCode.value.length==4&&
      /^\w{4}$/.test(verifyCode.value) && 
      verifyCode.value.toUpperCase()== verify.value){
        codeErr.value='';
      }else{
        codeErr.value='验证码错误'
      }
    }
    //图片标签
    const image=ref(null);
    const loginErr=ref('');
    // 验证码
    const verify=ref('');
      // 用户登录信息
 // 控制验证码是否禁用
    const verifyCodeDisable=ref(false);
      // 记录最后一次验证码更新的时间
    const lastRefreshVerifyCodeTime=ref(0);
    //延迟器
    const timer=ref(-1);
    const key=ref('');
    //返回前端博客
    const backFront=()=>{
      router.push({name:'front'})
    }
     // 验证码刷新
 const refreshVerifyCode=()=>{
  // 如果验证码已被禁用，不能再次刷新验证码
        if (verifyCodeDisable.value) { return;}
        // 禁用验证码
        verifyCodeDisable.value= true; 
        // 记录最后一次刷新验证码的时间
        lastRefreshVerifyCodeTime.value = new Date().getTime(); 
        getLoginCode(key.value).then(res=>{
          verify.value=res.data.code;
          key.value=res.data.key;
          image.value.src=res.data.image;
        })
      };
//是否禁用登录的禁用
    const isBand=computed(()=>{
      return !(username.value && 
      password.value && 
      verifyCode.value)&&(codeErr=='')&&(pwdErr=='')&&(nameErr=='')
    })
    onMounted(()=>{
      reset()
      const k=getYoukeId();
      key.value=k;
       //禁用验证码5秒
      timer.value = setInterval(() => {
      // 如果已经过了5s，则恢复验证码
      if ((new Date().getTime() - lastRefreshVerifyCodeTime.value) >= 5000) { 
        verifyCodeDisable.value = false;
      }
    },1000);
    //用户进来刷新验证码
    refreshVerifyCode()
    })
//清除定时器
onBeforeUnmount(()=>{
//清除定时器
clearInterval(timer.value)
})
//注册界面
const toRegister=()=> {
      router.replace({name:'register'});
    };
 //重置信息
 const reset=()=>{
          username.value= 'admin';
          password.value= '123456';
          verifyCode.value= '';
          rememberMe.value= false;
        
      }    
//登录验证    
const onSubmit= async ()=>  {
 //用户名验证
 checkName();
 checkPwd();
 checkCode();
 
if (nameErr.value==''&&pwdErr.value==''&&codeErr.value==''
&&username.value!=null
&&password.value!=null
&&verifyCode.value!=null) {
    const userInfo={
      username: username.value,
        password: password.value,
        verifyCode: verifyCode.value,
        remember: rememberMe.value,
        key:key.value,
    }
    const r = await login(userInfo);
      if (r.code === 200) {
        loginErr.value='';
        setTimeout(()=>{
          window.location='/home'
         },1000)
      }else{
        //登录失败
        loginErr.value='用户名或密码错误'
      }
      //清空数据
      reset();
   
  }
}  

  return {
  image,verify,verifyCodeDisable,
  lastRefreshVerifyCodeTime,isBand,refreshVerifyCode,
  toRegister,refreshVerifyCode,reset,onSubmit,
  nameErr,checkName,checkPwd,pwdErr,codeErr,loginErr,
  backFront,username,password,verifyCode,rememberMe
  
}


  }
})

</script>
  
<style scoped>
.backHome{
  font-size: large;
  font-weight: 500;
  cursor: pointer;
}
.loginErr{
  color:red;
  margin-bottom: 0px;
  position: fixed;
  bottom: 249px;
  font-size: large;
  font-weight: 400;
  left: 1230px;
}
.codeErr{
  color:red;
  padding-top:0px;
  padding-bottom: 0px;
  margin-bottom: 0px;
  margin-top:0px;
  position: fixed;
  font-weight: 500;
  font-size: large;
  top:440px;
  left:1223px;
}
.pwdErr{
  color:red;
  padding-top:0px;
  padding-bottom: 0px;
  margin-bottom: 0px;
  margin-top:0px;
  position: fixed;
  font-weight: 500;
  font-size: large;
  top:360px;
}
.nameErr{
  color:red;
  padding-top:0px;
  padding-bottom: 0px;
  margin-bottom: 0px;
  margin-top:0px;
  top:280px;
  font-weight: 500;
  font-size: large;
  position: fixed;
}
.loginButton{
  width: 50px;
  margin-left: 5px;
  margin-top:6px;
}
.rememberMe {
  float: right;
  margin-top: 16px;
  margin-right:22px; 
  width: 80px; 
  height: 30px;
  align-items: center;
}
.forgotPwd{
  font-size: large;
  font-weight: 400;
  cursor: pointer;
}

.registerA:hover,.forgotPwd:hover {
  color: lightblue;
  
}
.registerA{
  font-size: large;
  font-weight: 400;
  cursor: pointer;
}

* {
  box-sizing: border-box;
}

.background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url(../assets/img1.jpg);
  background-repeat: no-repeat;
  background-size: cover;
  z-index: -1;
}

.container {
  position: relative;
  left:-100px;
  top:-60px;
  z-index: 1;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;;
  align-items: center;
}

.verifyContainer {
  display: inline-flex; /* 设置成行内弹性盒子 */
  align-items: center; /* 垂直对齐 */
}

.verifyCodeImgDisabled {
  pointer-events: none;
  opacity: 0.5;
}

input[type="text"],
input[type="password"] {
  display: inline-block; /* 设置成行内块级元素 */
  width: 140px;
  height: 40px;
  margin-right: 10px; /* 添加元素之间的距离 */
}

.verifyCodeImg {
  display: inline-block;
  vertical-align: middle;
  height: 50px;
  width: 170px;
  margin-right: 7px;
  cursor: pointer; /* 鼠标移动到指针图像上时改变光标形状 */
}


h1 {
  margin-bottom: 30px;
  font-size: 60px;
  text-shadow: 0 0 10px rgba(0, 0, 0, 0.2),
  0 5px 10px rgba(0, 0, 0, 0.5);
  color: #fff;
  text-transform: uppercase;
}

form {
  width: 470px;
  height: 380px;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 0 20px rgba(255, 255, 255, 0.5);
  background-color: rgba(255, 255, 255, 0.15);
}

label {
  display: block;
  margin-bottom: 10px;
  font-size: 18px;
  color: #fff;
}

input[type=text],
input[type=password] {
    display: block;
    width: 100%;
    height: 40px;
    padding: 10px;
    margin-top: 11px;
    margin-bottom: 30px;
    border: none;
    border-radius: 5px;
    font-size: 16px;
    background-color: rgba(255, 255, 255, 0.7);
    color: #333;
}

button[type=button] {
  display: inline-block;
  width: 65%;
  height: 50px;
  border: none;
  border-radius: 5px;
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease-in-out;
  background-color: #00d2d3;
  color: #fff;
}

button[type=button]:hover {
  background-color: #ff846c;
}

a {
  color: #fff;
  text-decoration: none;
}

.register {
  margin-top: 20px;
  font-size: 14px;
  font-weight: bold;
  letter-spacing: 1px;
  text-transform: uppercase;
  color: #fff;
}
</style>