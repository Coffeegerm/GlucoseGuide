package io.github.coffeegerm.materiallogbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.model.EntryItem;

/**
 * Created by David Yarzebinski on 6/28/2017.
 * <p>
 * Adapter used to fill the Recycler View in the Graph Fragment
 */

public class GraphAdapter extends RecyclerView.Adapter<GraphAdapter.holder> {

    private List<EntryItem> mEntryItemList;
    private LayoutInflater inflater;

    public GraphAdapter(List<EntryItem> entryItemList, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.mEntryItemList = entryItemList;
    }

    @Override
    public holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.graph_list_item, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(holder holder, int position) {
        EntryItem item = mEntryItemList.get(position);
        holder.tvGraphDate.setText(item.getDate());
        holder.tvGraphTime.setText(item.getTime());
        holder.tvGraphBloodGlucose.setText(String.valueOf(item.getGlucose()));
    }

    @Override
    public int getItemCount() {
        return mEntryItemList.size();
    }

    class holder extends RecyclerView.ViewHolder {
        private TextView tvGraphDate;
        private TextView tvGraphTime;
        private TextView tvGraphBloodGlucose;

        public holder(View itemView) {
            super(itemView);
            tvGraphDate = (TextView) itemView.findViewById(R.id.graph_item_date_tv);
            tvGraphTime = (TextView) itemView.findViewById(R.id.graph_item_time_tv);
            tvGraphBloodGlucose = (TextView) itemView.findViewById(R.id.graph_item_glucose_level_tv);
        }
    }
}
