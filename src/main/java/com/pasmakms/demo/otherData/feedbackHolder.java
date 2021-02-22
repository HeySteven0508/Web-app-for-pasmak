package com.pasmakms.demo.otherData;

import lombok.Data;

@Data
public class feedbackHolder {

    public String date;
    public String user;
    public String feedback;
    public String billRemarks;

    public feedbackHolder()
    {

    }
    public feedbackHolder(String paramDate, String paramUser, String paramFeedback, String billRemarks){
        this.date = paramDate;
        this.user = paramUser;
        this.feedback = paramFeedback;
        this.billRemarks = billRemarks;
    }
}
