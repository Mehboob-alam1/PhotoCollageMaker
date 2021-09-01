package com.helloworld.photoeditor.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.helloworld.photoeditor.R;
import com.helloworld.photoeditor.adapters.MyRecyclerViewAdapter;
import com.helloworld.photoeditor.adapters.MyRecyclerViewAdapter.RecyclerAdapterIndexChangedListener;
import com.helloworld.photoeditor.adapters.MyRecyclerViewAdapter.SelectedIndexChangedListener;
import com.helloworld.photoeditor.common_lib.MyAsyncTask;
import com.helloworld.photoeditor.common_lib.MyAsyncTask.Status;
import com.helloworld.photoeditor.common_lib.Parameter;
import com.helloworld.photoeditor.common_lib.SeekBarHint;
import com.helloworld.photoeditor.fragments.FullEffectFragment.HideHeaderListener;
import com.helloworld.photoeditor.utils.LibUtility;
import com.helloworld.photoeditor.utils.LibUtility.ExcludeTabListener;
import com.helloworld.photoeditor.utils.LibUtility.FooterVisibilityListener;
import com.commit451.nativestackblur.NativeStackBlur;


public class EffectFragment extends Fragment {
    public static final int INDEX_ADJUSTMENT = 4;
    public static final int INDEX_BLUR = 10;
    public static final int INDEX_BRIGHTNESS = 4;
    public static final int INDEX_CONTRAST = 6;
    public static final int INDEX_FRAME = 1;
    public static final int INDEX_FX = 0;
    public static final int INDEX_LIGHT = 2;
    public static final int INDEX_SATURATION = 7;
    public static final int INDEX_SHARPEN = 9;
    public static final int INDEX_TEXTURE = 3;
    public static final int INDEX_TINT = 8;
    public static final int INDEX_VOID = 5;
    public static final int INDEX_WARMTH = 5;
    public static final int TAB_SIZE = 14;
    static final String TAG = "EffectFragment";
    private static String[] filterBitmapTitle = new String[]{"None", "Gray", "Sepia", "Joey", "Sancia", "Blair", "Sura", "Tara", "Summer", "Penny", "Cuddy", "Cameron", "Lemon", "Tanya", "Lorelai", "Quinn", "Izabella", "Amber", "Cersei", "Debra", "Ellen", "Gabrielle", "Arya"};
    public Paint abc1Paint;
    public Paint abc2Paint;
    public Paint abc3Paint;
    public Paint abc4Paint;
    public Paint abcPaint;
    Activity activity;
    Button adjustmentLabel;
    float[] autoParameters;
    int bitmapHeight;
    BitmapReady bitmapReadyListener;
    Bitmap bitmapTiltBlur;
    int bitmapWidth;
    MyRecyclerViewAdapter borderAdapter;
    RecyclerAdapterIndexChangedListener borderIndexChangedListener = null;
    public Paint bwPaint;
    public Paint coldlifePaint;
    Context context;
    public Paint cyanPaint;
    public Paint darkenPaint;
    ExcludeTabListener excludeTabListener;
    MyRecyclerViewAdapter filterAdapter;
    Bitmap filterBitmap;
    FooterVisibilityListener footerListener;
    FilterAndAdjustmentTask ft;
    public Paint grayPaint;
    HideHeaderListener hideHeaderListener;
    boolean inFilterAndAdjustment = false;
    public Paint invertPaint;
    public Paint lightenPaint;
    public Paint limePaint;
    public Paint milkPaint;
    OnSeekBarChangeListener mySeekBarListener = new C05181();
    public Paint oldtimesPaint;
    MyRecyclerViewAdapter overlayAdapter;
    Parameter parameterBackUp = new Parameter();
    public Parameter parameterGlobal;
    int parameterSize = 4;
    public Paint peachyPaint;
    public Paint polaroidPaint;
    public Paint purplePaint;
    public Paint scrimPaint;
    SeekBar seekBarAdjustment;
    Rect seekbarHintTextBounds = new Rect();
    LayoutParams seekbarHintTextLayoutParams;
    int selectedTab = 0;
    public Paint sepiaPaint;
    public Paint sepiumPaint;
    private Animation slideLeftIn;
    private Animation slideLeftOut;
    private Animation slideRightIn;
    private Animation slideRightOut;
    private Bitmap sourceBitmap;
    Button[] tabButtonList;
    TextView textHint;
    MyRecyclerViewAdapter textureAdapter;
    int thumbSize;
    ViewFlipper viewFlipper;
    private ViewSwitcher viewSwitcher;
    public Paint yellowPaint;

    public interface BitmapReady {
        void onBitmapReady(Bitmap bitmap);
    }

