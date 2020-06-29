package com.pasmakms.demo.otherData;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateToday {

    LocalDateTime localDateTime = LocalDateTime.now();
    Date currentDate = new Date();
    DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MMMM dd, YYYY");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, YYYY");

    String dateCreated = dateTimeFormat.format(localDateTime);

    Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());


    public String getDateToday() {
        return dateCreated;
    }

    public Date getCurrentDateToday() {
        return date ;
    }




}
