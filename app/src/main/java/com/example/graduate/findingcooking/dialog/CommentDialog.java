package com.example.graduate.findingcooking.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.graduate.findingcooking.R;

public class CommentDialog extends Dialog {
    private CommentFinishListener commentFinishListener;
    private Context context;

    public CommentDialog(@NonNull Context context, @NonNull CommentFinishListener commentFinishListener) {
        super(context, R.style.dialogStyle);
        this.context = context;
        this.commentFinishListener = commentFinishListener;
        initDialogParam();
    }

    private void initDialogParam() {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context, R.layout.dialog_publish_comment, null);
        EditText et_comment = view.findViewById(R.id.et_comment);
        TextView comment_publish = view.findViewById(R.id.comment_publish);
        comment_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentFinishListener != null) {
                    commentFinishListener.commentFinish(et_comment.getText().toString());
                    dismiss();
                }
            }
        });
        setContentView(view);
    }

    public interface CommentFinishListener {
        void commentFinish(String info);
    }
}
