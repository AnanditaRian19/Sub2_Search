package com.belajar.search.Ui.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.belajar.search.Adapter.MainAdapter;
import com.belajar.search.Model.GithubUser;
import com.belajar.search.Network.ApiService;
import com.belajar.search.Network.ServiceGenerator;
import com.belajar.search.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingFragment extends Fragment {

    private RecyclerView recyclerView;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shimmerFrameLayout = view.findViewById(R.id.shimmerLayout);

        showData(view);
    }

    private void showData(View view) {
        GithubUser githubUser = Objects.requireNonNull(getActivity()).getIntent().getParcelableExtra("DETAIL_USER");
        ApiService apiService = ServiceGenerator.getConnection()
                .create(ApiService.class);
        Call<List<GithubUser>> call = apiService.getFollowing(Objects.requireNonNull(githubUser).getLogin());
        call.enqueue(new Callback<List<GithubUser>>() {
            @Override
            public void onResponse(@NotNull Call<List<GithubUser>> call, @NotNull Response<List<GithubUser>> response) {
                MainAdapter mainAdapter = new MainAdapter(getContext(), response.body());
                recyclerView.setAdapter(mainAdapter);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<List<GithubUser>> call, @NotNull Throwable t) {
                Log.e("FAILED PARSING DATA", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}