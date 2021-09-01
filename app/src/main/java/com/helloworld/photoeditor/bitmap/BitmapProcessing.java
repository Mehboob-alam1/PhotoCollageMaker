package com.helloworld.photoeditor.bitmap;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

import com.helloworld.photoeditor.fragments.EffectFragment;

import java.io.IOException;

public class BitmapProcessing {
    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        float f;
        float f2 = -1.0f;
        Matrix matrix = new Matrix();
        if (horizontal) {
            f = -1.0f;
        } else {
            f = 1.0f;
        }
        if (!vertical) {
            f2 = 1.0f;
        }
        matrix.preScale(f, f2);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    public static Bitmap modifyOrientation(Bitmap bitmap, String image_url) throws IOException {
        switch (new ExifInterface(image_url).getAttributeInt("Orientation", 1)) {
            case EffectFragment.INDEX_LIGHT /*2*/:
                return flip(bitmap, true, false);
            case EffectFragment.INDEX_TEXTURE /*3*/:
                return rotate(bitmap, 180.0f);
            case EffectFragment.INDEX_BRIGHTNESS /*4*/:
                return flip(bitmap, false, true);
            case EffectFragment.INDEX_CONTRAST /*6*/:
                return rotate(bitmap, 90.0f);
            case EffectFragment.INDEX_TINT /*8*/:
                return rotate(bitmap, 270.0f);
            default:
                return bitmap;
        }
    }
}
