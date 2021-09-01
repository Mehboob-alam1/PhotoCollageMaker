package com.helloworld.photoeditor.pointlist;

import android.graphics.PointF;

import com.helloworld.photoeditor.R;

import java.util.ArrayList;

public class Collage1 extends Collage {
    public static int shapeCount = 1;

    public Collage1(int i, int j) {
        this.collageLayoutList = new ArrayList();
        ArrayList obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f)});
        this.collageLayoutList.add(new CollageLayout(obj));
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f)});
        CollageLayout obj2 = new CollageLayout(obj);
        obj2.maskPairList.add(new MaskPair(0, R.drawable.mask_butterfly));
        this.collageLayoutList.add(obj2);
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f)});
        obj2 = new CollageLayout(obj);
        obj2.maskPairList.add(new MaskPair(0, R.drawable.mask_cloud));
        this.collageLayoutList.add(obj2);
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f)});
        obj2 = new CollageLayout(obj);
        obj2.maskPairList.add(new MaskPair(0, R.drawable.mask_clover));
        this.collageLayoutList.add(obj2);
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f)});
        obj2 = new CollageLayout(obj);
        obj2.maskPairList.add(new MaskPair(0, R.drawable.mask_leaf));
        this.collageLayoutList.add(obj2);
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f)});
        obj2 = new CollageLayout(obj);
        obj2.maskPairList.add(new MaskPair(0, R.drawable.mask_left_foot));
        this.collageLayoutList.add(obj2);
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f)});
        obj2 = new CollageLayout(obj);
        obj2.maskPairList.add(new MaskPair(0, R.drawable.mask_diamond));
        this.collageLayoutList.add(obj2);
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f)});
        obj2 = new CollageLayout(obj);
        obj2.maskPairList.add(new MaskPair(0, R.drawable.mask_hexagon));
        this.collageLayoutList.add(obj2);
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f)});
        obj2 = new CollageLayout(obj);
        obj2.maskPairList.add(new MaskPair(0, R.drawable.mask_heart));
        this.collageLayoutList.add(obj2);
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f)});
        obj2 = new CollageLayout(obj);
        obj2.maskPairList.add(new MaskPair(0, R.drawable.mask_paw));
        this.collageLayoutList.add(obj2);
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f)});
        obj2 = new CollageLayout(obj);
        obj2.maskPairList.add(new MaskPair(0, R.drawable.mask_circle));
        this.collageLayoutList.add(obj2);
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f)});
        obj2 = new CollageLayout(obj);
        obj2.maskPairList.add(new MaskPair(0, R.drawable.mask_twitter));
        this.collageLayoutList.add(obj2);
    }
}
