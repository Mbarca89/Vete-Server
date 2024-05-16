package com.mbarca.vete.domain;

import java.util.List;

public class PaginatedResults<T> {
    private List<T> data;
    private int totalCount;

    public PaginatedResults(List<T> data, int totalCount) {
        this.data = data;
        this.totalCount = totalCount;
    }

    public PaginatedResults() {
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
