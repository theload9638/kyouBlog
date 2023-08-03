import requests from "../request";
import TurndownService from 'turndown';
import showdown from 'showdown';
const base='sys/article';

export function contentImg(data){
    return requests({
        url:`${base}/contentImg`,
        method:'post',
        data
    })
}
//新增文章
export function addArticle(data){
    return requests({
        url:`${base}/addArticle`,
        method:'post',
        data
    })
}
export function updArticle(data){
    return requests({
        url:`${base}/updArticle`,
        method:'put',
        data
    })
}
//分页查询
export function pageArticle(data){
    return requests({
        url:`${base}/page`,
        method:'get',
        params:data
    })
}
//审核文章
export function validArticle(id){
    return requests({
        url:`${base}/valid/${id}`,
        method:'get',
    })
}
//时间线文章
export function getTimeLineArticles(){
    return requests({
        url:`${base}/timeLine`,
        method:"get"
    })
}
//下架文章
export function takeDownArticle(id){
    return requests({
        url:`${base}/takeDown/${id}`,
        method:'get',
    })
}
//删除文章
export function delArticles(ids){
    return requests({
        url:`${base}?ids=${ids}`,
        method:'delete',
    })
}
//获取文章标签和分类
export function tagAndCategory(id){
    return requests({
        url:`${base}/tagAndCategory/${id}`,
        method:'get'
    })
}
//搜索文章
export function searchArticle(data){
    return requests({
        url:`${base}/searchArticle`,
        method:"get",
        params:data
    })
}
//获取热门
export function getHot(){
    return requests({
        url:`${base}/top10Article`,
        method:"get"
    })
}
//文章点赞
export function likeArticle(aid,uid,type){
    return requests({
        url:`${base}/likeArticle/${aid}?uid=${uid}&type=${type}`,
        method:"put"
    })
}
//获取文章的id集合
export function getPublishedIds(){
    return requests({
        url:`${base}/getPublishedIds`,
        method:'get'
    })
}
//获取作者的文章列表
export function getAuthorArticles(userId,num,size){
    return requests({
        url:`${base}/authorArticles/${userId}/${num}/${size}`,
        method:'get'
    })
}


//获取文章信息
export function getArticleInfo(id,uid,mode){
    return requests({
        url:`${base}/getInfo/${id}?uid=${uid}&mode=${mode}`,
        method:'get',
    })
}
export function textToHtml(text) {
    // 使用turndown库将文本转换为HTML代码
    const turndownService = new TurndownService();
    return turndownService.turndown(text);
}
export function htmlToText(html) {
    // 使用showdown库将HTML代码转换为文本
    const converter = new showdown.Converter();
    return converter.makeMarkdown(html);
  }


