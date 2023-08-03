<template>
  <BackMainHeader/>
  <el-row :gutter="24">
    <el-col :span="16">
      <el-input v-model="aricleTitle" placeholder="请输入文章标题" style="font-size: xx-large;height: 70px;border: 1px;font-weight: 700;"/>
      <v-md-editor 
      ref="mkd" v-model="aricleContent" 
      :disabled-menus="[]" @save="saveArticle"
      @upload-image="handleUploadImage" height="700px"/>
    </el-col>
    <!-- 右边 -->
    <el-col :span="8" style="width: 100%;">
        <el-collapse style="margin-top: 50px;" v-model="activeNames" @change="handleChange">
      <el-collapse-item name="1">
        <template #title><span style="font-size: x-large;">发布</span></template>
        是否开启评论：<el-switch v-model="openComment" size="large"/>   
    <el-button type="primary" class="fbn" @click="publish">发布文章</el-button>
    <el-button type="primary" @click="saveBase">保存草稿</el-button>
    <el-button type="primary" @click="reset"><el-icon><Refresh /></el-icon>重置</el-button>
      </el-collapse-item>
      <!-- 分类目录 -->
      <el-collapse-item name="2">
        <template #title><span style="font-size: x-large;">分类目录</span></template>
        <el-card style="height: 100%;background-color: lightgoldenrodyellow;">
            <span style="font-size: x-large;font-weight: 600;">选中的分类编号：{{ articleCategoryIds}}</span>
          <el-table
      :data="treeList" class="tableTree"
      row-key="id"
      border
      @selection-change="handleSelectionChange"
      highlight-current-row
    >
    <el-table-column type="selection"/>
      <el-table-column prop="id" label="编号" sortable />
      <el-table-column prop="name" label="名称" sortable />
      <el-table-column prop="articleNum" label="文章数量" sortable />
    </el-table>
        </el-card>
      </el-collapse-item>
      <el-collapse-item name="3">
        <template #title><span style="font-size: x-large;">标签</span></template>
    <!-- 文章选中的标签 -->
        <el-tag v-for="tag in aricleTags" :key="tag" closable
    :disable-transitions="false" @close="handleClose(tag.name)">
    <span style="font-size: medium;">{{ tag }}</span>
  </el-tag>
       <br/>
       <!-- 选择需要的标签 -->
       <el-select v-model="checkedTag" @change="pushTag" class="m-2" placeholder="选择文章标签" size="large">
    <el-option v-for="item in tags" :key="item.name" :label="item.name" :value="item.name"/>
  </el-select>  
      </el-collapse-item>
      <el-collapse-item  name="4">
        <template #title><span style="font-size: x-large;">缩略图</span></template>
  <!-- 上传缩略图 -->
        <el-upload
    class="avatar-uploader" :action="articleimgUrl"
    :show-file-list="false" ref="theImg" :on-exceed="handleExceed"
    :headers="headers" :limit="1" :on-success="handlerSuccess">
    <img v-if="aricleImg" :src="aricleImg" class="avatar" />
    <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
  </el-upload>
      </el-collapse-item>
    </el-collapse>
    </el-col>
  </el-row>
</template>

