package com.ecas.entity;


import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table
@Data
public class ExpressSendOrder {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    Courier courier;

    @ManyToOne
    SendExpress express;

    //    @ColumnTransformer(write = "uuid_short(?)")
    private String orderNo;

    @ManyToOne
    private Customer customer;

    private BigDecimal payment;

//    @Column(insertable = false)
    private int status;

    @Column(insertable = false)
    private Date paymentTime;

    @Column(insertable = false)
    private Date createTime;

    @Column(insertable = false)
    private  int code;

    @Column(insertable = false)
    private int isSave;
}
