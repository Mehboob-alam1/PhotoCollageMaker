package com.helloworld.photoeditor.bitmap;

import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BitmapResizer {


    public static Point decodeFileSize(File f, int requiredSize) {
        try {
            Options o = new Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            int REQUIRED_SIZE = requiredSize;
            int width_tmp = o.outWidth;
            int height_tmp = o.outHeight;
            int scale = 1;
            while (Math.max(width_tmp, height_tmp) / 2 > REQUIRED_SIZE) {
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            if (scale == 1) {
                return new Point(-1, -1);
            }
            return new Point(width_tmp, height_tmp);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

}
