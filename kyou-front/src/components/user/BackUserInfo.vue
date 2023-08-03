<template>
<el-form :model="tableData" label-width="120px" class="container">
    <el-form-item label="账号">
        <el-input class="in" v-if="type==''" v-model="tableData.username" disabled/>
        <el-input class="in" v-else v-model="tableData.username"  placeholder="数字字母下划线4-16位组成"/>
    </el-form-item>
    <el-form-item v-if="type=='insert'" label="密码">
        <el-input class="in" v-model="pwd" />
    </el-form-item>
    <el-form-item label="邮箱">
        <el-input class="in" v-model="tableData.email" />
    </el-form-item>
    <el-form-item label="当前角色">
        <div v-for="role in tableData.roles" :key="role">
          <el-tag>{{ role }}</el-tag> 
        </div>
        <el-button 
        type="primary" 
        size="large" 
        v-if="adminStatus"
        @click="assign"
        class="assignRole">分配角色</el-button>
      </el-form-item>
    <el-form-item label="性别" >
      <el-radio-group v-model="tableData.gender">
    <el-radio :label="'1'">男</el-radio>
    <el-radio :label="'2'">未知</el-radio>
    <el-radio :label="'0'">女</el-radio>
  </el-radio-group>
    </el-form-item>
    <el-form-item  label="昵称">
        <el-input class="in" v-model="tableData.nickname" placeholder="4到10位任意字符"/>
    </el-form-item>
    <el-form-item label="头像" v-if="type==''" class="hearImg">
      <el-image class="img" :src="tableData.headImage"/>
  <el-upload
    ref="uploadRef"
    class="upload-demo"
    :action="action"
    :limit="1"
    :headers="headers"
    :on-success="handlerSuccess"
    :on-exceed="handleExceed"
    :auto-upload="false"
  >
    <template #trigger>
      <el-button type="primary">选择文件</el-button>
    </template>
    <el-button class="ml-3" type="success" @click="submitUpload">
      确定上传
    </el-button>
    <template #tip>
      <div class="el-upload__tip">
        只支持jpg/png格式的文件，大小小于1M
      </div>
    </template>
  </el-upload>
    </el-form-item>
    <el-form-item label="个人说明">
      <el-input class="in" :rows="6" v-model="tableData.remark" type="textarea" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">保存</el-button>
    </el-form-item>
  </el-form>
  <el-dialog
    v-model="centerDialogVisible"
    title="分配角色"
    width="30%"
    draggable
    align-center>
    <div>
      <el-checkbox-group v-model="checkList"
       v-for="role in roleList" :key="role.id">
    <el-checkbox :label="role.name"/>
  </el-checkbox-group>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancelAssign">取消</el-button>
        <el-button type="primary" @click="assignRole">
          确认分配
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { 
  defineComponent,ref,onMounted,onBeforeMount,getCurrentInstance
} from 'vue'
import router from '@/router'
import { ElMessage } from 'element-plus'
import {saveUser,getUserInfo,addUserWithRole} from '@/api/back/user.js'
import emitter from '@/utils/event-bus.js'
import {getRoles} from '@/api/back/role.js';
import {isAdmin} from '@/api/back/user.js'

