package com.kyou.blog.common.util;

import com.baomidou.mybatisplus.extension.api.R;
import com.kyou.blog.common.Result;
import com.kyou.blog.model.vo.CodeVo;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * author CC
 * time 2023-04-22
 * description
 */
public class VerifyCodeUtil {

    private static int width =100;
    private static int height = 40;
    public static CodeVo handlerCode(String key,int len, HttpServletResponse response){
        String code = VerifyCodeUtil.generateCode(4);
        BufferedImage image = VerifyCodeUtil.generateImage(code);
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", -1);
        String base64 ="";
        try {
            base64 = VerifyCodeUtil.imageToBase64(image);
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate captcha.");
        }
        return new CodeVo(key, code, "data:image/png;base64," + base64);
    }
    /**
    *
    *@param code 根据验证码生成图片
    */
    public static BufferedImage generateImage(String code) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 生成随机背景色
        g.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
        g.fillRect(0, 0, width, height);

        // 绘制随机字符
        char[] chars = code.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            g.setFont(new Font("宋体", Font.BOLD, 20));
            g.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
            g.drawString(String.valueOf(chars[i]), 20 * i + 10, 25);
        }
        g.dispose();
        return image;
    }
    /**
    * 将生成的图片转换为base64
    *@param image 生成的图片
    */
    public static String imageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        return DatatypeConverter.printBase64Binary(out.toByteArray());
    }
    /**
    *   生成验证码
    *@param length 生成的验证码长度 
    */
    public static String generateCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) (Math.random() * 26 + 'A'));  // 随机生成大写字母
        }
        return sb.toString();
    }

}
