package com.ishaq.popularfilm.network;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.ishaq.popularfilm.models.Movie;

import java.util.List;

public class SearchViewModel extends ViewModel {

    private final SearchRepository searchRepository;

    public SearchViewModel() {
        searchRepository = new SearchRepository();
    }

    public LiveData<List<Movie>> getSearchedMovies(String query) {
        return searchRepository.getMutableLiveData(query);
    }
}

