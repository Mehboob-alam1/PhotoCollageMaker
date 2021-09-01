package com.helloworld.photoeditor.collagelib;

import android.annotation.SuppressLint;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.helloworld.photoeditor.R;
import com.helloworld.photoeditor.collagelib.MyAdapter.CurrentCollageIndexChangedListener;

import java.util.ArrayList;
import java.util.List;

@SuppressLint({"InflateParams"})
public class ColorPickerAdapter extends MyRecylceAdapterBase<ColorPickerAdapter.ViewHolder> implements OnClickListener {
    private static final String TAG = "Adapter";
    int colorDefault;
    private List<Integer> colorList = new ArrayList();
    int colorSelected;
    String[] colors = new String[]{"#FFFFFF", "#EFDECD", "#CD4A4A", "#CC6666", "#BC5D58", "#FF5349", "#FD5E53", "#FD7C6E", "#FDBCB4", "#FF6E4A", "#FFA089", "#EA7E5D", "#B4674D", "#A5694F", "#FF7538", "#FF7F49", "#DD9475", "#FF8243", "#FFA474", "#9F8170", "#CD9575", "#EFCDB8", "#D68A59", "#DEAA88", "#FAA76C", "#FFCFAB", "#FFBD88", "#FDD9B5", "#FFA343", "#EFDBC5", "#FFB653", "#E7C697", "#8A795D", "#FAE7B5", "#FFCF48", "#FCD975", "#FDDB6D", "#FCE883", "#F0E891", "#ECEABE", "#BAB86C", "#FDFC74", "#FDFC74", "#FFFF99", "#C5E384", "#B2EC5D", "#87A96B", "#A8E4A0", "#1DF914", "#76FF7A", "#71BC78", "#6DAE81", "#9FE2BF", "#1CAC78", "#30BA8F", "#45CEA2", "#3BB08F", "#1CD3A2", "#17806D", "#158078", "#1FCECB", "#78DBE2", "#77DDE7", "#80DAEB", "#414A4C", "#199EBD", "#1CA9C9", "#1DACD6", "#9ACEEB", "#1A4876", "#1974D2", "#2B6CC4", "#1F75FE", "#C5D0E6", "#B0B7C6", "#5D76CB", "#A2ADD0", "#979AAA", "#ADADD6", "#7366BD", "#7442C8", "#7851A9", "#9D81BA", "#926EAE", "#CDA4DE", "#8F509D", "#C364C5", "#FB7EFD", "#FC74FD", "#8E4585", "#FF1DCE", "#FF1DCE", "#FF48D0", "#E6A8D7", "#C0448F", "#6E5160", "#DD4492", "#FF43A4", "#F664AF", "#FCB4D5", "#FFBCD9", "#F75394", "#FFAACC", "#E3256B", "#FDD7E4", "#CA3767", "#DE5D83", "#FC89AC", "#F780A1", "#C8385A", "#EE204D", "#FF496C", "#EF98AA", "#FC6C85", "#FC2847", "#FF9BAA", "#CB4154", "#EDEDED", "#DBD7D2", "#CDC5C2", "#95918C", "#232323"};
    CurrentCollageIndexChangedListener listener;
    RecyclerView recylceView;
    View selectedListItem;
    int selectedPosition;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";
        public View colorPickerView;
        private int item;
        CurrentCollageIndexChangedListener viewHolderListener;

        public void setCurrentCollageIndexChangedListener(CurrentCollageIndexChangedListener l) {
            this.viewHolderListener = l;
        }

        public void setItem(int items) {
            this.item = items;
            this.colorPickerView.setBackgroundColor(this.item);
        }

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.colorPickerView = itemLayoutView.findViewById(R.id.color_picker_view);
        }
    }

    public ColorPickerAdapter(CurrentCollageIndexChangedListener l, int cDefault, int cSelected) {
        this.listener = l;
        this.colorDefault = cDefault;
        this.colorSelected = cSelected;
        createColorList();
    }

    private void createColorList() {
        for (String parseColor : this.colors) {
            this.colorList.add(Integer.valueOf(Color.parseColor(parseColor)));
        }
    }

    public void setSelectedPositinVoid() {
        this.selectedListItem = null;
        this.selectedPosition = -1;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_recycler_view_item, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        viewHolder.setCurrentCollageIndexChangedListener(this.listener);
        itemLayoutView.setOnClickListener(this);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.setItem(((Integer) this.colorList.get(position)).intValue());
        if (this.selectedPosition == position) {
            viewHolder.itemView.setBackgroundColor(this.colorSelected);
        } else {
            viewHolder.itemView.setBackgroundColor(this.colorDefault);
        }
    }

    public void onAttachedToRecyclerView(RecyclerView recylceView) {
        this.recylceView = recylceView;
    }

    public void onClick(View view) {
        int position = this.recylceView.getChildPosition(view);
        RecyclerView.ViewHolder oldViewHolder = this.recylceView.findViewHolderForPosition(this.selectedPosition);
        if (oldViewHolder != null) {
            View oldView = oldViewHolder.itemView;
            if (oldView != null) {
                oldView.setBackgroundColor(this.colorDefault);
            }
        }
        if (this.selectedListItem != null) {
            Log.d(TAG, "selectedListItem " + position);
        }
        Log.d(TAG, "onClick " + position);
        this.listener.onIndexChanged(((Integer) this.colorList.get(position)).intValue());
        this.selectedPosition = position;
        view.setBackgroundColor(this.colorSelected);
        this.selectedListItem = view;
    }

    public int getItemCount() {
        return this.colorList.size();
    }
}
