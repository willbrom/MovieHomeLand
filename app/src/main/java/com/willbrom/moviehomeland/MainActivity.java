package com.willbrom.moviehomeland;

import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.willbrom.moviehomeland.adapters.MovieListAdapter;
import com.willbrom.moviehomeland.utilities.NetworkUtils;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    @BindView(R.id.movie_recyclerView)
    RecyclerView movieRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private Unbinder unbinder;
    private static final int LOADER_NUM = 11;
    private static final String LOADER_EXTRA = "loader_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        movieRecyclerView.setLayoutManager(gridLayoutManager);
        MovieListAdapter movieAdapter = new MovieListAdapter();
        movieRecyclerView.setAdapter(movieAdapter);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(LOADER_NUM);
        Bundle loaderBundle = new Bundle();
        URL movieUrl = NetworkUtils.getMovieUrl("popular", "1");
        loaderBundle.putString(LOADER_EXTRA, movieUrl.toString());

        if (savedInstanceState == null) {
            if (loader == null)
                loaderManager.initLoader(LOADER_NUM, loaderBundle, this);
            else
                loaderManager.restartLoader(LOADER_NUM, loaderBundle, this);
        } else
            loaderManager.initLoader(LOADER_NUM, null, this);
    }

    @Override
    public Loader<String> onCreateLoader(final int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            String mJsonData;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                progressBar.setVisibility(View.VISIBLE);
                if (mJsonData != null)
                    deliverResult(mJsonData);
                else
                    forceLoad();
            }

            @Nullable
            @Override
            public String loadInBackground() {
                String movieUrl = args.getString(LOADER_EXTRA);
                try {
                    URL url = new URL(movieUrl);
                    return NetworkUtils.getHttpResponse(url);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(@Nullable String data) {
                mJsonData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
