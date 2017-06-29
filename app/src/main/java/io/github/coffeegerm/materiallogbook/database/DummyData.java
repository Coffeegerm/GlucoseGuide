package io.github.coffeegerm.materiallogbook.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.coffeegerm.materiallogbook.model.EntryItem;

/**
 * Created by David Yarzebinski on 6/25/2017.
 * <p>
 * Mock data for testing everything
 */

public class DummyData {

    public static List<EntryItem> getListData() {
        List<EntryItem> data = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            EntryItem item = new EntryItem();
            Date date = new Date();
            item.setDate(date);
            item.setGlucose(120);
            item.setCarbohydrates(45);
            item.setInsulin(4.5);
            data.add(item);
        }

        for (int i = 0; i < 2; i++) {
            EntryItem item = new EntryItem();
            Date date = new Date();
            item.setDate(date);
            item.setGlucose(98);
            item.setCarbohydrates(45);
            item.setInsulin(4.5);
            data.add(item);
        }

        for (int i = 0; i < 2; i++) {
            EntryItem item = new EntryItem();
            Date date = new Date();
            item.setDate(date);
            item.setGlucose(76);
            item.setCarbohydrates(45);
            item.setInsulin(4.5);
            data.add(item);
        }

        for (int i = 0; i < 2; i++) {
            EntryItem item = new EntryItem();
            Date date = new Date();
            item.setDate(date);
            item.setGlucose(50);
            item.setCarbohydrates(45);
            item.setInsulin(4.5);
            data.add(item);
        }

        for (int i = 0; i < 2; i++) {
            EntryItem item = new EntryItem();
            Date date = new Date();
            item.setDate(date);
            item.setGlucose(178);
            item.setCarbohydrates(45);
            item.setInsulin(4.5);
            data.add(item);
        }

        for (int i = 0; i < 2; i++) {
            EntryItem item = new EntryItem();
            Date date = new Date();
            item.setDate(date);
            item.setGlucose(130);
            item.setCarbohydrates(45);
            item.setInsulin(4.5);
            data.add(item);
        }

        return data;
    }

}
