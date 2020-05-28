package com.ecas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "courier")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Courier {
    @Id
    @GeneratedValue
    private Long id;

    private String phone;

    @JsonIgnore
    private String password;

    private String idCard;

    private String name;

    @ManyToOne
    @JoinColumn(name = "company" ,referencedColumnName = "id")
    private Company company;

}
