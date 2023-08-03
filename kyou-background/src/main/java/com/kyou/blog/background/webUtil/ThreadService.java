package com.kyou.blog.background.webUtil;

import com.kyou.blog.background.web.FreemarkerUtil;
import com.kyou.blog.common.constant.RedisConstant;
import com.kyou.blog.common.constant.StatusConstant;
import com.kyou.blog.dataService.service.UserCommentService;
import com.kyou.blog.model.entity.UserComment;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CC
 * time 2023-07-28
 * description
 */
@Component
@Slf4j
public class ThreadService {
    @DubboReference(interfaceClass = UserCommentService.class)
    private UserCommentService userCommentService;
    @Autowired
    private RedisUtil redisUtil;


    @Async("executor")
    public void generateArticle(Long id,String title,String dir){
        String path=String.format("%s\\%s%s%s",dir,"article",id,".vue");
        if (new File(path).exists()) {
            log.info("无需生成文章--》{}",id);
            return;
        }
        Map map=new HashMap();
        map.put("articleId",id);
        map.put("title",title);
        map.put("name","article"+id);
        FreemarkerUtil.generate(path,map);
        log.info("生成文章---》{}",id);
    }
    @Async("executor")
    public void generateUserHome(Long id,String dir){
        UserComment userComment = new UserComment();
        userComment.setId(id);
        userComment.setExperience(0);
        userComment.setLevel(1);
        userComment.setHomeLink("/userHome"+id);
        userCommentService.save(userComment);
        String path=String.format("%s\\%s.vue",dir,"userHome"+id);
        if (new File(path).exists()) {
            log.info("无需生成主页--》{}",id);
            return;
        }
        HashMap<Object, Object> h = new HashMap<>();
        h.put("userId",id);
        FreemarkerUtil.generateUserHome(path,h);
    }
    @Async("executor")
    public void sendSignRewards(Long userId, int monthValue, int curDay,int days)
    {
        //最大连续和当前连续次数
        int max=0;
        int curNum=0;
        String k= RedisConstant.USER_SIGN+monthValue+":"+userId;
        ValueOperations<String, String> val = redisUtil.val();
        for (int i = 0; i < curDay; i++) {
            Boolean bit = val.getBit(k, i);
            //已签到
            if(bit){
                curNum++;
            }else {
                max=Math.max(max,curNum);
                curNum=0;
            }
        }
        String key=RedisConstant.USER_SIGN_REWARD+monthValue+":"+userId;
        String s = redisUtil.val().get(key);
        //连续签到1个月
        if(max>=days){
            if (StringUtils.hasText(s)) {
                //说明已经领取了更高的奖励
                if(s.compareTo("5")>=0) {
                    return;
                }
            }else{
                UserComment uc = userCommentService.getById(userId);
                if (uc.getLevel()<6) {
                    uc.setExperience(uc.getExperience()+300);
                    checkExLevel(uc);
                    userCommentService.updateById(uc);
                }
                redisUtil.val().set(key,"5",Duration.ofDays(1));
            }
            //连续签到4个星期
        } else if (max>=28&&days!=28) {
            if (StringUtils.hasText(s)) {
                //说明已经领取了更高的奖励
                if(s.compareTo("4")>=0) {
                    return;
                }
            }else{
                UserComment uc = userCommentService.getById(userId);
                if (uc.getLevel()<6) {
                    uc.setExperience(uc.getExperience()+100);
                    checkExLevel(uc);
                    userCommentService.updateById(uc);
                }
                redisUtil.val().set(key,"4",Duration.ofDays(days));
            }
            //连续3个星期
        } else if (max>=21) {
            if (StringUtils.hasText(s)) {
                //说明已经领取了更高的奖励
                if(s.compareTo("3")>=0) {
                   return;
                }
            }else{
                UserComment uc = userCommentService.getById(userId);
                if (uc.getLevel()<6) {
                    uc.setExperience(uc.getExperience()+100);
                    checkExLevel(uc);
                    userCommentService.updateById(uc);
                }
                redisUtil.val().set(key,"3",Duration.ofDays(days));
            }
            //连续2个星期
        } else if (max >= 14) {
            if (StringUtils.hasText(s)) {
                //说明已经领取了更高的奖励
                if(s.compareTo("2")>=0) {
                    return;
                }
            }else{
                UserComment uc = userCommentService.getById(userId);
                if (uc.getLevel()<6) {
                    uc.setExperience(uc.getExperience()+100);
                    checkExLevel(uc);
                    userCommentService.updateById(uc);
                }
                redisUtil.val().set(key,"2",Duration.ofDays(days));
            }
        } else if (max>=7) {
            if (StringUtils.hasText(s)) {
                //说明已经领取了更高的奖励
                if(s.compareTo("1")>=0) {
                    return;
                }
            }else{
                UserComment uc = userCommentService.getById(userId);
                if (uc.getLevel()<6) {
                    uc.setExperience(uc.getExperience()+100);
                    checkExLevel(uc);
                    userCommentService.updateById(uc);
                }
                redisUtil.val().set(key,"1",Duration.ofDays(days));
            }
        }

    }
    private void checkExLevel(UserComment uc){
        if(uc.getExperience().compareTo(StatusConstant.EX_MAX)>=0){
            uc.setLevel(6);
            uc.setExperience(StatusConstant.EX_MAX);
        } else if(uc.getExperience().compareTo(StatusConstant.EX_5)>=0){
            uc.setLevel(5);
        } else if (uc.getExperience().compareTo(StatusConstant.EX_4)>=0) {
            uc.setLevel(4);
        } else if (uc.getExperience().compareTo(StatusConstant.EX_3)>=0) {
            uc.setLevel(3);
        } else if (uc.getExperience().compareTo(StatusConstant.EX_2) >= 0) {
            uc.setLevel(2);
        }
    }
}
