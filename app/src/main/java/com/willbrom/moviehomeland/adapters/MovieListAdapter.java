package com.willbrom.moviehomeland.adapters;

import android.content.Context;
import android.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.willbrom.moviehomeland.R;
import com.willbrom.moviehomeland.models.MovieResultsModel;
import com.willbrom.moviehomeland.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieItemViewHolder> {

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    private static final String TAG = MovieListAdapter.class.getSimpleName();
    private OnLoadMoreListener loadMoreListener;
    private List<MovieResultsModel> movieResultModels = new ArrayList<>();
    private Context context;

    public MovieListAdapter(Context context) {
        this.loadMoreListener = (OnLoadMoreListener) context;
        this.context = context;
    }

    @Override
    public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_item_layout, parent, false);
        return new MovieItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieItemViewHolder holder, int position) {
        URL posterUrl = NetworkUtils.getMoviePosterUrl(movieResultModels.get(position).getPoster_path());
        holder.movieTitle.setText(movieResultModels.get(position).getTitle());
        Picasso.with(context).load(String.valueOf(posterUrl)).into(holder.moviePoster);
        if ((position >= getItemCount() - 1)) {
            loadMoreListener.onLoadMore();
        }
    }

    @Override
    public int getItemCount() {
        if (movieResultModels == null) return 0;
        return movieResultModels.size();
    }

    public void setMovieResultModels(List<MovieResultsModel> movieResultModels) {
        this.movieResultModels.addAll(movieResultModels);
        notifyDataSetChanged();
    }

    class MovieItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_title_textView)
        TextView movieTitle;
        @BindView(R.id.movie_poster_imageView)
        ImageView moviePoster;

        MovieItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
