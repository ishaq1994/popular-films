package com.ishaq.popularfilm.models;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
@Entity(tableName = "movie_table")
public class Movie implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public String movieId;
    @SerializedName("vote_average")
    private String movieVote;
    @SerializedName("title")
    private String movieTitle;
    @SerializedName("poster_path")
    private String moviePoster;
    @SerializedName("overview")
    private String movieDescription;
    @SerializedName("release_date")
    private String movieReleaseDate;

    public Movie(String movieId,@NonNull String movieTitle, String movieVote, String movieDescription, String movieReleaseDate,String moviePoster) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieVote = movieVote;
        this.movieDescription = movieDescription;
        this.movieReleaseDate = movieReleaseDate;
        this.moviePoster = moviePoster;

    }

    @Ignore
    public Movie() {
    }
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieVote() {
        return movieVote;
    }

    public void setMovieVote(String movieVote) {
        this.movieVote = movieVote;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    @NonNull
    @Override

    public String toString() {
        return this.movieTitle;
    }

}


