<template>
  <BackMainHeader/>
   <el-row :gutter="24" style="margin-top: 10px;">
     <el-col :span="12" style="height: 201px;">
      <!-- 文章 -->
      <el-card style="width: 50%;background-color: lightblue;height: 200px;">
        <div style="display: flex;justify-content: space-between;align-items: center;">
        <em style="font-size: x-large;">发布的文章：<span style="font-size: xx-large;font-weight: 600;">{{showData.articlePublished}}</span></em>
        <span style="width: 70px;height: 70px;font-size: x-large;cursor: pointer;margin-bottom: -50px;" @click="goLink('/os/blog')">more</span>
      </div>
      </el-card>   
      <!-- 评论 -->
      <el-card style="width: 50%;background-color: lightgreen;height: 200px;top:-200px;margin-left: 51%;position: relative;">
        <div style="display: flex;justify-content: space-between;align-items: center;">
        <em style="font-size: x-large;">收到的评论数：<span style="font-size: xx-large;font-weight: 600;">{{showData.commentCount}}</span></em>
        <span style="width: 70px;height: 70px;font-size: x-large;cursor: pointer;margin-bottom: -50px;"  @click="goLink('os/comments')">more</span>
      </div>
      </el-card>      
    </el-col>
    <!-- 上右边 -->
     <el-col :span="12" style="height: 201px;">
      <!-- 标签 -->
        <el-card style="width: 50%;background-color: lightslategray;height: 200px;">
          <div style="display: flex;justify-content: space-between;align-items: center;">
        <em style="font-size: x-large;position: relative;">标签总数：<span style="font-size: xx-large;font-weight: 600;">{{showData.tagNum}}</span></em>
        <span style="font-size: x-large;cursor: pointer;margin-bottom: -150px;" @click="goLink('/os/tags')">more</span>
      </div>
      </el-card>  
      <!-- 加入天数 -->
      <el-card style="width: 50%;background-color: lightcoral;height: 200px;margin-left: 51%;position: relative;top:-200px;">
        <div style="display: flex;justify-content: space-between;align-items: center;">
        <em style="font-size: x-large;">加入天数：<span style="font-size: xx-large;font-weight: 600;">{{showData.joinDays}}</span></em>
      </div>
      </el-card>   
    </el-col>
   </el-row> 
   <!-- 下边 -->
   <el-row :gutter="24">
     <el-col :span="12">
      <el-card style="width: 100%;height: 400px;background-color: lightpink;">
        <template #header><span style="display: flex;justify-content: space-between;font-size: xx-large;margin-top: -80px;position: absolute;">排行榜</span></template>
        <el-table :data="showData.rankList" style="width: 100%;background-color: lightpink;">
      <el-table-column prop="rankId">
        <template #header="title"><span class="rankTitle">序号</span></template>
      </el-table-column>
      <el-table-column prop="username">
        <template #header="title"><span class="rankTitle">用户名</span></template>
      </el-table-column>
      <el-table-column prop="articleNum">
        <template #header="title"><span class="rankTitle">文章数</span></template>
      </el-table-column>
      <el-table-column prop="registerTime">
        <template #header="title"><span class="rankTitle">注册时间</span></template>
      </el-table-column>
     </el-table>
      </el-card>
    </el-col>
    <el-col :span="12">
      <el-card style="width: 100%;height: 400px;background-color: lightcyan;">
        <template #header><span style="display: flex;justify-content: space-between;font-size: xx-large;margin-top: -80px;position: absolute;">新注册用户</span></template>
          <span 
          style="width: 50px;"
          v-for="(user,index) in showData.users" 
         :key="user.id">
         <span style="margin-left: 100px;">
          <el-avatar :size="70" :src="user.headImage" style="cursor: pointer;"/>
            <span class="uname" @click="goHomeLink(user.id)">{{ user.username }}</span>
            <em class="urt">{{ user.createTime }}</em>
         </span>
      </span> 
      </el-card>
    </el-col>
   </el-row>
</template>

<script>
import { defineComponent,ref,onMounted } from 'vue'
import BackMainHeader from '@/components/BackMainHeader.vue'
import router from '@/router'
import {backInfo} from '@/api/back/index.js'

export default defineComponent({
    name:'index',
    components:{
        BackMainHeader,
    },
    setup() {
      //获取后台数据
      const getInfo=()=>{
        backInfo().then(res=>{
          showData.value=res.data;
        })
      }
      onMounted(()=>{
        getInfo();
      })
      const goHomeLink=(id)=>{
        router.push({path:'/userHome',query:{'id':id}});
      }
      const showData=ref({})
        const goLink=(path)=>{
          router.push({path:path})
        }

        return{
          goLink,showData,goHomeLink
        }
    },
})
</script>


<style scoped>
.rankTitle{
  font-size: x-large;
  font-weight: 1900;
}
.uname{
  font-size: large;
  position: absolute;
  margin-top: 20px;
  margin-left: -78px;
  height: 60px;
  width: 60px;
  color: lightcoral;
  cursor: pointer;
}
.urt{
  position: absolute;
  margin-top: 41px;
  font-size: medium;
  margin-left: -124px;
}
</style>