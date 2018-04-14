package com.careagle.sdk.selectpic;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.careagle.sdk.R;
import com.careagle.sdk.utils.ImageLoadManager;
import com.careagle.sdk.utils.MyToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.careagle.sdk.selectpic.SelectPicActivity.PARAM_MAX_PIC_COUNT;

/**
 * Created by admin on 2017/8/21.
 */

public class SelectPicAdapter extends RecyclerView.Adapter<SelectPicAdapter.MyViewHolder> {
    private ArrayList<String> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private AdapterListener listener;
    private ArrayList<Integer> selectIndex = new ArrayList<>();
    private int picCount;
    private int maxPicCount;

    public void setPicCount(int picCount) {
        this.picCount = picCount;
    }

    public void setMaxPicCount(int maxPicCount) {
        this.maxPicCount = maxPicCount;
    }

    public interface AdapterListener {
        void onCheckChangeListener(int selectCount);
    }

    public void setAdapterListener(AdapterListener listener) {
        this.listener = listener;
    }

    public SelectPicAdapter(Context context, ArrayList<String> datas) {
        this.mContext = context;
        this.mDatas = datas;
        inflater = LayoutInflater.from(mContext);
    }

    public List<Integer> getSelectIndex() {
        return selectIndex;
    }

    public ArrayList<String> getSelectPaths() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < selectIndex.size(); i++) {
            list.add(mDatas.get(selectIndex.get(i)));
        }
        return list;
    }

    public void setSelectIndex(ArrayList<Integer> selectIndex) {
        this.selectIndex = selectIndex;
        listener.onCheckChangeListener(selectIndex.size());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(final SelectPicAdapter.MyViewHolder holder, final int position) {
        String path = mDatas.get(position);
        ImageLoadManager.load(new File(path), holder.imageView);
        if (selectIndex.contains(position)) {
            holder.ivFlag.setChecked(true);
            holder.shaDow.setVisibility(View.VISIBLE);
        } else {
            holder.ivFlag.setChecked(false);
            holder.shaDow.setVisibility(View.GONE);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SelectedPicPreviewActivity.class);
                intent.putExtra("data", mDatas);
                intent.putExtra("selectedData", selectIndex);
                intent.putExtra(PARAM_MAX_PIC_COUNT, maxPicCount);
                intent.putExtra("position", position);
                intent.putExtra("picCount", picCount);
                mContext.startActivity(intent);
            }
        });
        holder.ivFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectIndex.contains(position)) {
                    if (selectIndex.size() >= (maxPicCount - picCount)) {
                        MyToast.toast("照片数量不能大于" + maxPicCount);
                        holder.ivFlag.setChecked(false);
                        holder.shaDow.setVisibility(View.GONE);
                        return;
                    }
                    selectIndex.add(position);
                    holder.ivFlag.setChecked(true);
                    holder.shaDow.setVisibility(View.VISIBLE);
                } else {
                    selectIndex.remove(Integer.valueOf(position));
                    holder.ivFlag.setChecked(false);
                    holder.shaDow.setVisibility(View.GONE);
                }

                if (listener != null) {
                    listener.onCheckChangeListener(selectIndex.size());
                }
            }
        });
    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public SelectPicAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_select_pic, parent, false);
        SelectPicAdapter.MyViewHolder holder = new SelectPicAdapter.MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CheckBox ivFlag;
        View shaDow;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.iv_item_pic);
            ivFlag = (CheckBox) view.findViewById(R.id.iv_check_flag);
            shaDow = view.findViewById(R.id.view_shadow);

        }

    }
}