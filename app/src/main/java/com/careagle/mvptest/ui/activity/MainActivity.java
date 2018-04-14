package com.careagle.mvptest.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.careagle.mvptest.R;
import com.careagle.mvptest.global.G;
import com.careagle.sdk.Config;
import com.careagle.sdk.base.BasePresenter;
import com.careagle.sdk.base.activity.BaseMVPActivity;
import com.careagle.sdk.selectpic.SelectPicActivity;
import com.careagle.sdk.utils.FileUtils;
import com.careagle.sdk.utils.PhotoUtils;
import com.soundcloud.android.crop.Crop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseMVPActivity {

    private static final int TAKE_PHOTO = 1;
    private static final int CODE_RESULT_REQUEST = 2;
    private static final int SELECT_PIC_MORE = 3;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_toolbar_subtitle)
    TextView tvToolbarSubtitle;
    @BindView(R.id.base_toolbar)
    Toolbar baseToolbar;
    @BindView(R.id.iv)
    ImageView iv;
    private Uri imageUri;
    private File file;
    private File fileCropUri;
    private Uri imageCropUri;

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolbar("MVPTest");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @OnClick({R.id.tv_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title:
                Intent intent = new Intent(this, SelectPicActivity.class);
                intent.putExtra(SelectPicActivity.PARAM_MAX_PIC_COUNT, 3);
                startActivityForResult(intent, SELECT_PIC_MORE);
//                showSelectDialog(new MyTakePhotoCallBack() {
//                    @Override
//                    public void takePhoto() {
//                        takePic();
//                    }
//
//                    @Override
//                    public void selectPhoto() {
//                        selectPicBySD();
//                    }
//                });
                break;
        }
    }

    //选择图片
    public void selectPicBySD() {
        PhotoUtils.openPic(this, 2);
    }

    private void takePic() {
        file = new File(Config.getFileRootPath(), "/temp/" + System.currentTimeMillis() + ".jpg");
        imageUri = FileUtils.file2Uri(file, G.FILE_PROVIDER_AUTHORITY);
        PhotoUtils.takePicture(this, imageUri, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
//                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(imageUri, this);
//                    JLog.e(bitmap.getHeight() + "---" + bitmap.getWidth());
//                    Bitmap image = MyBitmapUtils.getImage(file.getPath(), 1600, 1200);
//                    JLog.e(image.getHeight() + "---" + image.getWidth());
//                    if (bitmap != null) {
//                        iv.setImageBitmap(bitmap);
//                    }
                    fileCropUri = new File(Config.getFileRootPath(), System.currentTimeMillis() + ".jpg");
                    if (!fileCropUri.getParentFile().exists()) fileCropUri.getParentFile().mkdirs();
                    imageCropUri = Uri.fromFile(fileCropUri);
//                    PhotoUtils.cropImageUri(this, imageUri, imageCropUri, 1, 1, 3968, 500, CODE_RESULT_REQUEST);
                    Crop.of(imageUri, imageCropUri).withAspect(4, 3).start(this);
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(data.getData(), this);
                    if (bitmap != null) {
                        iv.setImageBitmap(bitmap);
                    }

                    break;
                case Crop.REQUEST_CROP:
                    bitmap = PhotoUtils.getBitmapFromUri(imageCropUri, this);
                    if (bitmap != null) {
                        iv.setImageBitmap(bitmap);
                    }
                    break;
                case SELECT_PIC_MORE:
//                    ArrayList<String> list = (ArrayList<String>) data.getSerializableExtra("data");
//                    JLog.e("list");
                    break;
            }
        }
    }
}
