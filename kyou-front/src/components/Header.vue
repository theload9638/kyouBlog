<template>
 <el-header class="top0">
   <div class="top1">
      <div v-if="isLogin">
        <el-dropdown @command="handleCommand">
    <span class="text1">{{ user.nickname||user.username }}, 您好!</span>
    <template #dropdown>
      <!-- 下拉菜单 -->
      <el-dropdown-menu class="menu">
        <!-- 头像 -->
        <el-dropdown-item>
            <img class="round-image" v-if="user.headImage!=undefined" :src="user.headImage">
            <img class="round-image" v-else src="https://img0.baidu.com/it/u=3927518935,2597159060&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1689181200&t=67138d7b6d58ad1b882a8bf595ca14c6">
        </el-dropdown-item>
        <el-dropdown-item command="info"><span class="sub-text">个人资料</span></el-dropdown-item>
        <el-dropdown-item command="signIn"><span class="sub-text">签到</span></el-dropdown-item>
        <el-dropdown-item command="exit" ><span class="sub-text">登出</span></el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
      </div>
      <!-- 游客模式 -->
      <div v-else>
        <span class="welcome">欢迎光临!</span>
        <div class="toLogin" @click="forLogin">
          <el-icon class="avatar"><Avatar /></el-icon>
          <span style="color: black;">登录</span>
        </div>
      </div>
    </div>
<div class="next">
    <div class="left-text"><span class="bigtitle">kyou博客</span></div>
  <div class="middle-text">
<el-menu :default-active="activeIndex" mode="horizontal" @select="handleSelect">
  <el-menu-item index="1">
    <template #title>主页</template>
  </el-menu-item>
  <!-- <el-menu-item index="2">
    <template #title>
      <span>分类</span>
    </template>
  </el-menu-item> -->
  <el-menu-item index="3">
      <template #title>
        <span>留言板</span>
      </template>
    </el-menu-item>
  <el-menu-item index="4">
    <template #title>
      <span>关于</span>
    </template>
  </el-menu-item>
  <el-menu-item index="5">
    <template #title>
      <span>归档</span>
    </template>
  </el-menu-item>
  </el-menu>
  <u-search :config="config" class="search" @submit="submit" v-show="activeIndex=='1'"/>
  </div>
</div>
 </el-header>
</template>
<script setup>
import {getHot} from '@/api/back/article'
import {ref,onMounted} from 'vue'
import router from '@/router';
import emitter from '@/utils/event-bus.js'
import { ElMessage } from 'element-plus'
import {getUserInfo,exit} from '@/api/back/user.js'
const isLogin=ref(false);
const config = ref({
  keywords: [
    'java成神之路', 
    'python爬虫从入门到入狱', 
    'redis主从搭建', 
    '玩转docker', 'c++'], 
  hotSearchList: [] // top10 热门搜索 最多显示10条数据
})

const submit = (val) => {
  if(val==null||val==undefined||val==''){
    return ;
  }
  emitter.emit('searchArticle',val);
}

onMounted(()=>{
  getHot().then(res=>{
    if(res.code==200){
      const e=res.data;
      config.value.hotSearchList=e.articles.map(i=>i.title);
      config.value.keywords=e.tagNames;
    }else{
      ElMessage.error(res.msg);
    }
  })
  checkLogin();
})
const user=ref({
  id:undefined,
  username:'',
  email:'',
  nickname:'',
  headImage:''
})
//检查登录状态
const checkLogin= async()=>{
  const token=localStorage.getItem('token');
  if(token&&token!=null){
   const res=await getUserInfo()
   if(res&&res.code==200){
        user.value=res.data;
        isLogin.value=true
      }else{
        isLogin.value=false;
        localStorage.removeItem('token');
        localStorage.removeItem('routeList');
      }
  }else{
    isLogin.value=false;
  }
}

const forLogin=()=>{
 router.push({name:'login'})
}
//激活的导航栏菜单
const activeIndex=ref('1')
//导航栏点击执行的函数
const handleCommand = (command) => {
    if(command=='exit'){
      exit().then(res=>{
        if(res.code==200){
          localStorage.removeItem('token');
          localStorage.removeItem('routeList');
        window.location.reload();
        }else{
          ElMessage(res.msg);
        }
      })
    }else if(command=='info'){
      // router.push('/menuBoard/dashboard')
      router.push('/back')
    }else if(command=='signIn'){
      router.push("/sign");
    }

}
const handleSelect=(index)=>{
  emitter.emit('changeMenu',index)
  activeIndex.value=index;
}
</script>

<style scoped>
.search{
  margin-left: 20px;width: 400px;
  font-size: x-large;
}
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
.signBoard{
  width: 110px;
  height: 70px;
}
.dialog-footer button:first-child {
  margin-right: 10px;
}
/*  */
.bigtitle{
  color:gold;
}
.top0 {
  position:fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 999;
  padding: 0 !important;
  height: 140px !important;
}
*{
    box-sizing: unset; /* 强制去除 box-sizing 属性 */
}
.top1 {
  /* position: absolute; */
  /* float: left; */
  top:0px;
  width: 100%;
  height: 40px;
  background-color: beige;
}
.text1{
    cursor: pointer;
    display: inline-block;
    font-size: x-large;
    font-weight: 700;
    color: black;
    transform: translate(300px, 4px);
}
.round-image {
  border-radius: 50%;
  width:180px;
  height: 180px;
}
 
.sub-text{
    margin: auto;
    font-size: large;
    font-weight: 600;
    
}
.next{
    /* position: fixed; */
    background-color: black;
    width:100%;
    height: 100px;
    /* top:40px; */
}
.left-text{

    font-size: 40px;
    font-weight: 1100;
    position: absolute;
    height: 80px;
    text-align: center;
    transform: translate(300px,12px);
}
.middle-text{
    width: 528px;
    height: 99px;
    color: black;
    transform: translate(700px,0px);
}
.middle-text .el-menu-item{
    font-size: x-large ;
}
.middle-text .el-menu{
    position: relative;
    height: 100px;

}
.welcome{
  color: black;
  font-size: x-large;
  font-weight: 400;
  position:absolute;
  margin-left: 200px;
}
.toLogin{
  font-size: x-large; 
  margin-left: 350px;
  font-weight: 400;
  position:absolute;
  cursor: pointer;
}
.toLogin :hover{
  color:lightblue;
}
.avatar{
  color: black;
  top:4px;
  margin-right: 6px;
}
</style>