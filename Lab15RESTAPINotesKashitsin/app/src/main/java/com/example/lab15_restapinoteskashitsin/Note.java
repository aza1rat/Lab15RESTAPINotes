package com.example.lab15_restapinoteskashitsin;

public class Note {
    public int id;
    public String name;
    public String text;


    public String toString() //Кашицын,393
    {
        String sText;
        if (text.length() < 21)
            sText = text;
        else
            sText = text.substring(0, 9) + "...";
        return name + "\n" + sText;
    }

}
