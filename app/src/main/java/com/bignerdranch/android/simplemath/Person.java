package com.bignerdranch.android.simplemath;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Person implements Serializable {
   private String namePerson;
    private  int multiplicationGood;
    private  int multiplicationBad;
    private int divideGood;
    private  int divideBad;

    public Person(String namePerson, int multiplicationGood, int multiplicationBad, int divideGood, int divideBad) {
        this.namePerson = namePerson;
        this.multiplicationGood = multiplicationGood;
        this.multiplicationBad = multiplicationBad;
        this.divideGood = divideGood;
        this.divideBad = divideBad;
    }

    public String getNamePerson() {
        return namePerson;
    }

    public void setNamePerson(String namePerson) {
        this.namePerson = namePerson;
    }

    public int getMultiplicationGood() {
        return multiplicationGood;
    }

    public void setMultiplicationGood(int multiplicationGood) {
        this.multiplicationGood = multiplicationGood;
    }

    public int getMultiplicationBad() {
        return multiplicationBad;
    }

    public void setMultiplicationBad(int multiplicationBad) {
        this.multiplicationBad = multiplicationBad;
    }

    public int getDivideGood() {
        return divideGood;
    }

    public void setDivideGood(int divideGood) {
        this.divideGood = divideGood;
    }

    public int getDivideBad() {
        return divideBad;
    }

    public void setDivideBad(int divideBad) {
        this.divideBad = divideBad;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        System.out.println("Our writeObject");
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        System.out.println("Our readObject");
    }

}

