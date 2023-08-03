package com.yc.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OpRecord {
    private int id;
    private int acccountid;
    private double opmoney;
    private String optime;
    private OpType opType;
    private Integer transferid;
}
