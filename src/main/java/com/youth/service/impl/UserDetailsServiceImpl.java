package com.youth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.youth.common.UserDetailsImpl;
import com.youth.dao.MenuDao;
import com.youth.dao.UserDao;
import com.youth.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private MenuDao menuDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = userDao.selectOne(queryWrapper);
        // 如果没有用户，就抛出异常
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户名或者密码错误");
        }

        // 查询用户对应的权限信息
        List<String> permissions = menuDao.selectPermsByUserId(user.getId());

        // 把数据封装为UserDetails返回
        return new UserDetailsImpl(user,permissions);
    }
}

