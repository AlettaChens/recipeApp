package com.example.graduate.findingcooking.adapter;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.bean.NotificationEvent;
import com.example.graduate.findingcooking.utils.DateFormatUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventView> implements View.OnClickListener, View.OnLongClickListener {

    private List<NotificationEvent> mList = new ArrayList<>();

    public void update(List<NotificationEvent> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public EventView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timer_notification, parent, false);
        view.setOnClickListener(this);
        view.setLongClickable(true);
        view.setOnLongClickListener(this);
        return new EventView(view);
    }

    @Override
    public void onBindViewHolder(EventView holder, int position) {
        NotificationEvent notificationEvent = mList.get(position);
        if (notificationEvent.getTime() != null) {
            holder.tv_start_time.setText(DateFormatUtil.transTime(notificationEvent.getTime()));
        }
        holder.tv_notification_content.setText(notificationEvent.getContent());
        holder.itemView.setTag(notificationEvent);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (NotificationEvent) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemLongClick(v, (NotificationEvent) v.getTag());
        }
        return true;
    }

    public static class DiaryDecoration extends RecyclerView.ItemDecoration {

        private int width;
        private int height;

        public DiaryDecoration(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = width;
            outRect.right = width;
            outRect.top = height;
            outRect.bottom = height;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = height * 2;
            }
        }
    }

    class EventView extends RecyclerView.ViewHolder {

        private TextView tv_start_time;
        private TextView tv_notification_content;

        public EventView(@NonNull View itemView) {
            super(itemView);
            tv_start_time = itemView.findViewById(R.id.tv_start_time);
            tv_notification_content = itemView.findViewById(R.id.tv_notification_content);
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, NotificationEvent data);

        void onItemLongClick(View view, NotificationEvent tag);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
