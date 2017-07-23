package io.github.coffeegerm.materiallogbook.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.adapter.GraphAdapter;
import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by David Yarzebinski on 6/25/2017.
 * <p>
 * Graph View fragment that will show a graph of sugar levels to show
 * patterns of highs and lows
 */

public class GraphFragment extends Fragment {

    private static final String TAG = "GraphFragment";

    int lineColor = R.color.colorPrimaryDark;

    @BindView(R.id.line_chart)
    LineChart lineChart;

    @BindView(R.id.graph_rec_view)
    RecyclerView recyclerView;

    GraphAdapter graphAdapter;

    // TODO Date formatting for x axis

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View graphView = inflater.inflate(R.layout.fragment_graph, container, false);
        ButterKnife.bind(this, graphView);
        setupRecView();
        setupGraph();
        return graphView;
    }

    private void setupRecView() {
        // Get sorted List from RealmResults
        List<EntryItem> entries = getDescendingDataList();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        graphAdapter = new GraphAdapter(entries, getActivity());
        recyclerView.setAdapter(graphAdapter);
    }

    private void setupGraph() {
        // Get sorted List from RealmResults
        List<EntryItem> listOfRealmEntries = getAscendingDataList();

        // List of Entries for Graph
        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < listOfRealmEntries.size(); i++) {
            // X value = Date/Time
            float itemDate = listOfRealmEntries.get(i).getDate().getTime();
            // Y value = Blood glucose level
            float itemGlucoseLevel = listOfRealmEntries.get(i).getGlucose();
            // Set X and Y values in the entries list
            entries.add(new Entry(itemDate, itemGlucoseLevel));
        }

        Log.i(TAG, "setupGraph: " + entries.toString());

        if (entries.size() == 0) {
            Log.i(TAG, "setupGraph: No entries found");
            Toast.makeText(getContext(), "Enter entries with glucose levels higher than 0 to create a graph", Toast.LENGTH_SHORT).show();
        } else {
            LineDataSet dataSet = new LineDataSet(entries, "Blood Sugar Levels"); // Adding entries to dataset
            dataSet.setValueTextColor(Color.BLACK); // Values on side will have text color of black
            LineData lineData = new LineData(dataSet); // Sets the data found in the database to the LineChart
            lineChart.setData(lineData);
            Description description = new Description();
            description.setText(""); // Disables description below chart
            lineChart.setDescription(description);
            lineChart.setScaleMinima(10f, 1f);
            lineChart.setScaleEnabled(true);
            lineChart.setDragEnabled(true); // Enables the user to drag the chart left and right to see varying days and times of pattern
            lineChart.setPinchZoom(false); // Disables the ability to pinch the chart to zoom in
            lineChart.setDoubleTapToZoomEnabled(false); // Disables the user to double tap to zoom, rather useless feature in present day form factor
            lineChart.getLegend().setEnabled(false); // Disables the legend at the bottom

            YAxis yAxisRight = lineChart.getAxisRight();
            yAxisRight.setEnabled(false); // Disables Y values on the right of chart

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Positions text of X Axis on bottom of Graph

            lineChart.invalidate(); // Refreshes
        }
    }

    // Method to get List of Realm Entries in timeline from newest to oldest
    private List<EntryItem> getDescendingDataList() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<EntryItem> entryItems = realm.where(EntryItem.class)
                .greaterThan("mGlucose", 0)
                .findAllSorted("mDate", Sort.DESCENDING); // Finds all entries with Blood Glucose higher than zero order from newest to oldest
        List<EntryItem> realmEntries = new ArrayList<>(entryItems);
        return realmEntries;
    }

    // Method to get List of Realm Entries for LineChart so that they are from the oldest to the newest
    private List<EntryItem> getAscendingDataList() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<EntryItem> entryItems = realm.where(EntryItem.class)
                .greaterThan("mGlucose", 0)
                .findAllSorted("mDate", Sort.ASCENDING); // Finds all entries with Blood Glucose higher than zero order from oldest to newest
        List<EntryItem> realmEntries = new ArrayList<>(entryItems);
        return realmEntries;
    }
}
