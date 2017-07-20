package io.github.coffeegerm.materiallogbook.adapter;

import android.content.Context;
import android.graphics.Typeface;
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
 * Created by David Yarzebinski on 6/25/2017.
 * <p>
 * Adapter used for listFragment
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.holder> {

    // Fonts used in itemView
    private Typeface avenirRegular;
    private Typeface avenirDemiBold;
    private Typeface avenirMedium;
    private List<EntryItem> mEntryItemList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
    private LayoutInflater inflater;

    public ListAdapter(List<EntryItem> entryItemList, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.mEntryItemList = entryItemList;
        avenirRegular = Typeface.createFromAsset(c.getAssets(), "fonts/AvenirNext-Regular.otf");
        avenirDemiBold = Typeface.createFromAsset(c.getAssets(), "fonts/AvenirNext-DemiBold.otf");
        avenirMedium = Typeface.createFromAsset(c.getAssets(), "fonts/AvenirNext-Medium.otf");
    }

    @Override
    public holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_card_list, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(holder holder, int position) {
        EntryItem item = mEntryItemList.get(position);
        String formattedDate = dateFormat.format(item.getDate());
        String formattedTime = timeFormat.format(item.getDate());
        holder.tvDate.setText(formattedDate);
        holder.tvTime.setText(formattedTime);

        if (String.valueOf(item.getGlucose()).equals("0")) {
            holder.tvBloodGlucose.setText(R.string.dash);
        } else {
            holder.tvBloodGlucose.setText(String.valueOf(item.getGlucose()));
        }

        if (String.valueOf(item.getCarbohydrates()).equals("0")) {
            holder.tvCarbohydrates.setText(R.string.dash);
        } else {
            holder.tvCarbohydrates.setText(String.valueOf(item.getCarbohydrates()));
        }

        if (String.valueOf(item.getInsulin()).equals("0.0")) {
            holder.tvInsulin.setText(R.string.dash);
        } else {
            holder.tvInsulin.setText(String.valueOf(item.getInsulin()));
        }
    }

    @Override
    public int getItemCount() {
        return mEntryItemList.size();
    }

    class holder extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private TextView tvTime;
        private TextView tvBloodGlucose;
        private TextView tvInsulin;
        private TextView tvCarbohydrates;
        private TextView bloodGlucoseLabel;
        private TextView carbohydratesLabel;
        private TextView insulinLabel;
        private TextView bloodGlucoseMeasurementLabel;
        private TextView carbsMeasurementLabel;
        private TextView insulinMeasurementLabel;

        holder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvBloodGlucose = (TextView) itemView.findViewById(R.id.tv_blood_glucose);
            tvInsulin = (TextView) itemView.findViewById(R.id.tv_insulin);
            tvCarbohydrates = (TextView) itemView.findViewById(R.id.tv_carbs);
            bloodGlucoseLabel = (TextView) itemView.findViewById(R.id.blood_glucose_label);
            carbohydratesLabel = (TextView) itemView.findViewById(R.id.carbohydrates_label);
            insulinLabel = (TextView) itemView.findViewById(R.id.insulin_label);
            bloodGlucoseMeasurementLabel = (TextView) itemView.findViewById(R.id.blood_glucose_measurement_label);
            carbsMeasurementLabel = (TextView) itemView.findViewById(R.id.carbs_measurement_label);
            insulinMeasurementLabel = (TextView) itemView.findViewById(R.id.insulin_measurement_label);
            tvDate.setTypeface(avenirRegular);
            tvTime.setTypeface(avenirRegular);
            tvBloodGlucose.setTypeface(avenirDemiBold);
            tvInsulin.setTypeface(avenirDemiBold);
            tvCarbohydrates.setTypeface(avenirDemiBold);
            bloodGlucoseLabel.setTypeface(avenirMedium);
            carbohydratesLabel.setTypeface(avenirMedium);
            insulinLabel.setTypeface(avenirMedium);
            insulinMeasurementLabel.setTypeface(avenirDemiBold);
            bloodGlucoseMeasurementLabel.setTypeface(avenirDemiBold);
            carbsMeasurementLabel.setTypeface(avenirDemiBold);
        }
    }
}
