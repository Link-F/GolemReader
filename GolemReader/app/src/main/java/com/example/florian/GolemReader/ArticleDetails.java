package com.example.florian.GolemReader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

    public Article article;
    public String api_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Article ....");
        pDialog.show();

        this.api_key = getString(R.string.api_key);

        // Get the article by article_id
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

        // Set the WebView
        WebView Web = (WebView) findViewById(R.id.WebView);
        Web.setWebViewClient(new WebViewClient());
        Web.loadUrl(article.url);

        new CountDownTimer(500, 1000) {

            @Override
            public void onTick(long milliseconds) {
                // Do nothing
            }

            @Override
            public void onFinish() {
                // End the load bar
                pDialog.dismiss();
            }
        }.start();
    }

    public class worker extends AsyncTask<Void, Void, String> {

        public int article_id;
        public String api_key;

        public worker(String api_key) {
            this.api_key = api_key;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                HttpClient client = new DefaultHttpClient();
                URI website = new URI("http://api.golem.de/api/article/meta/"+article_id+"/?key="+this.api_key+"&format=json");
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
                // Create a json object from the json text
                JSONObject object = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setArticle(int article_id) throws JSONException, ExecutionException, InterruptedException {

        worker myworker = new worker(this.api_key);
        myworker.article_id = article_id;

        JSONObject object = new JSONObject(myworker.execute().get());

        JSONObject temp = object.getJSONObject("data");

        // Get the image object inside the data object
        JSONObject image = temp.getJSONObject("leadimg");

            // Build an article object from the data
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