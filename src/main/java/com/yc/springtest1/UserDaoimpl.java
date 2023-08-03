package com.yc.springtest1;

import com.yc.springtest1.dao.UserDao;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoimpl implements UserDao {

    public UserDaoimpl() {
        System.out.println("UserDaoImpl类的构造。。。");
    }
    @Override
    public void add(String uname) {

        System.out.println("添加了："+uname);

    }
}
