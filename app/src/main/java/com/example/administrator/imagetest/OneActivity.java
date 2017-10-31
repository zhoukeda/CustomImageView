package com.example.administrator.imagetest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by dada on 2017/10/31.
 */

public class OneActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_one);
        MyImageView myImageView = (MyImageView) findViewById(R.id.iv);
        ScratchCardView cardView = (ScratchCardView) findViewById(R.id.iv2);
        switch (ImageType.type){
            case "0":
                myImageView.setScale(true);
                break;
            case "1":
                myImageView.setCircle(true);
                break;
            case "2":
                myImageView.setBounds(true,80);
                break;
            case "3":
                myImageView.setVisibility(View.GONE);
                cardView.setVisibility(View.VISIBLE);
                break;
        }
    }
}
