package io.github.coffeegerm.materiallogbook.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.adapter.GraphAdapter;
import io.github.coffeegerm.materiallogbook.database.DummyData;
import io.github.coffeegerm.materiallogbook.model.EntryItem;

/**
 * Created by David Yarzebinski on 6/25/2017.
 * <p>
 * Graph View fragment that will show a graph of sugar levels to show
 * patterns of highs and lows
 */

public class GraphFragment extends Fragment {

    private static final String TAG = "GraphFragment";

    @BindView(R.id.line_chart)
    LineChart mLineChart;

    @BindView(R.id.graph_rec_view)
    RecyclerView mGraphRecView;

    GraphAdapter mGraphAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View graphView = inflater.inflate(R.layout.fragment_graph, container, false);
        ButterKnife.bind(this, graphView);

        mGraphRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGraphAdapter = new GraphAdapter(DummyData.getListData(), getActivity());
        mGraphRecView.setAdapter(mGraphAdapter);

        List<EntryItem> mEntryItemList = DummyData.getListData();

        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < mEntryItemList.size(); i++) {
            // X value = Date/Time
            float itemDate = mEntryItemList.get(i).getDate().getTime();
            // Y value = Blood glucose level
            int itemGlucoseLevel = mEntryItemList.get(i).getGlucose();
            // Set X and Y values in the entries list
            entries.add(new Entry(itemDate, itemGlucoseLevel));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Blood Sugar Levels"); // Adding entries to dataset
        int lineColor = R.color.colorPrimaryDark;
        dataSet.setColor(lineColor); // Sets line color of graph to colorPrimarDark
        dataSet.setValueTextColor(Color.BLACK); // Values on side will have text color of black
        LineData lineData = new LineData(dataSet); // Sets the data found in the database to the LineChart
        mLineChart.setData(lineData);
        Description description = new Description();
        description.setText(""); // Disables description below chart
        mLineChart.setDescription(description);
        mLineChart.setDragEnabled(true); // Enables the user to drag the chart left and right to see varying days and times of pattern
        mLineChart.getLegend().setEnabled(false); // Disables the legend at the bottom
        mLineChart.invalidate(); // Refreshes

        return graphView;
    }
}
