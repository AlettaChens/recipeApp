package com.example.graduate.findingcooking.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.adapter.EventAdapter;
import com.example.graduate.findingcooking.base.BaseActivity;
import com.example.graduate.findingcooking.bean.NotificationEvent;
import com.example.graduate.findingcooking.dialog.NotificationDialog;
import com.example.graduate.findingcooking.utils.CalendarEventUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class TimerListActivity extends BaseActivity {
    @BindView(R.id.backend)
    ImageView backend;
    @BindView(R.id.rv_timer)
    RecyclerView rvTimer;
    EventAdapter eventAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_timer_list;
    }

    @Override
    public void onInit() {
        eventAdapter = new EventAdapter();
        rvTimer.setLayoutManager(new LinearLayoutManager(TimerListActivity.this));
        rvTimer.addItemDecoration(new EventAdapter.DiaryDecoration(20, 10));
        rvTimer.setAdapter(eventAdapter);
        eventAdapter.update(CalendarEventUtils.queryEvents(TimerListActivity.this));
    }

    @Override
    public void onBindData() {

        eventAdapter.setOnItemClickListener(new EventAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, NotificationEvent data) {
                NotificationDialog eventDialog = new NotificationDialog(TimerListActivity.this, data);
                eventDialog.show();
                eventDialog.setOnUpdateListener(new NotificationDialog.OnUpdateListener() {
                    @Override
                    public void onUpdate(boolean isNew, NotificationEvent notificationEvent) {
                        if (!isNew) {
                            CalendarEventUtils.updateEvent(notificationEvent, TimerListActivity.this);
                            eventAdapter.update(CalendarEventUtils.queryEvents(TimerListActivity.this));
                        }
                    }
                });
            }

            @Override
            public void onItemLongClick(View view, final NotificationEvent tag) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TimerListActivity.this);
                builder.setMessage("是否删除？")
                        .setCancelable(false)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CalendarEventUtils.deleteEvent(TimerListActivity.this, tag.getId());
                                eventAdapter.update(CalendarEventUtils.queryEvents(TimerListActivity.this));
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).create().show();
            }
        });

    }

    @OnClick(R.id.backend)
    public void onClick() {
        onBackPressed();
    }
}
