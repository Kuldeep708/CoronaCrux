package com.example.coronacrux.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.coronacrux.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {

    TextView totalTV,deathTV,recoveredTV,activeTV,zoneresultTV,districtTV,dateTV,deltarecoveredTV,deltadeathTV,deltaconfirmedTV;
    private OkHttpClient httpClient;
    private String TAG = Dashboard.class.getSimpleName();
    private ImageView imageView;
    int d=0;
    AutoCompleteTextView searchTV;
    String location = "Jodhpur";
    String state1="Rajasthan";
    String date= DateFormat.getDateInstance().format(new Date());
    ArrayList<String> districtNames=new ArrayList<>();
    public Dashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        totalTV=view.findViewById(R.id.totalcountHome);
        deathTV=view.findViewById(R.id.deathcountHome);
        recoveredTV=view.findViewById(R.id.recoveredcountHome);
        activeTV=view.findViewById(R.id.activecountHome);
        zoneresultTV=view.findViewById(R.id.zonedetailHome);
        districtTV=view.findViewById(R.id.districtNameHome);
        dateTV = view.findViewById(R.id.dateHome);
        searchTV=view.findViewById(R.id.locationSearch);
        imageView=view.findViewById(R.id.searchImageView);
        deltaconfirmedTV =view.findViewById(R.id.deltaactive);
        deltadeathTV=view.findViewById(R.id.deltadeath);
        deltarecoveredTV=view.findViewById(R.id.deltarecovered);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(),android.R.layout.select_dialog_item,districtNames);
        searchTV.setThreshold(1);
        searchTV.setAdapter(adapter);
        searchTV.setTextColor(Color.RED);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location="";
                state1="";
                deltaconfirmedTV.setVisibility(View.INVISIBLE);
                deltarecoveredTV.setVisibility(View.INVISIBLE);
                deltadeathTV.setVisibility(View.INVISIBLE);
                try {

                    //if(!(loc.equals("")))
                   // {
                    //    String loc1=loc.substring(0,1).toUpperCase()+loc.substring(1).toLowerCase();
                        location+=searchTV.getText();
                    InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(searchTV.getWindowToken(), 0);
                        Toast.makeText(getContext(),"Searching",Toast.LENGTH_SHORT).show();
                        fetchData();
                        fetchDataZone();
                        fetchDataChange();
                  //  }
                }catch (Exception e){e.printStackTrace();}

            }
        });
        fetchData();
        fetchDataZone();
        fetchDataChange();
        return view;
    }


    String state;
    private void fetchData() {

        httpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url("https://api.covid19india.org/v2/state_district_wise.json")
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "An error has occurred " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // More code goes here
                //String location = URLEncoder.encode(input.getText().toString(), "UTF-8");

                try {
                    JSONArray data = new JSONArray(response.body().string());
                    Log.i("data 1", "data1 is" + data);
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject data1 = data.getJSONObject(i);
                        String state = data1.getString("state");
                        Log.i("state", "state is" + state);
                        JSONArray districtData = data1.getJSONArray("districtData");
                        for (int j = 0; j < districtData.length(); j++) {
                            Log.i("districtData", "DistrictData at index"+j+":::" + districtData.get(j));
                            JSONObject individualdistrict =  districtData.getJSONObject(j);
                            districtNames.add(individualdistrict.getString("district"));
                            if( (individualdistrict.getString("district").matches(location)))
                            {

                                state1=state;
                                final String district = individualdistrict.getString("district");

                                final int active = individualdistrict.getInt("active");
                                final int confirmed = individualdistrict.getInt("confirmed");
                                final int recovered = individualdistrict.getInt("recovered");
                                final int death = individualdistrict.getInt("deceased");

                                Log.i("district and active ","Result of search is:- "+district+"\n"+active+"\n"+confirmed+"\n"+recovered+"\n"+death);


                                getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {

                                                                dateTV.setText(date);
                                                                totalTV.setText(String.valueOf(confirmed));
                                                                deathTV.setText(String.valueOf(death));
                                                                districtTV.setText(district);
                                                                activeTV.setText(String.valueOf(active));
                                                                recoveredTV.setText(String.valueOf(recovered));


                                                            }
                                                        });

                            }




                        }
                    }
                }
                catch (Exception e)
                {e.printStackTrace();}


            }



        });
    }


    private void fetchDataZone() {
        httpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url("https://api.covid19india.org/zones.json")
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "An error has occurred " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // More code goes here
                //String location = URLEncoder.encode(input.getText().toString(), "UTF-8");
                try {
                    JSONObject data = new JSONObject(response.body().string());

                    JSONArray zones = data.getJSONArray("zones");
                    for (int i = 0; i < zones.length(); i++) {

                        JSONObject district = zones.getJSONObject(i);
                        if(district.getString("district").matches(location))
                        {
                            Log.i("zone is", "zone is" + district);
                            final String zone = district.getString("zone");

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    zoneresultTV.setText(zone);
                                }
                            });


                        }
                    }


                }
                catch (Exception e)
                {e.printStackTrace();}


            }



        });
    }
    private void fetchDataChange() {
        httpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url("https://api.covid19india.org/districts_daily.json")
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "An error has occurred " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // More code goes here
                //
                Date now = new Date();
                DateFormat sdf;
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = sdf.format(now);
                System.out.println("Formatted date in yyyy-MM-dd is:"+strDate );
                try {
                    JSONObject data = new JSONObject(response.body().string());

                    JSONObject districtDaily = data.getJSONObject("districtsDaily");
                    for (int i = 0; i < districtDaily.length(); i++) {
                        JSONObject state = districtDaily.getJSONObject(state1);
                        Log.i("state ","state is"+state1);
                        JSONArray district = state.getJSONArray(location);
                        Log.i("districtArray ","districtarray data is"+district);
                        for(int j=0;j<district.length();j++)
                        {
                            JSONObject dailydata = district.getJSONObject(j);

                            Log.i("districtdataObject ","districtObject data is"+dailydata);

                            if((dailydata.getString("date").matches(strDate)))
                            {
                                JSONObject dailydataprevious=district.getJSONObject(j-1);
                                int tempdelconf=dailydataprevious.getInt("active");
                                int tempdeldeath=dailydataprevious.getInt("deceased");
                                int tempdelreco=dailydataprevious.getInt("recovered");
                                final int deltaconfirmed= dailydata.getInt("active")-tempdelconf;
                                final int deltadeceased = dailydata.getInt("deceased")-tempdeldeath;
                                final int deltarecovered = dailydata.getInt("recovered")-tempdelreco;
                                Log.i("delta ","delta is:- "+deltaconfirmed+"\n"+deltadeceased+"\n"+deltarecovered);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(deltaconfirmed > 0)
                                        {
                                            deltaconfirmedTV.setText(String.valueOf(deltaconfirmed));
                                            deltaconfirmedTV.setVisibility(View.VISIBLE);
                                        }
                                        if(deltadeceased >0)
                                        {
                                            deltadeathTV.setText(String.valueOf(deltadeceased));
                                            deltadeathTV.setVisibility(View.VISIBLE);
                                        }
                                        if(deltarecovered >0)
                                        {
                                            deltarecoveredTV.setText(String.valueOf(deltarecovered));
                                            deltarecoveredTV.setVisibility(View.VISIBLE);
                                        }

                                    }
                                });

                            }



                            }
                        }

                    }

                catch (Exception e)
                {e.printStackTrace();}


            }



        });
    }


}

