package com.inc.miki.filozofski_falutet_pale.Models;

/**
 * Created by MIKI on 13.2.2017.
 */

public class ClassRoom {
    private int Id;
    private String Name;


    private int getId() { return Id; }
    private String getName(){return Name;}

    private void setId(int value){Id = value;}
    private void setName(String value){Name = value;}

    public ClassRoom(int id, String name)
    {
        setId(id);
        setName(name);

    }
    @Override
    public String toString() {
        return getName();
    }
}
