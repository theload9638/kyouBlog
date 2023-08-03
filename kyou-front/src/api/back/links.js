import requests from "../request";

const base='sys/links';

export function listLinks(data){
    return requests({
        url:`${base}/list`,
        method:'get',
        params:data
    })
}
