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

        for (int i = 0; i < 200; i++) {
            EntryItem item = new EntryItem();
            item.setDate("Jun, 20 2017");
            item.setTime("12:00 pm");
            item.setGlucose(120);
            item.setCarbohydrates(45);
            item.setInsulin(4.5);
            data.add(item);
        }

        return data;
    }

}
