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
import com.trysafe.trysafe.Models.GridLayoutModel;
import com.trysafe.trysafe.R;
import com.trysafe.trysafe.ViewMoreActivity;
import com.trysafe.trysafe.WebViewActivity;

import java.util.List;

public class GridLayoutAdapter extends RecyclerView.Adapter<GridLayoutAdapter.ViewHolder> {

    private List<GridLayoutModel> list;

    public GridLayoutAdapter(List<GridLayoutModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categories_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        String mTitle = list.get(i).getTitle();
        String mImage = list.get(i).getImage();

        viewHolder.setTitle(mTitle);
        viewHolder.setImage(mImage);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent = new Intent(viewHolder.itemView.getContext(), ViewMoreActivity.class);
                viewIntent.putExtra("label",list.get(i).getLabel());
                viewIntent.putExtra("title",list.get(i).getTitle());
                viewHolder.itemView.getContext().startActivity(viewIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_view);
            image = itemView.findViewById(R.id.image_view);
        }

        private void setImage(String imageView){
            Glide.with(itemView.getContext()).load(imageView).placeholder(R.drawable.placeholder).into(image);
        }

        private void setTitle(String titleView){
            title.setText(titleView);
        }
    }
}
