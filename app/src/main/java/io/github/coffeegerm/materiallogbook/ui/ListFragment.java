package io.github.coffeegerm.materiallogbook.ui;

import android.app.ActivityOptions;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.adapter.EntryAdapter;
import io.github.coffeegerm.materiallogbook.database.DummyData;

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

    EntryAdapter mEntryAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View listView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, listView);
        setHasOptionsMenu(true);

        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEntryAdapter = new EntryAdapter(DummyData.getListData(), getActivity());
        recView.setAdapter(mEntryAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.from_x_100, R.anim.to_x_zero).toBundle();
                startActivity(new Intent(getContext(), NewEntryActivity.class), bundle);
            }
        });

        return listView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Create menu option to delete database entries
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
            // TODO Create AlertDialog to confirm deleting of entries
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
