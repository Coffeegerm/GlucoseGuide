package io.github.coffeegerm.materiallogbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.model.EntryItem;

/**
 * Created by David Yarzebinski on 6/28/2017.
 * <p>
 * Adapter used to fill the Recycler View in the Graph Fragment
 */

public class GraphAdapter extends RecyclerView.Adapter<GraphAdapter.holder> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
    private List<EntryItem> entryItemList;
    private LayoutInflater inflater;
    private Context context;

    public GraphAdapter(List<EntryItem> incomingEntryItemList, Context context) {
        this.inflater = LayoutInflater.from(context);
        this.entryItemList = incomingEntryItemList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        // TODO if the date of the item is the same as the previous return that it is of type item_graph_list
        return position;
    }

    @Override
    public holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_graph_list, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(holder holder, int position) {
        EntryItem item = entryItemList.get(position);
        String formattedDate = dateFormat.format(item.getDate());
        String formattedTime = timeFormat.format(item.getDate());
        holder.tvGraphDate.setText(formattedDate);
        holder.tvGraphTime.setText(formattedTime);
        holder.tvGraphBloodGlucose.setText(String.valueOf(item.getBloodGlucose()));
    }

    @Override
    public int getItemCount() {
        return entryItemList.size();
    }

    class holder extends RecyclerView.ViewHolder {
        private TextView tvGraphDate, tvGraphBloodGlucose, tvGraphTime;

        holder(View itemView) {
            super(itemView);
            tvGraphDate = (TextView) itemView.findViewById(R.id.graph_item_date_tv);
            tvGraphTime = (TextView) itemView.findViewById(R.id.graph_item_time_tv);
            tvGraphBloodGlucose = (TextView) itemView.findViewById(R.id.graph_item_glucose_level_tv);
        }
    }
}
