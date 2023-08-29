<template>
  <BackMainHeader/>
     <el-row :gutter="24" style="height: 100%;">
       <el-col :span="12">
    <el-card style="width: 100%;height: 100%;background-color: lightblue;">
        <h1 style="font-size: xx-large;">修改分类</h1>
       <div>
        <span style="font-size: x-large;">分类名称</span>
        <el-input
        style="height: 50px;width: 90%;"
         v-model="categoryItem.name" 
        placeholder="请输入分类名称"/>
       </div>
       <div>
        <span style="font-size: x-large;">父节点</span>
        <el-select v-model="categoryItem.pid" 
        style="width: 90%;"
   placeholder="选择分类父节点" size="large">
    <el-option
      v-for="item in selectList"
      :key="item.id"
      :label="item.name"
      :value="item.id"
    />
  </el-select>
       </div>
       <div>
        <span style="font-size: large;">描述(可选)</span>
        <el-input
        type="textarea"  :rows="5"
        style="height: 50px;width: 85%;"
         v-model="categoryItem.description" 
        placeholder="请输入分类描述"/>
       </div>
       <div>
        <el-button 
        style="margin-top: 50px;"
        type="primary"
        @click="save"
        >确认添加</el-button>
        <el-button 
        v-if="edit"
        style="margin-top: 50px;"
        type="primary"
        @click="backAdd"
        >返回添加</el-button>
       </div>
    </el-card>
    </el-col>
       <el-col :span="12" >
        <el-card style="width: 100%;height: 100%;background-color: lightgoldenrodyellow;">
            <el-table
      :data="treeList" class="tableTree"
      row-key="id"
      border
      highlight-current-row
    >
      <el-table-column prop="id" label="编号" sortable />
      <el-table-column prop="name" label="名称" sortable />
      <el-table-column prop="articleNum" label="文章数量" sortable />
      <el-table-column align="right">
      <template #default="scope">
        <el-button size="small"
         @click="handleEdit(scope.$index, scope.row)"
          >修改</el-button>
        <el-button
          size="small"
          type="danger"
          @click="remove(scope.$index, scope.row)"
          >删除</el-button>
      </template>
    </el-table-column>
    </el-table>
  </el-card>
    </el-col>
     </el-row>
</template>

<script>
import { defineComponent,ref,onMounted } from 'vue'
import BackMainHeader from '@/components/BackMainHeader.vue'
import { ElMessage } from 'element-plus'
import {listCategory,getOneCategory,
    delCategory,getSelectList,addCategory,
    editCategory,

} from '@/api/back/category'
export default defineComponent({
    components:{
        BackMainHeader,
    },
    name:'category',
    setup() {
        //下拉框分类列表
        const selectList=ref([]);
        //分类名称
        const categoryItem=ref({
            id:undefined,
            name:'',
            pid:undefined,
            description:'',
            pid:undefined,

        });
        //分类修改操作类型
        const edit=ref(false);
        //分类列表
        const treeList=ref([]);
        //获取分类数据
        const getCategroyList=()=>{
            listCategory().then(res=>{
                if(res.code==200){
                    treeList.value=res.data;
                }else{
                    ElMessage.error(res.msg);
                }
            })
        }
        //删除分类
        const remove=(index,row)=>{
            if(row.children.length>0){
                ElMessage.error('不能删除带有子分类的数据');
                return;
            }
            delCategory(row.id).then(res=>{
                if(res.code!=200){
                    ElMessage.error(res.msg);
                }
                getCategroyList();
            })
        }
        //修改分类
        const handleEdit=(index,row)=>{
            edit.value=true;
            getOneCategory(row.id).then(res=>{
                if(res.code==200){
                    categoryItem.value=res.data;
                }else{
                    ElMessage.error(res.msg);
                }
            })
        }
        onMounted(()=>{
            getCategroyList();
            getSelectList().then(res=>{
                if(res.code!=200){
                    ElMessage.error(res.msg);
                }else{
                    selectList.value=res.data;
                    selectList.value.push({id:0,name:'无父节点'})
                }
            })
        })
        //提交添加
        const save=()=>{
           if(categoryItem.value.name==undefined||
           categoryItem.value.name==''){
             ElMessage.error('分类名称不能为空');
             return;
           }
           if(categoryItem.value.pid==undefined||
           categoryItem.value.pid==null){
                ElMessage.error('分类的父节点不能为空');
                return;
           }    
           //修改状态
            if(edit.value){
                editCategory(categoryItem.value).then(res=>{
                    if(res.code!=200){
                        ElMessage.error(res.msg);
                    }
                    getCategroyList();
                })
            }else{
                //添加状态
                addCategory(categoryItem.value).then(res=>{
                    if(res.code!=200){
                        ElMessage.error(res.msg);
                    }
                    getCategroyList();
                })
            }
            
        }
        //返回添加
        const backAdd=()=>{
            edit.value=false;
            categoryItem.value={
            id:undefined,
            name:'',
            pid:undefined,
            description:'',
            pid:undefined,

        }

        }
        return {
            treeList,remove,handleEdit,categoryItem,
            edit, backAdd ,selectList,save,
        }
    },
})
</script>


<style>

</style>