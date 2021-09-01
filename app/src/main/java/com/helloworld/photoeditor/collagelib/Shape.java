package com.helloworld.photoeditor.collagelib;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Path.FillType;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Xfermode;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;

import com.helloworld.photoeditor.pointlist.Collage;

public class Shape {
    public static final int MATRIX_MODE_CENTER = 1;
    public static final int MATRIX_MODE_FIT = 0;
    public static final int MATRIX_MODE_FLIP_HORIZONTAL = 4;
    public static final int MATRIX_MODE_FLIP_VERTICAL = 5;
    public static final int MATRIX_MODE_MOVE_DOWN = 13;
    public static final int MATRIX_MODE_MOVE_LEFT = 10;
    public static final int MATRIX_MODE_MOVE_RIGHT = 11;
    public static final int MATRIX_MODE_MOVE_UP = 12;
    public static final int MATRIX_MODE_ROTATE_LEFT = 3;
    public static final int MATRIX_MODE_ROTATE_NEGATIVE = 6;
    public static final int MATRIX_MODE_ROTATE_POSITIVE = 7;
    public static final int MATRIX_MODE_ROTATE_RIGHT = 2;
    public static final int MATRIX_MODE_ZOOM_IN = 8;
    public static final int MATRIX_MODE_ZOOM_OUT = 9;
    public static final int MESSAGE_DEFAULT = 0;
    public static final int MESSAGE_MAX_BOTTOM = 6;
    public static final int MESSAGE_MAX_LEFT = 3;
    public static final int MESSAGE_MAX_RIGHT = 4;
    public static final int MESSAGE_MAX_TOP = 5;
    public static final int MESSAGE_MAX_ZOOM = 1;
    public static final int MESSAGE_MIN_ZOOM = 2;
    private static final String TAG = "Shape";
    static final int[] scrapBookRotation = new int[]{MATRIX_MODE_MOVE_DOWN, -13, -7, -12, MATRIX_MODE_MOVE_RIGHT, MATRIX_MODE_ZOOM_IN, -9, MATRIX_MODE_MOVE_LEFT, MATRIX_MODE_ZOOM_OUT};
    public final int SHAPE_MODE_MASK;
    public final int SHAPE_MODE_POINT;
    public final int SHAPE_MODE_RECT;
    private Bitmap bitmap;
    int bitmapHeight;
    Matrix bitmapMatrix;
    RectF bitmapRect;
    int bitmapWidth;
    Paint borderPaint;
    int borderStrokeWidth;
    RectF bounds;
    Bitmap btmDelete;
    Bitmap btmScale;
    PointF centerOriginal;
    Paint dashPaint;
    Path dashPathHorizontal;
    Path dashPathVertical;
    int delW;
    float deleteWidthHalf;
    float dx;
    float dy;
    int[] exceptionIndex;
    float[] f506f;
    float[] f507p;
    RectF f508r;
    Paint iconMaskPaint;
    Paint iconPaint;
    Xfermode iconXferMode;
    Matrix inverse;
    boolean isScrapBook;
    private Bitmap maskBitmap;
    private Matrix maskMatrix;
    Paint maskPaint;
    float maxScale;
    float minScale;
    NinePatchDrawable npd;
    int npdPadding;
    int offsetX;
    int offsetY;
    RectF originalBounds;
    Path originalPath;
    private Paint paintPath;
    Paint paintScrap;
    private Paint paintTransparent;
    Paint paintXferMode;
    Path path;
    Path[] pathList;
    int pathListLength;
    Matrix pathMatrix;
    PointF[] points;
    float[] pts;
    private String r0;
    Region region;
    Matrix removeBitmapMatrix;
    Matrix scaleBitmapMatrix;
    float scaleDown;
    float scaleUp;
    float scrapBookPadding;
    int screenWidth;
    int shapeMode;
    RectF sourceRect;
    final float tempRadius;
    RectF tempRect;
    final float tempScrapBookPadding;
    float tempTouchStrokeWidth;
    Paint touchPaint;
    RectF touchRect;
    float touchStrokeWidth;
    Matrix transparentMaskMatrix;
    float[] values;

    public Shape(PointF[] points, Bitmap b, int[] exceptionIndex, int offsetX, int offsetY, boolean isScrapBook, int index, boolean isDelete, Bitmap del, Bitmap scl, int screenWidth) {
        this.offsetY = MESSAGE_DEFAULT;
        this.offsetX = MESSAGE_DEFAULT;
        this.SHAPE_MODE_POINT = MESSAGE_MAX_ZOOM;
        this.SHAPE_MODE_RECT = MESSAGE_MIN_ZOOM;
        this.SHAPE_MODE_MASK = MESSAGE_MAX_LEFT;
        this.maskBitmap = null;
        this.maskMatrix = new Matrix();
        this.transparentMaskMatrix = new Matrix();
        this.tempRect = new RectF();
        this.f508r = new RectF();
        this.minScale = 1.0f;
        this.maxScale = 1.0f;
        this.bitmapRect = new RectF();
        this.f507p = new float[MESSAGE_MIN_ZOOM];
        this.dx = 0.0f;
        this.dy = 0.0f;
        this.scaleDown = 0.95f;
        this.scaleUp = 1.05f;
        this.f506f = new float[MESSAGE_MIN_ZOOM];
        this.centerOriginal = new PointF();
        this.touchPaint = new Paint(MESSAGE_MAX_ZOOM);
        this.borderPaint = new Paint(MESSAGE_MAX_ZOOM);
        this.paintScrap = new Paint(MESSAGE_MIN_ZOOM);
        this.pts = new float[MESSAGE_MIN_ZOOM];
        this.inverse = new Matrix();
        this.tempScrapBookPadding = 25.0f;
        this.scrapBookPadding = 25.0f;
        this.tempTouchStrokeWidth = 8.0f;
        this.touchStrokeWidth = this.tempTouchStrokeWidth;
        this.values = new float[MATRIX_MODE_ZOOM_OUT];
        this.tempRadius = 60.0f;
        this.borderStrokeWidth = MESSAGE_MAX_BOTTOM;
        this.dashPaint = new Paint();
        this.delW = MESSAGE_DEFAULT;
        this.deleteWidthHalf = 0.0f;
        this.npdPadding = 16;
        this.points = points;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.btmDelete = del;
        this.btmScale = scl;
        this.screenWidth = screenWidth;
        this.isScrapBook = isScrapBook;
        createPathFromPoints();
        this.path.offset((float) offsetX, (float) offsetY);
        this.exceptionIndex = exceptionIndex;
        this.bitmap = b;
        this.bitmapWidth = this.bitmap.getWidth();
        this.bitmapHeight = this.bitmap.getHeight();
        this.shapeMode = MESSAGE_MAX_ZOOM;
        init(isScrapBook, index, false, MESSAGE_DEFAULT, MESSAGE_DEFAULT);
    }

