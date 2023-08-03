import requests from "../request";

const base='sys/user'
//是否是admin
export function isAdmin(){
    return requests({
        url:`${base}/isAdmin`,
        method:'get'
    })
}
//进行签到
export function userSignIn(){
return requests({
    url:`${base}/signIn`,
    method:'put'
})
}
//检查是否签到
export function checkSignIn(){
    return requests({
        url:`${base}/checkSignIn`,
        method:'get'
    })
}
//获取签到信息
export function getsignInNum(month){
    return requests({
        url:`${base}/signInNum/${month}`,
        method:"get"
    })
}
//获取粉丝列表
export function getFansList(){
    return requests({
        url:`${base}/fansList`,
        method:"get"
    })
}
//获取关注列表
export function getFollowList(){
    return requests({
        url:`${base}/followList`,
        method:"get"
    })
}
//关注
export function follow(data,flag){
    return requests({
        url:`${base}/follow/${flag}`,
        method:'put',
        data
    })
}
//获取已注册的用户id
export function getRegisterUserId(){
    return requests({
        url:`${base}/registeredID`,
        method:'get'
    })
}
//获取作者信息通过用户id
export function getAuthorInfo(id){
    return requests({
        url:`${base}/getAuthorInfo/${id}`,
        method:'get'
    })
}
//获取作者信息通过文章id
export function getAuthorInfoByAid(id){
    return requests({
        url:`${base}/authorInfo/${id}`,
        method:'get'
    })
}

//获取验证码
export function getLoginCode(key){
    return requests({
        url:`${base}/loginCode?k=${key}`,
        method:'get',
    })
}
//登录
export function login(data){
    return requests({
        url:`${base}/login`,
        method:'post',
        data
    })
}
//注册用户
export function register(data){
    return requests({
        url:`${base}/register`,
        method:'post',
        data
    })
}
//获取用户详细信息
export function getUserInfo(id){
    return requests({
        url:`${base}/getInfo`,
        method:'get',
        params:{userId:id}
    })
}
//退出登录
export function exit(){
    return requests({
        url:`${base}/exit`,
        method:'get'
    })
}
//保存用户信息
export function saveUser(data){
    return requests({
        url:`${base}`,
        method:'put',
        data
    })
}
//修改密码
export function updPwd(data){
    return requests({
        url:`${base}/updatePwd`,
        method:'post',
        data
    })
}
//获取用户列表
export function listUsers(data){
    return requests({
        url:`${base}/list`,
        method:"get",
        params:data
    })
}
//修改账户状态
export function editUserStatus(id,status,delFlag){
    return requests({
        url:`${base}/editStatus/${id}/${status}/${delFlag}`,
        method:"put"
    })
}
//永久删除用户
export function dels(ids){
    return requests({
        url:`${base}/dels?ids=${ids}`,
        method:'delete'
    })
}
//新增用户
export function addUserWithRole(data){
    return requests({
        url:`${base}/addUserWithRole`,
        method:'post',
        data
    })
}


