<template>
    <el-row :gutter="24" justify="center" class="message" >
    <el-col :span="12" class="outer">
<el-card class="card-main">
  <h1 slot="header" class="blog-card-header">留言板</h1>
  <div>{{articleInfo.content ||'一些优秀的留言'}}</div>
  <div slot="footer" class="view">
    <span style="float: left;">
        <el-icon><View /></el-icon>
        <span style="margin-left: 5px;">{{articleInfo.viewCount || 0}}
        </span>
    </span>
    <el-icon v-if="isLike" class="articleLike" @click="likeAc"><StarFilled /></el-icon>
    <el-icon v-else class="articleLike" @click="likeAc"><Star /></el-icon>
    {{ articleInfo.likes || 0}}
</div>
</el-card>
<el-card class="comment-main">
<h1 slot="header" class="blog-card-comment-publish">发表评论</h1>
<el-scrollbar height="650px" >
    <u-comment v-if="isLogin" :config="config" @submit="submit" @like="like"/>
    <h1 v-else style="text-align: center;font-size: xx-large;font-weight: 400;">请登录后查看(<span style="cursor: pointer;" @click="goLogin">前往登录</span>)</h1>
</el-scrollbar>
    </el-card>
    </el-col>
  </el-row>
  <el-backtop :right="200" :bottom="120" />
</template>

<script>
import { defineComponent,onBeforeMount,reactive,ref} from 'vue'
import emoji from '../../public/emoji';
// import { UToast, createObjectURL } from 'undraw-ui'
import { ElMessage } from 'element-plus'
import {htmlToText,
    likeArticle,
    getArticleInfo
} from '@/api/back/article.js'
// import moment from 'moment';
import {getUserInfo} from '@/api/back/user.js'
import {addComment,
    showComments,getUserCommentInfo
    ,likeComment,
} from '@/api/back/comment.js'
import {commit} from '@/utils/comment.js'
import router from '@/router';
export default defineComponent({
    name:'Message',
setup() {
    const goLogin=()=>{
      router.push('/login')
    }
    //评论人信息
    const config = reactive({
        user: {
            id: undefined,
             username: '',
             avatar: '',
             level:1,
            //点赞的评论id
              likeIds: []
          },
              emoji: emoji,
              // 评论信息
             comments: []
    })
    //是否点赞
    const isLike=ref(false);
    //文章点赞事件
    const likeAc=()=>{
        isLike.value=!isLike.value;
        let type=isLike.value?1:-1;
        likeArticle(articleId,uid.value,type).then(res=>{
            if(res.code!=200){
                ElMessage.error(res.msg);
            }else{
                getCurInfo(uid.value);
            }
        })
    }
    //登陆状态
    const isLogin=ref(false);
    //用户/游客标识
    const uid=ref('');
    //检查登录状态并显示评论
const checkLoginAndShowComms= async()=>{
  const token=localStorage.getItem('token');
  if(token&&token!=null){
   const res=await getUserInfo();
   if(res.code==200){
        isLogin.value=true
        uid.value=res.data.id+'';
    }else{
        uid.value=localStorage.getItem('tourist')
        isLogin.value=false;
        localStorage.removeItem('token');
        localStorage.removeItem('routeList');
      }
  }else{
    isLogin.value=false;
    uid.value=localStorage.getItem('tourist')
  }
  
 //登陆后检查评论
 if(isLogin.value){
   getCommenterInfo();
}
//获取文章信息
getCurInfo(uid.value);
}   
        //文章id
    const articleId=3;
    //挂载前
    onBeforeMount(()=>{
        buildNetEnvComment();
        checkLoginAndShowComms();
    })
    const buildNetEnvComment=()=>{
        const o = navigator.userAgentData;
        if(!o){
            const modifiedUserAgentData = {
                     brands: [
{brand: 'Chromium', version: '116'},      
{brand: 'Not)A;Brand', version: '24'},
{brand: 'Google Chrome', version: '116'}             
            ],
            mobile: false,
            platform: "Windows"
            };
            // 重新设置浏览器的 User Agent 数据
Object.defineProperty(navigator, 'userAgentData', {
  value: modifiedUserAgentData,
  configurable: true,
  enumerable: true,
  writable: false
});}
    }
    //获取文章信息
    const getCurInfo=(uid)=>{
        getArticleInfo(articleId,uid,0).then(res=>{
            if(res.code==200){
                const e=res.data;
                articleInfo.viewCount=e.viewCount;
                articleInfo.likes=e.likeCount;
                articleInfo.content=htmlToText(e.content);
                isLike.value=e.likeStatus;
            }else{
                ElMessage.error(res.msg);
            }
        })
    }
    //文章内容信息
    const articleInfo=reactive({})
    //获取评论人信息
    const getCommenterInfo=async ()=>{
        const res=await getUserCommentInfo();
        if(res.code==200){
            const e=res.data;
            config.user.id=e.id;
            config.user.username=e.username;
            config.user.avatar=e.avatar;
            config.user.likeIds=e.likeIds;
            config.user.level=e.level;
            getCommentList();
        }else{
            ElMessage.error(res.msg);
        }
    }   
    //获取评论数据
    const getCommentList=()=>{
        showComments(articleId).then(rs=>{
            if(rs.code==200){
               config.comments= handleData(rs.data);
            }else{
               ElMessage.error(rs.msg);
            }
        })
    }
    //处理评论数据
    const handleData=(data)=>{
        if(data && data instanceof Array&&data.length>0){
           return data.map(e=>{
                const res=
                 {
    id: e.id,
    parentId: e.parentId,
    uid: e.uid,
    address: e.address,
    content:htmlToText(e.content),
    likes: e.likes,
    createTime: e.createTime,
    user: {
      username: e.username,
      avatar: e.avatar,
      level: e.level,
      homeLink: e.homeLink
    },
    reply:{
        list:handleData(e.list),
        total:e.total
    }
  }
  return res;})
        }else{
            return data;
        }
    }
// 提交评论事件
const submit = ({ content, parentId, finish,reply }) => {
    commit(config,articleId,content,parentId,finish,reply);
}
// 点赞按钮事件 将评论id返回后端判断是否点赞，然后在处理点赞状态
const like = (id) => {
  const likes = config.user.likeIds;
  if (likes.indexOf(id) == -1) {
    // 点赞 --后端接口处理
    likes.push(id)
    likeComment(id,1);
  } else {
    // 取消点赞 --后端接口
    likes.splice(likes.findIndex(item => item == id),1)
    likeComment(id,-1);
  }
}
return {
    config,like,submit,isLogin,articleInfo,isLike,
    likeAc,goLogin
}
    },
})
</script>
<style scoped>
::-webkit-scrollbar {
width: 10px;
}
/*  */
.articleLike{
    cursor: pointer;
    font-size: xx-large;
}
.view{
    margin-top: 380px;
    font-size:x-large;
    text-align: center;
}
.card-main{
    height: 40%;
    width: 1100px;
    margin-left: -310px;
    background-color: antiquewhite;
  
}
.comment-main{
    height: 52%;
    width: 1100px;
    margin-left: -310px;
}
.outer{
    width: 800px;

}
.message{
    height: 1300px;
    width: 1400px;
    
}
.blog-card-header{
    text-align: center;
    font-size: xx-large;
    font-weight: 700;
}
.blog-card-comment-publish{
    font-size: xx-large;
    font-weight: 600;
}
</style>