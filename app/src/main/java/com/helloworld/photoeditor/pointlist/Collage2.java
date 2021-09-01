package com.helloworld.photoeditor.pointlist;

import android.graphics.PointF;

import com.helloworld.photoeditor.R;

import java.util.ArrayList;

public class Collage2 extends Collage {
    public static int shapeCount = 2;

    public Collage2(int i, int j) {
        this.collageLayoutList = new ArrayList();
        ArrayList obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(0.5f * ((float) i), ((float) j) * 1.0f), new PointF(0.5f * ((float) i), ((float) j) * 0.0f), new PointF(((float) i) * 0.0f, ((float) j) * 0.0f)});
        obj.add(new PointF[]{new PointF(0.5f * ((float) i), ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f), new PointF(0.5f * ((float) i), ((float) j) * 1.0f)});
        this.collageLayoutList.add(new CollageLayout(obj));
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 0.0f, 0.5f * ((float) j)), new PointF(((float) i) * 1.0f, 0.5f * ((float) j)), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f)});
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, 0.5f * ((float) j)), new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, 0.5f * ((float) j))});
        this.collageLayoutList.add(new CollageLayout(obj));
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(0.6f * ((float) i), ((float) j) * 1.0f), new PointF(0.6f * ((float) i), ((float) j) * 0.0f), new PointF(((float) i) * 0.0f, ((float) j) * 0.0f)});
        obj.add(new PointF[]{new PointF(0.4f * ((float) i), ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f), new PointF(0.4f * ((float) i), ((float) j) * 1.0f)});
        CollageLayout obj2 = new CollageLayout(obj);
        obj2.maskPairList.add(new MaskPair(0, R.drawable.mask_heart));
        obj2.maskPairList.add(new MaskPair(1, R.drawable.mask_heart));
        obj2.setClearIndex(1);
        this.collageLayoutList.add(obj2);
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f)});
        obj.add(new PointF[]{new PointF(0.13f * ((float) i), 0.13f * ((float) j)), new PointF(0.13f * ((float) i), 0.87f * ((float) j)), new PointF(0.87f * ((float) i), 0.87f * ((float) j)), new PointF(0.87f * ((float) i), 0.13f * ((float) j))});
        obj2 = new CollageLayout(obj);
        obj2.maskPairList.add(new MaskPair(1, R.drawable.mask_heart));
        obj2.setClearIndex(1);
        this.collageLayoutList.add(obj2);
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 0.0f, 0.3333333f * ((float) j)), new PointF(((float) i) * 1.0f, 0.3333333f * ((float) j)), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f)});
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, 0.3333333f * ((float) j)), new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, 0.3333333f * ((float) j))});
        this.collageLayoutList.add(new CollageLayout(obj));
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f)});
        obj.add(new PointF[]{new PointF(0.13f * ((float) i), 0.13f * ((float) j)), new PointF(0.13f * ((float) i), 0.87f * ((float) j)), new PointF(0.87f * ((float) i), 0.87f * ((float) j)), new PointF(0.87f * ((float) i), 0.13f * ((float) j))});
        obj2 = new CollageLayout(obj);
        obj2.maskPairList.add(new MaskPair(1, R.drawable.mask_cloud));
        obj2.setClearIndex(1);
        this.collageLayoutList.add(obj2);
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 0.0f, 0.5833f * ((float) j)), new PointF(0.1667f * ((float) i), 0.41667f * ((float) j)), new PointF(0.333f * ((float) i), 0.5833f * ((float) j)), new PointF(0.5f * ((float) i), 0.41667f * ((float) j)), new PointF(0.6667f * ((float) i), 0.5833f * ((float) j)), new PointF(0.8333f * ((float) i), 0.41667f * ((float) j)), new PointF((float) (i * 1), 0.5833f * ((float) j)), new PointF((float) (i * 1), (float) (j * 1))});
        obj.add(new PointF[]{new PointF((float) (i * 0), 0.5833f * ((float) j)), new PointF((float) (i * 0), (float) (j * 0)), new PointF((float) (i * 1), (float) (j * 0)), new PointF((float) (i * 1), 0.5833f * ((float) j)), new PointF(0.8333f * ((float) i), 0.41667f * ((float) j)), new PointF(0.6667f * ((float) i), 0.5833f * ((float) j)), new PointF(0.5f * ((float) i), 0.41667f * ((float) j)), new PointF(0.333f * ((float) i), 0.5833f * ((float) j)), new PointF(0.1667f * ((float) i), 0.41667f * ((float) j))});
        this.collageLayoutList.add(new CollageLayout(obj));
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f)});
        obj.add(new PointF[]{new PointF(0.13f * ((float) i), 0.13f * ((float) j)), new PointF(0.13f * ((float) i), 0.87f * ((float) j)), new PointF(0.87f * ((float) i), 0.87f * ((float) j)), new PointF(0.87f * ((float) i), 0.13f * ((float) j))});
        obj2 = new CollageLayout(obj);
        obj2.maskPairList.add(new MaskPair(1, R.drawable.mask_hexagon));
        obj2.setClearIndex(1);
        this.collageLayoutList.add(obj2);
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 0.0f, 0.5f * ((float) j)), new PointF(((float) i) * 1.0f, 0.3333333f * ((float) j)), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f)});
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, 0.5f * ((float) j)), new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, 0.3333333f * ((float) j))});
        this.collageLayoutList.add(new CollageLayout(obj));
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 0.0f, 0.6666667f * ((float) j)), new PointF(((float) i) * 1.0f, 0.6666667f * ((float) j)), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f)});
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, 0.6666667f * ((float) j)), new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, 0.6666667f * ((float) j))});
        this.collageLayoutList.add(new CollageLayout(obj));
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 0.0f, 0.3333333f * ((float) j)), new PointF(((float) i) * 1.0f, 0.6666667f * ((float) j)), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f)});
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, 0.3333333f * ((float) j)), new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, 0.6666667f * ((float) j))});
        this.collageLayoutList.add(new CollageLayout(obj));
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f)});
        obj.add(new PointF[]{new PointF(0.6f * ((float) i), 0.6f * ((float) j)), new PointF(0.6f * ((float) i), 0.9333333f * ((float) j)), new PointF(0.9333333f * ((float) i), 0.9333333f * ((float) j)), new PointF(0.9333333f * ((float) i), 0.6f * ((float) j))});
        obj2 = new CollageLayout(obj);
        obj2.setClearIndex(1);
        this.collageLayoutList.add(obj2);
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(0.3333333f * ((float) i), ((float) j) * 0.0f), new PointF(0.3333333f * ((float) i), ((float) j) * 1.0f)});
        obj.add(new PointF[]{new PointF(0.3333333f * ((float) i), ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f), new PointF(0.3333333f * ((float) i), ((float) j) * 1.0f)});
        this.collageLayoutList.add(new CollageLayout(obj));
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(0.3333333f * ((float) i), ((float) j) * 0.0f), new PointF(0.6666667f * ((float) i), ((float) j) * 1.0f)});
        obj.add(new PointF[]{new PointF(0.3333333f * ((float) i), ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f), new PointF(0.6666667f * ((float) i), ((float) j) * 1.0f)});
        this.collageLayoutList.add(new CollageLayout(obj));
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(0.6666667f * ((float) i), ((float) j) * 0.0f), new PointF(0.6666667f * ((float) i), ((float) j) * 1.0f)});
        obj.add(new PointF[]{new PointF(0.6666667f * ((float) i), ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f), new PointF(0.6666667f * ((float) i), ((float) j) * 1.0f)});
        this.collageLayoutList.add(new CollageLayout(obj));
        obj = new ArrayList();
        obj.add(new PointF[]{new PointF(((float) i) * 0.0f, ((float) j) * 1.0f), new PointF(((float) i) * 0.0f, ((float) j) * 0.0f), new PointF(0.6666667f * ((float) i), ((float) j) * 0.0f), new PointF(0.3333333f * ((float) i), ((float) j) * 1.0f)});
        obj.add(new PointF[]{new PointF(0.6666667f * ((float) i), ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 0.0f), new PointF(((float) i) * 1.0f, ((float) j) * 1.0f), new PointF(0.3333333f * ((float) i), ((float) j) * 1.0f)});
        this.collageLayoutList.add(new CollageLayout(obj));
    }
}
