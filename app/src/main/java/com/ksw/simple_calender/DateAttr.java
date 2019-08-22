package com.ksw.simple_calender;

import java.util.Date;

public class DateAttr {

    int year;
    int month;
    int day;
    int hour;
    int minute;

    DateAttr(int year, int month, int day, int hour, int minute){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    DateAttr(long dateTime) {
        this.minute = (int)(dateTime % 100);
        dateTime /= 100;
        this.hour = (int)(dateTime % 100);
        dateTime /= 100;
        this.day = (int)(dateTime % 100);
        dateTime /= 100;
        this.month = (int)(dateTime % 100);
        dateTime /= 100;
        this.year = (int)dateTime;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public long getDateTime()
    {
        return (long)year * 100000000 + month * 1000000 + day * 10000 + hour * 100 + minute;
    }

    public DateAttr getNextDay(){
        Date myDate = new Date(year, month, 1);
        myDate.setDate(32);
        int last =  32- myDate.getDate();
        if (day == last){
            if (month == 12){
                return new DateAttr( year + 1,1,1,hour,minute);
            }

            return new DateAttr( year,month + 1,1,hour,minute);
        }
        else {
            return new DateAttr( year,month,day + 1,hour,minute);
        }
    }

    public DateAttr getPrevDay() {
        if (day == 1){
            if (month == 1) {
                Date myDate = new Date(year - 1, 12, 1);
                myDate.setDate(32);
                int last =  32- myDate.getDate();
                return new DateAttr( year - 1,12, last, hour, minute);
            }

            Date myDate = new Date(year,  month - 1, 1);
            myDate.setDate(32);
            int last =  32- myDate.getDate();

            return new DateAttr( year,month - 1,last, hour, minute);
        }
        else {
            return new DateAttr( year,month,day + 1,hour,minute);
        }
    }


    public DateAttr getNextMonth(){
        if (month == 12){
            return new DateAttr( year + 1,1,day, hour, minute);
        }

        return new DateAttr( year,month + 1,day, hour, minute);
    }

    public DateAttr getPrevMonth() {
        if (month == 1) {
            return new DateAttr( year - 1,12, day, hour, minute);
        }

        return new DateAttr( year,month - 1, day, hour, minute);
    }
}
