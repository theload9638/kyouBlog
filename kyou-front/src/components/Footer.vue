<template>
    <el-footer>
  <div class="footer">
    <div class="main">
        关于本站：基于springboot、mybatis-plus、springSecurity、FreeMarker、vue3、Dubbo、ElementPlus实现
    </div>
    <div class="boot">
        <el-icon :size="40"><Location/></el-icon>
       <div class="ni">kyou博客</div>
    </div>
  </div>
</el-footer>
</template>
<style scoped>
 .footer{
    background-color: skyblue;
    bottom: 0px;
    width: 100%;
    left: 0px;
    position:fixed;
    text-align: center;
 }
.boot{
    background-color: lightyellow;
    width: 100%;
    font-size: large;
    display: flex;
    justify-content: center;
    padding: 10px 0;
}
.main{
    font-size: x-large;
    font-weight: 600;
}

.ni{
    font-size: x-large;
    font-weight: 600;
}
</style>