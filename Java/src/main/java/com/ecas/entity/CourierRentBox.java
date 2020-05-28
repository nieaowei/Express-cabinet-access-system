package com.ecas.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.Date;

@Entity
@Table(name="courier_rent_box")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourierRentBox {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Courier courier;


    @ManyToOne
//    @JoinColumn(name = "cabinet_id",referencedColumnName = "id")
    private ExpressBox box;





    private Date finishTime;

        private int finishFlag;

}
