package com.ecas.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;

@Entity
@Table(name = "customer_ord")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String username;
    @Column
    private String phone;
    @Column
    @JsonIgnore
    private String password;

}
