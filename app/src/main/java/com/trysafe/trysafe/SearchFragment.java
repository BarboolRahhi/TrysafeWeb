package com.trysafe.trysafe;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.trysafe.trysafe.Adapters.PostAdapter;
import com.trysafe.trysafe.Adapters.ViewMoreAdapter;
import com.trysafe.trysafe.Api.BloggerApi;
import com.trysafe.trysafe.BloggerModels.Item;
import com.trysafe.trysafe.BloggerModels.PostList;
import com.trysafe.trysafe.Utils.EqualSpacingItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    ViewMoreAdapter adapter;
    List<Item> itemList;
    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    String token = "";

    ProgressBar loadMoreProgressBar;

    SearchView searchView;

    String query;

    RelativeLayout notFoundLayout;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = view.findViewById(R.id.searchView);
        notFoundLayout = view.findViewById(R.id.not_found_layout);
        loadMoreProgressBar = view.findViewById(R.id.loadProgressBar);

        itemList = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.postList);
        manager = new LinearLayoutManager(getContext());
        adapter = new ViewMoreAdapter(getContext(), itemList);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                itemList.clear();

                String url = "https://www.googleapis.com/blogger/v3/blogs/3162935987647642182/posts/search?q=" + s + "&key=AIzaSyAcl8zdcO9N_usuojiUEaB0jXiAW6U4cnQ";

                loadMoreProgressBar.setVisibility(View.VISIBLE);
                notFoundLayout.setVisibility(View.INVISIBLE);
                Call<PostList> postList = BloggerApi.getPostService().getPostList(url);

                postList.enqueue(new Callback<PostList>() {
                    @Override
                    public void onResponse(Call<PostList> call, Response<PostList> response) {


                        try {

                            if (response.isSuccessful()) {
                                PostList list = response.body();
                                token = list.getNextPageToken();
                                itemList.addAll(list.getItems());
                                adapter.notifyDataSetChanged();

                            } else {
                                Log.d(TAG, "Error" + response.errorBody().string());
                            }

                        } catch (NullPointerException e) {
                            Log.e(TAG, "onResponse :NullPointerException :" + e.getMessage());
                        } catch (IndexOutOfBoundsException e) {
                            Log.e(TAG, "onResponse :IndexOutOfBoundsException:" + e.getMessage());
                        } catch (IOException e) {
                            Log.e(TAG, "onResponse :IOException:" + e.getMessage());
                        }


                        loadMoreProgressBar.setVisibility(View.GONE);
                        if (itemList.size() > 0) {
                            notFoundLayout.setVisibility(View.INVISIBLE);

                        }else {
                            notFoundLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<PostList> call, Throwable t) {
                        Toast.makeText(getActivity(), "Error Occured", Toast.LENGTH_SHORT).show();
                        loadMoreProgressBar.setVisibility(View.GONE);

                    }
                });


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        return view;
    }


}
