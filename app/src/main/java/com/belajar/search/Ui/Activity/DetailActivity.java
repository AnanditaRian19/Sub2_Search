package com.belajar.search.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.belajar.search.Adapter.MainAdapter;
import com.belajar.search.Adapter.UserSearchAdapter;
import com.belajar.search.BuildConfig;
import com.belajar.search.Model.GithubDetail;
import com.belajar.search.Model.GithubUser;
import com.belajar.search.Network.ApiService;
import com.belajar.search.Network.ServiceGenerator;
import com.belajar.search.R;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private CircleImageView ivAvatar;
    private AVLoadingIndicatorView indicatorView;
    private TextView tvName, tvLocation, tvBlog, tvRepos, tvFollowers, tvFollowing;
    public static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getUserDetail();
    }

    private void initViews() {
        ivAvatar = findViewById(R.id.ivAvatar);
        tvName = findViewById(R.id.tvName);
        tvLocation = findViewById(R.id.tvLocation);
        tvBlog = findViewById(R.id.tvBlog);
        tvRepos = findViewById(R.id.tvRepos);
        tvFollowers = findViewById(R.id.tvFollowers);
        tvFollowing = findViewById(R.id.tvFollowing);
        indicatorView = findViewById(R.id.loading);

        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Detail");
            }
        } catch (Exception e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }

    private void getUserDetail() {
        GithubUser githubUser = getIntent().getParcelableExtra("DETAIL_USER");
        if (githubUser != null) {
            initViews();

            ApiService apiService = ServiceGenerator.getConnection()
                    .create(ApiService.class);
            Call<GithubDetail> call = apiService.getUserDetail(githubUser.getLogin());
            call.enqueue(new Callback<GithubDetail>() {
                @Override
                public void onResponse(@NotNull Call<GithubDetail> call, @NotNull Response<GithubDetail> response) {
                    if (response.body() != null) {
                        indicatorView.setVisibility(View.GONE);

                        Glide.with(getApplicationContext())
                                .load(response.body().getAvatarUrl())
                                .into(ivAvatar);
                        tvName.setText(response.body().getName());
                        tvRepos.setText(String.valueOf(response.body().getPublicRepos()));
                        tvFollowers.setText(String.valueOf(response.body().getFollowers()));
                        tvFollowing.setText(String.valueOf(response.body().getFollowing()));

                        if (response.body().getLocation() != null && response.body().getBlog() != null) {
                            tvLocation.setText(response.body().getLocation());
                            tvBlog.setText(response.body().getBlog());
                        } else if (response.body().getLocation() != null || response.body().getBlog() != null) {
                            if (response.body().getLocation() != null) {
                                tvLocation.setText(response.body().getLocation());
                                tvBlog.setText(R.string.str_null);
                                tvBlog.setTypeface(tvBlog.getTypeface(), Typeface.BOLD);
                            } else if (response.body().getBlog() != null) {
                                tvBlog.setText(response.body().getBlog());
                                tvLocation.setText(R.string.str_null);
                                tvLocation.setTypeface(tvLocation.getTypeface(), Typeface.BOLD);
                            }
                        } else {
                            tvLocation.setText(R.string.str_null);
                            tvBlog.setText(R.string.str_null);
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<GithubDetail> call, @NotNull Throwable t) {
                    Log.e("FAILED PARSING DATA ", Objects.requireNonNull(t.getMessage()));
                }
            });
        }
    }
}