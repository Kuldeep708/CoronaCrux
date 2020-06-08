package com.example.coronacrux.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coronacrux.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class abouusfragment extends Fragment {

    public abouusfragment() {
        // Required empty public constructor
    }
    TextView kd,appy,ashok,kraj;
    String url1="https://www.linkedin.com/in/kuldeep-meena-26b1a6191/";
    String url2="https://www.linkedin.com/in/kraj0291";
    String url3="https://www.linkedin.com/in/ashok-chouhan-3473b9165";
    String url4="https://www.linkedin.com/in/apoorva-panwar-7baa76184";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_abouusfragment, container, false);
        kd=view.findViewById(R.id.kuldeep);
        kraj=view.findViewById(R.id.kraj);
        ashok=view.findViewById(R.id.ashok);
        appy=view.findViewById(R.id.appy);
        kd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openURL = new Intent(Intent.ACTION_VIEW);
                openURL.setData(Uri.parse(url1));
                startActivity(openURL);
            }
        });
        kraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openURL = new Intent(Intent.ACTION_VIEW);
                openURL.setData(Uri.parse(url2));
                startActivity(openURL);
            }
        });
        ashok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openURL = new Intent(Intent.ACTION_VIEW);
                openURL.setData(Uri.parse(url3));
                startActivity(openURL);
            }
        });
        appy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openURL = new Intent(Intent.ACTION_VIEW);
                openURL.setData(Uri.parse(url4));
                startActivity(openURL);
            }
        });
            return view;
    }

}
