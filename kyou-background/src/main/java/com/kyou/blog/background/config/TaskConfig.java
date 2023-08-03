package com.kyou.blog.background.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kyou.blog.background.webUtil.LogQueue;
import com.kyou.blog.background.webUtil.RedisUtil;
import com.kyou.blog.background.webUtil.WebUtil;
import com.kyou.blog.common.constant.RedisConstant;
import com.kyou.blog.common.constant.StatusConstant;
import com.kyou.blog.dataService.service.ArticleService;
import com.kyou.blog.dataService.service.CommentsService;
import com.kyou.blog.dataService.service.SysLogService;
import com.kyou.blog.dataService.service.UserCommentService;
import com.kyou.blog.model.entity.Article;
import com.kyou.blog.model.entity.Comments;
import com.kyou.blog.model.entity.SysLog;
import com.kyou.blog.model.entity.UserComment;
import com.kyou.blog.model.vo.UserSignVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author CC
 * time 2023-07-17
 * description
 */
@EnableScheduling
@Configuration
@Slf4j
public class TaskConfig {

    @Autowired
    private RedisUtil redisUtil;
    @DubboReference(interfaceClass = UserCommentService.class)
    private UserCommentService userCommentService;


    @DubboReference(interfaceClass = ArticleService.class)
    private ArticleService articleService;
    @DubboReference(interfaceClass = CommentsService.class)
    private CommentsService commentsService;
    @Autowired
    private LogQueue logQueue;
    @DubboReference(interfaceClass = SysLogService.class)
    private SysLogService sysLogService;
    private volatile List<SysLog> logs= Collections.synchronizedList(new ArrayList<>());

