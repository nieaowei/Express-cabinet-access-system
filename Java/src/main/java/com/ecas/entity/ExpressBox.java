package com.ecas.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "express_box")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpressBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn
    private ExpressCabinet cabinet;

    private int size;

    private int isUsing;
}
