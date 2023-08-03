import requests from "../request";
const base='sys/menu'

export function getMenus(){
    return fetch(`http://localhost:8002/${base}/getMenus`)
}
export function menuList(){
    return requests({
        url:`${base}/getMenus`,
        method:'get'
    })
}
export function delMenu(id){
    return requests({
        url:`${base}/${id}`,
        method:'delete'
    })
}
export function addMenu(data){
    return requests({
        url:`${base}`,
        method:'put',
        data
    })
}
export function getMenuInfo(id){
    return requests({
        url:`${base}/${id}`,
        method:'get',
    })
}
export function updMenu(data){
    return requests({
        url:`${base}`,
        method:'post',
        data
    })
}
export function menuInfo(data){
    return requests({
        url:`${base}/info`,
        method:'get',
        params:data
    })
}
//获取权限
export function getPerms(rid){
    return requests({
        url:`${base}/getPerms`,
        method:'get',
        params:{id:rid}
    })
}
//分配权限
export function assignMenuPerms(rid,mids){
    return requests({
        url:`${base}/assignPerms/${rid}`,
        method:'post',
        data:mids
    })
}




