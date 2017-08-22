package io.github.coffeegerm.materiallogbook.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.github.coffeegerm.materiallogbook.ui.activity.EditEntryActivity;
import io.github.coffeegerm.materiallogbook.ui.activity.MainActivity;

/**
 * Created by David Yarzebinski on 6/25/2017.
 * <p>
 * Adapter used for filling RecyclerView within ListFragment
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.viewHolder> {

    private static final String TAG = "ListAdapter";
    private static int shortClickHintCount = 0;
    private static String itemId = "itemId";
    private List<EntryItem> entryItems;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
    private LayoutInflater inflater;
    private Context context;
    private EntryItem item;

    public ListAdapter(List<EntryItem> entryItemList, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.entryItems = entryItemList;
        this.context = c;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_card_list, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        item = entryItems.get(position);
        String formattedDate = dateFormat.format(item.getDate());
        String formattedTime = timeFormat.format(item.getDate());
        holder.tvDate.setText(formattedDate);
        holder.tvTime.setText(formattedTime);
        /*
        * Handle item clicks on List Fragment
        * Long click will start EditEntryActivity
        * */
        holder.view.setOnClickListener(view -> {
            if (shortClickHintCount <= 5) {
                Toast.makeText(context, R.string.list_item_short_click, Toast.LENGTH_SHORT).show();
                shortClickHintCount++;
            }
        });

        holder.view.setOnLongClickListener(view -> {
            Intent editEntryActivity = new Intent(context, EditEntryActivity.class);
            editEntryActivity.putExtra(itemId, item.getId());
            context.startActivity(editEntryActivity);
            return true;
        });

        if (String.valueOf(item.getBloodGlucose()).equals("0"))
            holder.tvBloodGlucose.setText(R.string.dash);
        else
            holder.tvBloodGlucose.setText(String.valueOf(item.getBloodGlucose()));

        if (String.valueOf(item.getCarbohydrates()).equals("0"))
            holder.tvCarbohydrates.setText(R.string.dash);
        else
            holder.tvCarbohydrates.setText(String.valueOf(item.getCarbohydrates()));

        if (String.valueOf(item.getInsulin()).equals("0.0"))
            holder.tvInsulin.setText(R.string.dash);
        else
            holder.tvInsulin.setText(String.valueOf(item.getInsulin()));

        String id = item.getId();
        Log.i(TAG, "item id:" + id);
    }

    @Override
    public int getItemCount() {
        return entryItems.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        public View view;
        private TextView tvDate, tvTime, tvBloodGlucose, tvInsulin, tvCarbohydrates;
        private ImageView ivInsulin, ivCarbs, ivFinger;
        private View line1, line2;

        viewHolder(View itemView) {
            super(itemView);
            this.view = itemView;

            // Find item's views
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvBloodGlucose = itemView.findViewById(R.id.tv_blood_glucose);
            tvInsulin = itemView.findViewById(R.id.tv_insulin);
            tvCarbohydrates = itemView.findViewById(R.id.tv_carbs);
            ivInsulin = itemView.findViewById(R.id.imgInsulins);
            ivCarbs = itemView.findViewById(R.id.imgCarbs);
            ivFinger = itemView.findViewById(R.id.imgFinger);
            line1 = itemView.findViewById(R.id.line1);
            line2 = itemView.findViewById(R.id.line2);

            if (MainActivity.sharedPreferences.getBoolean("pref_dark_mode", false)) {
                ivFinger.setImageResource(R.drawable.ic_finger_dark);
                ivCarbs.setImageResource(R.drawable.ic_food_dark);
                ivInsulin.setImageResource(R.drawable.ic_syringe_dark);
                int white = context.getResources().getColor(R.color.white);
                line1.setBackgroundColor(white);
                line2.setBackgroundColor(white);
            } else {
                ivFinger.setImageResource(R.drawable.ic_finger);
                ivCarbs.setImageResource(R.drawable.ic_food);
                ivInsulin.setImageResource(R.drawable.ic_syringe);
            }
        }
    }
}
