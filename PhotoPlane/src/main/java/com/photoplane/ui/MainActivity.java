/**
 * MainActivity.java
 * ImageChooser
 * 
 * Created by photoplane on 2014-4-22
 * Copyright (c) 1998-2014 http://photoplane.github.io/ All rights reserved.
 */

package com.photoplane.ui;


import android.content.Intent;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.photoplane.R;
import com.photoplane.listener.OnTaskResultListener;
import com.photoplane.model.ImageGroup;
import com.photoplane.task.ImageLoadTask;
import com.photoplane.ui.adapter.ImageGroupAdapter;
import com.photoplane.utils.SDcardUtil;
import com.photoplane.utils.TaskUtil;
import com.photoplane.widget.LoadingLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * 图片选择主界面，列出所有图片文件夹
 *
 * @author photoplane
 */
public class MainActivity extends ActionBarActivity implements OnItemClickListener {
    /**
     * loading布局
     */
    private LoadingLayout mLoadingLayout = null;

    /**
     * 图片组GridView
     */
    private GridView mGroupImagesGv = null;

    /**
     * 适配器
     */
    private ImageGroupAdapter mGroupAdapter = null;

    /**
     * 图片扫描一般任务
     */
    private ImageLoadTask mLoadTask = null;

    /*FloatingActionButton
*2015-05-05 16:37:02 by Junsion
*/
    private int mPreviousVisibleItem;
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();

    //dialog




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        有bug没解决 2015-05-06 00:31:12 by junsion
         */
//        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        setContentView(R.layout.activity_main);
        initView();
        loadImages();

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                menu1.showMenuButton(true);
                menu1.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.show_from_bottom));
                menu1.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.hide_to_bottom));
            }
        }, 300);
        mGroupImagesGv.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        mLoadingLayout = (LoadingLayout)findViewById(R.id.loading_layout);
        mGroupImagesGv = (GridView)findViewById(R.id.images_gv);
    }

    /**
     * 加载图片
     */
    private void loadImages() {
        mLoadingLayout.showLoading(true);
        if (!SDcardUtil.hasExternalStorage()) {
            mLoadingLayout.showEmpty(getString(R.string.donot_has_sdcard));
            return;
        }

        // 线程正在执行
        if (mLoadTask != null && mLoadTask.getStatus() == Status.RUNNING) {
            return;
        }

        mLoadTask = new ImageLoadTask(this, new OnTaskResultListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResult(boolean success, String error, Object result) {
                mLoadingLayout.showLoading(false);
                // 如果加载成功
                if (success && result != null && result instanceof ArrayList) {
                    setImageAdapter((ArrayList<ImageGroup>)result);
                } else {
                    // 加载失败，显示错误提示
                    mLoadingLayout.showFailed(getString(R.string.loaded_fail));
                }
            }
        });
        TaskUtil.execute(mLoadTask);
    }

    /**
     * 构建GridView的适配器
     * 
     * @param data
     */
    private void setImageAdapter(ArrayList<ImageGroup> data) {
        if (data == null || data.size() == 0) {
            mLoadingLayout.showEmpty(getString(R.string.no_images));
        }
        mGroupAdapter = new ImageGroupAdapter(this, data, mGroupImagesGv);
        mGroupImagesGv.setAdapter(mGroupAdapter);
        mGroupImagesGv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
        ImageGroup imageGroup = mGroupAdapter.getItem(position);
        if (imageGroup == null) {
            return;
        }
        ArrayList<String> childList = imageGroup.getImages();
        Intent mIntent = new Intent(MainActivity.this, ImageListActivity.class);
        mIntent.putExtra(ImageListActivity.EXTRA_TITLE, imageGroup.getDirName());
        mIntent.putStringArrayListExtra(ImageListActivity.EXTRA_IMAGES_DATAS, childList);
        startActivity(mIntent);
    }
    /*FloatingActionButton
            *2015-05-05 16:37:02 by Junsion
            */
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = "";

            Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
        }
    };

}