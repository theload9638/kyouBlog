<template>
    <el-radio-group 
    @change="cr"
    v-model="labelPosition" label="选项">
      <el-radio-button label="M">目录</el-radio-button>
      <el-radio-button label="C">菜单</el-radio-button>
      <el-radio-button label="F">按钮</el-radio-button>
    </el-radio-group>
    <el-form
    ref="form"
    :model="menuObj"
    label-width="auto"
    label-position="top"
    size="default">
    <el-form-item label="父菜单id">
      <el-input v-if="labelPosition=='M'" 
      v-model="menuObj.parentId" 
      :value="0" disabled/>
      <el-input v-else 
      v-model="menuObj.parentId"  disabled/>
    </el-form-item>
    <el-form-item label="名称">
      <el-input v-model="menuObj.name" />
    </el-form-item>
    <el-form-item label="菜单图标">
      <el-select
        v-model="menuObj.icon"
        placeholder="请选择图标"
      >
      <el-option
      v-for="(item) in icons"
      :key="item"
      :label="item"
      :value="item"
    />
      </el-select>
    </el-form-item>
    <el-form-item v-if="labelPosition!='F'" label="菜单路径">
        <el-input
         placeholder="例如：user"
         v-model="menuObj.path" />
    </el-form-item>
    <el-form-item v-if="labelPosition=='C'" label="组件路径">
        <el-input 
        placeholder="例如： sys/user/index" 
        v-model="menuObj.component" />
    </el-form-item>
    <el-form-item label="菜单类型">
        <el-input v-model="labelPosition" disabled/>
    </el-form-item>
    <el-form-item label="可见状态">
        <el-switch v-model="menuObj.visible" />
    </el-form-item>
    <el-form-item label="启用状态">
        <el-switch v-model="menuObj.status" />
    </el-form-item>
    <el-form-item label="权限标识">
        <el-input 
        placeholder="例如: sys:user:add"
        v-model="menuObj.perms" />
    </el-form-item>
    <el-form-item>
      <el-button 
      type="primary" 
      @click="onSubmit">保存</el-button>
      <el-button @click="cancel">取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script >
import { defineComponent,ref,onMounted,
    onBeforeUnmount
} from 'vue'
import {addMenu,updMenu} from '@/api/back/menu'
import emitter from '@/utils/event-bus.js'

export default defineComponent({
    name:'AddMenu',
    props:['active'],
    setup(props,context) {
        const pid=ref(0);
        const originData=ref({})
        const mode=ref('2')
        onMounted(()=>{
            mode.value=props.active;
            emitter.on('editer',(data)=>{
                labelPosition.value=data.type
                mode.value='3';
                originData.value=data;
                let menuData=JSON.parse(JSON.stringify(data));
                menuObj.value=menuData;
                if(menuData.status==0){
                    menuObj.value.status=true;
                }else{
                    menuObj.value.status=false;
                }
                if(menuData.visible==0){
                    menuObj.value.visible=true;
                }else{
                    menuObj.value.visible=false;
                }


            })
            emitter.on('sendPid',(id)=>{
                pid.value=parseInt(id);
                mode.value='2';
                menuObj.value.parentId=parseInt(id)
                labelPosition.value='C'
            })
        })
        onBeforeUnmount(()=>{
            emitter.all.clear();
        })
        let labelPosition=ref('M');
        const icons=ref([
          'Calendar','Avatar','Tools','HelpFilled','Comment',
          'Menu','User','Male','Star','Discount','Share','DeleteFilled',
          'Sort','List','Monitor','Picture','Coffee','Apple','Shop',
          'Moon','Lightning','Pouring','Sunny','Ship','ShoppingBag'
        ]);
        const menuObj=ref({
            parentId:0,
            name:'',
            path:'',
            component:'',
            type:'M',
            visible:0,
            status:0,
            perms:'',
            icon:''
        })
        const cancel=()=>{
            labelPosition.value='M'
            menuObj.value={
            parentId:0,
            name:'',
            path:'',
            component:'',
            type:'',
            visible:0,
            status:0,
            perms:'',
            icon:''
            }
        }
        const onSubmit=()=>{
            if(menuObj.value.status==0||menuObj.value.status==true){
                menuObj.value.status=0;
            }else{
                menuObj.value.status=1
            }
            if(menuObj.value.visible==0|| menuObj.value.visible==true){
                menuObj.value.visible=0;
            }else{
                menuObj.value.visible=1;
            }
            menuObj.value.type=labelPosition;
            if(mode.value=='2'){
                addMenu(menuObj.value).then(res=>{
                if(res.code==200){
                    cancel()
                    emitter.emit('updateMenu');
                    localStorage.removeItem('routeList')
                }
            })
            }else if(mode.value=='3'){
                updMenu(menuObj.value).then(res=>{
                    if(res.code==200){
                        cancel()
                    emitter.emit('updateMenu');
                    localStorage.removeItem('routeList')
                }
                })
            }
           
        }
        const cr=()=>{
            if(pid.value==0){
                menuObj.value={
            parentId:0,
            name:'',
            path:'',
            component:'',
            type:'M',
            visible:0,
            status:0,
            perms:'',
            icon:''
        }
            }else if(pid.value){
                menuObj.value={
            parentId:pid.value,
            name:'',
            path:'',
            component:'',
            type:'C',
            visible:0,
            status:0,
            perms:'',
            icon:''
        }
            }
        }
        return {
            labelPosition,menuObj,icons,
            onSubmit,cancel,cr

        }
    },
})
</script>


<style>

</style>