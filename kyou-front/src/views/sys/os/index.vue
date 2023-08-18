<template>
  <BackMainHeader/>
 <div style="background-color: lightgray;height: 100%;">
     <!-- 第一行 -->
  <el-tabs v-model="activeName" :stretch="true" type="card" @tab-click="handleClick" >
    <el-tab-pane name="3">
        <template #label><span class="xmal">已发布</span></template>
    </el-tab-pane>
    <el-tab-pane name="2">
        <template #label><span class="xmal">待审核</span></template>
    </el-tab-pane>
    <el-tab-pane label="草稿" name="1">
        <template #label><span class="xmal">草稿</span></template>
    </el-tab-pane>
    <el-tab-pane  name="4">
        <template #label><span class="xmal">审核未通过</span></template>
    </el-tab-pane>
    <el-tab-pane name="0">
        <template #label><span class="xmal">已下架</span></template>
    </el-tab-pane>
  
</el-tabs>
<!-- 第二行 -->
   <el-row :gutter="24">
     <el-col :span="4">
    <el-button type="primary" size="large" style="font-size: large;width: 100px;height: 50px;margin-top: -109px;" @click="addArticle"><el-icon><Plus /></el-icon>新增</el-button>
    </el-col>
     <el-col :span="4">
        <el-button type="danger" size="large" style="font-size: large;width: 100px;height: 50px;position: absolute;margin-top: 3px;margin-left: -140px;" @click="delBatch"><el-icon><DeleteFilled /></el-icon>批量删除</el-button>
    </el-col>
     <el-col :span="4" style="margin-top: -50px;">
        <el-input style="width: 300px;height: 50px;margin-left: -200px;" v-model="key" placeholder="搜索关键字"></el-input>
    </el-col>
     <el-col :span="4">
        <el-select v-model="contentType" style="margin-top: -50px;width: 200px;height: 90px;margin-left: -100px;" placeholder="搜索内容" size="large">
    <el-option
      v-for="item in option1"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    />
  </el-select>
    </el-col>
     <el-col :span="4">
        <el-select v-model="orderField" style="margin-top: -49px;width: 200px;height: 90px;margin-left: -130px;" placeholder="排序字段" size="large">
    <el-option
      v-for="item in option2"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    />
  </el-select>
    </el-col>
     <el-col :span="4">
        <el-button type="primary" style="font-size: x-large;height: 50px;margin-top: -100px;" @click="search"><el-icon><Search /></el-icon>搜索</el-button>
        <el-button type="primary" style="font-size: x-large;height: 50px;margin-top: -100px;" @click="reset"><el-icon><Refresh /></el-icon>重置</el-button>
    </el-col>
   </el-row>  
   <!-- 展示数据 -->
   <el-table
    :data="articleList" 
    style="width: 100%"
    @selection-change="handleSelectionChange"
  >
  <el-table-column type="selection" width="55" />
    <el-table-column prop="author" >
        <template #header="title"><span class="articleList">作者</span></template>
    </el-table-column>
    <el-table-column prop="title" >
        <template #header="title"><span class="articleList">标题</span></template>
    </el-table-column>
    <el-table-column prop="summary" >
        <template #header="title"><span class="articleList">内容</span></template>
    </el-table-column>
    <el-table-column prop="comment">
        <template #header="title"><span class="articleList">评论</span></template>
    </el-table-column>
    <el-table-column prop="viewNum">
        <template #header="title"><span class="articleList">访问量</span></template>
    </el-table-column>
    <el-table-column prop="publishDate">
        <template #header="title"><span class="articleList">发布日期</span></template>
    </el-table-column>
    <el-table-column>
        <template #header="title"><span class="articleList">操作</span></template>
      <template #default="scope">
        <el-button size="small"
        style="margin-right:8px;"
        v-if="activeName=='3'"
         @click="handleInfo(scope.$index, scope.row)"
          >查看</el-button
        >
        <el-button size="small"
         @click="handleEdit(scope.$index, scope.row)"
          >编辑</el-button
        >
        <el-button
          size="small"
          type="danger"
          v-if="activeName!='0'&&activeName!='4'&&activeName!='1'&&moreAuth"
          @click="remove(scope.$index, scope.row)"
          >下架</el-button
        >
        <el-button
          size="small"
          type="primary"
          v-if="activeName=='2'&&moreAuth"
          @click="handle(scope.$index, scope.row)"
          >审批</el-button
        >
      </template>
    </el-table-column>
  </el-table>
  <el-pagination 
  background 
  style="margin-left: 40%;"
  layout="prev, pager, next"
  v-model:current-page="pageNum"
  @current-change="handleCurrentChange"
   :total="total" />
 </div>
