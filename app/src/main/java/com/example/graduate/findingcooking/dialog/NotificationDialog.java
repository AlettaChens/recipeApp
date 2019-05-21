package com.example.graduate.findingcooking.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.bean.NotificationEvent;
import com.example.graduate.findingcooking.utils.DateFormatUtil;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NotificationDialog extends AppCompatDialog implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.tv_notification_time)
    TextView tv_time;
    @BindView(R.id.tv_content)
    EditText tv_content;
    @BindView(R.id.btn_save)
    Button btn_save;
    private OnUpdateListener mOnUpdateListener = null;

    private NotificationEvent mInfo;
    private DatePickerDialog mDateDialog;
    private TimePickerDialog mTimeDialog;
    private String mDate;

    public NotificationDialog(Context context, NotificationEvent data) {
        super(context, R.style.dialogStyle);
        this.mInfo = data;
        initDialogParam();
        init();
    }

    private void initDialogParam() {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    private void init() {
        setContentView(R.layout.dialog_notification_event);
        ButterKnife.bind(this);
        tv_content.setText(mInfo.getContent());
        if(mInfo.getTime()!=null){
            tv_time.setText(DateFormatUtil.transTime(mInfo.getTime()));
        }

    }


    private void showDateDialog() {
        if (mDateDialog == null) {
            Calendar now = Calendar.getInstance();
            mDateDialog = new DatePickerDialog(getContext()
                    , AlertDialog.THEME_DEVICE_DEFAULT_DARK, this
                    , now.get(Calendar.YEAR)
                    , now.get(Calendar.MONTH)
                    , now.get(Calendar.DAY_OF_MONTH));
        }
        mDateDialog.show();
    }

    private void showTimeDialog() {
        if (mTimeDialog == null) {
            mTimeDialog = new TimePickerDialog(getContext()
                    , AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, 0, 0, true);
        }
        mTimeDialog.show();
    }


    @OnClick(R.id.tv_notification_time)
    public void onSelectTime() {
        showDateDialog();
    }

    @OnClick(R.id.btn_save)
    public void onSave() {
        String content = tv_content.getText().toString();
        String time = DateFormatUtil.parseTime(tv_time.getText().toString());
        if (TextUtils.isEmpty(time)) {
            return;
        }
        if (mInfo.getId() == null) {
            //添加模式
            NotificationEvent notificationEvent = new NotificationEvent();
            notificationEvent.setTime(time);
            notificationEvent.setContent(content);

            if (mOnUpdateListener != null) {
                mOnUpdateListener.onUpdate(true, notificationEvent);
            }
        } else {
            //更新模式
            NotificationEvent notificationEvent = new NotificationEvent();
            notificationEvent.setId(mInfo.getId());
            notificationEvent.setTime(time);
            notificationEvent.setContent(content);

            if (mOnUpdateListener != null) {
                mOnUpdateListener.onUpdate(false, notificationEvent);
            }
        }
        dismiss();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String month = monthOfYear < 9 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1);
        String day = dayOfMonth < 10 ? ("0" + dayOfMonth) : ("" + dayOfMonth);
        mDate = year + "-" + month + "-" + day;
        showTimeDialog();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time = hourOfDay + ":" + minute;
        tv_time.setText(mDate + " " + time);
    }


    public void setOnUpdateListener(OnUpdateListener listener) {
        this.mOnUpdateListener = listener;
    }

    public interface OnUpdateListener {
        void onUpdate(boolean isNew, NotificationEvent notificationEvent);
    }
}
