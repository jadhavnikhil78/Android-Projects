package com.example.newsapi;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements GetNews.IData {

    private ArrayList<News> mydata;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final LinkedList<String> categories = new LinkedList<String>();
        categories.add("Business");
        categories.add("Entertainment");
        categories.add("General");
        categories.add("Health");
        categories.add("Science");

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder category = new AlertDialog.Builder(MainActivity.this);
                category.setTitle("Category").setItems(new String[]{"Business", "Entertainment", "General", "Health", "Science"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String selectedCategory = categories.get(which).toLowerCase();

                                findViewById(R.id.textView_Loading).setVisibility(View.VISIBLE);
                                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                                findViewById(R.id.imageButton_Next).setVisibility(View.VISIBLE);
                                findViewById(R.id.imageButton_Previous).setVisibility(View.VISIBLE);

                                new GetNews(MainActivity.this).execute("https://newsapi.org/v2/top-headlines?country=us&category=" + selectedCategory + "&apiKey=d4949b4e414f4770b3250f7c36be7490");
                                //Picasso.get().load("https://newsapi.org/v2/top-headlines?country=us&category="+selectedCategory+"business&apiKey=d4949b4e414f4770b3250f7c36be7490").into((ImageView) findViewById(R.id.imageView_Photo));
                            }
                        });
                final AlertDialog alertDialog = category.create();
                alertDialog.show();
            }
        });


        findViewById(R.id.imageButton_Next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                News nw = new News();
                Articles ar = new Articles();

                if (counter < 19 && counter >= 0) {
                    counter++;

                    nw = mydata.get(0);
                    ar = nw.articleDetails.get(counter);

                    TextView title = (TextView) findViewById(R.id.textView_Title);
                    title.setText(ar.title);

                    TextView publishedAT = (TextView) findViewById(R.id.textView_PublishedAt);
                    publishedAT.setText(ar.publishedAt);

                    Picasso.get().load(ar.urlToImage).into((ImageView) findViewById(R.id.imageView_Photo));

                    TextView content = (TextView) findViewById(R.id.textView_Description);
                    content.setText(ar.content);

                    TextView newsCounter = findViewById(R.id.textView_counter);
                    newsCounter.setText("" + (counter + 1) + " out of 20");

                    Log.d("demo", "Counter " + counter);
                }
                else
                {
                    Log.d("demo", "First News");
                    counter = 0;

                    nw = mydata.get(0);
                    ar = nw.articleDetails.get(counter);

                    TextView title = (TextView) findViewById(R.id.textView_Title);
                    title.setText(ar.title);

                    TextView publishedAT = (TextView) findViewById(R.id.textView_PublishedAt);
                    publishedAT.setText(ar.publishedAt);

                    Picasso.get().load(ar.urlToImage).into((ImageView) findViewById(R.id.imageView_Photo));

                    TextView content = (TextView) findViewById(R.id.textView_Description);
                    content.setText(ar.content);

                    TextView newsCounter = findViewById(R.id.textView_counter);
                    newsCounter.setText("" + (counter + 1) + " out of 20");
                }
            }
        });

        findViewById(R.id.imageButton_Previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                News nw = new News();
                Articles ar = new Articles();
                if (counter <= 19 && counter > 0) {
                    counter--;

                    nw = mydata.get(0);
                    ar = nw.articleDetails.get(counter);

                    TextView title = (TextView) findViewById(R.id.textView_Title);
                    title.setText(ar.title);

                    TextView publishedAT = (TextView) findViewById(R.id.textView_PublishedAt);
                    publishedAT.setText(ar.publishedAt);

                    Picasso.get().load(ar.urlToImage).into((ImageView) findViewById(R.id.imageView_Photo));

                    TextView content = (TextView) findViewById(R.id.textView_Description);
                    content.setText(ar.content);

                    TextView newsCounter = findViewById(R.id.textView_counter);
                    newsCounter.setText("" + (counter + 1) + " out of 20");

                    Log.d("demo", "Counter " + counter);
                }
                else
                {
                    counter = 19;
                    Log.d("demo","Last News:" + counter);

                    nw = mydata.get(0);
                    ar = nw.articleDetails.get(counter);

                    TextView title = (TextView) findViewById(R.id.textView_Title);
                    title.setText(ar.title);

                    TextView publishedAT = (TextView) findViewById(R.id.textView_PublishedAt);
                    publishedAT.setText(ar.publishedAt);

                    Picasso.get().load(ar.urlToImage).into((ImageView) findViewById(R.id.imageView_Photo));

                    TextView content = (TextView) findViewById(R.id.textView_Description);
                    content.setText(ar.content);

                    TextView newsCounter = findViewById(R.id.textView_counter);
                    newsCounter.setText("" + (counter + 1) + " out of 20");
                }
            }
        });
    }

    @Override
    public void handleData(ArrayList<News> data) {
        mydata = data;
        News nw = new News();
        Articles ar = new Articles();

        nw = data.get(0);
        ar = nw.articleDetails.get(0);

        findViewById(R.id.textView_Loading).setVisibility(View.INVISIBLE);
        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);

        findViewById(R.id.textView_Title).setVisibility(View.VISIBLE);
        findViewById(R.id.textView_PublishedAt).setVisibility(View.VISIBLE);
        findViewById(R.id.imageView_Photo).setVisibility(View.VISIBLE);
        findViewById(R.id.textView_Description).setVisibility(View.VISIBLE);
        TextView newsCounter = findViewById(R.id.textView_counter);
        newsCounter.setText("" + (counter + 1) + " out of 20");
        newsCounter.setVisibility(View.VISIBLE);

        TextView title = (TextView) findViewById(R.id.textView_Title);
        title.setText(ar.title);

        TextView publishedAT = (TextView) findViewById(R.id.textView_PublishedAt);
        publishedAT.setText(ar.publishedAt);

        Picasso.get().load(ar.urlToImage).into((ImageView) findViewById(R.id.imageView_Photo));

        TextView content = (TextView) findViewById(R.id.textView_Description);
        content.setText(ar.content);
    }
}
