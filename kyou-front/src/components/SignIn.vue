<template>
  <div style="background-color: lightgoldenrodyellow;width: 1000px;height: 780px;">
    <span style="float: left;font-size: xx-large;cursor: pointer;" @click="back"><el-icon><Back /></el-icon>返回</span>
  <div style="width: 500px;margin-left: 260px;float: left;">
  <el-menu :default-active="activeName" mode="horizontal" @select="handleMonth">
  <el-menu-item index="1">
    <template #title>
        <span class="lab">{{ label3 }}</span>
    </template>
  </el-menu-item>
  <el-menu-item index="2">
    <template #title>
        <span class="lab">{{ label2 }}</span>
    </template>
  </el-menu-item>
  <el-menu-item index="3">
    <template #title>
        <span class="lab">{{ label1 }}</span>
    </template>
  </el-menu-item>
 </el-menu>
 <el-tag v-for="i in days" :key="i" 
  size="large" style="width: 120px;height: 70px;">
 <span style="font-size: xx-large;font-weight: 900;" v-if="signedDays.includes(i)"> {{ i}}</span>
 <span style="font-size: large;font-weight: 600;color: red;" v-else>{{ i }}</span>
</el-tag>
<div style="font-size: large;">本月签到次数：{{ signNum }}</div>
<div style="font-size: large;">本月连续签到次数：{{ maxColiedNum }}</div>
<el-button v-show="activeName=='3'" v-if="!signStatus" class="sin" @click="sign">签到</el-button>
<el-button v-show="activeName=='3'" v-else class="sin" disabled>已签到</el-button>
  </div>
  </div>
</template>

<script>
import { defineComponent,ref,onMounted } from 'vue'
import router from '@/router';
import { ElMessage } from 'element-plus'

import {userSignIn,checkSignIn,getsignInNum} from '@/api/back/user'
export default defineComponent({
    name:'SignIn',
    setup() {
      //返回
     const back=()=>{
        router.back();
     }   
const label1=ref(new Date().getMonth()+1+'月份');
const label2=ref(new Date().getMonth()+'月份');
const label3=ref(new Date().getMonth()-1+'月份');
//进行签到
const sign=()=>{
  userSignIn().then(res=>{
    if(res.code==200){
       ElMessage.success(res.msg);
    }else{
      ElMessage.error(res.msg);
    }
    getSignNum();
  })
}
//本月天
const days=ref(30);
//签到次数
const signNum=ref(0);
//已签到天
const signedDays=ref([])
onMounted(()=>{
  checkSign();
  getSignNum();
})
//检查签到状态
const checkSign=()=>{
  checkSignIn().then(res=>{
    if(res.code==200){
      signStatus.value=res.data;
    }else{
      ElMessage.error(res.msg);
    }
  })
}
//签到状态
const signStatus=ref(false);
const handleMonth=(index)=>{
 activeName.value=index;
 getSignNum();
}
//连续签到次数
const maxColiedNum=ref(0);
//获取签到信息
const getSignNum=()=>{
  let month;
  if(activeName.value=='3'){
   month =new Date().getMonth()+1;
  }else if(activeName.value=='2'){
    month =new Date().getMonth();
  }else if(activeName.value=='1'){
    month =new Date().getMonth()-1;
  }else{
    return ;
  }
  getsignInNum(month).then(res=>{
    if(res.code==200){
      maxColiedNum.value=res.data.maxCoiledNum;
    days.value=res.data.daysMonth;
    signNum.value=res.data.signMonthNum;
    signedDays.value=res.data.signDays;
    }else{
      ElMessage.error(res.msg);
    }
  })
}
const activeName = ref('3');
    return {
        activeName,label1,label2,label3,handleMonth,
        back,signStatus,sign,days,signNum,signedDays,
        maxColiedNum,
    }

    },
})
</script>


<style scoped>
.sin{

  margin-top: 10px;
  margin-left: 60px;
  width: 100px;
  font-size: x-large;
  height: 60px;
  background-color: lightgrey;
}
.lab{
    font-size: x-large;
    font-weight: 600;
}
</style>