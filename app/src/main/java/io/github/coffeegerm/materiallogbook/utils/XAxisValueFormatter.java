package io.github.coffeegerm.materiallogbook.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by David Yarzebinski on 7/23/2017.
 * <p>
 * X Axis Value Formatter for Graph
 */

public class XAxisValueFormatter implements IAxisValueFormatter {

    private SimpleDateFormat sdf;

    public XAxisValueFormatter() {
        sdf = new SimpleDateFormat("MMM dd, hh:mm aa", Locale.US);
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return sdf.format(value);
    }
}
