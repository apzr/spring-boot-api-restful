package com.uxlt.project.userrrr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.uxlt.project.userrrr.entity.User;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author April Z
 * @since 2022-01-05
 */
public interface IUserService extends IService<User> {

    List<Map<String,Object>> execute();
}
