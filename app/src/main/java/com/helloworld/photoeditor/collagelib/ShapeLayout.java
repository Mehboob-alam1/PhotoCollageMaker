package com.helloworld.photoeditor.collagelib;

public class ShapeLayout {
    boolean isScalable = false;
    int porterDuffClearBorderIntex = -1;
    Shape[] shapeArr;

    public ShapeLayout(Shape[] arr) {
        this.shapeArr = arr;
    }

    public void setClearIndex(int index) {
        if (index >= 0 && index < this.shapeArr.length) {
            this.porterDuffClearBorderIntex = index;
        }
    }

    public void setScalibility(boolean scalebility) {
        this.isScalable = scalebility;
    }

    public int getClearIndex() {
        return this.porterDuffClearBorderIntex;
    }

    public boolean getScalibility() {
        return this.isScalable;
    }
}
