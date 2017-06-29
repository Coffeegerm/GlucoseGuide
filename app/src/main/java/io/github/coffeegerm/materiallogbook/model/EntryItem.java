package io.github.coffeegerm.materiallogbook.model;

import java.util.Date;

/**
 * Created by David Yarzebinski on 6/25/2017.
 * <p>
 * <p>
 * EntryItem POJO for setting and retrieving data for layouts
 */

public class EntryItem {
    private Date mDate;
    private int mGlucose;
    private int mCarbohydrates;
    private double mInsulin;

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
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
