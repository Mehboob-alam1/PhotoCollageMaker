package com.helloworld.photoeditor.canvastext;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.helloworld.photoeditor.R;
import com.helloworld.photoeditor.common_lib.OnItemSelected;

import java.util.ArrayList;

public class CustomRelativeLayout extends RelativeLayout implements OnClickListener, OnItemSelected {
    private static final String TAG = "CustomRelativeLayout";
    ApplyTextInterface applyListener;
    ArrayList<CanvasTextView> canvasTextViewList;
    Context context;
    int currentCanvasTextIndex = 0;
    LayoutParams levelParams;
    RelativeLayout mainLayout;
    public OnClickListener onClickListener;
    Bitmap removeBitmap;
    RemoveTextListener removeTextListener = new RemoveText();
    Bitmap scaleBitmap;
    SingleTap singleTapListener;
    ArrayList<TextData> textDataList;
    ArrayList<TextData> textDataListOriginal;
    ViewSelectedListener viewSelectedListner = new ViewSelector();

    public interface RemoveTextListener {
        void onRemove();
    }

    class RemoveText implements RemoveTextListener {
        RemoveText() {
        }

        public void onRemove() {
            if (!CustomRelativeLayout.this.canvasTextViewList.isEmpty()) {
                CanvasTextView canvasTextView = (CanvasTextView) CustomRelativeLayout.this.canvasTextViewList.get(CustomRelativeLayout.this.currentCanvasTextIndex);
                CustomRelativeLayout.this.mainLayout.removeView(canvasTextView);
                CustomRelativeLayout.this.canvasTextViewList.remove(canvasTextView);
                CustomRelativeLayout.this.textDataList.remove(canvasTextView.textData);
                CustomRelativeLayout.this.currentCanvasTextIndex = CustomRelativeLayout.this.canvasTextViewList.size() - 1;
                if (!CustomRelativeLayout.this.canvasTextViewList.isEmpty()) {
                    ((CanvasTextView) CustomRelativeLayout.this.canvasTextViewList.get(CustomRelativeLayout.this.currentCanvasTextIndex)).setTextSelected(true);
                }
            }
        }
    }

    class ViewSelector implements ViewSelectedListener {
        ViewSelector() {
        }

        public void setSelectedView(CanvasTextView cvt) {
            CustomRelativeLayout.this.currentCanvasTextIndex = CustomRelativeLayout.this.canvasTextViewList.indexOf(cvt);
            for (int i = 0; i < CustomRelativeLayout.this.canvasTextViewList.size(); i++) {
                if (CustomRelativeLayout.this.currentCanvasTextIndex != i) {
                    ((CanvasTextView) CustomRelativeLayout.this.canvasTextViewList.get(i)).setTextSelected(false);
                }
            }
        }
    }

    public void itemSelected(int color) {
        if (!this.canvasTextViewList.isEmpty()) {
            ((CanvasTextView) this.canvasTextViewList.get(this.currentCanvasTextIndex)).setTextColor(color);
        }
    }

    public void onClick(View v) {
        hideSoftKeyboard((Activity) this.context);
        int id = v.getId();
        if (id == R.id.button_text_color) {
            return;
        }
        int i;
        if (id == R.id.button_apply_action) {
            i = 0;
            while (i < this.textDataList.size()) {
                if (((TextData) this.textDataList.get(i)).message.compareTo(TextData.defaultMessage) == 0) {
                    this.textDataList.remove(i);
                    i--;
                }
                i++;
            }
            this.applyListener.onOk(this.textDataList);
        } else if (id == R.id.button_cancel_action) {
            this.textDataList.clear();
            for (i = 0; i < this.textDataListOriginal.size(); i++) {
                this.textDataList.add(this.textDataListOriginal.get(i));
            }
            this.applyListener.onCancel();
        }
    }

