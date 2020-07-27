package com.belajar.search.Ui.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.belajar.search.Adapter.MainAdapter;
import com.belajar.search.Adapter.UserSearchAdapter;
import com.belajar.search.Model.GithubResponseUser;
import com.belajar.search.Model.GithubUser;
import com.belajar.search.Network.ApiService;
import com.belajar.search.Network.Base;
import com.belajar.search.Network.ServiceGenerator;
import com.belajar.search.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.behavior.SwipeDismissBehavior;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    androidx.appcompat.widget.SearchView searchView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ShimmerFrameLayout shimmerFrameLayout;
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        getDataUser();

    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getDataUser();
            swipeRefreshLayout.setRefreshing(false);
        });

        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        shimmerFrameLayout.startShimmer();
    }

    private void getDataUser() {
        ApiService apiService = ServiceGenerator.getConnection()
                .create(ApiService.class);
        Call<List<GithubUser>> call = apiService.getGithubUser(Base.TOKEN);
        call.enqueue(new Callback<List<GithubUser>>() {
            @Override
            public void onResponse(@NotNull Call<List<GithubUser>> call, @NotNull Response<List<GithubUser>> response) {
                MainAdapter mainAdapter = new MainAdapter(MainActivity.this, response.body());
                recyclerView.setAdapter(mainAdapter);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<List<GithubUser>> call, Throwable t) {

            }
        });
    }

    private void getDataUserSearch(String name) {
        ApiService apiService = ServiceGenerator.getConnection()
                .create(ApiService.class);
        Call<GithubResponseUser> call = apiService.getGithubSearchUser(name);
        call.enqueue(new Callback<GithubResponseUser>() {
            @Override
            public void onResponse(@NotNull Call<GithubResponseUser> call, @NotNull Response<GithubResponseUser> response) {
                if (response.body() != null) {
                    UserSearchAdapter userSearchAdapter = new UserSearchAdapter(response.body().getItems());
                    recyclerView.setAdapter(userSearchAdapter);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NotNull Call<GithubResponseUser> call, @NotNull Throwable t) {
                Toast.makeText(MainActivity.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            searchView = (androidx.appcompat.widget.SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    getDataUserSearch(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
//                    getDataUserSearch(newText);
                    return true;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
                return true;
            default:
                return true;
        }
    }
}