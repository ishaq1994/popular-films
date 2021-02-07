package com.ishaq.popularfilm.network;


import com.ishaq.popularfilm.models.MovieApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    String API_KEY="faeba51b15e5f7b132c5045df6581659";

    @GET("movie/{sort}")
    Call<MovieApiResponse> getMovies(@Path("sort") String sortBy, @Query("page") int page, @Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MovieApiResponse> searchForMovies(@Query("query") String query, @Query("api_key") String apiKey);


}


