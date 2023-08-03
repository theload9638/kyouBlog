<template>
    <BackMainHeader />
 <el-scrollbar class="container">
      <p v-for="(url,index) in urls" :key="index">
        <el-link @click="go(url)" class="link" type="primary">{{ url.name }}</el-link>
    </p>  
</el-scrollbar>
<el-pagination
      class="ll"
      v-model:current-page="data.pageNum"
      :page-size="data.pageSize"
      :background="true"
      layout="total, prev, pager, next"
      :total="data.total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
</template>


<script>
import { defineComponent ,ref,onMounted} from 'vue'
import BackMainHeader from '@/components/BackMainHeader.vue';
import {listLinks} from '@/api/back/links'
export default defineComponent({
    components:{
        BackMainHeader,
    },
    name:'links',
    setup() {
        const go=(url)=>{
            window.location.href=url.path;
        }
        const data=ref({
            pageNum:1,
            pageSize:5,
            total:0,
        })
        const urls=ref([]);
        onMounted(()=>{
            getUrls();
        })
        const getUrls=()=>{
            listLinks(data.value).then(res=>{

                urls.value=res.data.records;
                data.value.total=res.data.total;
            })
        }
        const handleSizeChange=(size)=>{
            data.value.pageSize=size;
            getUrls();
        }
        const handleCurrentChange=(num)=>{
            data.value.pageNum=num;
            getUrls();
        }
        return {    
            urls,go,handleSizeChange,handleCurrentChange,
            data,
        }
    },
})
</script>


<style scoped>
.link{
    width: 100%;
    height: 100px;
    margin-top:0px;
    padding-top: 0px;
    padding-bottom: 0px;
    margin-bottom: 0px;
    top: 0px;
    font-size: xx-large;
}
.container{
    top:0px;
    height: 60%;
    padding: 0px;
    background-color: lightgoldenrodyellow;
}
.ll{
    width: 300px;
    height: 100px;
    font-size: xx-large;
    position: fixed;
    left: 1000px;
}
</style>