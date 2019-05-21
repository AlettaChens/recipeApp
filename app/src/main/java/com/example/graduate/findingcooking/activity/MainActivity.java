package com.example.graduate.findingcooking.activity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.base.BaseFragmentActivity;
import com.example.graduate.findingcooking.fragment.ClassFragment;
import com.example.graduate.findingcooking.fragment.HomeFragment;
import com.example.graduate.findingcooking.fragment.PersonFragment;
import butterknife.BindView;
import butterknife.OnCheckedChanged;


public class MainActivity extends BaseFragmentActivity {
    @BindView(R.id.fl_exhibition_view)
    FrameLayout flExhibitionView;
    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_talk)
    RadioButton rbTalk;
    @BindView(R.id.rb_person)
    RadioButton rbPerson;
    @BindView(R.id.rg_tab)
    RadioGroup rgTab;
    @BindView(R.id.ll_bottom_tab)
    LinearLayout llBottomTab;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onBindData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showFragment(R.id.fl_exhibition_view, new HomeFragment());
    }

    @OnCheckedChanged({R.id.rb_home, R.id.rb_talk, R.id.rb_person})
    public void onFragmentCheck(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.rb_home: {
                if(ischanged){
                    showFragment(R.id.fl_exhibition_view, new HomeFragment());
                }
                break;
            }
            case R.id.rb_talk: {
                if(ischanged){
                    showFragment(R.id.fl_exhibition_view, new ClassFragment());
                }
                break;
            }
            case R.id.rb_person: {
                if(ischanged){
                    showFragment(R.id.fl_exhibition_view, new PersonFragment());
                }
                break;
            }
        }
    }
}
