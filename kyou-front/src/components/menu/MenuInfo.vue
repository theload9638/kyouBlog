<template>
<el-descriptions
    :title="menuVo.name"
    direction="vertical"
    :column="4"
    :size="size"
    border
  >
    <el-descriptions-item label="组件名称">{{ menuVo.name }}</el-descriptions-item>
    <el-descriptions-item label="组件路径">{{ menuVo.path }}</el-descriptions-item>
    <el-descriptions-item label="组件" :span="2">{{ menuVo.components }}</el-descriptions-item>
    <el-descriptions-item label="图标">
        <el-icon>
            <component :is="menuVo.icon" />
          </el-icon>
    </el-descriptions-item>
    <el-descriptions-item label="子菜单数量">
        {{ menuSize}}
    </el-descriptions-item>
  </el-descriptions>
</template>

<script>
import { defineComponent,
    onMounted,ref,computed,
} from 'vue'
import emitter from '@/utils/event-bus.js'
import {menuInfo} from "@/api/back/menu.js"

export default defineComponent({
    name:'MenuInfo',
    setup(props,context) {
        const menuVo=ref({});
        const size=ref('')
        const menuSize=computed(()=>{
            if(menuVo.value.children&&menuVo.value.children instanceof Array){
                return menuVo.value.children.length
            }
            return ''; 
        })
        onMounted(()=>{
            emitter.on('getInfo',(oVal,nVal)=>{
                if(oVal){
             menuInfo({id:parseInt(oVal)}).then(res=>{
                if(res.code==200){
                    menuVo.value=res.data;
                }else{
                    
                }
            })
                }
            })
        })
        return {
            menuVo,menuSize,size
        }
    },
})
</script>


<style>

</style>