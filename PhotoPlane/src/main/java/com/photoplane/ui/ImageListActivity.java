/**
 * ImageListActivity.java
 * ImageChooser
 * 
 * Created by photoplane on 2014-4-23
 * Copyright (c) 1998-2014 http://photoplane.github.io/ All rights reserved.
 */

package com.photoplane.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.photoplane.R;
import com.photoplane.ui.adapter.ImageListAdapter;
import com.photoplane.utils.Util;

import java.util.ArrayList;

/**
 * 某个文件夹下的所有图片列表
 * 
 * @author photoplane
 */
public class ImageListActivity extends ActionBarActivity implements OnItemClickListener {

    /**
     * title
     */
    public static final String EXTRA_TITLE = "extra_title";

    /**
     * 图片列表extra
     */
    public static final String EXTRA_IMAGES_DATAS = "extra_images";

    /**
     * 图片列表GridView
     */
    private GridView mImagesGv = null;

    /**
     * 图片地址数据源
     */
    private ArrayList<String> mImages = new ArrayList<String>();

    /**
     * 适配器
     */
    private ImageListAdapter mImageAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }

        initView();
        if (getIntent().hasExtra(EXTRA_IMAGES_DATAS)) {
            mImages = getIntent().getStringArrayListExtra(EXTRA_IMAGES_DATAS);
            setAdapter(mImages);
        }
    }

    /**
     * 初始化界面元素
     */
    private void initView() {
        mImagesGv = (GridView)findViewById(R.id.images_gv);
    }

    /**
     * 构建并初始化适配器
     * 
     * @param datas
     */
    private void setAdapter(ArrayList<String> datas) {
        mImageAdapter = new ImageListAdapter(this, datas, mImagesGv);
        mImagesGv.setAdapter(mImageAdapter);
        mImagesGv.setOnItemClickListener(this);
    }

    /*
     * (non-Javadoc)
     * @see
     * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
     * .AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent i = new Intent(this, ImageBrowseActivity.class);
        i.putExtra(ImageBrowseActivity.EXTRA_IMAGES, mImages);
        i.putExtra(ImageBrowseActivity.EXTRA_INDEX, arg2);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (mImageAdapter != null) {
            Util.saveSelectedImags(this, mImageAdapter.getSelectedImgs());
        }
        super.onBackPressed();
    }

}
