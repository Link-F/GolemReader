package com.example.florian.GolemReader;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
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
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response;

            response = (HttpResponse)client.execute(request);
            HttpEntity entity = response.getEntity();
            BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(entity);
            InputStream inputStream = bufferedEntity.getContent();

            bmp = BitmapFactory.decodeStream(inputStream);

            if (null != bmp)
                return bmp;

        } catch (Exception e) {
            Log.d(TAG,"DEBUG: " + e);
        }
        return bmp;
    }
}
