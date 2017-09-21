package io.github.coffeegerm.materiallogbook.graph;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    public GraphAdapter(List<EntryItem> incomingEntryItemList, Context context) {
        this.inflater = LayoutInflater.from(context);
        this.entryItemList = incomingEntryItemList;
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
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        Calendar itemCalendar = Calendar.getInstance();
        itemCalendar.setTime(item.getDate());
        int itemDay = itemCalendar.get(Calendar.DAY_OF_MONTH);
        int yesterdayDay = yesterday.get(Calendar.DAY_OF_MONTH);
        int todayDay = today.get(Calendar.DAY_OF_MONTH);
        if (itemDay == todayDay) {
            holder.date.setText(R.string.today);
        } else if (itemDay == yesterdayDay) {
            holder.date.setText(R.string.yesterday);
        } else {
            holder.date.setText(dateFormat.format(item.getDate()));
        }
        holder.time.setText(timeFormat.format(item.getDate()));
        holder.bloodGlucose.setText(String.valueOf(item.getBloodGlucose()));
    }

    @Override
    public int getItemCount() {
        return entryItemList.size();
    }

    class holder extends RecyclerView.ViewHolder {
        @BindView(R.id.graph_item_header_date)
        TextView date;
        @BindView(R.id.graph_item_glucose_level_tv)
        TextView bloodGlucose;
        @BindView(R.id.graph_item_time_tv)
        TextView time;

        holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
