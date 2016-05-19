package com.example.florian.bung;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.florian.bung.MainActivity.*;

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
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.relativelayoutdetails);

        TextView view = new TextView(this);
        ImageView image = new ImageView(this);


        try {
            setArticle();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Höhe und Breite des Artikelbildes setzen
        android.view.ViewGroup.LayoutParams layoutParams = new ActionBar.LayoutParams(
                article.image_width,
                article.image_height);
        image.setLayoutParams(layoutParams);

        // View Parameter
        view.setSingleLine(false);
        //view.setBackgroundResource(R.drawable.row_border);

        // Die Bild URL als Tag setzen
        image.setTag(article.image_url);

        // Das Bild mit dem Worker runterladen und in den ImageView setzen
        ImageLoad task = new ImageLoad();
        task.execute(image);

        // Artikel in den Textview setzen
        view.setText(article.headline + "\n\n\n" + article.subheadline + "\n\n\n" + article.abstract_text);

        // Viewws in das Layout setzen
        layout.addView(image);
        layout.addView(view);

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

    public void setArticle() throws JSONException, ExecutionException, InterruptedException {

        // Artikel id holen
        Intent intent = getIntent();
        int article_id = intent.getIntExtra("article_id", 0);

        Log.d(TAG,"!!!Artikel ID:"+ article_id);
        worker myworker = new worker();
        myworker.article_id = article_id;
        JSONObject json = new JSONObject(myworker.execute().get());
        JSONArray data = json.getJSONArray("data");



        // Pro Schleifendurchgang ein JSONObjekt aus dem JSONArray holen
        JSONObject temp = data.getJSONObject(0);


        // Pro JSONObjekt gibt es nochmals ein JSONArray mit den Bildinformationen
        JSONObject image = temp.getJSONObject("leadimg");

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
        Log.d(TAG,"!!!Debug"+article.headline);
        this.article = article;

    }

}