export default defineComponent({
    name:'BackUserInfo',
    setup() {
      const { proxy } = getCurrentInstance();
      const adminStatus=ref(false);
      //显示对话框按钮
      const centerDialogVisible=ref(false);
      //分配角色的显示弹窗
      const assign =()=>{
        //初始化已分配的角色
        for(let role of tableData.value.roles){
           checkList.value.push(role);
        }
        centerDialogVisible.value=true;
      }
      //提交分配的角色
      const assignRole=()=>{
        centerDialogVisible.value= false
        tableData.value.roles=JSON.parse(JSON.stringify(checkList.value));
        checkList.value=[];
      }
      //所有角色集合
      const roleList=ref([]);
      //已分配的角色集合
      const checkList = ref([])
      //取消分配
      const cancelAssign=()=>{
        centerDialogVisible.value = false
        checkList.value=[];
      }
      //提交文件时，携带的请求头
      const headers = ref({
        Authorization:localStorage.getItem('token')
      })
      const pwd=ref('123456');
      //提交类型
      const type=ref('')
      //保存数据
      const saveData=(val)=>{
        saveUser(val).then(res=>{
            if(res.code==200){
            const val=router.currentRoute.value;
            if(Object.keys(val.query).length!=0){
                window.location.href='/user/useList';
            }else{
              ElMessage.success('保存成功');
              getUserInfo().then(res=>{
                if(res.code==200){
                  handlerInfo(res.data);
                }
              })
            }
            }else{
              ElMessage.success(res.msg);
            }
          })
      }
      const uploadRef = ref(null);
        const tableData=ref({
            id:undefined,
            username:'',
            email:'',
            gender:'2',
            nickname:'',
            headImage:"",
            remark:'',
            roles:[]
        })
        const action=ref(`${proxy.$key}/sys/user/uploadImg`)
        //提交用户信息
        const onSubmit=()=>{
          
          const val=JSON.parse(JSON.stringify(tableData.value));
          if(type.value=='insert'){
            if(tableData.value.username==null||
            tableData.value.username==undefined){
              ElMessage.error('用户名不能为空')
               return 
            }
            if(!/^\w{4,16}$/.test(tableData.value.username)){
              ElMessage.error('用户名有数字字母下划线组成，长度4-16位');
             return;
        }
        if (pwd.value==null||pwd.value==undefined||!/^\w{6,16}$/.test(pwd.value)) {
        ElMessage.error("请输入6-16位包含数字字母的复杂的密码");
        return;
      } 
         //是邮箱
      if(tableData.value.email==undefined||tableData.value.email==null||
      !/^\w{5,16}@.{2,8}\.[a-zA-Z]{1,4}$/.test(tableData.value.email)){
        ElMessage.error('邮箱格式错误')
          return;
        }
        //昵称
        if(tableData.value.nickname==undefined||tableData.value.nickname==null||
        tableData.value.nickname.length<4||tableData.value.nickname>10){
          ElMessage.error('昵称不能为空，长度在4到10位');
          return;
        }
            tableData.value.password=pwd.value;
            addUserWithRole(tableData.value).then(res=>{
              // window.location.href='/user/useList'
              router.push({path:'/user/useList'})
            })
            return;
          }{
            saveData(val);
          }
         
        }
        //文件上传成功后的回调
const handlerSuccess=(res,uploadFile,uploadFiles)=>{
  if(res.code==200){
    tableData.value.headImage=res.data;
  }
}
 const handleExceed = (files) => {
  uploadRef.value.clearFiles()
  const file = files[0]
  file.uid = genFileId()
  uploadRef.value.handleStart(file)
}
onBeforeMount(async ()=>{
  isAdmin().then(res=>{
    if(res.code==200){
      adminStatus.value=res.data;
    }
  })
  //绑定修改用户信息的事件
  const val=router.currentRoute.value;
            if(Object.keys(val.query).length!=0){
              const id=val.query.id;
              type.value=val.query.type;
              if(id){
                const res=await getUserInfo(id);
                handlerInfo(res.data);
              }else if(type.value=='insert'){
                  
              }
            }else{
              emitter.on('data',(data)=>{
              handlerInfo(data);
            });
            }
})
        onMounted(()=>{ 
            getRoles().then(res=>{
          roleList.value=res.data;
        })
        })
        //处理个人信息数据
    const handlerInfo=(data)=>{
      tableData.value.id=data.id;
      tableData.value.username=data.username;
                tableData.value.email=data.email;
                tableData.value.gender=data.gender;
                tableData.value.nickname=data.nickname;
                tableData.value.headImage=data.headImage;
                tableData.value.remark=data.remark;
                tableData.value.roles=[];
                for (let role of data.roles){
                  tableData.value.roles.push(role.name);
                }
    }    
    //提交上传的文件
      const submitUpload=()=>{
        uploadRef.value.submit()
      }
      //收集的文件集合
        const fileList=ref([])
        
        return {
            tableData,onSubmit,fileList,
            action,uploadRef,submitUpload,
            handleExceed,headers,handlerSuccess,
            centerDialogVisible,assign,cancelAssign,
            checkList,roleList,assignRole,type,pwd,
            adminStatus,

        }
    },
})
</script>


<style scoped>
.dialog-footer button {
  margin-right: 10px;
  font-size: large;
  font-weight: 500;
  width: 80px;
  height: 60px;
}
.in{
  width: 600px;
}
.container{
    background-color: lightyellow;
}
.img{
    width: 260px;
    height: 260px;
}
.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: var(--el-fill-color-light);
  color: var(--el-text-color-secondary);
  font-size: 30px;
}
.el-upload__tip{
    font-size: medium;
    font-weight: 500;
}
.upload-demo{
    right:200px;
    width: 400px;
    height: 400px;
    margin-top:160px;
    position:absolute;
}
.assignRole{
  width: 100px;
  height: 50px;
  margin-left: 50px;
}
.assignRole:hover{
  
  color: skyblue;
}
</style>