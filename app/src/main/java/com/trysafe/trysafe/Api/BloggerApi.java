package com.trysafe.trysafe.Api;

import com.trysafe.trysafe.BloggerModels.PostList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class BloggerApi {
    public static final String KEY = "AIzaSyAcl8zdcO9N_usuojiUEaB0jXiAW6U4cnQ";
    public static final String url = "https://www.googleapis.com/blogger/v3/blogs/3162935987647642182/posts/";
    public static final String URL_LIST_PROJECT = "https://www.googleapis.com/blogger/v3/blogs/3162935987647642182/posts/search?q=label:project&key=AIzaSyAcl8zdcO9N_usuojiUEaB0jXiAW6U4cnQ";
    public static final String URL_LIST_JAVA = "https://www.googleapis.com/blogger/v3/blogs/3162935987647642182/posts/search?q=label:Java&key=AIzaSyAcl8zdcO9N_usuojiUEaB0jXiAW6U4cnQ";
    public static final String URL_LIST_ANDROID = "https://www.googleapis.com/blogger/v3/blogs/3162935987647642182/posts/search?q=label:android&key=AIzaSyAcl8zdcO9N_usuojiUEaB0jXiAW6U4cnQ";

    public static final String searchUrl = "https://www.googleapis.com/blogger/v3/blogs/3162935987647642182/posts/search?q=";

    public static PostService postService = null;

    public static PostService getPostService(){
        if (postService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            postService = retrofit.create(PostService.class);
        }
        return postService;
    }


    public interface PostService{

        @GET
        Call<PostList> getPostList(@Url String url);

        @GET
        Call<PostList> getSearchPostList(@Url String searchUrl);
    }
}
