package io.github.coffeegerm.materiallogbook.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by David Yarzebinski on 6/25/2017.
 * <p>
 * <p>
 * EntryItem POJO for setting and retrieving data for layouts
 */

public class EntryItem extends RealmObject {
    private Date mDate;
    private int mGlucose;
    private int mCarbohydrates;
    private double mInsulin;

    public EntryItem() {
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public long getTimestamp() {
        return mDate.getTime();
    }

    public int getGlucose() {
        return mGlucose;
    }

    public void setGlucose(int glucose) {
        mGlucose = glucose;
    }

    public int getCarbohydrates() {
        return mCarbohydrates;
    }

    public void setCarbohydrates(int carbohydrates) {
        mCarbohydrates = carbohydrates;
    }

    public double getInsulin() {
        return mInsulin;
    }

    public void setInsulin(double insulin) {
        mInsulin = insulin;
    }
}
