package io.github.coffeegerm.materiallogbook.list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.github.coffeegerm.materiallogbook.activity.EditEntryActivity;
import io.github.coffeegerm.materiallogbook.activity.MainActivity;
import io.github.coffeegerm.materiallogbook.utils.Constants;

import static io.github.coffeegerm.materiallogbook.utils.Constants.PREF_DARK_MODE;

/**
 * Created by David Yarzebinski on 6/25/2017.
 * <p>
 * Adapter used for filling RecyclerView within ListFragment
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.viewHolder> {

    private static final String TAG = "ListAdapter";
    private static int shortClickHintCount = 0;
    private List<EntryItem> entryItems;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private SimpleDateFormat twelveHourTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
    private SimpleDateFormat twentyFourHourTimeFormat = new SimpleDateFormat("HH:mm", Locale.US);
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

    class viewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date)
        TextView date;
        @BindView(R.id.tv_time)
        TextView time;
        @BindView(R.id.tv_blood_glucose)
        TextView bloodGlucose;
        @BindView(R.id.tv_insulin)
        TextView insulin;
        @BindView(R.id.tv_carbs)
        TextView carbohydrates;
        @BindView(R.id.imgInsulins)
        ImageView ivInsulin;
        @BindView(R.id.imgCarbs)
        ImageView ivCarbs;
        @BindView(R.id.imgFinger)
        ImageView ivFinger;
        @BindView(R.id.line1)
        View line1;
        @BindView(R.id.line2)
        View line2;
        private View view;

        viewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, itemView);

            if (MainActivity.sharedPreferences.getBoolean(PREF_DARK_MODE, false)) {
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

    @Override
    public void onBindViewHolder(viewHolder holder, final int position) {
        item = entryItems.get(position);

        // Check if today or yesterday and set date accordingly
        Calendar today = Calendar.getInstance();
        int todayDay = today.get(Calendar.DAY_OF_MONTH);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        int yesterdayDay = yesterday.get(Calendar.DAY_OF_MONTH);
        Calendar itemCalendar = Calendar.getInstance();
        itemCalendar.setTime(item.getDate());
        int itemDay = itemCalendar.get(Calendar.DAY_OF_MONTH);
        if (itemDay == todayDay) holder.date.setText(R.string.today);
        else if (itemDay == yesterdayDay) holder.date.setText(R.string.yesterday);
        else {
            holder.date.setText(dateFormat.format(item.getDate()));
        }

        // Set time based on user preference
        if (MainActivity.sharedPreferences.getBoolean(Constants.MILITARY_TIME, false)) {
            holder.time.setText(twentyFourHourTimeFormat.format(item.getDate()));
        } else {
            holder.time.setText(twelveHourTimeFormat.format(item.getDate()));
        }

        /*
        * Handle item clicks on List Fragment
        * Long click will start EditEntryActivity
        * */
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shortClickHintCount <= 5) {
                    Toast.makeText(context, R.string.list_item_short_click, Toast.LENGTH_SHORT).show();
                    shortClickHintCount++;
                }
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent editEntryActivity = new Intent(context, EditEntryActivity.class);
                EntryItem selectedItem = entryItems.get(position);
                editEntryActivity.putExtra(EditEntryActivity.ITEM_ID, selectedItem.getId());
                context.startActivity(editEntryActivity);
                return true;
            }
        });

        if (String.valueOf(item.getBloodGlucose()).equals("0"))
            holder.bloodGlucose.setText(R.string.dash);
        else
            holder.bloodGlucose.setText(String.valueOf(item.getBloodGlucose()));

        if (String.valueOf(item.getCarbohydrates()).equals("0"))
            holder.carbohydrates.setText(R.string.dash);
        else
            holder.carbohydrates.setText(String.valueOf(item.getCarbohydrates()));

        if (String.valueOf(item.getInsulin()).equals("0.0"))
            holder.insulin.setText(R.string.dash);
        else
            holder.insulin.setText(String.valueOf(item.getInsulin()));
    }

    @Override
    public int getItemCount() {
        return entryItems.size();
    }
}
