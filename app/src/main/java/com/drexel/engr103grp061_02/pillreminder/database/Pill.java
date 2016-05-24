package com.drexel.engr103grp061_02.pillreminder.database;

import com.drexel.engr103grp061_02.pillreminder.Time;

/**
 * Created by Matt on 4/18/2016.
 */
public class Pill {
    //TODO Add required fields and methods
    String name;
    int quantity;
    int hours;
    int minutes;
    String instructions;
    int id;

    public Pill (){}
    public Pill(String _name, int _quantity, int _hours, int _minutes, String _instructions){
        name = _name;
        quantity = _quantity;
        hours = _hours;
        minutes = _minutes;
        instructions = _instructions;
    }

    //SETTER METHODS:
    public void setId(int _id){this.id = _id;}
    public void setName(String name)
    {
        this.name=name;
    }
    public void setQuantity(int quantity)
    {
        this.quantity=quantity;
    }
    public void setTime(int hours, int minutes) {
        this.hours=hours;
        this.minutes=minutes;
    }
    public void setInstrutctions(String instrutctions)
    {
        this.instructions=instrutctions;
    }

    //GETTER METHODS
    public int getId(){return id;}
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
    public String getInstructions(){
        return instructions;
    }
    public Time getTime(){
        return new Time(hours,minutes);
    }
}
