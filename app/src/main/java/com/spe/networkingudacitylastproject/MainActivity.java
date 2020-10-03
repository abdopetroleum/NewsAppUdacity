package com.spe.networkingudacitylastproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = News.class.getName();
    private static final String MYURL =
            "https://content.guardianapis.com/search";
    private static final int NEWS_LOADER_ID = 1;
    //gui vars
    private ListView newsList;
    private TextView noDataTextView;
    // list adapter
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeView();
    }

    public void initializeView() {
        newsList = findViewById(R.id.list);
        noDataTextView = findViewById(R.id.empty_view);
        newsList.setEmptyView(noDataTextView);
        adapter = new NewsAdapter(this, new ArrayList<News>());
        newsList.setAdapter(adapter);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager manager = getSupportLoaderManager();
            manager.initLoader(0, null, this);
        } else {
            noDataTextView.setText(R.string.no_internet);
        }
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News clickedNews = adapter.getItem(position);
                Uri newsLink = Uri.parse(clickedNews.getLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, newsLink);
                startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        Uri baseUri = Uri.parse(MYURL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q", "debates");
        uriBuilder.appendQueryParameter("api-key", "test");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        System.out.println(uriBuilder.toString());
        return new NewsLoader(MainActivity.this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {
        noDataTextView.setText(R.string.no_news);
        adapter.clear();
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        adapter.clear();
    }
}