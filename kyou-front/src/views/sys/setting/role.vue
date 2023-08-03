<template>
  <BackMainHeader/>
  <div class="container">
    <div class="left">
      <h1 class="title">角色操作</h1>
      <h2 class="errMsg">{{ errMsg }}</h2>
      <el-switch 
      v-model="form.type" 
      class="ro"
      size="large"
      active-text="修改" 
      inactive-text="新增" />
      <el-form :model="form" 
      label-width="120px">
        <el-form-item label="角色名称">
      <el-input v-model="form.name" class="oin"/>
    </el-form-item>
    <el-form-item label="权限标识">
      <el-input v-model="form.keyName" class="oin"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">保存</el-button>
    </el-form-item>
      </el-form>
  </div>
  <div class="right">
    <el-table 
    :data="tableData" 
    @selection-change="handleSelectionChange"
    cell-class-name="in"
    border
    row-class-name="in"
     style="width: 100%;height:80%;"
    >
    <el-table-column type="selection" width="50" />
    <el-table-column prop="id"  label="编号" />
    <el-table-column prop="name" label="角色名" />
    <el-table-column prop="keyName" label="权限标识" />
  <el-table-column prop="createTime" label="创建时间" />
    <el-table-column label="操作" class="in">
      <template #header>
        <el-input 
        v-model="search" 
        @keydown.enter="getRoleList(search)"
        size="large" 
        placeholder="查询角色" />
      </template>
      <template #default="scope">
     <el-button link type="warning" size="small" @click="handleDel(scope.row)">删除</el-button>
     <el-button link type="primary" size="small" @click="handleUpd(scope.row)">修改</el-button>
      </template>
    </el-table-column>
  </el-table>
  </div>
  </div>
</template>

<script >
import { defineComponent,ref,onMounted } from 'vue'
import BackMainHeader from '@/components/BackMainHeader.vue'
import {getRoles,delRoles,updRole,addRole} from '@/api/back/role.js'
export default defineComponent({
    name:'Role',
    components:{
        BackMainHeader
    },
    setup() {
      const errMsg=ref('')
        const search=ref('')
        const tableData=ref([]);
        const form=ref({
          id:undefined,
          name:'',
          keyName:'',
          type:false,
        })
        const handleDel=(row)=>{
          delRoles(row.id).then(res=>{
            if(res.code==200){
              getRoleList();
            }
          })
        }
        const handleUpd=(row)=>{
          form.value.id=row.id;
          form.value.type=true;
          form.value.name=row.name;
          form.value.keyName=row.keyName;
        }
        const onSubmit=()=>{
          if(form.value.keyName==''||form.value.keyName.trim().length<=0){
            errMsg.value='角色key不能为空'
              return;
            }else{
              errMsg.value=''
            }
            if(form.value.name==''||form.value.name.trim().length<=0){
              errMsg.value='角色名称不能为空'
              return;
            }else{
              errMsg.value=''
            }
          if(errMsg.value==''){
            if(form.value.type){
            //修改
            updRole(form.value).then(res=>{
              if(res.code==200){
                getRoleList('');
                errMsg.value=''
              }else{
                errMsg.value=res.msg;
              }
            })
            
          }else{
            addRole(form.value).then(res=>{
              if(res.code==200){
                getRoleList('');
                errMsg.value=''
              }else{
                errMsg.value=res.msg;
              }
            })
          }
          }
        }
        const getRoleList=(data)=>{
          getRoles(data).then(res=>{
            if(res.code==200){
              tableData.value=res.data;
            }
          })
        }
        const handleSelectionChange=(row)=>{
            
        }
        onMounted(()=>{
          getRoleList(search.value);
        })

        return {
          tableData,handleDel,handleUpd,handleSelectionChange
          ,form,onSubmit,search,getRoleList,errMsg
        }
    },
})
</script>


<style scoped>
.errMsg{
  color:red;
  margin-top: 0px;
  margin-bottom: 0px;
  padding-top: 0px;
  padding-bottom: 0px;
  font-size: xx-large;
  font-weight: 900;
  
}
.ro{
  left: 0px;
  margin-left: 0px;  
}
.oin{
  width: 700px;
  align-content: center;
  margin-left: 80px;
  margin-right: 120px;
}
.title{
  font-size: xx-large;
  font-weight: 900;
  left: 0px;
  top: 0px;
  margin-left: 0px;
  padding-left: 0px;
}
.container{
  top: 0px;
    right:0px;
    bottom: 0px;
    left: 0px;
    background-color: white;
}
.left{
  left: 0px;
  background-color: lightgoldenrodyellow;
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
.in{
  height: 50px;
}
</style>