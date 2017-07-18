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
import io.realm.RealmQuery;
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
    LineChart mLineChart;

    @BindView(R.id.graph_rec_view)
    RecyclerView mGraphRecView;

    GraphAdapter mGraphAdapter;

    // TODO if glucose has 0 value causes crash
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
        List<EntryItem> entries = getSortedDataList();
        mGraphRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGraphAdapter = new GraphAdapter(entries, getActivity());
        mGraphRecView.setAdapter(mGraphAdapter);
    }

    private void setupGraph() {
        // Get sorted List from RealmResults
        List<EntryItem> listOfRealmEntries = getSortedDataList();

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

        if (entries.size() == 0) {
            Log.i(TAG, "setupGraph: No entries found");
            Toast.makeText(getContext(), "Enter entries to create a graph", Toast.LENGTH_SHORT).show();
        } else {
            LineDataSet dataSet = new LineDataSet(entries, "Blood Sugar Levels"); // Adding entries to dataset
            dataSet.setColor(lineColor); // Sets line color of graph to colorPrimaryDark
            dataSet.setValueTextColor(Color.BLACK); // Values on side will have text color of black
            LineData lineData = new LineData(dataSet); // Sets the data found in the database to the LineChart
            mLineChart.setData(lineData);
            Description description = new Description();
            description.setText(""); // Disables description below chart
            mLineChart.setDescription(description);
            mLineChart.setScaleEnabled(true);
            mLineChart.setDragEnabled(true); // Enables the user to drag the chart left and right to see varying days and times of pattern
            mLineChart.setDoubleTapToZoomEnabled(false); // Disables the user to double tap to zoom, rather useless feature in present day form factor
            mLineChart.getLegend().setEnabled(false); // Disables the legend at the bottom

            XAxis xAxis = mLineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Positions text of X Axis on bottom of Graph

            mLineChart.invalidate(); // Refreshes
        }
    }

    private List<EntryItem> getSortedDataList() {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<EntryItem> entryQuery = realm.where(EntryItem.class);
        RealmResults<EntryItem> entryItems = entryQuery.findAllSorted("mDate", Sort.DESCENDING);
        List<EntryItem> realmEntries = new ArrayList<>(entryItems);
        return realmEntries;
    }
}
