package com.helloworld.photoeditor.common_lib;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatSeekBar;
import android.util.AttributeSet;

public class SeekBarHint extends AppCompatSeekBar {
    Drawable mThumb;

    public SeekBarHint(Context context) {
        super(context);
    }

    public SeekBarHint(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SeekBarHint(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setThumb(Drawable thumb) {
        super.setThumb(thumb);
        this.mThumb = thumb;
    }

    public Drawable getSeekBarThumb() {
        return this.mThumb;
    }
}
