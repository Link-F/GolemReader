package com.example.florian.bung;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

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
        TableLayout container = (TableLayout) findViewById(R.id.table_layout);

        // Array von Table Rows
        TableRow[] rows = new TableRow[latest_articles.length];

        // Array von TextViews
        TextView[] views = new TextView[latest_articles.length];

        // Die Arrays mit Instanzen füllen
        for(int x =0; x < latest_articles.length; x++){
            rows[x] = new TableRow(this);
            views[x] = new TextView(this);
        }


        Log.d(TAG,String.valueOf("latest_article.length="+latest_articles.length));


            String text;
            for(int i =0; i<latest_articles.length;i++){
                // Text bei jedem Durchlauf leeren
                text = "";
                Log.d(TAG,Integer.toString(i));
                // Layout Parameter für die Tablerows setzen
                LinearLayout.LayoutParams paramsLinLay = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.FILL_PARENT, 0);
                paramsLinLay.weight = 5f;
                rows[i].setLayoutParams(paramsLinLay);
                rows[i].setBackgroundColor(Color.BLUE);
                rows[i].setBackgroundResource(R.drawable.row_border);

                // ID der Reihe setzen
                rows[i].setId(latest_articles[i].article_id);

                // Aktion des Buttons festlegen aus der ID kriegen wir den richtigen Track
                rows[i].setOnClickListener(new View.OnClickListener() {
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

                text = "test";
                RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams
                        (RadioGroup.LayoutParams.WRAP_CONTENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT);

                // Das endgültige Layout zusammenbauen und hinzufügen
                views[i].setText(text);
                views[i].setPadding(10, 20, 10, 20);
                rows[i].addView(views[i]);
                container.addView(rows[i], params);
                i++;
            }
    }

}