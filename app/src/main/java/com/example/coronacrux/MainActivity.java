package com.example.coronacrux;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.coronacrux.Fragments.Dashboard;
import com.example.coronacrux.Fragments.abouusfragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private Dashboard dashboard;
    private abouusfragment abouusfragment;
    Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.btmnavigationview);
        frameLayout = findViewById(R.id.mainframe);
        dashboard = new Dashboard();

        abouusfragment= new abouusfragment();

        Toolbar toolbar = findViewById(R.id.mytoolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTitle("Corona Crux");
        }
        setSupportActionBar(toolbar);
        if(networkConnectivity())
        {
            Toast.makeText(MainActivity.this, "Network connection is available", Toast.LENGTH_SHORT).show();
            setFragment(dashboard);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                    switch (item.getItemId()) {
                        case R.id.profile:
                            setFragment(dashboard);
                            // bottomNavigationView.setItemBackgroundResource(R.color.dash);
                            return true;
                        case R.id.news:
                            Toast.makeText(getApplicationContext(),"Wait for while loading",Toast.LENGTH_SHORT).show();
                            Thread thread = new Thread()
                            {
                                @Override
                                public void run()
                                {
                                    try
                                    {
                                        sleep(2000);

                                    }

                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                    finally
                                    {
                                        Intent mainIntent = new Intent(MainActivity.this, ScrollingActivity.class);
                                        startActivity(mainIntent);

                                    }
                                }
                            };
                            thread.start();
                            return  true;


                        case R.id.aboutus:
                            setFragment(abouusfragment);
                            return true;
                        default:
                            return false;

                    }
                }
            });
        }
        else
        {
            Toast.makeText(MainActivity.this, "Network connection is not available", Toast.LENGTH_SHORT).show();
        }






    }


    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainframe, fragment);
        fragmentTransaction.commit();
    }
    public boolean networkConnectivity()
    {
        boolean have_wifi=false;
        boolean have_data=false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info:networkInfos)
        {
            if(info.getTypeName().equalsIgnoreCase("WIFI"))if(info.isConnected())have_wifi=true;
            if(info.getTypeName().equalsIgnoreCase("MOBILE"))if(info.isConnected())have_data=true;
        }
        return have_wifi || have_data;
    }




}
