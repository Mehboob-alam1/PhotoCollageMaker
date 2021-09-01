package com.helloworld.photoeditor.collagelib;

import android.content.Intent;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;

import com.helloworld.photoeditor.activities.MirrorNewActivity;
import com.helloworld.photoeditor.gallerylib.GalleryFragment;
import com.helloworld.photoeditor.gallerylib.GalleryFragment.GalleryListener;


public class CollageHelper {
    protected static final String TAG = "CollageHelper";

    public static GalleryFragment getGalleryFragment(FragmentActivity activity) {
        return (GalleryFragment) activity.getSupportFragmentManager().findFragmentByTag("myFragmentTag");
    }

    public static GalleryFragment addGalleryFragment(FragmentActivity activity, int gallery_fragment_container, Object o, boolean showInter, View view) {
        FragmentManager fm = activity.getSupportFragmentManager();
        GalleryFragment galleryFragment = (GalleryFragment) fm.findFragmentByTag("myFragmentTag");
        if (galleryFragment == null) {
            galleryFragment = new GalleryFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(gallery_fragment_container, galleryFragment, "myFragmentTag");
            ft.commitAllowingStateLoss();
            galleryFragment.setGalleryListener(createGalleryListener(activity, galleryFragment, showInter, view));
            activity.findViewById(gallery_fragment_container).bringToFront();
            return galleryFragment;
        }
        activity.getSupportFragmentManager().beginTransaction().show(galleryFragment).commitAllowingStateLoss();


        return galleryFragment;
    }

    public static GalleryListener createGalleryListener(FragmentActivity activity, GalleryFragment galleryFragment, boolean showInter, View view) {
        final View view2 = view;
        final FragmentActivity fragmentActivity = activity;
        final GalleryFragment galleryFragment2 = galleryFragment;
        //final boolean z = showInter;
        return new GalleryListener() {
            public void onGalleryCancel() {
                if (!(view2 == null || view2.getVisibility() == View.VISIBLE)) {
                    view2.setVisibility(View.VISIBLE);
                }
                fragmentActivity.getSupportFragmentManager().beginTransaction().hide(galleryFragment2).commitAllowingStateLoss();
            }

            public void onGalleryOkImageArray(long[] jArr, int[] iArr, boolean x, boolean y, boolean isMirror) {
                Intent localIntent;
                if (!(view2 == null || view2.getVisibility() == View.VISIBLE)) {
                    view2.setVisibility(View.VISIBLE);
                }
                if (isMirror) {
                    localIntent = new Intent(fragmentActivity, MirrorNewActivity.class);
                } else {
                    localIntent = new Intent(fragmentActivity, CollageActivity.class);
                }
                localIntent.putExtra("photo_id_list", jArr);
                localIntent.putExtra("photo_orientation_list", iArr);
                localIntent.putExtra("is_scrap_book", x);
                localIntent.putExtra("is_shape", y);
                fragmentActivity.startActivity(localIntent);
            }

            public void onGalleryOkImageArrayRemoveFragment(long[] jArt, int[] iArr, boolean x, boolean y) {
            }

            public void onGalleryOkSingleImage(long j, int i, boolean x, boolean y) {
            }
        };
    }
}
