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

package io.github.coffeegerm.glucoseguide.ui.list;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.glucoseguide.GlucoseGuide;
import io.github.coffeegerm.glucoseguide.R;
import io.github.coffeegerm.glucoseguide.data.model.EntryItem;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by David Yarzebinski on 6/7/17.
 * <p>
 * Fragment for list view of Entries
 */

public class ListFragment extends Fragment {
  
  @Inject
  public SharedPreferences sharedPreferences;
  
  @BindView(R.id.rec_view)
  RecyclerView recView;
  @BindView(R.id.empty_item_list)
  CardView emptyRecyclerViewExample;
  private Realm realm;
  private ListAdapter listAdapter;
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    final View listView = inflater.inflate(R.layout.fragment_list, container, false);
    ButterKnife.bind(this, listView);
    GlucoseGuide.syringe.inject(this);
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
      recView.setLayoutManager(new LinearLayoutManager(getActivity()));
      getListItems(1);
    }
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
        getListItems(0);
        return true;
      case R.id.list_sort_from_oldest:
        getListItems(1);
        return true;
      // breakfast only
      case R.id.list_status_breakfast:
        getListItems(2);
        return true;
      // lunch only
      case R.id.list_status_lunch:
        getListItems(3);
        return true;
      // dinner only
      case R.id.list_status_dinner:
        getListItems(4);
        return true;
      // exercise only
      case R.id.list_status_exercise:
        getListItems(5);
        return true;
      // sick only
      case R.id.list_status_sick:
        getListItems(6);
        return true;
      // sweets only
      case R.id.list_status_sweets:
        getListItems(7);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
  
  private void getListItems(int sortOrder) {
    switch (sortOrder) {
      case 0: // newest to older
        setAdapterItems(realm.where(EntryItem.class).sort("date", Sort.DESCENDING).findAll());
        break;
      case 1: // oldest to newest
        setAdapterItems(realm.where(EntryItem.class).sort("date", Sort.ASCENDING).findAll());
        break;
      case 2: // breakfast only, status of item = 1
        setAdapterItems(realm.where(EntryItem.class).equalTo("status", 1).sort("date", Sort.DESCENDING).findAll());
        break;
      case 3: // lunch only, status of item = 2
        setAdapterItems(realm.where(EntryItem.class).equalTo("status", 2).sort("date", Sort.DESCENDING).findAll());
        break;
      case 4: // dinner only, status of item = 3
        setAdapterItems(realm.where(EntryItem.class).equalTo("status", 3).sort("date", Sort.DESCENDING).findAll());
        break;
      case 5: // exercise only, status of item = 5
        setAdapterItems(realm.where(EntryItem.class).equalTo("status", 5).sort("date", Sort.DESCENDING).findAll());
        break;
      case 6: // sick only, status of item = 4
        setAdapterItems(realm.where(EntryItem.class).equalTo("status", 4).sort("date", Sort.DESCENDING).findAll());
        break;
      case 7: // sweets only, status of item = 6
        setAdapterItems(realm.where(EntryItem.class).equalTo("status", 6).sort("date", Sort.DESCENDING).findAll());
        break;
    }
  }
  
  private void setAdapterItems(RealmResults<EntryItem> entriesToShow) {
    listAdapter.setListItems(entriesToShow);
    notifyAdapterOfDataSetChange();
  }
  
  private void notifyAdapterOfDataSetChange() {
    listAdapter.notifyDataSetChanged();
  }
}
