package com.yc.springtest1.biz;

import com.yc.springtest1.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UserBizImpl implements UserBiz{
    @Autowired
    @Qualifier("userDaoImpl")
    private UserDao userDao;

    public UserBizImpl(){
        System.out.println("userBizImpl的构造");
    }

    @Override
    public void add(String uname) {
        userDao.add(uname);
    }
}