    /**
     * 每30分钟清理一次最近30天内的无效图片
     */
//    @Scheduled(cron = "0 0/30 * * * ?")
    public void clearInvalidImg(){


    }
    /**
     * 每5分钟更新一次文章访问量 和 更新一次评论数
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void updArticleView(){
        String k=RedisConstant.ARTICLE_VIEW+"*";
        Set<String> keys = redisUtil.keys(k);
        if (!CollectionUtils.isEmpty(keys)) {
            log.info("{}-->更新文章访问量和评论数");
            keys.forEach(key->{
                //获取访问数量
                Long articleNum = redisUtil.getSetSize(key);
                articleNum=articleNum==null?0:articleNum;
                //获取文章id
                Long articleId=Long.valueOf(key.substring(key.lastIndexOf(":")+1));
                //获取评论数
                int count = commentsService.count(
                        new LambdaQueryWrapper<Comments>().eq(Comments::getArticleId, articleId)
                );
                Article article = new Article();
                article.setId(articleId);
                article.setViewCount(articleNum.intValue());
                article.setCommentCount(count);
                articleService.updateById(article);
            });
            redisUtil.del(RedisConstant.KYOU_FRONT);
            redisUtil.del(RedisConstant.KYOU_BACK);
        }

    }
    /**
     * 每30分钟检查一次是否评论过三，是否接近凌晨
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void incrCommentExp(){
        String time = WebUtil.formatTime(WebUtil.DATE_PATH_LINUX);
        String key=RedisConstant.COMMENT_EX +time+":*";
        Set<String> keys = redisUtil.keys(key);
        if (!CollectionUtils.isEmpty(keys)) {
            log.info("{}-->检查评论次数",LocalDateTime.now());
            keys.forEach(k->{
                //获取今天发布过的评论
                String val = redisUtil.getVal(k);
                if (StringUtils.hasText(val)) {
                    //发布了评论
                    int i = Integer.parseInt(val);
                    //谁发布的评论
                    Integer uid=Integer.parseInt(k.substring(k.lastIndexOf(":")+1));
                    if (i==3){
                        UserComment u = userCommentService.getById(uid);
                        //每发3次评论，进行新增经验
                        if (u.getExperience().compareTo(StatusConstant.EX_MAX)<0) {
                            u.setExperience(u.getExperience()+30);
                            if(u.getExperience().compareTo(StatusConstant.EX_MAX)>=0){
                                u.setLevel(6);
                                u.setExperience(StatusConstant.EX_MAX);
                            } else if(u.getExperience().compareTo(StatusConstant.EX_5)>=0){
                                u.setLevel(5);
                            } else if (u.getExperience().compareTo(StatusConstant.EX_4)>=0) {
                                u.setLevel(4);
                            } else if (u.getExperience().compareTo(StatusConstant.EX_3)>=0) {
                                u.setLevel(3);
                            } else if (u.getExperience().compareTo(StatusConstant.EX_2) >= 0) {
                                u.setLevel(2);
                            }
                            userCommentService.updateById(u);
                        }
                        //离凌晨差30分钟
                    }else if (Duration.between(LocalTime.now(),LocalTime.MAX).toMinutes()<=30){
                        UserComment u = userCommentService.getById(uid);
                        //每发3次评论，进行新增经验
                        if (u.getExperience().compareTo(StatusConstant.EX_MAX)<0) {
                            u.setExperience(u.getExperience()+10*i);
                            if(u.getExperience().compareTo(StatusConstant.EX_MAX)>=0){
                                u.setLevel(6);
                                u.setExperience(StatusConstant.EX_MAX);
                            } else if(u.getExperience().compareTo(StatusConstant.EX_5)>=0){
                                u.setLevel(5);
                            } else if (u.getExperience().compareTo(StatusConstant.EX_4)>=0) {
                                u.setLevel(4);
                            } else if (u.getExperience().compareTo(StatusConstant.EX_3)>=0) {
                                u.setLevel(3);
                            } else if (u.getExperience().compareTo(StatusConstant.EX_2) >= 0) {
                                u.setLevel(2);
                            }
                            userCommentService.updateById(u);
                        }
                    }
                }
            });

        }
    }
    @PostConstruct
    public void init(){
        log.info("task初始化");
        clearBeforeTagLimit();
        clearBeforeCommentExp();

    }
    /**
     * 清理近三天的标签限制次数
     */
    public void clearBeforeTagLimit(){
        LocalDate today = LocalDate.now();
        List<String> list=new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            LocalDate before = today.minusDays(i);
            list.add(before.format(DateTimeFormatter.ofPattern(WebUtil.DATE_PATH_LINUX)));
        }
        log.info("清理往期标签限制次数");
        list.forEach(time->{
            String key =RedisConstant.TAG_LIMIT+time+":*";
            Set<String> keys = redisUtil.keys(key);
            if (!CollectionUtils.isEmpty(keys)) {
                keys.forEach(redisUtil::del);
            }
        });
    }
    /**
     * 清理之前最近5天未处理的评论数，服务器启动时检查一次
     */
    public void clearBeforeCommentExp(){
        LocalDate today = LocalDate.now();
        List<String> list=new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            LocalDate before = today.minusDays(i);
            list.add(before.format(DateTimeFormatter.ofPattern(WebUtil.DATE_PATH_LINUX)));
        }
        log.info("增加用户评论经验--》{}",list);
        list.forEach(k->{
            //评论区经验的key
            String key=RedisConstant.COMMENT_EX +k+":*";
            //获取所有经验key
            Set<String> keys = redisUtil.keys(key);
            if (!CollectionUtils.isEmpty(keys)) {
                //进行遍历获取所有人的经验Key
                keys.forEach(e->{
                    //获取当前发布的key的评论次数
                    String val = redisUtil.getVal(e);
                    if (StringUtils.hasText(val)) {
                        //发布了评论的次数
                        int i = Integer.parseInt(val);
                        //谁发布的评论的用户id
                        Long uid = Long.valueOf(e.substring(e.lastIndexOf(":") + 1));
                        UserComment u = userCommentService.getById(uid);
                        if (u.getExperience().compareTo(StatusConstant.EX_MAX)<0) {
                            u.setExperience(u.getExperience()+i*10);
                            if(u.getExperience().compareTo(StatusConstant.EX_MAX)>=0){
                                u.setLevel(6);
                                u.setExperience(StatusConstant.EX_MAX);
                            } else if(u.getExperience().compareTo(StatusConstant.EX_5)>=0){
                                u.setLevel(5);
                            } else if (u.getExperience().compareTo(StatusConstant.EX_4)>=0) {
                                u.setLevel(4);
                            } else if (u.getExperience().compareTo(StatusConstant.EX_3)>=0) {
                                u.setLevel(3);
                            } else if (u.getExperience().compareTo(StatusConstant.EX_2) >= 0) {
                                u.setLevel(2);
                            }
                            userCommentService.updateById(u);
                        }
                        redisUtil.del(e);
                        log.info("增加完成--》{}={}",e,val);
                    }
                });
            }
        });
    }
    /**
     * 每10分钟保存一次系统操作日志
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void saveLog(){
        log.info("开始保存日志------->");
        while (true){
            SysLog header = logQueue.poll();
            if (header == null) {
                break;
            }
            logs.add(header);
        }
        if (logs.size()>0) {
            log.info("开始批量保存日志---->");
            try {
                sysLogService.saveBatch(logs);
            } catch (Exception e) {
               log.error("批量保存日志异常",e);
               logs.clear();
            }
            logs.clear();
        }
    }


}
