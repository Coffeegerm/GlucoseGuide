package io.github.coffeegerm.materiallogbook.ui;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

    private Realm mRealm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View listView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, listView);
        setHasOptionsMenu(true);
        mRealm = Realm.getDefaultInstance();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_entries) {
            final AlertDialog.Builder builder;

            // Sets theme based on VERSION_CODE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(getContext());
            }

            builder.setTitle("Delete all entries")
                    .setMessage("Are you sure you want to delete all entries?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            mRealm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(EntryItem.class);
                                    setUpRecyclerView();
                                }
                            });
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            dialog.dismiss();
                        }
                    })
                    .setIcon(R.drawable.ic_trash)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpRecyclerView() {
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<EntryItem> entries = getSortedDataList();
        ListAdapter mListAdapter = new ListAdapter(entries, getActivity());
        recView.setAdapter(mListAdapter);
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
        * Starts NewEntryActivity, which allows the user to define
        * a new entry
        * */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.from_x_100, R.anim.to_x_zero).toBundle();
                startActivity(new Intent(getContext(), NewEntryActivity.class), bundle);
            }
        });
    }

    private List<EntryItem> getSortedDataList() {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<EntryItem> entryQuery = realm.where(EntryItem.class);
        RealmResults<EntryItem> entryItems = entryQuery.findAllSorted("mDate", Sort.DESCENDING);
        List<EntryItem> realmEntries = new ArrayList<>(entryItems);
        return realmEntries;
    }
}
