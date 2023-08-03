<template>
    <el-main class="app-main">
    <HomeShow v-if="act=='1'"/>
    <Message v-else-if="act=='3'"/>
    <About v-else-if="act=='4'"/>
    <ArticlesFile v-else-if="act=='5'"/>
    </el-main>
</template>

<script setup>
import ArticlesFile from './ArticlesFile.vue';
import About from '@/components/About.vue'
import HomeShow from '@/views/HomeShow.vue';
import Message from '@/views/Message.vue'
import {
  onBeforeMount,ref,
  onBeforeUnmount,

} from 'vue'
import { ElMessage } from 'element-plus'
import emitter from '@/utils/event-bus.js'

onBeforeMount(()=>{
  emitter.on('changeMenu',(i)=>act.value=i)
})
onBeforeUnmount(()=>emitter.all.clear());
const act=ref('1')


</script>

<style scoped>
.app-main{
 margin: 100px 0;

}
</style>