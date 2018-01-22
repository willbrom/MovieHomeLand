package com.willbrom.moviehomeland;

import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.willbrom.moviehomeland.adapters.MovieListAdapter;
import com.willbrom.moviehomeland.models.MoviePopularModel;
import com.willbrom.moviehomeland.utilities.NetworkUtils;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, MovieListAdapter.OnLoadMoreListener {

    @BindView(R.id.movie_recyclerView)
    RecyclerView movieRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private Unbinder unbinder;
    private MoviePopularModel moviePopularModel;
    private static final int LOADER_NUM = 11;
    private static final String LOADER_EXTRA = "loader_extra";
    private static final String TAG = MainActivity.class.getSimpleName();
    private MovieListAdapter movieAdapter;
    private Bundle loaderBundle;
    private URL movieUrl;
    private int pageNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        movieRecyclerView.setLayoutManager(gridLayoutManager);
        movieAdapter = new MovieListAdapter(this);
        movieRecyclerView.setAdapter(movieAdapter);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(LOADER_NUM);
        loaderBundle = new Bundle();
        movieUrl = NetworkUtils.getMovieUrl("popular", String.valueOf(pageNum));
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
        progressBar.setVisibility(View.INVISIBLE);
        if (data != null) {
            moviePopularModel = new Gson().fromJson(data, new TypeToken<MoviePopularModel>(){}.getType());
            Log.d(TAG, "Page num: " + moviePopularModel.getPage());
            movieAdapter.setMovieResultModels(moviePopularModel.getResults());
        } else {
            Log.d(TAG, "data is null");
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {}

    @Override
    public void onLoadMore() {
        pageNum++;
        loaderBundle = new Bundle();
        movieUrl = NetworkUtils.getMovieUrl("popular", String.valueOf(pageNum));
        loaderBundle.putString(LOADER_EXTRA, movieUrl.toString());
        getSupportLoaderManager().restartLoader(LOADER_NUM, loaderBundle, MainActivity.this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
