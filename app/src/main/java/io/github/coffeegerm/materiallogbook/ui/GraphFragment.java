package io.github.coffeegerm.materiallogbook.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.database.DummyData;

/**
 * Created by David Yarzebinski on 6/25/2017.
 * <p>
 * Graph View fragment that will show a graph of sugar levels to show
 * patterns of sugar
 */

public class GraphFragment extends Fragment {

    @BindView(R.id.line_chart)
    LineChart mLineChart;

    @BindView(R.id.graph_rec_view)
    RecyclerView mGraphRecView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View graphView = inflater.inflate(R.layout.fragment_graph, container, false);

        // TODO Create RecyclerView Adapter for graph list

        List mData = DummyData.getListData();

        List<Entry> entries = new ArrayList<Entry>();

        for (int i = 0; i < mData.size(); i++) {
            entries.add(new Entry());
            // Figure this shit out
        }

        return graphView;
    }
}
