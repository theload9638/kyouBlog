<template>
  <el-table 
  :data="tableData" 
  style="width: 100%"
  ref="multipleTableRef"
    @selection-change="handleSelectionChange"
  >
  <el-table-column type="selection" width="55" />
    <el-table-column label="编号" :width="width">
      <template #default="scope">
        <span class="username">{{ scope.row.id }}</span>
      </template>
    </el-table-column>
    <el-table-column label="用户名" :width="width">
      <template #default="scope">
        <span class="username">{{ scope.row.username }}</span>
      </template>
    </el-table-column>
    <el-table-column label="邮箱号" :width="width">
      <template #default="scope">
        <span class="username">{{ scope.row.email }}</span>
      </template>
    </el-table-column>
    <el-table-column label="性别"  :width="width">
      <template #default="scope">
        <span v-if="scope.row.gender=='0'" class="username">女</span>
        <span v-else-if="scope.row.gender=='1'" class="username">男</span>
        <span v-else class="username">未知</span>
      </template>
    </el-table-column>
    <el-table-column label="昵称" :width="width">
      <template #default="scope">
        <span class="username">{{ scope.row.nickname }}</span>
      </template>
    </el-table-column>
    <el-table-column label="头像" prop="headImage" :width="width">
      <template #default="scope">
        <span v-if="scope.row.headImage!=null">
          <el-image style="width: 100px; height: 100px" :src="scope.row.headImage" fit="cover" />
        </span>
        <span v-else class="errImg">
          <el-icon><icon-picture /></el-icon>
        </span>
      </template>
    </el-table-column>
    <el-table-column label="账号状态" :width="width">
      <template #default="scope">
       <span v-if="scope.row.status==0" class="status">已启用</span>
       <span v-else class="statusErr">已禁用</span>
      </template>
    </el-table-column>
    <el-table-column label="是否激活" :width="width">
      <template #default="scope">
       <span v-if="scope.row.delFlag=='0'" class="status">已激活</span>
       <span v-else class="statusErr">已注销</span>
      </template>
    </el-table-column>
    <el-table-column label="上次登录IP" prop="lastIp" :width="width"/>
    <el-table-column label="备注" prop="remark" :width="width"/>
    <el-table-column label="操作">
      <template #default="scope">
        <el-button 
        link
        size="large"
        type="primary"
         @click="handleEdit(scope.$index, scope.row)"
          >修改</el-button
        >
        <span v-if="scope.row.status=='0'">
          <el-button 
        link
        size="large"
        type="warning"
         @click="handleStatus(scope.$index, scope.row)"
          >禁用</el-button>
        </span>
        <span v-else>
          <el-button 
        link
        size="large"
        type="primary"
         @click="handleStatus(scope.$index, scope.row)"
          >启用</el-button>
        </span>
        <span v-if="scope.row.delFlag=='0'">
          <el-button
        link
          size="large"
          type="danger"
          @click="handleDelete(scope.$index, scope.row)"
          >注销此账号</el-button>
        </span>
        <span v-else>
          <el-button
          link
          size="large"
          type="primary"
          @click="handleDelete(scope.$index, scope.row)"
          >激活此账号</el-button>
        </span>

      </template>
    </el-table-column>
  </el-table>
  <el-pagination
      class="page"
      v-model:current-page="defaultQueryParam.pageNum"
      v-model:page-size="defaultQueryParam.pageSize"
      :page-sizes="[10, 20, 30, 40]"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
</template>

<script>
import { 
  defineComponent,ref ,onMounted,onBeforeUnmount,
} from 'vue'
import router from '@/router'
import { ElMessage } from 'element-plus'
import emitter from '@/utils/event-bus.js'
import { Picture as IconPicture } from '@element-plus/icons-vue'
import {listUsers,editUserStatus,dels} from '@/api/back/user.js'
export default defineComponent({
    name:'BackUserList',
    components:{
      IconPicture,
    },
    setup(props,context) {
      //列表单元格宽度
      const width=ref('130');
      const total=ref(0);
      //搜索参数
      const defaultQueryParam=ref({
        pageNum:1,
        pageSize:10,
        status:'',
        gender:'',
        key:''
      });
onMounted(()=>{
  //绑定重置事件和搜索事件
  emitter.on('refresh',getUserList);
  emitter.on('search',getUserList);
  //永久删除
  emitter.on('delAll',()=>{
      if(uids.value.length==0){
        return;
      }
    dels(uids.value).then(res=>{
      ElMessage.success(res.msg);
      getUserList();
    })
  })
  getUserList();
})
//注销事件
onBeforeUnmount(()=>{
  emitter.all.clear();
})
//列表数据获取
const getUserList=(data)=>{
  if(data!=undefined){
    if(data.pageNum&&data.pageSize){
      defaultQueryParam.value=data;
    }else{
      defaultQueryParam.value.status=data.status;
     defaultQueryParam.value.gender=data.gender;
     defaultQueryParam.value.key=data.key;
    }
  }else{
    data=defaultQueryParam.value;
  }
  listUsers(data).then(res=>{
    total.value=res.data.total;
    tableData.value=res.data.records;
  })
}
const multipleTableRef = ref(null)    
//列表数据    
const tableData=ref([])
//多选的用户编号
const uids=ref([])
        
// 修改
const handleEdit = (index, row )=> {
  router.push({path:'/user/userInfo',query:{id:row.id}})
}
// 状态修改
const handleStatus = (index, row )=> {
  let status;
  if(row.status==1){
    status=0;
  }else{
    status=1;
  }
  editUserStatus(row.id,status,row.delFlag)
  .then(res=>{
    if(!res||res.code!=200){
       ElMessage.info(res.msg);
    }
    getUserList();
  })
}
// 注销
const handleDelete = (index, row) => {
  const delFlag=(row.delFlag=='1')?"0":'1';
  editUserStatus(row.id,row.status,delFlag)
  .then(res=>{
    if(!res||res.code!=200){
       ElMessage.info(res.msg);
    }
    getUserList();
  })
}
//多选框事件
const handleSelectionChange = (rows) => {
  uids.value=[];
   for(let row of rows){
    uids.value.push(row.id);
   }
}
//改变搜索大小
const handleSizeChange=(size)=>{
  defaultQueryParam.value.pageSize=size;
  getUserList();
}
//改变页码
const handleCurrentChange=(num)=>{
  defaultQueryParam.value.pageNum=num;
  getUserList();
}
return {
    handleEdit, handleDelete,tableData,
    handleSelectionChange,multipleTableRef,
    width,handleStatus,defaultQueryParam,
    total,handleSizeChange,handleCurrentChange,
}
    },
})
</script>


<style scoped>
.errImg{
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: var(--el-fill-color-light);
  color: var(--el-text-color-secondary);
  font-size: 130px;
}
.username{
  font-size: large;
  font-weight: 600;
}
.status{
  font-size: large;
  font-weight: 600;
  color: skyblue;
}
.statusErr{
  font-size: large;
  font-weight: 600;
  color: red;
}
.page{
  margin-left: 35%;
  font-size: x-large;
  font-weight: 600;
  width: 200px;
  height: 100px;
}
</style>