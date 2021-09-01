package com.helloworld.photoeditor.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.helloworld.photoeditor.R;
import com.helloworld.photoeditor.common_lib.Parameter;
import com.helloworld.photoeditor.fragments.EffectFragment.BitmapReady;

import java.lang.reflect.Field;

public class FullEffectFragment extends Fragment {
    private static final String TAG = FullEffectFragment.class.getName();
    Activity activity;
    FullBitmapReady bitmapReadyListener;
    Context context;
    Bitmap currentBitmap;
    EffectFragment effectFragment;
    View header;
    ImageView imageView;
    Bitmap sourceBitmap;

    public interface FullBitmapReady {
        void onBitmapReady(Bitmap bitmap, Parameter parameter);

        void onCancel();
    }

    public interface HideHeaderListener {
        void hide(boolean z);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        boolean z = false;
        View fragmentView = inflater.inflate(R.layout.full_fragment_effect, container, false);
        this.imageView = fragmentView.findViewById(R.id.imageView1);
        this.header = fragmentView.findViewById(R.id.full_fragment_apply_filter_header);
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder("imageView is null ");
        if (this.imageView == null) {
            z = true;
        }
        Log.e(str, stringBuilder.append(z).toString());
        addFragment();
        return fragmentView;
    }

    public void onAttach(Activity act) {
        super.onAttach(act);
        this.context = getActivity();
        this.activity = getActivity();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.imageView.setImageBitmap(this.sourceBitmap);
    }

    public void setBitmapWithParameter(Bitmap btm, Parameter parameter) {
        this.sourceBitmap = btm;
        if (this.imageView != null) {
            this.imageView.setImageBitmap(btm);
        }
        if (this.effectFragment != null) {
            if (parameter == null || this.effectFragment.parameterGlobal == null || parameter.getId() != this.effectFragment.parameterGlobal.id) {
                this.effectFragment.setBitmapAndResetBlur(this.sourceBitmap);
            } else {
                this.effectFragment.setBitmap(this.sourceBitmap);
            }
            if (parameter != null) {
                this.effectFragment.setParameters(parameter);
            }
        }
    }

    void addFragment() {
        if (this.effectFragment == null) {
            this.effectFragment = (EffectFragment) getChildFragmentManager().findFragmentByTag("MY_FRAGMENT");
            if (this.effectFragment == null) {
                this.effectFragment = new EffectFragment();
                Log.e(TAG, "EffectFragment == null");
                this.effectFragment.setArguments(getArguments());
                getChildFragmentManager().beginTransaction().add(R.id.fragment_container, this.effectFragment, "MY_FRAGMENT").commit();
            }
            getChildFragmentManager().beginTransaction().show(this.effectFragment).commit();
            this.effectFragment.setBitmapReadyListener(new BitmapReady() {
                public void onBitmapReady(Bitmap bitmap) {
                    FullEffectFragment.this.imageView.setImageBitmap(bitmap);
                    FullEffectFragment.this.currentBitmap = bitmap;
                }
            });
            this.effectFragment.setHideHeaderListener(new HideHeaderListener() {
                public void hide(boolean show) {
                    if (show) {
                        FullEffectFragment.this.header.setVisibility(0);
                    } else {
                        FullEffectFragment.this.header.setVisibility(4);
                    }
                }
            });
        }
    }

    public void setFullBitmapReadyListener(FullBitmapReady listener) {
        this.bitmapReadyListener = listener;
    }

    public void myClickHandler(View view) {
        if (view.getId() == R.id.button_apply_filter) {
            if (this.currentBitmap == null) {
                this.effectFragment.resetParametersWithoutChange();
                this.bitmapReadyListener.onCancel();
                return;
            }
            Parameter parameter = new Parameter(this.effectFragment.parameterGlobal);
            this.effectFragment.resetParametersWithoutChange();
            this.bitmapReadyListener.onBitmapReady(this.currentBitmap, parameter);
        } else if (view.getId() == R.id.button_cancel_filter) {
            this.effectFragment.resetParametersWithoutChange();
            this.bitmapReadyListener.onCancel();
        } else {
            if (this.header == null) {
                this.header = getView().findViewById(R.id.full_fragment_apply_filter_header);
            }
            int id = view.getId();
            if (id == R.id.button_lib_cancel || id == R.id.button_lib_ok || id == R.id.button_filter_reset) {
                this.header.setVisibility(0);
            } else {
                this.header.setVisibility(4);
            }
            this.effectFragment.myClickHandler(id);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e2) {
            throw new RuntimeException(e2);
        }
    }
}
