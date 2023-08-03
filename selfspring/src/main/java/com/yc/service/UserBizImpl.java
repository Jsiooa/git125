package com.yc.service;

import com.yc.dao.UserDao;
import com.yc.service.UserBiz;
import org.ycframework.annotation.YcResource;
import org.ycframework.annotation.YcService;

@YcService
public class UserBizImpl implements UserBiz {
    @YcResource(name="userDaoImpl")
    private UserDao userDao;
    @Override
    public void add(String name) {
        userDao.add(name);
    }
}
