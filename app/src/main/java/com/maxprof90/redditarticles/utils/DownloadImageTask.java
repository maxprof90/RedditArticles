package com.maxprof90.redditarticles.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.maxprof90.redditarticles.models.Children;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    Children children;
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }
    // will be used in the post adapter to save the bitmap img in  child object
    public DownloadImageTask(ImageView bmImage, Children children) {
        this.bmImage = bmImage;
        this.children = children;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setVisibility(View.VISIBLE);
        bmImage.setImageBitmap(result);
        if (children!=null)
            children.getData().setThumbBitmap(result);
    }
}

