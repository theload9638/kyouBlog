<template>
  <BackMainHeader />
  <el-row>
    <el-col :span="24">
      <el-button type="primary" 
      :icon="Edit"
      @click="addUser"
       size="large">新增</el-button>
      <el-button type="danger" 
      :icon="Delete"
      @click="delSome"
       size="large">永久删除</el-button>
      <!-- 用户状态 -->
      <el-select v-model="statusVal" class="userStatusClz" 
      placeholder="状态" size="large">
    <el-option
      v-for="item in userStatus"
      :key="item.value"
      :label="item.label"
      :value="item.value"/>
  </el-select>
  <!-- 用户性别 -->
  <el-select v-model="genderVal" class="userStatusClz" 
      placeholder="性别" size="large">
    <el-option
      v-for="item in userGender"
      :key="item.value"
      :label="item.label"
      :value="item.value"/>
  </el-select>
      <el-input  v-model="key" class="in"
         size="large" placeholder="请输入关键字 " />
         <el-button type="primary" @click="search">搜索</el-button>
         <el-button :icon="Refresh" circle @click="refresh"/>
      </el-col>
  </el-row>
  <BackUserList/>
</template>

<script>
import { defineComponent,onMounted,ref } from 'vue'
import BackUserList from '@/components/BackUserList.vue';
import BackMainHeader from '@/components/BackMainHeader.vue';
import { Delete, Edit,Refresh } from '@element-plus/icons-vue'
import emitter from '@/utils/event-bus.js'
import router from '@/router';
export default defineComponent({
  name:'userList',
  components:{
    BackMainHeader,BackUserList
  },
  setup() {
    //用户状态集合
    const userStatus=ref([
      {
        value:'0',
        label:'启用'
      },
      {
        value:'1',
        label:'禁用'
      },
      {
        value:'',
        label:'全部状态'
      },
    ])
    //选中用户状态
    const statusVal=ref('');
    // 选中性别
    const genderVal=ref('')
    //性别集合
    const userGender=ref([
      {
        value:'1',
        label:'男'
      },
      {
        value:'0',
        label:'女'
      },
      {
        value:'2',
        label:'未知'
      },
      {
        value:'',
        label:'全部性别'
      },
    ])
    const key=ref('');
    //搜索
    const search=()=>{
      const conditional={
        status:statusVal.value,
        gender:genderVal.value,
        key:key.value
      };
      //触发搜索事件
      emitter.emit('search',conditional)
    }
    //重置
    const refresh=()=>{
      statusVal.value='';
      genderVal.value='';
      key.value='';
      const conditional={
        pageNum:1,
        pageSize:10,
        status:'',
        gender:'',
        key:''
      };
      //触发重置事件
      emitter.emit('refresh',conditional);
    }
    //删除
    const delSome=()=>{
      emitter.emit('delAll');
    }
    //新增用户
    const addUser=()=>{
      router.push({path:'/user/userInfo',query:{type:'insert'}})
    }
    onMounted(()=>{
      
 
  }
    )
    return {
      Edit,Delete,key,userStatus,statusVal,
      genderVal,userGender,Refresh,search,refresh,
      delSome,addUser,
    }
  },
})
</script>


<style scoped>
.row{
  background-color: red;
  height: 100px;
  display: flex;
  flex-direction: row;
}
.in{
  width: 500px;
}
.userStatusClz{
  width: 120px;
}


</style>