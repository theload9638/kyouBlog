<template>
  <el-scrollbar height="400px">
   <div v-if="listV.length>0">
    <p v-for="i in listV" :key="i.id" class="scrollbar-demo-item">
        <span>
          <el-avatar :size="60" class="avtor" @click="goHomeLink(i.fansId)" :src="i.headImage"/>
       </span>
       <span class="aname" @click="goHomeLink(i.fansId)">&nbsp;&nbsp;&nbsp;{{ i.nickname||i.username }}</span>
      </p>
   </div>
   <div v-else>
    <h1>暂无粉丝</h1>
   </div>
  </el-scrollbar>
</template>

<script setup>
import { ref,onMounted } from 'vue'
import {getFansList} from '@/api/back/user'
import { ElMessage } from 'element-plus'
import router from '@/router';
const goHomeLink=(id)=>{
  router.push({path:'/userHome',query:{'id':id}});
}
 const listV=ref([])
onMounted(()=>{
    getData();
})
const getData=()=>{
    getFansList().then(res=>{
        if(res.code==200){
            listV.value=res.data;
        }else{
            ElMessage.error(res.msg);
        }
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