    void loadBitmaps() {
        if (this.removeBitmap == null || this.removeBitmap.isRecycled()) {
            this.removeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.close);
        }
        if (this.scaleBitmap == null || this.scaleBitmap.isRecycled()) {
            this.scaleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_accept);
        }
    }

    public CustomRelativeLayout(Context c, ArrayList<TextData> textDataListParam, Matrix canvasMatrix, SingleTap l) {
        super(c);
        int i;
        this.context = c;
        this.singleTapListener = l;
        loadBitmaps();
        ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_canvas, this);
        this.mainLayout = (RelativeLayout) findViewById(R.id.canvas_relative_layout);
        this.levelParams = new LayoutParams(-1, -1);
        this.levelParams.addRule(15, -1);
        this.levelParams.addRule(14, -1);
        this.canvasTextViewList = new ArrayList();
        this.textDataList = new ArrayList();
        this.textDataListOriginal = new ArrayList();
        for (i = 0; i < textDataListParam.size(); i++) {
            this.textDataListOriginal.add(new TextData((TextData) textDataListParam.get(i)));
            this.textDataList.add(new TextData((TextData) textDataListParam.get(i)));
        }
        for (i = 0; i < this.textDataList.size(); i++) {
            CanvasTextView canvasTextView = new CanvasTextView(this.context, (TextData) this.textDataList.get(i), this.removeBitmap, this.scaleBitmap);
            canvasTextView.setSingleTapListener(this.singleTapListener);
            canvasTextView.setViewSelectedListener(this.viewSelectedListner);
            canvasTextView.setRemoveTextListener(new RemoveText());
            this.mainLayout.addView(canvasTextView, this.levelParams);
            this.canvasTextViewList.add(canvasTextView);
            MyMatrix textMatrix = new MyMatrix(canvasMatrix);
            textMatrix.set(((TextData) this.textDataList.get(i)).imageSaveMatrix);
            textMatrix.postConcat(canvasMatrix);
            canvasTextView.setMatrix(textMatrix);
        }
        if (!this.canvasTextViewList.isEmpty()) {
            ((CanvasTextView) this.canvasTextViewList.get(this.canvasTextViewList.size() - 1)).setTextSelected(true);
            this.currentCanvasTextIndex = this.canvasTextViewList.size() - 1;
        }
        TextView cancelButton = (TextView) findViewById(R.id.button_cancel_action);
        findViewById(R.id.button_apply_action).setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    public void setSingleTapListener(SingleTap l) {
        this.singleTapListener = l;
    }

    public void setApplyTextListener(ApplyTextInterface l) {
        this.applyListener = l;
    }

    public void addTextView(TextData textData) {
        if (this.textDataList.contains(textData)) {
            Log.e(TAG, "textDataList.contains(textData)");
            ((CanvasTextView) this.canvasTextViewList.get(this.currentCanvasTextIndex)).setNewTextData(textData);
            return;
        }
        for (int i = 0; i < this.canvasTextViewList.size(); i++) {
            ((CanvasTextView) this.canvasTextViewList.get(i)).setTextSelected(false);
        }
        this.currentCanvasTextIndex = this.canvasTextViewList.size();
        loadBitmaps();
        CanvasTextView canvasTextView = new CanvasTextView(this.context, textData, this.removeBitmap, this.scaleBitmap);
        canvasTextView.setSingleTapListener(this.singleTapListener);
        canvasTextView.setViewSelectedListener(this.viewSelectedListner);
        canvasTextView.setRemoveTextListener(new RemoveText());
        this.canvasTextViewList.add(canvasTextView);
        this.mainLayout.addView(canvasTextView);
        this.textDataList.add(canvasTextView.textData);
        canvasTextView.setTextSelected(true);
        canvasTextView.singleTapped();
    }

    public void addTextDataEx(TextData textData) {
        if (this.textDataList.contains(textData)) {
            Log.e(TAG, "textDataList.contains(textData)");
            ((CanvasTextView) this.canvasTextViewList.get(this.currentCanvasTextIndex)).setNewTextData(textData);
            return;
        }
        for (int i = 0; i < this.canvasTextViewList.size(); i++) {
            ((CanvasTextView) this.canvasTextViewList.get(i)).setTextSelected(false);
        }
        this.currentCanvasTextIndex = this.canvasTextViewList.size();
        CanvasTextView canvasTextView = new CanvasTextView(this.context, textData, this.removeBitmap, this.scaleBitmap);
        canvasTextView.setSingleTapListener(this.singleTapListener);
        canvasTextView.setViewSelectedListener(this.viewSelectedListner);
        canvasTextView.setRemoveTextListener(new RemoveText());
    }

    public boolean onTouchEvent(MotionEvent event) {
        hideSoftKeyboard((Activity) this.context);
        return true;
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService("input_method");
        if (inputMethodManager != null && activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
