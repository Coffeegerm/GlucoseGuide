package io.github.coffeegerm.materiallogbook.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.ui.activity.MainActivity;
import io.realm.Realm;

/**
 * Created by dyarz on 8/15/2017.
 * <p>
 * Fragment for Statistics ViewPager to show
 * one month data for user.
 */

public class OneMonthStatisticsFragment extends Fragment {
    private static final String TAG = "OneMonthStatistics";
    @BindView(R.id.imgAvg)
    ImageView ivAvg;
    @BindView(R.id.imgUpArrow)
    ImageView ivUpArrow;
    @BindView(R.id.imgDownArrow)
    ImageView ivDownArrow;
    private Realm realm;

    public static OneMonthStatisticsFragment newInstance(int pageNumber, String pageTitle) {
        OneMonthStatisticsFragment oneMonthStatisticsFragment = new OneMonthStatisticsFragment();
        Bundle args = new Bundle();
        args.putInt("pageNumber", pageNumber);
        args.putString("pageTitle", pageTitle);
        oneMonthStatisticsFragment.setArguments(args);
        return oneMonthStatisticsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View oneMonth = inflater.inflate(R.layout.fragment_one_month_statistics, container, false);
        ButterKnife.bind(this, oneMonth);
        realm = Realm.getDefaultInstance();
        setImages();
        return oneMonth;
    }

    public Date getOneMonthAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        return calendar.getTime();
    }

    private void setImages() {
        if (MainActivity.sharedPreferences.getBoolean("pref_dark_mode", false)) {
            ivAvg.setImageResource(R.drawable.ic_average_dark);
            ivUpArrow.setImageResource(R.drawable.ic_up_arrow_dark);
            ivDownArrow.setImageResource(R.drawable.ic_down_arrow_dark);
        }
    }
}
