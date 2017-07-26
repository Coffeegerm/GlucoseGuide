package io.github.coffeegerm.materiallogbook.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
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
import io.github.coffeegerm.materiallogbook.adapter.ListAdapter;
import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.github.coffeegerm.materiallogbook.utils.fabBehavior;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

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
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpRecyclerView() {
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (SORT_ORDER == 0) {
            ListAdapter descendingList = new ListAdapter(getDescendingList(), getActivity());
            recView.setAdapter(descendingList);
        } else if (SORT_ORDER == 1) {
            ListAdapter ascendingList = new ListAdapter(getAscendingList(), getActivity());
            recView.setAdapter(ascendingList);
        }
    }

    private void setFab() {
        /*
        * Creates new fabBehavior for animation of fab onScroll
        * */
        FloatingActionButton.Behavior fabBehavior = new fabBehavior();
        CoordinatorLayout.LayoutParams fabLayout = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        fabLayout.setBehavior(fabBehavior);
        fab.setLayoutParams(fabLayout);

        /*
        * Starts NewEntryActivity, which allows the user
        * to define a new entry
        * */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.from_x_100, R.anim.to_x_zero).toBundle();
                startActivity(new Intent(getContext(), NewEntryActivity.class), bundle);
            }
        });
    }

    private List<EntryItem> getDescendingList() {
        RealmQuery<EntryItem> entryQuery = realm.where(EntryItem.class);
        RealmResults<EntryItem> entryItems = entryQuery.findAllSorted("mDate", Sort.DESCENDING);
        return new ArrayList<>(entryItems);
    }

    private List<EntryItem> getAscendingList() {
        RealmQuery<EntryItem> entryQuery = realm.where(EntryItem.class);
        RealmResults<EntryItem> entryItems = entryQuery.findAllSorted("mDate", Sort.ASCENDING);
        return new ArrayList<>(entryItems);
    }
}
