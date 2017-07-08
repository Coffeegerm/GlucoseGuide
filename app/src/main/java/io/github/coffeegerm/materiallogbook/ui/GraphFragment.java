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
import io.github.coffeegerm.materiallogbook.utils.Utilities;
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

    @BindView(R.id.line_chart)
    LineChart mLineChart;

    @BindView(R.id.graph_rec_view)
    RecyclerView mGraphRecView;

    GraphAdapter mGraphAdapter;
    private Realm mRealm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View graphView = inflater.inflate(R.layout.fragment_graph, container, false);
        ButterKnife.bind(this, graphView);
        mRealm = Realm.getDefaultInstance();
        setupRecView();
        setupGraph();

        return graphView;
    }

    private void setupRecView() {
        RealmQuery<EntryItem> entryQuery = mRealm.where(EntryItem.class);
        RealmResults<EntryItem> entryItems = entryQuery.findAllSorted("mDate", Sort.DESCENDING);
        List<EntryItem> entries = new ArrayList<>(entryItems);

        mGraphRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGraphAdapter = new GraphAdapter(entries, getActivity());
        mGraphRecView.setAdapter(mGraphAdapter);
    }

    private void setupGraph() {
        List<EntryItem> realmEntries = Utilities.getSortedRealmList();

        // List of Entries for Graph
        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < realmEntries.size(); i++) {
            // Y value = Date/Time
            float itemDate = realmEntries.get(i).getDate().getTime();
            // X value = Blood glucose level
            int itemGlucoseLevel = realmEntries.get(i).getGlucose();
            // Set X and Y values in the entries list
            entries.add(new Entry(itemDate, itemGlucoseLevel));
        }

        if (entries.size() == 0) {
            Log.i(TAG, "setupGraph: No entries found");
            Toast.makeText(getContext(), "Enter entries to create a graph", Toast.LENGTH_SHORT).show();
        } else {
            LineDataSet dataSet = new LineDataSet(entries, "Blood Sugar Levels"); // Adding entries to dataset
            int lineColor = R.color.colorPrimaryDark;
            dataSet.setColor(lineColor); // Sets line color of graph to colorPrimaryDark
            dataSet.setValueTextColor(Color.BLACK); // Values on side will have text color of black
            LineData lineData = new LineData(dataSet); // Sets the data found in the database to the LineChart
            mLineChart.setData(lineData);
            Description description = new Description();
            description.setText("Shows sugar over time"); // Disables description below chart
            mLineChart.setDescription(description);
            mLineChart.setDragEnabled(true); // Enables the user to drag the chart left and right to see varying days and times of pattern
            mLineChart.getLegend().setEnabled(false); // Disables the legend at the bottom
            mLineChart.invalidate(); // Refreshes
        }
    }
}
