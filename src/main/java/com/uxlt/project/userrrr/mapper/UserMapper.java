package com.uxlt.project.userrrr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uxlt.project.userrrr.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author April Z
 * @since 2022-01-05
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    List execute();
}
