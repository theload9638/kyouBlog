package com.kyou.blog.common.util;

import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


/**
 * @author CC
 * time 2023-07-11
 * description  oss对象存储服务
 */
@Component
@Slf4j
@ConditionalOnClass(value = MinioClient.class)
@ConditionalOnProperty(prefix = "minio",name = "enabled",havingValue = "true")
public class MinioUtil {
    private MinioClient minioClient;
    public static final String[] types=new String[]{"image/png","image/jpeg"};
    @Value("${minio.bucket}")
    private String bucket;
    @Value("${minio.url}")
    private String url;
    @Value("${minio.username}")
    private String username;
    @Value("${minio.password}")
    private String password;
    {
        log.info("初始化oss对象存储服务工具类--》{},{},{},{}",bucket,url,username,password);
    }
    @PostConstruct
    public void init(){
        //创建minio客户端
        this.minioClient= MinioClient.builder().
                credentials(username, password)
                .endpoint(url).build();
    }
    /**
     * 上传文件
     * @param file
     * @return
     */
   public String simpleUploadFile(MultipartFile file){
       try {
           InputStream inputStream = file.getInputStream();
           String fileName = getSimpleUUidFileName(file);
           PutObjectArgs build = PutObjectArgs.builder()
                   .object(fileName)
                   .contentType(file.getContentType())
                   .bucket(bucket)
                   .stream(inputStream, inputStream.available(), -1)
                   .build();
           minioClient.putObject(build);
           return String.format("%s/%s/%s",url,bucket,fileName);
       }catch (Exception e){
           throw new RuntimeException(e);
       }
   }
    public static String getSimpleUUidFileName(MultipartFile multipartFile){
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        return uuid(true)+suffix;
    }
    public static String uuid(boolean isSimple){
        if(isSimple){
            return UUID.randomUUID().toString().replace("-","");
        }
        return UUID.randomUUID().toString();
    }
    /**
     * 删除文件
     * @param path
     */
   public void delFile(String path){
       try {
           String key = path.replace(String.format("%s/%s/", url, bucket), "");
           RemoveObjectArgs build = RemoveObjectArgs.builder().
                   bucket(bucket).object(key).build();
           minioClient.removeObject(build);
       } catch (Exception e) {
        throw new RuntimeException(e);
       }
   }
    /**
     * 下载文件
     * @param path
     * @return
     */
   public byte[] download(String path){
       try {
           String key = path.replace(String.format("%s/%s/", url, bucket), "");
           InputStream in = minioClient.getObject(GetObjectArgs.builder().
                   bucket(bucket).object(key).build());
           return streamToBytes(in);
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
   }
    public static byte[] streamToBytes(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        BufferedInputStream in=new BufferedInputStream(inputStream);
        byte[] bytes=new byte[1024];
        int receive;
        while ((receive=in.read(bytes))!=-1){
            out.write(bytes,0,receive);
        }
        out.close();
        return out.toByteArray();
    }
}
