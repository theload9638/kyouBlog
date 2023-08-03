<template>
  <BackMainHeader/>
  <el-row>
    <el-col :span="6">
      <el-button type="primary" size="large" style="font-size: large;" @click="reset"><el-icon><Refresh /></el-icon>重置</el-button>
    </el-col>
  </el-row>
  <el-table
    :data="logData"
    style="width: 100%"
    @selection-change="handleSelectionChange"
  >
  <el-table-column type="selection" width="60" />
  <el-table-column prop="articleId" :width="widthData" >
    <template #header="title"><span class="articleList">文章id</span></template>
  </el-table-column>
  <el-table-column prop="title"  :width="widthData" >
    <template #header="title"><span class="articleList">文章标题</span></template>
  </el-table-column>
  <el-table-column prop="userId" :width="widthData" >
    <template #header="title"><span class="articleList">用户id</span></template>
  </el-table-column>
  <el-table-column prop="content" :width="widthData" >
    <template #header="title"><span class="articleList">内容</span></template>
  </el-table-column>
  <el-table-column prop="ip" :width="widthData" >
    <template #header="title"><span class="articleList">IP地址</span></template>
  </el-table-column>
  <el-table-column prop="address" :width="widthData" >
    <template #header="title"><span class="articleList">浏览器信息</span></template>
  </el-table-column>
  <el-table-column prop="publishTime" :width="widthData" >
    <template #header="title"><span class="articleList">发布时间</span></template>
  </el-table-column>
  <el-table-column prop="likes" :width="widthData" >
    <template #header="title"><span class="articleList">点赞量</span></template>
  </el-table-column>
  <el-table-column>
<template #header="title"><span class="articleList">操作</span></template>
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
import { defineComponent,ref,onMounted } from 'vue'
import BackMainHeader from '@/components/BackMainHeader.vue'
import {listComments} from '@/api/back/comment'
import { ElMessage } from 'element-plus'

export default defineComponent({
    name:"comments",
    components:{
        BackMainHeader,
    },
    setup() {
        const widthData=150;
        const logData=ref([]);
        const handleCurrentChange=(num)=>{
            params.value.pageNum=num;
            getDataList();
        }
        const handleSizeChange=(size)=>{
            params.value.pageSize=size;
            getDataList();
        }
        //获取列表数据
        const getDataList=()=>{
            listComments(params.value.pageNum,params.value.pageSize).
            then(res=>{
                logData.value=res.data.records;
                total.value=res.data.total;
            })
        }
        onMounted(()=>{
            getDataList();
        })
        const total=ref(0);
        const params=ref({
            pageNum:1,
            pageSize:10,
        })
        const ids=ref([])
        const handleSelectionChange=(rows)=>{
            ids.value=rows.map(row=>row.id);
        }
        const reset=()=>{
            params.value={
            pageNum:1,
            pageSize:10,
        }
        }
        return {
            logData,params,handleCurrentChange,
            handleSizeChange,widthData,total,handleSelectionChange
            ,reset

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