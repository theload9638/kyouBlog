import requests from "../request";
const base='sys/log'

export function delLog(ids){
    if(ids==undefined){
        return ;
    }
    return requests({
        url:`${base}/del?ids=${ids}`,
        method:"delete"
    })
}
export function clearLogs(){
    return requests({
        url:`${base}`,
        method:'delete'
    })
}
export function listLogs(data){
    return requests({
        url:`${base}/list`,
        method:'get',
        params:data
    })
}
