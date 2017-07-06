package io.github.coffeegerm.materiallogbook.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;

/*
* News Fragment
*
* Fragment that shows an RSS feed of diabetic news from sources
* */

public class NewsFragment extends Fragment {

    String diabetesCoUkRelatedArticleLinks = "http://www.diabetes.co.uk/News/rss/newsindex.xml";

    @BindView(R.id.newsRecView)
    RecyclerView newsRecyclerView;

    @BindView(R.id.newsSwipeRefresh)
    SwipeRefreshLayout newsSwipeRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newsView = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, newsView);

        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        newsSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        return newsView;
    }
}
