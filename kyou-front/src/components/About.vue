<template>
    <el-row :gutter="24" justify="center" class="message" >
    <el-col :span="12" class="outer">
<el-card class="card-main">
  <h1 slot="header" class="blog-card-header">关于本系统</h1>
  <div style="font-size: large;">
    <p style="font-weight: 600;">这是博主为了巩固知识而制作的项目，博主不太擅长写前端页面，
    如果页面中遇到显示不了的，请多刷新几次页面。</p>
     <p style="font-weight: 600;"> 
        如果发现有bug，可以在留言板留言，我会定期查看系统
     </p>   
     <p>
        登录可享受更多功能福利哦~(评论区显示，后台管理，签到功能等)
     </p>
     <p style="color: red;font-weight: 600;">
        Kyou博客1.0版本注意事项：
     </p>
     <p>
        1.本系统每天发评论可以增加评论等级经验，每天评论经验获取次数为3次，每次评论获取经验点为10
     </p>
     <p>
        <!-- 2.评论等级：&nbsp;1级(0-2999)，2级(3000-5999)，3级(6000-8999)，4级(9000-11999)，5级(12000-29999)，6级 -->
        2.评论等级系统：
        <ul>
            <li>1级(0-2999)</li>
            <li>2级(3000-5999)</li>
            <li>3级(6000-8999)</li>
            <li>4级(9000-11999)</li>
            <li>5级(12000-29999)</li>
            <li>6级(最高级)</li>
        </ul>
    </p>
    <p>
        3.新增标签：每位用户每天只限新增3个标签
    </p>
    <p>
        4.签到系统：
        <ul>
            <li>签到1天能额外获取10点评论经验</li>
            <li>连续签到7天能额外获取100点评论经验(ps：2月份的28天不予获取连续周签到奖励)</li>
            <li>连续签到一个月能够额外获取300点评论经验</li>
        </ul>
    </p>
    <p>
        5.文章发布状态：（1）草稿 （2）待审核 （3）已发布 （4）已下架 （5）审核未通过
    </p>
    <p>
        版本bug修复(1-1)：
        <ol>
        <li>修复了文章列表和用户主页的重复Bug</li>
        <li>修复了线上环境不能加载路由的问题</li>
        <li>修复了无法修改密码的问题</li>
        <li>修复了无法查询用户的问题</li>
        </ol>
    </p>
  </div>
</el-card>
    </el-col>
  </el-row>
</template>

<script>
import { defineComponent } from 'vue'

export default defineComponent({
    name:'About', 
    setup() {
        
    },
})
</script>


<style scoped>
::-webkit-scrollbar {
width: 10px;
}
/*  */
.card-main{
    height: 100%;
    width: 1100px;
    margin-left: -310px;
    background-color: antiquewhite;
  
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

</style>