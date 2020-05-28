package com.ecas.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "express_company")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue
    private long id;

    String companyName;

}
