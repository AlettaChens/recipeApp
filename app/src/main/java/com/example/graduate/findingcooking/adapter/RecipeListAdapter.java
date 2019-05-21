package com.example.graduate.findingcooking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.bean.Recipe;
import com.example.graduate.findingcooking.utils.GlideX;

import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeHolder> {
    private Context context;
    private List<Recipe> recipeList;
    private RecipeClickListener RecipeClickListener;

    public RecipeListAdapter(Context context, List<Recipe> recipeList, RecipeListAdapter.RecipeClickListener recipeClickListener) {
        this.context = context;
        this.recipeList = recipeList;
        RecipeClickListener = recipeClickListener;
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe_list, viewGroup, false);
        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder recipeHolder, int i) {
        RecipeHolder recommendHolder = recipeHolder;
        recommendHolder.update(recipeList.get(i), i);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

       public class RecipeHolder extends RecyclerView.ViewHolder {
        private TextView recipe_name;
        private CircleImageView recipe_pic;
        private TextView recipe_update_time;
        private TextView recipe_class;
        private int position;

        public RecipeHolder(@NonNull View itemView) {
            super(itemView);
            recipe_name = itemView.findViewById(R.id.recipe_name);
            recipe_update_time = itemView.findViewById(R.id.recipe_update_time);
            recipe_pic = itemView.findViewById(R.id.recipe_pic);
            recipe_class=itemView.findViewById(R.id.recipe_class);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (RecipeClickListener != null) {
                        RecipeClickListener.RecipeListItemClick(recipeList.get(position));
                    }
                }
            });
        }

        public void update(Recipe item, int i) {
            position = i;
            GlideX.getInstance().loadImage(context,item.getFood().getFoodURL(),recipe_pic);
            recipe_name.setText(item.getFood().getFoodName());
            recipe_update_time.setText(item.getFood().getDate());
            recipe_class.setText(item.getFood().getFoodType());
        }
    }

    public interface RecipeClickListener {
        void RecipeListItemClick(Recipe info);
    }
}
