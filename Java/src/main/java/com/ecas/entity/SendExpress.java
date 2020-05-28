package com.ecas.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "send_express")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendExpress {
    @Id
    @GeneratedValue
    private Long id;

    private String sendName;

    private String sendPhone;

    private String sendAdress;

    private String reciveName;

    private String recivePhone;

    private String reciveAddress;

    @ManyToOne
    private ExpressBox box;

    @ManyToOne
    @JoinColumn(name = "company", referencedColumnName = "id")
    private Company company;

    private String detail;
}
