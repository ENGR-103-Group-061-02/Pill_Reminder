package com.drexel.engr103grp061_02.pillreminder;

import java.util.Calendar;

/**
 * Created by Matt on 5/13/2016.
 */
public class Time {
    int hours;
    int minutes;

    // Default Constructor
    public Time(){

    }

    //Preferred Constructor
    public Time(int hour, int minute){
        hours=hour;
        minutes = minute;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public boolean equals(Time t){
        if(hours == t.getHours()&& minutes == t.getMinutes()){
            return true;
        }
        return false;
    }

    // Return the times hours and minutes in a 12 hour format.
    // The int array returned has three elements the first being the hours, the second being the
    // minutes and the third being AM or PM. 0 for AM and 1 for PM
    public int[] get12HrFormat(){
        int[] hr12Format = new int[3];
        hr12Format[1] = minutes;
        if(hours == 0){
            hr12Format[0] = 12;
            hr12Format[2] = 0;
        }else if(hours == 12){
            hr12Format[0] = 12;
            hr12Format[2] = 1;
        }else if(hours > 12){
            hr12Format[0] = hours % 12;
            hr12Format[2] = 1;
        }else{
            hr12Format[0] = hours;
            hr12Format[2] = 0;
        }

        return hr12Format;
    }

    public String getTimeFormattedString(){
        String formattedString;

        int[] times12Format = this.get12HrFormat();
        formattedString = Integer.toString(times12Format[0])+":";

        if(times12Format[1]<10){
            formattedString = formattedString+"0"+Integer.toString(times12Format[1]);
        }else{
            formattedString = formattedString+Integer.toString(times12Format[1]);
        }

        if(times12Format[2]==0){
            formattedString = formattedString+" AM";
        }else{
            formattedString = formattedString+" PM";
        }

        return formattedString;
    }

    public Time subtract(Time time){
        Time out = new Time();
        out.setHours(this.getHours() - time.getHours());
        out.setMinutes(this.getMinutes() - time.getMinutes());
        return out;
    }
}
