package com.kyou.blog.model.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author CC
 * time 2023-07-14
 * description
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeVo implements Serializable {
    public static final long serialVersionUID=1L;
    @NotNull

    private String key;
    @NotNull
    @Length(min = 4)
    private String code;
    @NotNull
    private String image;


}
