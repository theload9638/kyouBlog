<template>
  <el-header class="header"   >
    <el-icon size="70" @click="openAside" class="controlMenu"><Menu /></el-icon>
    <a class="text1" @click="toHome">博客首页</a>
  </el-header>
</template>

<script setup>
 import emitter from '@/utils/event-bus.js'
 import router from '@/router/index'
  import {ref} from 'vue';
  let flag=ref(false);
 const openAside=()=>{
    flag.value=!flag.value;
    emitter.emit('openMenu',flag.value);
 }
 //跳转到博客首页
 const toHome=()=>{
  router.push({name:'front'})
 }

</script>

<style lang="css" scoped>
.controlMenu{
    cursor: pointer;
    position: absolute;
    left:305px;
    height: 60px;
    width: 60px;
}

.text1{
    position: absolute;
    float:right;
    right: 240px;
    font-size: x-large;
    font-weight: 700;
    text-decoration: none;
    cursor: pointer;
    color: black;
}
.header{
  position: absolute;
  top: 0px;
  left:0px;
  margin-left: 0px;
  margin-right: 0px;
  padding-right: 0px;
  opacity: 0.5;
  padding-left: 0px;
  width: 100%;
  background-color: white;
}

</style>