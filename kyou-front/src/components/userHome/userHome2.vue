<template>
    <div style="width: 100%px;height:100%;position: relative;margin-top:-40px;">
        <div style="background-color: lightgrey;width: 100%;">
            <span style="font-size: xx-large;color: black;font-weight: 600;cursor: pointer;" @click="back"><el-icon><Back /></el-icon>返回</span>
        </div>
        <div>
            <div>
                <el-card style="width:1000px;">
                    <template #header>
                        <div style="background-color: darkgrey;font-size: x-large;">
                            <el-icon><Memo /></el-icon> &nbsp;|&nbsp; 关于作者
                        </div>
                    </template>
                    <div style="text-align:center;">
                        <el-avatar :size="120" :src="userInfo.headImage"/>
                        <h3 style="font-size:x-large;font-weight:700px;color:black">账号：{{ userInfo.username }} &nbsp;&nbsp; 昵称：{{ userInfo.nickname }}</h3>
                        <div style="font-size:large;">文章：{{userInfo.articleNum}}&nbsp;&nbsp;|&nbsp;&nbsp;评论：{{ userInfo.commentNum }}</div>
                        <div style="font-size:large;">关注：{{userInfo.followNum}}&nbsp;&nbsp;|&nbsp;&nbsp;粉丝：{{ userInfo.fansNum }}</div>
                        <h3>评论等级：{{userInfo.level}}({{userInfo.experience}}/30000)</h3>
                        <el-input v-model="userInfo.remark" style="width:500px;" type="textarea" disabled placeholder="这个人有点懒，什么也没说"></el-input>
                        <div>
                            <el-button v-if="isLogin" v-show="!followStatus&&uid!=authorId" type="primary" style="font-size:large;" @click="followUser"><el-icon><Star /></el-icon>关注</el-button>
                            <el-button v-if="isLogin" v-show="followStatus&&uid!=authorId" type="primary" style="font-size:large;" @click="followUser"><el-icon><StarFilled /></el-icon>取消关注</el-button>
                        </div>
                    </div>
                </el-card>
                <!-- 文章列表 -->
                <div>
                    <div style="background-color:gray;font-size:x-large;">
                        <span style="margin-left:20px;">文章列表</span>
                    </div>
                    <el-scrollbar height="600px" v-if="articleList.length>0">
                        <!-- 作者的文章列表 -->
                        <el-card v-for="post in articleList" :key="post.id" style="margin-bottom: 20px;background-color: lightyellow;">
                            <!-- 标题 -->
                            <div slot="header" style=" font-size: 18px;font-weight: 700;cursor: pointer;" @click="goArticleInfo(post.id)">{{ post.title }}</div>
                            <div style=" margin-top: 20px;font-size: 16px;">
                                <!-- 文章图片 -->
                                <el-image style="width: 100px; height: 100px" :src="post.thumbnail" :zoom-rate="1.2" :initial-index="1" fit="cover" />
                                <!-- 文章简介 -->
                                <div style="font-size: x-large;float:right;font-weight: 600;">{{ post.summary }}</div>
                            </div>
                            <!-- 作者的文章基本信息 -->
                            <div slot="footer" style="text-align: right;margin-top: 0px;float: left;margin-left: 140px;">
                                <el-avatar :size="50" :src="post.userVo.headImage"/>
                                <span style="font-size: x-large;margin-left: 10px;font-weight: 600;" >{{ post.userVo.username }}</span>
                                <span style="font-size: large;margin-left: 20px;font-weight: 600;">{{ post.createTime }}</span>
                                <span style="font-size: x-large;margin-left: 25px;font-weight: 600;"><el-icon><View /></el-icon> {{ post.viewCount }}</span>
                            </div>
                        </el-card>
                        <el-pagination v-if="articleList.length>0" background layout="prev, pager, next"
                                       v-model:current-page="pages.pageNum" v-model:page-size="pages.pageSize"
                                       :total="total" @current-change="handleCurrentChange" style="float:right"/>
                    </el-scrollbar>
                    <el-backtop :right="200" :bottom="120" />

                </div>
            </div>
        </div>

    </div>

</template>

<script>
    import router from '@/router'
    import { defineComponent,ref ,onMounted} from 'vue'
    import {getAuthorArticles} from '@/api/back/article.js'
    import {getAuthorInfo,getUserInfo,follow} from '@/api/back/user'
    export default defineComponent({
        name:'userHome2',
        setup() {
            //退出
            const back=()=>{
                router.back();
            }
            //作者id
            const authorId=2;
            //文章列表
            const articleList=ref([]);
            //文章数
            const total=ref(0)
            //请求参数
            const pages=ref({
                pageNum:1,
                pageSize:6,
            })
            //获取文章列表
            const getData=()=>{
                getAuthorArticles(authorId,pages.value.pageNum,pages.value.pageSize).
                then(res=>{
                    if(res.code==200){
                        total.value=res.data.total;
                        articleList.value=res.data.records;
                    }
                })
            }
            //登录用户的id，用于判断是否关注
            const uid=ref(undefined)
            //检查登录状态
            const checkLogin= async()=>{
                const token=localStorage.getItem('token');
                if(token&&token!=null){
                    const res=await getUserInfo();
                    if(res.code==200){
                        isLogin.value=true
                        uid.value=res.data.id;
                    }else{
                        isLogin.value=false;
                        localStorage.removeItem('token');
                        localStorage.removeItem('routeList');
                    }
                }else{
                    isLogin.value=false;
                }
                //获取作者信息
                getUserData();
//获取文章数据
                getData();
            }
            //登录状态
            const isLogin=ref(false);
            //下一页
            const handleCurrentChange=(num)=>{
                pages.value.pageNum=num;
                getData();
            }
            //关注
            const followUser=()=>{
                followStatus.value=!followStatus.value;
                //false是未关注，true是已关注
                const flag=followStatus.value?1:0;
                const uf={
                    curId:authorId,
                    fansId:uid.value
                }
                follow(uf,flag);
            }
            //关注状态
            const followStatus=ref(false);
            //作者信息
            const userInfo=ref({
                experience:0,
                articleNum:0,
                commentNum:0,
                headImage:'',
                level:1,
                nickname:"",
                username:'',
                remark:'',
                fansNum:0,
                followNum:0,
                followIds:[],
                fansIds:[]
            })
            //获取作者信息和判断是否关注
            const getUserData=()=>{
                getAuthorInfo(authorId).then(res=>{
                    if(res.code==200){
                        followStatus.value=(res.data.fansIds.includes(uid.value))
                        userInfo.value=res.data;
                    }
                })
            }
            onMounted(()=>{
                checkLogin();
            })
            //显示文章详情页
            const goArticleInfo=(id)=>{
                router.push({path:'/article'+id});
            }
            return {
                back,articleList,goArticleInfo,pages,total,
                handleCurrentChange,userInfo,isLogin,followUser,
                followStatus,uid,authorId
            }
        },
    })
</script>