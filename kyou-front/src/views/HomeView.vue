<template>
 <el-container style="width: 1500px;height:100%">
    <Header/>
    <Middle/>
    <Footer/>
  </el-container>
</template>
           
 
<script>
import Header from "@/components/Header.vue";
import { defineComponent  } from "vue";
import Footer from "@/components/Footer.vue";
import Middle from '@/components/Middle.vue'
export default defineComponent({
  name: 'HomeView',
  components: {Header,Footer,Middle},
  setup() {
   
  }
});
</script>
<style scoped>
::-webkit-scrollbar {
    width: 10px;
    }
</style>