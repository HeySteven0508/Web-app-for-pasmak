package com.pasmakms.demo.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class BillingFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int billingId;
    private String billFeedback;
    private Date billDate;
    private String user;
    private String billRemarks;

    public BillingFeedback(){

    }
    public BillingFeedback(int billingId, String billFeedback, Date billDate, String user, String remarks){
        this.billingId = billingId;
        this.billFeedback = billFeedback;
        this.billDate = billDate;
        this.user = user;
        this.billRemarks = remarks;
    }


}
