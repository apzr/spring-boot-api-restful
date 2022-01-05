package com.uxlt.project.userrrr.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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
@ApiModel(value = "UserInfo对象", description = "")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String loginName;


    @Override
    public String toString(){
        return JSON.toJSONString(
                this,
                SerializerFeature.WRITE_MAP_NULL_FEATURES, //保留为null的属性
                SerializerFeature.QuoteFieldNames //输出key时是否使用双引号（""）将键值都包裹起来,默认为true
                );
    }
}
