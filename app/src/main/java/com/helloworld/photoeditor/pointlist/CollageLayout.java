package com.helloworld.photoeditor.pointlist;

import java.util.ArrayList;
import java.util.List;

public class CollageLayout {
    public int[][] exceptionIndexForShapes = ((int[][]) null);
    boolean isScalable = false;
    public List<MaskPair> maskPairList = new ArrayList();
    public List<MaskPairSvg> maskPairListSvg = new ArrayList();
    int porterDuffClearBorderIntex = -1;
    public List shapeList;
    public boolean useLine = true;

    public CollageLayout(List list) {
        this.shapeList = list;
    }

    public int getClearIndex() {
        return this.porterDuffClearBorderIntex;
    }

    public boolean getScalibility() {
        return this.isScalable;
    }

    public int[] getexceptionIndex(int i) {
        if (this.exceptionIndexForShapes == null || i >= this.exceptionIndexForShapes.length || i < 0) {
            return null;
        }
        return this.exceptionIndexForShapes[i];
    }

    public void setClearIndex(int i) {
        if (i >= 0 && i < this.shapeList.size()) {
            this.porterDuffClearBorderIntex = i;
        }
    }

    public void setScalibility(boolean flag) {
        this.isScalable = flag;
    }
}
