package com.trysafe.trysafe;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.trysafe.trysafe.Adapters.PostAdapter;
import com.trysafe.trysafe.Api.BloggerApi;
import com.trysafe.trysafe.BloggerModels.Item;
import com.trysafe.trysafe.BloggerModels.PostList;
import com.trysafe.trysafe.Utils.EqualSpacingItemDecoration;
import com.trysafe.trysafe.Utils.SnackbarHelper;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedsFragment extends Fragment {


    public FeedsFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    PostAdapter adapter;
    List<Item> items = new ArrayList<>();
    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    String token = "";

    ProgressBar loadMoreProgressBar;
    CircleImageView accountImage;

    CoordinatorLayout coordinatorLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_feeds, container, false);

        loadMoreProgressBar = view.findViewById(R.id.loadProgressBar);

        accountImage = view.findViewById(R.id.profile_image);
        coordinatorLayout = view.findViewById(R.id.coordinator_layout);

        accountImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AccountActivity.class);
                getActivity().startActivity(intent);
                (getActivity()).overridePendingTransition(R.anim.side_from_right,R.anim.sideout_from_left);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.postList);
        manager = new LinearLayoutManager(getContext());
        adapter = new PostAdapter(getContext(),items);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(40));
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
                    getData();

                }
            }
        });


        getData();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    String imageUrl = task.getResult().getString("image");

                    Glide.with(getActivity())
                            .load(imageUrl)
                            .placeholder(R.drawable.defaultavater)
                            .into(accountImage);

                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getData()
    {

        String url = BloggerApi.url + "?key=" + BloggerApi.KEY;
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

                loadMoreProgressBar.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(coordinatorLayout,"Check Your Network Connection",Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getData();
                            }
                        }).setActionTextColor(Color.parseColor("#f4d03f"));

                SnackbarHelper.configSnackbar(getContext(), snackbar);

                snackbar.show();

            }
        });


    }

}
