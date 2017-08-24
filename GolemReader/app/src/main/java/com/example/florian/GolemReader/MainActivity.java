package com.example.florian.GolemReader;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.json.JSONException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    Article[] latest_articles = new Article[5];
    // Asynchroner Worker der ein Artikelbild runterlädt


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            // Die neuesten Artikel in die latest_articles Klassenvariable schreiben
            News news = new News();
        try {
            // Die neuesten Artikel werden geladen und als Mitgliedsvariable gespeichert
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
        container.setOrientation(LinearLayout.VERTICAL);


        // Arrays der Layout Elemente
        LinearLayout[] temp  = new LinearLayout[latest_articles.length];
        LinearLayout[] layout = new LinearLayout[latest_articles.length];
        TextView[] headviews = new TextView[latest_articles.length];
        TextView[] contentviews = new TextView[latest_articles.length];
        ImageView[] imageViews = new ImageView[latest_articles.length];

        // Die Arrays mit Instanzen füllen
        for(int x =0; x < latest_articles.length; x++){
            layout[x] = new LinearLayout(this);
            layout[x].setOrientation(LinearLayout.VERTICAL);
            temp[x] = new LinearLayout(this);
            temp[x].setOrientation(LinearLayout.HORIZONTAL);
            headviews[x] = new TextView(this);
            contentviews[x] = new TextView(this);
            imageViews[x] = new ImageView(this);
        }

        for(int i =0; i<latest_articles.length;i++){

            // ID der Reihe setzen
            contentviews[i].setId(latest_articles[i].article_id);
            contentviews[i].setSingleLine(false);
            layout[i].setBackgroundResource(R.drawable.row_border);

            // Ein Clicklistener auf den View setzen
            contentviews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ArticleDetails.class);
                    Log.d(TAG,"!!!Artikel ID Übergabe"+v.getId());
                    // Artikel id an die nächste Activity weitergeben
                    intent.putExtra("article_id",v.getId());

                    // Startet die ViewTrack Activity
                    startActivity(intent);
                }
            });

            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams
                    (RadioGroup.LayoutParams.WRAP_CONTENT,
                            RadioGroup.LayoutParams.WRAP_CONTENT);


            // Die Bild URL als Tag setzen
            imageViews[i].setTag(latest_articles[i].image_url);

            Log.d(TAG,"!!!Image URL::::::"+latest_articles[i].image_url);


            // Das Bild mit dem Worker runterladen und in den ImageView setzen
            ImageLoad task = new ImageLoad();
            task.execute(imageViews[i]);

            // Höhe und Breite des Artikelbildes setzen
            android.view.ViewGroup.LayoutParams layoutParams = new ActionBar.LayoutParams(
                    latest_articles[i].image_width,
                    latest_articles[i].image_height);


            imageViews[i].setLayoutParams(layoutParams);
            imageViews[i].setPadding(5,5,5,5);

            // Den Unix timestamp aus dem Artikel in ein Datum umwandeln
            Date date = new Date(Integer.parseInt(latest_articles[i].date)*1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
            String formattedDate = sdf.format(date);

            // Texte in die Views setzen
            headviews[i].setText(latest_articles[i].headline);
            headviews[i].setTextSize(16);
            contentviews[i].setText(latest_articles[i].abstract_text + "\n\n" + formattedDate);
            contentviews[i].setPadding(5,5,5,5);



            // Den Headview und das Image in das horizontale Lineare Layout setzen
            temp[i].addView(imageViews[i]);
            temp[i].addView(headviews[i]);

            // Das vertikale Lineare Layout in das vertikale Lineare Layout setzen
            layout[i].addView(temp[i]);

            // das Lineare vertikale Layout in den Scrollview setzen
            layout[i].addView(contentviews[i]);
            container.addView(layout[i]);
        }
    }

    ArrayList<String> getArticle(int i){
        ArrayList<String> Article = new ArrayList<>(10);

        Article.add(this.latest_articles[i].headline);
        Article.add(this.latest_articles[i].abstract_text);
        Article.add(this.latest_articles[i].image_url);
        Article.add(String.valueOf(this.latest_articles[i].image_width));
        Article.add(String.valueOf(this.latest_articles[i].image_height));
        Article.add(this.latest_articles[i].url);

        return Article;
    }
}