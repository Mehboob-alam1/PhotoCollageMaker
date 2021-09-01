package com.helloworld.photoeditor.pointlist;

import com.helloworld.photoeditor.R;

import java.util.ArrayList;
import java.util.List;

public class Collage {
    public static int[][] collageIconArray;
    public static float scrapBookShapeScale = 1.0f;
    public List collageLayoutList;

    static {
        int[] ai1 = new int[]{R.drawable.collage_3_0, R.drawable.collage_3_1, R.drawable.collage_3_2, R.drawable.collage_3_3, R.drawable.collage_3_4, R.drawable.collage_3_5, R.drawable.collage_3_6, R.drawable.collage_3_7, R.drawable.collage_3_8, R.drawable.collage_3_9, R.drawable.collage_3_10, R.drawable.collage_3_11, R.drawable.collage_3_12, R.drawable.collage_3_13, R.drawable.collage_3_14, R.drawable.collage_3_15, R.drawable.collage_3_16, R.drawable.collage_3_17, R.drawable.collage_3_18, R.drawable.collage_3_19, R.drawable.collage_3_20, R.drawable.collage_3_21};
        int[] ai2 = new int[]{R.drawable.collage_5_0, R.drawable.collage_5_1, R.drawable.collage_5_2, R.drawable.collage_5_3, R.drawable.collage_5_4, R.drawable.collage_5_5, R.drawable.collage_5_6, R.drawable.collage_5_7, R.drawable.collage_5_8, R.drawable.collage_5_9, R.drawable.collage_5_10, R.drawable.collage_5_11, R.drawable.collage_5_12, R.drawable.collage_5_13, R.drawable.collage_5_14, R.drawable.collage_5_15, R.drawable.collage_5_16, R.drawable.collage_5_17, R.drawable.collage_5_18, R.drawable.collage_5_19, R.drawable.collage_5_20};
        int[] ai3 = new int[]{R.drawable.collage_7_0, R.drawable.collage_7_1, R.drawable.collage_7_2, R.drawable.collage_7_3, R.drawable.collage_7_4, R.drawable.collage_7_5, R.drawable.collage_7_6, R.drawable.collage_7_7, R.drawable.collage_7_8, R.drawable.collage_7_9};
        int[] ai4 = new int[]{R.drawable.collage_8_0, R.drawable.collage_8_1, R.drawable.collage_8_2, R.drawable.collage_8_3, R.drawable.collage_8_4, R.drawable.collage_8_5, R.drawable.collage_8_6, R.drawable.collage_8_7, R.drawable.collage_8_8, R.drawable.collage_8_9, R.drawable.collage_8_10, R.drawable.collage_8_11, R.drawable.collage_8_12, R.drawable.collage_8_13, R.drawable.collage_8_14, R.drawable.collage_8_15};
        int[][] r62 = new int[9][];
        r62[1] = new int[]{R.drawable.collage_2_0, R.drawable.collage_2_1, R.drawable.collage_2_2, R.drawable.collage_2_3, R.drawable.collage_2_4, R.drawable.collage_2_5, R.drawable.collage_2_6, R.drawable.collage_2_7, R.drawable.collage_2_8, R.drawable.collage_2_9, R.drawable.collage_2_10, R.drawable.collage_2_11, R.drawable.collage_2_12, R.drawable.collage_2_13, R.drawable.collage_2_14, R.drawable.collage_2_15};
        r62[2] = ai1;
        r62[3] = new int[]{R.drawable.collage_4_0, R.drawable.collage_4_1, R.drawable.collage_4_2, R.drawable.collage_4_3, R.drawable.collage_4_4, R.drawable.collage_4_5, R.drawable.collage_4_6, R.drawable.collage_4_7, R.drawable.collage_4_8, R.drawable.collage_4_9, R.drawable.collage_4_10, R.drawable.collage_4_11, R.drawable.collage_4_12, R.drawable.collage_4_13, R.drawable.collage_4_14, R.drawable.collage_4_15, R.drawable.collage_4_16, R.drawable.collage_4_17};
        r62[4] = ai2;
        r62[5] = new int[]{R.drawable.collage_6_0, R.drawable.collage_6_1, R.drawable.collage_6_2, R.drawable.collage_6_3, R.drawable.collage_6_4, R.drawable.collage_6_5, R.drawable.collage_6_6, R.drawable.collage_6_7, R.drawable.collage_6_8, R.drawable.collage_6_9, R.drawable.collage_6_10, R.drawable.collage_6_11, R.drawable.collage_6_12};
        r62[6] = ai3;
        r62[7] = ai4;
        r62[8] = new int[]{R.drawable.collage_9_0, R.drawable.collage_9_1, R.drawable.collage_9_2, R.drawable.collage_9_3, R.drawable.collage_9_4, R.drawable.collage_9_5, R.drawable.collage_9_6, R.drawable.collage_9_7, R.drawable.collage_9_8};
        collageIconArray = r62;
    }

