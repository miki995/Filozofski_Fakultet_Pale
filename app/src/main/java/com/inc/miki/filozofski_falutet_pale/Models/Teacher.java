package com.inc.miki.filozofski_falutet_pale.Models;

/**
 * Created by MIKI on 13.2.2017.
 */

public class Teacher {

        private int Id;
        private String FirstName;
        private String LastName;


        public int getId() { return Id; }
        public String getFirstName(){return FirstName;}
        public String getLastName(){return LastName;}

        private void setId(int value){Id = value;}
        private void setFirstName(String value){FirstName = value;}
        private void setLastName(String value){LastName = value;}

    public Teacher(int id, String name, String surname)
    {
            setId(id);
            setFirstName(name);
            setLastName(surname);
    }
    @Override
    public String toString() {
        return getFirstName()+ " " + getLastName();
    }
}
