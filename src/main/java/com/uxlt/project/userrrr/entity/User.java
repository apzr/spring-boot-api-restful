package com.uxlt.project.userrrr.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author April Z
 * @since 2022-01-05
 */
@Getter
@Setter
@TableName("t_user")
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String activationCode;

    private LocalDateTime birthday;

    private String cellphone;

    private String createBy;

    private LocalDateTime createTime;

    private Integer eduQuali;

    private String email;

    private String findPwdCode;

    private LocalDateTime findPwdLastDate;

    private LocalDateTime lastLoginTime;

    private String loginName;

    private String name;

    private String salt;

    private Integer sex;

    private String shaPassword;

    private Integer status;

    private Integer version;


    @Override
    public String toString(){
        return JSON.toJSONString(
                this,
                SerializerFeature.WRITE_MAP_NULL_FEATURES, //保留为null的属性
                SerializerFeature.QuoteFieldNames //输出key时是否使用双引号（""）将键值都包裹起来,默认为true
                );
    }
}
