<template>
  <el-menu
    default-active="1"
    class="el-menu-vertical-demo aside"
    active-text-color="#ffd04b"
    background-color="#545c64"
    text-color="#fff"
    @openMenu="openMenu" 
    :collapse="isCollapse"
    @open="handleOpen"
    @close="handleClose"
  >
  <el-menu-item index="-1"></el-menu-item>
  <el-menu-item index="0">
    <el-image class="el-image" :src="user.headImage"/>
    <span class="first">{{ user.nickname||user.username }}</span>
  </el-menu-item>
    <el-sub-menu v-for="(item) in menus" :key="item.id" :index="item.id.toString()">
      <template #title>
        <el-icon>
            <component :is="item.icon" />
          </el-icon>
        <span class="menuName">{{ item.name }}</span>
      </template>
      <el-menu-item-group v-for="(child,j) in item.children" :key="child.id.toString()">
        <el-menu-item :index="(item.id-j).toString()" @click="goLink(child)">
            <el-icon>
            <component :is="child.icon" />
          </el-icon>
            <span class="subMenuName">{{ child.name }}</span>
        </el-menu-item>
      </el-menu-item-group>
    </el-sub-menu>
  </el-menu>
</template>

<script setup>
 import emitter from '@/utils/event-bus.js'
 import {onMounted,onBeforeUnmount,ref} from 'vue'   
import { menuList } from '@/api/back/menu';
import {getUserInfo} from '@/api/back/user.js'
import router from '@/router/index';
let menus=ref([])
onMounted(()=>{
  osMenus(); 
})
const user=ref({})
// 获取菜单
const osMenus= ()=>{
  menuList().then(res=>{
    menus.value=res.data;
  })
}
onMounted(()=>{
  //绑定事件
    emitter.on('openMenu',openMenu);
    checkLogin();
})
//检查登录状态
const checkLogin=()=>{
  const token=localStorage.getItem('token');
  if(token&&token!=null){
    getUserInfo().then(res=>{
      if(res&&res.code==200){
        user.value=res.data;
      }else{
        router.replace({name:'front'})
      }
    })
  }else{
    isLogin.value=false;
  }
}
onBeforeUnmount(()=>{
    emitter.all.clear();//解绑所有事件
})
//菜单是否开启状态
const isCollapse = ref(true)
const handleOpen = (key, keyPath) => {
  
}
const handleClose = (key, keyPath) => {
  
}
// 打开侧边栏菜单
const openMenu=(flag)=>{
    isCollapse.value=flag;
}


const goLink=(child)=>{
    if(child&&child.type==='C'&& child.name){
        if(child.name=='前往博客'){
          router.push({name:'front'});
        }else{
          router.push({name:child.name})
        }
        
    }

}



</script>

<style scoped>
.el-image{
  width: 120px;
  margin-top: -45px;
  padding-top: 0px;
}
.first{
    margin-left: 40px;
    font-size: xx-large;
    font-weight: 900;
    text-align: center;
    align-items: center;

}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 300px;
  min-height: 400px;

}

.aside{
  height: 103vh;
  position: absolute;
  left: 0px;
  top:0px;
  bottom: -100px;
  padding-top: 0px;
  padding-right: 0px;
  margin-bottom: -100px;
  padding-bottom: -100px;
  float: left;
  background-color: #545c64;
}
.menuName{
    font-size: x-large;
    font-weight: 700;
    text-align: center;
    color: gold;
}
.subMenuName{
    font-size: large;
    font-weight: 500;
    color: skyblue;
}
</style>