<template>
 <BackMainHeader/>
 <el-tabs type="border-card">
    <el-tab-pane label="个人信息">
      <BackUserInfo/>
    </el-tab-pane>
    <el-tab-pane label="密码修改">
      <UpdatePwd/>
    </el-tab-pane>
    <el-tab-pane label="关注列表">
      <FollowUserList/>
    </el-tab-pane>
    <el-tab-pane label="粉丝列表">
      <FansUserList/>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import FansUserList from '@/components/user/FansUserList.vue';
import FollowUserList from '@/components/user/FollowUserList.vue';
import { defineComponent,ref,onMounted } from 'vue'
import UpdatePwd from '@/components/user/UpdatePwd.vue'
import BackUserInfo from '@/components/user/BackUserInfo.vue'
import BackMainHeader from '@/components/BackMainHeader.vue';
import {getUserInfo} from '@/api/back/user.js'
import emitter from '@/utils/event-bus.js'
export default defineComponent({
    name:'Info',
    components:{
      BackUserInfo,BackMainHeader,UpdatePwd,FollowUserList,FansUserList
    },
    setup() {
      const active=ref('1');
      onMounted(()=>{
        getUserInfo().then(res=>{
          emitter.emit('data',res.data);
        })
      })

      return {
        active,
      }  
    },
})
</script>


<style>

</style>