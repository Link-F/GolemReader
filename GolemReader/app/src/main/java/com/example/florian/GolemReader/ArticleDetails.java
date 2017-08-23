package com.example.florian.GolemReader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

public class ArticleDetails extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Article ....");
        pDialog.show();

        // Referenz auf Layout holen
        LinearLayout layout = (LinearLayout)findViewById(R.id.LinearLayout);
        layout.setOrientation(LinearLayout.VERTICAL);


        TextView contentview = new TextView(this);
        TextView headview = new TextView(this);
        ImageView image = new ImageView(this);


        try {
            Intent intent = getIntent();
            int article_id = intent.getIntExtra("article_id", 0);
            setArticle(article_id);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Eigenschaften des Image View setzen
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                article.image_width*3,
                article.image_height*3);

        layoutParams.gravity= Gravity.CENTER;
        image.setLayoutParams(layoutParams);


        // View Parameter
        contentview.setSingleLine(false);
        headview.setSingleLine(false);
        //view.setBackgroundResource(R.drawable.row_border);


        // Die Bild URL als Tag setzen
        image.setTag(article.image_url);

        // Das Bild mit dem Worker runterladen und in den ImageView setzen
        ImageLoad task = new ImageLoad();
        task.execute(image);


        // extview EigenschaftenT
        headview.setText(article.headline);
        headview.setTextSize(20);
        headview.setGravity(Gravity.CENTER);

        contentview.setText(article.subheadline + "\n\n\n" + article.abstract_text);
        contentview.setGravity(Gravity.CENTER);

        // Viewws in das Layout setzen
        layout.addView(image);
        layout.addView(headview);
        layout.addView(contentview);

        final TextView link = new TextView(this);
        link.setText(article.url);
        Linkify.addLinks(link, Linkify.WEB_URLS);

        link.setGravity(Gravity.CENTER);
        layout.addView(link);


        new CountDownTimer(500, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // Tue nichts
            }

            @Override
            public void onFinish() {
                // Ladebalken beenden
                pDialog.dismiss();

            }
        }.start();
    }

    public class worker extends AsyncTask<Void, Void, String> {

        public int article_id;

        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                URI website = new URI("http://api.golem.de/api/article/meta/"+article_id+"/?key=6ea752bf080139b5507ef7b6245dc710&format=json");
                HttpGet request = new HttpGet();
                request.setURI(website);

                HttpResponse response = client.execute(request);

                HttpEntity e = response.getEntity();

                JSONObject object = new JSONObject(EntityUtils.toString(e));
                return object.toString();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "Fehler";
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                // JSON Objekt aus den geladenem JSON erstellen
                JSONObject object = new JSONObject(s);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void setArticle(int article_id) throws JSONException, ExecutionException, InterruptedException {

        worker myworker = new worker();
        myworker.article_id = article_id;

        JSONObject object = new JSONObject(myworker.execute().get());
        //JSONArray data = json.getJSONArray("data");

        JSONObject temp = object.getJSONObject("data");

        Log.d(TAG,"!!!DEBUGasdfasdf:"+ temp.getString("headline"));


        // Pro JSONObjekt gibt es nochmals ein JSONObjekt mit den Bildinformationen
        JSONObject image = temp.getJSONObject("leadimg");

        //JSONArray image_arr = temp.getJSONArray("leadimg");


        /*if(Boolean.parseBoolean(String.valueOf(image_arr.length()))) {

            int[] width_arr = new int[image_arr.length()];
            int[] height_arr = new int[image_arr.length()];
            String[] image_url_arr = new String[image_arr.length()];

            for (int i = 0; i < image_arr.length(); i++) {

                JSONObject image_obj = image_arr.getJSONObject(i);

                width_arr[i] = Integer.parseInt(image_obj.getString("width"));
                height_arr[i] = Integer.parseInt(image_obj.getString("height"));
                image_url_arr[i] = image_obj.getString("url");
            }
            // Aus diesen ganzen Daten wird nun ein Article Objekt zusammengebaut
            Article article = new Article(temp.getInt("articleid"),
                    temp.getString("headline"),
                    temp.getString("subheadline"),
                    temp.getString("abstracttext"),
                    temp.getString("url"),
                    temp.getString("date"),
                    image_url_arr,
                    width_arr,
                    height_arr,
                    Integer.parseInt(temp.getString("pages"))
            );
            Log.d(TAG,"!!!Debug"+article.headline);
            this.article = article;
        }
        else
        {*/

            // Aus diesen ganzen Daten wird nun ein Article Objekt zusammengebaut
            Article article = new Article(temp.getInt("articleid"),
                    temp.getString("headline"),
                    temp.getString("subheadline"),
                    temp.getString("abstracttext"),
                    temp.getString("url"),
                    temp.getString("date"),
                    image.getString("url"),
                    Integer.parseInt(image.getString("width")),
                    Integer.parseInt(image.getString("height")),
                    Integer.parseInt(temp.getString("pages"))
            );
            this.article = article;
    }
}
