package com.example.coronacrux;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.coronacrux.Adapter.newsRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;

public class ScrollingActivity extends AppCompatActivity {
    private OkHttpClient httpClient;
    ArrayList<String> titleList= new ArrayList<>();
    ArrayList<String> urlImages= new ArrayList<String>();
    ArrayList<String> descriptionList= new ArrayList<String>();
    ArrayList<String> sourceName= new ArrayList<String>();
    ArrayList<String> publishTime= new ArrayList<String>();
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager LayoutManager;
    EditText search;
    ImageView ivsearch;
    String defaultnews="coronavirus";
    String usersearch;
    newsRecyclerAdapter newsRecyclerAdapter;
    private int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScrollingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        search = findViewById(R.id.edtNews);
        ivsearch=findViewById(R.id.newssearch);
        recyclerView=findViewById(R.id.newsrecyclerVIew);
        LayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(LayoutManager);
        if(flag ==0) {
            fetchNews(defaultnews);
        }

        ivsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == 1)
                {
                    try {
                        usersearch= URLEncoder.encode(search.getText().toString(), "UTF-8");
                        titleList.clear();
                        urlImages.clear();
                        descriptionList.clear();
                        sourceName.clear();
                        publishTime.clear();
                        fetchNews(usersearch);
                        Toast.makeText(getApplicationContext(),"WAIT !,Preparing Search Results.",Toast.LENGTH_SHORT).show();
                        flag--;
                    }catch (Exception e){e.printStackTrace();}
                }
            }

        });



    }
    public void fetchNews(String news1)
    {


        DownloadTask task = new DownloadTask();
        try {
            Toast.makeText(this,"WAIT ! News Loading.",Toast.LENGTH_SHORT).show();
            task.execute("https://newsapi.org/v2/everything?q="+news1+"&apiKey=8b592dec2a1647d79d012dbc27bace07").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        newsRecyclerAdapter= new newsRecyclerAdapter(getApplicationContext(),titleList,descriptionList,urlImages,sourceName,publishTime);
        recyclerView.setAdapter(newsRecyclerAdapter);
        flag++;

    }



    public class DownloadTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection connection=null;
            try{
                url= new URL(urls[0]);
                connection=(HttpURLConnection)url.openConnection();
                InputStream in =connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data=reader.read();
                while(data != -1)
                {
                    char current=(char)data;
                    result +=current;
                    data=reader.read();
                }
                Log.i("DATA is","Rsult is "+result);
                return  result;
            }
            catch (Exception e)
            {e.printStackTrace();
                // Toast.makeText(getContext(),"Could not find any news",Toast.LENGTH_SHORT).show();
                return  null;}

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                String title = "";
                String desc="";
                String image="";
                String source="";
                String publishdate="";
                JSONObject data = new JSONObject(s);
                JSONArray items = data.getJSONArray("articles");

                for (int i = 0; i <15; i++) {
                    JSONObject item = items.getJSONObject(i);
                    title += item.getString("title");
                    desc+=item.get("description");
                    image+=item.getString("urlToImage");
                    JSONObject sourcekey = item.getJSONObject("source");
                    source+=sourcekey.getString("name");
                    publishdate+=item.getString("publishedAt");
                    titleList.add(title);
                    descriptionList.add(desc);
                    urlImages.add(image);
                    sourceName.add(source);
                    publishTime.add(publishdate);
                    title="";
                    desc="";
                    image="";
                    source="";
                    publishdate="";
                }

            }catch (JSONException e) {
                e.printStackTrace();
                // Toast.makeText(getContext(),"Could not find news",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
