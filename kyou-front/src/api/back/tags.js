import requests from '../request'
const base='sys/tag'

export function listTags(){
    return requests({
        url:`${base}/list`,
        method:"get"
    })
}
export function insertTag(name){
    return requests({
        url:`${base}?name=${name}`,
        method:'post',
    })
}



