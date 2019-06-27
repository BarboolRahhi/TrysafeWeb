package com.trysafe.trysafe.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.trysafe.trysafe.BloggerModels.Item;
import com.trysafe.trysafe.R;
import com.trysafe.trysafe.WebViewActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

import static com.trysafe.trysafe.Utils.DateFormateClass.DateFormat2;

public class ViewMoreAdapter extends RecyclerView.Adapter<ViewMoreAdapter.PostViewHolder> {

    private Context context;
    private List<Item> items;

    public ViewMoreAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }


    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_more_item_layout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        final Item item = items.get(position);
        holder.postTitle.setText(item.getTitle());

        Document document = Jsoup.parse(item.getContent());
        holder.postDescription.setText(document.text());


        Elements elements = document.select("img");
        Glide.with(context).load(elements.get(0).attr("src")).placeholder(R.drawable.placeholder).into(holder.postImage);

        String d = item.getPublished().substring(0,10);
        holder.date.setText(DateFormat2(d));



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", item.getUrl());
                intent.putExtra("title", item.getTitle());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;
        TextView postTitle;
        TextView postDescription;
        TextView date;


        public PostViewHolder(View itemView) {
            super(itemView);
            postImage = (ImageView) itemView.findViewById(R.id.postImage);
            postTitle = (TextView) itemView.findViewById(R.id.postTitle);
            postDescription = (TextView) itemView.findViewById(R.id.postDescription);
            date = (TextView) itemView.findViewById(R.id.date);


        }
    }
}
