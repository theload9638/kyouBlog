<template>
  <BackMainHeader/>
  <el-row>
    <el-col :span="6">
      <el-button type="danger" size="large" style="font-size: large;" @click="delBatch"><el-icon><DeleteFilled /></el-icon>批量删除</el-button>
      <el-button type="danger" size="large" style="font-size: large;" @click="clear"><el-icon><CloseBold /></el-icon>清空日志</el-button>
    </el-col>
    <el-col :span="6">
      <el-select v-model="params.opType" placeholder="操作类型" size="large">
    <el-option
      v-for="item in opTypes"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    />
  </el-select>
      <el-select v-model="params.statu" placeholder="执行状态" size="large">
    <el-option
      v-for="item in status"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    />
  </el-select>
    </el-col>
    <el-col :span="6">
      <el-input  v-model="params.uri" style="width: 300px;height: 40px;" placeholder="请求路径" clearable />
      <el-button type="primary" size="large" style="font-size: large;" @click="reset"><el-icon><Refresh /></el-icon>重置</el-button>
      <el-button type="primary" size="large" style="font-size: large;"><el-icon><Search /></el-icon>搜索</el-button>
    </el-col>
  </el-row>
  <el-table
    :data="logData"
    style="width: 100%"
    @selection-change="handleSelectionChange"
  >
  <el-table-column type="selection" width="60" />
  <el-table-column prop="operator" :width="widthData" >
    <template #header="title"><span class="articleList">操作人</span></template>
  </el-table-column>
  <el-table-column prop="uri"  :width="widthData" >
    <template #header="title"><span class="articleList">请求路径</span></template>
  </el-table-column>
  <el-table-column prop="type" :width="widthData" >
    <template #header="title"><span class="articleList">操作类型</span></template>
  </el-table-column>
  <el-table-column prop="ip" :width="widthData" >
    <template #header="title"><span class="articleList">IP地址</span></template>
  </el-table-column>
  <el-table-column prop="runTime" :width="widthData" >
    <template #header="title"><span class="articleList">执行时间</span></template>
  </el-table-column>
  <el-table-column prop="method" :width="widthData" >
    <template #header="title"><span class="articleList">操作方法</span></template>
  </el-table-column>
  <el-table-column prop="params" :width="widthData" >
    <template #header="title"><span class="articleList">请求参数</span></template>
  </el-table-column>
  <el-table-column prop="result" :width="widthData" >
    <template #header="title"><span class="articleList">请求结果</span></template>
  </el-table-column>
  <el-table-column>
<template #header="title"><span class="articleList">操作</span></template>
      <template #default="scope">
        <el-button size="small"
        type="danger"
        style="margin-right:8px;"
         @click="handleRemove(scope.$index, scope.row)"
          >删除</el-button>
      </template>
    </el-table-column>
</el-table>
<el-pagination
      v-model:current-page="params.pageNum"
      v-model:page-size="params.pageSize"
      :page-sizes="[10, 20, 30, 40]"
      :background="true"
      layout="sizes, prev, pager, next"
      :total="total"
      style="justify-content: center;"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
</template>

<script>
import { defineComponent,ref ,onMounted} from 'vue'
import BackMainHeader from '@/components/BackMainHeader.vue'
import {delLog,clearLogs,listLogs} from '@/api/back/log.js'
import { ElMessage } from 'element-plus'
export default defineComponent({
    name:'log',
    components:{
        BackMainHeader
    },
    setup() {
      const reset=()=>{
       params.value={
        pageNum:1,
        pageSize:10,
        opType:'',
        uri:'',
        statu:'',
      }
       ids.value=[];
       getDate();
    }
    const getDate=()=>{
      listLogs(params.value).then(res=>{
        if(res.code==200){
          total.value=res.data.total;
          logData.value=res.data.records;
        }else{
          ElMessage.error(res.msg);
        }
      })
    }
    const handleCurrentChange=(num)=>{
      params.value.pageNum=num;
      getDate();
    }
    onMounted(()=>{
      getDate();
    })
    const handleSizeChange=(size)=>{
      params.value.pageSize=size;
      getDate();
    }
      const widthData=150;
      const ids=ref([]);
      const handleSelectionChange=(rows)=>{
          ids.value=rows.map(row=>row.id);
      }
      const delBatch=()=>{
         if(ids.value.length<=0){
           return;
         }
         delLog(ids.value).then(res=>{
           if(res.code==200){
            ElMessage.success(res.msg);
           }else{
            ElMessage.error(res.msg);
           }
           getDate();
         })
      }
      const handleRemove=(i,r)=>{
         delLog(r.id).then(res=>{
           if(res.code==200){
            ElMessage.success(res.msg);
           }else{
            ElMessage.error(res.msg);
           }
           getDate();
         })
      }
      const clear=()=>{
        clearLogs().then(res=>{
          getDate();
        })
      }
      const opTypes=[
        {
          value:'INSERT'
        },
        {
          value:'DELETE'
        },
        {
          value:'UPDATE'
        }
      ]
      const status=[
        {
          value:'成功',
        },
        {
          value:'失败'
        }

      ]
      const total=ref(0);
      const params=ref({
        pageNum:1,
        pageSize:10,
        opType:'',
        uri:'',
        statu:'',
      })
     const logData=ref([]);

      return {
        logData,handleSelectionChange,widthData,handleRemove,
        delBatch,clear,opTypes,status,params,total,reset,
        handleSizeChange,handleCurrentChange,
      }
    },
})
</script>


<style scoped>
.articleList{
    font-size: x-large;
    font-weight: 900;
}

</style>