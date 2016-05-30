package com.example.apaul5.nytimessearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by apaul5 on 5/28/16.
 */
public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, ArrayList<Article> articles){
        super(context, android.R.layout.simple_list_item_1,articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Article article = this.getItem(position);

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.imagelayout, parent, false);
        }
        TextView tvHeadline = (TextView) convertView.findViewById(R.id.tvTitle);
        ImageView ivThumbImage = (ImageView) convertView.findViewById(R.id.ivImage);

        tvHeadline.setText(article.getNewsHeadline());

        ivThumbImage.setImageResource(0);
        String thumbnail = article.getNewsThumbImage();

        if(!thumbnail.isEmpty()){
            Picasso.with(getContext()).load(thumbnail).into(ivThumbImage);
        }
        return convertView;
    }
}
