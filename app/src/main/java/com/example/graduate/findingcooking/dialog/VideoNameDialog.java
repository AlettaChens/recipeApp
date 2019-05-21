package com.example.graduate.findingcooking.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.graduate.findingcooking.R;


public class VideoNameDialog extends Dialog {
    private RenameHandle renameFinish;
    private Context context;


    public VideoNameDialog(@NonNull Context context, RenameHandle renameFinish) {
        super(context, R.style.dialogStyle);
        this.renameFinish = renameFinish;
        this.context = context;
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
        View view = View.inflate(context, R.layout.dialog_video_name, null);
        final EditText content_ed = view.findViewById(R.id.content_ed);
        Button negative_btn = view.findViewById(R.id.negative_btn);
        Button positive_btn = view.findViewById(R.id.positive_btn);
        negative_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        positive_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (renameFinish != null) {
                    renameFinish.renameFinish(content_ed.getText().toString());
                }
                dismiss();
            }
        });
        setContentView(view);
    }

    public interface RenameHandle {
        void renameFinish(String info);
    }
}
