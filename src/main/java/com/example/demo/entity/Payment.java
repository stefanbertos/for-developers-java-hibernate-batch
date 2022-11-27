package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq")
    @SequenceGenerator(name = "payment_gen", sequenceName = "payment_seq")
    private long id;

    private int step;
    private String type;
    private Double amount;
    @Column(name = "nameorig")
    private String nameOrig;
    @Column(name = "oldbalanceorg")
    private Double oldBalanceOrg;
    @Column(name = "newbalanceorig")
    private Double newBalanceOrig;
    @Column(name = "namedest")
    private String nameDest;
    @Column(name = "oldbalancedest")
    private Double oldBalanceDest;
    @Column(name = "newbalancedest")
    private Double newBalanceDest;
    @Column(name = "isfraud")
    private int isFraud;
    @Column(name = "isflaggedfraud")
    private int isFlaggedFraud;
}
