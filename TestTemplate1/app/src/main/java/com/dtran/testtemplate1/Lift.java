package com.dtran.testtemplate1;

public class Lift {
    private long id;
    private String lift;
    private String bodyPart;
    private double weight;
    private int reps;
    private long insert_date;
    private long lift_date;
    private int active;
    //private long update_date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLift() {
        return lift;
    }

    public void setLift(String lift) {
        this.lift = lift;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setInsertDate(long insert_date) {
        this.insert_date = insert_date;
    }

    public long getInsertDate() {
        return insert_date;
    }

    public void setLiftDate(long lift_date) {
        this.lift_date = lift_date;
    }

    public long getLiftDate() {
        return lift_date;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getActive() {
        return active;
    }

    // Will be used by the ArrayAdapter in the ListView
    /*
    @Override
    public String toString() {
        return Lift;
    }
    */
}
