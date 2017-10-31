package com.example.administrator.imagetest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dada on 2017/10/31.
 */

public class OneActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_one);
        MyImageView myImageView = (MyImageView) findViewById(R.id.iv);
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
        }
    }
}
