package com.and2long.facecompare.view;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.and2long.facecompare.GlideApp;
import com.and2long.facecompare.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class CompareActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvResult;
    private ProgressBar pb;
    private ImageView iv1;
    private ImageView iv2;
    private File IMAGE_FILE1;
    private File IMAGE_FILE2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        //初始化图片地址
        IMAGE_FILE1 = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "pic1.jpg");
        IMAGE_FILE2 = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "pic2.jpg");

        setTitle("人脸对比");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);
        Button btnComapare = findViewById(R.id.btn_compare);
        btnComapare.setOnClickListener(this);
        tvResult = findViewById(R.id.tv_result);
        pb = findViewById(R.id.pb);

        loadImages();
    }

    /**
     * 加载图片
     */
    private void loadImages() {
        GlideApp.with(this).load(IMAGE_FILE1).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(iv1);
        GlideApp.with(this).load(IMAGE_FILE2).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(iv2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_compare:
                pb.setVisibility(View.VISIBLE);
                compress();
                break;
            default:

                break;
        }
    }

    private static final String TAG = "CompareActivity";

    private void compress() {
        ArrayList<String> photos = new ArrayList<>();
        photos.add(IMAGE_FILE1.getAbsolutePath());
        photos.add(IMAGE_FILE2.getAbsolutePath());
        final ArrayList<File> results = new ArrayList<>();
        Luban.with(this)
                .load(photos)
                .setTargetDir(getCacheDir().getAbsolutePath())
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        results.add(file);
                        if (results.size() == 2) {
                            compare(results);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        pb.setVisibility(View.GONE);
                    }
                }).launch();
    }

    private void compare(List<File> fileList) {
        OkGo.<String>post("https://api-cn.faceplusplus.com/facepp/v3/compare")
                .params("api_key", "qQHuZbTMKCtpAg_SQP9_SsvtGRqs_xpF")
                .params("api_secret", "ICiGq1LPceMx_tUrsDt9wzyys271e3K8")
                .params("image_file1", fileList.get(0))
                .params("image_file2", fileList.get(1))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        pb.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.has("confidence")) {
                                double confidence = (double) jsonObject.get("confidence");
                                tvResult.setText("对比相似度：" + confidence);
                            } else {
                                tvResult.setText("至少一张图片中未检测到人脸！");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
    }
}
