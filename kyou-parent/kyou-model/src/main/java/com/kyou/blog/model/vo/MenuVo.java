package com.kyou.blog.model.vo;

import com.kyou.blog.model.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * @author CC
 * time 2023-07-12
 * description
 */
@Data
public class MenuVo extends Menu {
    private List<Menu> children;
}
