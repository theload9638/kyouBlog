import requests from "../request";
const base='sys/role';
export function getRoles(data){
    if(data==undefined||data==null){
        data='';
    }
    return requests({
        url:`${base}/list?condition=${data}`,
        method:'get'
    })
}
export function delRoles(id){
    return requests({
        url:`${base}/del/${id}`,
        method:'delete'
    })
}
export function isAudit(){
    return requests({
        url:`${base}/isAudit`,
        method:'get'
    })
}
export function updRole(data){
    return requests({
        url:`${base}`,
        method:'put',
        data
    })
}
export function addRole(data){
    return requests({
        url:`${base}/add`,
        method:'post',
        data
    })
}