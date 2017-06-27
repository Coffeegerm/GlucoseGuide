package io.github.coffeegerm.materiallogbook.model;

/**
 * Created by David Yarzebinski on 6/25/2017.
 * <p>
 * EntryItem POJO for setting and retrieving data for layouts
 */

public class EntryItem {
    private String mDate;
    private String mTime;
    private int mGlucose;
    private int mCarbohydrates;
    private double mInsulin;

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
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
