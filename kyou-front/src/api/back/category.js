import requests from "../request";

const base='sys/category';

export function listCategory(){
    return requests({
        url:`${base}/list`,
        method:'get'
    })
}
//删除分类
export function delCategory(id){
    return requests({
        url:`${base}/${id}`,
        method:'delete'
    })
}
//获取下拉列表
export function getSelectList(){
    return requests({
        url:`${base}/selectList`,
        method:"get"
    })
}
//获取单个
export function getOneCategory(id){
    return requests({
        url:`${base}/${id}`,
        method:'get'
    })
}
//新增
export function addCategory(data){
    return requests({
        url:base,
        method:'put',
        data
    })
}
//修改
export function editCategory(data){
    return requests({
        url:base,
        method:'post',
        data
    })
}


