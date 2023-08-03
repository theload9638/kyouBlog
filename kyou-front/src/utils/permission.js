import { getMenus,menuList } from '@/api/back/menu'
import router from '@/router';
import {getPublishedIds} from '@/api/back/article'
import {getRegisterUserId } from '@/api/back/user'

//动态添加路由
export function addDynamicRoutes(data){
  if(data instanceof Array){
    let routes=[];
    data.forEach(item=>{
      const route ={
        name:item.name,
        path:`/${item.path}`,
        component:()=>import('@/views/background/BackHome.vue'),
        children:[]
      };
      findChilren(item,route,item.children);
      routes.push(route);
    })
    return routes;
  }
}
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
//给父路由找子路由
function findChilren(item,par,data){
   if(item!=null&&item!=undefined&&par!=null&&par!=undefined&&data!=null&&data instanceof Array&&data.length>0){
    //找出所有子菜单  
    data.filter(i=>i.type==='C').forEach(i=>{
          if(i&&item&&i.parentId==item.id){
            const route ={
              name:i.name,
              path:i.path,
              component:()=>import(`@/views/${i.component}.vue`),
              children:[]
            };
            findChilren(i,route,i.children);
            par.children.push(route);
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
  return ()=>import('../views/'+view+'.vue')
 }
 //获取文章id
 const getAIds=async()=>{
  let ids=localStorage.getItem('aids');
  if(ids!=undefined){
    const res=await getPublishedIds();
    if(res.code==200){
      ids=res.data;
      }
      localStorage.setItem('aids',JSON.stringify(ids));
    }
    if(ids&&ids instanceof Array){
      for (let i of ids){
        const route={
          name:`article${i}`,
          path:`/article${i}`,
          component:loadArticle(i)
        }
        router.addRoute(route);
      }
  }

}
//动态加载文章
export const loadArticle=(id)=>{
  return ()=>import('../components/articles/article'+id+'.vue');
}
//获取用户id并加载路由
const getUserIds=async ()=>{
  const res=await getRegisterUserId();
  if(res.code==200){
    const uids=res.data;
    if(uids&&uids instanceof Array){
      for (let i of uids){
      const route={
        name:`userHome${i}`,
        path:`/userHome${i}`,
        component:loadUserHome(i)
      }
      router.addRoute(route);
    }
    }
  }
}
//动态加载用户主页
const loadUserHome=(id)=>{
  return ()=>import('../components/userHome/userHome'+id+'.vue')
}


export function handler(func){
let cache=[];
let i=0;
const origin=window.fetch;
window.fetch=(...args)=>{
  if(cache[i]){
    if(cache[i].status==='fulfilled'){
      return cache[i].data;
    }else if(cache[i].status==='rejected'){
      throw cache[i].err;
    }
  }
  const result={
    status:'pending',
    data:null,
    err:null
  };
  cache[i++]=result; 
  //发送请求
  const prom=origin(...args).then(res=>res.json()).then(
    (res)=>{
      result.status='fulfilled';
      result.data=res;
    },
    err=>{
      result.status='rejected';
      result.err=err;
    }
    );
   //报错
   throw prom  
}
try{
  func()
}catch(err){
  if(err instanceof Promise){
    const re=()=>{
      i=0;
      func();
    }
    err.then(re,re);
  }
}
}
//解决白屏
const flag=localStorage.getItem('token');
    if(flag!=null){
      await menuList1();
    }
    await getAIds();
    await getUserIds();
router.beforeEach(async(to,from,next)=>{
  if(to.path=='/login'){
    next();
  }else if(to.path=='/register'){
    next();
  }else if(to.path=='/home'){
    getAIds();
    getUserIds();
    next();
  }else{
    const flag=localStorage.getItem('token');
    if(flag!=null){
      menuList1();
      getAIds();
      getUserIds();
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






