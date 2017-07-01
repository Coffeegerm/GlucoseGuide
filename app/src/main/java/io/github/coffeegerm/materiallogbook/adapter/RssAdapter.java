package io.github.coffeegerm.materiallogbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.model.EntryItem;

/**
 * Created by David Yarzebinski on 6/30/2017.
 * <p>
 * Adapter used to fill newsFragment
 */

public class RssAdapter extends RecyclerView.Adapter<RssAdapter.holder> {

    private LayoutInflater inflater;

    public RssAdapter(List<EntryItem> entryItemList, Context c) {
        this.inflater = LayoutInflater.from(c);
    }

    @Override
    public holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(holder holder, int position) {
        // RssItem item = mEntryItemList.get(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class holder extends RecyclerView.ViewHolder {

        public holder(View itemView) {
            super(itemView);

        }
    }

}
