/*
 * Copyright 2017 Coffee and Cream Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.coffeegerm.materiallogbook.ui.list;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.MaterialLogbook;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.data.model.EntryItem;
import io.github.coffeegerm.materiallogbook.ui.entry.NewEntryActivity;
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
  
  @Inject
  public SharedPreferences sharedPreferences;
  
  @BindView(R.id.fab)
  FloatingActionButton fab;
  @BindView(R.id.rec_view)
  RecyclerView recView;
  @BindView(R.id.empty_item_list)
  CardView emptyRecyclerViewExample;
  private int SORT_ORDER = 0; // 0 = Descending, 1 = Ascending
  private Realm realm;
  private ListAdapter listAdapter;
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    final View listView = inflater.inflate(R.layout.fragment_list, container, false);
    ButterKnife.bind(this, listView);
    MaterialLogbook.syringe.inject(this);
    setHasOptionsMenu(true);
    realm = Realm.getDefaultInstance();
    listAdapter = new ListAdapter(getContext());
    recView.setAdapter(listAdapter);
    if (realm.where(EntryItem.class).findAll().size() == 0) {
      recView.setVisibility(View.GONE);
      emptyRecyclerViewExample.setVisibility(View.VISIBLE);
    } else {
      recView.setVisibility(View.VISIBLE);
      emptyRecyclerViewExample.setVisibility(View.GONE);
      setUpRecyclerView();
    }
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
        List<EntryItem> descendingList = realm.where(EntryItem.class).sort("date", Sort.DESCENDING).findAll();
        listAdapter.setListItems(descendingList);
        listAdapter.notifyDataSetChanged();
        break;
      case 1: // oldest to newest
        List<EntryItem> ascendingList = realm.where(EntryItem.class).sort("date", Sort.ASCENDING).findAll();
        listAdapter.setListItems(ascendingList);
        listAdapter.notifyDataSetChanged();
        break;
      case 2: // breakfast only, status of item = 1
        RealmResults<EntryItem> breakfastEntries = realm.where(EntryItem.class).equalTo("status", 1).sort("date", Sort.DESCENDING).findAll();
        listAdapter.setListItems(breakfastEntries);
        listAdapter.notifyDataSetChanged();
        break;
      case 3: // lunch only, status of item = 2
        RealmResults<EntryItem> lunchEntries = realm.where(EntryItem.class).equalTo("status", 2).sort("date", Sort.DESCENDING).findAll();
        listAdapter.setListItems(lunchEntries);
        listAdapter.notifyDataSetChanged();
        break;
      case 4: // dinner only, status of item = 3
        RealmResults<EntryItem> dinnerEntries = realm.where(EntryItem.class).equalTo("status", 3).sort("date", Sort.DESCENDING).findAll();
        listAdapter.setListItems(dinnerEntries);
        listAdapter.notifyDataSetChanged();
        break;
      case 5: // exercise only, status of item = 5
        RealmResults<EntryItem> exerciseEntries = realm.where(EntryItem.class).equalTo("status", 5).sort("date", Sort.DESCENDING).findAll();
        listAdapter.setListItems(exerciseEntries);
        listAdapter.notifyDataSetChanged();
        break;
      case 6: // sick only, status of item = 4
        RealmResults<EntryItem> sickEntries = realm.where(EntryItem.class).equalTo("status", 4).sort("date", Sort.DESCENDING).findAll();
        listAdapter.setListItems(sickEntries);
        listAdapter.notifyDataSetChanged();
        break;
      case 7: // sweets only, status of item = 6
        RealmResults<EntryItem> sweetEntries = realm.where(EntryItem.class).equalTo("status", 6).sort("date", Sort.DESCENDING).findAll();
        listAdapter.setListItems(sweetEntries);
        listAdapter.notifyDataSetChanged();
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
  
  private void setFab() {
    if (sharedPreferences.getBoolean(PREF_DARK_MODE, false))
      fab.setImageResource(R.drawable.add_dark);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(getContext(), NewEntryActivity.class));
      }
    });
  }
}
