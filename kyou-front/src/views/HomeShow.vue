<template>
    <el-row class="row">
      <el-col :span="16">
        <el-card v-for="post in articleList" :key="post.id" class="blog-card">
          <!-- 标题 -->
          <div slot="header" class="blog-card-header" @click="goArticleInfo(post.id)">{{ post.title }}</div>
          <div class="blog-card-content">
            <!-- 文章图片 -->
            <el-image style="width: 100px; height: 100px" :src="post.thumbnail" :zoom-rate="1.2" :initial-index="1" fit="cover" />
    <!-- 文章简介 -->
       <div class="acsum">{{ post.summary }}</div>
          </div>
          <!-- 作者的文章基本信息 -->
          <div slot="footer" class="blog-card-footer">
            <el-avatar :size="50" :src="post.userVo.headImage" @click="goUserHome(post.userId)" style="cursor: pointer;"/>
            <span class="auname" @click="goUserHome(post.userId)">{{ post.userVo.username }}</span>
            <span class="actime">{{ post.createTime }}</span>
            <span class="aview"><el-icon><View /></el-icon> {{ post.viewCount }}</span>
          </div>
        </el-card>
        <!-- 分页 -->
        <el-pagination
    :small="false" background
    v-model:current-page="params.pageNum"
    v-model:page-size="params.pageSize"
    layout="prev, pager, next"  :total="total"
    @size-change="handleSizeChange"  />
      </el-col>
      <el-col :span="8">
        <el-row class="second-col-content" style="height: 100%;margin-left: 30px;">
           <!-- 第一张卡片：网站简介 -->
          <el-col :span="24" class="vertical-layout">
            <el-card class="box-card">
    <template #header>
      <!-- 第一行头部 -->
      <div class="right-card-header">
        <el-icon class="cardIcon1"><Memo/></el-icon><span class="cardTitle1">| 关于本站</span>
      </div>
    </template>
    <div>
      <el-row :gutter="24">
        <el-col :span="12">
          <img src="../assets/cardTitle.jpg" class="card1-img"/>
        </el-col>
        <el-col :span="12" class="first-con">{{ introduce }}</el-col>
      </el-row>    
    </div>
    <div>
      <el-row :gutter="24">
    <el-col :span="12">
      <div class="card1-bottom1">
        <div class="card1-bottom1-title">文章数量：{{showData.articleNum||0}}</div>
      </div>
    </el-col>
    <el-col :span="12">
      <div class="card1-bottom1">
        <div class="card1-bottom1-title">留言量：0</div>
      </div>
    </el-col>    
  </el-row>
    </div>
           </el-card>
        </el-col>
        <!-- 第二张卡片：网站概述 -->
        <el-col :span="24" class="vertical-layout">
          <el-card class="box-card">
    <template #header>
      <div class="right-card-header">
        <el-icon class="cardIcon1"><Memo /></el-icon>
        <span class="cardTitle1">| 网站概况</span>
      </div>
    </template>
    <div>
      <h2><el-icon><Avatar /></el-icon> 用户总数：{{showData.userNum||0}}个</h2>
      <h2><el-icon><Document /></el-icon> 文章总数：{{showData.articleNum||0}}篇</h2>  
      <h2><el-icon><ChatLineRound /></el-icon> 分类总数：{{showData.categoryNum||0}}条</h2>   
      <h2><el-icon><ChatSquare /></el-icon> 标签总数：{{showData.tagNum||0}}条</h2>   
      <h2><el-icon><Comment /></el-icon> 评论总数：{{showData.commentNum||0}}条</h2>   
      <h2><el-icon><Link /></el-icon> 链接数量：{{showData.linkNum||0}}个</h2>   
      <h2><el-icon><View /></el-icon> 浏览总量：{{showData.viewNum||0}}次</h2>     
    </div>
    <div>
    </div>
    </el-card>
        </el-col>
        <!-- 第三者卡片：热门文章 -->
        <el-col :span="24" class="vertical-layout">
          <el-card>
            <!-- 头 -->
    <template #header>
      <div class="right-card-header">
        <el-icon class="cardIcon1"><Memo /></el-icon><span class="cardTitle1">| 热门文章</span>
      </div>
    </template>
    <!--热门文章列表 -->
    <h3 v-for="(i,j) in hots" :key="i.id">
        <span class="ftt" v-if="j<3">{{ j+1 }}</span> 
        <span class="artilast" v-else>{{ j+1 }}</span>
        <span class="artileTitela" @click="goArticleInfo(i.id)">{{i.title}}</span>
      </h3>
    </el-card>
        </el-col>
      </el-row>
    </el-col>
    </el-row>
