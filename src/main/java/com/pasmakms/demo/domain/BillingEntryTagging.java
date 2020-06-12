package com.pasmakms.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class BillingEntryTagging {

    @Id
    private long id;
    private long billingEntryId;
    private String billingIdNo;
    private String documentType;

    public BillingEntryTagging(){

    }
    public BillingEntryTagging(long id){
        this.billingEntryId = id;
    }




}
