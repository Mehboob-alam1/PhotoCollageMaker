package com.helloworld.photoeditor.collagelib;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.NinePatchDrawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdSize;
import com.helloworld.photoeditor.Glob;
import com.helloworld.photoeditor.R;
import com.helloworld.photoeditor.canvastext.ApplyTextInterface;
import com.helloworld.photoeditor.canvastext.CustomRelativeLayout;
import com.helloworld.photoeditor.canvastext.SingleTap;
import com.helloworld.photoeditor.canvastext.TextData;
import com.helloworld.photoeditor.collagelib.MyAdapter.CurrentCollageIndexChangedListener;
import com.helloworld.photoeditor.collagelib.RotationGestureDetector.OnRotationGestureListener;
import com.helloworld.photoeditor.common_lib.Parameter;
import com.helloworld.photoeditor.fragments.FontFragment;
import com.helloworld.photoeditor.fragments.FontFragment.FontChoosedListener;
import com.helloworld.photoeditor.fragments.FullEffectFragment;
import com.helloworld.photoeditor.fragments.FullEffectFragment.FullBitmapReady;
import com.helloworld.photoeditor.pointlist.Collage;
import com.helloworld.photoeditor.pointlist.CollageLayout;
import com.helloworld.photoeditor.pointlist.MaskPair;
import com.helloworld.photoeditor.share.ImageShareActivity;
import com.helloworld.photoeditor.utils.BlurBuilderNormal;
import com.helloworld.photoeditor.utils.LibUtility;
import com.commit451.nativestackblur.NativeStackBlur;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CollageActivity extends FragmentActivity {
    private static final String TAG = "CollageView";
    private static final float UPPER_SIZE_FOR_LOAD = 1500.0f;
    private int RATIO_BUTTON_SIZE = 11;
    private Bitmap[] bitmapList;
    private Bitmap btmDelete;
    private Bitmap btmScale;
    private CustomRelativeLayout canvasText;
    private MyAdapter collageAdapter;
    private RecyclerView collageRecyclerView;
    private CollageView collageView;
    private LinearLayout colorContainer;
    private ViewGroup contextFooter;
    private FontChoosedListener fontChoosedListener = new C05021();
    private FontFragment fontFragment;
    private FullEffectFragment fullEffectFragment;
    private int height;
    private boolean isScrapBook = false;
    private RotationGestureDetector mRotationDetector;
    private OnSeekBarChangeListener mSeekBarListener = new C05032();
    private RelativeLayout mainLayout;
    private float mulX = 1.0f;
    private float mulY = 1.0f;
    private NinePatchDrawable npd;
    private Parameter[] parameterList;
    private ArrayList<MyRecylceAdapterBase> patternAdapterList = new ArrayList();
    private Button[] ratioButtonArray;
    private AlertDialog saveImageAlert;
    private SeekBar seekBarPadding;
    private SeekBar seekBarRound;
    private SeekBar seekbarBlur;
    private SeekBar seekbarSize;
    private View selectFilterTextView;
    private boolean selectImageForAdj = false;
    private View selectSwapTextView;
    private boolean showText = false;
    private Animation slideLeftIn;
    private Animation slideLeftOut;
    private Animation slideRightIn;
    private Animation slideRightOut;
    private boolean swapMode = false;
    private View[] tabButtonList;
    private ArrayList<TextData> textDataList = new ArrayList<>();
    private ViewFlipper viewFlipper;
    private int width;
    private InterstitialAd mInterstitialAd;
    private LinearLayout adExitChoicesContainer;
    private LinearLayout adExitView;
    private AlertDialog alertDialog = null;
    private ProgressBar progressBarExitRefresh;
    private LinearLayout nativeAdContainerExitAds;
    private AdView adView;
    private com.google.android.gms.ads.AdView admob_adView;
    private com.facebook.ads.InterstitialAd interstitialAd;

    class C05021 implements FontChoosedListener {
        C05021() {
        }

        public void onOk(TextData textData) {
            if (CollageActivity.this.canvasText == null) {
                CollageActivity.this.addCanvasTextView();
            }
            CollageActivity.this.canvasText.addTextView(textData);
            CollageActivity.this.getSupportFragmentManager().beginTransaction().remove(CollageActivity.this.fontFragment).commit();
            Log.e(CollageActivity.TAG, "onOK called");
        }
    }

    class C05032 implements OnSeekBarChangeListener {
        C05032() {
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            int id = seekBar.getId();
            if (id == R.id.seekbar_round) {
                if (CollageActivity.this.collageView != null) {
                    CollageActivity.this.collageView.setCornerRadius((float) progress);
                }
            } else if (id == R.id.seekbar_padding) {
                if (CollageActivity.this.collageView != null) {
                    CollageActivity.this.collageView.setPathPadding(CollageActivity.this.collageView.currentCollageIndex, (float) progress);
                }
            } else if (id == R.id.seekbar_size) {
                if (CollageActivity.this.collageView != null) {
                    CollageActivity.this.collageView.setCollageSize(CollageActivity.this.collageView.sizeMatrix, progress);
                }
            } else if (id == R.id.seekbar_collage_blur) {
                float f = ((float) progress) / 4.0f;
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (seekBar.getId() == R.id.seekbar_collage_blur) {
                float radius = ((float) seekBar.getProgress()) / 4.0f;
                if (radius > 25.0f) {
                    radius = 25.0f;
                }
                if (radius < 0.0f) {
                    radius = 0.0f;
                }
                Log.e(CollageActivity.TAG, "blur radius " + radius);
                CollageActivity.this.collageView.setBlurBitmap((int) radius, false);
            }
        }
    }

    class C05065 implements CurrentCollageIndexChangedListener {
        C05065() {
        }

        public void onIndexChanged(int i) {
            CollageActivity.this.collageView.setCurrentCollageIndex(i);
        }
    }

    class C05087 implements CurrentCollageIndexChangedListener {
        C05087() {
        }

        public void onIndexChanged(int color) {
            CollageActivity.this.collageView.setPatternPaintColor(color);
        }
    }

    class BitmapWorkerTask extends AsyncTask<Bundle, Void, Void> {
        int arraySize;
        Bundle data;
        ProgressDialog progressDialog;
        Bundle savedInstanceState;

        BitmapWorkerTask() {
        }

        protected void onPreExecute() {
            this.progressDialog = new ProgressDialog(CollageActivity.this);
            this.progressDialog.setCancelable(false);
            this.progressDialog.setMessage("loading images!");
            this.progressDialog.show();
        }

        protected Void doInBackground(Bundle... params) {
            int i;
            Log.e("doInBG", "load");
            this.data = params[0];
            this.savedInstanceState = params[1];
            CollageActivity.this.isScrapBook = this.data.getBoolean("is_scrap_book", false);
            long[] selectedImageList = this.data.getLongArray("photo_id_list");
            int[] selectedImageOrientationList = this.data.getIntArray("photo_orientation_list");
            this.arraySize = 0;
            int maxDivider;
            if (selectedImageList == null) {
                String selectedImagePath = this.data.getString("selected_image_path");
                if (selectedImagePath != null) {
                    this.arraySize = 1;
                    CollageActivity.this.bitmapList = new Bitmap[this.arraySize];
                    maxDivider = this.arraySize;
                    if (maxDivider < 3) {
                        maxDivider = 3;
                    }
                    CollageActivity.this.bitmapList[0] = Utility.decodeFile(selectedImagePath, Utility.maxSizeForDimension(CollageActivity.this, maxDivider, CollageActivity.UPPER_SIZE_FOR_LOAD), CollageActivity.this.isScrapBook);
                }
            } else {
                this.arraySize = selectedImageList.length;
                CollageActivity.this.bitmapList = new Bitmap[this.arraySize];
                maxDivider = this.arraySize;
                if (maxDivider < 3) {
                    maxDivider = 3;
                }
                int requiredSize = Utility.maxSizeForDimension(CollageActivity.this, maxDivider, CollageActivity.UPPER_SIZE_FOR_LOAD);
                int loadingImageError = 0;
                for (i = 0; i < this.arraySize; i++) {
                    Bitmap bitmap = Utility.getScaledBitmapFromId(CollageActivity.this, selectedImageList[i], selectedImageOrientationList[i], requiredSize, CollageActivity.this.isScrapBook);
                    if (bitmap != null) {
                        CollageActivity.this.bitmapList[i] = bitmap;
                    } else {
                        loadingImageError++;
                    }
                }
                if (loadingImageError > 0) {
                    int newSize = this.arraySize - loadingImageError;
                    Bitmap[] arr = new Bitmap[newSize];
                    int j = 0;
                    for (i = 0; i < this.arraySize; i++) {
                        if (CollageActivity.this.bitmapList[i] != null) {
                            arr[j] = CollageActivity.this.bitmapList[i];
                            j++;
                        }
                    }
                    this.arraySize = newSize;
                    CollageActivity.this.bitmapList = arr;
                }
            }
            CollageActivity.this.parameterList = new Parameter[this.arraySize];
            for (i = 0; i < CollageActivity.this.parameterList.length; i++) {
                CollageActivity.this.parameterList[i] = new Parameter();
            }
            return null;
        }

        protected void onPostExecute(Void v) {
            try {
                this.progressDialog.dismiss();
            } catch (Exception e) {
            }
            if (this.arraySize <= 0) {
                Toast msg = Toast.makeText(CollageActivity.this, "Couldn't load images!", Toast.LENGTH_SHORT);
                msg.setGravity(17, msg.getXOffset() / 2, msg.getYOffset() / 2);
                msg.show();
                CollageActivity.this.finish();
                return;
            }
            if (Collage.collageIconArray[CollageActivity.this.bitmapList.length - 1] != CollageActivity.this.collageAdapter.iconList) {
                CollageActivity.this.collageAdapter.setData(Collage.collageIconArray[CollageActivity.this.bitmapList.length - 1]);
                CollageActivity.this.collageAdapter.notifyDataSetChanged();
                Log.e(CollageActivity.TAG, "change collage icons");
            }
            if (CollageActivity.this.isScrapBook) {
                CollageActivity.this.btmDelete = BitmapFactory.decodeResource(CollageActivity.this.getResources(), R.drawable.scrapbook_remove);
                CollageActivity.this.btmScale = BitmapFactory.decodeResource(CollageActivity.this.getResources(), R.drawable.scrapbook_scale);
            }
            if (CollageActivity.this.isScrapBook) {
                CollageActivity.this.npd = (NinePatchDrawable) ContextCompat.getDrawable(CollageActivity.this, R.drawable.shadow_7);
                Log.e(CollageActivity.TAG, "ndp width " + CollageActivity.this.npd.getMinimumHeight());
            }
            CollageActivity.this.collageView = new CollageView(CollageActivity.this, CollageActivity.this.width, CollageActivity.this.height);
            CollageActivity.this.mainLayout = CollageActivity.this.findViewById(R.id.collage_main_layout);
            CollageActivity.this.mainLayout.addView(CollageActivity.this.collageView);
            CollageActivity.this.viewFlipper.bringToFront();
            CollageActivity.this.slideLeftIn = AnimationUtils.loadAnimation(CollageActivity.this, R.anim.slide_in_left);
            CollageActivity.this.slideLeftOut = AnimationUtils.loadAnimation(CollageActivity.this, R.anim.slide_out_left);
            CollageActivity.this.slideRightIn = AnimationUtils.loadAnimation(CollageActivity.this, R.anim.slide_in_right);
            CollageActivity.this.slideRightOut = AnimationUtils.loadAnimation(CollageActivity.this, R.anim.slide_out_right);
            CollageActivity.this.addEffectFragment();
            if (this.arraySize == 1) {
                CollageActivity.this.setVisibilityForSingleImage();
            }
            if (CollageActivity.this.isScrapBook) {
                CollageActivity.this.setVisibilityForScrapbook();
            }
            CollageActivity.this.viewFlipper = CollageActivity.this.findViewById(R.id.collage_view_flipper);
            CollageActivity.this.viewFlipper.bringToFront();
            CollageActivity.this.findViewById(R.id.collage_footer).bringToFront();
            CollageActivity.this.findViewById(R.id.collage_header).bringToFront();
            CollageActivity.this.contextFooter = CollageActivity.this.findViewById(R.id.collage_context_menu);
            CollageActivity.this.contextFooter.bringToFront();
            CollageActivity.this.selectSwapTextView = CollageActivity.this.findViewById(R.id.select_image_swap);
            CollageActivity.this.selectSwapTextView.bringToFront();
            CollageActivity.this.selectSwapTextView.setVisibility(View.INVISIBLE);
            CollageActivity.this.selectFilterTextView = CollageActivity.this.findViewById(R.id.select_image_filter);
            CollageActivity.this.selectFilterTextView.bringToFront();
            CollageActivity.this.selectFilterTextView.setVisibility(View.INVISIBLE);
        }
    }

    class CollageView extends View {
        public static final int BACKGROUND_BLUR = 1;
        public static final int BACKGROUND_PATTERN = 0;
        private static final int INVALID_POINTER_ID = 1;
        public static final int PATTERN_SENTINEL = -1;
        static final float RATIO_CONSTANT = 1.25f;
        private static final int UPPER_SIZE_LIMIT = 2048;
        float MIN_ZOOM;
        RectF above;
        int animEpsilon = 20;
        int animHalfTime = ((this.animationLimit / 2) + 1);
        int animSizeSeekbarProgress = 0;
        boolean animate = false;
        int animationCount = 0;
        int animationDurationLimit = 50;
        int animationLimit = 31;
        private Runnable animator;
        int backgroundMode;
        Bitmap blurBitmap;
        BlurBuilderNormal blurBuilderNormal;
        int blurRadius = 14;
        RectF blurRectDst;
        Rect blurRectSrc;
        Paint borderPaint;
        RectF bottom;
        RectF bottomLeft;
        RectF bottomRight;
        Paint circlePaint;
        float cornerRadius = 0.0f;
        int currentCollageIndex = 0;
        RectF drawingAreaRect;
        final float epsilon;
        float finalAngle;
        Bitmap frameBitmap;
        int frameDuration = 10;
        RectF frameRect;
        Matrix identityMatrix = new Matrix();
        boolean isInCircle;
        boolean isOnCross;
        RectF left;
        private int mActivePointerId;
        float mLastTouchX;
        float mLastTouchY;
        private ScaleGestureDetector mScaleDetector;
        float mScaleFactor;
        private GestureDetectorCompat mTouchDetector;
        Bitmap[] maskBitmapArray;
        int[] maskResIdList = new int[]{R.drawable.mask_butterfly, R.drawable.mask_cloud, R.drawable.mask_clover, R.drawable.mask_leaf, R.drawable.mask_left_foot, R.drawable.mask_diamond, R.drawable.mask_santa, R.drawable.mask_snowman, R.drawable.mask_paw, R.drawable.mask_egg, R.drawable.mask_twitter, R.drawable.mask_circle, R.drawable.mask_hexagon, R.drawable.mask_heart};
        float[] matrixValues;
        boolean move;
        int offsetX;
        int offsetY;
        boolean orthogonal;
        float paddingDistance = 0.0f;
        Paint paint = new Paint();
        Paint paintGray;
        Bitmap patternBitmap;
        Paint patternPaint = new Paint(1);
        int previousIndex;
        float[] pts;
        Rect rectAnim;
        RectF right;
        OnRotationGestureListener rotateListener;
        Shape scaleShape;
        int screenHeight;
        int screenWidth;
        int shapeIndex = -1;
        List<ShapeLayout> shapeLayoutList = new ArrayList<>();
        Matrix sizeMatrix = new Matrix();
        Matrix sizeMatrixSaved;
        float sizeScale = 1.0f;
        ArrayList<Float> smallestDistanceList = new ArrayList<>();
        private float startAngle;
        Matrix startMatrix;
        long startTime = System.nanoTime();
        Matrix textMatrix;
        RectF topLeft;
        RectF topRight;
        float[] values;
        float xscale = 1.0f;
        float yscale = 1.0f;
        PointF zoomStart;

        class MyGestureListener extends SimpleOnGestureListener {
            private static final String DEBUG_TAG = "Gestures";

            MyGestureListener() {
            }

            public boolean onSingleTapConfirmed(MotionEvent event) {
                Log.d(DEBUG_TAG, "onSingleTapConfirmed: ");
                return true;
            }

            public boolean onSingleTapUp(MotionEvent event) {
                Log.d(DEBUG_TAG, "onSingleTapUp: ");
                if (!CollageView.this.isOnCross) {
                    CollageActivity.this.collageView.selectCurrentShape(event.getX(), event.getY(), true);
                }
                return true;
            }
        }

        private class ScaleListener extends SimpleOnScaleGestureListener {
            private ScaleListener() {
            }

            public boolean onScale(ScaleGestureDetector detector) {
                if (CollageView.this.shapeIndex >= 0) {
                    CollageView.this.mScaleFactor = detector.getScaleFactor();
                    detector.isInProgress();
                    CollageView.this.mScaleFactor = Math.max(0.1f, Math.min(CollageView.this.mScaleFactor, 5.0f));
                    CollageView.this.scaleShape = CollageView.this.shapeLayoutList.get(CollageView.this.currentCollageIndex).shapeArr[CollageView.this.shapeIndex];
                    if (CollageActivity.this.isScrapBook) {
                        CollageView.this.scaleShape.bitmapMatrixScaleScrapBook(CollageView.this.mScaleFactor, CollageView.this.mScaleFactor);
                    } else {
                        CollageView.this.scaleShape.bitmapMatrixScale(CollageView.this.mScaleFactor, CollageView.this.mScaleFactor, CollageView.this.scaleShape.bounds.centerX(), CollageView.this.scaleShape.bounds.centerY());
                    }
                    CollageView.this.invalidate();
                    CollageView.this.requestLayout();
                }
                return true;
            }
        }

        @SuppressLint({"NewApi"})
        public CollageView(Context context, int width, int height) {
            super(context);
            this.animator = new Runnable(/*CollageActivity.this*/) {  //Dt Change
                public void run() {
                    boolean scheduleNewFrame = false;
                    int iter = ((int) (((float) (System.nanoTime() - CollageView.this.startTime)) / 1000000.0f)) / CollageView.this.animationDurationLimit;
                    if (iter <= 0) {
                        iter = 1;
                    }
                    CollageView collageView;
                    if (CollageView.this.animationCount == 0) {
                        collageView = CollageActivity.this.collageView;
                        collageView.animationCount++;
                    } else {
                        collageView = CollageActivity.this.collageView;
                        collageView.animationCount += iter;
                    }
                    CollageView.this.setCollageSize(CollageView.this.sizeMatrix, CollageView.this.animSize(CollageView.this.animationCount));
                    if (CollageView.this.animationCount < CollageView.this.animationLimit) {
                        scheduleNewFrame = true;
                    } else {
                        CollageView.this.animate = false;
                    }
                    if (scheduleNewFrame) {
                        CollageView.this.postDelayed(this, (long) CollageView.this.frameDuration);
                    } else {
                        CollageView.this.sizeMatrix.set(CollageView.this.sizeMatrixSaved);
                    }
                    CollageView.this.shapeLayoutList.get(CollageView.this.currentCollageIndex).shapeArr[0].f508r.roundOut(CollageView.this.rectAnim);
                    CollageView.this.invalidate(CollageView.this.rectAnim);
                    CollageView.this.startTime = System.nanoTime();
                }
            };
            this.rectAnim = new Rect();
            this.textMatrix = new Matrix();
            this.blurRectDst = new RectF();
            this.drawingAreaRect = new RectF();
            this.above = new RectF();
            this.left = new RectF();
            this.right = new RectF();
            this.bottom = new RectF();
            this.move = false;
            this.paintGray = new Paint(1);
            this.mActivePointerId = 1;
            this.zoomStart = new PointF();
            this.startMatrix = new Matrix();
            this.startAngle = 0.0f;
            this.MIN_ZOOM = 0.1f;
            this.isInCircle = false;
            this.isOnCross = false;
            this.orthogonal = false;
            this.mScaleFactor = 1.0f;
            this.matrixValues = new float[9];
            this.finalAngle = 0.0f;
            this.epsilon = 4.0f;
            this.rotateListener = new OnRotationGestureListener(/*CollageActivity.this*/) {  //Dt Change
                public void OnRotation(RotationGestureDetector rotationGestureDetector) {
                    if (CollageView.this.shapeIndex >= 0) {
                        float angle = rotationGestureDetector.getAngle();
                        CollageView.this.scaleShape = CollageView.this.shapeLayoutList.get(CollageView.this.currentCollageIndex).shapeArr[CollageView.this.shapeIndex];
                        float rotation = CollageView.this.getMatrixRotation(CollageView.this.scaleShape.bitmapMatrix);
                        if ((rotation == 0.0f || rotation == 90.0f || rotation == 180.0f || rotation == -180.0f || rotation == -90.0f) && Math.abs(CollageView.this.finalAngle - angle) < 4.0f) {
                            CollageView.this.orthogonal = true;
                            return;
                        }
                        if (Math.abs((rotation - CollageView.this.finalAngle) + angle) < 4.0f) {
                            angle = CollageView.this.finalAngle - rotation;
                            CollageView.this.orthogonal = true;
                        }
                        if (Math.abs(90.0f - ((rotation - CollageView.this.finalAngle) + angle)) < 4.0f) {
                            angle = (CollageView.this.finalAngle + 90.0f) - rotation;
                            CollageView.this.orthogonal = true;
                        }
                        if (Math.abs(180.0f - ((rotation - CollageView.this.finalAngle) + angle)) < 4.0f) {
                            angle = (CollageView.this.finalAngle + 180.0f) - rotation;
                            CollageView.this.orthogonal = true;
                        }
                        if (Math.abs(-180.0f - ((rotation - CollageView.this.finalAngle) + angle)) < 4.0f) {
                            angle = (CollageView.this.finalAngle - 0.024902344f) - rotation;
                            CollageView.this.orthogonal = true;
                        }
                        if (Math.abs(-90.0f - ((rotation - CollageView.this.finalAngle) + angle)) < 4.0f) {
                            angle = (CollageView.this.finalAngle - 0.049804688f) - rotation;
                            CollageView.this.orthogonal = true;
                        } else {
                            CollageView.this.orthogonal = false;
                        }
                        CollageView.this.scaleShape.bitmapMatrixRotate(CollageView.this.finalAngle - angle);
                        CollageView.this.finalAngle = angle;
                        CollageView.this.invalidate();
                        CollageView.this.requestLayout();
                    }
                }
            };
            this.values = new float[9];
            this.backgroundMode = 0;
            this.blurRectSrc = new Rect();
            this.maskBitmapArray = new Bitmap[this.maskResIdList.length];
            this.borderPaint = new Paint(1);
            this.borderPaint.setColor(getResources().getColor(R.color.blue));
            this.borderPaint.setStyle(Style.STROKE);
            this.borderPaint.setStrokeWidth(10.0f);
            this.screenWidth = width;
            this.screenHeight = height;
            this.circlePaint = new Paint();
            this.circlePaint.setColor(SupportMenu.CATEGORY_MASK);
            this.identityMatrix.reset();
            this.topLeft = new RectF((float) (width * 0), (float) (height * 0), 0.5f * ((float) width), 0.5f * ((float) height));
            this.topRight = new RectF(0.5f * ((float) width), 0.0f * ((float) height), 1.0f * ((float) width), 0.5f * ((float) height));
            this.bottomLeft = new RectF((float) (width * 0), 0.5f * ((float) height), 0.5f * ((float) width), 1.0f * ((float) height));
            this.bottomRight = new RectF(0.5f * ((float) width), 0.5f * ((float) height), 1.0f * ((float) width), 1.0f * ((float) height));
            Path pathTopLeft = new Path();
            Path pathTopRight = new Path();
            Path pathBottomLeft = new Path();
            Path pathBottomRight = new Path();
            pathTopLeft.addRect(this.topLeft, Direction.CCW);
            pathTopRight.addRect(this.topRight, Direction.CCW);
            pathBottomLeft.addRect(this.bottomLeft, Direction.CCW);
            pathBottomRight.addRect(this.bottomRight, Direction.CCW);
            this.mTouchDetector = new GestureDetectorCompat(context, new MyGestureListener());
            this.mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
            CollageActivity.this.mRotationDetector = new RotationGestureDetector(this.rotateListener);
            calculateOffset();
            this.patternPaint = new Paint(1);
            this.patternPaint.setColor(-1);
            createShapeList(CollageActivity.this.bitmapList.length, width, height);
            this.paintGray.setColor(-12303292);
        }

        private void calculateOffset() {
            PointF scale = getRatio();
            this.offsetX = (int) ((((float) CollageActivity.this.width) - (scale.x * ((float) CollageActivity.this.width))) / 2.0f);
            this.offsetY = (int) ((((float) CollageActivity.this.height) - (scale.y * ((float) CollageActivity.this.width))) / 2.0f);
        }

        public void setCropBitmap(int left, int top, int right, int bottom) {
            if (this.shapeIndex >= 0) {
                Bitmap sourceBitmap = CollageActivity.this.bitmapList[this.shapeIndex];
                boolean isFilter = sourceBitmap != this.shapeLayoutList.get(0).shapeArr[this.shapeIndex].getBitmap();
                if (isFilter) {
                    doCrop(left, top, right, bottom, sourceBitmap, false, false);
                    doCrop(left, top, right, bottom, this.shapeLayoutList.get(0).shapeArr[this.shapeIndex].getBitmap(), true, true);
                } else {
                    doCrop(left, top, right, bottom, sourceBitmap, false, true);
                }
                if (!(!isFilter || CollageActivity.this.parameterList == null || CollageActivity.this.parameterList[this.shapeIndex] == null)) {
                    CollageActivity.this.parameterList[this.shapeIndex].setId(Parameter.uniqueId.getAndIncrement());
                }
                invalidate();
            }
        }

        public void doCrop(int left, int top, int right, int bottom, Bitmap sourceBitmap, boolean isFilter, boolean last) {
            Bitmap localCropBtm = sourceBitmap;
            int bitmapWidth = sourceBitmap.getWidth();
            int bitmapHeight = sourceBitmap.getHeight();
            if (right > bitmapWidth) {
                right = bitmapWidth;
            }
            if (bottom > bitmapHeight) {
                bottom = bitmapHeight;
            }
            if (right - left > 0 && bottom - top > 0) {
                if (VERSION.SDK_INT < 12) {
                    sourceBitmap = BlurBuilderNormal.createCroppedBitmap(localCropBtm, left, top, right - left, bottom - top, false);
                } else {
                    sourceBitmap = Bitmap.createBitmap(localCropBtm, left, top, right - left, bottom - top);
                }
                if (localCropBtm != sourceBitmap) {
                    localCropBtm.recycle();
                }
                if (!isFilter) {
                    CollageActivity.this.bitmapList[this.shapeIndex] = sourceBitmap;
                }
                if (last) {
                    int i = 0;
                    while (this.shapeLayoutList.size() > 0) {
                        this.shapeLayoutList.get(i).shapeArr[this.shapeIndex].setBitmap(sourceBitmap, false);
                        if (CollageActivity.this.isScrapBook) {
                            this.shapeLayoutList.get(i).shapeArr[this.shapeIndex].resetDashPaths();
                        }
                        i += 0;
                    }
                }
            }
        }

        public String saveBitmap(int width, int height) {
            int i;
            int btmWidth = (int) (((float) width) * CollageActivity.this.collageView.xscale);
            int btmHeight = (int) (((float) width) * CollageActivity.this.collageView.yscale);
            float btmScale = ((float) Utility.maxSizeForSave(CollageActivity.this, 2048.0f)) / ((float) Math.max(btmWidth, btmHeight));
            int newBtmWidth = (int) (((float) btmWidth) * btmScale);
            int newBtmHeight = (int) (((float) btmHeight) * btmScale);
            if (newBtmWidth <= 0) {
                newBtmWidth = btmWidth;
                Log.e(CollageActivity.TAG, "newBtmWidth");
            }
            if (newBtmHeight <= 0) {
                newBtmHeight = btmHeight;
                Log.e(CollageActivity.TAG, "newBtmHeight");
            }
            Bitmap savedBitmap = Bitmap.createBitmap(newBtmWidth, newBtmHeight, Config.ARGB_8888);
            Canvas bitmapCanvas = new Canvas(savedBitmap);
            ShapeLayout arr = this.shapeLayoutList.get(this.currentCollageIndex);
            Matrix sizeMat = new Matrix();
            sizeMat.reset();
            sizeMat.preScale(btmScale, btmScale);
            bitmapCanvas.setMatrix(sizeMat);
            if (this.backgroundMode == 0) {
                bitmapCanvas.drawRect(0.0f, 0.0f, (float) btmWidth, (float) btmHeight, this.patternPaint);
            }
            if (!(this.blurBitmap == null || this.blurBitmap.isRecycled() || this.backgroundMode != 1)) {
                RectF rectF = new RectF(0.0f, 0.0f, (float) btmWidth, (float) btmHeight);
                bitmapCanvas.drawBitmap(this.blurBitmap, this.blurRectSrc, rectF, this.paint);
            }
            sizeMat.postScale(this.sizeScale, this.sizeScale, ((float) newBtmWidth) / 2.0f, ((float) newBtmHeight) / 2.0f);
            sizeMat.preTranslate((float) (-this.offsetX), (float) (-this.offsetY));
            bitmapCanvas.setMatrix(sizeMat);
            @SuppressLint("WrongConstant") int q = bitmapCanvas.saveLayer(((float) (-width)) / this.sizeScale, ((float) (-height)) / this.sizeScale, ((float) this.offsetX) + (((float) width) / this.sizeScale), ((float) this.offsetY) + (((float) height) / this.sizeScale), null, 31);
            for (i = 0; i < arr.shapeArr.length; i++) {
                boolean drawPorterClear = false;
                if (i == arr.getClearIndex()) {
                    drawPorterClear = true;
                }
                Log.e(CollageActivity.TAG, "drawPorterClear " + drawPorterClear);
                if (CollageActivity.this.isScrapBook) {
                    arr.shapeArr[i].drawShapeForScrapBook(bitmapCanvas, newBtmWidth, newBtmHeight, false, false);
                } else {
                    arr.shapeArr[i].drawShapeForSave(bitmapCanvas, newBtmWidth, newBtmHeight, q, drawPorterClear);
                }
            }
            if (CollageActivity.this.textDataList != null) {
                for (i = 0; i < CollageActivity.this.textDataList.size(); i++) {
                    Matrix mat = new Matrix();
                    mat.set(CollageActivity.this.textDataList.get(i).imageSaveMatrix);
                    mat.postTranslate((float) (-this.offsetX), (float) (-this.offsetY));
                    mat.postScale(btmScale, btmScale);
                    bitmapCanvas.setMatrix(mat);
                    bitmapCanvas.drawText(CollageActivity.this.textDataList.get(i).message, CollageActivity.this.textDataList.get(i).xPos, CollageActivity.this.textDataList.get(i).yPos, CollageActivity.this.textDataList.get(i).textPaint);
                }
            }
            bitmapCanvas.restoreToCount(q);

//            String resultPath = String.valueOf(Environment.getExternalStorageDirectory().toString()) + CollageActivity.this.getString(R.string.directory) + String.valueOf(System.currentTimeMillis()) + ".jpg";
            String resultPath = Environment.getExternalStorageDirectory().toString() + "/" + getString(R.string.app_name) + "/" + System.currentTimeMillis() + ".jpg";
            new File(resultPath).getParentFile().mkdirs();
            try {
                OutputStream fileOutputStream = new FileOutputStream(resultPath);
                savedBitmap.compress(CompressFormat.JPEG, 90, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            savedBitmap.recycle();
            return resultPath;
        }

        int getMaskIndex(int resId) {
            for (int i = 0; i < this.maskResIdList.length; i++) {
                if (resId == this.maskResIdList[i]) {
                    return i;
                }
            }
            return -1;
        }

        private void createShapeList(int shapeCount, int width, int height) {
            this.shapeLayoutList.clear();
            this.smallestDistanceList.clear();
            Collage collage = Collage.CreateCollage(shapeCount, width, width, CollageActivity.this.isScrapBook);
            int size = ((CollageLayout) collage.collageLayoutList.get(0)).shapeList.size();
            Log.e(CollageActivity.TAG, "bitmapList.length " + CollageActivity.this.bitmapList.length);
            int i = 0;
            while (i < collage.collageLayoutList.size()) {
                Shape[] shapeArray = new Shape[size];
                for (int j = 0; j < shapeCount; j++) {
                    boolean masked = false;
                    int resId = 0;
                    if (!(((CollageLayout) collage.collageLayoutList.get(i)).maskPairList == null || ((CollageLayout) collage.collageLayoutList.get(i)).maskPairList.isEmpty())) {
                        for (MaskPair maskPair : ((CollageLayout) collage.collageLayoutList.get(i)).maskPairList) {
                            if (j == maskPair.index) {
                                masked = true;
                                resId = maskPair.id;
                            }
                        }
                    }
                    if (masked) {
                        Bitmap maskBitmap = null;
                        int maskIndex = getMaskIndex(resId);
                        if (maskIndex >= 0) {
                            if (this.maskBitmapArray == null) {
                                this.maskBitmapArray = new Bitmap[this.maskResIdList.length];
                            }
                            if (this.maskBitmapArray[maskIndex] == null) {
                                this.maskBitmapArray[maskIndex] = loadMaskBitmap2(resId);
                                Log.e(CollageActivity.TAG, "load mask bitmap from factory");
                            } else {
                                Log.e(CollageActivity.TAG, "load mask bitmap from pool");
                            }
                            maskBitmap = this.maskBitmapArray[maskIndex];
                        }
                        shapeArray[j] = new Shape((PointF[]) ((CollageLayout) collage.collageLayoutList.get(i)).shapeList.get(j), CollageActivity.this.bitmapList[j], null, this.offsetX, this.offsetY, maskBitmap, CollageActivity.this.isScrapBook, j, false, CollageActivity.this.btmDelete, CollageActivity.this.btmScale, this.screenWidth);
                        if (CollageActivity.this.isScrapBook) {
                            shapeArray[j].initScrapBook(CollageActivity.this.npd);
                        }
                    } else {
                        shapeArray[j] = new Shape((PointF[]) ((CollageLayout) collage.collageLayoutList.get(i)).shapeList.get(j), CollageActivity.this.bitmapList[j], ((CollageLayout) collage.collageLayoutList.get(i)).getexceptionIndex(j), this.offsetX, this.offsetY, CollageActivity.this.isScrapBook, j, false, CollageActivity.this.btmDelete, CollageActivity.this.btmScale, this.screenWidth);
                        if (CollageActivity.this.isScrapBook) {
                            shapeArray[j].initScrapBook(CollageActivity.this.npd);
                        }
                    }
                }
                this.smallestDistanceList.add(Float.valueOf(smallestDistance(shapeArray)));
                ShapeLayout shapeLayout = new ShapeLayout(shapeArray);
                shapeLayout.setClearIndex(((CollageLayout) collage.collageLayoutList.get(i)).getClearIndex());
                this.shapeLayoutList.add(shapeLayout);
                i++;
            }
            if (!CollageActivity.this.isScrapBook) {
                if (shapeCount != 1) {
                    for (int index = 0; index < this.shapeLayoutList.size(); index++) {
                        setPathPadding(index, (float) getResources().getInteger(R.integer.default_space_value));
                        for (Shape scaleMatrix : this.shapeLayoutList.get(index).shapeArr) {
                            scaleMatrix.setScaleMatrix(1);
                        }
                    }
                    setCollageSize(this.sizeMatrix, getResources().getInteger(R.integer.default_ssize_value));
                } else if (CollageActivity.this.bitmapList.length == 1) {
                    setCollageSize(this.sizeMatrix, getResources().getInteger(R.integer.default_ssize_value));
                }
            }
        }

        private int setShapeScaleMatrix(int mode) {
            if (this.shapeIndex < 0) {
                return -1;
            }
            int message = this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[this.shapeIndex].setScaleMatrix(mode);
            invalidate();
            return message;
        }

        private void deleteBitmap(int index, int width, int height) {
//            Log.e(CollageActivity.TAG, Param.INDEX + index);
            Shape[] scrapBookTemp = this.shapeLayoutList.get(0).shapeArr;
            if (index >= 0 && index < this.shapeLayoutList.get(0).shapeArr.length) {
                int i;
                int newSize = this.shapeLayoutList.get(0).shapeArr.length - 1;
                Bitmap[] currentBitmapListTemp = new Bitmap[newSize];
                Bitmap[] bitmapListTemp = new Bitmap[newSize];
                int j = 0;
                for (i = 0; i < currentBitmapListTemp.length + 1; i++) {
                    if (i != index) {
                        currentBitmapListTemp[j] = this.shapeLayoutList.get(0).shapeArr[i].getBitmap();
                        bitmapListTemp[j] = CollageActivity.this.bitmapList[i];
                        j++;
                    }
                }
                CollageActivity.this.bitmapList[index].recycle();
                this.shapeLayoutList.get(0).shapeArr[index].getBitmap().recycle();
                this.shapeLayoutList.clear();
                this.smallestDistanceList.clear();
                Collage collage = Collage.CreateCollage(newSize, width, width, CollageActivity.this.isScrapBook);
                int size = ((CollageLayout) collage.collageLayoutList.get(0)).shapeList.size();
                CollageActivity.this.bitmapList = bitmapListTemp;
                i = 0;
                while (i < collage.collageLayoutList.size()) {
                    Shape[] shapeArray = new Shape[size];
                    for (j = 0; j < currentBitmapListTemp.length; j++) {
                        boolean masked = false;
                        int resId = 0;
                        if (!(((CollageLayout) collage.collageLayoutList.get(i)).maskPairList == null || ((CollageLayout) collage.collageLayoutList.get(i)).maskPairList.isEmpty())) {
                            for (MaskPair maskPair : ((CollageLayout) collage.collageLayoutList.get(i)).maskPairList) {
                                if (j == maskPair.index) {
                                    masked = true;
                                    resId = maskPair.id;
                                }
                            }
                        }
                        if (masked) {
                            Bitmap maskBitmap = null;
                            int maskIndez = getMaskIndex(resId);
                            if (maskIndez >= 0) {
                                if (this.maskBitmapArray == null) {
                                    this.maskBitmapArray = new Bitmap[this.maskResIdList.length];
                                }
                                if (this.maskBitmapArray[maskIndez] == null) {
                                    this.maskBitmapArray[maskIndez] = loadMaskBitmap2(resId);
                                    Log.e(CollageActivity.TAG, "load mask bitmap from factory");
                                } else {
                                    Log.e(CollageActivity.TAG, "load mask bitmap from pool");
                                }
                                maskBitmap = this.maskBitmapArray[maskIndez];
                            }
                            shapeArray[j] = new Shape((PointF[]) ((CollageLayout) collage.collageLayoutList.get(i)).shapeList.get(j), currentBitmapListTemp[j], null, this.offsetX, this.offsetY, maskBitmap, CollageActivity.this.isScrapBook, j, true, CollageActivity.this.btmDelete, CollageActivity.this.btmScale, this.screenWidth);
                            if (CollageActivity.this.isScrapBook) {
                                shapeArray[j].initScrapBook(CollageActivity.this.npd);
                            }
                        } else {
                            shapeArray[j] = new Shape((PointF[]) ((CollageLayout) collage.collageLayoutList.get(i)).shapeList.get(j), currentBitmapListTemp[j], ((CollageLayout) collage.collageLayoutList.get(i)).getexceptionIndex(j), this.offsetX, this.offsetY, CollageActivity.this.isScrapBook, j, true, CollageActivity.this.btmDelete, CollageActivity.this.btmScale, this.screenWidth);
                            if (CollageActivity.this.isScrapBook) {
                                shapeArray[j].initScrapBook(CollageActivity.this.npd);
                            }
                        }
                    }
                    if (CollageActivity.this.isScrapBook) {
                        for (int k = 0; k < scrapBookTemp.length; k++) {
                            if (k < index) {
                                shapeArray[k].bitmapMatrix.set(scrapBookTemp[k].bitmapMatrix);
                            }
                            if (k > index) {
                                shapeArray[k - 1].bitmapMatrix.set(scrapBookTemp[k].bitmapMatrix);
                            }
                        }
                    }
                    ShapeLayout shapeLayout = new ShapeLayout(shapeArray);
                    shapeLayout.setClearIndex(((CollageLayout) collage.collageLayoutList.get(i)).getClearIndex());
                    this.shapeLayoutList.add(shapeLayout);
                    this.smallestDistanceList.add(Float.valueOf(smallestDistance(shapeArray)));
                    i++;
                }
                this.currentCollageIndex = 0;
                CollageActivity.this.collageAdapter.selectedPosition = 0;
                CollageActivity.this.collageAdapter.setData(Collage.collageIconArray[newSize - 1]);
                CollageActivity.this.collageAdapter.notifyDataSetChanged();
                if (!CollageActivity.this.isScrapBook) {
                    updateShapeListForRatio(width, height);
                }
                unselectShapes();
                for (i = 0; i < this.shapeLayoutList.get(0).shapeArr.length; i++) {
                    Log.e(CollageActivity.TAG, "i " + i + "is recylced " + this.shapeLayoutList.get(0).shapeArr[i].getBitmap().isRecycled());
                }
                invalidate();
                if (currentBitmapListTemp.length == 1) {
                    CollageActivity.this.setVisibilityForSingleImage();
                }
                if (newSize == 1) {
                    setPathPadding(0, 0.0f);
                    if (this.sizeScale == 1.0f && !CollageActivity.this.isScrapBook) {
                        setCollageSize(this.sizeMatrix, getResources().getInteger(R.integer.default_ssize_value));
                    }
                }
            }
        }

        Bitmap loadMaskBitmapx(int resId) {
            new Options().inPreferredConfig = Config.ARGB_8888;
            Bitmap source = BitmapFactory.decodeResource(getResources(), resId);
            Bitmap mask = source.extractAlpha();
            source.recycle();
            return mask;
        }

        Bitmap loadMaskBitmap2(int resId) {
            return convertToAlphaMask(BitmapFactory.decodeResource(getResources(), resId));
        }

        private Bitmap convertToAlphaMask(Bitmap b) {
            Bitmap a = Bitmap.createBitmap(b.getWidth(), b.getHeight(), Config.ALPHA_8);
            new Canvas(a).drawBitmap(b, 0.0f, 0.0f, null);
            b.recycle();
            return a;
        }

        public float smallestDistance(Shape[] shapeArray) {
            float smallestDistance = shapeArray[0].smallestDistance();
            for (Shape smallestDistance2 : shapeArray) {
                float distance = smallestDistance2.smallestDistance();
                if (distance < smallestDistance) {
                    smallestDistance = distance;
                }
            }
            return smallestDistance;
        }

        private void updateShapeListForRatio(int width, int height) {
            int shapeCount = this.shapeLayoutList.get(0).shapeArr.length;
            PointF scale = getRatio();
            calculateOffset();
            Collage collage = Collage.CreateCollage(shapeCount, (int) (scale.x * ((float) width)), (int) (((float) width) * scale.y), CollageActivity.this.isScrapBook);
            this.smallestDistanceList.clear();
            for (int index = 0; index < this.shapeLayoutList.size(); index++) {
                if (shapeCount == 1) {
                    this.shapeLayoutList.get(index).shapeArr[0].changeRatio((PointF[]) ((CollageLayout) collage.collageLayoutList.get(index)).shapeList.get(0), null, this.offsetX, this.offsetY, CollageActivity.this.isScrapBook, 0, (int) (scale.x * ((float) width)), (int) (((float) width) * scale.y));
                } else {
                    for (int j = 0; j < shapeCount; j++) {
                        this.shapeLayoutList.get(index).shapeArr[j].changeRatio((PointF[]) ((CollageLayout) collage.collageLayoutList.get(index)).shapeList.get(j), null, this.offsetX, this.offsetY, CollageActivity.this.isScrapBook, j, (int) (scale.x * ((float) width)), (int) (((float) width) * scale.y));
                    }
                }
                this.smallestDistanceList.add(Float.valueOf(smallestDistance(this.shapeLayoutList.get(index).shapeArr)));
                setPathPadding(index, this.paddingDistance);
                if (!CollageActivity.this.isScrapBook) {
                    for (Shape scaleMatrix : this.shapeLayoutList.get(index).shapeArr) {
                        scaleMatrix.setScaleMatrix(1);
                    }
                }
            }
            setCornerRadius(this.cornerRadius);
            if (this.blurBitmap != null) {
                setBlurRect2((float) this.blurBitmap.getWidth(), (float) this.blurBitmap.getHeight());
            }
            postInvalidate();
        }

        PointF getRatio() {
            this.yscale = 1.0f;
            this.xscale = 1.0f;
            this.yscale = CollageActivity.this.mulY / CollageActivity.this.mulX;
            if (!CollageActivity.this.isScrapBook && this.yscale > RATIO_CONSTANT) {
                this.xscale = RATIO_CONSTANT / this.yscale;
                this.yscale = RATIO_CONSTANT;
            }
            return new PointF(this.xscale, this.yscale);
        }

        private void updateShapeListForFilterBitmap(Bitmap bitmap) {
            if (this.shapeIndex >= 0) {
                for (int i = 0; i < this.shapeLayoutList.size(); i++) {
                    this.shapeLayoutList.get(i).shapeArr[this.shapeIndex].setBitmap(bitmap, true);
                }
            }
        }

        void updateParamList(Parameter p) {
            if (this.shapeIndex >= 0) {
                CollageActivity.this.parameterList[this.shapeIndex] = new Parameter(p);
            }
        }

        @SuppressLint({"WrongConstant"})
        private void swapBitmaps(int index1, int index2) {
            Bitmap bitmap1 = this.shapeLayoutList.get(0).shapeArr[index1].getBitmap();
            Bitmap bitmap2 = this.shapeLayoutList.get(0).shapeArr[index2].getBitmap();
            for (int i = 0; i < this.shapeLayoutList.size(); i++) {
                this.shapeLayoutList.get(i).shapeArr[index1].setBitmap(bitmap2, false);
                this.shapeLayoutList.get(i).shapeArr[index2].setBitmap(bitmap1, false);
            }
            Bitmap temp = CollageActivity.this.bitmapList[index1];
            CollageActivity.this.bitmapList[index1] = CollageActivity.this.bitmapList[index2];
            CollageActivity.this.bitmapList[index2] = temp;
            Parameter tempParam = CollageActivity.this.parameterList[index1];
            CollageActivity.this.parameterList[index1] = CollageActivity.this.parameterList[index2];
            CollageActivity.this.parameterList[index2] = tempParam;
            float sd = this.smallestDistanceList.get(index1).floatValue();
            this.smallestDistanceList.set(index1, this.smallestDistanceList.get(index2));
            this.smallestDistanceList.set(index2, Float.valueOf(sd));
            CollageActivity.this.selectSwapTextView.setVisibility(INVISIBLE);
            unselectShapes();
        }

        void setCurrentCollageIndex(int index) {
            this.currentCollageIndex = index;
            if (this.currentCollageIndex >= this.shapeLayoutList.size()) {
                this.currentCollageIndex = 0;
            }
            if (this.currentCollageIndex < 0) {
                this.currentCollageIndex = this.shapeLayoutList.size() - 1;
            }
            setCornerRadius(this.cornerRadius);
            setPathPadding(this.currentCollageIndex, this.paddingDistance);
        }

        private void setCornerRadius(float radius) {
            this.cornerRadius = radius;
            CornerPathEffect corEffect = new CornerPathEffect(radius);
            for (Shape radius2 : this.shapeLayoutList.get(this.currentCollageIndex).shapeArr) {
                radius2.setRadius(corEffect);
            }
            postInvalidate();
        }

        private void setPathPadding(int index, float distance) {
            this.paddingDistance = distance;
            for (int i = 0; i < this.shapeLayoutList.get(index).shapeArr.length; i++) {
                this.shapeLayoutList.get(index).shapeArr[i].scalePath((this.smallestDistanceList.get(index).floatValue() / 250.0f) * distance, (float) this.screenWidth, (float) this.screenWidth);
                if (!CollageActivity.this.isScrapBook) {
                    this.shapeLayoutList.get(index).shapeArr[i].checkScaleBounds();
                    this.shapeLayoutList.get(index).shapeArr[i].checkBoundries();
                }
            }
            postInvalidate();
        }

        private void setCollageSize(Matrix matrix, int progress) {
            matrix.reset();
            this.sizeScale = calculateSize((float) progress);
            matrix.postScale(this.sizeScale, this.sizeScale, (((float) (this.offsetX + this.offsetX)) + (((float) CollageActivity.this.width) * this.xscale)) / 2.0f, (((float) (this.offsetY + this.offsetY)) + (((float) CollageActivity.this.width) * this.yscale)) / 2.0f);
            invalidate();
        }

        float calculateSize(float progress) {
            return 1.0f - (progress / 200.0f);
        }

        int calculateSizeProgress(float size) {
            int progress = 200 - Math.round(200.0f * size);
            if (progress < 0) {
                progress = 0;
            }
            if (progress > 100) {
                return 100;
            }
            return progress;
        }

        void setPatternPaint(int resId) {
            if (this.patternPaint == null) {
                this.patternPaint = new Paint(1);
                this.patternPaint.setColor(-1);
            }
            if (resId == -1) {
                this.patternPaint.setShader(null);
                this.patternPaint.setColor(-1);
                postInvalidate();
                return;
            }
            this.patternBitmap = BitmapFactory.decodeResource(getResources(), resId);
            this.patternPaint.setShader(new BitmapShader(this.patternBitmap, TileMode.REPEAT, TileMode.REPEAT));
            postInvalidate();
        }

        void setPatternPaintColor(int color) {
            if (this.patternPaint == null) {
                this.patternPaint = new Paint(1);
            }
            this.patternPaint.setShader(null);
            this.patternPaint.setColor(color);
            postInvalidate();
        }

        public void setFrame(int index) {
            if (this.frameRect == null) {
                this.frameRect = new RectF(0.0f, 0.0f, (float) this.screenWidth, (float) this.screenWidth);
            }
            if (!(this.frameBitmap == null || this.frameBitmap.isRecycled())) {
                this.frameBitmap.recycle();
                this.frameBitmap = null;
            }
            if (index != 0) {
                this.frameBitmap = BitmapFactory.decodeResource(getResources(), LibUtility.borderRes[index]);
                postInvalidate();
            }
        }

        public void startAnimator() {
            if (CollageActivity.this.seekbarSize != null) {
                this.animSizeSeekbarProgress = CollageActivity.this.seekbarSize.getProgress();
            } else {
                this.animSizeSeekbarProgress = 0;
            }
            this.sizeMatrixSaved = new Matrix(this.sizeMatrix);
            this.animationCount = 0;
            this.animate = true;
            removeCallbacks(this.animator);
            postDelayed(this.animator, 150);
        }

        int animSize(int value) {
            int res;
            if (value < this.animHalfTime) {
                res = value;
            } else {
                res = this.animationLimit - value;
            }
            return this.animSizeSeekbarProgress + Math.round((float) (res * 2));
        }

        @SuppressLint("WrongConstant")
        public void onDraw(Canvas canvas) {
            int width = getWidth();
            int height = getHeight();
            canvas.save();
            this.drawingAreaRect.set((float) this.offsetX, (float) this.offsetY, ((float) this.offsetX) + (((float) width) * this.xscale), ((float) this.offsetY) + (((float) width) * this.yscale));
            canvas.drawPaint(this.paintGray);
            if (this.backgroundMode == 0) {
                canvas.drawRect(this.drawingAreaRect, this.patternPaint);
            }
            if (!(this.blurBitmap == null || this.blurBitmap.isRecycled() || this.backgroundMode != 1)) {
                this.blurRectDst.set(this.drawingAreaRect);
                canvas.drawBitmap(this.blurBitmap, this.blurRectSrc, this.blurRectDst, this.paint);
            }
            if (!CollageActivity.this.isScrapBook) {
                canvas.setMatrix(this.sizeMatrix);
            }
            int j = 0;
            if (!CollageActivity.this.isScrapBook || CollageActivity.this.showText) {
                j = canvas.saveLayer(0.0f, 0.0f, ((float) width) / this.sizeScale, ((float) height) / this.sizeScale, null, 31);
            }
            int i = 0;
            while (i < this.shapeLayoutList.get(this.currentCollageIndex).shapeArr.length) {
                boolean drawPorterClear = false;
                if (i == this.shapeLayoutList.get(this.currentCollageIndex).getClearIndex()) {
                    drawPorterClear = true;
                }
                if (CollageActivity.this.isScrapBook) {
                    this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[i].drawShapeForScrapBook(canvas, width, height, i == this.shapeIndex, this.orthogonal);
                } else {
                    this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[i].drawShape(canvas, width, height, j, drawPorterClear);
                }
                i++;
            }
            if (!CollageActivity.this.isScrapBook && this.shapeIndex >= 0 && this.shapeLayoutList.get(0).shapeArr.length > 1) {
                canvas.drawRect(this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[this.shapeIndex].bounds, this.borderPaint);
            }
            if (CollageActivity.this.showText) {
                canvas.restoreToCount(j);
                for (i = 0; i < CollageActivity.this.textDataList.size(); i++) {
                    this.textMatrix.set(CollageActivity.this.textDataList.get(i).imageSaveMatrix);
                    canvas.setMatrix(this.textMatrix);
                    canvas.drawText(CollageActivity.this.textDataList.get(i).message, CollageActivity.this.textDataList.get(i).xPos, CollageActivity.this.textDataList.get(i).yPos, CollageActivity.this.textDataList.get(i).textPaint);
                    canvas.setMatrix(this.identityMatrix);
                }
            }
            if (!(this.frameBitmap == null || this.frameBitmap.isRecycled())) {
                canvas.drawBitmap(this.frameBitmap, null, this.frameRect, this.paint);
            }
            if (CollageActivity.this.isScrapBook) {
                canvas.restore();
                this.above.set(0.0f, 0.0f, (float) canvas.getWidth(), this.drawingAreaRect.top);
                this.left.set(0.0f, this.drawingAreaRect.top, this.drawingAreaRect.left, this.drawingAreaRect.bottom);
                this.right.set(this.drawingAreaRect.right, this.drawingAreaRect.top, (float) canvas.getWidth(), this.drawingAreaRect.bottom);
                this.bottom.set(0.0f, this.drawingAreaRect.bottom, (float) canvas.getWidth(), (float) canvas.getHeight());
                canvas.drawRect(this.above, this.paintGray);
                canvas.drawRect(this.left, this.paintGray);
                canvas.drawRect(this.right, this.paintGray);
                canvas.drawRect(this.bottom, this.paintGray);
            }
        }

        public boolean onTouchEvent(MotionEvent ev) {
            this.mScaleDetector.onTouchEvent(ev);
            this.mTouchDetector.onTouchEvent(ev);
            if (CollageActivity.this.isScrapBook) {
                CollageActivity.this.mRotationDetector.onTouchEvent(ev);
            }
            int action = ev.getAction();
            float x;
            float y;
            int pointerIndex;
            switch (action & 255) {
                case 0:
                    this.previousIndex = this.shapeIndex;
                    x = ev.getX();
                    y = ev.getY();
                    this.mLastTouchX = x;
                    this.mLastTouchY = y;
                    this.orthogonal = false;
                    this.mActivePointerId = ev.getPointerId(0);
                    if (CollageActivity.this.isScrapBook && this.shapeIndex >= 0) {
                        this.zoomStart.set(x, y);
                        this.pts = this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[this.shapeIndex].getMappedCenter();
                        if (this.pts != null) {
                            this.startAngle = -Utility.pointToAngle(x, y, this.pts[0], this.pts[1]);
                        }
                        this.isInCircle = this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[this.shapeIndex].isInCircle(x, y);
                        this.isOnCross = this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[this.shapeIndex].isOnCross(x, y);
                        break;
                    }
                    selectCurrentShape(x, y, false);
                    break;
//                break;
                case 1:
                    this.orthogonal = false;
                    this.mActivePointerId = 1;
                    if (this.isOnCross) {
                        CollageActivity.this.createDeleteDialog();
                    }
                    this.isInCircle = false;
                    this.isOnCross = false;
                    invalidate();
                    break;
                case 2:
                    if (!this.isOnCross) {
                        pointerIndex = ev.findPointerIndex(this.mActivePointerId);
                        x = ev.getX(pointerIndex);
                        y = ev.getY(pointerIndex);
                        if (this.shapeIndex < 0) {
                            selectCurrentShape(x, y, false);
                        }
                        if (this.shapeIndex >= 0) {
                            if (!CollageActivity.this.isScrapBook || !this.isInCircle) {
                                this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[this.shapeIndex].bitmapMatrixTranslate(x - this.mLastTouchX, y - this.mLastTouchY);
                                this.mLastTouchX = x;
                                this.mLastTouchY = y;
                                invalidate();
                                break;
                            }
                            this.pts = this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[this.shapeIndex].getMappedCenter();
                            float currentAngle = -Utility.pointToAngle(x, y, this.pts[0], this.pts[1]);
                            Log.d(CollageActivity.TAG, "currentAngle " + currentAngle);
                            float rotation = getMatrixRotation(this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[this.shapeIndex].bitmapMatrix);
                            if ((rotation == 0.0f || rotation == 90.0f || rotation == 180.0f || rotation == -180.0f || rotation == -90.0f) && Math.abs(this.startAngle - currentAngle) < 4.0f) {
                                this.orthogonal = true;
                            } else {
                                if (Math.abs((rotation - this.startAngle) + currentAngle) < 4.0f) {
                                    currentAngle = this.startAngle - rotation;
                                    this.orthogonal = true;
                                    Log.d(CollageActivity.TAG, "aaaaa " + rotation);
                                } else if (Math.abs(90.0f - ((rotation - this.startAngle) + currentAngle)) < 4.0f) {
                                    currentAngle = (90.0f + this.startAngle) - rotation;
                                    this.orthogonal = true;
                                    Log.d(CollageActivity.TAG, "bbbbb " + rotation);
                                } else if (Math.abs(180.0f - ((rotation - this.startAngle) + currentAngle)) < 4.0f) {
                                    currentAngle = (180.0f + this.startAngle) - rotation;
                                    this.orthogonal = true;
                                    Log.d(CollageActivity.TAG, "cccc " + rotation);
                                } else if (Math.abs(-180.0f - ((rotation - this.startAngle) + currentAngle)) < 4.0f) {
                                    currentAngle = (-180.0f + this.startAngle) - rotation;
                                    this.orthogonal = true;
                                } else if (Math.abs(-90.0f - ((rotation - this.startAngle) + currentAngle)) < 4.0f) {
                                    currentAngle = (-90.0f + this.startAngle) - rotation;
                                    this.orthogonal = true;
                                    Log.d(CollageActivity.TAG, "dddd " + rotation);
                                } else {
                                    this.orthogonal = false;
                                }
                                this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[this.shapeIndex].bitmapMatrixRotate(this.startAngle - currentAngle);
                                this.startAngle = currentAngle;
                            }
                            float scaley = ((float) Math.sqrt((double) (((x - this.pts[0]) * (x - this.pts[0])) + ((y - this.pts[1]) * (y - this.pts[1]))))) / ((float) Math.sqrt((double) (((this.zoomStart.x - this.pts[0]) * (this.zoomStart.x - this.pts[0])) + ((this.zoomStart.y - this.pts[1]) * (this.zoomStart.y - this.pts[1])))));
                            float scale = this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[this.shapeIndex].getScale();
                            if (scale >= this.MIN_ZOOM || (scale < this.MIN_ZOOM && scaley > 1.0f)) {
                                this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[this.shapeIndex].bitmapMatrixScaleScrapBook(scaley, scaley);
                                this.zoomStart.set(x, y);
                            }
                            invalidate();
                            return true;
                        }
                    }
                    break;
                case 3:
                    this.mActivePointerId = 1;
                    this.isInCircle = false;
                    this.isOnCross = false;
                    break;
                case 6:
                    this.finalAngle = 0.0f;
                    pointerIndex = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & action) >> 8;
                    if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
                        int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        this.mLastTouchX = ev.getX(newPointerIndex);
                        this.mLastTouchY = ev.getY(newPointerIndex);
                        this.mActivePointerId = ev.getPointerId(newPointerIndex);
                        break;
                    }
                    break;
            }
            return true;
        }

        @SuppressLint({"WrongConstant"})
        private void selectCurrentShapeScrapBook(float x, float y, boolean isSingleTap) {
            int i;
            int length = this.shapeLayoutList.get(this.currentCollageIndex).shapeArr.length;
            boolean isSelected = false;
            for (i = length - 1; i >= 0; i--) {
                if (this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[i].isScrapBookSelected(x, y)) {
                    this.shapeIndex = i;
                    isSelected = true;
                    break;
                }
            }
            if (this.previousIndex == this.shapeIndex && isSingleTap) {
                unselectShapes();
            } else if (!isSelected) {
                unselectShapes();
            } else if (CollageActivity.this.selectImageForAdj) {
                openFilterFragment();
            } else if (this.shapeIndex >= 0 && this.shapeIndex < length) {
                Shape shapeTemp = this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[this.shapeIndex];
                Bitmap btmTemp = CollageActivity.this.bitmapList[this.shapeIndex];
                Parameter prmTemp = CollageActivity.this.parameterList[this.shapeIndex];
                for (i = 0; i < length; i++) {
                    if (i >= this.shapeIndex) {
                        if (i < length - 1) {
                            this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[i] = this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[i + 1];
                            CollageActivity.this.bitmapList[i] = CollageActivity.this.bitmapList[i + 1];
                            CollageActivity.this.parameterList[i] = CollageActivity.this.parameterList[i + 1];
                        } else {
                            this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[i] = shapeTemp;
                            CollageActivity.this.bitmapList[i] = btmTemp;
                            CollageActivity.this.parameterList[i] = prmTemp;
                        }
                    }
                }
                if (this.previousIndex == this.shapeIndex) {
                    this.previousIndex = length - 1;
                } else if (this.previousIndex > this.shapeIndex) {
                    this.previousIndex--;
                }
                this.shapeIndex = length - 1;
                if (this.shapeLayoutList.get(0).shapeArr.length > 0) {
                    CollageActivity.this.contextFooter.setVisibility(View.VISIBLE);
                    CollageActivity.this.setSelectedTab(5);
                }
            }
            if (this.shapeIndex >= 0) {
                this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[this.shapeIndex].bitmapMatrixgGetValues(this.matrixValues);
                this.mScaleFactor = this.matrixValues[0];
            }
            postInvalidate();
        }

        private void selectCurrentShape(float x, float y, boolean isSingleTap) {
            if (CollageActivity.this.isScrapBook) {
                selectCurrentShapeScrapBook(x, y, isSingleTap);
            } else {
                selectCurrentShapeCollage(x, y, isSingleTap);
            }
        }

        private void selectCurrentShapeCollage(float x, float y, boolean isSingleTap) {
            int swapIndex = this.shapeIndex;
            for (int i = 0; i < this.shapeLayoutList.get(this.currentCollageIndex).shapeArr.length; i++) {
                if (this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[i].region.contains((int) x, (int) y)) {
                    this.shapeIndex = i;
                }
            }
            if (CollageActivity.this.selectImageForAdj) {
                openFilterFragment();
            } else if (CollageActivity.this.swapMode) {
                Log.e(CollageActivity.TAG, "PRE SWAP");
                if (swapIndex != this.shapeIndex && swapIndex > -1 && this.shapeIndex > -1) {
                    Log.e(CollageActivity.TAG, "SWAP");
                    swapBitmaps(this.shapeIndex, swapIndex);
                    CollageActivity.this.swapMode = false;
                }
            } else if (this.previousIndex == this.shapeIndex && isSingleTap) {
                unselectShapes();
            } else if (this.shapeLayoutList.get(0).shapeArr.length > 0) {
                CollageActivity.this.contextFooter.setVisibility(VISIBLE);
                CollageActivity.this.setSelectedTab(5);
                Log.e(CollageActivity.TAG, "VISIBLE");
            }
            if (this.shapeIndex >= 0) {
                this.shapeLayoutList.get(this.currentCollageIndex).shapeArr[this.shapeIndex].bitmapMatrixgGetValues(this.matrixValues);
                this.mScaleFactor = this.matrixValues[0];
            }
            postInvalidate();
        }

        void unselectShapes() {
            CollageActivity.this.contextFooter.setVisibility(INVISIBLE);
            this.shapeIndex = -1;
            Log.e(CollageActivity.TAG, "unselectShapes");
            postInvalidate();
        }

        public void openFilterFragment() {
            selectFilterTextView.setVisibility(INVISIBLE);
            selectImageForAdj = false;
            if (this.shapeIndex >= 0) {
                fullEffectFragment.setBitmapWithParameter(CollageActivity.this.bitmapList[this.shapeIndex], CollageActivity.this.parameterList[this.shapeIndex]);
                setVisibilityOfFilterHorizontalListview(true);
            }
        }

        float getMatrixRotation(Matrix matrix) {
            matrix.getValues(this.values);
            return (float) Math.round(Math.atan2((double) this.values[1], (double) this.values[0]) * 57.29577951308232d);
        }

        public void setBlurBitmap(int radius, boolean cascade) {
            if (this.blurBuilderNormal == null) {
                this.blurBuilderNormal = new BlurBuilderNormal();
            }
            if (cascade) {
                this.backgroundMode = 2;
                if (!CollageActivity.this.isScrapBook) {
                    CollageActivity.this.seekbarSize.setProgress(CollageActivity.this.seekbarSize.getMax());
                }
            } else {
                this.backgroundMode = 1;
            }
            this.blurBitmap = NativeStackBlur.process(CollageActivity.this.bitmapList[0].copy(CollageActivity.this.bitmapList[0].getConfig(), true), radius);
            if (this.blurBitmap != null) {
                setBlurRect2((float) this.blurBitmap.getWidth(), (float) this.blurBitmap.getHeight());
            }
            postInvalidate();
        }

        void setBlurRect2(float btmwidth, float btmheight) {
            float w;
            float h;
            if ((CollageActivity.this.mulY * btmwidth) / CollageActivity.this.mulX < btmheight) {
                w = (float) ((int) btmwidth);
                h = (CollageActivity.this.mulY * btmwidth) / CollageActivity.this.mulX;
            } else {
                w = (((float) ((int) CollageActivity.this.mulX)) * btmheight) / CollageActivity.this.mulY;
                h = (float) ((int) btmheight);
            }
            int l = (int) ((btmwidth - w) / 2.0f);
            int t = (int) ((btmheight - h) / 2.0f);
            this.blurRectSrc.set(l, t, (int) (((float) l) + w), (int) (((float) t) + h));
        }
    }

    private final class MyMediaScannerConnectionClient implements MediaScannerConnectionClient {
        private MediaScannerConnection mConn;
        private String mFilename;
        private String mMimetype;

        MyMediaScannerConnectionClient(Context ctx, File file, String mimetype) {
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

        protected void onPreExecute() {
            this.progressDialog = new ProgressDialog(CollageActivity.this);
            this.progressDialog.setMessage("Saving image...");
            this.progressDialog.show();
        }

        protected Object doInBackground(Object... arg0) {
            this.resultPath = CollageActivity.this.collageView.saveBitmap(CollageActivity.this.width, CollageActivity.this.height);
            return null;
        }

        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (this.progressDialog != null && this.progressDialog.isShowing()) {
                this.progressDialog.cancel();
            }
            if (this.resultPath != null) {
                final Intent intent = new Intent(CollageActivity.this, ImageShareActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

                                CollageActivity.this.startActivity(intent);
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
                    }else{
                        CollageActivity.this.startActivity(intent);
                        finish();  //DT finish
                    }

                }else if(Glob.isAdmob){
                    if (mInterstitialAd != null && Glob.isOnline(CollageActivity.this)) {
                        showInterstitialAd();
                        mInterstitialAd.setAdListener(new AdListener(){
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                CollageActivity.this.startActivity(intent);
                                finish();  //DT finish
                                loadInterstitialAd();
                            }
                        });
                    }else{
                        CollageActivity.this.startActivity(intent);
                        finish();  //DT finish
                    }
                }
                else{
                    CollageActivity.this.startActivity(intent);
                    finish();  //DT finish
                }
            }
            MyMediaScannerConnectionClient myMediaScannerConnectionClient = new MyMediaScannerConnectionClient(CollageActivity.this.getApplicationContext(), new File(this.resultPath), null);
        }

    }

    @SuppressLint({"WrongConstant"})
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().addFlags(1024);
        Display display = getWindowManager().getDefaultDisplay();
        this.width = display.getWidth();
        this.height = display.getHeight();
        setContentView(R.layout.activity_collage);

        loadFbBanner();
        loadFbInterstitial();


        if (Glob.isOnline(CollageActivity.this)) {
            loadInterstitialAd();
        }

        if (Glob.isOnline(CollageActivity.this)) {

        }
        int arraySize = getCollageSize(getIntent().getExtras());
        this.seekBarRound = findViewById(R.id.seekbar_round);
        this.seekBarRound.setOnSeekBarChangeListener(this.mSeekBarListener);
        this.seekBarPadding = findViewById(R.id.seekbar_padding);
        this.seekBarPadding.setOnSeekBarChangeListener(this.mSeekBarListener);
        this.seekbarSize = findViewById(R.id.seekbar_size);
        this.seekbarSize.setOnSeekBarChangeListener(this.mSeekBarListener);
        this.seekbarBlur = findViewById(R.id.seekbar_collage_blur);
        this.seekbarBlur.setOnSeekBarChangeListener(this.mSeekBarListener);
        RecyclerView recyclerViewColor = findViewById(R.id.recyclerView_color);
        this.collageRecyclerView = findViewById(R.id.recyclerView_grid);
        int colorDefault = getResources().getColor(R.color.view_flipper_bg_color);
        int colorSelected = getResources().getColor(R.color.app_foreground);
        LinearLayoutManager/*LayoutManager*/ linearLayoutManager = new LinearLayoutManager(this);  //DT Change
        linearLayoutManager.setOrientation(0);
        this.collageRecyclerView.setLayoutManager(linearLayoutManager);
        this.collageAdapter = new MyAdapter(Collage.collageIconArray[arraySize - 1], new C05065(), colorDefault, colorSelected, false, true);
        this.collageRecyclerView.setAdapter(this.collageAdapter);
        this.collageRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.viewFlipper = findViewById(R.id.collage_view_flipper);
        this.viewFlipper.setDisplayedChild(5);
        createAdapterList(colorDefault, colorSelected);
        RecyclerView recyclerViewPattern = findViewById(R.id.recyclerView_pattern);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(0);
        this.colorContainer = findViewById(R.id.color_container);
        recyclerViewPattern.setLayoutManager(linearLayoutManager);
        final RecyclerView recyclerView = recyclerViewColor;
        recyclerViewPattern.setAdapter(new MyAdapter(Utility.patternResIdList3, new CurrentCollageIndexChangedListener() {
            @SuppressLint({"WrongConstant"})
            public void onIndexChanged(int position) {
                CollageActivity.this.collageView.backgroundMode = 0;
                if (position == 0) {
                    CollageActivity.this.collageView.setPatternPaint(-1);
                    return;
                }
                int newPos = position - 1;
                if (CollageActivity.this.patternAdapterList.get(newPos) != recyclerView.getAdapter()) {
                    recyclerView.setAdapter(CollageActivity.this.patternAdapterList.get(newPos));
                    CollageActivity.this.patternAdapterList.get(newPos).setSelectedPositinVoid();
                } else {
                    CollageActivity.this.patternAdapterList.get(newPos).setSelectedPositinVoid();
                    CollageActivity.this.patternAdapterList.get(newPos).notifyDataSetChanged();
                }
                CollageActivity.this.colorContainer.setVisibility(View.VISIBLE);
            }
        }, colorDefault, colorSelected, false, false));
        recyclerViewPattern.setItemAnimator(new DefaultItemAnimator());
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(0);
        recyclerViewColor.setLayoutManager(linearLayoutManager);
        recyclerViewColor.setAdapter(new ColorPickerAdapter(new C05087(), colorDefault, colorSelected));
        recyclerViewColor.setItemAnimator(new DefaultItemAnimator());
        HorizontalScrollView horizontalScrollView = findViewById(R.id.collage_footer);
        horizontalScrollView.bringToFront();
        HorizontalScrollView horizontalScrollView2 = horizontalScrollView;
        final HorizontalScrollView finalHorizontalScrollView = horizontalScrollView2;
        horizontalScrollView.postDelayed(new Runnable() {
            public void run() {
                finalHorizontalScrollView.scrollTo(finalHorizontalScrollView.getChildAt(0).getMeasuredWidth(), 0);
            }
        }, 50);
        horizontalScrollView2 = horizontalScrollView;
        final HorizontalScrollView finalHorizontalScrollView1 = horizontalScrollView2;
        horizontalScrollView.postDelayed(new Runnable() {
            public void run() {
                finalHorizontalScrollView1.fullScroll(17);
            }
        }, 600);


        Bundle extras = getIntent().getExtras();  //DT Change

        new BitmapWorkerTask().execute(extras, bundle);  //DT Change
    }

    void loadInterstitialAd() {
        this.mInterstitialAd = new InterstitialAd(this);
        this.mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder().build();

        this.mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                loadInterstitialAd();
            }
        });
    }

    void showInterstitialAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

    }

    private void createAdapterList(int colorDefault, int colorSelected) {
        this.patternAdapterList.clear();
        this.patternAdapterList.add(new ColorPickerAdapter(new CurrentCollageIndexChangedListener() {
            public void onIndexChanged(int color) {
                CollageActivity.this.collageView.setPatternPaintColor(color);
            }
        }, colorDefault, colorSelected));
        for (int[] myAdapter : Utility.patternResIdList2) {
            this.patternAdapterList.add(new MyAdapter(myAdapter, new CurrentCollageIndexChangedListener() {
                public void onIndexChanged(int positionOrColor) {
                    CollageActivity.this.collageView.setPatternPaint(positionOrColor);
                }
            }, colorDefault, colorSelected, true, true));
        }
    }

    int getCollageSize(Bundle extras) {
        long[] selectedImageList = extras.getLongArray("photo_id_list");
        if (selectedImageList == null) {
            return 1;
        }
        return selectedImageList.length;
    }

    public void addCanvasTextView() {
        this.canvasText = new CustomRelativeLayout(this, this.textDataList, this.collageView.identityMatrix, new SingleTap() {
            public void onSingleTap(TextData textData) {
                CollageActivity.this.fontFragment = new FontFragment();
                Bundle arguments = new Bundle();
                arguments.putSerializable("text_data", textData);
                CollageActivity.this.fontFragment.setArguments(arguments);
                CollageActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.collage_text_view_fragment_container, CollageActivity.this.fontFragment, "FONT_FRAGMENT").commit();
                Log.e(CollageActivity.TAG, "replace fragment");
                CollageActivity.this.fontFragment.setFontChoosedListener(CollageActivity.this.fontChoosedListener);
            }
        });
        this.canvasText.setApplyTextListener(new ApplyTextInterface() {
            public void onOk(ArrayList<TextData> tdList) {
                Iterator it = tdList.iterator();
                while (it.hasNext()) {
                    ((TextData) it.next()).setImageSaveMatrix(CollageActivity.this.collageView.identityMatrix);
                }
                CollageActivity.this.textDataList = tdList;
                CollageActivity.this.showText = true;
                if (CollageActivity.this.mainLayout == null) {
                    CollageActivity.this.mainLayout = CollageActivity.this.findViewById(R.id.collage_main_layout);
                }
                CollageActivity.this.mainLayout.removeView(CollageActivity.this.canvasText);
                CollageActivity.this.collageView.postInvalidate();
            }

            public void onCancel() {
                CollageActivity.this.showText = true;
                CollageActivity.this.mainLayout.removeView(CollageActivity.this.canvasText);
                CollageActivity.this.collageView.postInvalidate();
            }
        });
        this.showText = false;
        this.collageView.invalidate();
        this.mainLayout.addView(this.canvasText);
        findViewById(R.id.collage_text_view_fragment_container).bringToFront();
        this.fontFragment = new FontFragment();
        this.fontFragment.setArguments(new Bundle());
        getSupportFragmentManager().beginTransaction().add(R.id.collage_text_view_fragment_container, this.fontFragment, "FONT_FRAGMENT").commit();
        Log.e(TAG, "add fragment");
        this.fontFragment.setFontChoosedListener(this.fontChoosedListener);
    }

    private void setVisibilityForScrapbook() {
        findViewById(R.id.button_collage_layout).setVisibility(View.GONE);
        findViewById(R.id.button_collage_space).setVisibility(View.GONE);
        findViewById(R.id.button_collage_context_swap).setVisibility(View.GONE);
        findViewById(R.id.button_collage_context_fit).setVisibility(View.GONE);
        findViewById(R.id.button_collage_context_center).setVisibility(View.GONE);
        findViewById(R.id.button_collage_context_delete).setVisibility(View.VISIBLE);
    }

    void addEffectFragment() {
        if (this.fullEffectFragment == null) {
            this.fullEffectFragment = (FullEffectFragment) getSupportFragmentManager().findFragmentByTag("FULL_FRAGMENT");
            Log.e(TAG, "addEffectFragment");
            if (this.fullEffectFragment == null) {
                this.fullEffectFragment = new FullEffectFragment();
                Log.e(TAG, "EffectFragment == null");
                this.fullEffectFragment.setArguments(getIntent().getExtras());
                Log.e(TAG, "fullEffectFragment null");
                getSupportFragmentManager().beginTransaction().add(R.id.collage_effect_fragment_container, this.fullEffectFragment, "FULL_FRAGMENT").commitAllowingStateLoss();
            } else {
                Log.e(TAG, "not null null");
                if (this.collageView.shapeIndex >= 0) {
                    this.fullEffectFragment.setBitmapWithParameter(this.bitmapList[this.collageView.shapeIndex], this.parameterList[this.collageView.shapeIndex]);
                }
            }
            getSupportFragmentManager().beginTransaction().hide(this.fullEffectFragment).commitAllowingStateLoss();
            this.fullEffectFragment.setFullBitmapReadyListener(new FullBitmapReady() {
                public void onBitmapReady(Bitmap bitmap, Parameter parameter) {
                    CollageActivity.this.collageView.updateShapeListForFilterBitmap(bitmap);
                    CollageActivity.this.collageView.updateParamList(parameter);
                    CollageActivity.this.collageView.postInvalidate();
                    CollageActivity.this.getSupportFragmentManager().beginTransaction().hide(CollageActivity.this.fullEffectFragment).commit();
                    CollageActivity.this.collageView.postInvalidate();
                }

                public void onCancel() {
                    CollageActivity.this.setVisibilityOfFilterHorizontalListview(false);
                    CollageActivity.this.collageView.postInvalidate();
                }
            });
            findViewById(R.id.collage_effect_fragment_container).bringToFront();
        }
    }

    protected void onDestroy() {
        int i;
        super.onDestroy();
        if (this.bitmapList != null) {
            for (i = 0; i < this.bitmapList.length; i++) {
                if (this.bitmapList[i] != null) {
                    this.bitmapList[i].recycle();
                }
            }
        }
        if (this.collageView != null) {
            if (this.collageView.shapeLayoutList != null) {
                for (i = 0; i < this.collageView.shapeLayoutList.size(); i++) {
                    for (int j = 0; j < this.collageView.shapeLayoutList.get(i).shapeArr.length; j++) {
                        if (this.collageView.shapeLayoutList.get(i).shapeArr[j] != null) {
                            this.collageView.shapeLayoutList.get(i).shapeArr[j].freeBitmaps();
                        }
                    }
                }
            }
            if (this.collageView.maskBitmapArray != null) {
                for (i = 0; i < this.collageView.maskBitmapArray.length; i++) {
                    if (this.collageView.maskBitmapArray[i] != null) {
                        if (!this.collageView.maskBitmapArray[i].isRecycled()) {
                            this.collageView.maskBitmapArray[i].recycle();
                        }
                        this.collageView.maskBitmapArray[i] = null;
                    }
                }
            }
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("show_text", this.showText);
        outState.putSerializable("text_data", this.textDataList);
        if (this.fontFragment != null && this.fontFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().remove(this.fontFragment).commit();
        }
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.showText = savedInstanceState.getBoolean("show_text");
        this.textDataList = (ArrayList) savedInstanceState.getSerializable("text_data");
        if (this.textDataList == null) {
            this.textDataList = new ArrayList();
        }
        if (this.contextFooter == null) {
            this.contextFooter = findViewById(R.id.collage_context_menu);
        }
        if (this.contextFooter != null) {
            this.contextFooter.bringToFront();
        }
    }

    public void onBackPressed() {
        if (this.fontFragment != null && this.fontFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().remove(this.fontFragment).commit();
        } else if (!this.showText && this.canvasText != null) {
            this.showText = true;
            this.mainLayout.removeView(this.canvasText);
            this.collageView.postInvalidate();
            this.canvasText = null;
            Log.e(TAG, "replace fragment");
        } else if (this.fullEffectFragment != null && this.fullEffectFragment.isVisible()) {
        } else {
            if (this.colorContainer.getVisibility() == View.VISIBLE) {
                hideColorContainer();
            } else if (this.swapMode) {
                this.selectSwapTextView.setVisibility(View.INVISIBLE);
                this.swapMode = false;
            } else if (this.collageView != null && this.collageView.shapeIndex >= 0) {
                this.collageView.unselectShapes();
            } else if (this.selectImageForAdj) {
                this.selectFilterTextView.setVisibility(View.INVISIBLE);
                this.selectImageForAdj = false;
            } else if (this.viewFlipper == null || this.viewFlipper.getDisplayedChild() == 5) {
                backButtonAlertBuilder();
            } else {
                setSelectedTab(5);
            }
        }
    }

    private void backButtonAlertBuilder() {
        try {
            /*View view = View.inflate(this, R.layout.dialog_exit_layout, null);

            TextView dialog_heading = view.findViewById(R.id.dialog_heading);
            dialog_heading.setText("Want to Discard Editing?");

            TextView exit_app = view.findViewById(R.id.exit_app);
            exit_app.setText("Yes");

            TextView cancel_app = view.findViewById(R.id.cancel_app);
            cancel_app.setText("No");


            view.findViewById(R.id.exit_app).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CollageActivity.this.finish();
                    alertDialog.dismiss();
//                    new SaveImageTask().execute(new Object[0]);
                }
            });
            view.findViewById(R.id.cancel_app).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    CollageActivity.this.finish();
                    alertDialog.dismiss();
                }
            });*/
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            //showHomeScreenFacebookAds(view);
            //alertDialogBuilder.setView(view);
            alertDialogBuilder.setMessage("Want to Discard Editing?")
                    .setPositiveButton("No", null)
                    .setNegativeButton("Yes", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                            CollageActivity.this.finish();
                        }
                    });
            this.alertDialog = alertDialogBuilder.create();
            this.alertDialog.setCancelable(false);
            this.alertDialog.show();
        } catch (Exception e) {
        }
    }

    public void myClickHandler(View view) {
        int id = view.getId();
        if (id == R.id.button_collage_layout) {
            setSelectedTab(0);
        } else if (id == R.id.button_collage_ratio) {
            setSelectedTab(3);
        } else if (id == R.id.button_collage_blur) {
            this.collageView.setBlurBitmap(this.collageView.blurRadius, false);
            setSelectedTab(4);
            this.collageView.startAnimator();
        } else if (id == R.id.button_collage_background) {
            setSelectedTab(1);
        } else if (id == R.id.button_collage_space) {
            setSelectedTab(2);
        } else if (id == R.id.button_collage_adj) {
            if (this.collageView.shapeLayoutList.get(0).shapeArr.length == 1) {
                this.collageView.shapeIndex = 0;
                this.collageView.openFilterFragment();
            } else if (this.collageView.shapeIndex >= 0) {
                this.collageView.openFilterFragment();
                Log.e(TAG, "collageView.shapeIndex>=0 openFilterFragment");
            } else {
                setSelectedTab(5);
                this.selectFilterTextView.setVisibility(View.VISIBLE);
                this.selectImageForAdj = true;
            }
        } else if (id == R.id.button_collage_context_swap) {
            if (this.collageView.shapeLayoutList.get(this.collageView.currentCollageIndex).shapeArr.length == 2) {
                this.collageView.swapBitmaps(0, 1);
            } else {
                this.selectSwapTextView.setVisibility(View.VISIBLE);
                this.swapMode = true;
            }
        } else if (id == R.id.button_collage_context_delete) {
            createDeleteDialog();
        } else if (id == R.id.button_collage_context_filter) {
            this.collageView.openFilterFragment();
        } else if (id == R.id.button_save_collage_image) {
            setSelectedTab(5);
            Log.e("CollegeActivity", "save button");
            new SaveImageTask().execute();
        } else if (id == R.id.button_cancel_collage_image) {
            backButtonAlertBuilder();
        } else if (id == R.id.button11) {
            this.mulX = 1.0f;
            this.mulY = 1.0f;
            this.collageView.updateShapeListForRatio(this.width, this.height);
            setRatioButtonBg(0);
        } else if (id == R.id.button21) {
            this.mulX = 2.0f;
            this.mulY = 1.0f;
            this.collageView.updateShapeListForRatio(this.width, this.height);
            setRatioButtonBg(1);
        } else if (id == R.id.button12) {
            this.mulX = 1.0f;
            this.mulY = 2.0f;
            this.collageView.updateShapeListForRatio(this.width, this.height);
            setRatioButtonBg(2);
        } else if (id == R.id.button32) {
            this.mulX = 3.0f;
            this.mulY = 2.0f;
            this.collageView.updateShapeListForRatio(this.width, this.height);
            setRatioButtonBg(3);
        } else if (id == R.id.button23) {
            this.mulX = 2.0f;
            this.mulY = 3.0f;
            this.collageView.updateShapeListForRatio(this.width, this.height);
            setRatioButtonBg(4);
        } else if (id == R.id.button43) {
            this.mulX = 4.0f;
            this.mulY = 3.0f;
            this.collageView.updateShapeListForRatio(this.width, this.height);
            setRatioButtonBg(5);
        } else if (id == R.id.button34) {
            this.mulX = 3.0f;
            this.mulY = 4.0f;
            this.collageView.updateShapeListForRatio(this.width, this.height);
            setRatioButtonBg(6);
        } else if (id == R.id.button45) {
            this.mulX = 4.0f;
            this.mulY = 5.0f;
            this.collageView.updateShapeListForRatio(this.width, this.height);
            setRatioButtonBg(7);
        } else if (id == R.id.button57) {
            this.mulX = 5.0f;
            this.mulY = 7.0f;
            this.collageView.updateShapeListForRatio(this.width, this.height);
            setRatioButtonBg(8);
        } else if (id == R.id.button169) {
            this.mulX = 16.0f;
            this.mulY = 9.0f;
            this.collageView.updateShapeListForRatio(this.width, this.height);
            setRatioButtonBg(9);
        } else if (id == R.id.button916) {
            this.mulX = 9.0f;
            this.mulY = 16.0f;
            this.collageView.updateShapeListForRatio(this.width, this.height);
            setRatioButtonBg(10);
        } else if (id == R.id.hide_select_image_warning) {
            this.selectSwapTextView.setVisibility(View.INVISIBLE);
            this.swapMode = false;
        } else if (id == R.id.hide_select_image_warning_filter) {
            this.selectFilterTextView.setVisibility(View.INVISIBLE);
            this.selectImageForAdj = false;
        } else if (id == R.id.hide_color_container) {
            hideColorContainer();
        } else if (id == R.id.button_mirror_text) {
            addCanvasTextView();
            clearViewFlipper();
        } else if (id == R.id.button_mirror_sticker) {
        }
        if (id == R.id.button_collage_context_fit) {
            this.collageView.setShapeScaleMatrix(0);
        } else if (id == R.id.button_collage_context_center) {
            this.collageView.setShapeScaleMatrix(1);
        } else if (id == R.id.button_collage_context_rotate_left) {
            this.collageView.setShapeScaleMatrix(3);
        } else if (id == R.id.button_collage_context_rotate_right) {
            this.collageView.setShapeScaleMatrix(2);
        } else if (id == R.id.button_collage_context_flip_horizontal) {
            this.collageView.setShapeScaleMatrix(4);
        } else if (id == R.id.button_collage_context_flip_vertical) {
            this.collageView.setShapeScaleMatrix(5);
        } else if (id == R.id.button_collage_context_rotate_negative) {
            this.collageView.setShapeScaleMatrix(6);
        } else if (id == R.id.button_collage_context_rotate_positive) {
            this.collageView.setShapeScaleMatrix(7);
        } else if (id == R.id.button_collage_context_zoom_in) {
            toastMatrixMessage(this.collageView.setShapeScaleMatrix(8));
        } else if (id == R.id.button_collage_context_zoom_out) {
            toastMatrixMessage(this.collageView.setShapeScaleMatrix(9));
        } else if (id == R.id.button_collage_context_move_left) {
            toastMatrixMessage(this.collageView.setShapeScaleMatrix(10));
        } else if (id == R.id.button_collage_context_move_right) {
            toastMatrixMessage(this.collageView.setShapeScaleMatrix(11));
        } else if (id == R.id.button_collage_context_move_up) {
            toastMatrixMessage(this.collageView.setShapeScaleMatrix(12));
        } else if (id == R.id.button_collage_context_move_down) {
            toastMatrixMessage(this.collageView.setShapeScaleMatrix(13));
        } else if (this.fullEffectFragment != null && this.fullEffectFragment.isVisible()) {
            this.fullEffectFragment.myClickHandler(view);
        }
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
        for (int i = 0; i < this.RATIO_BUTTON_SIZE; i++) {
            Log.i("mytag", "index: " + i);
            this.ratioButtonArray[i].setBackgroundResource(R.drawable.selector_collage_ratio_button);
        }
        this.ratioButtonArray[index].setBackgroundResource(R.drawable.collage_ratio_bg_pressed);
    }

    void toastMatrixMessage(int message) {
        String str = null;
        if (message == 1) {
            str = "You reached maximum zoom!";
        } else if (message == 2) {
            str = "You reached minimum zoom!";
        } else if (message == 6) {
            str = "You reached max bottom!";
        } else if (message == 5) {
            str = "You reached max top!";
        } else if (message == 4) {
            str = "You reached max right!";
        } else if (message == 3) {
            str = "You reached max left!";
        }
        if (str != null) {
            Toast msg = Toast.makeText(this, str, Toast.LENGTH_SHORT);
            msg.setGravity(17, msg.getXOffset() / 2, msg.getYOffset() / 2);
            msg.show();
        }
    }

    void clearViewFlipper() {
        this.viewFlipper.setDisplayedChild(5);
        setTabBg(-1);
    }

    private void setVisibilityForSingleImage() {
        findViewById(R.id.seekbar_corner_container).setVisibility(View.GONE);
        findViewById(R.id.seekbar_space_container).setVisibility(View.GONE);
        findViewById(R.id.button_collage_blur).setVisibility(View.VISIBLE);
        findViewById(R.id.button_collage_context_delete).setVisibility(View.GONE);
        findViewById(R.id.button_collage_context_swap).setVisibility(View.GONE);
        if (!this.isScrapBook) {
            this.collageView.setCollageSize(this.collageView.sizeMatrix, 45);
            if (this.seekbarSize != null) {
                this.seekbarSize.setProgress(45);
            }
        }
        this.collageView.setBlurBitmap(this.collageView.blurRadius, false);
        if (!this.isScrapBook) {
            setSelectedTab(2);
        }
    }

    void setSelectedTab(int index) {
        if (this.viewFlipper != null) {
            setTabBg(0);
            int displayedChild = this.viewFlipper.getDisplayedChild();
            if (displayedChild != 1) {
                hideColorContainer();
            }
            if (index == 0) {
                if (displayedChild != 0) {
                    this.viewFlipper.setInAnimation(this.slideLeftIn);
                    this.viewFlipper.setOutAnimation(this.slideRightOut);
                    this.viewFlipper.setDisplayedChild(0);
                } else {
                    return;
                }
            }
            if (index == 1) {
                setTabBg(1);
                if (displayedChild != 1) {
                    if (displayedChild == 0) {
                        this.viewFlipper.setInAnimation(this.slideRightIn);
                        this.viewFlipper.setOutAnimation(this.slideLeftOut);
                    } else {
                        this.viewFlipper.setInAnimation(this.slideLeftIn);
                        this.viewFlipper.setOutAnimation(this.slideRightOut);
                    }
                    this.viewFlipper.setDisplayedChild(1);
                } else {
                    return;
                }
            }
            if (index == 4) {
                setTabBg(4);
                if (displayedChild != 4) {
                    if (displayedChild == 0) {
                        this.viewFlipper.setInAnimation(this.slideRightIn);
                        this.viewFlipper.setOutAnimation(this.slideLeftOut);
                    } else {
                        this.viewFlipper.setInAnimation(this.slideLeftIn);
                        this.viewFlipper.setOutAnimation(this.slideRightOut);
                    }
                    this.viewFlipper.setDisplayedChild(4);
                } else {
                    return;
                }
            }
            if (index == 2) {
                setTabBg(2);
                if (displayedChild != 2) {
                    if (displayedChild == 0 || displayedChild == 1) {
                        this.viewFlipper.setInAnimation(this.slideRightIn);
                        this.viewFlipper.setOutAnimation(this.slideLeftOut);
                    } else {
                        this.viewFlipper.setInAnimation(this.slideLeftIn);
                        this.viewFlipper.setOutAnimation(this.slideRightOut);
                    }
                    this.viewFlipper.setDisplayedChild(2);
                } else {
                    return;
                }
            }
            if (index == 3) {
                setTabBg(3);
                if (displayedChild != 3) {
                    if (displayedChild == 5) {
                        this.viewFlipper.setInAnimation(this.slideLeftIn);
                        this.viewFlipper.setOutAnimation(this.slideRightOut);
                    } else {
                        this.viewFlipper.setInAnimation(this.slideRightIn);
                        this.viewFlipper.setOutAnimation(this.slideLeftOut);
                    }
                    this.viewFlipper.setDisplayedChild(3);
                } else {
                    return;
                }
            }
            if (index == 5) {
                setTabBg(-1);
                if (displayedChild != 5) {
                    this.viewFlipper.setInAnimation(this.slideRightIn);
                    this.viewFlipper.setOutAnimation(this.slideLeftOut);
                    this.viewFlipper.setDisplayedChild(5);
                }
            }
        }
    }

    private void setTabBg(int index) {
        if (this.tabButtonList == null) {
            this.tabButtonList = new View[6];
            this.tabButtonList[0] = findViewById(R.id.button_collage_layout);
            this.tabButtonList[2] = findViewById(R.id.button_collage_space);
            this.tabButtonList[4] = findViewById(R.id.button_collage_blur);
            this.tabButtonList[1] = findViewById(R.id.button_collage_background);
            this.tabButtonList[3] = findViewById(R.id.button_collage_ratio);
            this.tabButtonList[5] = findViewById(R.id.button_collage_adj);
        }
        for (View backgroundResource : this.tabButtonList) {
            backgroundResource.setBackgroundResource(R.drawable.collage_footer_button);
        }
        if (index >= 0) {
            this.tabButtonList[index].setBackgroundResource(R.color.footer_button_color_pressed);
        }
    }

    void setVisibilityOfFilterHorizontalListview(boolean show) {
        if (show && this.fullEffectFragment.isHidden()) {
            getSupportFragmentManager().beginTransaction().show(this.fullEffectFragment).commit();
        }
        if (!show && this.fullEffectFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(this.fullEffectFragment).commit();
        }
        findViewById(R.id.collage_effect_fragment_container).bringToFront();
    }

    private void hideColorContainer() {
        if (this.colorContainer == null) {
            this.colorContainer = findViewById(R.id.color_container);
        }
        this.colorContainer.setVisibility(View.INVISIBLE);
    }

    void createDeleteDialog() {
        if (this.collageView.shapeLayoutList.get(0).shapeArr.length == 1) {
            Toast msg = Toast.makeText(this, "You can't delete last image!", Toast.LENGTH_SHORT);
            msg.setGravity(17, msg.getXOffset() / 2, msg.getYOffset() / 2);
            msg.show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete it?").setCancelable(true).setPositiveButton("Yes", new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                CollageActivity.this.collageView.deleteBitmap(CollageActivity.this.collageView.shapeIndex, CollageActivity.this.width, CollageActivity.this.height);
            }
        }).setNegativeButton("No", new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        this.saveImageAlert = builder.create();
        this.saveImageAlert.show();
    }

    private void loadFbBanner() {
        if(com.helloworld.photoeditor.Glob.isFB) {
            adView = new com.facebook.ads.AdView(this, getResources().getString(R.string.fb_banner), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
            LinearLayout adContainer = findViewById(R.id.fb_banner_container);
            adContainer.addView(adView);
            adView.loadAd();
        }else if(Glob.isAdmob){
            com.google.android.gms.ads.AdView adview = new com.google.android.gms.ads.AdView(this);
            adview.setAdSize(AdSize.BANNER);
            adview.setAdUnitId(getString(R.string.banner_ad_id));
            ((RelativeLayout) findViewById(R.id.adView)).addView(adview);

            adview.loadAd(new AdRequest.Builder().build());
        }

    }
    public void loadFbInterstitial(){
        interstitialAd = new com.facebook.ads.InterstitialAd(this, getResources().getString(R.string.fb_interstitial));
        interstitialAd.loadAd();
    }

   }