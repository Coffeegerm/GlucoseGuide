package io.github.coffeegerm.materiallogbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.prof.rssparser.Article;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.github.coffeegerm.materiallogbook.R;

/**
 * Created by David Yarzebinski on 6/30/2017.
 * <p>
 * Adapter used to fill newsFragment
 */

public class RssAdapter extends RecyclerView.Adapter<RssAdapter.holder> {

    private final static String NON_THIN = "[^iIl1\\.,']";
    private LayoutInflater inflater;
    private ArrayList<Article> articleList;
    private Context mContext;

    public RssAdapter(ArrayList<Article> articleList, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.articleList = articleList;
        this.mContext = c;
    }

    private static int textWidth(String str) {
        return (str.length() - str.replaceAll(NON_THIN, "").length() / 2);
    }

    public static String ellipsize(String text, int max) {

        if (textWidth(text) <= max)
            return text;

        // Start by chopping off at the word before max
        // This is an over-approximation due to thin-characters...
        int end = text.lastIndexOf(' ', max - 3);

        // Just one long word. Chop it off.
        if (end == -1)
            return text.substring(0, max - 3) + "...";

        // Step forward as long as textWidth allows.
        int newEnd = end;
        do {
            end = newEnd;
            newEnd = text.indexOf(' ', end + 1);

            // No more spaces.
            if (newEnd == -1)
                newEnd = text.length();

        } while (textWidth(text.substring(0, newEnd) + "...") < max);

        return text.substring(0, end) + "...";
    }

    @Override
    public holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_rss_list, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(holder holder, final int position) {
        Article currentArticle = articleList.get(position);
        Date date = currentArticle.getPubDate();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        final String pubDate = sdf.format(date);

        holder.articleTitle.setText(currentArticle.getTitle());
        holder.articleDesc.setText(ellipsize(currentArticle.getDescription(), 250));
        holder.articlePubDate.setText(pubDate);

        // Opens desired article onClick
        holder.itemView.setOnClickListener(v -> {
            WebView articleView = new WebView(mContext);
            articleView.getSettings().setLoadWithOverviewMode(true);
            String articleLink = articleList.get(position).getLink();
            articleView.setHorizontalScrollBarEnabled(false);
            articleView.setWebChromeClient(new WebChromeClient());
            articleView.loadUrl(articleLink);
            android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(mContext).create();
            alertDialog.setView(articleView);
            alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL, "Close",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class holder extends RecyclerView.ViewHolder {
        private TextView articleTitle;
        private TextView articleDesc;
        private TextView articlePubDate;

        holder(View itemView) {
            super(itemView);
            articleTitle = itemView.findViewById(R.id.articleTitleTextView);
            articleDesc = itemView.findViewById(R.id.articleDescriptionTextView);
            articlePubDate = itemView.findViewById(R.id.articlePubDateTextView);
        }
    }

}