</template>

<script>
import {
    reactive,
    onBeforeMount,ref, 
    defineComponent
} from 'vue'
import {frontInfo} from '@/api/back/index.js'
import { ElMessage } from 'element-plus'
import emitter from '@/utils/event-bus.js'
import router from '@/router'
import {searchArticle} from '@/api/back/article'
export default defineComponent({
    name:'HomeShow',
    setup(){
  //前往用户主页
  const goUserHome=(data)=>{
    router.push({path:'/userHome'+data});
  }    
//显示文章详情页
const goArticleInfo=(id)=>{
  router.push({path:'/article'+id});
}     
//本站介绍
const introduce='kyou博客，个人程序。为了打发无聊时光和巩固知识而制作';
      // 分页参数
const params=reactive({
  pageNum:1,
  pageSize:10,
})
const total=ref(0);
const showData=ref({})
const articleList=ref([]);
const hots=ref([]);
onBeforeMount(()=>{
  emitter.on('searchArticle',(kw)=>{
     if(kw==null||kw==undefined||kw==''||kw.length<=0){
      return ;
     }
     const data={
       kw:kw,
       pageNum:params.pageNum,
       pageSize:params.pageSize,
       status:1
     }
     searchArticle(data).then(res=>{
        if(res.code==200){
          const e=res.data;
          total.value=e.total;
          articleList.value=e.records;
        }else{
          ElMessage.error(res.msg);
        }
     })
  })
  getFrontInfo();
})
const getFrontInfo=()=>{
  frontInfo(params).then(res=>{
    if(res.code==200){
    articleList.value=res.data.articleList.records;
    total.value=res.data.articleList.total;
    showData.value=res.data;
    hots.value=res.data.hotsArticles;
    }else{
      ElMessage.error(res.msg);
    }
    
  })
}
const handleSizeChange=(num)=>{
  params.value.pageNum=num;

}
    return {
        handleSizeChange,getFrontInfo,total,
        showData,params,articleList,hots,introduce,
        goArticleInfo,goUserHome
    }
    }

})



</script>

<style scoped>
.artileTitela{
  margin-left: 20px;font-size: large;
  color: lightcoral;
  cursor: pointer;
}
.artileTitela:hover{
  color: skyblue;
}
.artilast{
  background-color: gray;font-size: x-large;
}
.ftt{
  background-color: red;font-size: x-large;
}
.first-con{
  font-size: large;font-weight: 700;
}
.acsum{
  float:right;font-size: x-large;font-weight: 700;
}
.aview{
  font-size: x-large;margin-left: 25px;font-weight: 600;
}
.actime{
  font-size: large;margin-left: 20px;font-weight: 600;
}
.auname{
  font-size: x-large;margin-left: 10px;font-weight: 600;
  cursor: pointer;
}
.blog-card-content {
  margin-top: 20px;
  font-size: 16px;
}

.blog-card-footer {
  text-align: right;
  margin-top: 0px;
  float: left;
  margin-left: 140px;
}
.blog-card {
  margin-bottom: 20px;
  background-color: lightyellow;
}

.blog-card-header {
  font-size: 18px;
  font-weight: 700;
  cursor: pointer;
}

/*  */
.row{
    width: 1360px;
    height: 900px;
}
.cardIcon1{
  font-size: xx-large;
}
.cardTitle1{
  font-size: x-large;
  margin-left: 20px;
}
.right-card-header{
  background-color: darkgrey;
}
.card1-bottom1{
  background-color: darkgray;
  width: 100%;
  height: 50px;
}
.card1-bottom1-title{
  font-size: x-large;
  font-weight: 800;
  text-align: center;
}
.card1-img{
  width: 150px;
  height:150px;
}

</style>