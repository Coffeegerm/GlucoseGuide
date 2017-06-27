package io.github.coffeegerm.materiallogbook.database;

import java.util.ArrayList;
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

        for (int i = 0; i < 12; i++) {
            EntryItem item = new EntryItem();
            item.setDate("June 20, 2016");
            item.setTime("12:00pm");
            item.setGlucose(98);
            item.setCarbohydrates(45);
            item.setInsulin(4.0);
            data.add(item);
        }

        return data;
    }

}
