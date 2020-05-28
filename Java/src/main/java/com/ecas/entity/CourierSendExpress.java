package com.ecas.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "courier_send_express")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class CourierSendExpress {
    @Id
    @GeneratedValue
    private Long id;

    private Long code;

    private String trackNo;

    @ManyToOne()
    @JoinColumn
    private Courier courier;

    private String reciverPhone;

    @Column
    private String saveTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private ExpressBox box;

    private int isPick;

    //防止sql注入
    @Column(insertable = false)
    private int isDelay;
    @Column(insertable = false)
    private int delayDay;


}
