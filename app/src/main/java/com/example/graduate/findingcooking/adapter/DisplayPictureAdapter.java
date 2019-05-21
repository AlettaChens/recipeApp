package com.example.graduate.findingcooking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.bean.Image;
import com.example.graduate.findingcooking.utils.GlideX;

import java.util.List;

public class DisplayPictureAdapter extends RecyclerView.Adapter<DisplayPictureAdapter.DisplayHolder> {
    private Context context;
    private List<Image> picURL;
    private DisplayClickListener displayClickListener;

    public DisplayPictureAdapter(Context context, List<Image> picURL, @NonNull DisplayClickListener displayClickListener) {
        this.context = context;
        this.picURL = picURL;
        this.displayClickListener = displayClickListener;
    }

    @NonNull
    @Override
    public DisplayHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_picture_dispaly, viewGroup, false);
        return new DisplayHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayHolder displayHolder, int i) {
        displayHolder.update(picURL.get(i), i);
    }

    @Override
    public int getItemCount() {
        return picURL.size();
    }

    public class DisplayHolder extends RecyclerView.ViewHolder {
        private ImageView display_image;
        private int position;

        public DisplayHolder(@NonNull View itemView) {
            super(itemView);
            display_image = itemView.findViewById(R.id.display_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayClickListener.showDisplay(picURL.get(position).getPictureURL());
                }
            });
        }

        public void update(Image item, int i) {
            position = i;
            GlideX.getInstance().loadImage(context, item.getPictureURL(), display_image);
        }
    }

    public interface DisplayClickListener {
        void showDisplay(String url);
    }
}
