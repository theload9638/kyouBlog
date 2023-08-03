<template>
<el-scrollbar height="700px">
   <el-timeline>
    <el-timeline-item v-for="(i,ind) in dataList" :key="ind" :timestamp="ind" size="large" placement="top">
      <el-card v-for="(j,index) in i" :key="index">
        <div class="blog-card-header" @click="goArticleInfo(j.id)">{{ j.title }}</div>
          <div class="blog-card-content">
            <el-image style="width: 100px; height: 100px" :src="j.thumbnail" :zoom-rate="1.2" :initial-index="1" fit="cover" />
       <div class="acsum">{{ j.summary }}</div>
          </div>
      </el-card>
    </el-timeline-item>
  </el-timeline>
</el-scrollbar>
</template>

<script setup>
  import { ElMessage } from 'element-plus'
  import {
    onMounted,ref
} from 'vue'  
import router from '@/router';
import {getTimeLineArticles} from '@/api/back/article'
onMounted(()=>{
    getTimeLineArticles().then(res=>{
        if(res.code==200){
            dataList.value=res.data;
        }else{
            ElMessage.error(res.msg);
        }
    })   
})
//显示文章详情页
const goArticleInfo=(id)=>{
  router.push({path:'/article'+id});
}      
const dataList =ref([]);


</script>

<style scoped>
.blog-card-content {
  margin-top: 10px;
  font-size: 16px;
}

.acsum{
  float:right;font-size: x-large;font-weight: 700;
}
.blog-card-header {
  font-size: 18px;
  font-weight: 700;
  cursor: pointer;
}
</style>