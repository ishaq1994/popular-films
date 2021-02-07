package com.ishaq.popularfilm.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


import com.google.android.material.snackbar.Snackbar;
import com.ishaq.popularfilm.R;

import com.ishaq.popularfilm.adapters.MovieAdapter;
import com.ishaq.popularfilm.adapters.SearchAdapter;
import com.ishaq.popularfilm.models.Movie;
import com.ishaq.popularfilm.network.MovieViewModel;
import com.ishaq.popularfilm.network.SearchViewModel;
import com.ishaq.popularfilm.receiver.NetworkChangeReceiver;
import com.ishaq.popularfilm.utils.OnNetworkListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.ishaq.popularfilm.utils.Constant.MOVIE;


public class MainActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener, MovieAdapter.MovieAdapterOnClickHandler,OnNetworkListener {

    private static final String TAG = "MainActivity";
    public static Dialog progressDialog;

    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    SearchAdapter searchAdapter;
    List<Movie> searchedMovieList;
    MovieViewModel movieViewModel;
    SearchViewModel searchViewModel;
    TextView no_movies_found;

    private NetworkChangeReceiver mNetworkReceiver;
    private Snackbar snack;

    SharedPreferences sharedPreferences;
    public static String sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = createProgressDialog(MainActivity.this);

        snack = Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_INDEFINITE);

        // Get ViewModels
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sort = sharedPreferences.getString(getString(R.string.sort_key), getString(R.string.popular_value));

        setViews();

        loadMovies();

        // Register the listener
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        mNetworkReceiver = new NetworkChangeReceiver();
        mNetworkReceiver.setOnNetworkListener(this);

    }

    private void setViews() {
        recyclerView = findViewById(R.id.movie_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ? 3 : 5));
        recyclerView.setHasFixedSize(true);
        no_movies_found = findViewById(R.id.no_movies_found);

        movieAdapter = new MovieAdapter(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);


        MenuItem searchViewItem = menu.findItem(R.id.search);

        final SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search_for_movies));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (isNetworkConnected()) {
                    Search(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (isNetworkConnected()) {
                    Search(newText);
                }
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (searchedMovieList != null) {
                    searchAdapter.clear();
                    loadMovies();
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.setting) {
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Search for movies.
     */
    private void Search(String query) {
        searchViewModel.getSearchedMovies(query).observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {

                if (!movies.isEmpty()) {
                    searchedMovieList = movies;

                    searchAdapter = new SearchAdapter(getApplicationContext(), searchedMovieList, new SearchAdapter.SearchAdapterOnClickHandler() {
                        @Override
                        public void onClick(Movie movie) {
                            Intent intent = new Intent(MainActivity.this, MovieActivity.class);
                            // Pass an object of movie class
                            intent.putExtra(MOVIE, (movie));
                            startActivity(intent);
                        }
                    });

                    recyclerView.setAdapter(searchAdapter);
                }else {
                   getNoResult();
                }
            }
        });
    }

    /**
     * Get no result for search.
     */
    private void getNoResult() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setMessage(getResources().getString(R.string.no_match));

        // Create and show the AlertDialog
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                alertDialog.dismiss();
                timer.cancel();
            }
        }, 1000);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            progressDialog.dismiss();
            showSnackBar();
            return false;
        }
    }
    public void showSnackBar() {
        snack.setAction("CLOSE", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snack.dismiss();
            }
        });
        snack.setActionTextColor(getResources().getColor(R.color.colorAccent));
        snack.show();
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(MainActivity.this, MovieActivity.class);
        // Pass an object of movie class
        intent.putExtra(MOVIE, (movie));
        startActivity(intent);
    }

    public static Dialog createProgressDialog(Context context) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
        return progressDialog;
    }

    public static String getSort() {
        return sort;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.sort_key))) {
            if (isNetworkConnected()) {
                sort = sharedPreferences.getString(getString(R.string.sort_key), getString(R.string.popular_value));
                movieViewModel.invalidateDataSource();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    private void loadMovies() {
        if (isNetworkConnected()) {
            // Observe the moviePagedList from ViewModel
            movieViewModel.moviePagedList.observe(this, new Observer<PagedList<Movie>>() {
                @Override
                public void onChanged(@Nullable PagedList<Movie> movies) {
                    // In case of any changes, submitting the movies to adapter
                    movieAdapter.submitList(movies);
                    // when screen is rotated
                    if (movies != null && !movies.isEmpty()) {
                        progressDialog.dismiss();
                    }
                }
            });
        }

        recyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
    }
    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            registerNetworkBroadcastForNougat();
        }catch (Exception e){
            Log.d(TAG, "onStart: " + "already registered");
        }
    }

    @Override
    protected void onStop() {
        try {
            unregisterReceiver(mNetworkReceiver);
        }catch (Exception e){
            Log.d(TAG, "onStop: " + "already unregistered");
        }
        super.onStop();
    }
    @Override
    public void onNetworkConnected() {
        snack.dismiss();
        progressDialog.show();
        sort = sharedPreferences.getString(getString(R.string.sort_key), getString(R.string.popular_value));
        movieViewModel.invalidateDataSource();
        loadMovies();
    }

    @Override
    public void onNetworkDisconnected() {
        showSnackBar();
    }
}
