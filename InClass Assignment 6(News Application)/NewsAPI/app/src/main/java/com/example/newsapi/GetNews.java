package com.example.newsapi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

public class GetNews extends AsyncTask<String, Void, ArrayList<News>>{

    IData iData;

    public GetNews(IData iData) {
        this.iData = iData;
    }


    @Override
    protected void onPostExecute(ArrayList<News> strings) {
        iData.handleData(strings);
    }

    @Override
    protected ArrayList<News> doInBackground(String... strings) {
        HttpURLConnection connection = null;
        ArrayList<News> result = new ArrayList<>();
        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF-8");

                LinkedList<Articles> my_linked = new LinkedList<>();
                JSONObject root = new JSONObject(json);
                News news = new News();
                news.setStatus(root.getString("status"));
                news.setTotalResults(root.getInt("totalResults"));

                JSONArray articledetails = root.getJSONArray("articles");
                for (int i =0; i < articledetails.length(); i++) {
                    JSONObject article = articledetails.getJSONObject(i);
                    Articles articleData = new Articles();

                    articleData.setTitle(article.getString("title"));
                    articleData.setPublishedAt(article.getString("publishedAt"));
                    articleData.setUrlToImage(article.getString("urlToImage"));
                    articleData.setContent(article.getString("content"));

                    my_linked.add(articleData);
                }
                news.articleDetails = my_linked;
                result.add(news);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }



    public static interface IData{
        public void handleData(ArrayList<News> data);
    }
}