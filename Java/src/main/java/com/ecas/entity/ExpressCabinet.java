package com.ecas.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "express_cabinet")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class ExpressCabinet {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String address;

    private int bigbox;

    private int middlebox;

    private int smallbox;
}
