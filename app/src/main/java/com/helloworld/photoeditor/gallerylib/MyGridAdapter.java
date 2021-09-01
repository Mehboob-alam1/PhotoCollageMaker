package com.helloworld.photoeditor.gallerylib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.helloworld.photoeditor.BuildConfig;
import com.helloworld.photoeditor.R;

import java.lang.ref.WeakReference;
import java.util.List;

public class MyGridAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    List<GridViewItem> items;
    private Bitmap placeHolder;

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            this.bitmapWorkerTaskReference = new WeakReference<>(bitmapWorkerTask);
        }

        BitmapWorkerTask getBitmapWorkerTask() {
            return this.bitmapWorkerTaskReference.get();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class BitmapWorkerTask extends AsyncTask<Long, Void, Bitmap> {
        private long data = 0;
        private final WeakReference<ImageView> imageViewReference;
        private GridViewItem item;

        BitmapWorkerTask(ImageView imageView, GridViewItem item) {
            this.imageViewReference = new WeakReference<>(imageView);
            this.item = item;
        }

        protected Bitmap doInBackground(Long... params) {
            this.data = params[0];
            return this.item.getImage();
        }

        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }
            if (bitmap != null) {
                ImageView imageView = this.imageViewReference.get();
                if (this == MyGridAdapter.getBitmapWorkerTask(imageView)) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    static class ViewHolder {
        ImageView imageView;
        TextView selectedCount;
        View textContainer;
        TextView textCount;
        TextView textPath;

        ViewHolder() {
        }
    }

    MyGridAdapter(Context context, List<GridViewItem> items) {
        this.items = items;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.placeHolder = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_pattern);
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.grid_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textPath = convertView.findViewById(R.id.textView_path);

            viewHolder.textPath.setSelected(true);
            viewHolder.textPath.setSingleLine(true);
            viewHolder.textPath.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            viewHolder.textPath.setHorizontallyScrolling(true);
            viewHolder.textPath.setLines(1);
            viewHolder.textPath.setMarqueeRepeatLimit(-1);

            viewHolder.textCount = convertView.findViewById(R.id.textViewCount);
            viewHolder.imageView = convertView.findViewById(R.id.imageView);
            viewHolder.textContainer = convertView.findViewById(R.id.grid_item_text_container);
            viewHolder.selectedCount = convertView.findViewById(R.id.textViewSelectedItemCount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String folderName = this.items.get(position).getFolderName();
        if (folderName == null || folderName.length() == 0) {
            if (viewHolder.textContainer.getVisibility() == View.VISIBLE) {
                viewHolder.textContainer.setVisibility(View.GONE);
            }
            if (this.items.get(position).selectedItemCount > 0) {
                viewHolder.selectedCount.setText(BuildConfig.FLAVOR + this.items.get(position).selectedItemCount);
                if (viewHolder.selectedCount.getVisibility() == View.INVISIBLE) {
                    viewHolder.selectedCount.setVisibility(View.VISIBLE);
                }
            } else if (viewHolder.selectedCount.getVisibility() == View.VISIBLE) {
                viewHolder.selectedCount.setVisibility(View.INVISIBLE);
            }
        } else {
            if (viewHolder.textContainer.getVisibility() == View.GONE) {
                viewHolder.textContainer.setVisibility(View.VISIBLE);
            }
            viewHolder.textPath.setText(this.items.get(position).getFolderName());
            viewHolder.textCount.setText(this.items.get(position).count);
            if (viewHolder.selectedCount.getVisibility() == View.VISIBLE) {
                viewHolder.selectedCount.setVisibility(View.INVISIBLE);
            }
        }
        loadBitmap((long) position, viewHolder.imageView, this.items.get(position));
        return convertView;
    }

    private void loadBitmap(long resId, ImageView imageView, GridViewItem item) {
        if (cancelPotentialWork(resId, imageView)) {
            BitmapWorkerTask task = new BitmapWorkerTask(imageView, item);
            imageView.setImageDrawable(new AsyncDrawable(this.context.getResources(), this.placeHolder, task));
            task.execute(resId);
        }
    }

    private boolean cancelPotentialWork(long data, ImageView imageView) {
        BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask == null) {
            return true;
        }
        long bitmapData = bitmapWorkerTask.data;
        if (bitmapData != 0 && bitmapData == data) {
            return false;
        }
        bitmapWorkerTask.cancel(true);
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                return ((AsyncDrawable) drawable).getBitmapWorkerTask();
            }
        }
        return null;
    }
}
