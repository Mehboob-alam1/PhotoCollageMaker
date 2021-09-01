package com.helloworld.photoeditor;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerItemCustomAdapter extends ArrayAdapter<DrawerItem> {
    DrawerItem[] data = null;
    int layoutResourceId = R.layout.list_view_item_row;
    Context mContext;

    public DrawerItemCustomAdapter(Context mContext, DrawerItem[] data) {
        super(mContext, R.layout.list_view_item_row, data);
        this.mContext = mContext;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        listItem = ((Activity) this.mContext).getLayoutInflater().inflate(this.layoutResourceId, parent, false);
        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.imageViewIcon);
        TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);
//        textViewName.setTypeface(Utils.typeFaceBold);
        DrawerItem folder = this.data[position];
        imageViewIcon.setImageResource(folder.icon);
        textViewName.setText(folder.name);
        return listItem;
    }
}
