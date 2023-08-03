package com.kyou.blog.background.security;

import com.alibaba.fastjson.annotation.JSONField;
import com.kyou.blog.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CC
 * time 2023-07-19
 * description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@NotNull
public class SysUser implements UserDetails {
    private static final long serialVersionUID = 1L;
    @Min(value = 0,message = "用户标识错误")
    @NotNull
    private Long id;
    @Valid
    private User user;
    private List<String> perms;
    public SysUser(User user,List<String> perms){
        this.id=user.getId();
        this.user=user;
        this.perms=perms;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return perms.stream().distinct().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return true;
    }
}
