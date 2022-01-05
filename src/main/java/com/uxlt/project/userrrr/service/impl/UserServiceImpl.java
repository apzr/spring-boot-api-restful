package com.uxlt.project.userrrr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uxlt.project.userrrr.entity.User;
import com.uxlt.project.userrrr.mapper.UserMapper;
import com.uxlt.project.userrrr.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author April Z
 * @since 2022-01-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public List execute(){
        return userMapper.execute();
    }
}
