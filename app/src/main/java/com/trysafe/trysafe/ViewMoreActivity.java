package com.trysafe.trysafe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.trysafe.trysafe.Adapters.PostAdapter;
import com.trysafe.trysafe.Adapters.ViewMoreAdapter;
import com.trysafe.trysafe.Api.BloggerApi;
import com.trysafe.trysafe.BloggerModels.Item;
import com.trysafe.trysafe.BloggerModels.PostList;
import com.trysafe.trysafe.Utils.EqualSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewMoreActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    ViewMoreAdapter adapter;
    List<Item> items = new ArrayList<>();
    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    String token = "";

    ProgressBar loadMoreProgressBar;

    String getLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_more);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));

        getLabel = getIntent().getStringExtra("label");

        loadMoreProgressBar = findViewById(R.id.loadProgressBar);

        recyclerView = (RecyclerView) findViewById(R.id.postList);
        manager = new LinearLayoutManager(this);
        adapter = new ViewMoreAdapter(this,items);
        recyclerView.setLayoutManager(manager);
 //       recyclerView.addItemDecoration(new EqualSpacingItemDecoration(20));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems + scrollOutItems == totalItems))
                {
                    isScrolling = false;
                    getData(getLabel);

                }
            }
        });


        getData(getLabel);

    }

    private void getData(String label)
    {



        String url = "https://www.googleapis.com/blogger/v3/blogs/3162935987647642182/posts/search?q=label:"+label+"&key=AIzaSyAcl8zdcO9N_usuojiUEaB0jXiAW6U4cnQ";
        if(token != ""){
            url = url+ "&pageToken="+ token;
        }
        if(token == null){
            return;
        }

        loadMoreProgressBar.setVisibility(View.VISIBLE);
        Call<PostList> postList = BloggerApi.getPostService().getPostList(url);

        postList.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                if (response.isSuccessful()) {
                    PostList list = response.body();
                    token = list.getNextPageToken();
                    items.addAll(list.getItems());
                    adapter.notifyDataSetChanged();

                }
                loadMoreProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                Toast.makeText(ViewMoreActivity.this
                        , "Error Occured", Toast.LENGTH_SHORT).show();
                loadMoreProgressBar.setVisibility(View.GONE);

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}
