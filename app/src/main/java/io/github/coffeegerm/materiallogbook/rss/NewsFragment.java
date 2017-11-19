package io.github.coffeegerm.materiallogbook.rss;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;

import static io.github.coffeegerm.materiallogbook.utils.Constants.ARTICLE_LINK;

/*
* News Fragment
*
* Fragment that shows an RSS feed of diabetic news from sources
*/

public class NewsFragment extends Fragment {

    private static final String TAG = "NewsFragment";

    @BindView(R.id.newsRecView)
    RecyclerView newsRecyclerView;
    @BindView(R.id.newsSwipeRefresh)
    SwipeRefreshLayout newsSwipeRefresh;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private RssAdapter rssAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newsView = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, newsView);
        return newsView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rssAdapter = new RssAdapter(getActivity());
        newsRecyclerView.setAdapter(rssAdapter);
        swipeRefreshSetup();
        loadNews();
    }

    private void loadNews() {
        if (!newsSwipeRefresh.isRefreshing()) progressBar.setVisibility(View.VISIBLE);
        Parser parser = new Parser();
        parser.execute(ARTICLE_LINK);
        parser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<Article> list) {
                Log.i(TAG, "News successfully loaded");
                progressBar.setVisibility(View.GONE);
                rssAdapter.setNewsList(list);
                Log.i(TAG, "News List Set");
                rssAdapter.notifyDataSetChanged();
                Log.i(TAG, "notifyDataSetChanged");
                newsSwipeRefresh.setRefreshing(false);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                newsSwipeRefresh.setRefreshing(false);
                Log.i(TAG, "Error loading news feed");
                Toast.makeText(getContext(), "Error loading feed, swipe down to try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void swipeRefreshSetup() {
        newsSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews();
            }
        });
    }
}
