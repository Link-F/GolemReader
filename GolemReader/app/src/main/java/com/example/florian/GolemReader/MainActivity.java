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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            // Load the newest articles
            News news = new News();
        try {
            // Save the newest articles in latest_articles
            this.latest_articles = news.getLatestNews();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // Load references of the layout
        LinearLayout container = (LinearLayout) findViewById(R.id.linearlayout);
        container.setOrientation(LinearLayout.VERTICAL);

        // Arrays of the layout elements
        LinearLayout[] temp  = new LinearLayout[latest_articles.length];
        LinearLayout[] layout = new LinearLayout[latest_articles.length];
        TextView[] headviews = new TextView[latest_articles.length];
        TextView[] contentviews = new TextView[latest_articles.length];
        ImageView[] imageViews = new ImageView[latest_articles.length];

        // Fill the layout arrays with instances
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

            // Set the id of the rows
            contentviews[i].setId(latest_articles[i].article_id);
            contentviews[i].setSingleLine(false);
            layout[i].setBackgroundResource(R.drawable.row_border);

            // Set a click listener on the contentview
            contentviews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ArticleDetails.class);
                    Log.d(TAG,"!!!Artikel ID Übergabe"+v.getId());
                    // Give the article id to the next activity
                    intent.putExtra("article_id", v.getId());

                    // Start the new activity
                    startActivity(intent);
                }
            });

            // Set the image URl as tag
            imageViews[i].setTag(latest_articles[i].image_url);

            // Load the picture with the worker and set it in ImageView
            ImageLoad task = new ImageLoad(imageViews[i].getTag().toString());
            task.execute(imageViews[i]);

            // Set the width and height of the image
            android.view.ViewGroup.LayoutParams layoutParams = new ActionBar.LayoutParams(
                    latest_articles[i].image_width,
                    latest_articles[i].image_height);


            imageViews[i].setLayoutParams(layoutParams);
            imageViews[i].setPadding(5,5,5,5);

            // Convert the unix timestamp in SimpleDateFormat
            Date date = new Date(Integer.parseInt(latest_articles[i].date)*1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
            String formattedDate = sdf.format(date);

            // Set text in views
            headviews[i].setText(latest_articles[i].headline);
            headviews[i].setTextSize(16);
            contentviews[i].setText(latest_articles[i].abstract_text + "\n\n" + formattedDate);
            contentviews[i].setPadding(5,5,5,5);

            // Set the headview and imageView in horizontal linear layout
            temp[i].addView(imageViews[i]);
            temp[i].addView(headviews[i]);

            // Set the vertical layout in the horizontal linear layout
            layout[i].addView(temp[i]);

            // Set the linear vertical layout in the scroll view
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