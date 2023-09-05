package com.kyou.blog.background.webUtil;

import com.kyou.blog.common.constant.MsgConstant;
import com.kyou.blog.common.constant.RoleConstant;
import com.kyou.blog.model.entity.Comments;
import com.kyou.blog.model.entity.Role;
import com.kyou.blog.model.vo.CommentsVo;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author CC
 * time 2023-07-26
 * description
 */
public final class WebUtil {
    public static final String DATE_PATH_LINUX ="yyyy/MM/dd";
    public static final int[] max_31={1,3,5,7,8,10,12};
    public static boolean checkAdmin(Long userID,List<Role> roles){
      if (RoleConstant.ADMIN==userID){
          return true;
      }
        if (CollectionUtils.isEmpty(roles)) {
            return false;
        }
        for (Role role : roles) {
            if (role.getKeyName().equals("admin")) {
                return true;
            }
        }
        return false;
    }
    public static int judgeDay(LocalDate now){
        int monthValue = now.getMonthValue();
        int i = Arrays.binarySearch(max_31, monthValue);
        if (i<0||max_31[i]!=monthValue) {
            if(monthValue==2){
                return now.isLeapYear()?29:28;
            }else {
                return 30;
            }
        }
        return 31;
    }
    public static boolean isTrue(Boolean b){
        return Boolean.TRUE.equals(b);
    }
    public static boolean containRole(List<Role> roles,Integer roleID){
        return roles.stream().anyMatch(role -> role.getId().intValue()==roleID);
    }
    public static Integer listSize(List list){
        return list==null?0:list.size();
    }
    public static Comments wrapperComments(Map<String,Object> map, HttpServletRequest request){
        if (CollectionUtils.isEmpty(map)) {
            throw new NullPointerException(MsgConstant.QUERY_PARAM_ERR);
        }
        Long uid=Long.valueOf(((Integer) map.get("uid")));
        String address = (String) map.get("address");
        String content = (String) map.get("content");
        String pname = (String) map.get("pname");
        Long likes = Long.valueOf(((Integer) map.get("likes")));
        String createTime = (String) map.get("createTime");
        Map<String, Object> user = (Map<String, Object>) map.get("user");
        String username = (String) user.get("username");
        String avatar = (String) user.get("avatar");
        Object parentId = map.get("parentId");
        Long articleId = Long.valueOf((String) map.get("articleId"));
        Comments comments = new Comments();
        comments.setAddress(address);
        comments.setIp(request.getRemoteAddr());
        comments.setLikes(likes);
        comments.setCreateTime(WebUtil.parseDateTime(createTime).withNano(0));
        comments.setArticleId(articleId);
        comments.setUid(uid);
        comments.setContent(content);
        comments.setParentId(parentId==null?null:Long.valueOf(String.valueOf(parentId)));
        comments.setAvatar(avatar);
        comments.setNickname(username);
        comments.setPname(pname);
        return comments;
    }

    public static void findCommentChild(CommentsVo e, List<CommentsVo> cvs) {
        if (CollectionUtils.isEmpty(e.getList())) {
            e.setList(new ArrayList<>());
        }
        cvs.forEach(o->{
            Long pid = o.getParentId();
            if (pid!=null&&pid.equals(e.getId())) {
                findCommentChild(o,cvs);
                e.getList().add(o);
                e.setTotal(e.getList().size());
            }
        });

    }

