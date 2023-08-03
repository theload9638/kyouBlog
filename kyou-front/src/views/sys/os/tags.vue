<template>
    <BackMainHeader />
    <div class="common-layout">
    <el-container>
      <el-aside width="30%">
        <span class="addTagTitle">新增标签(每天限3个)</span>
        <el-input v-model="addTagData" class="in" placeholder="新增标签" clearable />
      <el-button type="primary" class="bu" @click="addTag">添加标签</el-button>
      </el-aside>
      <el-main>
        <span v-for="tag in tags" :key="tag.id" style="margin-left: 10px;">
            <el-tag class="tag">{{ tag.name }}</el-tag>
        </span>
        
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { defineComponent,ref,onMounted } from 'vue'
import BackMainHeader from '@/components/BackMainHeader.vue';
import {listTags,insertTag} from '@/api/back/tags.js'
import { ElMessage } from 'element-plus'
export default defineComponent({
    name:'tags',
    components:{
        BackMainHeader,
    },
    setup() {
        const tags=ref([])
        const addTagData=ref('')
        const addTag=()=>{
            if(addTagData.value&&addTagData.value!=''){
                insertTag(addTagData.value).then(res=>{
                    if(res.code!=200){
                        ElMessage.error(res.msg);
                    }
                    addTagData.value='';
                    getTagList();
                })
            }else{
                ElMessage.error('标签名称不能为空');
            }
        }
        const getTagList=()=>{
            listTags().then(res=>{
                if(res.code==200){
                    tags.value=res.data;
                }
            })
        }
        onMounted(()=>{
            getTagList()
        })

        return {
            addTagData,addTag,tags
        }
    },
})
</script>

<style scoped>
.common-layout{
    background-color: lightyellow;
}
.addTagTitle{
    font-size: xx-large;
    font-weight: 700;
    margin-left: 10px;
    padding-left: 10px;
    margin-top: 5px;
    padding-top: 5px;
    position:absolute;
}
.in{
    width: 100%;
    height: 70px;
    margin-top: 150px;
    padding-right: 0px;
}
.bu{
    width: 120px;
    margin-top: 20px;
    left: 10px;
    margin-left: 10px;
    float: left;
    height: 60px;
}
.tag{
    width: 90px;
    font-size:large;
    height: 50px;
}
</style>