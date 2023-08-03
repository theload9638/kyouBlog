import { v4 as uuidv4 } from 'uuid'

export function findMenuName(path){
  if(typeof path =='string'){
   if(path.includes('os')){
      return '系统模块'
     }else if(path.includes('user')){
      return '用户模块'
     }else if(path.includes('menuBoard')){
      return '菜单面板'
     }else if(path.includes('setting')){
      return '设置'
     }
  }
  return null;
}

export function getYoukeId(){
  const json=localStorage.getItem('tourist');
      if(json){
        return JSON.parse(json);
      }else{
        const uuid = uuidv4();
        localStorage.setItem('tourist',JSON.stringify(uuid));
        return uuid;
    }
}