    public Shape(PointF[] points, Bitmap b, int[] exceptionIndex, int offsetX, int offsetY, Bitmap mask, boolean isScrapBook, int index, boolean isDelete, Bitmap del, Bitmap scl, int screenWidth) {
        this.offsetY = MESSAGE_DEFAULT;
        this.offsetX = MESSAGE_DEFAULT;
        this.SHAPE_MODE_POINT = MESSAGE_MAX_ZOOM;
        this.SHAPE_MODE_RECT = MESSAGE_MIN_ZOOM;
        this.SHAPE_MODE_MASK = MESSAGE_MAX_LEFT;
        this.maskBitmap = null;
        this.maskMatrix = new Matrix();
        this.transparentMaskMatrix = new Matrix();
        this.tempRect = new RectF();
        this.f508r = new RectF();
        this.minScale = 1.0f;
        this.maxScale = 1.0f;
        this.bitmapRect = new RectF();
        this.f507p = new float[MESSAGE_MIN_ZOOM];
        this.dx = 0.0f;
        this.dy = 0.0f;
        this.scaleDown = 0.95f;
        this.scaleUp = 1.05f;
        this.f506f = new float[MESSAGE_MIN_ZOOM];
        this.centerOriginal = new PointF();
        this.touchPaint = new Paint(MESSAGE_MAX_ZOOM);
        this.borderPaint = new Paint(MESSAGE_MAX_ZOOM);
        this.paintScrap = new Paint(MESSAGE_MIN_ZOOM);
        this.pts = new float[MESSAGE_MIN_ZOOM];
        this.inverse = new Matrix();
        this.tempScrapBookPadding = 25.0f;
        this.scrapBookPadding = 25.0f;
        this.tempTouchStrokeWidth = 8.0f;
        this.touchStrokeWidth = this.tempTouchStrokeWidth;
        this.values = new float[MATRIX_MODE_ZOOM_OUT];
        this.tempRadius = 60.0f;
        this.borderStrokeWidth = MESSAGE_MAX_BOTTOM;
        this.dashPaint = new Paint();
        this.delW = MESSAGE_DEFAULT;
        this.deleteWidthHalf = 0.0f;
        this.npdPadding = 16;
        this.maskBitmap = mask;
        this.points = points;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.btmDelete = del;
        this.btmScale = scl;
        this.screenWidth = screenWidth;
        this.isScrapBook = isScrapBook;
        createPathFromPoints();
        this.path.offset((float) offsetX, (float) offsetY);
        this.exceptionIndex = exceptionIndex;
        this.bitmap = b;
        this.bitmapWidth = this.bitmap.getWidth();
        this.bitmapHeight = this.bitmap.getHeight();
        this.shapeMode = MESSAGE_MAX_LEFT;
        init(isScrapBook, index, false, MESSAGE_DEFAULT, MESSAGE_DEFAULT);
    }

    public void changeRatio(PointF[] points, int[] exceptionIndex, int offsetX, int offsetY, boolean isScrapBook, int index, int w, int h) {
        this.points = points;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        createPathFromPoints();
        this.path.offset((float) offsetX, (float) offsetY);
        this.exceptionIndex = exceptionIndex;
        init(isScrapBook, index, true, w, h);
    }

    public void freeBitmaps() {
        if (!(this.bitmap == null || this.bitmap.isRecycled())) {
            this.bitmap.recycle();
        }
        if (this.maskBitmap != null && !this.maskBitmap.isRecycled()) {
            this.maskBitmap = null;
        }
    }

    public void setRadius(CornerPathEffect corEffect) {
        this.paintPath.setPathEffect(corEffect);
        this.paintTransparent.setPathEffect(corEffect);
    }

    public float smallestDistance() {
        float smallestDistance = 1500.0f;
        for (int i = MESSAGE_DEFAULT; i < this.points.length; i += MESSAGE_MAX_ZOOM) {
            for (int j = MESSAGE_DEFAULT; j < this.points.length; j += MESSAGE_MAX_ZOOM) {
                if (i != j) {
                    float distance = Math.abs(this.points[i].x - this.points[j].x) + Math.abs(this.points[i].y - this.points[j].y);
                    if (distance < smallestDistance) {
                        smallestDistance = distance;
                    }
                }
            }
        }
        return smallestDistance;
    }

    public void init(boolean isScrapBook, int index, boolean isChangeRatio, int w, int h) {
        this.bounds = new RectF();
        this.originalPath = new Path(this.path);
        this.path.computeBounds(this.bounds, true);
        this.originalBounds = new RectF(this.bounds);
        this.paintXferMode = new Paint(MESSAGE_MAX_ZOOM);
        this.paintXferMode.setFilterBitmap(true);
        this.paintXferMode.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        this.paintPath = new Paint(MESSAGE_MAX_ZOOM);
        this.paintPath.setFilterBitmap(true);
        this.maskPaint = new Paint(MESSAGE_MAX_ZOOM);
        this.maskPaint.setFilterBitmap(true);
        this.paintTransparent = new Paint(MESSAGE_MAX_ZOOM);
        this.paintTransparent.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        this.paintTransparent.setFilterBitmap(true);
        if (isScrapBook) {
            setScrapBookBitmapPosition(index, isChangeRatio, w, h);
        } else {
            setBitmapPosition();
        }
        this.paintPath.setPathEffect(new CornerPathEffect(3.0f));
        this.pathMatrix = new Matrix();
        this.region = new Region();
        this.region.setPath(this.path, new Region((int) this.bounds.left, (int) this.bounds.top, (int) this.bounds.right, (int) this.bounds.bottom));
        if (isScrapBook) {
            this.dashPaint.setColor(7829368);
            this.dashPaint.setStyle(Style.STROKE);
            float strokeW = ((float) this.screenWidth) / 120.0f;
            if (strokeW <= 0.0f) {
                strokeW = 5.0f;
            }
            this.dashPaint.setStrokeWidth(strokeW);
            Paint paint = this.dashPaint;
            float[] fArr = new float[MESSAGE_MIN_ZOOM];
            fArr[MESSAGE_DEFAULT] = strokeW;
            fArr[MESSAGE_MAX_ZOOM] = strokeW;
            paint.setPathEffect(new DashPathEffect(fArr, 0.0f));
            this.dashPathVertical = new Path();
            this.dashPathVertical.moveTo((float) (this.bitmapWidth / MESSAGE_MIN_ZOOM), (float) ((-this.bitmapHeight) / MESSAGE_MAX_TOP));
            this.dashPathVertical.lineTo((float) (this.bitmapWidth / MESSAGE_MIN_ZOOM), (float) ((this.bitmapHeight * MESSAGE_MAX_BOTTOM) / MESSAGE_MAX_TOP));
            this.dashPathHorizontal = new Path();
            this.dashPathHorizontal.moveTo((float) ((-this.bitmapWidth) / MESSAGE_MAX_TOP), (float) (this.bitmapHeight / MESSAGE_MIN_ZOOM));
            this.dashPathHorizontal.lineTo((float) ((this.bitmapWidth * MESSAGE_MAX_BOTTOM) / MESSAGE_MAX_TOP), (float) (this.bitmapHeight / MESSAGE_MIN_ZOOM));
        }
    }

