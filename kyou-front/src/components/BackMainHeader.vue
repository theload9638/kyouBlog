<template>
  <div class="mainHeader">
    <span class="title">{{ title }}</span>
    <el-breadcrumb :separator-icon="ArrowRight" class="bread">
    <el-breadcrumb-item class="bi" :to="{ path: '/menuBoard/dashboard' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item class="bi">{{ menuName }}</el-breadcrumb-item>
    <el-breadcrumb-item class="bi">{{ title }}</el-breadcrumb-item>
  </el-breadcrumb>
  </div>
</template>

<script>
import { defineComponent,onMounted,ref,computed } from 'vue'
import { ArrowRight } from '@element-plus/icons-vue'
import router from '@/router/index'
import {findMenuName} from '@/hooks/header'
export default defineComponent({
    name:'BackMainHeader',
    setup(props,context) {
        const menuName=ref('');
        onMounted(()=>{
            setMenuName();
        })
        const setMenuName=()=>{
            menuName.value=findMenuName(router.currentRoute.value.path);
        }
        const title=computed(()=>{
            return router.currentRoute.value.name;
        })

        return {
            title,ArrowRight,menuName
        }
    },
})
</script>


<style scoped>
.mainHeader{
    background-color: #E9EEF3;
    margin-right: 0px;
    padding-right: 0px;
    right: 0px;
    width: 100%;
    height: 100px;
    
}
.title{
    font-size: xx-large;
    position:absolute;
    font-weight: 800;
    left: 20px;
    top:-30px;
}
.bread{
  position:absolute;
  right: 30px;
  top:30px;
}
.bi{
    font-size: large;
    font-weight: 800;
}
</style>