import requests from "../request";

const base='sys/comments'

export function addComment(data){
    return requests({
        url:base,
        method:'put',
        data
    })
}

//获取评论列表
export function listComments(num,size){
    return requests({
        url:`${base}/list/${num}/${size}`,
        method:"get"
    })
}
//展示评论
export function showComments(aid){
    return requests({
        url:`${base}/${aid}`,
        method:"get"
    })
}
//点赞
export function likeComment(cid,type){
    return requests({
        url:`${base}/likeComment?cid=${cid}&type=${type}`,
        method:'put'
    })
}
//获取当前登录的评论人信息
export function getUserCommentInfo(){
    return requests({
        url:`${base}/userCommentInfo`,
        method:"get"
    })
}

