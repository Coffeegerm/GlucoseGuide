package io.github.coffeegerm.materiallogbook.model;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;

/**
 * Created by David Yarzebinski on 6/25/2017.
 * <p>
 * <p>
 * EntryItem POJO for setting and retrieving data for layouts
 */

public class EntryItem extends RealmObject {
    private Date date;
    private int bloodGlucose;
    private int carbohydrates;
    private double insulin;
    private String id;

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
}