    public static Collage CreateCollage(int i, int j, int k, boolean flag) {
        if (flag) {
            return createScrapLaout(i, j, k);
        }
        if (i == 1) {
            return new Collage1(j, k);
        }
        if (i == 2) {
            return new Collage2(j, k);
        }
        if (i == 3) {
            return new Collage3(j, k);
        }
        if (i == 4) {
            return new Collage4(j, k);
        }
        if (i == 5) {
            return new Collage5(j, k);
        }
        if (i == 6) {
            return new Collage6(j, k);
        }
        if (i == 7) {
            return new Collage7(j, k);
        }
        if (i == 8) {
            return new Collage8(j, k);
        }
        if (i == 9) {
            return new Collage9(j, k);
        }
        if (i == 10) {
            return new Collage10(j, k);
        }
        return null;
    }

    public static Collage createScrapLaout(int i, int j, int k) {
        Collage collage = new Collage();
        collage.collageLayoutList = new ArrayList();
        CollageScrapBook collagescrapbook = new CollageScrapBook(j, k);
        if (i == 1) {
            collage.collageLayoutList.add(collagescrapbook.collageLayoutList.get(0));
            scrapBookShapeScale = 0.7f;
        }
        if (i == 2) {
            if (k > j) {
                collage.collageLayoutList.add(collagescrapbook.collageLayoutList.get(2));
            } else {
                collage.collageLayoutList.add(collagescrapbook.collageLayoutList.get(1));
            }
            scrapBookShapeScale = 1.0f;
        } else if (i == 3) {
            collage.collageLayoutList.add(collagescrapbook.collageLayoutList.get(3));
            scrapBookShapeScale = 0.95f;
        } else if (i == 4) {
            collage.collageLayoutList.add(collagescrapbook.collageLayoutList.get(4));
            scrapBookShapeScale = 1.0f;
        } else if (i == 5) {
            collage.collageLayoutList.add(collagescrapbook.collageLayoutList.get(5));
            scrapBookShapeScale = 0.95f;
        } else if (i == 6) {
            if (k > j) {
                collage.collageLayoutList.add(collagescrapbook.collageLayoutList.get(6));
            } else {
                collage.collageLayoutList.add(collagescrapbook.collageLayoutList.get(7));
            }
            scrapBookShapeScale = 1.0f;
        } else if (i == 7) {
            if (k > j) {
                collage.collageLayoutList.add(collagescrapbook.collageLayoutList.get(8));
            } else {
                collage.collageLayoutList.add(collagescrapbook.collageLayoutList.get(9));
            }
            scrapBookShapeScale = 1.0f;
        } else if (i == 8) {
            collage.collageLayoutList.add(collagescrapbook.collageLayoutList.get(10));
            scrapBookShapeScale = 1.0f;
        } else if (i == 9) {
            collage.collageLayoutList.add(collagescrapbook.collageLayoutList.get(11));
            scrapBookShapeScale = 1.0f;
        } else if (i == 10) {
            collage.collageLayoutList.add(collagescrapbook.collageLayoutList.get(12));
            scrapBookShapeScale = 1.0f;
        }
        return collage;
    }
}
