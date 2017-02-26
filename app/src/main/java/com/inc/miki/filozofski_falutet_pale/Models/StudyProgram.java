package com.inc.miki.filozofski_falutet_pale.Models;

/**
 * Created by MIKI on 13.2.2017.
 */

public class StudyProgram {
    private int Id;
    private String Name;


    public int getId() { return Id; }
    public String getName(){return Name;}

    private void setId(int value){Id = value;}
    private void setName(String value){Name = value;}

    public StudyProgram(int id, String name)
    {
        setId(id);
        setName(name);

    }
    @Override
    public String toString() {
        return getName();
    }
}
