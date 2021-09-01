package com.helloworld.photoeditor.canvastext;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Typeface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TextData implements Serializable {
    public static final int defBgAlpha = 255;
    public static final int defBgColor = 0;
    public static final String defaultMessage = "Enter Text";
    private static final long serialVersionUID = 3789254181897332363L;
    public MyMatrix canvasMatrix;
    private String fontPath;
    public MyMatrix imageSaveMatrix;
    public boolean isTypeFaceSet;
    public String message;
    public MyPaint textPaint;
    public float textSize;
    public float xPos;
    public float yPos;

    public void setTextFont(String path, Context context) {
        this.fontPath = path;
        if (this.fontPath != null) {
            Typeface typeFace = FontCache.get(context, this.fontPath);
            if (typeFace != null) {
                this.textPaint.setTypeface(typeFace);
            }
            this.isTypeFaceSet = true;
        }
    }

    public String getFontPath() {
        return this.fontPath;
    }

    public TextData(float textSize) {
        this.message = defaultMessage;
        this.isTypeFaceSet = false;
        this.canvasMatrix = new MyMatrix(this.canvasMatrix);
        this.textPaint = new MyPaint();
        this.textPaint.setAntiAlias(true);
        this.textPaint.setColor(-65536);
        this.textSize = textSize;
        this.textPaint.setTextSize(textSize);
        this.fontPath = null;
    }

    public TextData() {
        this.message = defaultMessage;
        this.isTypeFaceSet = false;
        this.canvasMatrix = new MyMatrix(this.canvasMatrix);
        this.textPaint = new MyPaint();
        this.textPaint.setAntiAlias(true);
        this.textPaint.setColor(-1);
        this.textSize = 60.0f;
        this.textPaint.setTextSize(this.textSize);
        this.fontPath = null;
    }

    public TextData(TextData src) {
        this.message = defaultMessage;
        this.isTypeFaceSet = false;
        this.canvasMatrix = new MyMatrix(src.canvasMatrix);
        this.textPaint = new MyPaint(src.textPaint);
        this.textPaint.setAntiAlias(true);
        this.message = new String(src.message);
        this.textSize = src.textSize;
        this.xPos = src.xPos;
        this.yPos = src.yPos;
        if (src.imageSaveMatrix != null) {
            this.imageSaveMatrix = new MyMatrix(src.imageSaveMatrix);
        }
        if (src.fontPath != null) {
            this.fontPath = src.fontPath;
        }
    }

    public void setImageSaveMatrix(Matrix mtr) {
        MyMatrix inverse = new MyMatrix(mtr);
        mtr.invert(inverse);
        inverse.preConcat(this.canvasMatrix);
        this.imageSaveMatrix = inverse;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeFloat(this.xPos);
        oos.writeFloat(this.yPos);
        oos.writeFloat(this.textSize);
        oos.writeObject(this.textPaint);
        oos.writeObject(this.message);
        oos.writeObject(this.canvasMatrix);
        oos.writeObject(this.imageSaveMatrix);
        oos.writeObject(this.fontPath);
    }

    private void readObject(ObjectInputStream ois) throws Exception {
        ois.defaultReadObject();
        this.xPos = ois.readFloat();
        this.yPos = ois.readFloat();
        this.textSize = ois.readFloat();
        this.textPaint = (MyPaint) ois.readObject();
        this.message = (String) ois.readObject();
        this.canvasMatrix = (MyMatrix) ois.readObject();
        this.imageSaveMatrix = (MyMatrix) ois.readObject();
        try {
            this.fontPath = (String) ois.readObject();
        } catch (Exception e) {
            this.fontPath = null;
        }
        this.textPaint.setAntiAlias(true);
        this.isTypeFaceSet = false;
    }
}
