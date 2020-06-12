package com.pasmakms.demo.domain;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateToday {

    LocalDateTime localDateTime = LocalDateTime.now();
    Date currentDate = new Date();
    DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MMMM dd, YYYY");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    String dateCreated =  dateTimeFormat.format(localDateTime);

    public String getDateToday(){
        return dateCreated;
    }


}
