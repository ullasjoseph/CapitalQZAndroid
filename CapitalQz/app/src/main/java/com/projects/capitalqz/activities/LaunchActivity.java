package com.projects.capitalqz.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.projects.capitalqz.R;


public class LaunchActivity extends Activity implements View.OnClickListener {


    private Button buttonPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        if(view == buttonPlay) {
            Intent i = new Intent(this, QuizActivity.class);
            startActivity(i);
        }
    }
}
