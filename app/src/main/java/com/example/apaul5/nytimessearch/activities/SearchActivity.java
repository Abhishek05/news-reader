package com.example.apaul5.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.apaul5.nytimessearch.Article;
import com.example.apaul5.nytimessearch.ArticleArrayAdapter;


import com.example.apaul5.nytimessearch.EndlessScrollListener;
import com.example.apaul5.nytimessearch.R;
import com.example.apaul5.nytimessearch.model.RequestModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    EditText edText;
    Button btSearch;
    //GridView gvResult;

    ListView lvView;

    //RecyclerView gvResult;

    ArrayList<Article> articles;


    ArticleArrayAdapter articleArrayAdapter;

    RequestModel request;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupViews();
    }

    public void setupViews() {
        edText = (EditText) findViewById(R.id.edSearch);
        //gvResult = (GridView) findViewById(R.id.gridView);

        lvView = (ListView)findViewById(R.id.lvView);
        btSearch = (Button) findViewById(R.id.btSearch);

        //request = new RequestModel();

        articles = new ArrayList<Article>();

        articleArrayAdapter = new ArticleArrayAdapter(this,articles);
        //gvResult.setAdapter(articleArrayAdapter);
        lvView.setAdapter(articleArrayAdapter);

        lvView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = articles.get( i);
                intent.putExtra("url", article.getNewsUrl());
                intent.putExtra("article", article);
                startActivity(intent);
            }
        });

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        lvView.setOnScrollListener(new EndlessScrollListener(){


            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page);
                return true;
            }

            @Override
            public int getFooterViewType() {
                return 0;
            }


        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Menu item tapped !", Toast.LENGTH_SHORT).show();
            launchRequestActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchRequestActivity(){
        // start the requestactivity
        //startActivity(intent);
        Intent intent = new Intent(this, RequestActivity.class);
        startActivityForResult( intent, 1);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // To be filled out....
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                request  = (RequestModel)data.getSerializableExtra("request");

                Toast.makeText(this, request.toString(), Toast.LENGTH_SHORT).show();
            }
        }


    }



    public void onArticleSearch (View view){
        String text = edText.getText().toString();
        //Toast.makeText(this, text, Toast.LENGTH_LONG).show();

        AsyncHttpClient async = new AsyncHttpClient();

        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams param = new RequestParams();
        param.put("api-key", "6cf601355e4249689117fa77dc282ed6");
        param.put("page", 0);
        param.put("q", text);
        if(request != null){

            Log.d("DEBUG","date "+ request.getBeginDate());
            param.put("begin_date", request.getBeginDate());
            param.put("fq", "news_desk:("+request.getDeskValue()+")");
            param.put("sort", request.getSort());
        }

        async.get(url, param, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("DEBUG", response.toString());
                JSONArray articleArray = null;
                try {

                    articleArray = response.getJSONObject("response").getJSONArray("docs");
                    articleArrayAdapter.clear();

                    articleArrayAdapter.addAll(Article.fromJsonArray(articleArray));
                    articleArrayAdapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }

        });
    }

    public void customLoadMoreDataFromApi(int offset) {
        // Send an API request to retrieve appropriate data using the offset value as a parameter.
        // Deserialize API response and then construct new objects to append to the adapter
        // Add the new objects to the data source for the adapter


        String text = edText.getText().toString();
        Toast.makeText(this, String.valueOf(offset), Toast.LENGTH_LONG).show();

        AsyncHttpClient async = new AsyncHttpClient();

        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams param = new RequestParams();
        param.put("api-key", "6cf601355e4249689117fa77dc282ed6");

        param.put("page", offset);
        param.put("q", text);
        if(request != null){

            Log.d("DEBUG","date "+ request.getBeginDate());
            param.put("begin_date", request.getBeginDate());
            param.put("fq", "news_desk:("+request.getDeskValue()+")");
            param.put("sort", request.getSort());
        }


        async.get(url, param, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("DEBUG", response.toString());
                JSONArray articleArray = null;
                try {

                    articleArray = response.getJSONObject("response").getJSONArray("docs");
                    //articleArrayAdapter.clear();

                    articleArrayAdapter.addAll(Article.fromJsonArray(articleArray));

                    int curSize = articleArrayAdapter.getCount();
                    articleArrayAdapter.notifyDataSetChanged();

                    Log.d("DEBUG", articles.toString());

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }

        });



    }



}
