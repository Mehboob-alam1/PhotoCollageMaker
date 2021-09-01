package com.helloworld.photoeditor.imagesavelib;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.util.Log;

import com.helloworld.photoeditor.BuildConfig;
import com.helloworld.photoeditor.R;
import com.helloworld.photoeditor.utils.UriToUrl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageLoader {
    String TAG = "ImageLoader";
    Context context;
    int count = 0;
    Cursor cursorBackup;
    public String filemanagerstring;
    ImageLoaded imageLoadedListener;
    String loadImageMessage = "Loading image!";
    public String selectedImagePath;

    public interface ImageLoaded {
        void callFileSizeAlertDialogBuilder();
    }

    private class LoadImage19Task extends AsyncTask<Uri, Void, Void> {
        String path;
        ProgressDialog saveImageDialog;
        Uri uri;

        private LoadImage19Task() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            ImageLoader.this.loadImageMessage = ImageLoader.this.context.getString(R.string.loading_image);
            try {
                this.saveImageDialog = new ProgressDialog(ImageLoader.this.context);
                this.saveImageDialog.setMessage(ImageLoader.this.loadImageMessage);
                this.saveImageDialog.show();
            } catch (Exception e) {
            }
        }

        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            ImageLoader.this.selectedImagePath = this.path;
            ImageLoader.this.startActivityFromUri(this.uri);
            try {
                this.saveImageDialog.dismiss();
            } catch (Exception e) {
            }
        }

        protected Void doInBackground(Uri... arg0) {
            if (arg0 != null) {
                try {
                    this.uri = arg0[0];
                    if (this.uri != null) {
                        this.path = ImageLoader.this.getRealPathFromURI19(this.uri);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public void setListener(ImageLoaded l) {
        this.imageLoadedListener = l;
    }

    public ImageLoader(Context context) {
        this.context = context;
    }

    public void getImageFromIntent(Intent intent) {
        Uri selectedImageUri = intent.getData();
        if (selectedImageUri == null) {
            selectedImageUri = (Uri) intent.getExtras().get("android.intent.extra.STREAM");
        }
        Log.e(this.TAG, "a" + selectedImageUri);
        if (VERSION.SDK_INT >= 19) {
            this.selectedImagePath = getPathForKitKat(selectedImageUri);
            Log.e(this.TAG, "b");
            Log.e(this.TAG, "getPathForKitKat " + this.selectedImagePath);
            if (this.selectedImagePath == null) {
                new LoadImage19Task().execute(new Uri[]{selectedImageUri});
                return;
            }
            startActivityFromUri(selectedImageUri);
            return;
        }
        this.selectedImagePath = getRealPathFromURI(selectedImageUri);
        Log.e(this.TAG, "getImageFromIntent selectedImagePath  " + this.selectedImagePath);
        startActivityFromUri(selectedImageUri);
    }

    void startActivityFromUri(Uri selectedImageUri) {
        this.filemanagerstring = selectedImageUri.getPath();
        Log.w(this.TAG, "startActivityFromUri selectedImagePath" + this.selectedImagePath);
        if (this.selectedImagePath == null) {
            this.selectedImagePath = this.filemanagerstring;
            Log.w(this.TAG, "null selectedImagePath " + this.selectedImagePath);
        }
        if (this.selectedImagePath == null || this.selectedImagePath.length() == 0 || this.selectedImagePath.toLowerCase().contains("http")) {
            new LoadImage19Task().execute(new Uri[]{selectedImageUri});
            this.count++;
        } else if (checkFileExtension(this.selectedImagePath)) {
            this.imageLoadedListener.callFileSizeAlertDialogBuilder();
        } else {
            Builder builder = new Builder(this.context);
            builder.setMessage("Image Format Error").setCancelable(false).setNegativeButton("Ok", new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = new String[]{"_data"};
        Cursor cursor = this.context.getContentResolver().query(contentUri, proj, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                res = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
            }
            cursor.close();
        } catch (Exception e) {
        }
        if (cursor == null || res == null) {
            this.cursorBackup = this.context.getContentResolver().query(contentUri, proj, null, null, null);
            try {
                if (this.cursorBackup != null) {
                    int column_index = this.cursorBackup.getColumnIndexOrThrow("_data");
                    this.cursorBackup.moveToFirst();
                    res = this.cursorBackup.getString(column_index);
                }
            } catch (Exception e2) {
            }
        }
        return res;
    }

    public String getRealPathFromURI19(Uri contentUri) throws IOException {
        return saveImageToTemp(getBitmapFromUri(contentUri));
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = this.context.getContentResolver().openFileDescriptor(uri, "r");
        Bitmap image = BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.getFileDescriptor());
        parcelFileDescriptor.close();
        return image;
    }

    private String saveImageToTemp(Bitmap bitmap) throws FileNotFoundException {
        String resultPath = new StringBuilder(String.valueOf(ImageUtility.getPrefferredDirectoryPath(this.context, false, true, false))).append("temp/dump.dump").toString();
        File f = new File(resultPath);
        f.getParentFile().mkdirs();
        Log.i(this.TAG, "resultPath " + resultPath);
        bitmap.compress(CompressFormat.JPEG, 90, new FileOutputStream(resultPath));
        bitmap.recycle();
        Log.i(this.TAG, "is file exist " + f.exists());
        return resultPath;
    }

    private String getFileExtension(String str) {
        if (str == null) {
            str = BuildConfig.FLAVOR;
        }
        int dotPos = str.lastIndexOf(".");
        String extension = BuildConfig.FLAVOR;
        if (dotPos > 0) {
            return str.substring(dotPos);
        }
        return extension;
    }

    private boolean checkFileExtension(String str) {
        String extension = getFileExtension(str).toLowerCase();
        return extension.contains("jpg") || extension.contains("png") || extension.contains("jpeg") || extension.contains("gif") || extension.contains("bmp") || extension.contains("webp") || extension.contains("dump");
    }

    public void closeCursor() {
        if (this.cursorBackup != null) {
            this.cursorBackup.close();
        }
    }

    @SuppressLint({"NewApi"})
    public String getPathForKitKat(Uri uri) {
        boolean isKitKat;
        if (VERSION.SDK_INT >= 19) {
            isKitKat = true;
        } else {
            isKitKat = false;
        }
        if (isKitKat && DocumentsContract.isDocumentUri(this.context, uri)) {
            if (isExternalStorageDocument(uri)) {
                UriToUrl.split = DocumentsContract.getDocumentId(uri).split(":");
                if ("primary".equalsIgnoreCase(UriToUrl.split[0])) {
                    return Environment.getExternalStorageDirectory() + "/" + UriToUrl.split[1];
                }
                return null;
            } else if (isDownloadsDocument(uri)) {
                return getDataColumn(this.context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
            } else {
                if (!isMediaDocument(uri)) {
                    return null;
                }
                String type = DocumentsContract.getDocumentId(uri).split(":")[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = "_id=?";
                return getDataColumn(this.context, contentUri, "_id=?", new String[]{UriToUrl.split[1]});
            }
        }else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        } else {
            return null;
        }
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        try {
            cursor = context.getContentResolver().query(uri, new String[]{"_data"}, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                DatabaseUtils.dumpCursor(cursor);
                String string = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                if (cursor == null) {
                    return string;
                }
                cursor.close();
                return string;
            } else if (cursor == null) {
                return null;
            } else {
                cursor.close();
                return null;
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            return column;
        }
    }
}