</template>

<script>
import { defineComponent,onMounted,ref} from 'vue'
import BackMainHeader from '@/components/BackMainHeader.vue'
import { ElMessage } from 'element-plus'
import router from '@/router'
import {isAudit} from '@/api/back/role'
import {pageArticle,
    validArticle,takeDownArticle,
    delArticles,updArticle,
} from '@/api/back/article'
export default defineComponent({
    name:'index',
    components:{BackMainHeader},
    setup() {
        onMounted(()=>{
            hasMoreAuth();
            getArticleList();
        })
        const moreAuth=ref(false);
        const hasMoreAuth=()=>{
            isAudit().then(res=>{
                if(res.code==200){
                    moreAuth.value=res.data;
                }
            })
        }
        //激活的菜单
        const activeName=ref('3');
        //改变菜单
        const handleClick=(a)=>{
            activeName.value=a.props.name;
            getArticleList();
        }
        //选中的多选id
        const ids=ref([]);
        //选中事件
        const handleSelectionChange=(rows)=>{
           ids.value=rows.map(row=>row.id); 
        }
        //获取文章列表
        const getArticleList=()=>{
            const params={
                pageNum:pageNum.value,
                pageSize:10,
                kw:key.value,
                type:contentType.value,
                order:orderField.value,
                status:activeName.value
            }
            pageArticle(params).then(res=>{
                articleList.value=res.data.records;
                total.value=res.data.total;
            })
        }
        //总数
        const total=ref(0)
        //页码
        const pageNum=ref(1);
        const handleCurrentChange=(num)=>{
            pageNum.value=num;
        }
        //搜索
        const search=()=>{
            getArticleList();
        }
        //排序值
        const orderField=ref('')
        //文章列表
        const articleList=ref([
            
        ])
        //搜索类型
        const contentType=ref('');
        //搜索内容
        const option1=ref([
            {
                label:'标题',
                value:'0'
            },
            {
                label:'内容',
                value:'1'
            }
        ]);
        const option2=ref([
        {
                label:'发布时间',
                value:'0'
            },
            {
                label:'访问量',
                value:'1'
            },
            {
                label:'点赞量',
                value:'2'
            },
            {
                label:'评论数',
                value:'3'
            }
        ])
        //关键字
        const key=ref('')
        //重置
        const reset=()=>{
            key.value='';
            orderField.value='';
            contentType.value=''
            getArticleList();
        }
        //查询函数
        const handleInfo=(i,r)=>{
            if(r.id==3){
                router.push({name:'front'});
            }else{
                router.push({path:'/article',query:{'id':r.id}});
            }    
        }
        //编辑文字
        const handleEdit=(i,r)=>{
            router.push({path:'/menuBoard/publish',query:{edit:r.id}})
        }
        //审批
        const handle=(i,r)=>{
            validArticle(r.id).then(res=>{
                if(res.code==200){
                    ElMessage.success(res.msg);
                }else{
                    ElMessage.error(res.msg);
                }
                getArticleList();
            })
        }
        //下架文章
        const remove=(i,r)=>{
            takeDownArticle(r.id).then(res=>{
                if(res.code==200){
                    ElMessage.success(res.msg);
                }else{
                    ElMessage.error(res.msg);
                }
                getArticleList();
            })
        }
        //新增文章
        const addArticle=()=>{
            router.push({path:'/menuBoard/publish'})
        }
        //批量删除
        const delBatch=()=>{
            if(ids.value.length<=0){
                ElMessage.info('请选择要删除的文章');
                return;
            }
            if(ids.value.includes(3)){
                ElMessage.error('无法删除留言板');
                return;
            }
            delArticles(ids.value).then(res=>{
                if(res.code==200){
                    ElMessage.success(res.msg);
                }else{
                    ElMessage.error(res.msg);
                }
                getArticleList();
            })
        }
        return {
            activeName,handleClick,key,option1,contentType,option2,
            articleList,handleSelectionChange,search,orderField,handleCurrentChange,
            pageNum,total,handleInfo,handle,remove,addArticle,delBatch,
            handleEdit,reset,moreAuth


        }
    },
})
</script>


<style scoped>
.xmal{
    font-size: x-large;
    font-weight: 900;
}
.articleList{
    font-size: x-large;
    font-weight: 900;
}
</style>