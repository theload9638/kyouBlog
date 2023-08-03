<template>
    <BackMainHeader/>
    <div class="contain">
      <div class="left">
  <el-menu
    :default-active="activeMenu"
    class="el-menu-demo"
    mode="horizontal"
    background-color="#545c64"
    text-color="#fff"
    active-text-color="#ffd04b"
    @select="handleSelect"
  >
    <el-menu-item index="1">
        <template #title>菜单详细信息</template>
    </el-menu-item>
    <el-menu-item index="2">
      <template #title>新增菜单</template>
    </el-menu-item>
    <el-menu-item index="3">
        <template #title>修改菜单</template>
    </el-menu-item>
  </el-menu>
    <div v-if="activeMenu=='1'">
      <MenuInfo />
    </div>
    <div v-else-if="activeMenu==2">
      <AddMenuVue :active="'2'"/>
    </div>
    <div v-else>
      <AddMenuVue :active="'3'"/>
    </div>
    </div>
    <div class="right">
        <el-table
      :data="treeList"
      class="tableTree"
      row-key="id"
      border
      highlight-current-row
    >
      <el-table-column prop="id" label="id" sortable />
      <el-table-column prop="label" label="label" sortable />
      <el-table-column align="right"> 
      <template #default="scope">
        <el-button size="small"
        style="margin-right:8px;"
         @click="handleInfo(scope.$index, scope.row)"
          >详细</el-button
        >
        <el-button size="small"
         @click="handleEdit(scope.$index, scope.row)"
          >修改</el-button
        >
        <el-button
          size="small"
          type="danger"
          @click="remove(scope.$index, scope.row)"
          >删除</el-button
        >
        <el-button
          size="small"
          type="primary"
          @click="append(scope.$index, scope.row)"
          >新增</el-button
        >
      </template>
    </el-table-column>
    </el-table>
    </div>
    </div>
</template>

<script >
import { defineComponent,ref,onMounted,onBeforeUnmount } from 'vue'
import {menuList,delMenu,getMenuInfo} from '@/api/back/menu'
import BackMainHeader from "@/components/BackMainHeader.vue";
import { ElMessage } from 'element-plus'
import MenuInfo from '@/components/menu/MenuInfo.vue'
import emitter from '@/utils/event-bus.js'
import AddMenuVue from '@/components/menu/AddMenu.vue';
export default defineComponent({
    name:'Menu',
    components:{
        BackMainHeader,AddMenuVue,MenuInfo
    },
    setup(props,context) {
        let activeMenu=ref('1');
        let search=ref('')
        let treeList=ref([]);
        const handleInfo=(index,data)=>{
           emitter.emit('getInfo',data.id);
           activeMenu.value='1';
        }
        //处理子节点数据
        const handlerChild=(par,node,data)=>{
            if(data&&data instanceof Array&&data.length>0){
                data.map(child=>{
                    let treeNode={
                        id:child.id,
                        label:child.name,
                        children:[]
                    };
                    handlerChild(child,treeNode,child.children)
                    node.children.push(treeNode);
                })
            }
        }
        //处理树形数据
        const handlerData =(data)=>{
           if(data&&data instanceof Array&&data.length>0){
            const list=data.map(item=>{
                const node={
                    id:item.id,
                    label:item.name,
                    children:[]
                }
                handlerChild(item,node,item.children);
                return node;
              });
              treeList.value=list;
           }
        }
        // 获取树形菜单
        const handlerMenu=()=>{
            menuList().then(res=>{
                handlerData(res.data);
            })
        }
        //菜单激活回调
        const handleSelect=(a)=>{
          activeMenu.value=a;
        }
        onMounted(()=>{
          emitter.on('updateMenu',handlerMenu)
           handlerMenu();
            
            
        })
        onBeforeUnmount(()=>{
          emitter.all.clear()
        })
       const append=(index,data)=>{
        activeMenu.value='2';
        emitter.emit('sendPid',data.id)
       }
       const remove=(index,data)=>{
        if(data.children.length>0){
          ElMessage({
          message: '该菜单项包含子项，请先删除子菜单项',
          type: 'warning',
        })
        return;
        }
        delMenu(data.id).then(res=>{
          ElMessage({
          message: res.msg,
          type: 'success',
        })
         handlerMenu();
         localStorage.removeItem('routeList');
        })
       }
       const handleEdit=(index,data)=>{
        activeMenu.value='3'
        getMenuInfo(data.id).then(res=>{
          if(res.code==200){
            emitter.emit('editer',res.data);
          }
        })

       }

       return {
        treeList,append,activeMenu,handleInfo,
        remove,handleEdit,search,handleSelect,
        
       }
    },
})
</script>


<style scoped>
.contain{
    top: 0px;
    right:0px;
    bottom: 0px;
    left: 0px;
    background-color: white;
}
.left{
    left: 0px;
    position:absolute;
    bottom: 0px;
    height: 100%;
    top: 100px;
    width: 50%;
    margin-left: 0px;
}
.right{
    right: 0px;
    position:absolute;
    bottom: 0px;
    height: 100%;
    top: 100px;
    width: 50%;
    margin-right: 0px;
    padding-right: 0px;
}

.tableTree{
    width: 100%; 
    margin-bottom: 20px;
}

</style>