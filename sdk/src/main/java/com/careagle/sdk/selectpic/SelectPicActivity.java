package com.careagle.sdk.selectpic;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.careagle.sdk.R;
import com.careagle.sdk.base.BasePresenter;
import com.careagle.sdk.base.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SelectPicActivity extends BaseActivity implements View.OnClickListener {

    public static final String PARAM_CURRENT_PIC_COUNT = "currentPicCount";
    public static final String PARAM_MAX_PIC_COUNT = "maxPicCount";
    private TextView tvCancel;
    private RecyclerView recyclerView;
    private TextView tvFinished;
    private ArrayList<String> fileNames;
    //要选择几张照片
    private int picCount;
    private SelectPicAdapter adapter;
    private TextView tvPreview;
    private int maxPicCount;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_pic;
    }

    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById();
        initRecyclerView();
        EventBus.getDefault().register(this);
    }

    private void initRecyclerView() {
        getAllImgInSDCard();
        picCount = getIntent().getIntExtra(PARAM_CURRENT_PIC_COUNT, 0);
        maxPicCount = getIntent().getIntExtra(PARAM_MAX_PIC_COUNT, 9);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new SelectPicAdapter(this, fileNames);
        adapter.setPicCount(picCount);
        adapter.setMaxPicCount(maxPicCount);
        recyclerView.setAdapter(adapter);
        adapter.setAdapterListener(new SelectPicAdapter.AdapterListener() {
            @Override
            public void onCheckChangeListener(int selectCount) {
                if (selectCount != 0) {
                    tvPreview.setTextColor(getResources().getColor(R.color.text_title));
                    tvFinished.setText("完成(" + selectCount + ")");
                    tvPreview.setClickable(true);
                } else {
                    tvPreview.setTextColor(getResources().getColor(R.color.text_gray));
                    tvFinished.setText("完成");
                    tvPreview.setClickable(false);
                }
            }
        });
    }

    private void findViewById() {
        tvCancel = findViewById(R.id.tv_cancel);
        tvFinished = findViewById(R.id.tv_finished);
        tvPreview = findViewById(R.id.tv_preview);
        recyclerView = findViewById(R.id.recyclerView);
        tvCancel.setOnClickListener(this);
        tvPreview.setOnClickListener(this);
        tvFinished.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        //取消
        if (view.getId() == R.id.tv_cancel) {
            finish();
        } else if (view.getId() == R.id.tv_finished) {
            //完成
            ArrayList<String> selectPaths = adapter.getSelectPaths();
            Intent intent = new Intent();
            intent.putExtra("data", selectPaths);
            setResult(RESULT_OK, intent);
            finish();
        } else if (view.getId() == R.id.tv_preview) {
            //预览
            if (adapter.getSelectIndex().size() == 0) {
                return;
            }
            preview();
        }
    }

    //查询手机内的所有图片资源
    public void getAllImgInSDCard() {
        fileNames = new ArrayList();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            //获取图片的名称
//            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            //获取图片的数据
            byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //获取图片的详细信息
//            String desc = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION));
            fileNames.add(new String(data, 0, data.length - 1));
        }
        Collections.reverse(fileNames); // 倒序排
    }

    private void preview() {
        List<Integer> selectIndex = adapter.getSelectIndex();
        ArrayList<String> selectedPaths = new ArrayList<>();
        ArrayList<Integer> selectedPosition = new ArrayList<>();
        int i = 0;
        for (Integer integer : selectIndex) {
            selectedPaths.add(fileNames.get(integer));
            selectedPosition.add(i++);
        }
        Intent intent = new Intent(this, SelectedPicPreviewActivity.class);
        intent.putExtra("data", selectedPaths);
        intent.putExtra("selectedData", selectedPosition);
        intent.putExtra(PARAM_MAX_PIC_COUNT, maxPicCount);
        intent.putExtra("picCount", picCount);
        intent.putExtra("position", 0);
        intent.putExtra("isPreview", true);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateSelectAdapter(SparseArray<Boolean> selectMap) {
        ArrayList<Integer> selectList = new ArrayList<>();
        for (int i = 0; i < selectMap.size(); i++) {
            if (selectMap.valueAt(i)) {
                selectList.add(selectMap.keyAt(i));
            }
        }
        adapter.setSelectIndex(selectList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
