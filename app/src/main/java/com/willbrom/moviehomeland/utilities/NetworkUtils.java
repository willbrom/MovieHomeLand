package com.willbrom.moviehomeland.utilities;

import android.net.Uri;
import android.util.Log;

import com.willbrom.moviehomeland.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class NetworkUtils {
    private static final String BASE_URL_MOVIES = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY_VALUE = BuildConfig.THE_MOVIE_DB_API_KEY;

    public static URL getMovieUrl(String orderBy, String page) {
        Uri movieUri = Uri.parse(BASE_URL_MOVIES + orderBy).buildUpon()
                .appendQueryParameter("api_key", API_KEY_VALUE)
                .appendQueryParameter("page", page)
                .build();
        URL movieUrl = null;
        try {
            movieUrl = new URL(movieUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return movieUrl;
    }

    public static String getHttpResponse(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            boolean hasNext = scanner.hasNext();
            if (hasNext)
                return scanner.next();
            else
                return null;
        } finally {
            connection.disconnect();
        }
    }
}
