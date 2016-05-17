package com.example.florian.bung;

import android.os.AsyncTask;
import android.view.View;
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by florian on 17.05.16.
 */
public class News {
    public class worker extends AsyncTask<Void, Void, String> {

        private JSONArray data;

        worker(){

        }

        @Override
        protected String doInBackground(Void... params) {
            try
            {
                HttpClient client = new DefaultHttpClient();
                URI website = new URI("http://api.golem.de/api/article/latest/limit/?key=6ea752bf080139b5507ef7b6245dc710&format=json");
                HttpGet request = new HttpGet();
                request.setURI(website);

                HttpResponse response = client.execute(request);

                HttpEntity e = response.getEntity();

                JSONObject object = new JSONObject(EntityUtils.toString(e));
                return object.toString();
            }
            catch(URISyntaxException e) {
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

                this.data =  object.getJSONArray("data");

            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    public Article[] getLatestNews() throws JSONException {
        worker myworker = new worker();
        myworker.execute();

        // Article Array von der Größe des Data Arrays
        Article[] latestArticle = new Article[myworker.data.length()];

        for(int i=0; i< myworker.data.length();i++){
            // Pro Schleifendurchgang ein JSONObjekt aus dem JSONArray holen
            JSONObject temp = myworker.data.getJSONObject(i);

            // Pro JSONObjekt gibt es nochmals ein JSONArray mit den Bildinformationen
            JSONArray images = temp.getJSONArray("leadimg");

                    // Diese Bildinformationen holen wir aus einem JSON Array und speichern sie
                    String image_url[] = new String[images.length()];
                    int image_width[] = new int[images.length()];
                    int image_height[] = new int[images.length()];
                    for(int x=0; x<images.length();x++){
                        JSONObject image = images.getJSONObject(i);
                        image_url[x] = image.getString("url");
                        image_width[x] = Integer.parseInt(image.getString("width"));
                        image_height[x] = Integer.parseInt(image.getString("height"));
                    }
            // Aus diesen ganzen Daten wird nun ein Article Objekt zusammengebaut
            Article article = new Article(temp.getInt("articleid"),
                                            temp.getString("headline"),
                                            temp.getString("abstracttext"),
                                            temp.getString("url"),
                                            temp.getString("date"),
                                            image_url,
                                            image_width,
                                            image_height
                    );
            // Pro Schleifendurchlauf wird ein Article Objekt erstellt und in das latestArticle Array beigefügt
            latestArticle[i] = article;
        }
        return latestArticle;
    }

}