    public static boolean isNumber(String str){
        if (!StringUtils.hasText(str)) {
            return false;
        }
        return str.matches("^\\d+$");
    }
    public static void delExistsFile(String path){
        File r = new File(path);
        if (r.exists()) {
            r.delete();
        }
    }
    public static HttpServletRequest getReq(){
        RequestAttributes rs = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes ss = (ServletRequestAttributes) rs;
        return ss.getRequest();
    }
    public static Set<String> loadYml(String fileName, String pattern){
        YamlPropertiesFactoryBean yml = new YamlPropertiesFactoryBean();
        yml.setResources(new ClassPathResource(fileName));
        Properties object = yml.getObject();
        return object.keySet().stream().filter(i -> i != null&&i.toString().startsWith(pattern))
                .map(i -> object.getProperty(i.toString())).collect(Collectors.toSet());
    }
    public static String formatTime(String pattern){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }
    public static boolean strContains(String str,String[] aim){
        if (!StringUtils.hasText(str)||aim==null||aim.length<=0) {
            return false;
        }
        for (int i = 0; i < aim.length; i++) {
            if (aim[i].equals(str)) {
                return true;
            }
        }
        return false;
    }
    public static boolean notNullNumber(Number number){
        return number!=null&&number.doubleValue()>0.0;
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
    public static String uuid(boolean isSimple){
        if(isSimple){
            return UUID.randomUUID().toString().replace("-","");
        }
        return UUID.randomUUID().toString();
    }
    public static LocalDateTime parseDateTime(String dateStr){
        checkDateTime(dateStr);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
        return LocalDateTime.parse(dateStr,format);
    }
    public static boolean checkTime(String timeStr){
        if (!StringUtils.hasText(timeStr)) {
            throw new IllegalArgumentException("无效时间");
        }
        if (!timeStr.matches("^[0-2][0-9]:[0-6][0-9]:[0-6][0-9]")) {
            throw new IllegalArgumentException("格式不匹配-》");
        }
        String[] s = timeStr.split(":");
        if (s[0].compareTo("00") < 0 || s[0].compareTo("24") >0) {
            throw new IllegalArgumentException("无效时间-》"+timeStr);
        }
        for (int i=1;i<=2;i++){
            if (s[i].compareTo("00")<0||s[i].compareTo("60")>0) {
                throw new IllegalArgumentException("无效时间-》"+timeStr);
            }
        }
        return true;
    }
    public static Claims parseJWT(String secret, String jwt) {
        SecretKey secretKey = generalKey(secret);
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException(e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
    public static SecretKey generalKey(String secret) {
        byte[] encodedKey = Base64.getEncoder().encode(secret.getBytes());
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    public static boolean checkDateTime(String dateTimeStr){
        if (!StringUtils.hasText(dateTimeStr)) {
            throw new IllegalArgumentException("无效参数-->"+dateTimeStr);
        }
        String trim = dateTimeStr.trim();
        String date = trim.substring(0, 10);
        boolean v1 = checkDate(date);
        String time= trim.substring(11);
        boolean v2 = checkTime(time);
        return v2;
    }
    public static String createJWT(String secret,Map<String,Object> data,long expireMinute) {
        if (CollectionUtils.isEmpty(data)) {
            throw new RuntimeException("参数无效");
        }
        Date now = new Date();
        Date expDate = new Date(now.getTime()+ TimeUnit.MINUTES.toMillis(expireMinute));
        SecretKey secretKey = generalKey(secret);
        return Jwts.builder()
                // 设置唯一编号
                .setId(uuid(true)).setIssuer("admin")
                // 设置签发日期和过期时间
                .setIssuedAt(now).setExpiration(expDate)
                // 设置载体，签名 使用HS256算法 并设置SecretKey(字符串)
                .addClaims(data).signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }
    public static boolean checkDate(String dateStr){
        if (!StringUtils.hasText(dateStr)) {
            throw new IllegalArgumentException("无效日期");
        }
        if (!dateStr.matches("^[1-9]\\d{3}-\\d{2}-\\d{2}$")) {
            throw new RuntimeException("格式不匹配--");
        }
        String[] date = dateStr.split("-");
        if (date[1].compareTo("12")>0) {
            throw new RuntimeException("非正确日期--"+dateStr);
        }
        if ("00".equals(date[1])||"00".equals(date[2])) {
            throw new RuntimeException("没有此日期--"+dateStr);
        }
        if (date[1].equals("02")&&date[2].compareTo("28")>0) {
            throw new RuntimeException("没有此日期--"+dateStr);
        }
        if (!"02".equals(date[1])) {
            String[] more={"01","03","05","07","08","10","12"};
            if (Arrays.stream(more).anyMatch(str->str.equals(date[1]))) {
                if (date[2].compareTo("31")>0) {
                    throw new RuntimeException("没有此日期--"+dateStr);
                }
            }else {
                if (date[2].compareTo("30")>0) {
                    throw new RuntimeException("没有此日期--"+dateStr);
                }
            }
        }
        return true;
    }
}
