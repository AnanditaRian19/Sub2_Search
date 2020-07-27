package com.belajar.search.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GithubResponseUser {

    @SerializedName("total_count")
    private int totalCount;

    @SerializedName("incomplete_result")
    private boolean incompleteResult;

    @SerializedName("items")
    private List<GithubUser> items;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isIncompleteResult() {
        return incompleteResult;
    }

    public void setIncompleteResult(boolean incompleteResult) {
        this.incompleteResult = incompleteResult;
    }

    public List<GithubUser> getItems() {
        return items;
    }

    public void setItems(List<GithubUser> items) {
        this.items = items;
    }
}
