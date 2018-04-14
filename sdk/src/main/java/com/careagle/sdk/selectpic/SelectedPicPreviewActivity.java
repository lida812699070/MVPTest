package com.careagle.sdk.selectpic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.careagle.sdk.R;
import com.careagle.sdk.base.BasePresenter;
import com.careagle.sdk.base.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedPicPreviewActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;
    private ViewPager viewPager;
    private ArrayList<String> list;//所有数据
    private ArrayList<Integer> selectedList;//已经被选中
    private SparseArray<Boolean> selectMap;//是否选中列表
    private int selectedCount;
    private TextView tvFinished;
    private int currPosition;
    private int picCount;
    private ImageView ivToolbarRight;
    private IntruderViewPagerAdapter viewPagerAdapter;
    private int maxPicCount;

    @Override
    public int getLayoutId() {
        return R.layout.activity_selected_pic_preview;
    }

    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById();
        Intent intent = getIntent();
        list = (ArrayList<String>) intent.getSerializableExtra("data");
        selectedList = (ArrayList<Integer>) intent.getSerializableExtra("selectedData");
//        boolean isPreview = intent.getBooleanExtra("isPreview", false);
//        if (!isPreview) {
//            tvRotate.setVisibility(View.INVISIBLE);
//            tvSmear.setVisibility(View.INVISIBLE);
//            tvMark.setVisibility(View.INVISIBLE);
//        }
        selectMap = new SparseArray<>();
        for (int i = 0; i < selectedList.size(); i++) {
            selectMap.put(selectedList.get(i), true);
        }
        selectedCount = selectedList.size();
        tvFinished.setText("完成(" + selectedList.size() + ")");
        currPosition = intent.getIntExtra("position", -1);
        maxPicCount = intent.getIntExtra(SelectPicActivity.PARAM_MAX_PIC_COUNT, 9);
        picCount = intent.getIntExtra("picCount", 0);
        initToolbar(currPosition + 1 + "/" + list.size());
        if (selectMap.get(0) != null && selectMap.get(0)) {
            ivToolbarRight.setImageResource(R.drawable.common_button_multiselect_l_selected_3x);
        } else {
            ivToolbarRight.setImageResource(R.drawable.common_button_multiselect_l_unselected_3x);
        }
        ivToolbarRight.setVisibility(View.VISIBLE);
        viewPagerAdapter = new IntruderViewPagerAdapter(this, list);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvTitle.setText(position + 1 + "/" + list.size());
                if (selectMap.get(position) != null && selectMap.get(position)) {
                    ivToolbarRight.setImageResource(R.drawable.common_button_multiselect_l_selected_3x);
                    ivToolbarRight.setSelected(true);
                } else {
                    ivToolbarRight.setImageResource(R.drawable.common_button_multiselect_l_unselected_3x);
                    ivToolbarRight.setSelected(false);
                }
                currPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(currPosition);
    }

    private void findViewById() {
        ivBack = findViewById(R.id.iv_back);
        ivToolbarRight = findViewById(R.id.iv_toolbar_right);
        tvTitle = findViewById(R.id.tv_title);
        tvFinished = findViewById(R.id.tv_finished);
        viewPager = findViewById(R.id.iv_viewPager);
        ivBack.setOnClickListener(this);
        ivToolbarRight.setOnClickListener(this);
        tvFinished.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_back) {
            finish();
        } else if (view.getId() == R.id.tv_finished) {
            EventBus.getDefault().post(selectMap);
            finish();
        } else if (view.getId() == R.id.iv_toolbar_right) {
            if (ivToolbarRight.isSelected()) {
                ivToolbarRight.setSelected(false);
                ivToolbarRight.setImageResource(R.drawable.common_button_multiselect_l_unselected_3x);
                selectedCount--;
                selectMap.put(currPosition, false);
            } else {
                if (selectedCount >= maxPicCount - picCount) {
                    showMessage("照片数量不能大于" + maxPicCount);
                    return;
                }
                ivToolbarRight.setSelected(true);
                ivToolbarRight.setImageResource(R.drawable.common_button_multiselect_l_selected_3x);
                selectMap.put(currPosition, true);
                selectedCount++;
            }
            if (selectedCount != 0) {
                tvFinished.setText("完成(" + selectedCount + ")");
            } else {
                tvFinished.setText("完成");
            }
        }
    }
}
