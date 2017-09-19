package io.github.coffeegerm.materiallogbook.rss;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

/*
* News Fragment
*
* Fragment that shows an RSS feed of diabetic news from sources
*/

public class NewsFragment extends Fragment {

//    private static final String TAG = "NewsFragment";

    String diabetesCoUkRelatedArticleLinks = "http://www.diabetes.co.uk/News/rss/newsindex.xml";
    @BindView(R.id.newsRecView)
    RecyclerView newsRecyclerView;
    @BindView(R.id.newsSwipeRefresh)
    SwipeRefreshLayout newsSwipeRefresh;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private RssAdapter mRssAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newsView = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, newsView);
        swipeRefreshSetup();
        loadNews();
        return newsView;
    }

    private void loadNews() {
        if (!newsSwipeRefresh.isRefreshing()) progressBar.setVisibility(View.VISIBLE);
        Parser parser = new Parser();
        parser.execute(diabetesCoUkRelatedArticleLinks);
        parser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<Article> list) {
                progressBar.setVisibility(View.GONE);
                newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRssAdapter = new RssAdapter(list, getActivity());
                newsSwipeRefresh.setRefreshing(false);
                newsRecyclerView.setAdapter(mRssAdapter);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                newsSwipeRefresh.setRefreshing(false);
                Toast.makeText(getContext(), "Error loading feed, swipe down to try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void swipeRefreshSetup() {
        newsSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark));
        newsSwipeRefresh.canChildScrollUp();
        newsSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews();
            }
        });
    }
}
