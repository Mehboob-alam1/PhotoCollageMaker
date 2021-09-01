package com.helloworld.photoeditor.imagesavelib;

import android.content.Context;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.helloworld.photoeditor.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class ImageUtility {
    public static int SPLASH_TIME_OUT_LONG = 0;
    static int SPLASH_TIME_OUT_MAX = 0;
    public static int SPLASH_TIME_OUT_SHORT = 0;
    private static final String TAG = "SaveImage Utility";
    public static Writer writer;


    static {
        SPLASH_TIME_OUT_LONG = 0;
        SPLASH_TIME_OUT_MAX = 0;
        SPLASH_TIME_OUT_SHORT = 0;
        SPLASH_TIME_OUT_LONG = 0;
        SPLASH_TIME_OUT_MAX = 0;
        SPLASH_TIME_OUT_SHORT = 0;
        SPLASH_TIME_OUT_SHORT = 150;
        SPLASH_TIME_OUT_LONG = 800;
        SPLASH_TIME_OUT_MAX = 1300;
    }



    public static String getPrefferredDirectoryPath(Context mContext, boolean showErrorMessage, boolean getPrefDirectoryNoMatterWhat, boolean isAppCamera) {
        String directory;
        if (isAppCamera) {
            directory = new StringBuilder(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())).append(File.separator).append(mContext.getResources().getString(R.string.directory)).toString();
        } else {
            directory = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append(mContext.getResources().getString(R.string.directory)).toString();
        }
        String prefDir = PreferenceManager.getDefaultSharedPreferences(mContext).getString("save_image_directory_custom", null);
        if (prefDir != null) {
            prefDir = new StringBuilder(String.valueOf(prefDir)).append(File.separator).toString();
            if (getPrefDirectoryNoMatterWhat) {
                return prefDir;
            }
            File dirFile = new File(prefDir);
            String finalDirectory = directory;
            if (dirFile.canRead() && dirFile.canWrite() && checkIfEACCES(prefDir)) {
                directory = prefDir;
            } else if (showErrorMessage) {
            }
            Log.e(TAG, "prefDir " + prefDir);
        }
        Log.e(TAG, "getPrefferredDirectoryPath " + directory);
        return directory;
    }

    public static boolean checkIfEACCES(String dir) {
        boolean z = false;
        try {
            File f = new File(dir);
            String localPath = new StringBuilder(String.valueOf(dir)).append("mpp").toString();
            f.mkdirs();
            Writer writer2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(localPath), "utf-8"));
            try {
                writer2.write("Something");
                z = true;
                writer2.close();
                Log.e(TAG, "f.delete() = " + new File(localPath).delete());
                try {
                    writer2.close();
                    writer = writer2;
                } catch (Exception e) {
                    writer = writer2;
                }
            } catch (IOException e2) {
                writer = writer2;
                Log.e(TAG, e2.toString());
                try {
                    writer.close();
                } catch (Exception e3) {
                }
                return z;
            } catch (Throwable th2) {
                Throwable th = th2;
                try {
                    writer.close();
                } catch (Exception e4) {
                }
            }
            return z;
        } catch (IOException e5) {
            Log.e(TAG, e5.toString());
            return z;
        }
    }

}
