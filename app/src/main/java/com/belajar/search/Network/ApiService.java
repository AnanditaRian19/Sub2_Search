package com.belajar.search.Network;

import com.belajar.search.Model.GithubDetail;
import com.belajar.search.Model.GithubResponseUser;
import com.belajar.search.Model.GithubUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("users")
    Call<List<GithubUser>> getGithubUser(
            @Header("Authentication") String token);

    @GET("search/users")
    Call<GithubResponseUser> getGithubSearchUser(
            @Query("q") String username
    );

    @GET("users/{username}/followers")
    Call<List<GithubUser>> getFollowers(
            @Path("username") String username);

    @GET("users/{username}/following")
    Call<List<GithubUser>> getFollowing(
            @Path("username") String username);

    @GET("users/{username}")
    Call<GithubDetail> getUserDetail(
            @Path("username") String username);
}
