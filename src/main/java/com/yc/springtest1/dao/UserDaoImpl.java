package com.yc.springtest1.dao;

import com.yc.springtest1.UserDaoimpl;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao{
    public UserDaoImpl(){
        System.out.println("UserDaoImpl类的构造");
    }

    @Override
    public void add(String uname) {
        System.out.println("添加了"+uname);
    }
}
