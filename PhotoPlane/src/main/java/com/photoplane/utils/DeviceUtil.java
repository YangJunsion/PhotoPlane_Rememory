/**
 * DeviceUtil.java
 * ImageChooser
 * 
 * Created by photoplane on 2014-4-24
 * Copyright (c) 1998-2014 http://photoplane.github.io/ All rights reserved.
 */

package com.photoplane.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;

/**
 * 与设备相关的工具类
 * 
 * @author photoplane
 */
public class DeviceUtil {
    private DeviceUtil() {
    }

    /**
     * 获取屏幕的宽高
     * 
     * @return
     */
    public static Point getDeviceSize(Context ctx) {
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        Point size = new Point();
        size.x = dm.widthPixels;
        size.y = dm.heightPixels;
        return size;
    }
}
