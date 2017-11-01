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
        final MyImageView myImageView = (MyImageView) findViewById(R.id.iv);
        final ScratchCardView cardView = (ScratchCardView) findViewById(R.id.iv2);
        findViewById(R.id.btn_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myImageView.returnFirst();
                cardView.returnFirst();
            }
        });
        switch (ImageType.type){
            case "0":
                myImageView.setScale(true);
//                myImageView.setBounds(true,80);
//                myImageView.setRoate(true);
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
            case "4":
                myImageView.setRoate(true);
                break;
        }

    }
}
