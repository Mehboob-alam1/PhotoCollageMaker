package com.helloworld.photoeditor.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAdListener;
import com.helloworld.photoeditor.Glob;
import com.helloworld.photoeditor.R;
import com.helloworld.photoeditor.adapters.MyRecyclerViewAdapter.RecyclerAdapterIndexChangedListener;
import com.helloworld.photoeditor.canvastext.ApplyTextInterface;
import com.helloworld.photoeditor.canvastext.CustomRelativeLayout;
import com.helloworld.photoeditor.canvastext.SingleTap;
import com.helloworld.photoeditor.canvastext.TextData;
import com.helloworld.photoeditor.collagelib.CollageActivity;
import com.helloworld.photoeditor.collagelib.Utility;
import com.helloworld.photoeditor.fragments.EffectFragment;
import com.helloworld.photoeditor.fragments.EffectFragment.BitmapReady;
import com.helloworld.photoeditor.fragments.FontFragment;
import com.helloworld.photoeditor.fragments.FontFragment.FontChoosedListener;
import com.helloworld.photoeditor.share.ImageShareActivity;
import com.helloworld.photoeditor.utils.LibUtility;
import com.helloworld.photoeditor.utils.MirrorMode;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class MirrorNewActivity extends AppCompatActivity {
    public static final int INDEX_MIRROR = 0;
    public static final int INDEX_MIRROR_3D = 1;
    public static final int INDEX_MIRROR_ADJUSTMENT = 5;
    public static final int INDEX_MIRROR_EFFECT = 3;
    public static final int INDEX_MIRROR_FRAME = 4;
    public static final int INDEX_MIRROR_INVISIBLE_VIEW = 7;
    public static final int INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX = 4;
    public static final int INDEX_MIRROR_RATIO = 2;
    static final int SAVE_IMAGE_ID = 1348;
    public static final int TAB_SIZE = 6;
    private static final String TAG = "MirrorNewActivity";
    int D3_BUTTON_SIZE = 24;
    int MIRROR_BUTTON_SIZE = 15;
    int RATIO_BUTTON_SIZE = 11;
    CustomRelativeLayout canvasText;
    int currentSelectedTabIndex = -1;
    ImageView[] d3ButtonArray;
    private int[] d3resList = new int[]{R.drawable.mirror_3d_14, R.drawable.mirror_3d_14, R.drawable.mirror_3d_10, R.drawable.mirror_3d_10, R.drawable.mirror_3d_11, R.drawable.mirror_3d_11, R.drawable.mirror_3d_4, R.drawable.mirror_3d_4, R.drawable.mirror_3d_3, R.drawable.mirror_3d_3, R.drawable.mirror_3d_1, R.drawable.mirror_3d_1, R.drawable.mirror_3d_6, R.drawable.mirror_3d_6, R.drawable.mirror_3d_13, R.drawable.mirror_3d_13, R.drawable.mirror_3d_15, R.drawable.mirror_3d_15, R.drawable.mirror_3d_15, R.drawable.mirror_3d_15, R.drawable.mirror_3d_16, R.drawable.mirror_3d_16, R.drawable.mirror_3d_16, R.drawable.mirror_3d_16};
    EffectFragment effectFragment;
    Bitmap filterBitmap;
    FontChoosedListener fontChoosedListener = new FontChoosedListener() {
        public void onOk(TextData textData) {
            MirrorNewActivity.this.canvasText.addTextView(textData);
            MirrorNewActivity.this.getSupportFragmentManager().beginTransaction().remove(MirrorNewActivity.this.fontFragment).commit();
        }
    };
    FontFragment fontFragment;
    int initialYPos = INDEX_MIRROR;
    RelativeLayout mainLayout;
    Matrix matrixMirror1 = new Matrix();
    Matrix matrixMirror2 = new Matrix();
    Matrix matrixMirror3 = new Matrix();
    Matrix matrixMirror4 = new Matrix();
    ImageView[] mirrorButtonArray;
    MirrorView mirrorView;
    float mulX = 16.0f;
    float mulY = 16.0f;
    Button[] ratioButtonArray;
    AlertDialog saveImageAlert;
    int screenHeightPixels;
    int screenWidthPixels;
    boolean showText = false;
    private Animation slideLeftIn;
    private Animation slideLeftOut;
    private Animation slideRightIn;
    private Animation slideRightOut;
    Bitmap sourceBitmap;
    View[] tabButtonList;
    ArrayList<TextData> textDataList = new ArrayList<>();
    ViewFlipper viewFlipper;
    InterstitialAd mInterstitialAd;
    private LinearLayout adExitChoicesContainer;
    private LinearLayout adExitView;
    android.app.AlertDialog alertDialog = null;
    private ProgressBar progressBarExitRefresh;
    private LinearLayout nativeAdContainerExitAds;
    private AdView adView;
    private com.facebook.ads.InterstitialAd interstitialAd;


    class MirrorView extends View {
        int currentModeIndex = MirrorNewActivity.INDEX_MIRROR;
        Bitmap d3Bitmap;
        boolean d3Mode = false;
        int defaultColor = R.color.bg;
        RectF destRect1;
        RectF destRect1X;
        RectF destRect1Y;
        RectF destRect2;
        RectF destRect2X;
        RectF destRect2Y;
        RectF destRect3;
        RectF destRect4;
        boolean drawSavedImage = false;
        RectF dstRectPaper1;
        RectF dstRectPaper2;
        RectF dstRectPaper3;
        RectF dstRectPaper4;
        final Matrix f510I = new Matrix();
        Bitmap frameBitmap;
        Paint framePaint = new Paint();
        int height;
        boolean isTouchStartedLeft;
        boolean isTouchStartedTop;
        boolean isVerticle = false;
        Matrix m1 = new Matrix();
        Matrix m2 = new Matrix();
        Matrix m3 = new Matrix();
        MirrorMode[] mirrorModeList = new MirrorMode[20];
        MirrorMode modeX;
        MirrorMode modeX10;
        MirrorMode modeX11;
        MirrorMode modeX12;
        MirrorMode modeX13;
        MirrorMode modeX14;
        MirrorMode modeX15;
        MirrorMode modeX16;
        MirrorMode modeX17;
        MirrorMode modeX18;
        MirrorMode modeX19;
        MirrorMode modeX2;
        MirrorMode modeX20;
        MirrorMode modeX3;
        MirrorMode modeX4;
        MirrorMode modeX5;
        MirrorMode modeX6;
        MirrorMode modeX7;
        MirrorMode modeX8;
        MirrorMode modeX9;
        float oldX;
        float oldY;
        RectF srcRect1;
        RectF srcRect2;
        RectF srcRect3;
        RectF srcRectPaper;
        int tMode1;
        int tMode2;
        int tMode3;
        Matrix textMatrix = new Matrix();
        Paint textRectPaint = new Paint(MirrorNewActivity.INDEX_MIRROR_3D);
        RectF totalArea1;
        RectF totalArea2;
        RectF totalArea3;
        int width;

        public MirrorView(Context context, int screenWidth, int screenHeight) {
            super(context);
            this.width = MirrorNewActivity.this.sourceBitmap.getWidth();
            this.height = MirrorNewActivity.this.sourceBitmap.getHeight();
            int widthPixels = screenWidth;
            int heightPixels = screenHeight;
            createMatrix(widthPixels, heightPixels);
            createRectX(widthPixels, heightPixels);
            createRectY(widthPixels, heightPixels);
            createRectXY(widthPixels, heightPixels);
            createModes();
            this.framePaint.setAntiAlias(true);
            this.framePaint.setFilterBitmap(true);
            this.framePaint.setDither(true);
            this.textRectPaint.setColor(getResources().getColor(R.color.bg));
        }

        private void reset(int widthPixels, int heightPixels, boolean invalidate) {
            createMatrix(widthPixels, heightPixels);
            createRectX(widthPixels, heightPixels);
            createRectY(widthPixels, heightPixels);
            createRectXY(widthPixels, heightPixels);
            createModes();
            if (invalidate) {
                postInvalidate();
            }
        }

        private String saveBitmap(boolean saveToFile, int widthPixel, int heightPixel) {
            float upperScale = (float) Utility.maxSizeForSave();
            float scale = upperScale / ((float) Math.min(widthPixel, heightPixel));
            Log.e(MirrorNewActivity.TAG, "upperScale" + upperScale);
            Log.e(MirrorNewActivity.TAG, "scale" + scale);
            if (MirrorNewActivity.this.mulY > MirrorNewActivity.this.mulX) {
                float f = MirrorNewActivity.this.mulX;
                scale = (1.0f * scale) / MirrorNewActivity.this.mulY;
            }
            if (scale <= 0.0f) {
                scale = 1.0f;
            }
            Log.e(MirrorNewActivity.TAG, "scale" + scale);
            int wP = Math.round(((float) widthPixel) * scale);
            int wH = Math.round(((float) heightPixel) * scale);
            RectF srcRect = this.mirrorModeList[this.currentModeIndex].getSrcRect();
            reset(wP, wH, false);
            int btmWidth = Math.round(MirrorNewActivity.this.mirrorView.getCurrentMirrorMode().rectTotalArea.width());
            int btmHeight = Math.round(MirrorNewActivity.this.mirrorView.getCurrentMirrorMode().rectTotalArea.height());
            if (btmWidth % MirrorNewActivity.INDEX_MIRROR_RATIO == MirrorNewActivity.INDEX_MIRROR_3D) {
                btmWidth--;
            }
            if (btmHeight % MirrorNewActivity.INDEX_MIRROR_RATIO == MirrorNewActivity.INDEX_MIRROR_3D) {
                btmHeight--;
            }
            Bitmap savedBitmap = Bitmap.createBitmap(btmWidth, btmHeight, Config.ARGB_8888);
            Canvas bitmapCanvas = new Canvas(savedBitmap);
            Matrix matrix = new Matrix();
            matrix.reset();
            Log.e(MirrorNewActivity.TAG, "btmWidth " + btmWidth);
            Log.e(MirrorNewActivity.TAG, "btmHeight " + btmHeight);
            matrix.postTranslate(((float) (-(wP - btmWidth))) / 2.0f, ((float) (-(wH - btmHeight))) / 2.0f);
            MirrorMode saveMode = this.mirrorModeList[this.currentModeIndex];
            saveMode.setSrcRect(srcRect);
            if (MirrorNewActivity.this.filterBitmap == null) {
                drawMode(bitmapCanvas, MirrorNewActivity.this.sourceBitmap, saveMode, matrix);
            } else {
                drawMode(bitmapCanvas, MirrorNewActivity.this.filterBitmap, saveMode, matrix);
            }
            if (!(!this.d3Mode || this.d3Bitmap == null || this.d3Bitmap.isRecycled())) {
                bitmapCanvas.setMatrix(matrix);
                bitmapCanvas.drawBitmap(this.d3Bitmap, null, this.mirrorModeList[this.currentModeIndex].rectTotalArea, this.framePaint);
            }
            if (MirrorNewActivity.this.textDataList != null) {
                for (int i = MirrorNewActivity.INDEX_MIRROR; i < MirrorNewActivity.this.textDataList.size(); i += MirrorNewActivity.INDEX_MIRROR_3D) {
                    Matrix mat = new Matrix();
                    mat.set(MirrorNewActivity.this.textDataList.get(i).imageSaveMatrix);
                    mat.postScale(scale, scale);
                    mat.postTranslate(((float) (-(wP - btmWidth))) / 2.0f, ((float) (-(wH - btmHeight))) / 2.0f);
                    bitmapCanvas.setMatrix(mat);
                    bitmapCanvas.drawText(MirrorNewActivity.this.textDataList.get(i).message, MirrorNewActivity.this.textDataList.get(i).xPos, MirrorNewActivity.this.textDataList.get(i).yPos, MirrorNewActivity.this.textDataList.get(i).textPaint);
                }
            }
            if (!(this.frameBitmap == null || this.frameBitmap.isRecycled())) {
                bitmapCanvas.setMatrix(matrix);
                bitmapCanvas.drawBitmap(this.frameBitmap, null, this.mirrorModeList[this.currentModeIndex].rectTotalArea, this.framePaint);
            }
            String resultPath = null;
            if (saveToFile) {
                resultPath = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().toString())).append("/").append(getString(R.string.app_name)).append("/").append(System.currentTimeMillis()).append(".jpg").toString();
                new File(resultPath).getParentFile().mkdirs();
                try {
                    FileOutputStream out = new FileOutputStream(resultPath);
                    savedBitmap.compress(CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            savedBitmap.recycle();
            reset(widthPixel, heightPixel, false);
            this.mirrorModeList[this.currentModeIndex].setSrcRect(srcRect);
            return resultPath;
        }

        private void setCurrentMode(int index) {
            this.currentModeIndex = index;
        }

        public MirrorMode getCurrentMirrorMode() {
            return this.mirrorModeList[this.currentModeIndex];
        }

        private void createModes() {
            this.modeX = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX, this.srcRect3, this.destRect1, this.destRect1, this.destRect3, this.destRect3, MirrorNewActivity.this.matrixMirror1, this.f510I, MirrorNewActivity.this.matrixMirror1, this.tMode3, this.totalArea3);
            this.modeX2 = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX, this.srcRect3, this.destRect1, this.destRect4, this.destRect1, this.destRect4, MirrorNewActivity.this.matrixMirror1, MirrorNewActivity.this.matrixMirror1, this.f510I, this.tMode3, this.totalArea3);
            this.modeX3 = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX, this.srcRect3, this.destRect3, this.destRect2, this.destRect3, this.destRect2, MirrorNewActivity.this.matrixMirror1, MirrorNewActivity.this.matrixMirror1, this.f510I, this.tMode3, this.totalArea3);
            this.modeX8 = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX, this.srcRect3, this.destRect1, this.destRect1, this.destRect1, this.destRect1, MirrorNewActivity.this.matrixMirror1, MirrorNewActivity.this.matrixMirror2, MirrorNewActivity.this.matrixMirror3, this.tMode3, this.totalArea3);
            int m9TouchMode = MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX;
            if (this.tMode3 == 0) {
                m9TouchMode = MirrorNewActivity.INDEX_MIRROR;
            }
            this.modeX9 = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX, this.srcRect3, this.destRect2, this.destRect2, this.destRect2, this.destRect2, MirrorNewActivity.this.matrixMirror1, MirrorNewActivity.this.matrixMirror2, MirrorNewActivity.this.matrixMirror3, m9TouchMode, this.totalArea3);
            int m10TouchMode = MirrorNewActivity.INDEX_MIRROR_EFFECT;
            if (this.tMode3 == MirrorNewActivity.INDEX_MIRROR_3D) {
                m10TouchMode = MirrorNewActivity.INDEX_MIRROR_3D;
            }
            this.modeX10 = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX, this.srcRect3, this.destRect3, this.destRect3, this.destRect3, this.destRect3, MirrorNewActivity.this.matrixMirror1, MirrorNewActivity.this.matrixMirror2, MirrorNewActivity.this.matrixMirror3, m10TouchMode, this.totalArea3);
            int m11TouchMode = MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX;
            if (this.tMode3 == 0) {
                m11TouchMode = MirrorNewActivity.INDEX_MIRROR_EFFECT;
            }
            this.modeX11 = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX, this.srcRect3, this.destRect4, this.destRect4, this.destRect4, this.destRect4, MirrorNewActivity.this.matrixMirror1, MirrorNewActivity.this.matrixMirror2, MirrorNewActivity.this.matrixMirror3, m11TouchMode, this.totalArea3);
            this.modeX4 = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_RATIO, this.srcRect1, this.destRect1X, this.destRect1X, MirrorNewActivity.this.matrixMirror1, this.tMode1, this.totalArea1);
            int m5TouchMode = MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX;
            if (this.tMode1 == 0) {
                m5TouchMode = MirrorNewActivity.INDEX_MIRROR;
            } else if (this.tMode1 == MirrorNewActivity.INDEX_MIRROR_ADJUSTMENT) {
                m5TouchMode = MirrorNewActivity.INDEX_MIRROR_ADJUSTMENT;
            }
            this.modeX5 = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_RATIO, this.srcRect1, this.destRect2X, this.destRect2X, MirrorNewActivity.this.matrixMirror1, m5TouchMode, this.totalArea1);
            this.modeX6 = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_RATIO, this.srcRect2, this.destRect1Y, this.destRect1Y, MirrorNewActivity.this.matrixMirror2, this.tMode2, this.totalArea2);
            int m7TouchMode = MirrorNewActivity.INDEX_MIRROR_EFFECT;
            if (this.tMode2 == MirrorNewActivity.INDEX_MIRROR_3D) {
                m7TouchMode = MirrorNewActivity.INDEX_MIRROR_3D;
            } else if (this.tMode2 == MirrorNewActivity.TAB_SIZE) {
                m7TouchMode = MirrorNewActivity.TAB_SIZE;
            }
            this.modeX7 = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_RATIO, this.srcRect2, this.destRect2Y, this.destRect2Y, MirrorNewActivity.this.matrixMirror2, m7TouchMode, this.totalArea2);
            this.modeX12 = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_RATIO, this.srcRect1, this.destRect1X, this.destRect2X, MirrorNewActivity.this.matrixMirror4, this.tMode1, this.totalArea1);
            this.modeX13 = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_RATIO, this.srcRect2, this.destRect1Y, this.destRect2Y, MirrorNewActivity.this.matrixMirror4, this.tMode2, this.totalArea2);
            this.modeX14 = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_RATIO, this.srcRect1, this.destRect1X, this.destRect1X, MirrorNewActivity.this.matrixMirror3, this.tMode1, this.totalArea1);
            this.modeX15 = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_RATIO, this.srcRect2, this.destRect1Y, this.destRect1Y, MirrorNewActivity.this.matrixMirror3, this.tMode2, this.totalArea2);
            this.modeX16 = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX, this.srcRectPaper, this.dstRectPaper1, this.dstRectPaper2, this.dstRectPaper3, this.dstRectPaper4, MirrorNewActivity.this.matrixMirror1, MirrorNewActivity.this.matrixMirror1, this.f510I, this.tMode1, this.totalArea1);
            this.modeX17 = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX, this.srcRectPaper, this.dstRectPaper1, this.dstRectPaper3, this.dstRectPaper3, this.dstRectPaper1, this.f510I, MirrorNewActivity.this.matrixMirror1, MirrorNewActivity.this.matrixMirror1, this.tMode1, this.totalArea1);
            this.modeX18 = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX, this.srcRectPaper, this.dstRectPaper2, this.dstRectPaper4, this.dstRectPaper2, this.dstRectPaper4, this.f510I, MirrorNewActivity.this.matrixMirror1, MirrorNewActivity.this.matrixMirror1, this.tMode1, this.totalArea1);
            this.modeX19 = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX, this.srcRectPaper, this.dstRectPaper1, this.dstRectPaper2, this.dstRectPaper2, this.dstRectPaper1, this.f510I, MirrorNewActivity.this.matrixMirror1, MirrorNewActivity.this.matrixMirror1, this.tMode1, this.totalArea1);
            this.modeX20 = new MirrorMode(MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX, this.srcRectPaper, this.dstRectPaper4, this.dstRectPaper3, this.dstRectPaper3, this.dstRectPaper4, this.f510I, MirrorNewActivity.this.matrixMirror1, MirrorNewActivity.this.matrixMirror1, this.tMode1, this.totalArea1);
            this.mirrorModeList[MirrorNewActivity.INDEX_MIRROR] = this.modeX4;
            this.mirrorModeList[MirrorNewActivity.INDEX_MIRROR_3D] = this.modeX5;
            this.mirrorModeList[MirrorNewActivity.INDEX_MIRROR_RATIO] = this.modeX6;
            this.mirrorModeList[MirrorNewActivity.INDEX_MIRROR_EFFECT] = this.modeX7;
            this.mirrorModeList[MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX] = this.modeX8;
            this.mirrorModeList[MirrorNewActivity.INDEX_MIRROR_ADJUSTMENT] = this.modeX9;
            this.mirrorModeList[MirrorNewActivity.TAB_SIZE] = this.modeX10;
            this.mirrorModeList[MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW] = this.modeX11;
            this.mirrorModeList[8] = this.modeX12;
            this.mirrorModeList[9] = this.modeX13;
            this.mirrorModeList[10] = this.modeX14;
            this.mirrorModeList[11] = this.modeX15;
            this.mirrorModeList[12] = this.modeX;
            this.mirrorModeList[13] = this.modeX2;
            this.mirrorModeList[14] = this.modeX3;
            this.mirrorModeList[15] = this.modeX7;
            this.mirrorModeList[16] = this.modeX17;
            this.mirrorModeList[17] = this.modeX18;
            this.mirrorModeList[18] = this.modeX19;
            this.mirrorModeList[19] = this.modeX20;
        }

        public Bitmap getBitmap() {
            setDrawingCacheEnabled(true);
            buildDrawingCache();
            Bitmap bmp = Bitmap.createBitmap(getDrawingCache());
            setDrawingCacheEnabled(false);
            return bmp;
        }

        public void setFrame(int index) {
            if (!(this.frameBitmap == null || this.frameBitmap.isRecycled())) {
                this.frameBitmap.recycle();
                this.frameBitmap = null;
            }
            if (index == 0) {
                postInvalidate();
                return;
            }
            this.frameBitmap = BitmapFactory.decodeResource(getResources(), LibUtility.borderRes[index]);
            postInvalidate();
        }

        private void createMatrix(int widthPixels, int heightPixels) {
            this.f510I.reset();
            MirrorNewActivity.this.matrixMirror1.reset();
            MirrorNewActivity.this.matrixMirror1.postScale(-1.0f, 1.0f);
            MirrorNewActivity.this.matrixMirror1.postTranslate((float) widthPixels, 0.0f);
            MirrorNewActivity.this.matrixMirror2.reset();
            MirrorNewActivity.this.matrixMirror2.postScale(1.0f, -1.0f);
            MirrorNewActivity.this.matrixMirror2.postTranslate(0.0f, (float) heightPixels);
            MirrorNewActivity.this.matrixMirror3.reset();
            MirrorNewActivity.this.matrixMirror3.postScale(-1.0f, -1.0f);
            MirrorNewActivity.this.matrixMirror3.postTranslate((float) widthPixels, (float) heightPixels);
        }

        private void createRectX(int widthPixels, int heightPixels) {
            float destH = ((float) widthPixels) * (MirrorNewActivity.this.mulY / MirrorNewActivity.this.mulX);
            float destW = ((float) widthPixels) / 2.0f;
            float destX = 0.0f;
            float destY = (float) MirrorNewActivity.this.initialYPos;
            if (destH > ((float) heightPixels)) {
                destH = (float) heightPixels;
                destW = ((MirrorNewActivity.this.mulX / MirrorNewActivity.this.mulY) * destH) / 2.0f;
                destX = (((float) widthPixels) / 2.0f) - destW;
            }
            destY = ((float) MirrorNewActivity.this.initialYPos) + ((((float) heightPixels) - destH) / 2.0f);
            float srcX = 0.0f;
            float srcY = 0.0f;
            float srcX2 = (float) this.width;
            float srcY2 = (float) this.height;
            this.destRect1X = new RectF(destX, destY, destW + destX, destH + destY);
            float destXX = destX + destW;
            this.destRect2X = new RectF(destXX, destY, destW + destXX, destH + destY);
            this.totalArea1 = new RectF(destX, destY, destW + destXX, destH + destY);
            this.tMode1 = MirrorNewActivity.INDEX_MIRROR_3D;
            if (MirrorNewActivity.this.mulX * ((float) this.height) <= (MirrorNewActivity.this.mulY * 2.0f) * ((float) this.width)) {
                srcX = (((float) this.width) - (((MirrorNewActivity.this.mulX / MirrorNewActivity.this.mulY) * ((float) this.height)) / 2.0f)) / 2.0f;
                srcX2 = srcX + (((MirrorNewActivity.this.mulX / MirrorNewActivity.this.mulY) * ((float) this.height)) / 2.0f);
            } else {
                srcY = (((float) this.height) - (((float) (this.width * MirrorNewActivity.INDEX_MIRROR_RATIO)) * (MirrorNewActivity.this.mulY / MirrorNewActivity.this.mulX))) / 2.0f;
                srcY2 = srcY + (((float) (this.width * MirrorNewActivity.INDEX_MIRROR_RATIO)) * (MirrorNewActivity.this.mulY / MirrorNewActivity.this.mulX));
                this.tMode1 = MirrorNewActivity.INDEX_MIRROR_ADJUSTMENT;
            }
            this.srcRect1 = new RectF(srcX, srcY, srcX2, srcY2);
            this.srcRectPaper = new RectF(srcX, srcY, ((srcX2 - srcX) / 2.0f) + srcX, srcY2);
            float destWPapar = destW / 2.0f;
            this.dstRectPaper1 = new RectF(destX, destY, destWPapar + destX, destH + destY);
            float dextXP = destX + destWPapar;
            this.dstRectPaper2 = new RectF(dextXP, destY, destWPapar + dextXP, destH + destY);
            dextXP += destWPapar;
            this.dstRectPaper3 = new RectF(dextXP, destY, destWPapar + dextXP, destH + destY);
            dextXP += destWPapar;
            this.dstRectPaper4 = new RectF(dextXP, destY, destWPapar + dextXP, destH + destY);
        }

        private void createRectY(int widthPixels, int heightPixels) {
            float destH = (((float) widthPixels) * (MirrorNewActivity.this.mulY / MirrorNewActivity.this.mulX)) / 2.0f;
            float destW = (float) widthPixels;
            float destX = 0.0f;
            float destY = (float) MirrorNewActivity.this.initialYPos;
            if (destH > ((float) heightPixels)) {
                destH = (float) heightPixels;
                destW = ((MirrorNewActivity.this.mulX / MirrorNewActivity.this.mulY) * destH) / 2.0f;
                destX = (((float) widthPixels) / 2.0f) - destW;
            }
            destY = ((float) MirrorNewActivity.this.initialYPos) + ((((float) heightPixels) - (2.0f * destH)) / 2.0f);
            this.destRect1Y = new RectF(destX, destY, destW + destX, destH + destY);
            float destYY = destY + destH;
            this.destRect2Y = new RectF(destX, destYY, destW + destX, destH + destYY);
            this.totalArea2 = new RectF(destX, destY, destW + destX, destH + destYY);
            float srcX = 0.0f;
            float srcY = 0.0f;
            float srcX2 = (float) this.width;
            float srcY2 = (float) this.height;
            this.tMode2 = MirrorNewActivity.INDEX_MIRROR;
            if ((MirrorNewActivity.this.mulX * 2.0f) * ((float) this.height) > MirrorNewActivity.this.mulY * ((float) this.width)) {
                srcY = (((float) this.height) - (((MirrorNewActivity.this.mulY / MirrorNewActivity.this.mulX) * ((float) this.width)) / 2.0f)) / 2.0f;
                srcY2 = srcY + (((MirrorNewActivity.this.mulY / MirrorNewActivity.this.mulX) * ((float) this.width)) / 2.0f);
            } else {
                srcX = (((float) this.width) - (((float) (this.height * MirrorNewActivity.INDEX_MIRROR_RATIO)) * (MirrorNewActivity.this.mulX / MirrorNewActivity.this.mulY))) / 2.0f;
                srcX2 = srcX + (((float) (this.height * MirrorNewActivity.INDEX_MIRROR_RATIO)) * (MirrorNewActivity.this.mulX / MirrorNewActivity.this.mulY));
                this.tMode2 = MirrorNewActivity.TAB_SIZE;
            }
            this.srcRect2 = new RectF(srcX, srcY, srcX2, srcY2);
        }

        private void createRectXY(int widthPixels, int heightPixels) {
            float destH = (((float) widthPixels) * (MirrorNewActivity.this.mulY / MirrorNewActivity.this.mulX)) / 2.0f;
            float destW = ((float) widthPixels) / 2.0f;
            float destX = 0.0f;
            float destY = (float) MirrorNewActivity.this.initialYPos;
            if (destH > ((float) heightPixels)) {
                destH = (float) heightPixels;
                destW = ((MirrorNewActivity.this.mulX / MirrorNewActivity.this.mulY) * destH) / 2.0f;
                destX = (((float) widthPixels) / 2.0f) - destW;
            }
            destY = ((float) MirrorNewActivity.this.initialYPos) + ((((float) heightPixels) - (2.0f * destH)) / 2.0f);
            float srcX = 0.0f;
            float srcY = 0.0f;
            float srcX2 = (float) this.width;
            float srcY2 = (float) this.height;
            this.destRect1 = new RectF(destX, destY, destW + destX, destH + destY);
            float destX2 = destX + destW;
            this.destRect2 = new RectF(destX2, destY, destW + destX2, destH + destY);
            float destY2 = destY + destH;
            this.destRect3 = new RectF(destX, destY2, destW + destX, destH + destY2);
            this.destRect4 = new RectF(destX2, destY2, destW + destX2, destH + destY2);
            this.totalArea3 = new RectF(destX, destY, destW + destX2, destH + destY2);
            if (MirrorNewActivity.this.mulX * ((float) this.height) <= MirrorNewActivity.this.mulY * ((float) this.width)) {
                srcX = (((float) this.width) - ((MirrorNewActivity.this.mulX / MirrorNewActivity.this.mulY) * ((float) this.height))) / 2.0f;
                srcX2 = srcX + ((MirrorNewActivity.this.mulX / MirrorNewActivity.this.mulY) * ((float) this.height));
                this.tMode3 = MirrorNewActivity.INDEX_MIRROR_3D;
            } else {
                srcY = (((float) this.height) - (((float) this.width) * (MirrorNewActivity.this.mulY / MirrorNewActivity.this.mulX))) / 2.0f;
                srcY2 = srcY + (((float) this.width) * (MirrorNewActivity.this.mulY / MirrorNewActivity.this.mulX));
                this.tMode3 = MirrorNewActivity.INDEX_MIRROR;
            }
            this.srcRect3 = new RectF(srcX, srcY, srcX2, srcY2);
        }

        @SuppressLint({"ResourceAsColor"})
        public void onDraw(Canvas canvas) {
            canvas.drawColor(this.defaultColor);
            if (MirrorNewActivity.this.filterBitmap == null) {
                drawMode(canvas, MirrorNewActivity.this.sourceBitmap, this.mirrorModeList[this.currentModeIndex], this.f510I);
            } else {
                drawMode(canvas, MirrorNewActivity.this.filterBitmap, this.mirrorModeList[this.currentModeIndex], this.f510I);
            }
            if (!(!this.d3Mode || this.d3Bitmap == null || this.d3Bitmap.isRecycled())) {
                canvas.setMatrix(this.f510I);
                canvas.drawBitmap(this.d3Bitmap, null, this.mirrorModeList[this.currentModeIndex].rectTotalArea, this.framePaint);
            }
            if (MirrorNewActivity.this.showText) {
                for (int i = MirrorNewActivity.INDEX_MIRROR; i < MirrorNewActivity.this.textDataList.size(); i += MirrorNewActivity.INDEX_MIRROR_3D) {
                    this.textMatrix.set(MirrorNewActivity.this.textDataList.get(i).imageSaveMatrix);
                    this.textMatrix.postConcat(this.f510I);
                    canvas.setMatrix(this.textMatrix);
                    canvas.drawText(MirrorNewActivity.this.textDataList.get(i).message, MirrorNewActivity.this.textDataList.get(i).xPos, MirrorNewActivity.this.textDataList.get(i).yPos, MirrorNewActivity.this.textDataList.get(i).textPaint);
                    canvas.setMatrix(this.f510I);
                    canvas.drawRect(0.0f, 0.0f, this.mirrorModeList[this.currentModeIndex].rectTotalArea.left, (float) MirrorNewActivity.this.screenHeightPixels, this.textRectPaint);
                    canvas.drawRect(0.0f, 0.0f, (float) MirrorNewActivity.this.screenWidthPixels, this.mirrorModeList[this.currentModeIndex].rectTotalArea.top, this.textRectPaint);
                    canvas.drawRect(this.mirrorModeList[this.currentModeIndex].rectTotalArea.right, 0.0f, (float) MirrorNewActivity.this.screenWidthPixels, (float) MirrorNewActivity.this.screenHeightPixels, this.textRectPaint);
                    canvas.drawRect(0.0f, this.mirrorModeList[this.currentModeIndex].rectTotalArea.bottom, (float) MirrorNewActivity.this.screenWidthPixels, (float) MirrorNewActivity.this.screenHeightPixels, this.textRectPaint);
                }
            }
            if (!(this.frameBitmap == null || this.frameBitmap.isRecycled())) {
                canvas.setMatrix(this.f510I);
                canvas.drawBitmap(this.frameBitmap, null, this.mirrorModeList[this.currentModeIndex].rectTotalArea, this.framePaint);
            }
            super.onDraw(canvas);
        }

        private void drawMode(Canvas canvas, Bitmap bitmap, MirrorMode mirrorMode, Matrix matrix) {
            canvas.setMatrix(matrix);
            canvas.drawBitmap(bitmap, mirrorMode.getDrawBitmapSrc(), mirrorMode.rect1, this.framePaint);
            this.m1.set(mirrorMode.matrix1);
            this.m1.postConcat(matrix);
            canvas.setMatrix(this.m1);
            if (!(bitmap == null || bitmap.isRecycled())) {
                canvas.drawBitmap(bitmap, mirrorMode.getDrawBitmapSrc(), mirrorMode.rect2, this.framePaint);
            }
            if (mirrorMode.count == MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX) {
                this.m2.set(mirrorMode.matrix2);
                this.m2.postConcat(matrix);
                canvas.setMatrix(this.m2);
                if (!(bitmap == null || bitmap.isRecycled())) {
                    canvas.drawBitmap(bitmap, mirrorMode.getDrawBitmapSrc(), mirrorMode.rect3, this.framePaint);
                }
                this.m3.set(mirrorMode.matrix3);
                this.m3.postConcat(matrix);
                canvas.setMatrix(this.m3);
                if (bitmap != null && !bitmap.isRecycled()) {
                    canvas.drawBitmap(bitmap, mirrorMode.getDrawBitmapSrc(), mirrorMode.rect4, this.framePaint);
                }
            }
        }

        public boolean onTouchEvent(MotionEvent event) {
            boolean z = false;
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MirrorNewActivity.INDEX_MIRROR /*0*/:
                    boolean z2;
                    z2 = x < ((float) (MirrorNewActivity.this.screenWidthPixels / MirrorNewActivity.INDEX_MIRROR_RATIO));
                    this.isTouchStartedLeft = z2;
                    if (y < ((float) (MirrorNewActivity.this.screenHeightPixels / MirrorNewActivity.INDEX_MIRROR_RATIO))) {
                        z = true;
                    }
                    this.isTouchStartedTop = z;
                    this.oldX = x;
                    this.oldY = y;
                    break;
                case MirrorNewActivity.INDEX_MIRROR_RATIO /*2*/:
                    moveGrid(this.mirrorModeList[this.currentModeIndex].getSrcRect(), x - this.oldX, y - this.oldY);
                    this.mirrorModeList[this.currentModeIndex].updateBitmapSrc();
                    this.oldX = x;
                    this.oldY = y;
                    break;
            }
            postInvalidate();
            return true;
        }

        void moveGrid(RectF srcRect, float x, float y) {
            if (this.mirrorModeList[this.currentModeIndex].touchMode == MirrorNewActivity.INDEX_MIRROR_3D || this.mirrorModeList[this.currentModeIndex].touchMode == MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX || this.mirrorModeList[this.currentModeIndex].touchMode == MirrorNewActivity.TAB_SIZE) {
                if (this.mirrorModeList[this.currentModeIndex].touchMode == MirrorNewActivity.INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX) {
                    x *= -1.0f;
                }
                if (this.isTouchStartedLeft && this.mirrorModeList[this.currentModeIndex].touchMode != MirrorNewActivity.TAB_SIZE) {
                    x *= -1.0f;
                }
                if (srcRect.left + x < 0.0f) {
                    x = -srcRect.left;
                }
                if (srcRect.right + x >= ((float) this.width)) {
                    x = ((float) this.width) - srcRect.right;
                }
                srcRect.left += x;
                srcRect.right += x;
            } else if (this.mirrorModeList[this.currentModeIndex].touchMode == 0 || this.mirrorModeList[this.currentModeIndex].touchMode == MirrorNewActivity.INDEX_MIRROR_EFFECT || this.mirrorModeList[this.currentModeIndex].touchMode == MirrorNewActivity.INDEX_MIRROR_ADJUSTMENT) {
                if (this.mirrorModeList[this.currentModeIndex].touchMode == MirrorNewActivity.INDEX_MIRROR_EFFECT) {
                    y *= -1.0f;
                }
                if (this.isTouchStartedTop && this.mirrorModeList[this.currentModeIndex].touchMode != MirrorNewActivity.INDEX_MIRROR_ADJUSTMENT) {
                    y *= -1.0f;
                }
                if (srcRect.top + y < 0.0f) {
                    y = -srcRect.top;
                }
                if (srcRect.bottom + y >= ((float) this.height)) {
                    y = ((float) this.height) - srcRect.bottom;
                }
                srcRect.top += y;
                srcRect.bottom += y;
            }
        }
    }

    final class MyMediaScannerConnectionClient implements MediaScannerConnectionClient {
        private MediaScannerConnection mConn;
        private String mFilename;
        private String mMimetype;

        public MyMediaScannerConnectionClient(Context ctx, File file, String mimetype) {
            this.mFilename = file.getAbsolutePath();
            this.mConn = new MediaScannerConnection(ctx, this);
            this.mConn.connect();
        }

        public void onMediaScannerConnected() {
            this.mConn.scanFile(this.mFilename, this.mMimetype);
        }

        public void onScanCompleted(String path, Uri uri) {
            this.mConn.disconnect();
        }
    }

    private class SaveImageTask extends AsyncTask<Object, Object, Object> {
        ProgressDialog progressDialog;
        String resultPath;

        private SaveImageTask() {
            this.resultPath = null;
        }

        protected Object doInBackground(Object... arg0) {
            this.resultPath = MirrorNewActivity.this.mirrorView.saveBitmap(true, MirrorNewActivity.this.screenWidthPixels, MirrorNewActivity.this.screenHeightPixels);
            return null;
        }

        protected void onPreExecute() {
            this.progressDialog = new ProgressDialog(MirrorNewActivity.this);
            this.progressDialog.setMessage("Saving image ...");
            this.progressDialog.show();
        }

        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (this.progressDialog != null && this.progressDialog.isShowing()) {
                this.progressDialog.cancel();
            }
            if (this.resultPath != null) {
                final Intent intent = new Intent(MirrorNewActivity.this, ImageShareActivity.class);
                intent.putExtra("imagePath", this.resultPath);

                //showInterstitialAd();
               if(Glob.isFB){
                   if(interstitialAd.isAdLoaded() && !interstitialAd.isAdInvalidated()){
                       interstitialAd.show();
                       interstitialAd.setAdListener(new InterstitialAdListener() {
                           @Override
                           public void onInterstitialDisplayed(Ad ad) {
                               // Interstitial ad displayed callback
                               Log.e(TAG, "Interstitial ad displayed.");
                           }

                           @Override
                           public void onInterstitialDismissed(Ad ad) {
                               // Interstitial dismissed callback
                               Log.e(TAG, "Interstitial ad dismissed.");

                               MirrorNewActivity.this.startActivity(intent);
                               finish();  //DT finish
                               loadFbInterstitial();
                           }

                           @Override
                           public void onError(Ad ad, AdError adError) {
                               // Ad error callback
                               Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                           }

                           @Override
                           public void onAdLoaded(Ad ad) {
                               // Interstitial ad is loaded and ready to be displayed
                               Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                           }

                           @Override
                           public void onAdClicked(Ad ad) {
                               // Ad clicked callback
                               Log.d(TAG, "Interstitial ad clicked!");
                           }

                           @Override
                           public void onLoggingImpression(Ad ad) {
                               // Ad impression logged callback
                               Log.d(TAG, "Interstitial ad impression logged!");
                           }
                       });
                   } else{
                       MirrorNewActivity.this.startActivity(intent);
                       finish();  //DT finish
                   }

               }else if(Glob.isAdmob){
                   if (mInterstitialAd != null && Glob.isOnline(MirrorNewActivity.this)) {
                       showInterstitialAd();
                       mInterstitialAd.setAdListener(new AdListener() {
                           @Override
                           public void onAdClosed() {
                               super.onAdClosed();
                               MirrorNewActivity.this.startActivity(intent);
                               finish();  //DT finish
                               loadInterstitialAd();
                           }
                       });
                   }
               }
               else{
                   MirrorNewActivity.this.startActivity(intent);
                   finish();  //DT finish
               }
            }
            MyMediaScannerConnectionClient myMediaScannerConnectionClient = new MyMediaScannerConnectionClient(MirrorNewActivity.this.getApplicationContext(), new File(this.resultPath), null);
        }
    }

    @SuppressLint({"NewApi", "WrongConstant"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("MrrorNewActivity", "onCreate");
        getWindow().addFlags(1024);
        Bundle extras = getIntent().getExtras();
        this.sourceBitmap = Utility.getScaledBitmapFromId(this, extras.getLongArray("photo_id_list")[INDEX_MIRROR], extras.getIntArray("photo_orientation_list")[INDEX_MIRROR], -1, false);
        if (this.sourceBitmap == null) {
            Toast msg = Toast.makeText(this, "Could not load the photo, please use another GALLERY app!", INDEX_MIRROR_3D);
            msg.setGravity(17, msg.getXOffset() / INDEX_MIRROR_RATIO, msg.getYOffset() / INDEX_MIRROR_RATIO);
            msg.show();
            finish();
            return;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.screenHeightPixels = metrics.heightPixels;
        this.screenWidthPixels = metrics.widthPixels;
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        if (this.screenWidthPixels <= 0) {
            this.screenWidthPixels = width;
        }
        if (this.screenHeightPixels <= 0) {
            this.screenHeightPixels = height;
        }
        this.mirrorView = new MirrorView(this, this.screenWidthPixels, this.screenHeightPixels);
        setContentView(R.layout.activity_mirror_new);
        Log.i("mytag", "set content view)");

        this.mInterstitialAd = new InterstitialAd(this);
        this.mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        if (Glob.isOnline(MirrorNewActivity.this)) {
            loadInterstitialAd();

        }
        loadFbBanner();
        loadFbInterstitial();

        this.mainLayout = findViewById(R.id.layout_mirror_activity);
        this.mainLayout.addView(this.mirrorView);
        this.viewFlipper = findViewById(R.id.mirror_view_flipper);
        this.viewFlipper.bringToFront();
        findViewById(R.id.mirror_footer).bringToFront();
        this.slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        this.slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        this.slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        this.slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        findViewById(R.id.mirror_header).bringToFront();
        addEffectFragment();
        setSelectedTab(INDEX_MIRROR);
    }

    @SuppressLint({"WrongConstant"})
    private boolean isOnline() {
        try {
            return ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }


    void addEffectFragment() {
        if (this.effectFragment == null) {
            this.effectFragment = (EffectFragment) getSupportFragmentManager().findFragmentByTag("MY_EFFECT_FRAGMENT");
            if (this.effectFragment == null) {
                this.effectFragment = new EffectFragment();
                Log.e(TAG, "EffectFragment == null");
                this.effectFragment.setBitmap(this.sourceBitmap);
                this.effectFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().add(R.id.mirror_effect_fragment_container, this.effectFragment, "MY_EFFECT_FRAGMENT").commit();
            } else {
                this.effectFragment.setBitmap(this.sourceBitmap);
                this.effectFragment.setSelectedTabIndex(INDEX_MIRROR);
            }
            this.effectFragment.setBitmapReadyListener(new BitmapReady() {
                public void onBitmapReady(Bitmap bitmap) {
                    MirrorNewActivity.this.filterBitmap = bitmap;
                    MirrorNewActivity.this.mirrorView.postInvalidate();
                }
            });
            this.effectFragment.setBorderIndexChangedListener(new RecyclerAdapterIndexChangedListener() {
                public void onIndexChanged(int i) {
                    MirrorNewActivity.this.mirrorView.setFrame(i);
                }
            });
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("show_text", this.showText);
        savedInstanceState.putSerializable("text_data", this.textDataList);
        if (this.fontFragment != null && this.fontFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().remove(this.fontFragment).commit();
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.showText = savedInstanceState.getBoolean("show_text");
        this.textDataList = (ArrayList) savedInstanceState.getSerializable("text_data");
        if (this.textDataList == null) {
            this.textDataList = new ArrayList<>();
        }
    }

    public void myClickHandler(View view) {
        int id = view.getId();
        this.mirrorView.drawSavedImage = false;
        if (id == R.id.button_save_mirror_image) {
            new SaveImageTask().execute();
        } else if (id == R.id.closeScreen) {
            backButtonAlertBuilder();
        } else if (id == R.id.button_mirror) {
            setSelectedTab(INDEX_MIRROR);
        } else if (id == R.id.button_mirror_frame) {
            setSelectedTab(INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX);
        } else if (id == R.id.button_mirror_ratio) {
            setSelectedTab(INDEX_MIRROR_RATIO);
        } else if (id == R.id.button_mirror_effect) {
            setSelectedTab(INDEX_MIRROR_EFFECT);
        } else if (id == R.id.button_mirror_adj) {
            setSelectedTab(INDEX_MIRROR_ADJUSTMENT);
        } else if (id == R.id.button_mirror_3d) {
            setSelectedTab(INDEX_MIRROR_3D);
        } else if (id == R.id.button_3d_1) {
            set3dMode(INDEX_MIRROR);
        } else if (id == R.id.button_3d_2) {
            set3dMode(INDEX_MIRROR_3D);
        } else if (id == R.id.button_3d_3) {
            set3dMode(INDEX_MIRROR_RATIO);
        } else if (id == R.id.button_3d_4) {
            set3dMode(INDEX_MIRROR_EFFECT);
        } else if (id == R.id.button_3d_5) {
            set3dMode(INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX);
        } else if (id == R.id.button_3d_6) {
            set3dMode(INDEX_MIRROR_ADJUSTMENT);
        } else if (id == R.id.button_3d_7) {
            set3dMode(TAB_SIZE);
        } else if (id == R.id.button_3d_8) {
            set3dMode(INDEX_MIRROR_INVISIBLE_VIEW);
        } else if (id == R.id.button_3d_9) {
            set3dMode(8);
        } else if (id == R.id.button_3d_10) {
            set3dMode(9);
        } else if (id == R.id.button_3d_11) {
            set3dMode(10);
        } else if (id == R.id.button_3d_12) {
            set3dMode(11);
        } else if (id == R.id.button_3d_13) {
            set3dMode(12);
        } else if (id == R.id.button_3d_14) {
            set3dMode(13);
        } else if (id == R.id.button_3d_15) {
            set3dMode(14);
        } else if (id == R.id.button_3d_16) {
            set3dMode(15);
        } else if (id == R.id.button_3d_17) {
            set3dMode(16);
        } else if (id == R.id.button_3d_18) {
            set3dMode(17);
        } else if (id == R.id.button_3d_19) {
            set3dMode(18);
        } else if (id == R.id.button_3d_20) {
            set3dMode(19);
        } else if (id == R.id.button_3d_21) {
            set3dMode(20);
        } else if (id == R.id.button_3d_22) {
            set3dMode(21);
        } else if (id == R.id.button_3d_23) {
            set3dMode(22);
        } else if (id == R.id.button_3d_24) {
            set3dMode(23);
        } else if (id == R.id.button11) {
            this.mulX = 1.0f;
            this.mulY = 1.0f;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setRatioButtonBg(INDEX_MIRROR);
        } else if (id == R.id.button21) {
            this.mulX = 2.0f;
            this.mulY = 1.0f;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setRatioButtonBg(INDEX_MIRROR_3D);
        } else if (id == R.id.button12) {
            this.mulX = 1.0f;
            this.mulY = 2.0f;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setRatioButtonBg(INDEX_MIRROR_RATIO);
        } else if (id == R.id.button32) {
            this.mulX = 3.0f;
            this.mulY = 2.0f;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setRatioButtonBg(INDEX_MIRROR_EFFECT);
        } else if (id == R.id.button23) {
            this.mulX = 2.0f;
            this.mulY = 3.0f;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setRatioButtonBg(INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX);
        } else if (id == R.id.button43) {
            this.mulX = 4.0f;
            this.mulY = 3.0f;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setRatioButtonBg(INDEX_MIRROR_ADJUSTMENT);
        } else if (id == R.id.button34) {
            this.mulX = 3.0f;
            this.mulY = 4.0f;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setRatioButtonBg(TAB_SIZE);
        } else if (id == R.id.button45) {
            this.mulX = 4.0f;
            this.mulY = 5.0f;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setRatioButtonBg(INDEX_MIRROR_INVISIBLE_VIEW);
        } else if (id == R.id.button57) {
            this.mulX = 5.0f;
            this.mulY = 7.0f;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setRatioButtonBg(8);
        } else if (id == R.id.button169) {
            this.mulX = 16.0f;
            this.mulY = 9.0f;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setRatioButtonBg(9);
        } else if (id == R.id.button916) {
            this.mulX = 9.0f;
            this.mulY = 16.0f;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setRatioButtonBg(10);
        } else if (id == R.id.button_m1) {
            this.mirrorView.setCurrentMode(INDEX_MIRROR);
            this.mirrorView.d3Mode = false;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setMirrorButtonBg(INDEX_MIRROR);
        } else if (id == R.id.button_m2) {
            this.mirrorView.setCurrentMode(INDEX_MIRROR_3D);
            this.mirrorView.d3Mode = false;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setMirrorButtonBg(INDEX_MIRROR_3D);
        } else if (id == R.id.button_m3) {
            this.mirrorView.setCurrentMode(INDEX_MIRROR_RATIO);
            this.mirrorView.d3Mode = false;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setMirrorButtonBg(INDEX_MIRROR_RATIO);
        } else if (id == R.id.button_m4) {
            this.mirrorView.setCurrentMode(INDEX_MIRROR_EFFECT);
            this.mirrorView.d3Mode = false;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setMirrorButtonBg(INDEX_MIRROR_EFFECT);
        } else if (id == R.id.button_m5) {
            this.mirrorView.setCurrentMode(INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX);
            this.mirrorView.d3Mode = false;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setMirrorButtonBg(INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX);
        } else if (id == R.id.button_m6) {
            this.mirrorView.setCurrentMode(INDEX_MIRROR_ADJUSTMENT);
            this.mirrorView.d3Mode = false;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setMirrorButtonBg(INDEX_MIRROR_ADJUSTMENT);
        } else if (id == R.id.button_m7) {
            this.mirrorView.setCurrentMode(TAB_SIZE);
            this.mirrorView.d3Mode = false;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setMirrorButtonBg(TAB_SIZE);
        } else if (id == R.id.button_m8) {
            this.mirrorView.setCurrentMode(INDEX_MIRROR_INVISIBLE_VIEW);
            this.mirrorView.d3Mode = false;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setMirrorButtonBg(INDEX_MIRROR_INVISIBLE_VIEW);
        } else if (id == R.id.button_m9) {
            this.mirrorView.setCurrentMode(8);
            this.mirrorView.d3Mode = false;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setMirrorButtonBg(8);
        } else if (id == R.id.button_m10) {
            this.mirrorView.setCurrentMode(9);
            this.mirrorView.d3Mode = false;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setMirrorButtonBg(9);
        } else if (id == R.id.button_m11) {
            this.mirrorView.setCurrentMode(10);
            this.mirrorView.d3Mode = false;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setMirrorButtonBg(10);
        } else if (id == R.id.button_m12) {
            this.mirrorView.setCurrentMode(11);
            this.mirrorView.d3Mode = false;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setMirrorButtonBg(11);
        } else if (id == R.id.button_m13) {
            this.mirrorView.setCurrentMode(12);
            this.mirrorView.d3Mode = false;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setMirrorButtonBg(12);
        } else if (id == R.id.button_m14) {
            this.mirrorView.setCurrentMode(13);
            this.mirrorView.d3Mode = false;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setMirrorButtonBg(13);
        } else if (id == R.id.button_m15) {
            this.mirrorView.setCurrentMode(14);
            this.mirrorView.d3Mode = false;
            this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, true);
            setMirrorButtonBg(14);
        } else if (id == R.id.button_mirror_text) {
            addCanvasTextView();
            clearViewFlipper();
        } else if (id != R.id.button_mirror_sticker) {
            this.effectFragment.myClickHandler(id);
            if (id == R.id.button_lib_cancel || id == R.id.button_lib_ok) {
                clearFxAndFrame();
            }
        }
    }

    private void clearFxAndFrame() {
        int selectedTabIndex = this.effectFragment.getSelectedTabIndex();
        if (this.currentSelectedTabIndex != INDEX_MIRROR_EFFECT && this.currentSelectedTabIndex != INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX) {
            return;
        }
        if (selectedTabIndex == 0 || selectedTabIndex == INDEX_MIRROR_3D) {
            clearViewFlipper();
        }
    }

    void addCanvasTextView() {
        Log.i("mytag", "add canvas text");
        this.canvasText = new CustomRelativeLayout(this, this.textDataList, this.mirrorView.f510I, new SingleTap() {
            public void onSingleTap(TextData textData) {
                Log.i("mytag", "single tap");
                MirrorNewActivity.this.fontFragment = new FontFragment();
                Bundle arguments = new Bundle();
                arguments.putSerializable("text_data", textData);
                MirrorNewActivity.this.fontFragment.setArguments(arguments);
                MirrorNewActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.text_view_fragment_container, MirrorNewActivity.this.fontFragment, "FONT_FRAGMENT").commit();
                Log.e(MirrorNewActivity.TAG, "replace fragment");
                MirrorNewActivity.this.fontFragment.setFontChoosedListener(MirrorNewActivity.this.fontChoosedListener);
            }
        });
        this.canvasText.setApplyTextListener(new ApplyTextInterface() {
            public void onCancel() {
                Log.i("mytag", "cancel");
                MirrorNewActivity.this.showText = true;
                MirrorNewActivity.this.mainLayout.removeView(MirrorNewActivity.this.canvasText);
                MirrorNewActivity.this.mirrorView.postInvalidate();
            }

            public void onOk(ArrayList<TextData> arrayList) {
                Log.i("mytag", "ok");
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    ((TextData) it.next()).setImageSaveMatrix(MirrorNewActivity.this.mirrorView.f510I);
                }
                MirrorNewActivity.this.textDataList = arrayList;
                MirrorNewActivity.this.showText = true;
                if (MirrorNewActivity.this.mainLayout == null) {
                    MirrorNewActivity.this.mainLayout = MirrorNewActivity.this.findViewById(R.id.layout_mirror_activity);
                }
                MirrorNewActivity.this.mainLayout.removeView(MirrorNewActivity.this.canvasText);
                MirrorNewActivity.this.mirrorView.postInvalidate();
            }
        });
        this.showText = false;
        this.mirrorView.invalidate();
        this.mainLayout.addView(this.canvasText);
        findViewById(R.id.text_view_fragment_container).bringToFront();
        this.fontFragment = new FontFragment();
        this.fontFragment.setArguments(new Bundle());
        getSupportFragmentManager().beginTransaction().add(R.id.text_view_fragment_container, this.fontFragment, "FONT_FRAGMENT").commit();
        Log.e(TAG, "add fragment");
        this.fontFragment.setFontChoosedListener(this.fontChoosedListener);
    }

    private void set3dMode(int index) {
        this.mirrorView.d3Mode = true;
        if (index > 15 && index < 20) {
            this.mirrorView.setCurrentMode(index);
        } else if (index > 19) {
            this.mirrorView.setCurrentMode(index - 4);
        } else if (index % INDEX_MIRROR_RATIO == 0) {
            this.mirrorView.setCurrentMode(INDEX_MIRROR);
        } else {
            this.mirrorView.setCurrentMode(INDEX_MIRROR_3D);
        }
        this.mirrorView.reset(this.screenWidthPixels, this.screenHeightPixels, false);
        if (VERSION.SDK_INT < 19) {
            if (!(this.mirrorView.d3Bitmap == null || this.mirrorView.d3Bitmap.isRecycled())) {
                this.mirrorView.d3Bitmap.recycle();
            }
            this.mirrorView.d3Bitmap = BitmapFactory.decodeResource(getResources(), this.d3resList[index]);
        } else {
            loadInBitmap(this.d3resList[index]);
        }
        this.mirrorView.postInvalidate();
        setD3ButtonBg(index);
    }

    private void loadInBitmap(int resId) {
        Log.e(TAG, "loadInBitmap");
        Options options = new Options();
        if (this.mirrorView.d3Bitmap == null || this.mirrorView.d3Bitmap.isRecycled()) {
            options.inJustDecodeBounds = true;
            options.inMutable = true;
            BitmapFactory.decodeResource(getResources(), resId, options);
            this.mirrorView.d3Bitmap = Bitmap.createBitmap(options.outWidth, options.outHeight, Config.ARGB_8888);
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = INDEX_MIRROR_3D;
        options.inBitmap = this.mirrorView.d3Bitmap;
        try {
            this.mirrorView.d3Bitmap = BitmapFactory.decodeResource(getResources(), resId, options);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            if (!(this.mirrorView.d3Bitmap == null || this.mirrorView.d3Bitmap.isRecycled())) {
                this.mirrorView.d3Bitmap.recycle();
            }
            this.mirrorView.d3Bitmap = BitmapFactory.decodeResource(getResources(), resId);
        }
    }

    private void setD3ButtonBg(int index) {
        if (this.d3ButtonArray == null) {
            this.d3ButtonArray = new ImageView[this.D3_BUTTON_SIZE];
            this.d3ButtonArray[INDEX_MIRROR] = findViewById(R.id.button_3d_1);
            this.d3ButtonArray[INDEX_MIRROR_3D] = findViewById(R.id.button_3d_2);
            this.d3ButtonArray[INDEX_MIRROR_RATIO] = findViewById(R.id.button_3d_3);
            this.d3ButtonArray[INDEX_MIRROR_EFFECT] = findViewById(R.id.button_3d_4);
            this.d3ButtonArray[INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX] = findViewById(R.id.button_3d_5);
            this.d3ButtonArray[INDEX_MIRROR_ADJUSTMENT] = findViewById(R.id.button_3d_6);
            this.d3ButtonArray[TAB_SIZE] = findViewById(R.id.button_3d_7);
            this.d3ButtonArray[INDEX_MIRROR_INVISIBLE_VIEW] = findViewById(R.id.button_3d_8);
            this.d3ButtonArray[8] = findViewById(R.id.button_3d_9);
            this.d3ButtonArray[9] = findViewById(R.id.button_3d_10);
            this.d3ButtonArray[10] = findViewById(R.id.button_3d_11);
            this.d3ButtonArray[11] = findViewById(R.id.button_3d_12);
            this.d3ButtonArray[12] = findViewById(R.id.button_3d_13);
            this.d3ButtonArray[13] = findViewById(R.id.button_3d_14);
            this.d3ButtonArray[14] = findViewById(R.id.button_3d_15);
            this.d3ButtonArray[15] = findViewById(R.id.button_3d_16);
            this.d3ButtonArray[16] = findViewById(R.id.button_3d_17);
            this.d3ButtonArray[17] = findViewById(R.id.button_3d_18);
            this.d3ButtonArray[18] = findViewById(R.id.button_3d_19);
            this.d3ButtonArray[19] = findViewById(R.id.button_3d_20);
            this.d3ButtonArray[20] = findViewById(R.id.button_3d_21);
            this.d3ButtonArray[21] = findViewById(R.id.button_3d_22);
            this.d3ButtonArray[22] = findViewById(R.id.button_3d_23);
            this.d3ButtonArray[23] = findViewById(R.id.button_3d_24);
        }
        for (int i = INDEX_MIRROR; i < this.D3_BUTTON_SIZE; i += INDEX_MIRROR_3D) {
            this.d3ButtonArray[i].setBackgroundColor(getResources().getColor(R.color.white));
        }
        this.d3ButtonArray[index].setBackgroundColor(getResources().getColor(R.color.yellow));
    }

    private void setMirrorButtonBg(int index) {
        if (this.mirrorButtonArray == null) {
            this.mirrorButtonArray = new ImageView[this.MIRROR_BUTTON_SIZE];
            this.mirrorButtonArray[INDEX_MIRROR] = findViewById(R.id.button_m1);
            this.mirrorButtonArray[INDEX_MIRROR_3D] = findViewById(R.id.button_m2);
            this.mirrorButtonArray[INDEX_MIRROR_RATIO] = findViewById(R.id.button_m3);
            this.mirrorButtonArray[INDEX_MIRROR_EFFECT] = findViewById(R.id.button_m4);
            this.mirrorButtonArray[INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX] = findViewById(R.id.button_m5);
            this.mirrorButtonArray[INDEX_MIRROR_ADJUSTMENT] = findViewById(R.id.button_m6);
            this.mirrorButtonArray[TAB_SIZE] = findViewById(R.id.button_m7);
            this.mirrorButtonArray[INDEX_MIRROR_INVISIBLE_VIEW] = findViewById(R.id.button_m8);
            this.mirrorButtonArray[8] = findViewById(R.id.button_m9);
            this.mirrorButtonArray[9] = findViewById(R.id.button_m10);
            this.mirrorButtonArray[10] = findViewById(R.id.button_m11);
            this.mirrorButtonArray[11] = findViewById(R.id.button_m12);
            this.mirrorButtonArray[12] = findViewById(R.id.button_m13);
            this.mirrorButtonArray[13] = findViewById(R.id.button_m14);
            this.mirrorButtonArray[14] = findViewById(R.id.button_m15);
        }
        for (int i = INDEX_MIRROR; i < this.MIRROR_BUTTON_SIZE; i += INDEX_MIRROR_3D) {
            this.mirrorButtonArray[i].setBackgroundResource(R.color.primary);
        }
        this.mirrorButtonArray[index].setBackgroundResource(R.color.app_background);
    }

    private void setRatioButtonBg(int index) {
        if (this.ratioButtonArray == null) {
            this.ratioButtonArray = new Button[this.RATIO_BUTTON_SIZE];
            this.ratioButtonArray[0] = findViewById(R.id.button11);
            this.ratioButtonArray[1] = findViewById(R.id.button21);
            this.ratioButtonArray[2] = findViewById(R.id.button12);
            this.ratioButtonArray[3] = findViewById(R.id.button32);
            this.ratioButtonArray[4] = findViewById(R.id.button23);
            this.ratioButtonArray[5] = findViewById(R.id.button43);
            this.ratioButtonArray[6] = findViewById(R.id.button34);
            this.ratioButtonArray[7] = findViewById(R.id.button45);
            this.ratioButtonArray[8] = findViewById(R.id.button57);
            this.ratioButtonArray[9] = findViewById(R.id.button169);
            this.ratioButtonArray[10] = findViewById(R.id.button916);
        }
        for (int i = INDEX_MIRROR; i < this.RATIO_BUTTON_SIZE; i += INDEX_MIRROR_3D) {
            this.ratioButtonArray[i].setBackgroundResource(R.drawable.selector_collage_ratio_button);
        }
        this.ratioButtonArray[index].setBackgroundResource(R.drawable.collage_ratio_bg_pressed);
    }

    void setSelectedTab(int index) {
        setTabBg(INDEX_MIRROR);
        int displayedChild = this.viewFlipper.getDisplayedChild();
        if (index == 0) {
            if (displayedChild != 0) {
                this.viewFlipper.setInAnimation(this.slideLeftIn);
                this.viewFlipper.setOutAnimation(this.slideRightOut);
                this.viewFlipper.setDisplayedChild(INDEX_MIRROR);
            } else {
                return;
            }
        }
        if (index == INDEX_MIRROR_3D) {
            setTabBg(INDEX_MIRROR_3D);
            if (displayedChild != INDEX_MIRROR_3D) {
                if (displayedChild == 0) {
                    this.viewFlipper.setInAnimation(this.slideRightIn);
                    this.viewFlipper.setOutAnimation(this.slideLeftOut);
                } else {
                    this.viewFlipper.setInAnimation(this.slideLeftIn);
                    this.viewFlipper.setOutAnimation(this.slideRightOut);
                }
                this.viewFlipper.setDisplayedChild(INDEX_MIRROR_3D);
            } else {
                return;
            }
        }
        if (index == INDEX_MIRROR_RATIO) {
            setTabBg(INDEX_MIRROR_RATIO);
            if (displayedChild != INDEX_MIRROR_RATIO) {
                if (displayedChild == 0) {
                    this.viewFlipper.setInAnimation(this.slideRightIn);
                    this.viewFlipper.setOutAnimation(this.slideLeftOut);
                } else {
                    this.viewFlipper.setInAnimation(this.slideLeftIn);
                    this.viewFlipper.setOutAnimation(this.slideRightOut);
                }
                this.viewFlipper.setDisplayedChild(INDEX_MIRROR_RATIO);
            } else {
                return;
            }
        }
        if (index == INDEX_MIRROR_EFFECT) {
            setTabBg(INDEX_MIRROR_EFFECT);
            this.effectFragment.setSelectedTabIndex(INDEX_MIRROR);
            if (displayedChild != INDEX_MIRROR_EFFECT) {
                if (displayedChild == 0 || displayedChild == INDEX_MIRROR_RATIO) {
                    this.viewFlipper.setInAnimation(this.slideRightIn);
                    this.viewFlipper.setOutAnimation(this.slideLeftOut);
                } else {
                    this.viewFlipper.setInAnimation(this.slideLeftIn);
                    this.viewFlipper.setOutAnimation(this.slideRightOut);
                }
                this.viewFlipper.setDisplayedChild(INDEX_MIRROR_EFFECT);
            } else {
                return;
            }
        }
        if (index == INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX) {
            setTabBg(INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX);
            this.effectFragment.setSelectedTabIndex(INDEX_MIRROR_3D);
            if (displayedChild != INDEX_MIRROR_EFFECT) {
                if (displayedChild == INDEX_MIRROR_ADJUSTMENT) {
                    this.viewFlipper.setInAnimation(this.slideLeftIn);
                    this.viewFlipper.setOutAnimation(this.slideRightOut);
                } else {
                    this.viewFlipper.setInAnimation(this.slideRightIn);
                    this.viewFlipper.setOutAnimation(this.slideLeftOut);
                }
                this.viewFlipper.setDisplayedChild(INDEX_MIRROR_EFFECT);
            } else {
                return;
            }
        }
        if (index == INDEX_MIRROR_ADJUSTMENT) {
            setTabBg(INDEX_MIRROR_ADJUSTMENT);
            this.effectFragment.showToolBar();
            if (displayedChild != INDEX_MIRROR_EFFECT) {
                this.viewFlipper.setInAnimation(this.slideRightIn);
                this.viewFlipper.setOutAnimation(this.slideLeftOut);
                this.viewFlipper.setDisplayedChild(INDEX_MIRROR_EFFECT);
            } else {
                return;
            }
        }
        if (index == INDEX_MIRROR_INVISIBLE_VIEW) {
            setTabBg(-1);
            if (displayedChild != INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX) {
                this.viewFlipper.setInAnimation(this.slideRightIn);
                this.viewFlipper.setOutAnimation(this.slideLeftOut);
                this.viewFlipper.setDisplayedChild(INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX);
            }
        }
    }

    private void setTabBg(int index) {
        this.currentSelectedTabIndex = index;
        if (this.tabButtonList == null) {
            this.tabButtonList = new View[TAB_SIZE];
            this.tabButtonList[INDEX_MIRROR] = findViewById(R.id.button_mirror);
            this.tabButtonList[INDEX_MIRROR_3D] = findViewById(R.id.button_mirror_3d);
            this.tabButtonList[INDEX_MIRROR_EFFECT] = findViewById(R.id.button_mirror_effect);
            this.tabButtonList[INDEX_MIRROR_RATIO] = findViewById(R.id.button_mirror_ratio);
            this.tabButtonList[INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX] = findViewById(R.id.button_mirror_frame);
            this.tabButtonList[INDEX_MIRROR_ADJUSTMENT] = findViewById(R.id.button_mirror_adj);
        }
        for (int i = INDEX_MIRROR; i < this.tabButtonList.length; i += INDEX_MIRROR_3D) {
            this.tabButtonList[i].setBackgroundResource(R.drawable.collage_footer_button);
        }
        if (index >= 0) {
            this.tabButtonList[index].setBackgroundResource(R.color.app_foreground);
        }
    }

    void clearViewFlipper() {
        this.viewFlipper.setInAnimation(null);
        this.viewFlipper.setOutAnimation(null);
        this.viewFlipper.setDisplayedChild(INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX);
        setTabBg(-1);
    }

    public void onBackPressed() {
        if (this.fontFragment != null && this.fontFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().remove(this.fontFragment).commit();
        } else if (this.viewFlipper.getDisplayedChild() == INDEX_MIRROR_EFFECT) {
            clearFxAndFrame();
            clearViewFlipper();
        } else if (!this.showText && this.canvasText != null) {
            this.showText = true;
            this.mainLayout.removeView(this.canvasText);
            this.mirrorView.postInvalidate();
            this.canvasText = null;
            Log.e(TAG, "replace fragment");
        } else if (this.viewFlipper.getDisplayedChild() != INDEX_MIRROR_INVISIBLE_VIEW_ACTUAL_INDEX) {
            clearViewFlipper();
        } else {
            backButtonAlertBuilder();
        }
    }

    private void backButtonAlertBuilder() {

        try {
            View view = View.inflate(this, R.layout.dialog_exit_layout, null);
            //showHomeScreenFacebookAds(view);
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            //alertDialogBuilder.setView(view);

            /*TextView dialog_heading = view.findViewById(R.id.dialog_heading);
            dialog_heading.setText("Want to Discard Editing?");

            TextView exit_app = view.findViewById(R.id.exit_app);
            exit_app.setText("Yes");

            TextView cancel_app = view.findViewById(R.id.cancel_app);
            cancel_app.setText("No");

            view.findViewById(R.id.exit_app).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    new SaveImageTask().execute(new Object[MirrorNewActivity.INDEX_MIRROR]);
                    alertDialog.dismiss();
                    MirrorNewActivity.this.finish();
                }
            });
            view.findViewById(R.id.cancel_app).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
//                    MirrorNewActivity.this.finish();

                }
            });*/
            alertDialogBuilder.setMessage("Want to Discard Editing?")
                    .setPositiveButton("No", null)
                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                            MirrorNewActivity.this.finish();
                        }
                    });
            this.alertDialog = alertDialogBuilder.create();
            this.alertDialog.setCancelable(false);
            this.alertDialog.show();
        } catch (Exception e) {
        }
    }

    void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        this.mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                loadInterstitialAd();
            }
        });
    }

    void showInterstitialAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void loadFbBanner() {
        if(Glob.isFB){
            adView = new AdView(MirrorNewActivity.this, getResources().getString(R.string.fb_banner), AdSize.BANNER_HEIGHT_50);
            LinearLayout adContainer = findViewById(R.id.banner_container);
            adContainer.addView(adView);
            adView.loadAd();
        }else if(Glob.isAdmob){
            com.google.android.gms.ads.AdView adview = new com.google.android.gms.ads.AdView(this);
            adview.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
            adview.setAdUnitId(getString(R.string.banner_ad_id));
            ((LinearLayout) findViewById(R.id.banner_container)).addView(adview);
            adview.loadAd(new AdRequest.Builder().build());
        }
    }

    public void loadFbInterstitial(){
        interstitialAd = new com.facebook.ads.InterstitialAd(this, getResources().getString(R.string.fb_interstitial));
        interstitialAd.loadAd();
    }
}
