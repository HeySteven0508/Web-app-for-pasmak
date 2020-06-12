package com.pasmakms.demo.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class BillingFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int billingId;
    private String billFeedback;


}
