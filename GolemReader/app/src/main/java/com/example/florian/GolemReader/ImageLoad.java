package com.example.florian.GolemReader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoad extends AsyncTask<ImageView, Void, Bitmap> {

    ImageView imageView = null;
    String ImageURL = null;
    private static final String TAG = MainActivity.class.getSimpleName();

    ImageLoad(String ImageURL){
        this.ImageURL = ImageURL;
    }

    @Override
    protected Bitmap doInBackground(ImageView... imageViews) {
        this.imageView = imageViews[0];
        return download_Image(this.ImageURL);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }

    // Load the image from the url
    private Bitmap download_Image(String url) {

        Bitmap bmp = null;
        try {
            URL ulrn = new URL(url);
            HttpURLConnection con = (HttpURLConnection) ulrn.openConnection();
            InputStream is = con.getInputStream();
            bmp = BitmapFactory.decodeStream(is);
            if (null != bmp)
                return bmp;

        } catch (Exception e) {
            Log.d(TAG,"DEBUG: " + e);
        }
        return bmp;
    }
}
