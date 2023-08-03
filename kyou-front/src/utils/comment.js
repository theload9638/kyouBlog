import { UToast, createObjectURL } from 'undraw-ui'
import moment from 'moment';
import {addComment} from '@/api/back/comment.js'

export function commit(config,articleId,content, parentId, finish,reply){
    let pname=null;
    if(reply){pname=reply.user.username;}
   const comment = {
    id: undefined,
    parentId: parentId,
    pname:pname,
    uid: config.user.id,
    address: navigator.userAgentData.brands[2].brand ||'',
    content: content,
    likes: 0,
    createTime: moment().format('YYYY-MM-DD HH:mm:ss'),
    articleId:articleId,
    user: {
      username: config.user.username,
      avatar: config.user.avatar,
      level: config.user.level,
      homeLink: `/${config.user.id}`
    },
  }
  addComment(comment).then(res=>{
     if(res.code==200){
        setTimeout(() => {
            finish(comment)
            UToast({ message: '评论成功!', type: 'info' })
             }, 200)
     }else{
        ElMessage.error("评论失败："+res.msg);
     }
  })

}