    class C05181 implements OnSeekBarChangeListener {
        C05181() {
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            if (EffectFragment.this.textHint == null) {
                EffectFragment.this.textHint = EffectFragment.this.getView().findViewById(R.id.seekbar_hint);
            }
            if (EffectFragment.this.seekbarHintTextLayoutParams == null) {
                EffectFragment.this.seekbarHintTextLayoutParams = (LayoutParams) EffectFragment.this.textHint.getLayoutParams();
            }
            Rect thumbRect = ((SeekBarHint) seekBar).getSeekBarThumb().getBounds();
            EffectFragment.this.textHint.setText(String.valueOf(progress));
            EffectFragment.this.textHint.getPaint().getTextBounds(EffectFragment.this.textHint.getText().toString(), 0, EffectFragment.this.textHint.getText().length(), EffectFragment.this.seekbarHintTextBounds);
            EffectFragment.this.seekbarHintTextLayoutParams.setMargins(thumbRect.centerX() - (EffectFragment.this.seekbarHintTextBounds.width() / 2), 0, 0, 0);
            EffectFragment.this.textHint.setLayoutParams(EffectFragment.this.seekbarHintTextLayoutParams);
            if (EffectFragment.this.parameterGlobal.seekBarMode == 0) {
                EffectFragment.this.parameterGlobal.setBrightness(progress);
            } else if (EffectFragment.this.parameterGlobal.seekBarMode == 1) {
                EffectFragment.this.parameterGlobal.setContrast(progress);
            } else if (EffectFragment.this.parameterGlobal.seekBarMode == 2) {
                EffectFragment.this.parameterGlobal.setTemperature(progress);
            } else if (EffectFragment.this.parameterGlobal.seekBarMode == 3) {
                EffectFragment.this.parameterGlobal.setSaturation(progress);
            } else if (EffectFragment.this.parameterGlobal.seekBarMode == 4) {
                EffectFragment.this.parameterGlobal.setTint(progress);
            } else if (EffectFragment.this.parameterGlobal.seekBarMode == 5) {
                EffectFragment.this.parameterGlobal.setSharpen(progress);
            } else if (EffectFragment.this.parameterGlobal.seekBarMode == 6) {
                EffectFragment.this.parameterGlobal.setBlur(progress);
            } else if (EffectFragment.this.parameterGlobal.seekBarMode == 7) {
                EffectFragment.this.parameterGlobal.setHighlight(progress);
            } else if (EffectFragment.this.parameterGlobal.seekBarMode == 8) {
                EffectFragment.this.parameterGlobal.setShadow(progress);
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            if (EffectFragment.this.textHint == null) {
                EffectFragment.this.textHint = EffectFragment.this.getView().findViewById(R.id.seekbar_hint);
            }
            EffectFragment.this.textHint.setVisibility(0);
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (EffectFragment.this.textHint == null) {
                EffectFragment.this.textHint = EffectFragment.this.getView().findViewById(R.id.seekbar_hint);
            }
            EffectFragment.this.textHint.setVisibility(4);
            EffectFragment.this.callBack();
        }
    }

    class C05192 implements RecyclerAdapterIndexChangedListener {
        C05192() {
        }

        public void onIndexChanged(int i) {
            EffectFragment.this.applyChangesOnBitmap();
        }
    }

    class C05203 implements SelectedIndexChangedListener {
        C05203() {
        }

        public void selectedIndexChanged(int i) {
            Log.e(EffectFragment.TAG, "selectedIndexChanged " + i);
            EffectFragment.this.parameterGlobal.selectedBorderIndex = i;
        }
    }

    class C05214 implements RecyclerAdapterIndexChangedListener {
        C05214() {
        }

        public void onIndexChanged(int i) {
            EffectFragment.this.applyChangesOnBitmap();
        }
    }

    class C05225 implements SelectedIndexChangedListener {
        C05225() {
        }

        public void selectedIndexChanged(int i) {
            EffectFragment.this.parameterGlobal.selectedTextureIndex = i;
        }
    }

    class C05236 implements RecyclerAdapterIndexChangedListener {
        C05236() {
        }

        public void onIndexChanged(int i) {
            EffectFragment.this.applyChangesOnBitmap();
        }
    }

    class C05247 implements SelectedIndexChangedListener {
        C05247() {
        }

        public void selectedIndexChanged(int i) {
            EffectFragment.this.parameterGlobal.selectedOverlayIndex = i;
        }
    }

    class C05258 implements RecyclerAdapterIndexChangedListener {
        C05258() {
        }

        public void onIndexChanged(int i) {
            EffectFragment.this.applyChangesOnBitmap();
        }
    }

    class C05269 implements SelectedIndexChangedListener {
        C05269() {
        }

        public void selectedIndexChanged(int i) {
            EffectFragment.this.parameterGlobal.selectedFilterIndex = i;
        }
    }

    class FilterAndAdjustmentTask extends MyAsyncTask<Void, Void, Void> {
        int lastBlurRadius = -1;
        Matrix matrixBlur = new Matrix();
        Paint paintBlur = new Paint(2);
        ProgressDialog progressDialog;
        Bitmap smallBitmap;

        FilterAndAdjustmentTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            EffectFragment.this.inFilterAndAdjustment = true;
            try {
                this.progressDialog = new ProgressDialog(EffectFragment.this.context);
                this.progressDialog.setMessage("Please Wait...");
                this.progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected Void doInBackground(Void... arg0) {
            if (EffectFragment.this.isAdded()) {
                if (EffectFragment.this.filterBitmap == null) {
                    EffectFragment.this.filterBitmap = EffectFragment.this.sourceBitmap.copy(Config.ARGB_8888, true);
                } else {
                    new Canvas(EffectFragment.this.filterBitmap).drawBitmap(EffectFragment.this.sourceBitmap, 0.0f, 0.0f, new Paint());
                }
                new Canvas(EffectFragment.this.filterBitmap).drawBitmap(EffectFragment.this.sourceBitmap, 0.0f, 0.0f, new Paint());
                if (EffectFragment.this.parameterGlobal.blur > 0 && VERSION.SDK_INT > 17) {
                    Bitmap copyBitmap = EffectFragment.this.sourceBitmap.copy(EffectFragment.this.sourceBitmap.getConfig(), true);
                    EffectFragment.this.filterBitmap = NativeStackBlur.process(copyBitmap, EffectFragment.this.parameterGlobal.blur);
                }
                if (EffectFragment.this.isAdded()) {
                    pipeline(EffectFragment.this.filterBitmap);
                } else {
                    cancel(true);
                    EffectFragment.this.inFilterAndAdjustment = false;
                }
            } else {
                EffectFragment.this.inFilterAndAdjustment = false;
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            EffectFragment.this.inFilterAndAdjustment = false;
            try {
                this.progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (EffectFragment.this.isAdded()) {
                EffectFragment.this.bitmapReadyListener.onBitmapReady(EffectFragment.this.filterBitmap);
            }
        }

        void pipeline(Bitmap btmPipe) {
            if (EffectFragment.this.parameterGlobal.selectedFilterIndex <= 22) {
                EffectFragment.this.setFilter(EffectFragment.this.parameterGlobal.selectedFilterIndex, btmPipe);
            }
            Bitmap overlayBitmap = EffectFragment.this.getOverlayBitmap(EffectFragment.this.parameterGlobal.selectedOverlayIndex);
            if (!(overlayBitmap == null || overlayBitmap.isRecycled())) {
                if (VERSION.SDK_INT > 10) {
                    EffectFragment.this.applyOverlay11(overlayBitmap, btmPipe, EffectFragment.isOverlayScreenMode(EffectFragment.this.parameterGlobal.selectedOverlayIndex));
                } else if (EffectFragment.isOverlayScreenMode(EffectFragment.this.overlayAdapter.getSelectedIndex()) != 0) {
                    EffectFragment.this.applyOverlay11(overlayBitmap, btmPipe, EffectFragment.isOverlayScreenMode(EffectFragment.this.parameterGlobal.selectedOverlayIndex));
                }
            }
            EffectFragment.this.filterMultiply(btmPipe, EffectFragment.this.parameterGlobal.selectedTextureIndex, false);
            if (EffectFragment.this.borderIndexChangedListener == null) {
                EffectFragment.this.setBorder(btmPipe, EffectFragment.this.parameterGlobal.selectedBorderIndex, false);
            }
            Canvas cvs = new Canvas(btmPipe);
            if (EffectFragment.this.parameterGlobal.selectedFilterIndex < 22) {
                cvs.drawBitmap(btmPipe, 0.0f, 0.0f, new Paint());
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.thumbSize = (int) getResources().getDimension(R.dimen.lib_thumb_save_size);
        return inflater.inflate(R.layout.horizontal_fragment_effect, container, false);
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(getString(R.string.effect_parameter_bundle_name), this.parameterGlobal);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            this.parameterGlobal = savedInstanceState.getParcelable(getString(R.string.effect_parameter_bundle_name));
        } else if (getArguments() != null) {
            this.parameterGlobal = getArguments().getParcelable(getString(R.string.effect_parameter_bundle_name));
        }
        if (this.parameterGlobal == null) {
            this.parameterGlobal = new Parameter();
        }
        this.context = getActivity();
        this.activity = getActivity();
        initPaints();
        initAdapters();
        this.viewSwitcher = getView().findViewById(R.id.viewswitcher);
        Log.e(TAG, "viewSwitcher getDisplayedChild" + this.viewSwitcher.getDisplayedChild());
        this.viewFlipper = getView().findViewById(R.id.control_container);
        this.slideLeftIn = AnimationUtils.loadAnimation(this.activity, R.anim.slide_in_left);
        this.slideLeftOut = AnimationUtils.loadAnimation(this.activity, R.anim.slide_out_left);
        this.slideRightIn = AnimationUtils.loadAnimation(this.activity, R.anim.slide_in_right);
        this.slideRightOut = AnimationUtils.loadAnimation(this.activity, R.anim.slide_out_right);
        this.adjustmentLabel = getView().findViewById(R.id.lib_current_adjustmen_label);
        setSelectedTab(this.selectedTab);
        this.viewSwitcher.setDisplayedChild(1);
        setTabBg(this.selectedTab);
        if (this.excludeTabListener != null) {
            this.excludeTabListener.exclude();
        }
        if (this.footerListener != null) {
            this.footerListener.setVisibility();
        }
        this.seekBarAdjustment = getView().findViewById(R.id.seek_bar_adjustment);
        this.seekBarAdjustment.setOnSeekBarChangeListener(this.mySeekBarListener);
    }

    public void initPaints() {
        this.sepiaPaint = new Paint();
        ColorMatrix sepiaMatrix = new ColorMatrix();
        float[] fArr = new float[25];
        float[] fArr2 = new float[25];
        sepiaMatrix.set(new float[]{0.393f, 0.769f, 0.189f, 0.0f, 0.0f, 0.349f, 0.686f, 0.168f, 0.0f, 0.0f, 0.272f, 0.534f, 0.131f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f});
        this.sepiaPaint.setColorFilter(new ColorMatrixColorFilter(sepiaMatrix));
        this.grayPaint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0f);
        this.grayPaint.setColorFilter(new ColorMatrixColorFilter(cm));
        this.invertPaint = new Paint();
        ColorMatrix invertMatrix = new ColorMatrix();
        float[] fArr22 = new float[20];
        float[] fArr3 = new float[20];
        invertMatrix.set(new ColorMatrix(new float[]{-1.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, -1.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, -1.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        this.invertPaint.setColorFilter(new ColorMatrixColorFilter(invertMatrix));
        this.polaroidPaint = new Paint();
        ColorMatrix polaroidMatrix = new ColorMatrix();
        fArr22 = new float[20];
        fArr3 = new float[20];
        polaroidMatrix.set(new ColorMatrix(new float[]{2.0f, 0.0f, 0.0f, 0.0f, -130.0f, 0.0f, 2.0f, 0.0f, 0.0f, -130.0f, 0.0f, 0.0f, 2.0f, 0.0f, -130.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        this.polaroidPaint.setColorFilter(new ColorMatrixColorFilter(polaroidMatrix));
        this.scrimPaint = new Paint();
        ColorMatrix scrimMatrix = new ColorMatrix();
        fArr22 = new float[20];
        fArr3 = new float[20];
        scrimMatrix.set(new ColorMatrix(new float[]{5.0f, 0.0f, 0.0f, 0.0f, -254.0f, 0.0f, 5.0f, 0.0f, 0.0f, -254.0f, 0.0f, 0.0f, 5.0f, 0.0f, -254.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        this.scrimPaint.setColorFilter(new ColorMatrixColorFilter(scrimMatrix));
        this.abcPaint = new Paint();
        ColorMatrix abcMatrix = new ColorMatrix();
        fArr22 = new float[20];
        fArr3 = new float[20];
        abcMatrix.set(new ColorMatrix(new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        this.abcPaint.setColorFilter(new ColorMatrixColorFilter(abcMatrix));
        this.abc1Paint = new Paint();
        ColorMatrix abc1Matrix = new ColorMatrix();
        fArr22 = new float[20];
        fArr3 = new float[20];
        abc1Matrix.set(new ColorMatrix(new float[]{-0.36f, 1.691f, -0.32f, 0.0f, 0.0f, 0.325f, 0.398f, 0.275f, 0.0f, 0.0f, 0.79f, 0.796f, -0.76f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        this.abc1Paint.setColorFilter(new ColorMatrixColorFilter(abc1Matrix));
        this.abc2Paint = new Paint();
        ColorMatrix abc2Matrix = new ColorMatrix();
        fArr22 = new float[20];
        fArr3 = new float[20];
        abc2Matrix.set(new ColorMatrix(new float[]{-0.41f, 0.539f, -0.873f, 0.0f, 0.0f, 0.452f, 0.666f, -0.11f, 0.0f, 0.0f, -0.3f, 1.71f, -0.4f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        this.abc2Paint.setColorFilter(new ColorMatrixColorFilter(abc2Matrix));
        this.abc3Paint = new Paint();
        ColorMatrix abc3Matrix = new ColorMatrix();
        fArr22 = new float[20];
        fArr3 = new float[20];
        abc3Matrix.set(new ColorMatrix(new float[]{3.074f, -1.82f, -0.24f, 0.0f, 50.8f, -0.92f, 2.171f, -0.24f, 0.0f, 50.8f, -0.92f, -1.82f, 3.754f, 0.0f, 50.8f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        this.abc3Paint.setColorFilter(new ColorMatrixColorFilter(abc3Matrix));
        this.abc4Paint = new Paint();
        ColorMatrix abc4Matrix = new ColorMatrix();
        fArr22 = new float[20];
        fArr3 = new float[20];
        abc4Matrix.set(new ColorMatrix(new float[]{0.14f, 0.45f, 0.05f, 0.0f, 0.0f, 0.12f, 0.39f, 0.04f, 0.0f, 0.0f, 0.07999999821186066f, 0.28f, 0.03f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        this.abc4Paint.setColorFilter(new ColorMatrixColorFilter(abc4Matrix));
        this.purplePaint = new Paint();
        ColorMatrix purpleMatrix = new ColorMatrix();
        fArr22 = new float[20];
        fArr3 = new float[20];
        purpleMatrix.set(new ColorMatrix(new float[]{1.0f, -0.2f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, -0.1f, 0.0f, 0.0f, 1.2f, 1.0f, 0.1f, 0.0f, 0.0f, 0.0f, 1.7f, 1.0f, 0.0f}));
        this.purplePaint.setColorFilter(new ColorMatrixColorFilter(purpleMatrix));
        this.yellowPaint = new Paint();
        ColorMatrix yellowMatrix = new ColorMatrix();
        fArr22 = new float[20];
        fArr3 = new float[20];
        yellowMatrix.set(new ColorMatrix(new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.2f, 1.0f, 0.3f, 0.1f, 0.0f, -3.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        this.yellowPaint.setColorFilter(new ColorMatrixColorFilter(yellowMatrix));
        this.cyanPaint = new Paint();
        ColorMatrix cyanMatrix = new ColorMatrix();
        fArr22 = new float[20];
        fArr3 = new float[20];
        cyanMatrix.set(new ColorMatrix(new float[]{1.0f, 0.0f, 0.0f, 1.9f, -2.2f, 0.0f, 1.0f, 0.0f, 0.0f, 0.3f, 3.0f, 0.0f, 1.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.2f}));
        this.cyanPaint.setColorFilter(new ColorMatrixColorFilter(cyanMatrix));
        this.bwPaint = new Paint();
        ColorMatrix bwMatrix = new ColorMatrix();
        fArr22 = new float[20];
        fArr3 = new float[20];
        bwMatrix.set(new ColorMatrix(new float[]{0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f}));
        this.bwPaint.setColorFilter(new ColorMatrixColorFilter(bwMatrix));
        this.oldtimesPaint = new Paint();
        ColorMatrix oldtimesMatrix = new ColorMatrix();
        fArr22 = new float[20];
        fArr3 = new float[20];
        oldtimesMatrix.set(new ColorMatrix(new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.4f, 1.3f, -0.4f, 0.2f, -0.1f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        this.oldtimesPaint.setColorFilter(new ColorMatrixColorFilter(oldtimesMatrix));
        this.coldlifePaint = new Paint();
        ColorMatrix coldlifeMatrix = new ColorMatrix();
        fArr22 = new float[20];
        fArr3 = new float[20];
        coldlifeMatrix.set(new ColorMatrix(new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, -0.2f, 0.2f, 0.1f, 0.4f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        this.coldlifePaint.setColorFilter(new ColorMatrixColorFilter(coldlifeMatrix));
        this.sepiumPaint = new Paint();
        ColorMatrix sepiumMatrix = new ColorMatrix();
        fArr22 = new float[20];
        fArr3 = new float[20];
        sepiumMatrix.set(new ColorMatrix(new float[]{1.3f, -0.3f, 1.1f, 0.0f, 0.0f, 0.0f, 1.3f, 0.2f, 0.0f, 0.0f, 0.0f, 0.0f, 0.8f, 0.2f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        this.sepiumPaint.setColorFilter(new ColorMatrixColorFilter(sepiumMatrix));
        this.milkPaint = new Paint();
        ColorMatrix milkMatrix = new ColorMatrix();
        fArr22 = new float[20];
        fArr3 = new float[20];
        milkMatrix.set(new ColorMatrix(new float[]{0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.6f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        this.milkPaint.setColorFilter(new ColorMatrixColorFilter(milkMatrix));
        this.limePaint = new Paint();
        ColorMatrix limeMatrix = new ColorMatrix();
        fArr22 = new float[20];
        fArr3 = new float[20];
        limeMatrix.set(new ColorMatrix(new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        this.limePaint.setColorFilter(new ColorMatrixColorFilter(limeMatrix));
        this.peachyPaint = new Paint();
        ColorMatrix peachyMatrix = new ColorMatrix();
        fArr22 = new float[20];
        fArr3 = new float[20];
        peachyMatrix.set(new ColorMatrix(new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        this.peachyPaint.setColorFilter(new ColorMatrixColorFilter(peachyMatrix));
        this.lightenPaint = new Paint();
        ColorMatrix lightenMatrix = new ColorMatrix();
        fArr22 = new float[20];
        fArr3 = new float[20];
        lightenMatrix.set(new ColorMatrix(new float[]{1.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        this.lightenPaint.setColorFilter(new ColorMatrixColorFilter(lightenMatrix));
        this.darkenPaint = new Paint();
        ColorMatrix darkenMatrix = new ColorMatrix();
        fArr22 = new float[20];
        fArr3 = new float[20];
        darkenMatrix.set(new ColorMatrix(new float[]{0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        this.darkenPaint.setColorFilter(new ColorMatrixColorFilter(darkenMatrix));
    }

    private void initAdapters() {
        RecyclerAdapterIndexChangedListener borderL;
        RecyclerAdapterIndexChangedListener c09632 = new C05192();
        if (this.borderIndexChangedListener != null) {
            borderL = this.borderIndexChangedListener;
        } else {
            borderL = c09632;
        }
        this.borderAdapter = new MyRecyclerViewAdapter(LibUtility.borderResThumb, borderL, R.color.bg, R.color.app_foreground, 100);
        this.borderAdapter.setSelectedIndexChangedListener(new C05203());
        this.textureAdapter = new MyRecyclerViewAdapter(LibUtility.textureResThumb, new C05214(), R.color.bg, R.color.app_foreground, 100);
        this.textureAdapter.setSelectedIndexChangedListener(new C05225());
        this.overlayAdapter = new MyRecyclerViewAdapter(LibUtility.overlayResThumb, new C05236(), R.color.bg, R.color.app_foreground, 100);
        this.overlayAdapter.setSelectedIndexChangedListener(new C05247());
        this.filterAdapter = new MyRecyclerViewAdapter(LibUtility.filterResThumb, new C05258(), R.color.bg, R.color.app_foreground, 100);
        this.filterAdapter.setSelectedIndexChangedListener(new C05269());
        RecyclerView borderRecyclerView = getView().findViewById(R.id.border_RecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
        linearLayoutManager.setOrientation(0);
        borderRecyclerView.setLayoutManager(linearLayoutManager);
        borderRecyclerView.setAdapter(this.borderAdapter);
        borderRecyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView textureRecyclerView = getView().findViewById(R.id.texture_RecyclerView);
        linearLayoutManager = new LinearLayoutManager(this.context);
        linearLayoutManager.setOrientation(0);
        textureRecyclerView.setLayoutManager(linearLayoutManager);
        textureRecyclerView.setAdapter(this.textureAdapter);
        textureRecyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView overlayRecyclerView = getView().findViewById(R.id.overlay_RecyclerView);
        linearLayoutManager = new LinearLayoutManager(this.context);
        linearLayoutManager.setOrientation(0);
        overlayRecyclerView.setLayoutManager(linearLayoutManager);
        overlayRecyclerView.setAdapter(this.overlayAdapter);
        overlayRecyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView filterRecyclerView = getView().findViewById(R.id.filter_RecyclerView);
        linearLayoutManager = new LinearLayoutManager(this.context);
        linearLayoutManager.setOrientation(0);
        filterRecyclerView.setLayoutManager(linearLayoutManager);
        filterRecyclerView.setAdapter(this.filterAdapter);
        filterRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.textureAdapter.setSelectedView(this.parameterGlobal.selectedTextureIndex);
        this.borderAdapter.setSelectedView(this.parameterGlobal.selectedBorderIndex);
        this.overlayAdapter.setSelectedView(this.parameterGlobal.selectedOverlayIndex);
        if (this.parameterGlobal.selectedFilterIndex >= this.filterAdapter.getItemCount()) {
            this.parameterGlobal.selectedFilterIndex = 0;
        }
        this.filterAdapter.setSelectedView(this.parameterGlobal.selectedFilterIndex);
    }

    private void setTabBg(int index) {
        if (this.tabButtonList == null) {
            this.tabButtonList = new Button[14];
            this.tabButtonList[0] = getView().findViewById(R.id.button_fx);
            this.tabButtonList[1] = getView().findViewById(R.id.button_frame);
            this.tabButtonList[2] = getView().findViewById(R.id.button_light);
            this.tabButtonList[3] = getView().findViewById(R.id.button_texture);
            this.tabButtonList[10] = getView().findViewById(R.id.button_blur);
        }
        if (index >= 0) {
            this.adjustmentLabel.setText(this.tabButtonList[index].getText());
        }
    }

    void setSelectedTab(int index) {
        this.viewSwitcher.setDisplayedChild(0);
        int displayedChild = this.viewFlipper.getDisplayedChild();
        if (index == 0) {
            setTabBg(0);
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
        if (index == 2) {
            setTabBg(2);
            if (displayedChild != 2) {
                if (displayedChild == 3 || displayedChild == 4) {
                    this.viewFlipper.setInAnimation(this.slideLeftIn);
                    this.viewFlipper.setOutAnimation(this.slideRightOut);
                } else {
                    this.viewFlipper.setInAnimation(this.slideRightIn);
                    this.viewFlipper.setOutAnimation(this.slideLeftOut);
                }
                this.viewFlipper.setDisplayedChild(2);
            } else {
                return;
            }
        }
        if (index == 3) {
            setTabBg(3);
            if (displayedChild != 3) {
                if (displayedChild == 4) {
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
        if (index == 4 || index == 6 || index == 7 || index == 5 || index == 8 || index == 9 || index == 10) {
            setTabBg(index);
            if (displayedChild != 4) {
                this.viewFlipper.setInAnimation(this.slideRightIn);
                this.viewFlipper.setOutAnimation(this.slideLeftOut);
                this.viewFlipper.setDisplayedChild(4);
            }
        }
    }

    public void setBitmapReadyListener(BitmapReady listener) {
        this.bitmapReadyListener = listener;
    }

    public void setHideHeaderListener(HideHeaderListener l) {
        this.hideHeaderListener = l;
    }

    public boolean showToolBar() {
        if (this.viewSwitcher.getDisplayedChild() != 0) {
            return false;
        }
        cancelViewSwitcher();
        this.viewSwitcher.setDisplayedChild(1);
        return true;
    }

    public void onAttach(Activity act) {
        super.onAttach(act);
        this.context = getActivity();
        this.activity = getActivity();
    }

    public void setBorderIndexChangedListener(RecyclerAdapterIndexChangedListener l) {
        this.borderIndexChangedListener = l;
    }

    public void setSelectedTabIndex(int index) {
        if (index >= 0 && index < 14) {
            this.selectedTab = index;
            if (getView() != null) {
                setSelectedTab(index);
            }
        }
    }

    public int getSelectedTabIndex() {
        if (this.viewFlipper != null) {
            return this.viewFlipper.getDisplayedChild();
        }
        return -1;
    }

    void setSeekBarProgress() {
        int progress = 50;
        if (this.parameterGlobal.seekBarMode == 0) {
            progress = this.parameterGlobal.getBrightProgress();
        } else if (this.parameterGlobal.seekBarMode == 1) {
            progress = this.parameterGlobal.getContrastProgress();
        } else if (this.parameterGlobal.seekBarMode == 2) {
            progress = this.parameterGlobal.getTemperatureProgress();
        } else if (this.parameterGlobal.seekBarMode == 3) {
            progress = this.parameterGlobal.saturation;
        } else if (this.parameterGlobal.seekBarMode == 4) {
            progress = this.parameterGlobal.getTintProgressValue();
        } else if (this.parameterGlobal.seekBarMode == 5) {
            progress = this.parameterGlobal.getSharpenValue();
        } else if (this.parameterGlobal.seekBarMode == 6) {
            progress = this.parameterGlobal.getBlurValue();
        } else if (this.parameterGlobal.seekBarMode == 7) {
            progress = this.parameterGlobal.getHighlightValue();
        } else if (this.parameterGlobal.seekBarMode == 8) {
            progress = this.parameterGlobal.getShadowValue();
        }
        this.seekBarAdjustment.setProgress(progress);
    }

    public void callBack() {
        execQueue();
    }

    public void setBitmap(Bitmap btm) {
        this.sourceBitmap = btm;
        this.bitmapWidth = this.sourceBitmap.getWidth();
        this.bitmapHeight = this.sourceBitmap.getHeight();
        this.filterBitmap = null;
    }

    public void setBitmapAndResetBlur(Bitmap btm) {
        setBitmap(btm);
        Log.e(TAG, "setBitmapAndResetBlur setBitmapAndResetBlur");
        if (!(this.bitmapTiltBlur == null || this.bitmapTiltBlur.isRecycled())) {
            this.bitmapTiltBlur.recycle();
        }
        this.bitmapTiltBlur = null;
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    static int getBorderMode(int index) {
        return 0;
    }

    public synchronized void setBorder(Bitmap btm, int index, boolean isThumb) {
        if (isAdded() && index != 0 && LibUtility.borderRes.length > index) {
            Bitmap borderBitmap;
            Paint paint = new Paint(1);
            if (getBorderMode(index) == 1) {
                paint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));
            }
            Matrix borderMatrix = new Matrix();
            if (isThumb) {
                borderBitmap = BitmapFactory.decodeResource(getResources(), LibUtility.borderResThumb[index]);
            } else {
                borderBitmap = BitmapFactory.decodeResource(getResources(), LibUtility.borderRes[index]);
            }
            float wScale = ((float) btm.getWidth()) / ((float) borderBitmap.getWidth());
            float hScale = ((float) btm.getHeight()) / ((float) borderBitmap.getHeight());
            borderMatrix.reset();
            Canvas cvs = new Canvas(btm);
            borderMatrix.postScale(wScale, hScale);
            cvs.drawBitmap(borderBitmap, borderMatrix, paint);
            if (!(borderBitmap == null || btm == borderBitmap)) {
                borderBitmap.recycle();
            }
        }
    }

    @SuppressLint({"NewApi"})
    public void filterMultiply(Bitmap btm, int index, boolean isThumb) {
        if (index != 0 && isAdded()) {
            Bitmap textureBitmap;
            Paint paint = new Paint(1);
            Mode mode = Mode.SCREEN;
            if (LibUtility.textureModes[index] == LibUtility.MODE_MULTIPLY) {
                mode = Mode.MULTIPLY;
            } else if (LibUtility.textureModes[index] == LibUtility.MODE_OVERLAY && VERSION.SDK_INT > 10) {
                mode = Mode.OVERLAY;
            } else if (LibUtility.textureModes[index] == LibUtility.MODE_OVERLAY && VERSION.SDK_INT <= 10) {
                mode = Mode.MULTIPLY;
            }
            paint.setXfermode(new PorterDuffXfermode(mode));
            Matrix borderMatrix = new Matrix();
            if (isThumb) {
                textureBitmap = BitmapFactory.decodeResource(getResources(), LibUtility.textureResThumb[index]);
            } else {
                Options o2 = new Options();
                if (LibUtility.getLeftSizeOfMemory() > 1.024E7d) {
                    o2.inSampleSize = 1;
                } else {
                    o2.inSampleSize = 2;
                }
                textureBitmap = BitmapFactory.decodeResource(getResources(), LibUtility.textureRes[index], o2);
            }
            float wScale = ((float) btm.getWidth()) / ((float) textureBitmap.getWidth());
            float hScale = ((float) btm.getHeight()) / ((float) textureBitmap.getHeight());
            borderMatrix.reset();
            Canvas cvs = new Canvas(btm);
            borderMatrix.postScale(wScale, hScale);
            cvs.drawBitmap(textureBitmap, borderMatrix, paint);
            if (textureBitmap != null && btm != textureBitmap) {
                textureBitmap.recycle();
            }
        }
    }

    Bitmap getOverlayBitmap(int index) {
        Bitmap bitmap = null;
        if (isAdded()) {
            Options opts = new Options();
            opts.inPreferredConfig = Config.ARGB_8888;
            if (LibUtility.getLeftSizeOfMemory() > 1.024E7d) {
                opts.inSampleSize = 1;
            } else {
                opts.inSampleSize = 2;
            }
            if (index > 0 && index < LibUtility.overlayDrawableList.length) {
                Bitmap temp;
                bitmap = BitmapFactory.decodeResource(getResources(), LibUtility.overlayDrawableList[index], opts);
                if (bitmap.getConfig() != Config.ARGB_8888) {
                    temp = bitmap;
                    bitmap = bitmap.copy(Config.ARGB_8888, false);
                    if (bitmap != temp) {
                        temp.recycle();
                    }
                }
                int overlayWidth = bitmap.getWidth();
                int overlayHeight = bitmap.getHeight();
                if ((this.bitmapHeight > this.bitmapWidth && overlayHeight < overlayWidth) || (this.bitmapHeight < this.bitmapWidth && overlayHeight > overlayWidth)) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90.0f);
                    temp = bitmap;
                    bitmap = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), matrix, true);
                    if (bitmap != temp) {
                        temp.recycle();
                    }
                }
            }
        }
        return bitmap;
    }

    static int isOverlayScreenMode(int index) {
        if (index == 2) {
            return 2;
        }
        return 1;
    }


    public void myClickHandler(int id) {
        if (id != R.id.button_lib_cancel) {
            this.parameterBackUp.set(this.parameterGlobal);
        }
        if (id == R.id.button_fx) {
            setSelectedTab(0);
        } else if (id == R.id.button_frame) {
            setSelectedTab(1);
        } else if (id == R.id.button_light) {
            setSelectedTab(2);
        } else if (id == R.id.button_texture) {
            setSelectedTab(3);
        } else if (id == R.id.button_filter_reset) {
            resetParameters();
        } else if (id == R.id.button_blur) {
            setSelectedTab(10);
            this.parameterGlobal.seekBarMode = 6;
            setSeekBarProgress();
        } else if (id == R.id.button_lib_cancel) {
            cancelViewSwitcher();
            this.viewSwitcher.setDisplayedChild(1);
        } else if (id == R.id.button_lib_ok) {
            this.viewSwitcher.setDisplayedChild(1);
        }
    }

    @SuppressLint({"NewApi"})
    public void applyOverlay11(Bitmap overlay, Bitmap btm, int screenMode) {
        Paint paint = new Paint(1);
        paint.setFilterBitmap(true);
        Mode mode = Mode.SCREEN;
        if (screenMode == 0) {
            mode = Mode.OVERLAY;
        }
        PorterDuffXfermode porterMode = new PorterDuffXfermode(mode);
        if (screenMode == 2) {
            porterMode = null;
        }
        paint.setXfermode(porterMode);
        Matrix borderMatrix = new Matrix();
        float wScale = ((float) btm.getWidth()) / ((float) overlay.getWidth());
        float hScale = ((float) btm.getHeight()) / ((float) overlay.getHeight());
        borderMatrix.reset();
        Canvas cvs = new Canvas(btm);
        borderMatrix.postScale(wScale, hScale);
        cvs.drawBitmap(overlay, borderMatrix, paint);
    }

    private void cancelViewSwitcher() {
        Log.e(TAG, "parameterGlobal borderAdapter index " + this.parameterGlobal.selectedBorderIndex);
        Log.e(TAG, "parameterBackUp index " + this.parameterBackUp.selectedBorderIndex);
        Log.e(TAG, "borderAdapter index " + this.borderAdapter.getSelectedIndex());
        if (this.parameterGlobal.isParameterReallyChanged(this.parameterBackUp)) {
            this.parameterGlobal.set(this.parameterBackUp);
            this.textureAdapter.setSelectedView(this.parameterGlobal.selectedTextureIndex);
            this.borderAdapter.setSelectedView(this.parameterGlobal.selectedBorderIndex);
            if (this.borderIndexChangedListener != null) {
                this.borderIndexChangedListener.onIndexChanged(this.parameterGlobal.selectedBorderIndex);
            }
            Log.e(TAG, "borderAdapter index " + this.borderAdapter.getSelectedIndex());
            this.overlayAdapter.setSelectedView(this.parameterGlobal.selectedOverlayIndex);
            if (this.parameterGlobal.selectedFilterIndex >= this.filterAdapter.getItemCount()) {
                this.parameterGlobal.selectedFilterIndex = 0;
            }
            this.filterAdapter.setSelectedView(this.parameterGlobal.selectedFilterIndex);
            execQueue();
        }
    }

    void resetParameters() {
        this.parameterGlobal.reset();
        setAdjustmentSeekbarProgress();
    }

    public Parameter getParameter() {
        return this.parameterGlobal;
    }

    public void setParameters(Parameter parameter) {
        this.parameterGlobal.set(parameter);
        setAdjustmentSeekbarProgress();
    }

    void resetParametersWithoutChange() {
        this.parameterGlobal.reset();
        setSelectedIndexes();
        setSeekBarProgress();
    }

    public void setAutoParameters(int brightness, int saturation, int contrast, int warmth) {
        this.parameterGlobal.setBrightness(brightness);
        this.parameterGlobal.setContrast(contrast);
        this.parameterGlobal.setSaturation(saturation);
        this.parameterGlobal.setTemperature(warmth);
        this.parameterGlobal.setTint(50);
        setAdjustmentSeekbarProgress();
    }

    void setAdjustmentSeekbarProgress() {
        setSeekBarProgress();
        setSelectedIndexes();
        execQueue();
    }

    void setSelectedIndexes() {
        this.textureAdapter.setSelectedView(this.parameterGlobal.selectedTextureIndex);
        this.borderAdapter.setSelectedView(this.parameterGlobal.selectedBorderIndex);
        this.overlayAdapter.setSelectedView(this.parameterGlobal.selectedOverlayIndex);
        this.filterAdapter.setSelectedView(this.parameterGlobal.selectedFilterIndex);
    }

    void applyChangesOnBitmap() {
        this.parameterGlobal.selectedFilterIndex = this.filterAdapter.getSelectedIndex();
        this.parameterGlobal.selectedBorderIndex = this.borderAdapter.getSelectedIndex();
        this.parameterGlobal.selectedTextureIndex = this.textureAdapter.getSelectedIndex();
        this.parameterGlobal.selectedOverlayIndex = this.overlayAdapter.getSelectedIndex();
        execQueue();
    }

    public void execQueue() {
        if (this.ft == null || this.ft.getStatus() != Status.RUNNING) {
            this.ft = new FilterAndAdjustmentTask();
                this.ft.execute();
        }
    }

    public void setFilter(int index, Bitmap btm) {
        if (index >= filterBitmapTitle.length) {
            index = 0;
        }
        index--;
        if (VERSION.SDK_INT != 7 && index != -1) {
            if (index == 0) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.grayPaint);
            } else if (index == 1) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.sepiaPaint);
            } else if (index == 2) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.purplePaint);
            } else if (index == 3) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.yellowPaint);
            } else if (index == 4) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.milkPaint);
            } else if (index == 5) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.coldlifePaint);
            } else if (index == 6) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.bwPaint);
            } else if (index == 7) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.limePaint);
            } else if (index == 8) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.sepiumPaint);
            } else if (index == 9) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.oldtimesPaint);
            } else if (index == 10) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.cyanPaint);
            } else if (index == 11) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.polaroidPaint);
            } else if (index == 12) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.invertPaint);
            } else if (index == 13) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.abc1Paint);
            } else if (index == 14) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.abc4Paint);
            } else if (index == 15) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.lightenPaint);
            } else if (index == 16) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.abc3Paint);
            } else if (index == 17) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.scrimPaint);
            } else if (index == 18) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.abc2Paint);
            } else if (index == 19) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.darkenPaint);
            } else if (index == 20) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.abcPaint);
            } else if (index == 21) {
                new Canvas(btm).drawBitmap(btm, 0.0f, 0.0f, this.peachyPaint);
            }
        }
    }

    boolean checkAutoParameters() {
        if (this.autoParameters == null) {
            return false;
        }
        for (int i = 0; i < this.parameterSize; i++) {
            if (this.autoParameters[i] <= 0.0f) {
                return false;
            }
        }
        return true;
    }
}
