package com.pasmakms.demo.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class BillingEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;
    private String centerName;
    private String qualification;
    private String sgNo;
    private String typeOfScholar;
    private String startOfTraining;
    private String endOfTraining;
    private String billStatus;
    private String sourceOfFunds;
    private String documentType;
    private String billingIdNo;
    private String category;



}
