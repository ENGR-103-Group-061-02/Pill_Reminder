package com.drexel.engr103grp061_02.pillreminder.database;

/**
 * Created by Matt on 4/18/2016.
 */
public class Pill {
    //TODO Add required fields and methods
    String name;
    int quantity;
    int hours;
    int minutes;
    String instrutctions;

    //SETTER METHODS:
    public void setName(String name)
    {
        this.name=name;
    }
    public void setQuantity(int quantity)
    {
        this.quantity=quantity;
    }
    public void setTime(int hours, int minutes)
    {
        this.hours=hours;
        this.minutes=minutes;
    }
    public void setInstrutctions(String instrutctions)
    {
        this.instrutctions=instrutctions;
    }
    //GETTER METHODS
    public String getName(){
        return name;
    }
    public int getQuantity(){
        return quantity;
    }
    public int getHours(){
        return hours;
    }
    public int getMinutes(){
        return minutes;
    }
    public String getInstrutctions(){
        return instrutctions;
    }







}
