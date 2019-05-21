package com.example.graduate.findingcooking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.bean.Recipe;
import com.example.graduate.findingcooking.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int BANNER_VIEW_TYPE = 0;//轮播图
    private final int CLASSIFY_VIEW_TYPE = 1;//分类
    private final int RECOMMEND_VIEW_TYPE = 2;//推荐

    private List<Recipe> recommendList;
    private Context context;
    private List<Integer> picList;
    private ClassifyClickListener classifyClickListener;

    private RecommendClickListener recommendClickListener;

    public HomeAdapter(List<Recipe> recommendList, Context context, List<Integer> picList, ClassifyClickListener classifyClickListener, RecommendClickListener recommendClickListener) {
        this.recommendList = recommendList;
        this.context = context;
        this.picList = picList;
        this.classifyClickListener = classifyClickListener;
        this.recommendClickListener = recommendClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return BANNER_VIEW_TYPE;
        } else if (position == 1) {
            return CLASSIFY_VIEW_TYPE;
        } else {
            return RECOMMEND_VIEW_TYPE;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof BannerHolder) {
            BannerHolder bannerHolder = (BannerHolder) viewHolder;
            setBanner(bannerHolder);
        } else if (viewHolder instanceof ClassifyHolder) {
            ClassifyHolder classifyHolder = (ClassifyHolder) viewHolder;
            classifyHolder.classifyEvent();
        } else {
            RecommendHolder recommendHolder = (RecommendHolder) viewHolder;
            recommendHolder.update(recommendList.get(i - 2), i - 2);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == BANNER_VIEW_TYPE) {
            view = LayoutInflater.from(context).inflate(R.layout.section_home_head_banner, parent, false);
            return new BannerHolder(view);
        } else if (viewType == CLASSIFY_VIEW_TYPE) {
            view = LayoutInflater.from(context).inflate(R.layout.section_home_info_classify, parent, false);
            return new ClassifyHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_recipe_list, parent, false);
            return new RecommendHolder(view);
        }
    }


    @Override
    public int getItemCount() {
        return recommendList.size() + 2;
    }


    private void setBanner(BannerHolder bannerHolder) {
        bannerHolder.vp_banner.setImageLoader(new GlideImageLoader());
        bannerHolder.vp_banner.setImages(picList);
        bannerHolder.vp_banner.setBannerAnimation(Transformer.Default);
        bannerHolder.vp_banner.isAutoPlay(true);
        bannerHolder.vp_banner.setDelayTime(2000);
        bannerHolder.vp_banner.start();
    }

    public class RecommendHolder extends RecyclerView.ViewHolder {
        private TextView recipe_name;
        private CircleImageView recipe_pic;
        private TextView recipe_update_time;
        private int position;

        public RecommendHolder(@NonNull View itemView) {
            super(itemView);
            recipe_name = itemView.findViewById(R.id.recipe_name);
            recipe_update_time = itemView.findViewById(R.id.recipe_update_time);
            recipe_pic = itemView.findViewById(R.id.recipe_pic);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recommendClickListener != null) {
                        recommendClickListener.RecommendListItemClick(recommendList.get(position));
                    }
                }
            });
        }

        public void update(Recipe item, int i) {
            position = i;
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.default_recipe_pic)
                    .error(R.mipmap.default_recipe_pic);
            Glide.with(context).load(item.getFood().getFoodURL()).apply(options).into(recipe_pic);
            recipe_name.setText(item.getFood().getFoodName());
            recipe_update_time.setText(item.getFood().getDate());
        }
    }


    public class BannerHolder extends RecyclerView.ViewHolder {
        private Banner vp_banner;

        public BannerHolder(View itemView) {
            super(itemView);
            vp_banner = itemView.findViewById(R.id.vp_banner);
        }
    }

    public class ClassifyHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_city;
        private LinearLayout ll_all;
        private LinearLayout ll_timer;
        private LinearLayout ll_video;


        public ClassifyHolder(@NonNull View itemView) {
            super(itemView);
            ll_city = itemView.findViewById(R.id.ll_city);
            ll_all = itemView.findViewById(R.id.ll_all);
            ll_timer = itemView.findViewById(R.id.ll_timer);
            ll_video = itemView.findViewById(R.id.ll_video);
        }

        public void classifyEvent() {
            ll_city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (classifyClickListener != null) {
                        classifyClickListener.onCityClick();
                    }
                }
            });
            ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (classifyClickListener != null) {
                        classifyClickListener.onAllClick();
                    }
                }
            });
            ll_timer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (classifyClickListener != null) {
                        classifyClickListener.onActiveClick();
                    }
                }
            });
            ll_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (classifyClickListener != null) {
                        classifyClickListener.onVideoClick();
                    }
                }
            });
        }
    }

    public interface ClassifyClickListener {
        void onCityClick();

        void onAllClick();

        void onActiveClick();

        void onVideoClick();
    }

    public interface RecommendClickListener {
        void RecommendListItemClick(Recipe info);
    }

}
