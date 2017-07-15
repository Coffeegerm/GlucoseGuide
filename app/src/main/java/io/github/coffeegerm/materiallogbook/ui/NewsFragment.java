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
import android.widget.Toast;

import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.adapter.RssAdapter;

/*
* News Fragment
*
* Fragment that shows an RSS feed of diabetic news from sources
* */

public class NewsFragment extends Fragment {

    private static final String TAG = "NewsFragment";

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
        setupNewsFeed();

//        newsSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                setupNewsFeed();
//            }
//        });

        return newsView;
    }

    private void setupNewsFeed() {
        Parser parser = new Parser();
        parser.execute(diabetesCoUkRelatedArticleLinks);
        parser.onFinish(new Parser.OnTaskCompleted() {

            @Override
            public void onTaskCompleted(ArrayList<Article> list) {
                newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                newsRecyclerView.setAdapter(new RssAdapter(list, getActivity()));
            }

            @Override
            public void onError() {
                Toast.makeText(getContext(), "Error loading feed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
