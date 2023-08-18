import { menuList } from '@/api/back/menu'
import router from '@/router';

//动态添加路由
export function addStatic(data){
  if(data instanceof Array){
    let routes=[];
    //路由采用显示的
    data.forEach(item=>{
      const route ={
        name:item.name,
        path:`/${item.path}`,
        component:'background/BackHome',
        children:[]
      };
      getChilren(item,route,item.children);
      routes.push(route);
    })
    return routes;
  }
}
//给父路由找子路由
function getChilren(parent,parRoute,data){
  if(parent!=null&&parent!=undefined&&
    parRoute!=null&&parRoute!=undefined
    &&data!=null&&data instanceof Array&&data.length>0){
   //找出所有子菜单  
   data.filter(i=>i.type=='C').forEach(i=>{
    if(i&&parent){
      if(i.parentId==parent.id){ 
        const route ={
          name:i.name,
          path:i.path,
          component:i.component,
          children:[]
        };
        getChilren(i,route,i.children);
        parRoute.children.push(route);
      }
    }
     })
  }
}
//递归添加组件
export function recursion(arr){
  if(arr&&arr instanceof Array&&arr.length>0){
    arr.forEach(i=>{
      recursion(i.children);
      i.component=loadView(i.component);
    })
  }
}
//动态加载组件
export const loadView=(view)=>{
  return ()=>import('/src/views/'+view+'.vue')
 }

async function menuList1(){
  const json=localStorage.getItem('routeList');
  if(json){
    const list=JSON.parse(json);
    if(list instanceof Array){
      list.forEach(i=>{
        recursion(i.children);
        i.component=loadView(i.component);
        router.addRoute(i);
      });
    }
  }else{
    const res=await menuList();
    let routeList=[]
    if(res.data){
      routeList=addStatic(res.data);
      routeList.forEach(item=>router.addRoute(item));
    }
    localStorage.setItem('routeList',JSON.stringify(routeList))
  }
}
//解决白屏
const flag=localStorage.getItem('token');
    if(flag!=null){
      await menuList1();
    }
router.beforeEach(async(to,from,next)=>{
  if(to.path=='/login'){
    next();
  }else if(to.path=='/register'){
    next();
  }else if(to.path=='/home'){
    next();
  }else{
    const flag=localStorage.getItem('token');
    if(flag!=null){
      menuList1()
      next();
    }else{
      if(to.path.indexOf('/article')!=-1){
         next();
      }else if(to.path.indexOf('/userHome')!=-1){
        next();
      }else{
        next('/')
      }
      
    }
  }
})







