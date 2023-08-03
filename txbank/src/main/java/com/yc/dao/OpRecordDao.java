package com.yc.dao;

import com.yc.bean.OpRecord;

import java.util.List;

public interface OpRecordDao {

    public void insertOpRecord(OpRecord opRecord);

    public List<OpRecord> findOpRecord(int accountid);

    public List<OpRecord> findOpRecord(int accountid,String opType);

    public List<OpRecord> findOpRecord(OpRecord opRecord);
}
