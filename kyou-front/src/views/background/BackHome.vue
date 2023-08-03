<template>
  <div class="common-layout" id="module" >
    <el-container class="big">
      <BackAside />
      <el-container >
        <BackHeader/>
        <el-main class="main">
          <RouterView/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { defineComponent} from "vue";
import BackHeader from '@/components/BackHeader.vue'
import BackAside from '@/components/BackAside.vue'
import { RouterView } from 'vue-router'

export default defineComponent({
  name:'BackHome',
  components:{
    BackAside,BackHeader,RouterView
  },
  setup() {
   
    return {}
  },
})
</script>



<style lang="css" scoped>


#module .el-menu{
  height: 100vh;
  background-color: #333444;
}
#module .el-menu li,#module .el-menu .el-submenu__title{
  color: #fff;
}


.main{
  width: 84%;
  height: 100%; 
  padding: 0px;
  position:absolute;
  left:300px;
  top:60px;
  bottom: -100px;
  background-color: white;
  margin-bottom: 0px;
  padding-bottom: 0px;
}
  .el-header {
    background-color: #B3C0D1;
    color: #333;
    text-align: center;
    line-height: 60px;
  }
  .el-main {
    text-align: center;
    line-height: 160px;
  }
  *{
    margin: 0px;
    padding: 0px;
  }
  body > .el-container {
    position:fixed;
    margin-bottom: 40px;
  }
  
  .el-container:nth-child(5) .el-aside,
  .el-container:nth-child(6) .el-aside {
    line-height: 260px;
  }
  
  .el-container:nth-child(7) .el-aside {
    line-height: 320px;
  }





</style>