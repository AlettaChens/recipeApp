package com.example.graduate.findingcooking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.bean.Comment;
import java.util.List;


public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentHolder> {
    private Context context;
    private List<Comment> commentList;

    public CommentListAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment_list, viewGroup, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder commentHolder, int i) {
        commentHolder.update(commentList.get(i), i);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        private TextView comment_user;
        private TextView comment_content;
        private TextView comment_time;
        private int position;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            comment_user = itemView.findViewById(R.id.comment_user);
            comment_content = itemView.findViewById(R.id.comment_content);
            comment_time = itemView.findViewById(R.id.comment_time);
        }

        public void update(Comment item, int i) {
            position = i;
            comment_user.setText(item.getComment_user());
            comment_content.setText(item.getContent());
            comment_time.setText(item.getDate());
        }
    }
}
