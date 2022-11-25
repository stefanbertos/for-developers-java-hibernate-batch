package com.example.demo.vo;

import lombok.Data;

@Data
public class PaymentVO {
    private int step;
    private String type;
    private Double amount;
    private String nameOrig;
    private Double oldBalanceOrg;
    private Double newBalanceOrig;
    private String nameDest;
    private Double oldBalanceDest;
    private Double newBalanceDest;
    private int isFraud;
    private int isFlaggedFraud;
}
