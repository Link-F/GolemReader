package com.example.florian.bung;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            // Die neuesten Artikel in die latest_articles Klassenvariable schreiben
            News news = new News();
            Article[] latest_articles = new Article[5];
        try {
            latest_articles = news.getLatestNews();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // Lineares Layout Referenz holen
        LinearLayout container = (LinearLayout) findViewById(R.id.linearlayout);

        // Array von TextViews
        TextView[] views = new TextView[latest_articles.length];

        ImageView[] imageViews = new ImageView[latest_articles.length];

        // Die Arrays mit Instanzen füllen
        for(int x =0; x < latest_articles.length; x++){
            views[x] = new TextView(this);
            imageViews[x] = new ImageView(this);
        }


        Log.d(TAG,String.valueOf("!!!latest_article.length="+latest_articles.length));


            String text;
            for(int i =0; i<latest_articles.length;i++){
                // Text bei jedem Durchlauf leeren
                text = "";
                Log.d(TAG,"!!!Schleifendurchlauf:"+Integer.toString(i));


                // ID der Reihe setzen
                views[i].setId(latest_articles[i].article_id);
                views[i].setSingleLine(false);
                views[i].setBackgroundResource(R.drawable.row_border);

                // Ein Clicklistener auf den View setzen
                views[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ArticleDetails.class);
                        intent.putExtra("ArticleID", v.getId());

                        // Startet die ViewTrack Activity
                        startActivity(intent);
                    }
                });
                Log.d(TAG, "!!Durchlauf" + i);
                // Beschreibung des Artikels wird zusammengesetzt
                text = latest_articles[i].headline+"\n\n" + latest_articles[i].abstract_text+"\n\n"
                        + latest_articles[i].date;

                RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams
                        (RadioGroup.LayoutParams.WRAP_CONTENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT);

                Log.d(TAG,"!!!!!ImageURL:"+latest_articles[i].image_url);
                /*Bitmap bmp = null;
                try {
                    URL url = new URL(latest_articles[i].image_url);
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Das endgültige Layout mit Bild zusammenbauen und hinzufügen
                imageViews[i].setImageBitmap(bmp);
                container.addView(imageViews[i]);
                views[i].setText(text);
                views[i].setPadding(10, 20, 10, 20);
                container.addView(views[i], params);*/
            }
    }

}