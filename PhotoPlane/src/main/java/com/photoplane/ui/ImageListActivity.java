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
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.photoplane.R;
import com.photoplane.ui.adapter.ImageListAdapter;
import com.photoplane.utils.Util;

import java.util.ArrayList;
import java.util.List;

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

    /*FloatingActionButton
*2015-05-05 16:37:02 by Junsion
*/
    private int mPreviousVisibleItem;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionButton fab12;
    private FloatingActionButton fab22;
    private FloatingActionButton fab32;
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();


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

                /*FloatingActionButton
        *2015-05-05 16:37:02 by Junsion
        */
        final FloatingActionMenu menu1 = (FloatingActionMenu) findViewById(R.id.menu1);
        menus.add(menu1);
        menu1.hideMenuButton(false);
        int delay = 400;
        for (final FloatingActionMenu menu : menus) {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menu.showMenuButton(true);
                }
            }, delay);
            delay += 150;
        }
        menu1.setClosedOnTouchOutside(true);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);
        fab3.setOnClickListener(clickListener);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                menu1.showMenuButton(true);
                menu1.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(ImageListActivity.this, R.anim.show_from_bottom));
                menu1.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(ImageListActivity.this, R.anim.hide_to_bottom));
            }
        }, 300);
        mImagesGv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > mPreviousVisibleItem) {
                    menu1.hideMenuButton(true);
                } else if (firstVisibleItem < mPreviousVisibleItem) {
                    menu1.showMenuButton(true);
                }
                mPreviousVisibleItem = firstVisibleItem;
            }
        });
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
    /*FloatingActionButton
        *2015-05-05 16:37:02 by Junsion
        */
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = "";

            switch (v.getId()) {
                case R.id.fab1:
                    text = fab1.getLabelText();
                    break;
                case R.id.fab2:
                    text = fab2.getLabelText();
                    break;
                case R.id.fab3:
                    text = fab3.getLabelText();
                    break;
            }
            Toast.makeText(ImageListActivity.this, text, Toast.LENGTH_SHORT).show();
        }
    };
}
