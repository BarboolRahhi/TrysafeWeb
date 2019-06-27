package com.trysafe.trysafe.Adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.trysafe.trysafe.BloggerModels.Item;
import com.trysafe.trysafe.Models.HomePageModel;
import com.trysafe.trysafe.Models.GridLayoutModel;
import com.trysafe.trysafe.R;
import com.trysafe.trysafe.Utils.EqualSpacingItemDecoration;
import com.trysafe.trysafe.Utils.ItemOffsetDecoration;
import com.trysafe.trysafe.ViewMoreActivity;

import java.util.List;

public class HomePageAdapter extends RecyclerView.Adapter {

    private List<HomePageModel> homePageModels;
    private FragmentActivity activity;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public HomePageAdapter(List<HomePageModel> homePageModels, FragmentActivity activity) {
        this.homePageModels = homePageModels;
        this.activity = activity;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModels.get(position).getType()) {
            case 0:
                return HomePageModel.HORIZONTAL_JAVA_VIEW;
            case 1:
                return HomePageModel.CATEGORIES_VIEW;
            default:
                return -1;

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case HomePageModel.HORIZONTAL_JAVA_VIEW:
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_layout, viewGroup, false);
                return new HorizontalJavaLayoutViewHolder(view);
            case HomePageModel.CATEGORIES_VIEW:
                View view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_layout, viewGroup, false);
                return new CategotiesGridViewHolder(view1);
            default:
                return null;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (homePageModels.get(position).getType()) {
            case HomePageModel.HORIZONTAL_JAVA_VIEW:
                String title = homePageModels.get(position).getTitle();
                String label = homePageModels.get(position).getLabel();
                List<Item> list = homePageModels.get(position).getItems();
                ((HorizontalJavaLayoutViewHolder) viewHolder).setHorizontalJavaLayout(list, title,label);
                break;
            case HomePageModel.CATEGORIES_VIEW:
                String title1 = homePageModels.get(position).getTitle();
                List<GridLayoutModel> list1 = homePageModels.get(position).getGridLayoutModelList();
                ((CategotiesGridViewHolder) viewHolder).setGridLayout(list1,title1);
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return homePageModels.size();
    }

    public class HorizontalJavaLayoutViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView horizontalRecyclerView;
        private TextView horizontalTitle;
        private Button horizontalBtn;

        public HorizontalJavaLayoutViewHolder(@NonNull final View itemView) {
            super(itemView);

            horizontalTitle = itemView.findViewById(R.id.horizontal_layout_title);
            horizontalBtn = itemView.findViewById(R.id.horizontal_layout_btn);
            horizontalRecyclerView = itemView.findViewById(R.id.horizontl_layout_recycleview);
            horizontalRecyclerView.setRecycledViewPool(recycledViewPool);
        }


        private void setHorizontalJavaLayout(List<Item> items, final String title, final String label) {

            horizontalBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewIntent = new Intent(itemView.getContext(), ViewMoreActivity.class);
                    viewIntent.putExtra("label",label);
                    viewIntent.putExtra("title",title);
                    itemView.getContext().startActivity(viewIntent);
                    activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                }
            });

            if (items.size() > 0){
                horizontalTitle.setVisibility(View.VISIBLE);
                horizontalTitle.setText(title);
            }else {
                horizontalTitle.setVisibility(View.INVISIBLE);
            }
            if (items.size() > 4) {
                horizontalBtn.setVisibility(View.VISIBLE);
            } else {
                horizontalBtn.setVisibility(View.INVISIBLE);
            }

            HorizontalLayoutAdapter horizontalLayoutAdapter = new HorizontalLayoutAdapter(items);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

            horizontalRecyclerView.setLayoutManager(linearLayoutManager);
            horizontalRecyclerView.addItemDecoration(new EqualSpacingItemDecoration(12, EqualSpacingItemDecoration.HORIZONTAL));

            horizontalRecyclerView.setAdapter(horizontalLayoutAdapter);
            horizontalLayoutAdapter.notifyDataSetChanged();
        }
    }

    public class CategotiesGridViewHolder extends RecyclerView.ViewHolder{

        private RecyclerView gridRecyclerView;
        private TextView gridTitle;
        private Button gridBtn;

        public CategotiesGridViewHolder(@NonNull View itemView) {
            super(itemView);
            gridTitle = itemView.findViewById(R.id.horizontal_layout_title);
            gridBtn = itemView.findViewById(R.id.horizontal_layout_btn);
            gridRecyclerView = itemView.findViewById(R.id.horizontl_layout_recycleview);
            gridRecyclerView.setRecycledViewPool(recycledViewPool);

        }

        private void setGridLayout(List<GridLayoutModel> horizontalLayoutModels, String title) {



            if (horizontalLayoutModels.size() > 0){
                gridTitle.setVisibility(View.VISIBLE);
                gridTitle.setText(title);
            }else {
                gridTitle.setVisibility(View.INVISIBLE);
            }
            if (horizontalLayoutModels.size() > 4) {
                gridBtn.setVisibility(View.VISIBLE);
            } else {
                gridBtn.setVisibility(View.INVISIBLE);
            }


            GridLayoutAdapter gridLayoutAdapter = new GridLayoutAdapter(horizontalLayoutModels);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
            gridRecyclerView.setLayoutManager(staggeredGridLayoutManager);

           //ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(itemView.getContext(), R.dimen.item_offset);
          //  gridRecyclerView.addItemDecoration(itemDecoration);

            gridRecyclerView.setPadding(18,16,18,0);
            gridRecyclerView.setAdapter(gridLayoutAdapter);
            gridLayoutAdapter.notifyDataSetChanged();
        }
    }
}
