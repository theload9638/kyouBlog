import requests from "../request";

const base='sys/index';

export function backInfo(){
    return requests({
        url:`${base}/back`,
        method:"get"
    })
}
//获取首页数据
export function frontInfo(data){
    return requests({
        url:`${base}/front`,
        method:'get',
        params:data
    })
}


