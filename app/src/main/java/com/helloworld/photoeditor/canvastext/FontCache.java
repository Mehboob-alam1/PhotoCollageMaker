package com.helloworld.photoeditor.canvastext;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;


public class FontCache {

    private static Map<String, Typeface> sCachedFonts = new HashMap<String, Typeface>();

    public static Typeface get(Context context, String assetPath) {
        if (!sCachedFonts.containsKey(assetPath)) {
            Typeface tf = Typeface.createFromAsset(context.getAssets(), assetPath);
            sCachedFonts.put(assetPath, tf);
        }

        return sCachedFonts.get(assetPath);
    }
}
