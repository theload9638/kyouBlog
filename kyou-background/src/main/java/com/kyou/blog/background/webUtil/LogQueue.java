package com.kyou.blog.background.webUtil;

import com.kyou.blog.model.entity.SysLog;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author CC
 * time 2023-07-31
 * description
 */
@Component
public class LogQueue {

    private volatile Queue<SysLog> queue=new LinkedBlockingQueue<>();

    public void add(SysLog log){
        queue.add(log);
    }
    public SysLog poll(){
        return queue.poll();
    }
}
