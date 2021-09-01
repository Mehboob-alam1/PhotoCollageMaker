package com.helloworld.photoeditor.collagelib;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

public class MyRecylceAdapterBase<VH extends ViewHolder> extends Adapter<VH> {
    public int getItemCount() {
        return 0;
    }

    public void onBindViewHolder(VH vh, int arg1) {
    }

    public VH onCreateViewHolder(ViewGroup arg0, int arg1) {
        return null;
    }

    public void setSelectedPositinVoid() {
    }
}
