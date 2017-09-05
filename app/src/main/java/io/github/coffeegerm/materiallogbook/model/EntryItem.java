package io.github.coffeegerm.materiallogbook.model;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;

/**
 * Created by David Yarzebinski on 6/25/2017.
 * <p>
 * <p>
 * EntryItem POJO for setting and retrieving data for layouts
 * <p>
 * for Status
 * <p>
 * case 0: null
 * 1: breakfast
 * 2: lunch
 * 3: dinner
 * 4: sick
 * 5: exercise
 * 6: sweets
 */

public class EntryItem extends RealmObject {
    private String id;
    private int status;
    private Date date;
    private int bloodGlucose;
    private int carbohydrates;
    private double insulin;


    public EntryItem() {
        this.id = UUID.randomUUID().toString();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getBloodGlucose() {
        return bloodGlucose;
    }

    public void setBloodGlucose(int bloodGlucose) {
        this.bloodGlucose = bloodGlucose;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getInsulin() {
        return insulin;
    }

    public void setInsulin(double insulin) {
        this.insulin = insulin;
    }

    public String getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