    public void setBitmap(Bitmap bitmap, boolean isFilter) {
        this.bitmap = bitmap;
        this.bitmapWidth = bitmap.getWidth();
        this.bitmapHeight = bitmap.getHeight();
        if (!isFilter) {
            setBitmapPosition();
        }
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public Bitmap getMaskBitmap() {
        return this.maskBitmap;
    }

    private void setBitmapPosition() {
        float scaleBitmap = getBitmapScale();
        float bitmapY = this.bounds.top - (((((float) this.bitmapHeight) * scaleBitmap) - this.bounds.height()) / 2.0f);
        float bitmapX = this.bounds.left - (((((float) this.bitmapWidth) * scaleBitmap) - this.bounds.width()) / 2.0f);
        this.bitmapMatrix = new Matrix();
        this.bitmapMatrix.reset();
        this.bitmapMatrix.postScale(scaleBitmap, scaleBitmap);
        this.bitmapMatrix.postTranslate(bitmapX, bitmapY);
        if (this.shapeMode == MESSAGE_MAX_LEFT) {
            setMaskBitmapPositions();
        }
        setMaxMinScales(scaleBitmap);
    }

    private float getBitmapScale() {
        float scaleBitmapX = this.bounds.width() / ((float) this.bitmapWidth);
        float scaleBitmapY = this.bounds.height() / ((float) this.bitmapHeight);
        return scaleBitmapX < scaleBitmapY ? scaleBitmapY : scaleBitmapX;
    }

    void setMaxMinScales(float scaleBitmap) {
        if (this.isScrapBook) {
            this.minScale = scaleBitmap / 2.0f;
        } else {
            this.minScale = scaleBitmap;
        }
        if (this.isScrapBook) {
            this.maxScale = scaleBitmap * 2.0f;
        } else {
            this.maxScale = 4.0f * scaleBitmap;
        }
    }

    void setMinScales(float scaleBitmap) {
        if (this.isScrapBook) {
            this.minScale = scaleBitmap / 2.0f;
        } else {
            this.minScale = scaleBitmap;
        }
    }

    private void setMaskBitmapPositions() {
        if (this.maskBitmap != null) {
            float scaleMaskBitmap;
            float scaleMaskBitmapTr;
            int maskBitmapWidth = this.maskBitmap.getWidth();
            int maskBitmapHeight = this.maskBitmap.getHeight();
            float scaleMaskBitmapX = this.bounds.width() / ((float) maskBitmapWidth);
            float scaleMaskBitmapY = this.bounds.height() / ((float) maskBitmapHeight);
            if (scaleMaskBitmapX > scaleMaskBitmapY) {
                scaleMaskBitmap = scaleMaskBitmapY;
            } else {
                scaleMaskBitmap = scaleMaskBitmapX;
            }
            float maskBitmapY = this.bounds.top - (((((float) maskBitmapHeight) * scaleMaskBitmap) - this.bounds.height()) / 2.0f);
            float maskBitmapX = this.bounds.left - (((((float) maskBitmapWidth) * scaleMaskBitmap) - this.bounds.width()) / 2.0f);
            this.maskMatrix = new Matrix();
            this.maskMatrix.reset();
            this.maskMatrix.postScale(scaleMaskBitmap, scaleMaskBitmap);
            this.maskMatrix.postTranslate(maskBitmapX, maskBitmapY);
            float scaleMaskBitmapXTr = this.originalBounds.width() / ((float) maskBitmapWidth);
            float scaleMaskBitmapYTr = this.originalBounds.height() / ((float) maskBitmapHeight);
            if (scaleMaskBitmapXTr > scaleMaskBitmapYTr) {
                scaleMaskBitmapTr = scaleMaskBitmapYTr;
            } else {
                scaleMaskBitmapTr = scaleMaskBitmapXTr;
            }
            float maskBitmapYTr = this.originalBounds.top - (((((float) maskBitmapHeight) * scaleMaskBitmapTr) - this.originalBounds.height()) / 2.0f);
            float maskBitmapXTr = this.originalBounds.left - (((((float) maskBitmapWidth) * scaleMaskBitmapTr) - this.originalBounds.width()) / 2.0f);
            this.transparentMaskMatrix = new Matrix();
            this.transparentMaskMatrix.reset();
            this.transparentMaskMatrix.postScale(scaleMaskBitmapTr, scaleMaskBitmapTr);
            this.transparentMaskMatrix.postTranslate(maskBitmapXTr, maskBitmapYTr);
        }
    }

    public void scalePath(float distance, float width, float height) {
        if (this.shapeMode == MESSAGE_MAX_ZOOM) {
            pathTransform(this.points, this.path, distance, this.originalBounds.centerX(), this.originalBounds.centerY());
        } else if (this.shapeMode == MESSAGE_MIN_ZOOM) {
            pathTransformFromRect(distance);
        } else {
            float scaleX = (width - (2.0f * distance)) / width;
            float scaleY = (height - (2.0f * distance)) / height;
            this.pathMatrix.reset();
            this.pathMatrix.setScale(scaleX, scaleY, this.originalBounds.centerX(), this.originalBounds.centerY());
            this.originalPath.transform(this.pathMatrix, this.path);
        }
        this.path.computeBounds(this.bounds, true);
        if (this.shapeMode == MESSAGE_MAX_LEFT) {
            setMaskBitmapPositions();
        }
    }

    void createPathFromPoints() {
        this.path = new Path();
        this.path.setFillType(FillType.EVEN_ODD);
        this.path.moveTo(this.points[MESSAGE_DEFAULT].x, this.points[MESSAGE_DEFAULT].y);
        for (int i = MESSAGE_MAX_ZOOM; i < this.points.length; i += MESSAGE_MAX_ZOOM) {
            this.path.lineTo(this.points[i].x, this.points[i].y);
        }
        this.path.lineTo(this.points[MESSAGE_DEFAULT].x, this.points[MESSAGE_DEFAULT].y);
        this.path.close();
    }

    void createPathFromRect() {
        this.path = new Path();
        this.path.addRect(this.sourceRect, Direction.CCW);
    }

    void pathTransform(PointF[] points, Path path, float distance, float centerX, float centerY) {
        int i;
        centerX -= (float) this.offsetX;
        centerY -= (float) this.offsetY;
        path.rewind();
        path.setFillType(FillType.EVEN_ODD);
        int size = points.length;
        float[] distanceArray = new float[size];
        for (i = MESSAGE_DEFAULT; i < size; i += MESSAGE_MAX_ZOOM) {
            distanceArray[i] = distance;
        }
        if (this.exceptionIndex != null) {
            for (i = MESSAGE_DEFAULT; i < this.exceptionIndex.length; i += MESSAGE_MAX_ZOOM) {
                distanceArray[this.exceptionIndex[i]] = 2.0f * distance;
            }
        }
        path.moveTo(checkRange(points[MESSAGE_DEFAULT].x, distanceArray[MESSAGE_DEFAULT], centerX), checkRange(points[MESSAGE_DEFAULT].y, distance, centerY));
        for (i = MESSAGE_MAX_ZOOM; i < size; i += MESSAGE_MAX_ZOOM) {
            path.lineTo(checkRange(points[i].x, distanceArray[i], centerX), checkRange(points[i].y, distance, centerY));
        }
        path.lineTo(checkRange(points[MESSAGE_DEFAULT].x, distanceArray[MESSAGE_DEFAULT], centerX), checkRange(points[MESSAGE_DEFAULT].y, distance, centerY));
        path.close();
        path.offset((float) this.offsetX, (float) this.offsetY);
    }

    void pathTransformFromRect(float distance) {
        this.tempRect.set(this.sourceRect.left + distance, this.sourceRect.top + distance, this.sourceRect.right - distance, this.sourceRect.bottom - distance);
        this.path.rewind();
        this.path.addRect(this.tempRect, Direction.CCW);
    }

    float checkRange(float pointA, float distance, float centerA) {
        if (pointA > centerA) {
            return pointA - distance;
        }
        if (pointA < centerA) {
            return pointA + distance;
        }
        return pointA;
    }

    public void drawShape(Canvas canvas, int width, int height, int j, boolean drawPorterClear) {
        if (drawPorterClear) {
            if (this.shapeMode != MESSAGE_MAX_LEFT) {
                canvas.drawPath(this.originalPath, this.paintTransparent);
            } else if (!(this.maskBitmap == null || this.maskBitmap.isRecycled())) {
                canvas.drawBitmap(this.maskBitmap, this.transparentMaskMatrix, this.paintTransparent);
            }
            canvas.restoreToCount(j);
        }
        this.f508r.set(0.0f, 0.0f, (float) this.bitmapWidth, (float) this.bitmapHeight);
        this.bitmapMatrix.mapRect(this.f508r);
        int k = canvas.saveLayer(this.f508r, null, 31);
        if (this.shapeMode != MESSAGE_MAX_LEFT) {
            canvas.drawPath(this.path, this.paintPath);
        } else if (!(this.maskBitmap == null || this.maskBitmap.isRecycled())) {
            canvas.drawBitmap(this.maskBitmap, this.maskMatrix, this.maskPaint);
        }
        canvas.drawBitmap(this.bitmap, this.bitmapMatrix, this.paintXferMode);
        canvas.restoreToCount(k);
    }

    public void drawShapeForSave(Canvas canvas, int width, int height, int j, boolean drawPorterClear) {
        if (drawPorterClear) {
            if (this.shapeMode != MESSAGE_MAX_LEFT) {
                canvas.drawPath(this.originalPath, this.paintTransparent);
            } else if (!(this.maskBitmap == null || this.maskBitmap.isRecycled())) {
                canvas.drawBitmap(this.maskBitmap, this.transparentMaskMatrix, this.paintTransparent);
            }
            canvas.restoreToCount(j);
        }
        RectF r = new RectF(0.0f, 0.0f, (float) (this.bitmapWidth + MESSAGE_DEFAULT), (float) (this.bitmapHeight + MESSAGE_DEFAULT));
        this.bitmapMatrix.mapRect(r);
        int k = canvas.saveLayer(r, null, 31);
        if (this.shapeMode != MESSAGE_MAX_LEFT) {
            canvas.drawPath(this.path, this.paintPath);
        } else if (!(this.maskBitmap == null || this.maskBitmap.isRecycled())) {
            canvas.drawBitmap(this.maskBitmap, this.maskMatrix, this.maskPaint);
        }
        canvas.drawBitmap(this.bitmap, this.bitmapMatrix, this.paintXferMode);
        canvas.restoreToCount(k);
    }

    public void initIcon(int width, int height) {
        this.iconPaint = new Paint(MESSAGE_MAX_ZOOM);
        this.iconPaint.setFilterBitmap(true);
        this.iconPaint.setColor(7829368);
        this.paintXferMode.setColor(7829368);
        scalePath(5.0f, (float) width, (float) height);
        this.iconMaskPaint = new Paint(MESSAGE_MAX_ZOOM);
        this.iconMaskPaint.setFilterBitmap(true);
        this.iconMaskPaint.setColor(7829368);
        this.iconXferMode = new PorterDuffXfermode(Mode.SRC_IN);
        this.iconMaskPaint.setXfermode(this.iconXferMode);
    }

    void drawShapeIcon(Canvas canvas, int width, int height, int j, boolean drawPorterClear) {
        setMaskBitmapPositions();
        this.path.offset((float) (-this.offsetX), (float) (-this.offsetY));
        this.originalPath.offset((float) (-this.offsetX), (float) (-this.offsetY));
        this.maskMatrix.postTranslate((float) (-this.offsetX), (float) (-this.offsetY));
        this.transparentMaskMatrix.postTranslate((float) (-this.offsetX), (float) (-this.offsetY));
        if (drawPorterClear) {
            if (this.shapeMode == MESSAGE_MAX_LEFT) {
                canvas.drawBitmap(this.maskBitmap, this.transparentMaskMatrix, this.paintTransparent);
            } else {
                canvas.drawPath(this.originalPath, this.paintTransparent);
            }
            canvas.restoreToCount(j);
        }
        if (this.shapeMode == MESSAGE_MAX_LEFT) {
            int i = canvas.saveLayer(0.0f, 0.0f, (float) width, (float) height, null, 31);
            canvas.drawBitmap(this.maskBitmap, this.maskMatrix, this.iconPaint);
            canvas.drawBitmap(this.maskBitmap, this.maskMatrix, this.iconMaskPaint);
            canvas.restoreToCount(i);
            return;
        }
        canvas.drawPath(this.path, this.iconPaint);
    }

    void drawShapeIcon2(Canvas canvas, int width, int height) {
        this.path.offset((float) (-this.offsetX), (float) (-this.offsetY));
        this.originalPath.offset((float) (-this.offsetX), (float) (-this.offsetY));
        this.maskMatrix.postTranslate((float) (-this.offsetX), (float) (-this.offsetY));
        this.transparentMaskMatrix.postTranslate((float) (-this.offsetX), (float) (-this.offsetY));
        Paint p2 = new Paint();
        if (this.shapeMode == MESSAGE_MAX_LEFT) {
            int i = canvas.saveLayer(0.0f, 0.0f, (float) width, (float) height, null, 31);
            canvas.drawBitmap(this.maskBitmap, this.transparentMaskMatrix, p2);
            p2.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawRect(0.0f, 0.0f, (float) width, (float) height, p2);
            p2.setXfermode(null);
            canvas.restoreToCount(i);
            return;
        }
        canvas.drawPath(this.path, this.iconPaint);
    }

    void bitmapMatrixScale(float scaleX, float scaleY, float centerX, float centerY) {
        this.bitmapMatrix.postScale(scaleX, scaleY, centerX, centerY);
        checkScaleBoundries();
    }

    void bitmapMatrixScaleScrapBook(float scaleX, float scaleY) {
        this.f507p[MESSAGE_DEFAULT] = (float) (this.bitmapWidth / MESSAGE_MIN_ZOOM);
        this.f507p[MESSAGE_MAX_ZOOM] = (float) (this.bitmapHeight / MESSAGE_MIN_ZOOM);
        this.bitmapMatrix.mapPoints(this.f507p);
        this.bitmapMatrix.postScale(scaleX, scaleY, this.f507p[MESSAGE_DEFAULT], this.f507p[MESSAGE_MAX_ZOOM]);
        checkScaleBoundries();
    }

    void checkScaleBoundries() {
        float scale = getScale();
        if (scale < this.minScale) {
            this.bitmapMatrix.postScale(this.minScale / scale, this.minScale / scale, this.f507p[MESSAGE_DEFAULT], this.f507p[MESSAGE_MAX_ZOOM]);
        }
        if (scale > this.maxScale) {
            this.bitmapMatrix.postScale(this.maxScale / scale, this.maxScale / scale, this.f507p[MESSAGE_DEFAULT], this.f507p[MESSAGE_MAX_ZOOM]);
        }
    }

    void bitmapMatrixTranslate(float dx, float dy) {
        this.bitmapMatrix.postTranslate(dx, dy);
        if (!this.isScrapBook) {
            checkBoundries();
        }
    }

    public void checkBoundries() {
        this.bitmapRect.set(0.0f, 0.0f, (float) this.bitmapWidth, (float) this.bitmapHeight);
        this.bitmapMatrix.mapRect(this.bitmapRect);
        float dx = 0.0f;
        float dy = 0.0f;
        if (this.bitmapRect.left > this.bounds.left) {
            dx = this.bounds.left - this.bitmapRect.left;
        }
        if (this.bitmapRect.top > this.bounds.top) {
            dy = this.bounds.top - this.bitmapRect.top;
        }
        if (this.bitmapRect.right < this.bounds.right) {
            dx = this.bounds.right - this.bitmapRect.right;
        }
        if (this.bitmapRect.bottom < this.bounds.bottom) {
            dy = this.bounds.bottom - this.bitmapRect.bottom;
        }
        this.bitmapMatrix.postTranslate(dx, dy);
    }

    public void checkScaleBounds() {
        setMinScales(getBitmapScale());
        checkScaleBoundries();
    }

    void bitmapMatrixgGetValues(float[] values) {
        this.bitmapMatrix.getValues(values);
    }

    void bitmapMatrixRotate(float angle) {
        this.f507p[MESSAGE_DEFAULT] = (float) (this.bitmapWidth / MESSAGE_MIN_ZOOM);
        this.f507p[MESSAGE_MAX_ZOOM] = (float) (this.bitmapHeight / MESSAGE_MIN_ZOOM);
        this.bitmapMatrix.mapPoints(this.f507p);
        this.bitmapMatrix.postRotate(angle, this.f507p[MESSAGE_DEFAULT], this.f507p[MESSAGE_MAX_ZOOM]);
    }

    public int setScaleMatrix(int mode) {
        if (this.dx <= 0.5f) {
            this.dx = ((float) this.bitmapWidth) / 100.0f;
        }
        if (this.dy <= 0.5f) {
            this.dy = ((float) this.bitmapHeight) / 100.0f;
        }
        PointF centerOfImage = getCenterOfImage();
        if (mode == 0) {
            setMatrixFit();
        } else if (mode == MESSAGE_MAX_ZOOM) {
            setBitmapPosition();
        } else if (mode == MESSAGE_MAX_LEFT) {
            this.bitmapMatrix.postRotate(-90.0f, centerOfImage.x, centerOfImage.y);
        } else if (mode == MESSAGE_MIN_ZOOM) {
            this.bitmapMatrix.postRotate(90.0f, centerOfImage.x, centerOfImage.y);
        } else if (mode == MESSAGE_MAX_RIGHT) {
            this.bitmapMatrix.postScale(-1.0f, 1.0f, centerOfImage.x, centerOfImage.y);
        } else if (mode == MESSAGE_MAX_TOP) {
            this.bitmapMatrix.postScale(1.0f, -1.0f, centerOfImage.x, centerOfImage.y);
        } else if (mode == MESSAGE_MAX_BOTTOM) {
            this.bitmapMatrix.postRotate(-10.0f, centerOfImage.x, centerOfImage.y);
        } else if (mode == MATRIX_MODE_ROTATE_POSITIVE) {
            this.bitmapMatrix.postRotate(10.0f, centerOfImage.x, centerOfImage.y);
        } else if (mode == MATRIX_MODE_ZOOM_IN) {
            if (getScale() >= this.maxScale) {
                return MESSAGE_MAX_ZOOM;
            }
            this.bitmapMatrix.postScale(this.scaleUp, this.scaleUp, centerOfImage.x, centerOfImage.y);
        } else if (mode == MATRIX_MODE_ZOOM_OUT) {
            if (getScale() <= this.minScale) {
                return MESSAGE_MIN_ZOOM;
            }
            this.bitmapMatrix.postScale(this.scaleDown, this.scaleDown, centerOfImage.x, centerOfImage.y);
        } else if (mode == MATRIX_MODE_MOVE_LEFT) {
            this.bitmapRect.set(0.0f, 0.0f, (float) this.bitmapWidth, (float) this.bitmapHeight);
            this.bitmapMatrix.mapRect(this.bitmapRect);
            if (this.bitmapRect.right <= this.bounds.right && !this.isScrapBook) {
                return MESSAGE_MAX_LEFT;
            }
            this.bitmapMatrix.postTranslate(-this.dx, 0.0f);
        } else if (mode == MATRIX_MODE_MOVE_RIGHT) {
            this.bitmapRect.set(0.0f, 0.0f, (float) this.bitmapWidth, (float) this.bitmapHeight);
            this.bitmapMatrix.mapRect(this.bitmapRect);
            if (this.bitmapRect.left >= this.bounds.left && !this.isScrapBook) {
                return MESSAGE_MAX_RIGHT;
            }
            this.bitmapMatrix.postTranslate(this.dx, 0.0f);
        } else if (mode == MATRIX_MODE_MOVE_UP) {
            this.bitmapRect.set(0.0f, 0.0f, (float) this.bitmapWidth, (float) this.bitmapHeight);
            this.bitmapMatrix.mapRect(this.bitmapRect);
            if (this.bitmapRect.bottom <= this.bounds.bottom && !this.isScrapBook) {
                return MESSAGE_MAX_TOP;
            }
            this.bitmapMatrix.postTranslate(0.0f, -this.dy);
        } else if (mode == MATRIX_MODE_MOVE_DOWN) {
            this.bitmapRect.set(0.0f, 0.0f, (float) this.bitmapWidth, (float) this.bitmapHeight);
            this.bitmapMatrix.mapRect(this.bitmapRect);
            if (this.bitmapRect.top >= this.bounds.top && !this.isScrapBook) {
                return MESSAGE_MAX_BOTTOM;
            }
            this.bitmapMatrix.postTranslate(0.0f, this.dy);
        }
        checkScaleBoundries();
        if (!this.isScrapBook) {
            checkBoundries();
        }
        return MESSAGE_DEFAULT;
    }

    PointF getCenterOfImage() {
        if (this.centerOriginal == null) {
            this.centerOriginal = new PointF();
        }
        if (this.f506f == null) {
            this.f506f = new float[MESSAGE_MIN_ZOOM];
        }
        float y = ((float) this.bitmapHeight) / 2.0f;
        this.f506f[MESSAGE_DEFAULT] = ((float) this.bitmapWidth) / 2.0f;
        this.f506f[MESSAGE_MAX_ZOOM] = y;
        this.bitmapMatrix.mapPoints(this.f506f);
        this.centerOriginal.set(this.f506f[MESSAGE_DEFAULT], this.f506f[MESSAGE_MAX_ZOOM]);
        return this.centerOriginal;
    }

    void setMatrixFit() {
        float scaleBitmap = Math.min(this.bounds.width() / ((float) this.bitmapWidth), this.bounds.height() / ((float) this.bitmapHeight));
        if (this.isScrapBook) {
            scaleBitmap *= Collage.scrapBookShapeScale;
        }
        Log.e(TAG, "Collage.scrapBookShapeScale " + Collage.scrapBookShapeScale);
        float bitmapY = this.bounds.top + ((this.bounds.height() - (((float) this.bitmapHeight) * scaleBitmap)) / 2.0f);
        float bitmapX = this.bounds.left + ((this.bounds.width() - (((float) this.bitmapWidth) * scaleBitmap)) / 2.0f);
        this.bitmapMatrix.reset();
        this.bitmapMatrix.postScale(scaleBitmap, scaleBitmap);
        this.bitmapMatrix.postTranslate(bitmapX, bitmapY);
    }

    private void setScrapBookBitmapPosition(int index, boolean isChangeRatio, int width, int height) {
        if (isChangeRatio) {
            int w = this.bitmapWidth;
            int h = this.bitmapHeight;
            float[] points = new float[MATRIX_MODE_ZOOM_IN];
            points[MESSAGE_DEFAULT] = 0.0f;
            points[MESSAGE_MAX_ZOOM] = 0.0f;
            points[MESSAGE_MIN_ZOOM] = (float) w;
            points[MESSAGE_MAX_LEFT] = 0.0f;
            points[MESSAGE_MAX_RIGHT] = (float) w;
            points[MESSAGE_MAX_TOP] = (float) h;
            points[MESSAGE_MAX_BOTTOM] = 0.0f;
            points[MATRIX_MODE_ROTATE_POSITIVE] = (float) h;
            this.bitmapMatrix.mapPoints(points);
            RectF drawArea = new RectF((float) this.offsetX, (float) this.offsetY, (float) (this.offsetX + width), (float) (this.offsetY + height));
            if (!drawArea.contains(points[MESSAGE_DEFAULT], points[MESSAGE_MAX_ZOOM])) {
                if (!drawArea.contains(points[MESSAGE_MIN_ZOOM], points[MESSAGE_MAX_LEFT])) {
                    if (!drawArea.contains(points[MESSAGE_MAX_RIGHT], points[MESSAGE_MAX_TOP])) {
                        if (!drawArea.contains(points[MESSAGE_MAX_BOTTOM], points[MATRIX_MODE_ROTATE_POSITIVE])) {
                            PointF A = new PointF((float) this.offsetX, (float) this.offsetY);
                            PointF B = new PointF((float) (this.offsetX + width), (float) this.offsetY);
                            PointF P = new PointF();
                            float[] f;
                            float min;
                            int minIndex;
                            int i;
                            if (points[MESSAGE_MAX_ZOOM] < ((float) this.offsetY)) {
                                P.set(points[MESSAGE_DEFAULT], points[MESSAGE_MAX_ZOOM]);
                                f = new float[MESSAGE_MAX_RIGHT];
                                f[MESSAGE_DEFAULT] = pointToLineDistance(A, B, P);
                                Log.e(TAG, "0  " + distToSegment(P, A, B));
                                P.set(points[MESSAGE_MIN_ZOOM], points[MESSAGE_MAX_LEFT]);
                                f[MESSAGE_MAX_ZOOM] = pointToLineDistance(A, B, P);
                                Log.e(TAG, "1  " + distToSegment(P, A, B));
                                P.set(points[MESSAGE_MAX_RIGHT], points[MESSAGE_MAX_TOP]);
                                f[MESSAGE_MIN_ZOOM] = pointToLineDistance(A, B, P);
                                Log.e(TAG, "2  " + distToSegment(P, A, B));
                                P.set(points[MESSAGE_MAX_BOTTOM], points[MATRIX_MODE_ROTATE_POSITIVE]);
                                f[MESSAGE_MAX_LEFT] = pointToLineDistance(A, B, P);
                                Log.e(TAG, "3  " + distToSegment(P, A, B));
                                min = f[MESSAGE_DEFAULT];
                                minIndex = MESSAGE_DEFAULT;
                                for (i = MESSAGE_MAX_ZOOM; i < MESSAGE_MAX_RIGHT; i += MESSAGE_MAX_ZOOM) {
                                    if (f[i] < min) {
                                        min = f[i];
                                        minIndex = i;
                                    }
                                    Log.e(TAG, "fi  " + f[i]);
                                }
                                this.bitmapMatrix.postTranslate(0.0f, ((float) (this.offsetY + 120)) - points[(minIndex * MESSAGE_MIN_ZOOM) + MESSAGE_MAX_ZOOM]);
                                return;
                            }
                            A = new PointF((float) this.offsetX, (float) (this.offsetY + height));
                            B = new PointF((float) (this.offsetX + width), (float) (this.offsetY + height));
                            P.set(points[MESSAGE_DEFAULT], points[MESSAGE_MAX_ZOOM]);
                            f = new float[MESSAGE_MAX_RIGHT];
                            f[MESSAGE_DEFAULT] = pointToLineDistance(A, B, P);
                            float f2 = A.x;
                            Log.e(TAG, "A  x " + this.r0 + " y " + A.y);
                            f2 = B.x;
                            Log.e(TAG, "B  x " + this.r0 + " y " + B.y);
                            Log.e(TAG, "0  " + distToSegment(P, A, B));
                            f2 = P.x;
                            Log.e(TAG, "0  x " + this.r0 + " y " + P.y);
                            P.set(points[MESSAGE_MIN_ZOOM], points[MESSAGE_MAX_LEFT]);
                            f[MESSAGE_MAX_ZOOM] = pointToLineDistance(A, B, P);
                            Log.e(TAG, "1  " + distToSegment(P, A, B));
                            f2 = P.x;
                            Log.e(TAG, "1  x " + this.r0 + " y " + P.y);
                            P.set(points[MESSAGE_MAX_RIGHT], points[MESSAGE_MAX_TOP]);
                            f[MESSAGE_MIN_ZOOM] = pointToLineDistance(A, B, P);
                            Log.e(TAG, "2  " + distToSegment(P, A, B));
                            f2 = P.x;
                            Log.e(TAG, "2  x " + this.r0 + " y " + P.y);
                            P.set(points[MESSAGE_MAX_BOTTOM], points[MATRIX_MODE_ROTATE_POSITIVE]);
                            f[MESSAGE_MAX_LEFT] = pointToLineDistance(A, B, P);
                            Log.e(TAG, "3  " + distToSegment(P, A, B));
                            f2 = P.x;
                            Log.e(TAG, "3  x " + this.r0 + " y " + P.y);
                            min = f[MESSAGE_DEFAULT];
                            minIndex = MESSAGE_DEFAULT;
                            Log.e(TAG, "bi  " + f[MESSAGE_DEFAULT]);
                            for (i = MESSAGE_MAX_ZOOM; i < MESSAGE_MAX_RIGHT; i += MESSAGE_MAX_ZOOM) {
                                if (f[i] < min) {
                                    min = f[i];
                                    minIndex = i;
                                }
                                Log.e(TAG, "bi  " + f[i]);
                            }
                            Log.e(TAG, "minIndex  " + minIndex);
                            Log.e(TAG, " points[minIndex*2+1] " + points[(minIndex * MESSAGE_MIN_ZOOM) + MESSAGE_MAX_ZOOM]);
                            Log.e(TAG, "translate " + (((float) ((this.offsetY + height) - 120)) - points[(minIndex * MESSAGE_MIN_ZOOM) + MESSAGE_MAX_ZOOM]));
                            this.bitmapMatrix.postTranslate(0.0f, ((float) ((this.offsetY + height) - 120)) - points[(minIndex * MESSAGE_MIN_ZOOM) + MESSAGE_MAX_ZOOM]);
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
        this.bitmapMatrix = new Matrix();
        setMatrixFit();
        float actualScale = getScale();
        setMaxMinScales(actualScale);
        float scale = 1.0f / actualScale;
        this.touchStrokeWidth = this.tempTouchStrokeWidth * scale;
        this.scrapBookPadding = 25.0f * scale;
        this.bitmapMatrix.postRotate((float) scrapBookRotation[index], this.bounds.left + (this.bounds.width() / 2.0f), this.bounds.top + (this.bounds.height() / 2.0f));
        this.touchRect = new RectF(-this.scrapBookPadding, -this.scrapBookPadding, ((float) this.bitmapWidth) + this.scrapBookPadding, ((float) this.bitmapHeight) + this.scrapBookPadding);
        this.touchPaint.setColor(1290417);
        this.touchPaint.setFilterBitmap(true);
        this.touchPaint.setStyle(Style.STROKE);
        this.touchPaint.setStrokeWidth(this.touchStrokeWidth);
        this.borderPaint.setColor(-1);
        this.borderPaint.setStyle(Style.STROKE);
        this.borderPaint.setStrokeWidth((float) this.borderStrokeWidth);
        this.borderPaint.setAntiAlias(true);
    }

    public float pointToLineDistance(PointF A, PointF B, PointF P) {
        return Math.abs(((P.x - A.x) * (B.y - A.y)) - ((P.y - A.y) * (B.x - A.x))) / ((float) Math.sqrt((double) (((B.x - A.x) * (B.x - A.x)) + ((B.y - A.y) * (B.y - A.y)))));
    }

    float sqr(float x) {
        return x * x;
    }

    float dist2(PointF v, PointF w) {
        return sqr(v.x - w.x) + sqr(v.y - w.y);
    }

    float distToSegmentSquared(PointF p, PointF v, PointF w) {
        float l2 = dist2(v, w);
        if (l2 == 0.0f) {
            return dist2(p, v);
        }
        float t = (((p.x - v.x) * (w.x - v.x)) + ((p.y - v.y) * (w.y - v.y))) / l2;
        if (t < 0.0f) {
            return dist2(p, v);
        }
        if (t > 1.0f) {
            return dist2(p, w);
        }
        return dist2(p, new PointF(v.x + ((w.x - v.x) * t), v.y + ((w.y - v.y) * t)));
    }

    float distToSegment(PointF p, PointF v, PointF w) {
        return (float) Math.sqrt((double) distToSegmentSquared(p, v, w));
    }

    float[] getMappedCenter() {
        this.pts[MESSAGE_DEFAULT] = (float) (this.bitmapWidth / MESSAGE_MIN_ZOOM);
        this.pts[MESSAGE_MAX_ZOOM] = (float) (this.bitmapHeight / MESSAGE_MIN_ZOOM);
        this.bitmapMatrix.mapPoints(this.pts, this.pts);
        return this.pts;
    }

    public boolean isScrapBookSelected(float x1, float y1) {
        this.pts[MESSAGE_DEFAULT] = x1;
        this.pts[MESSAGE_MAX_ZOOM] = y1;
        this.inverse.reset();
        boolean isInversable = this.bitmapMatrix.invert(this.inverse);
        this.inverse.mapPoints(this.pts, this.pts);
        float x = this.pts[MESSAGE_DEFAULT];
        float y = this.pts[MESSAGE_MAX_ZOOM];
        if (x < 0.0f || x > ((float) this.bitmapWidth) || y < 0.0f || y > ((float) this.bitmapHeight)) {
            return false;
        }
        return true;
    }

    public void drawShapeForScrapBook(Canvas canvas, int width, int height, boolean isSelected, boolean isOrthogonal) {
        canvas.save();
        canvas.concat(this.bitmapMatrix);
        this.npd.setBounds((-this.npdPadding) - this.borderStrokeWidth, (-this.npdPadding) - this.borderStrokeWidth, (this.bitmapWidth + this.npdPadding) + this.borderStrokeWidth, (this.bitmapHeight + this.npdPadding) + this.borderStrokeWidth);
        this.npd.draw(canvas);
        canvas.drawBitmap(this.bitmap, 0.0f, 0.0f, this.paintScrap);
        if (isSelected) {
            this.touchStrokeWidth = this.tempTouchStrokeWidth * (1.0f / getScale());
            this.touchPaint.setStrokeWidth(this.touchStrokeWidth);
            canvas.drawRect(this.touchRect, this.touchPaint);
            setDelAndScaleBitmapMatrix();
            if (!(this.btmDelete == null || this.btmDelete.isRecycled())) {
                canvas.drawBitmap(this.btmDelete, this.removeBitmapMatrix, this.touchPaint);
            }
            if (!(this.btmScale == null || this.btmScale.isRecycled())) {
                canvas.drawBitmap(this.btmScale, this.scaleBitmapMatrix, this.touchPaint);
            }
            if (isOrthogonal) {
                canvas.drawPath(this.dashPathVertical, this.dashPaint);
                canvas.drawPath(this.dashPathHorizontal, this.dashPaint);
            }
        }
        canvas.drawRect((float) ((-this.borderStrokeWidth) / MESSAGE_MIN_ZOOM), (float) ((-this.borderStrokeWidth) / MESSAGE_MIN_ZOOM), (float) (this.bitmapWidth + (this.borderStrokeWidth / MESSAGE_MIN_ZOOM)), (float) (this.bitmapHeight + (this.borderStrokeWidth / MESSAGE_MIN_ZOOM)), this.borderPaint);
        canvas.restore();
    }

    void setDelAndScaleBitmapMatrix() {
        if (this.removeBitmapMatrix == null) {
            this.removeBitmapMatrix = new Matrix();
        }
        if (this.scaleBitmapMatrix == null) {
            this.scaleBitmapMatrix = new Matrix();
        }
        this.removeBitmapMatrix.reset();
        this.scaleBitmapMatrix.reset();
        if (this.delW == 0 && this.btmDelete != null) {
            this.delW = this.btmDelete.getWidth();
        }
        if (this.screenWidth <= 0) {
            this.screenWidth = 720;
        }
        float bitmapScale = ((((float) this.screenWidth) / 20.0f) * 2.0f) / ((float) this.delW);
        this.deleteWidthHalf = (((float) this.delW) * bitmapScale) / 1.4f;
        this.removeBitmapMatrix.postScale(bitmapScale, bitmapScale);
        this.removeBitmapMatrix.postTranslate((-this.scrapBookPadding) - ((((float) this.delW) * bitmapScale) / 2.0f), (-this.scrapBookPadding) - ((((float) this.delW) * bitmapScale) / 2.0f));
        this.scaleBitmapMatrix.postScale(bitmapScale, bitmapScale);
        this.scaleBitmapMatrix.postTranslate((((float) this.bitmapWidth) + this.scrapBookPadding) - ((((float) this.delW) * bitmapScale) / 2.0f), (((float) this.bitmapHeight) + this.scrapBookPadding) - ((((float) this.delW) * bitmapScale) / 2.0f));
        float scale = getScale();
        this.scaleBitmapMatrix.postScale(1.0f / scale, 1.0f / scale, this.touchRect.right, this.touchRect.bottom);
        this.removeBitmapMatrix.postScale(1.0f / scale, 1.0f / scale, this.touchRect.left, this.touchRect.top);
        if (this.screenWidth > 0) {
            this.tempTouchStrokeWidth = ((float) this.screenWidth) / 120.0f;
        }
    }

    public void initScrapBook(NinePatchDrawable npd) {
        setNinePatch(npd);
    }

    public void setNinePatch(NinePatchDrawable npd) {
        this.npd = npd;
        this.touchRect.round(new Rect());
    }

    float getScale() {
        this.bitmapMatrix.getValues(this.values);
        float scalex = this.values[MESSAGE_DEFAULT];
        float skewy = this.values[MESSAGE_MAX_LEFT];
        float scale = (float) Math.sqrt((double) ((scalex * scalex) + (skewy * skewy)));
        if (scale <= 0.0f) {
            return 1.0f;
        }
        return scale;
    }

    boolean isInCircle(float x1, float y1) {
        this.pts[MESSAGE_DEFAULT] = x1;
        this.pts[MESSAGE_MAX_ZOOM] = y1;
        this.bitmapMatrix.invert(this.inverse);
        this.inverse.mapPoints(this.pts, this.pts);
        float x = this.pts[MESSAGE_DEFAULT];
        float y = this.pts[MESSAGE_MAX_ZOOM];
        float scale = getScale();
        if (((x - this.touchRect.right) * (x - this.touchRect.right)) + ((y - this.touchRect.bottom) * (y - this.touchRect.bottom)) < (this.deleteWidthHalf * this.deleteWidthHalf) / (scale * scale)) {
            return true;
        }
        return false;
    }

    boolean isOnCross(float x1, float y1) {
        this.pts[MESSAGE_DEFAULT] = x1;
        this.pts[MESSAGE_MAX_ZOOM] = y1;
        this.bitmapMatrix.invert(this.inverse);
        this.inverse.mapPoints(this.pts, this.pts);
        float x = this.pts[MESSAGE_DEFAULT];
        float y = this.pts[MESSAGE_MAX_ZOOM];
        float scale = getScale();
        if (((x - this.touchRect.left) * (x - this.touchRect.left)) + ((y - this.touchRect.top) * (y - this.touchRect.top)) < (this.deleteWidthHalf * this.deleteWidthHalf) / (scale * scale)) {
            return true;
        }
        return false;
    }

    public void resetDashPaths() {
        if (this.dashPathVertical == null) {
            this.dashPathVertical = new Path();
        }
        this.dashPathVertical.reset();
        this.dashPathVertical.moveTo((float) (this.bitmapWidth / MESSAGE_MIN_ZOOM), (float) ((-this.bitmapHeight) / MESSAGE_MAX_TOP));
        this.dashPathVertical.lineTo((float) (this.bitmapWidth / MESSAGE_MIN_ZOOM), (float) ((this.bitmapHeight * MESSAGE_MAX_BOTTOM) / MESSAGE_MAX_TOP));
        if (this.dashPathHorizontal == null) {
            this.dashPathHorizontal = new Path();
        }
        this.dashPathHorizontal.reset();
        this.dashPathHorizontal.moveTo((float) ((-this.bitmapWidth) / MESSAGE_MAX_TOP), (float) (this.bitmapHeight / MESSAGE_MIN_ZOOM));
        this.dashPathHorizontal.lineTo((float) ((this.bitmapWidth * MESSAGE_MAX_BOTTOM) / MESSAGE_MAX_TOP), (float) (this.bitmapHeight / MESSAGE_MIN_ZOOM));
    }
}
