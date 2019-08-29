package com.ksw.simple_calender;

import java.util.Date;

public class DateAttr {

    int year;
    int month;
    int day;
    int hour;
    int minute;
    int week;

    DateAttr(int year, int month, int day, int hour, int minute){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    DateAttr(int year, int month, int day, int week){
        this.year = year;
        this.month = month;
        this.day = day;
        this.week = week;
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

    public long getDateTime() {
        return (long)year * 100000000 + month * 1000000 + day * 10000 + hour * 100 + minute;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public DateAttr getNextDay(){
        Date myDate = new Date(year - 1900, month - 1, 1);
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
                Date myDate = new Date(year - 1900 - 1, 12 - 1, 1);
                myDate.setDate(32);
                int last =  32- myDate.getDate();
                return new DateAttr( year - 1,12, last, hour, minute);
            }

            Date myDate = new Date(year - 1900, month - 1 - 1, 1);
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

    public boolean isSameDay(DateAttr date){
        if (year == date.year && month == date.month && day == date.day){
            return true;
        }

        return false;
    }

    public boolean isSameDate(DateAttr start) {
        if (isSameDay(start)){
            if (hour == start.hour && minute == start.minute){
                return true;
            }
        }

        return false;
    }

    public void copyTo(DateAttr date) {
        this.year = date.year;
        this.month = date.month;
        this.day = date.day;
        this.hour = date.hour;
        this.minute = date.minute;
        this.week = date.week;
    }
}
