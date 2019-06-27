package com.trysafe.trysafe.Adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.trysafe.trysafe.BloggerModels.Item;
import com.trysafe.trysafe.R;
import com.trysafe.trysafe.WebViewActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HorizontalLayoutAdapter extends RecyclerView.Adapter<HorizontalLayoutAdapter.ViewHolder> {

    List<Item> items;

    public HorizontalLayoutAdapter(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        String mTitle = items.get(i).getTitle();

        Document document = Jsoup.parse(items.get(i).getContent());
        Elements elements = document.select("img");


        viewHolder.setTitle(mTitle);
        viewHolder.setImage(elements.get(0).attr("src"));
        viewHolder.setIntent(items.get(i).getUrl(),mTitle);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView image;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_view);
            image = itemView.findViewById(R.id.image_view);


        }

        private void setImage(String imageView){
            Glide.with(itemView.getContext())
                    .load(imageView)
                    .placeholder(R.drawable.placeholder)
                    .into(image);
        }

        private void setTitle(String titleView){
            title.setText(titleView);
        }

        private void setIntent(final String url, final String title){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(),WebViewActivity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("title",title);
                    itemView.getContext().startActivity(intent);
                }
            });
        }



    }
}
