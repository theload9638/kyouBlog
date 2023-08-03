<template>
   <el-scrollbar height="400px">
   <div v-if="listV.length>0">
    <p v-for="i in listV" :key="i.id" class="scrollbar-demo-item">
        <span>
          <el-avatar :size="60" @click="goHomeLink(i.curId)" class="avtor" :src="i.headImage"/>
       </span>
       <span class="aname" @click="goHomeLink(i.curId)">&nbsp;&nbsp;&nbsp;{{ i.nickname||i.username }}</span>
       <span>
        <el-button class="flu" type="primary" style="font-size:large;" @click="followUser(i.curId,i.fansId)"><el-icon><StarFilled /></el-icon>取消关注</el-button>
       </span>
      </p>
   </div>
   <div v-else>
    <h1>暂无关注</h1>
   </div>
  </el-scrollbar>
</template>

<script setup>
import { ElMessage } from 'element-plus'

import { ref,onMounted } from 'vue'
import {getFollowList,follow} from '@/api/back/user'
import router from '@/router';
const goHomeLink=(id)=>{
    router.push({path:'/userHome'+id})
}
onMounted(()=>{
  getList();
})
const getList=()=>{
  getFollowList().then(res=>{
    if(res.code==200){
      listV.value=res.data;
    }else{
      ElMessage.error(res.msg);
    }  
  })
}
const listV=ref([]);
const followUser=(curId,fansId)=>{
  const data={
    curId:curId,
    fansId:fansId
  }
  follow(data,0).then(res=>{
    getList();
  })
}
</script>


<style scoped>
.flu{
  margin-left: 30px;
}
.aname{
  cursor: pointer;font-size: large;
}
.avtor{
  margin-top: 68px;cursor: pointer;
}
.scrollbar-demo-item {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 70px;
  margin: 20px;
  text-align: center;
  border-radius: 4px;
  background: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}


</style>