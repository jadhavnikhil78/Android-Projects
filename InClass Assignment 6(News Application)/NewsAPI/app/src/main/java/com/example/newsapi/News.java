package com.example.newsapi;

import java.util.LinkedList;

public class News {
    String status;
    int totalResults;
    LinkedList<Articles> articleDetails;

    public News() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "News{" +
                "status='" + status + '\'' +
                ", totalResults=" + totalResults +
                ", articleDetails=" + articleDetails +
                '}';
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

}
