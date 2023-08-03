package com.yc.dao;

import com.yc.bean.Account;

import java.util.List;

public interface AccountDao {

    public int insert(double money);


    public void update(int accountid,double money);

    public void delete(int accountid);

    public int findCount();

    public List<Account> findAll();

    public Account findById(int accountid);
}
