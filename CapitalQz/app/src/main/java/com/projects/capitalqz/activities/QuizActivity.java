package com.projects.capitalqz.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projects.capitalqz.R;
import com.projects.capitalqz.utils.CountryDetailsLoader;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by vaisakhprakash on 18/07/14.
 */
public class QuizActivity extends Activity implements View.OnClickListener {


    private TextView question;
    private TextView textViewOptionA;
    private TextView textViewOptionB;
    private TextView textViewOptionC;
    private TextView textViewOptionD;
    private RelativeLayout optionA;
    private RelativeLayout optionB;
    private RelativeLayout optionC;
    private RelativeLayout optionD;
    private static ArrayList<JSONObject> countryDetailsList;
    private JSONObject selectedQuestion;
    private ArrayList<JSONObject> options;
    private static final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();
    private boolean answerSelected;
    private int count = 0;
    private int currect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        question = (TextView) findViewById(R.id.question);
        textViewOptionA = (TextView) findViewById(R.id.textViewOptionA);
        textViewOptionB = (TextView) findViewById(R.id.textViewOptionB);
        textViewOptionC = (TextView) findViewById(R.id.textViewOptionC);
        textViewOptionD = (TextView) findViewById(R.id.textViewOptionD);

        optionA = (RelativeLayout) findViewById(R.id.optionA);
        optionB = (RelativeLayout) findViewById(R.id.optionB);
        optionC = (RelativeLayout) findViewById(R.id.optionC);
        optionD = (RelativeLayout) findViewById(R.id.optionD);
        optionA.setOnClickListener(this);
        optionB.setOnClickListener(this);
        optionC.setOnClickListener(this);
        optionD.setOnClickListener(this);

        countryDetailsList = CountryDetailsLoader.getCountryDetails(getBaseContext());
        if(countryDetailsList != null)
            loadRandomQuestion();

    }


    @Override
    public void onClick(View view) {

        if (answerSelected)
            return;
        answerSelected = true;
        if (view == optionA) {
            checkSelectedAnswer((String)textViewOptionA.getText(), view);
        } else if (view == optionB) {
            checkSelectedAnswer((String)textViewOptionB.getText(), view);
        } else if (view == optionC) {
            checkSelectedAnswer((String)textViewOptionC.getText(), view);
        } else if (view == optionD) {
            checkSelectedAnswer((String)textViewOptionD.getText(), view);
        }
    }

    private void clearAnimation() {
        optionA.setBackgroundColor(getResources().getColor(R.color.answr_options));
        optionB.setBackgroundColor(getResources().getColor(R.color.answr_options));
        optionC.setBackgroundColor(getResources().getColor(R.color.answr_options));
        optionD.setBackgroundColor(getResources().getColor(R.color.answr_options));

        optionA.clearAnimation();
        optionB.clearAnimation();
        optionC.clearAnimation();
        optionD.clearAnimation();
    }
    private void loadRandomQuestion() {

        System.out.println("--------------------------------------");

        clearAnimation();

        answerSelected = false;
        Collections.shuffle(countryDetailsList);
        selectedQuestion = countryDetailsList.get(0);

        String name = (String) selectedQuestion.get("name");
        String capital = (String) selectedQuestion.get("capital");

        if(name.length() == 0 || capital.length() == 0)
            loadRandomQuestion();

        question.setText("Which is the capital of "+ name + "?");

        options = new ArrayList<JSONObject>(4);
        for (int i = 0; i < 4; i++) {
            options.add(countryDetailsList.get(i));

            String optionName = (String) countryDetailsList.get(i).get("name");
            String optioncapital = (String) countryDetailsList.get(i).get("capital");

            if(optionName.length() == 0 || optioncapital.length() == 0)
                loadRandomQuestion();
        }
        Collections.shuffle(options);

        textViewOptionA.setText((CharSequence) (options.get(0)).get("capital"));
        textViewOptionB.setText((CharSequence) (options.get(1)).get("capital"));
        textViewOptionC.setText((CharSequence) (options.get(2)).get("capital"));
        textViewOptionD.setText((CharSequence) (options.get(3)).get("capital"));
        count++;

        System.out.println("selectedQuestion name :"+selectedQuestion.get("name"));
        System.out.println("selectedQuestion capital :"+selectedQuestion.get("capital"));
        for (JSONObject object : options){
            System.out.println("options capital : "+object.get("capital"));
        }
    }

    private void checkSelectedAnswer(String answer, View viewSelected) {

        String currectAnswr = (String) selectedQuestion.get("capital");
        View view = null;
        if (currectAnswr.equals(answer)) {
            view = viewSelected;
            currect++;
        } else {
            viewSelected.setBackgroundColor(Color.RED);
            if (options.get(0).get("capital").equals(currectAnswr))
                view = optionA;
            else if((options.get(1).get("capital").equals(currectAnswr)))
                view = optionB;
            else if((options.get(2).get("capital").equals(currectAnswr)))
                view = optionC;
            else if((options.get(3).get("capital").equals(currectAnswr)))
                view = optionD;
        }
        if (view == null) {
            loadRandomQuestion();
            return;
        }

        view.setBackgroundColor(Color.GREEN);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        view.startAnimation(anim);

        Runnable task = null;
        if (count == 10){
            task = new Runnable() {
                public void run() {
                    QuizActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            showFinishMessage();
                        }
                    });
                }
            };

        } else {
            task = new Runnable() {
                public void run() {
                    loadRandomQuestion();
                }
            };
        }
        worker.schedule(task, 2, TimeUnit.SECONDS);
    }

    private void showFinishMessage() {

        clearAnimation();
        AlertDialog alertDialog = new AlertDialog.Builder(
                QuizActivity.this).create();

        // Seting Dialog Title
        if(currect > count/2){
            alertDialog.setTitle("Congratzzz..!");
        } else {
            alertDialog.setTitle("Opsss..!");
        }
        alertDialog.setMessage(currect+" out of "+count+" are currect");

        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

        alertDialog.setButton("End", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
