package com.example.coronacrux.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coronacrux.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class newsRecyclerAdapter extends RecyclerView.Adapter<newsRecyclerAdapter.newsDataViewHolder> {
    private ArrayList<String> mtitle ;
    private ArrayList<String> mDesc ;
    private ArrayList<String> mImageLink ;
    private ArrayList<String> mSource ;
    private ArrayList<String> mPublish ;

    private Context mcontext;

    public newsRecyclerAdapter(Context contxt, ArrayList<String> title,ArrayList<String> Desc,ArrayList<String> imagelink,ArrayList<String> sourcce,ArrayList<String> publish) {
        this.mtitle = title;
        this.mDesc = Desc;
        this.mcontext=contxt;
        this.mImageLink=imagelink;
        this.mSource=sourcce;
        this.mPublish=publish;

    }

    @NonNull
    @Override
    public newsDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newsrecycleritemdesign, parent, false);
        newsDataViewHolder nn = new newsDataViewHolder(view);
        return nn;
    }

    @Override
    public void onBindViewHolder(@NonNull newsDataViewHolder holder, int position) {
        holder.newstitle.setText(mtitle.get(position));
        Log.i("Data","Printes is "+mtitle.get(position));
        holder.newsDesc.setText(mDesc.get(position));
        String image = mImageLink.get(position);
        holder.setImage(image);
        holder.newsSource.setText(mSource.get(position));
        holder.publish.setText(mPublish.get(position));


    }

    @Override
    public int getItemCount() {


        return mtitle.size();
    }

    public static  class newsDataViewHolder extends  RecyclerView.ViewHolder
    {
        TextView newstitle;
        TextView newsDesc,newsSource,publish;
        ImageView imageView;

        public newsDataViewHolder(@NonNull View itemView) {
            super(itemView);
            newsSource=itemView.findViewById(R.id.newsitemrecycler_SourceTV);
            newstitle=itemView.findViewById(R.id.newsitemrecycler_titleTV);
            newsDesc = itemView.findViewById(R.id.newsitemrecycler_DescTV);
            publish=itemView.findViewById(R.id.newsitemrecycler_PublishTimeTV);


        }
        public void setImage(final String image)
        {
            imageView =itemView.findViewById(R.id.newsitemimage);

            Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(image).into(imageView);

                }
            });

        }
    }
}
