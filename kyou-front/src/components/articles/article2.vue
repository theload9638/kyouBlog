<template>
     <el-container style="width: 100%;height:100%">
    <el-header style="background-color: lightgrey;width: 100%;">
<span style="font-size: xx-large;color: black;font-weight: 600;cursor: pointer;" @click="back"><el-icon><Back /></el-icon>返回</span>
    </el-header>
  <el-main>
    <el-row :gutter="24" justify="center"  style="height: 1300px;width: 1400px;">
        <el-col :span="12" style="width: 800px;">
            <el-card style="height: 40%;width: 1100px;margin-left: -310px;background-color: antiquewhite;">
                <h1 slot="header" style="text-align: center;font-size: xx-large;font-weight: 700;">测试文章1</h1>
                <div>{{articleInfo.content}}</div>
                <div slot="footer" style="margin-top: 380px;font-size:x-large;text-align: center;">
    <span style="float: left;">
        <el-icon><View /></el-icon><span style="margin-left: 5px;">{{articleInfo.viewCount}}</span>
    </span>
                    <el-icon v-if="isLike" style="cursor: pointer;font-size: xx-large;" @click="likeAc"><StarFilled /></el-icon>
                    <el-icon v-else style="cursor: pointer;font-size: xx-large;" @click="likeAc"><Star /></el-icon>
                    {{articleInfo.likes}}
                    <span style="float: right;">作者：<span style="cursor: pointer;color: rgb(110, 140, 150);" @click="goHomeLink">{{ authorName }}</span></span>
                </div>
            </el-card>
            <el-card style="width: 1100px;height: 8%;margin-left: -310px;">
                <div  style="font-size: x-large;font-weight: 600;">文章标签
                    <el-tag v-if="tNames.length>0" v-for="t in tNames" :key="t" style="margin-left: 5px;font-size: large;">{{ t }}</el-tag>
                </div>
                <div style="font-size: x-large;font-weight: 600;">文章分类
                    <el-tag v-if="cNames.length>0" v-for="t in cNames" :key="t" style="margin-left: 5px;font-size: large;">{{ t }}</el-tag>
                </div>
            </el-card>
            <el-card style="height: 60%;width: 1100px;margin-left: -310px;">
                <h1 slot="header" style="font-size: xx-large;font-weight: 600;">发表评论</h1>
                <el-scrollbar height="650px" >
                    <u-comment v-if="isLogin&&isComment" :config="config" @submit="submit" @like="like"/>
                    <h1 v-else-if="!isLogin" style="text-align: center;font-size: xx-large;font-weight: 400;">请登录后查看(<span style="cursor: pointer;" @click="goLogin">前往登录</span>)</h1>
                    <h1 v-else-if="isLogin&&!isComment" style="text-align: center;font-size: xx-large;font-weight: 400;">评论区已关闭，无法评论</h1>
                </el-scrollbar>
            </el-card>
        </el-col>
    </el-row>
    <el-backtop :right="200" :bottom="120" />
</el-main>
</el-container>
</template>

<script>
    import { defineComponent,onBeforeMount,reactive,ref} from 'vue'
    import emoji from '../../../public/emoji';
    import { UToast, createObjectURL } from 'undraw-ui'
    import { ElMessage } from 'element-plus'
    import {htmlToText,
        likeArticle, getArticleInfo,tagAndCategory
    } from '@/api/back/article.js'
    import moment from 'moment';
    import {getUserInfo,getAuthorInfoByAid} from '@/api/back/user.js'
    import {addComment,
        showComments,getUserCommentInfo
        ,likeComment,
    } from '@/api/back/comment.js'
    import router from '@/router';
    import {commit} from '@/utils/comment.js'
import '@/assets/article.css'
    export default defineComponent({
        name:'article2',
        setup() {
            const back=()=>{
                router.back();
            }
            //获取文章分类和标签
            const getSomeInfo=()=>{
                tagAndCategory(articleId).then(res=>{
                    if(res.code==200){
                        const e=res.data;
                        tNames.value=e.tags.map(i=>i.name);
                        cNames.value=e.categories.map(i=>i.name);
                    }
                })
            }
            //分类集
            const cNames=ref([])
            //标签集
            const tNames=ref([])
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
                getSomeInfo();
                getAuthorInfoByAid(articleId).then(res=>{
                    if(res.code==200){
                        const e=res.data;
                        authorId.value=e.id;
                        authorName.value=e.nickname || e.username;
                    }
                })
            }
             const goHomeLink=()=>{
                router.push("/userHome"+authorId.value);
            }
            const authorId=ref(undefined);
            const authorName=ref('');
            //文章id
            const articleId=2;
            //挂载前:ok
            onBeforeMount(()=>{
                checkLoginAndShowComms();
            })
            const goLogin=()=>{
      router.push('/login')
    }
    const isComment=ref(true);
            //获取文章信息:ok
            const getCurInfo=(uid)=>{
                getArticleInfo(articleId,uid,0).then(res=>{
                    if(res.code==200){
                        const e=res.data;
                        isComment.value=e.isComment;
                        articleInfo.viewCount=e.viewCount;
                        articleInfo.likes=e.likeCount;
                        articleInfo.content=htmlToText(e.content);
                        isLike.value=e.likeStatus;
                    }
                })
            }
            //文章内容信息:ok
            const articleInfo=reactive({
                viewCount:0,
                likes:0,
                content:''
            })
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
                }
            }
            //获取评论数据
            const getCommentList=()=>{
                showComments(articleId).then(rs=>{
                    config.comments= handleData(rs.data);
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
                likeAc,tNames,cNames,back,goLogin,isComment,authorName,goHomeLink
            }
        },
    })
</script>


