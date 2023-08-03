<template>
    <BackMainHeader />
    <el-table :data="tableData" border style="width: 100%">
    <el-table-column prop="id" label="编号" width="180" />
    <el-table-column prop="name" label="角色名称" width="180" />
    <el-table-column label="操作" class="in">
      <template #default="scope">
     <el-button link type="primary" size="small" @click="assignPerms(scope.row)">分配权限</el-button>      </template>
    </el-table-column>
  </el-table>
  <el-dialog
    v-model="centerDialogVisible"
    title="分配权限"
    width="60%"
    draggable
    align-center
  >
  <el-tree
  ref="treeRef"
    :data="treeData"
    :show-checkbox="true"
    node-key="id"
    default-expand-all
    :props="props"
    :check-strictly="true"
    :check-on-click-node="true"
    highlight-current
    :default-checked-keys="assigned"
  >
  <template #default="{ node, data }">
        <span class="custom-tree-node">
          <span class='labes'>{{ node.label }}</span>
        </span>
      </template>
</el-tree>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="assign">
          确认分配
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { defineComponent,ref,onMounted } from 'vue'
import BackMainHeader from '@/components/BackMainHeader.vue';
import {getRoles} from '@/api/back/role.js'
import { ElMessage } from 'element-plus'
import {menuList,getPerms,assignMenuPerms} from '@/api/back/menu.js'
export default defineComponent({
    name:'auth',
    components:{
        BackMainHeader,
    },
    setup() {
        //角色已分配的权限
        const assigned=ref([])
        const treeRef=ref(null);
        //可视按钮
        const centerDialogVisible = ref(false)
        const tableData=ref([]);
        //获取角色列表
        const getRoleList=()=>{
            getRoles().then(res=>{
                tableData.value=res.data;
            }) 
        }
        //树形数据
        const treeData=ref([]);
        //属性
        const props = {
        id: 'id',
        label: 'label',
        children: 'children',
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
              treeData.value=list;
           }
        }
        const curRoleID=ref(undefined)
        //分配权限的弹窗显示
        const assignPerms=(row)=>{
            curRoleID.value=row.id;
            getPerms(row.id).then(res=>{                
                assigned.value=res.data.map(i=>i.id);
            })
            menuList().then(res=>{
                if(res.code==200){
                    handlerData(res.data);
                }
            })
           
            centerDialogVisible.value=true
        }
        //取消分配
        const cancel=()=>{
            centerDialogVisible.value= false
            assigned.value=[];
            treeData.value=[];
            curRoleID.value=undefined;
        }
        //上传分配
        const assign=()=>{
            const ids=treeRef.value.getCheckedNodes(false,true).map(i=>i.id);
            // const checked=treeRef.value.getCheckedKeys(false,true);
            assignMenuPerms(curRoleID.value,ids).then(res=>{
                if(res.code!=200){
                    ElMessage.error(res.msg);
                }
            })
            centerDialogVisible.value=false;
        }
        onMounted(()=>{
            getRoleList();
            
            
        })
        

        return {
            getRoleList,tableData,centerDialogVisible,assignPerms,
            cancel,assign,treeData,props,treeRef,assigned
        }
    },
})
</script>


<style>
.dialog-footer button {
  margin-right: 10px;
  font-size: large;
}
.labes{
    font-size: x-large;
    font-weight: 500;
    margin-left: 20px;
}


</style>