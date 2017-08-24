package com.example.florian.GolemReader;

import android.os.AsyncTask;
import android.util.Log;

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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

/**
 * Created by florian on 17.05.16.
 */
public class News {
    private static final String TAG = MainActivity.class.getSimpleName();

    public JSONArray data = new JSONArray();

    public class worker extends AsyncTask<Void, Void, String> {
        worker() {
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                URI website = new URI("http://api.golem.de/api/article/latest/15/?key=6ea752bf080139b5507ef7b6245dc710&format=json");
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

                /*String Success = object.getString("success");
                if ( Success == "true" ){
                    text.setText("\n\nKein Artikel gefunden");
                }*/

                data = object.getJSONArray("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    public Article[] getLatestNews() throws JSONException, ExecutionException, InterruptedException {
        worker myworker = new worker();
        JSONObject json = new JSONObject(myworker.execute().get());
        this.data = json.getJSONArray("data");

        Log.d(TAG, "!!myworker.data.length=" + data.length());
        // Article Array von der Größe des Data Arrays
        Article[] latestArticle = new Article[this.data.length()];

        for (int i = 0; i < this.data.length(); i++) {
            // Pro Schleifendurchgang ein JSONObjekt aus dem JSONArray holen
            JSONObject temp = this.data.getJSONObject(i);

            Log.d(TAG,"!!article.headline="+temp.getString("headline"));

            // Pro JSONObjekt gibt es nochmals ein JSONArray mit den Bildinformationen
            JSONObject image = temp.getJSONObject("leadimg");

            // Aus diesen ganzen Daten wird nun ein Article Objekt zusammengebaut
            latestArticle[i] = new Article(temp.getInt("articleid"),
                    temp.getString("headline"),
                    temp.getString("abstracttext"),
                    temp.getString("url"),
                    temp.getString("date"),
                    image.getString("url"),
                    Integer.parseInt(image.getString("width")),
                    Integer.parseInt(image.getString("height"))
            );
            // Pro Schleifendurchlauf wird ein Article Objekt erstellt und in das latestArticle Array beigefügt

        Log.d(TAG,"!!!!!!headline:"+latestArticle[i].headline);
        }
        return latestArticle;

    }
}








