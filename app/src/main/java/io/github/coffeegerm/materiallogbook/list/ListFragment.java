package io.github.coffeegerm.materiallogbook.list;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.github.coffeegerm.materiallogbook.ui.activity.NewEntryActivity;
import io.realm.Realm;
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
    private static boolean hasShownAnimation = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View listView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, listView);
        setHasOptionsMenu(true);
        realm = Realm.getDefaultInstance();
        setUpRecyclerView();
        setFab();
        if (!hasShownAnimation) fabAnimate();
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
        if (SORT_ORDER == 0)            // descending list
            recView.setAdapter(new ListAdapter(getDescendingList(), getActivity()));
        else if (SORT_ORDER == 1) {     // ascending list
            recView.setAdapter(new ListAdapter(getAscendingList(), getActivity()));
        }
    }

    private void setFab() {
        /*
        * Creates new fabBehavior for animation of fab onScroll
        * */
        FloatingActionButton.Behavior fabBehavior = new FabBehavior();
        CoordinatorLayout.LayoutParams fabLayout = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        fabLayout.setBehavior(fabBehavior);
        fab.setLayoutParams(fabLayout);

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

    private void fabAnimate() {
        Animation fab_wiggle = AnimationUtils.loadAnimation(getContext(), R.anim.fab_wiggle);
        fab_wiggle.setRepeatCount(5);
        fab.startAnimation(fab_wiggle);
        hasShownAnimation = true;
    }

    private List<EntryItem> getDescendingList() {
        RealmResults<EntryItem> entryItems = realm.where(EntryItem.class).findAllSorted("date", Sort.DESCENDING);
        return new ArrayList<>(entryItems);
    }

    private List<EntryItem> getAscendingList() {
        RealmResults<EntryItem> entryItems = realm.where(EntryItem.class).findAllSorted("date", Sort.ASCENDING);
        return new ArrayList<>(entryItems);
    }

    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
