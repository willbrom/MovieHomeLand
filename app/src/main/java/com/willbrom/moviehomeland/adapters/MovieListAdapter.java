package com.willbrom.moviehomeland.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.willbrom.moviehomeland.R;
import com.willbrom.moviehomeland.models.MoviePopularModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieItemViewHolder> {

    private MoviePopularModel moviePopularModel;

    @Override
    public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_item_layout, parent, false);
        return new MovieItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieItemViewHolder holder, int position) {
        holder.movieTitle.setText(moviePopularModel.getResults().get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if (moviePopularModel == null) return 0;
        return moviePopularModel.getResults().size();
    }

    public void setMoviePopularModel(MoviePopularModel moviePopularModel) {
        this.moviePopularModel = moviePopularModel;
        notifyDataSetChanged();
    }

    class MovieItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_title)
        TextView movieTitle;

        MovieItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