<script>
import { defineComponent,ref,onBeforeMount,getCurrentInstance } from 'vue'
import BackMainHeader from '@/components/BackMainHeader.vue'
import {listTags} from '@/api/back/tags.js'
import 'highlight.js/styles/a11y-dark.css';
import emitter from '@/utils/event-bus.js'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import router from '@/router';
import {getUserInfo} from '@/api/back/user.js'
import {listCategory} from '@/api/back/category'
import {contentImg,addArticle
,getArticleInfo,htmlToText,updArticle
} from '@/api/back/article.js'
import { async } from '@kangc/v-md-editor';
export default defineComponent({
    name:"publish",
    components:{
        BackMainHeader,Plus
    },
    setup() {
      const { proxy } = getCurrentInstance();
      const uid=ref(localStorage.getItem('tourist'))
      const handleEditData=(data)=>{
        //已保存模式
        flag.value=true;
        //文章标题
        aricleTitle.value=data.title;
        //是否开启评论
        openComment.value=data.isComment;
        //文章内容
        aricleContent.value=htmlToText(data.content);
        //id
        articleId.value=data.id;
        //文章状态
        articleStatus.value=data.status;
        //文章缩略图
        aricleImg.value=data.thumbnail;
        //分类id集合
        articleCategoryIds.value=data.categoryIds;
        //标签
        aricleTags.value=data.tags;
      }
      const isEdit=ref(false);
      onBeforeMount(async()=>{
        const query=router.currentRoute.value.query;
        const k=Object.keys(query);
        if(k.length>0){
          if(query.edit){
            //登录后可编辑
            const result=await getUserInfo();
            if(result.code==200){
              uid.value=result.data.id+'';
            }else{
              return;
            }
            isEdit.value=true;
            getArticleInfo(query.edit,uid.value,1).then(res=>{
              if(res.code==200){
                handleEditData(res.data);
              }else{
                ElMessage.error(res.msg);
              }
            })
          }
        }

          })
        //编译后的HTML代码
        const compiledHtml=ref('');
     //选择分类目录数据
     const handleSelectionChange=(rows)=>{
        articleCategoryIds.value=rows.map(i=>i.id);
     }   
        //获取分类数据
   const getCategroyList=()=>{
            listCategory().then(res=>{
                if(res.code==200){
                    treeList.value=res.data;
                }else{
                    ElMessage.error(res.msg);
                }
            })
        }
 //分类数据
 const treeList=ref([]);       
        //激活的手风琴
const activeNames = ref(['1'])
        //显示出的手风琴
const handleChange = (val) => {
  if(val instanceof Array){
    //标签列
     if(val.indexOf('3')){
        if(tags.value.length==0){
            getTagList();
        }
     }
     if(val.indexOf('2')){
        if(treeList.value.length<=0){
            getCategroyList();
        }
     }
  }
}
const articleimgUrl=ref(`${proxy.$key}/sys/article/uploadImg`)
//文件超出限制的回调
const handleExceed = (files) => {
    theImg.value.clearFiles()
  const file = files[0]
  file.uid = genFileId()
  theImg.value.handleStart(file)
}
//上传文件完成后的回调
const handlerSuccess=(res,uploadFile,uploadFiles)=>{
    if(res.code==200){
    aricleImg.value=res.data;
  }else{
    ElMessage.error(res.msg);
  }
}
//缩略图对象
const theImg=ref(null)
//放置标签
const pushTag=()=>{
    if(checkedTag.value!=''){
        if(aricleTags.value.indexOf(checkedTag.value)==-1){
            aricleTags.value.push(checkedTag.value);
        }
    }
}
//删除标签
const handleClose = (tag) => {
    aricleTags.value.splice(aricleTags.value.indexOf(tag), 1)
}
//缩略图携带的token
const headers={
    Authorization:localStorage.getItem('token')
}
//文章状态：默认编辑状态
const articleStatus=ref(1);
//当前选中的标签
const checkedTag=ref('')
//标签列表
const tags=ref([]);
//获取标签列表
const getTagList=()=>{
            listTags().then(res=>{
                if(res.code==200){
                    tags.value=res.data;
                }
            })
        }
        //是否开启评论
        const openComment=ref(false);
        const mkd=ref(null)
        //上传文件的
        const handleUploadImage=(event, insertImage, files)=> {
        // 拿到 files 之后上传到文件服务器，然后向编辑框中插入对应的内容
        const formData = new FormData();
        formData.append('image', files[0]);
        contentImg(formData).then(res=>{
            insertImage({url:res.data})
        })
      }
        //是否已保存 
        const flag=ref(false);
        //保存草稿
        const saveArticle=(text,html)=>{
          articleStatus.value=1;
          flag.value=true;
            articleContent.value=html;
            aricleContent.value=text;
            const article={
                id:articleId.value,
                //标题
                title:aricleTitle.value,
                //内容
                content:articleContent.value,
                //是否开启评论
                isComment:openComment.value,
                //缩略图
                thumbnail:aricleImg.value,
                //摘要
                summary:aricleContent.value.substring(0,40),
                //分类id集合
                categoryIds:articleCategoryIds.value,
                //文章标签
                tags:aricleTags.value,
                //文章状态
                status:articleStatus.value
            }
            if(isEdit.value){
              updArticle(article).then(res=>{
                if(res.code==200){
                    ElMessage.success(res.msg);
                }else{
                    ElMessage.error(res.msg);
                }
              })
            }else{
              addArticle(article).then(res=>{
                if(res.code==200){
                    ElMessage.success(res.msg);
                    articleId.value=res.data;
                }else{
                    ElMessage.error(res.msg);
                }
            })
            }
            
        }
        //重置
        const reset=()=>{
          isEdit.value=false;
          articleStatus.value=1;
          flag.value=false;
          articleId.value=undefined;
            articleCategoryIds.value=[];
            aricleTitle.value='';
            articleContent.value='';
            aricleContent.value='';
            aricleTags.value=[];
            aricleImg.value='';
            openComment.value=false;
            compiledHtml.value='';
            treeList.value=[];
            checkedTag.value=''
            tags.value=[];

        }
        //发布文章
        const publish=()=>{
          articleStatus.value=2;
          const article={
                id:articleId.value,
                //标题
                title:aricleTitle.value,
                //内容
                content:articleContent.value || aricleContent.value,
                //是否开启评论
                isComment:openComment.value,
                //缩略图
                thumbnail:aricleImg.value,
                //摘要
                summary:aricleContent.value.substring(0,40),
                //分类id集合
                categoryIds:articleCategoryIds.value,
                //文章标签
                tags:aricleTags.value,
                //文章状态
                status:articleStatus.value
              };
            if(flag.value){
              //已经保存了，需要更新
              updArticle(article).then(res=>{
                if(res.code==200){
                    ElMessage.success(res.msg);
                }else{
                    ElMessage.error(res.msg);
                }
              })
            }else{
              addArticle(article).then(res=>{
                if(res.code==200){
                    ElMessage.success(res.msg);
                }else{
                    ElMessage.error(res.msg);
                }
            })     
            }
           
        }
        //文章id
        const articleId=ref(undefined);
        //保存草稿的按钮
        const saveBase=()=>{
            mkd.value.save();
        }
        //文章分类id集合
        const articleCategoryIds=ref([])
        //文章标题
        const aricleTitle=ref('')
        //上传内容
        const articleContent=ref('')
        //绑定的内容
        const aricleContent=ref('')
        //文章标签
        const aricleTags=ref([]);
        //文章缩略图
        const aricleImg=ref('');
        return {
            aricleTitle,aricleContent,handleUploadImage,
            publish,mkd,saveArticle,openComment,activeNames,
            handleChange,tags,aricleTags,handleClose,
            checkedTag,pushTag,headers,theImg,handlerSuccess,
            aricleImg,handleExceed,articleimgUrl,treeList,
            handleSelectionChange,saveBase,compiledHtml,reset,
            articleCategoryIds

        }
    }
})
</script>


<style scoped>
.box-card {

  width: 480px;
  height: 200px;
  margin-top: 40px;
  background-color: bisque;
}
.fbn{
    font-size: medium;
    cursor: pointer;
    
}
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader{
    background-color:darkgrey;
    width: 300px;
    height: 300px;
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>