package com.pasmakms.demo.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@Entity
public class BillingEntryNotes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String billCreated;
    private String billChecked;
    private String billVerified;
    private String billAudited;
    private String billDocControl;
    private String billCheckPrepare;
    private String billCheckRelease;
    private String billNotes = "";
    private long billingEntryId;

    public BillingEntryNotes(){

    }



    public BillingEntryNotes(String steps,long id) {

        if(steps.equalsIgnoreCase("New")){
            LocalDateTime localDateTime = LocalDateTime.now();
            Date currentDate = new Date();
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MMMM dd, YYYY");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            String dateCreated =  dateTimeFormat.format(localDateTime);
            String timeCreated = timeFormat.format(currentDate);

            this.billCreated = dateCreated;
            this.billNotes = this.billNotes + dateCreated + " - " + timeCreated + " :" +" The Scholarship Billing Entry was created By Sir Nap. ||";
            this.billingEntryId = id;


        }
    }


    public void addBillingNotes (String status){

        LocalDateTime localDateTime;

        if(status.equalsIgnoreCase("for Review")){
            localDateTime = LocalDateTime.now();
            Date currentDate = new Date();
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MMMM dd, YYYY");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            String dateCreated =  dateTimeFormat.format(localDateTime);
            String timeCreated = timeFormat.format(currentDate);

            this.billNotes = this.billNotes +  dateCreated + " - " + timeCreated + " :" +" The billing document was finalized and submitted to Leo for reviwing the document. ||   ";

        }
        else if(status.equalsIgnoreCase("for Verify")){
            localDateTime = LocalDateTime.now();
            Date currentDate = new Date();
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MMMM dd, YYYY");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            String dateCreated =  dateTimeFormat.format(localDateTime);
            String timeCreated = timeFormat.format(currentDate);

            this.billChecked = dateCreated;
            this.billNotes = this.billNotes +  dateCreated + " - " + timeCreated + " :" +" The billing document was checked and submitted to Josie for verifying the candidates. ||   ";

        }

        else if(status.equalsIgnoreCase("for Audit")){
            localDateTime = LocalDateTime.now();
            Date currentDate = new Date();
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MMMM dd, YYYY");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            String dateCreated =  dateTimeFormat.format(localDateTime);
            String timeCreated = timeFormat.format(currentDate);

            this.billVerified = dateCreated;
            this.billNotes = this.billNotes +  dateCreated + " - " + timeCreated + " :" +" The billing document was verified with a minimum of 5 candidates and submitted to Ms. Kim for Audit. || ";

        }

        else if(status.equalsIgnoreCase("For Signature")){
            localDateTime = LocalDateTime.now();
            Date currentDate = new Date();
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MMMM dd, YYYY");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String dateCreated =  dateTimeFormat.format(localDateTime);
            String timeCreated = timeFormat.format(currentDate);

            this.billAudited = dateCreated;
            this.billNotes = this.billNotes +  dateCreated + " - " + timeCreated + " :" +" The billing document was audited by Ms. Kim and sent back to leo for signatory. || ";

        }


        else if(status.equalsIgnoreCase("Prepare Checks")){
            localDateTime = LocalDateTime.now();
            Date currentDate = new Date();
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MMMM dd, YYYY");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            String dateCreated =  dateTimeFormat.format(localDateTime);
            String timeCreated = timeFormat.format(currentDate);

            this.billDocControl = dateCreated;
            this.billNotes = this.billNotes +  dateCreated + " - " + timeCreated + " :" +" The billing document was signed by the signatories and submitted to Ma'am. Beth for preparation of Checks. || ";

        }

        else if(status.equalsIgnoreCase("Check Issuance")){
            localDateTime = LocalDateTime.now();
            Date currentDate = new Date();
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MMMM dd, YYYY");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            String dateCreated =  dateTimeFormat.format(localDateTime);
            String timeCreated = timeFormat.format(currentDate);

            this.billCheckPrepare = dateCreated;
            this.billNotes = this.billNotes +  dateCreated + " - " + timeCreated + " :" +" All checks are prepared and ready to be released. || ";


        }

        else if(status.equalsIgnoreCase("Completed")){
            localDateTime = LocalDateTime.now();
            Date currentDate = new Date();
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MMMM dd, YYYY");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            String dateCreated =  dateTimeFormat.format(localDateTime);
            String timeCreated = timeFormat.format(currentDate);

            this.billCheckRelease = dateCreated;
            this.billNotes = this.billNotes +  dateCreated + " - " + timeCreated + " :" +" All checks are released and all the process of this billing is completed.";


        }




    }
}
