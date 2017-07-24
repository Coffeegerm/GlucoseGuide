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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.adapter.GraphAdapter;
import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.github.coffeegerm.materiallogbook.utils.XAxisValueFormatter;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        graphAdapter = new GraphAdapter(getDescendingDataList(), getActivity());
        recyclerView.setAdapter(graphAdapter);
    }

    private void setupGraph() {
        // Get sorted List from RealmResults
        List<EntryItem> listOfRealmEntries = getAscendingDataList();

        // List of Entries for Graph
        List<Entry> graphEntryPoints = new ArrayList<>();

        for (int positionInList = 0; positionInList < listOfRealmEntries.size(); positionInList++) {
            // X value = Date/Time
            float itemDate = listOfRealmEntries.get(positionInList).getDate().getTime();
            // Y value = Blood glucose level
            float itemGlucoseLevel = listOfRealmEntries.get(positionInList).getGlucose();
            // Set X and Y values in the graphEntryPoints list
            graphEntryPoints.add(new Entry(itemDate, itemGlucoseLevel));
        }

        Log.i(TAG, "setupGraph: " + graphEntryPoints.toString());

        if (graphEntryPoints.size() == 0) {
            Log.i(TAG, "setupGraph: No graphEntryPoints found");
            Toast.makeText(getContext(), "Enter graphEntryPoints with glucose levels higher than 0 to create a graph", Toast.LENGTH_SHORT).show();
        } else {
            LineDataSet dataSet = new LineDataSet(graphEntryPoints, "Blood Sugar Levels"); // Adding graphEntryPoints to dataset
            dataSet.setValueTextColor(Color.BLACK); // Values on side will have text color of black
            LineData lineData = new LineData(dataSet); // Sets the data found in the database to the LineChart
            lineChart.setData(lineData);
            lineChart.getDescription().setEnabled(false); // Disables description below chart
            lineChart.setDrawGridBackground(false);
            lineChart.setScaleMinima(10f, 1f);
            lineChart.setScaleEnabled(true);
            lineChart.setDragEnabled(true); // Enables the user to drag the chart left and right to see varying days and times of pattern
            lineChart.setPinchZoom(false); // Disables the ability to pinch the chart to zoom in
            lineChart.setDoubleTapToZoomEnabled(false); // Disables the user to double tap to zoom, rather useless feature in present day form factor
            lineChart.getLegend().setEnabled(false); // Disables the legend at the bottom

            YAxis yAxisRight = lineChart.getAxisRight();
            yAxisRight.setEnabled(false); // Disables Y values on the right of chart

            XAxis xAxis = lineChart.getXAxis();
            IAxisValueFormatter xAxisFormatter = new XAxisValueFormatter();
            xAxis.setValueFormatter(xAxisFormatter);
            xAxis.setGranularity(1f); // only intervals of one day
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
        return new ArrayList<>(entryItems);
    }

    // Method to get List of Realm Entries for LineChart so that they are from the oldest to the newest
    private List<EntryItem> getAscendingDataList() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<EntryItem> entryItems = realm.where(EntryItem.class)
                .greaterThan("mGlucose", 0)
                .findAllSorted("mDate", Sort.ASCENDING); // Finds all entries with Blood Glucose higher than zero order from oldest to newest
        return new ArrayList<>(entryItems);
    }
}
