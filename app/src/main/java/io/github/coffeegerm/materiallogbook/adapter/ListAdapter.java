package io.github.coffeegerm.materiallogbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.github.coffeegerm.materiallogbook.ui.activity.MainActivity;

/**
 * Created by David Yarzebinski on 6/25/2017.
 * <p>
 * Adapter used for listFragment
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.viewHolder> {

    private List<EntryItem> entryItems;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
    private LayoutInflater inflater;
    private Context context;

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
        EntryItem item = entryItems.get(position);
        String formattedDate = dateFormat.format(item.getDate());
        String formattedTime = timeFormat.format(item.getDate());
        holder.tvDate.setText(formattedDate);
        holder.tvTime.setText(formattedTime);

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
    }

    @Override
    public int getItemCount() {
        return entryItems.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate, tvTime, tvBloodGlucose, tvInsulin, tvCarbohydrates;
        private ImageView ivInsulin, ivCarbs, ivFinger;
        private View line1, line2;

        viewHolder(View itemView) {
            super(itemView);

            // Find item's views
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvBloodGlucose = (TextView) itemView.findViewById(R.id.tv_blood_glucose);
            tvInsulin = (TextView) itemView.findViewById(R.id.tv_insulin);
            tvCarbohydrates = (TextView) itemView.findViewById(R.id.tv_carbs);
            ivInsulin = (ImageView) itemView.findViewById(R.id.imgInsulins);
            ivCarbs = (ImageView) itemView.findViewById(R.id.imgCarbs);
            ivFinger = (ImageView) itemView.findViewById(R.id.imgFinger);
            line1 = itemView.findViewById(R.id.view);
            line2 = itemView.findViewById(R.id.view2);

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
