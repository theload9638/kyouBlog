package com.kyou.blog.common.util;

import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author CC
 * time 2023-07-10
 * description
 */
@Slf4j
//@Component
//@ConditionalOnClass(com.google.code.kaptcha.Producer.class)
public class CodeUtil {
    static {
        log.info("验证码工具注册...");
    }
    @Resource
    private Producer kaptchaProducer;
    /**
     * 生成验证码并发送
     * @param response
     * @return
     */
    public String generateAndSend(HttpServletResponse response){
        try {
            //响应立即过期
            response.setDateHeader("Expires",0);
            //不缓存任何图片数据
            response.setHeader("Cache-Control" , "no-store,no-cache,must-revalidate");
            response.setHeader("Cache-Control" , "post-check=0,pre-check=0");
            response.setHeader("Pragma" , "no-cache");
            response.setContentType("image/png");
            //生成验证码字符文本
            String verifyCode = kaptchaProducer.createText();
            //创建验证码图片
            BufferedImage image = kaptchaProducer.createImage(verifyCode);
            //二进制的图片，用getOutputStream()方法
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image,"png",byteArrayOutputStream);
            BASE64Encoder base64Encoder = new BASE64Encoder();
            String str="data:image/png;base64,";
            //前端直接用src接着
            String base64Img = str+base64Encoder.encode(byteArrayOutputStream.toByteArray());
            log.info("生成的验证码：--->{}",verifyCode);
            return base64Img;
        } catch (IOException e) {
            log.error("验证码发生异常：-->{}",e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
