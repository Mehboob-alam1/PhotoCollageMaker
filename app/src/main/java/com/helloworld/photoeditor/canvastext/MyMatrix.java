package com.helloworld.photoeditor.canvastext;

import android.graphics.Matrix;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MyMatrix extends Matrix implements Serializable {
    private static final long serialVersionUID = 6346371585195628612L;

    public MyMatrix(Matrix src) {
        super(src);
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        float[] values = new float[9];
        super.getValues(values);
        oos.writeObject(values);
    }

    private void readObject(ObjectInputStream ois) throws Exception {
        ois.defaultReadObject();
        float[] fArr = new float[9];
        super.setValues((float[]) ois.readObject());
    }
}
