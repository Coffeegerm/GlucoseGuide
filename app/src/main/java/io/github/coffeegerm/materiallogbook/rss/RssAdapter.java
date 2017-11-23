package io.github.coffeegerm.materiallogbook.rss;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.prof.rssparser.Article;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import timber.log.Timber;

import static io.github.coffeegerm.materiallogbook.utils.Constants.DATE_FORMAT;

/**
 * Created by David Yarzebinski on 6/30/2017.
 * <p>
 * Adapter used to fill newsFragment
 */

public class RssAdapter extends RecyclerView.Adapter<RssAdapter.NewsViewHolder> {

    private static final String TAG = "NewsFragmentAdapter";

    private final static String NON_THIN = "[^iIl1.,']";
    private LayoutInflater inflater;
    private ArrayList<Article> articleList;
    private Context context;

    RssAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.articleList = new ArrayList<>();
        this.context = context;
    }

    void setNewsList(ArrayList<Article> articles) {
        this.articleList = articles;
        Timber.i("Articles set");
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsViewHolder(inflater.inflate(R.layout.item_rss_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, int position) {
        Article currentArticle = articleList.get(position);
        Date date = currentArticle.getPubDate();
        final String pubDate = DATE_FORMAT.format(date);

        holder.articleTitle.setText(currentArticle.getTitle());
        holder.articleDesc.setText(ellipsize(currentArticle.getDescription()));
        holder.articlePubDate.setText(pubDate);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onClick(View view) {
                WebView articleView = new WebView(context);
                articleView.getSettings().setLoadWithOverviewMode(true);
                String articleLink = articleList.get(holder.getAdapterPosition()).getLink();
                articleView.getSettings().setJavaScriptEnabled(true);
                articleView.setHorizontalScrollBarEnabled(false);
                articleView.setWebChromeClient(new WebChromeClient());
                articleView.loadUrl(articleLink);
                android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(context).create();
                alertDialog.setView(articleView);
                alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL, "Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.articleTitleTextView)
        TextView articleTitle;
        @BindView(R.id.articleDescriptionTextView)
        TextView articleDesc;
        @BindView(R.id.articlePubDateTextView)
        TextView articlePubDate;

        NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private static int textWidth(String str) {
        return (str.length() - str.replaceAll(NON_THIN, "").length() / 2);
    }

    private static String ellipsize(String text) {
        int maxLength = 250;
        if (textWidth(text) <= maxLength)
            return text;

        // Start by chopping off at the word before max
        // This is an over-approximation due to thin-characters...
        int end = text.lastIndexOf(' ', maxLength - 3);

        // Just one long word. Chop it off.
        if (end == -1)
            return text.substring(0, maxLength - 3) + "...";

        // Step forward as long as textWidth allows.
        int newEnd = end;
        do {
            end = newEnd;
            newEnd = text.indexOf(' ', end++);

            // No more spaces.
            if (newEnd == -1)
                newEnd = text.length();

        } while (textWidth(text.substring(0, newEnd) + "...") < maxLength);

        return text.substring(0, end) + "...";
    }

}
