package com.ecas.entity;


import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "comment")
@Data
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Customer consumer;

    @ManyToOne
    @JoinColumn(name = "object_to",referencedColumnName = "id")
    private Courier objectTo;

    private String content;

    private Date date;
}
