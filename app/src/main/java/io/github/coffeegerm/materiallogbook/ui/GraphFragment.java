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

        // TODO Create RecyclerView Adapter for graph list

        // TODO Fix graph data chart
        List<EntryItem> mEntryItemList = DummyData.getListData();

        List<Entry> entries = new ArrayList<Entry>();

        for (int i = 0; i < mEntryItemList.size(); i++) {
            // X value = Date/Time
            float itemDate = mEntryItemList.get(i).getDate().getTime();
            // Y value = Blood glucose level
            int itemGlucoseLevel = mEntryItemList.get(i).getGlucose();
            // Set X and Y values in the entries list
            entries.add(new Entry(itemDate, itemGlucoseLevel));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Test"); // Adding entries to dataset
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);
        LineData lineData = new LineData(dataSet);
        mLineChart.setData(lineData);
        mLineChart.invalidate(); // Refreshes


        return graphView;
    }
}
