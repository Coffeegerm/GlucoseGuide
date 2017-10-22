package io.github.coffeegerm.materiallogbook.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.activity.MainActivity;
import io.github.coffeegerm.materiallogbook.activity.NewEntryActivity;
import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static io.github.coffeegerm.materiallogbook.utils.Constants.PREF_DARK_MODE;

/**
 * Created by David Yarzebinski on 6/7/17.
 * <p>
 * Fragment for list view of Entries
 */

public class ListFragment extends Fragment {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.rec_view)
    RecyclerView recView;
    private int SORT_ORDER = 0; // 0 = Descending, 1 = Ascending
    private Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View listView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, listView);
        setHasOptionsMenu(true);
        realm = Realm.getDefaultInstance();
        setUpRecyclerView();
        setFab();
        if (MainActivity.sharedPreferences.getBoolean(PREF_DARK_MODE, false))
            fab.setImageResource(R.drawable.add_dark);
        return listView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_sort_from_newest:
                SORT_ORDER = 0;
                setUpRecyclerView();
                return true;
            case R.id.list_sort_from_oldest:
                SORT_ORDER = 1;
                setUpRecyclerView();
                return true;
            // breakfast only
            case R.id.list_status_breakfast:
                SORT_ORDER = 2;
                setUpRecyclerView();
                return true;
            // lunch only
            case R.id.list_status_lunch:
                SORT_ORDER = 3;
                setUpRecyclerView();
                return true;
            // dinner only
            case R.id.list_status_dinner:
                SORT_ORDER = 4;
                setUpRecyclerView();
                return true;
            // exercise only
            case R.id.list_status_exercise:
                SORT_ORDER = 5;
                setUpRecyclerView();
                return true;
            // sick only
            case R.id.list_status_sick:
                SORT_ORDER = 6;
                setUpRecyclerView();
                return true;
            // sweets only
            case R.id.list_status_sweets:
                SORT_ORDER = 7;
                setUpRecyclerView();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpRecyclerView() {
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
        switch (SORT_ORDER) {
            case 0: // newest to older
                recView.setAdapter(new ListAdapter(getDescendingList(), getActivity()));
                break;
            case 1: // oldest to newest
                recView.setAdapter(new ListAdapter(getAscendingList(), getActivity()));
                break;
            case 2: // breakfast only, status of item = 1
                RealmResults<EntryItem> breakfastEntries = realm.where(EntryItem.class).equalTo("status", 1).findAllSorted("date", Sort.DESCENDING);
                recView.setAdapter(new ListAdapter(breakfastEntries, getActivity()));
                break;
            case 3: // lunch only, status of item = 2
                RealmResults<EntryItem> lunchEntries = realm.where(EntryItem.class).equalTo("status", 2).findAllSorted("date", Sort.DESCENDING);
                recView.setAdapter(new ListAdapter(lunchEntries, getActivity()));
                break;
            case 4: // dinner only, status of item = 3
                RealmResults<EntryItem> dinnerEntries = realm.where(EntryItem.class).equalTo("status", 3).findAllSorted("date", Sort.DESCENDING);
                recView.setAdapter(new ListAdapter(dinnerEntries, getActivity()));
                break;
            case 5: // exercise only, status of item = 5
                RealmResults<EntryItem> exerciseEntries = realm.where(EntryItem.class).equalTo("status", 5).findAllSorted("date", Sort.DESCENDING);
                recView.setAdapter(new ListAdapter(exerciseEntries, getActivity()));
                break;
            case 6: // sick only, status of item = 4
                RealmResults<EntryItem> sickEntries = realm.where(EntryItem.class).equalTo("status", 4).findAllSorted("date", Sort.DESCENDING);
                recView.setAdapter(new ListAdapter(sickEntries, getActivity()));
                break;
            case 7: // sweets only, status of item = 6
                RealmResults<EntryItem> sweetEntries = realm.where(EntryItem.class).equalTo("status", 6).findAllSorted("date", Sort.DESCENDING);
                recView.setAdapter(new ListAdapter(sweetEntries, getActivity()));
                break;
        }

        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 & fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });
    }

    private List<EntryItem> getDescendingList() {
        RealmResults<EntryItem> entryItems = realm.where(EntryItem.class).findAllSorted("date", Sort.DESCENDING);
        return new ArrayList<>(entryItems);
    }

    private List<EntryItem> getAscendingList() {
        RealmResults<EntryItem> entryItems = realm.where(EntryItem.class).findAllSorted("date", Sort.ASCENDING);
        return new ArrayList<>(entryItems);
    }

    private void setFab() {
        /*
        * Starts NewEntryActivity, which allows the user
        * to define a new entry
        * */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), NewEntryActivity.class));
            }
        });
    }

    @Override
    public void onDestroyView() {
        realm.close();
        super.onDestroyView();
    }
}
