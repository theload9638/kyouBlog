import { createRouter, createWebHistory ,createWebHashHistory} from 'vue-router'
//公共路由
export const constantRoutes=[
  {
    path:'/sign',
    component:()=>import('../components/SignIn.vue')
  },
  {
    path:'/',
    redirect: '/home',
  },
  {
    path: '/home',
    name: 'front',
    component: ()=>import('../views/HomeView.vue')
  },
  {
    path:'/back',
    name:'back',
    component:()=>import('../views/background/BackHome.vue')
  },
  {
    path:'/login',
    name:'login',
    component:()=>import('../views/Login.vue')
  },
  {
    path:'/register',
    name:'register',
    component:()=>import('../views/Register.vue')
  },
  {
    path:'/article',
    name:"article",
    component:()=>import("../components/articles/Article.vue")
  },
  {
    path:'/userHome',
    name:"userHome",
    component:()=>import("../components/userHome/userHome.vue")
  }
];
//创建路由
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  // history:createWebHashHistory(import.meta.env.BASE_URL),
  routes:constantRoutes
})
// 保存原始的push方法
let originPush = router.push;
let originReplace=router.replace

// 重写push方法
router.push = function (location, onComplete, onAbort) {
  if (onComplete && onAbort) {
    // 原始push方法的调用保持this指向为router
    originPush.call(this, location, onComplete, onAbort);
  } else {
    // 如果没有传入回调函数，将其替换为空的回调函数
    originPush.call(this, location, () => {}, () => {});
  }
}
router.replace=function(location, onComplete, onAbort) {
  if (onComplete && onAbort) {
    // 原始push方法的调用保持this指向为router
    originReplace.call(this, location, onComplete, onAbort);
  } else {
    // 如果没有传入回调函数，将其替换为空的回调函数
    originReplace.call(this, location, () => {}, () => {});
  }
}

export default router
