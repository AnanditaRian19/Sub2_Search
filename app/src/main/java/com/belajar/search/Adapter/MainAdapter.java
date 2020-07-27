package com.belajar.search.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.belajar.search.Model.GithubUser;
import com.belajar.search.R;
import com.belajar.search.Ui.Activity.DetailActivity;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private Context context;
    private List<GithubUser> mUser;

    public MainAdapter(Context context, List<GithubUser> users) {
        this.context = context;
        this.mUser = users;
    }

    @NonNull
    @Override
    public MainAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.MainViewHolder holder, int position) {

        holder.bind(mUser.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GithubUser githubUser = mUser.get(position);
                Intent mIntent = new Intent(context, DetailActivity.class);
                mIntent.putExtra("DETAIL_USER", githubUser);
                view.getContext().startActivity(mIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView ivAvatar;
        private TextView tvName, tvLink;
        private CardView cardView;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvLink = itemView.findViewById(R.id.tvLink);
            cardView = itemView.findViewById(R.id.cardView);
        }

        private void bind(GithubUser githubUser) {
            Glide.with(itemView.getContext())
                    .load(githubUser.getAvatarUrl())
                    .into(ivAvatar);
            tvName.setText(githubUser.getLogin());
            tvLink.setText(githubUser.getHtmlUrl());
        }
    }
}
