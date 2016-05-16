package com.example.florian.bung;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void request(View view) {
        TextView field = (TextView)findViewById(R.id.editText);
        worker myworker = new worker(field.getText().toString());
        myworker.execute();
    }

    public class worker extends AsyncTask<Void, Void, String>{

        String text;
        worker(String text){
            this.text = text;
        }

        @Override
        protected String doInBackground(Void... params) {
            try
            {
                HttpClient client = new DefaultHttpClient();
                URI website = new URI("http://api.golem.de/api/article/search/2/"+this.text+"/?key=6ea752bf080139b5507ef7b6245dc710&format=json");
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
            TextView  text = (TextView)findViewById(R.id.textView);
            try {
                JSONObject object = new JSONObject(s);

                /*String Success = object.getString("success");
                if ( Success == "true" ){
                    text.setText("\n\nKein Artikel gefunden");
                }*/

                JSONObject data =  object.getJSONObject("data");
                JSONArray record = data.getJSONArray("records");
                JSONObject article1 = record.getJSONObject(0);
                JSONObject article2 = record.getJSONObject(1);

                String item =
                        "\n\n" + article1.getString("headline") +
                        "\n\n" + article1.getString("abstracttext") +
                        "\n\n\n" + article1.getString("url")+
                                "\n\n---------------------------------------------" +
                        "\n\n" + article2.getString("headline") +
                        "\n\n" + article2.getString("abstracttext") +
                        "\n\n\n" + article2.getString("url");

                text.setText(item);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}