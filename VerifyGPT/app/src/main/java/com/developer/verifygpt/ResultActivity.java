package com.developer.verifygpt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {
    ImageView btn_back;
    TextView tv_description, tv_question, tv_mandatoryFound, preferredFound, notIncludedFound, tv_totalScore, tv_authentic, tv_avg, tv_poor;
    Button btn_viewHistory;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        btn_back = findViewById(R.id.btn_back);
        tv_question = findViewById(R.id.tv_question);
        tv_description = findViewById(R.id.tv_description);
        tv_mandatoryFound = findViewById(R.id.tv_mandatoryFound);
        preferredFound = findViewById(R.id.preferredFound);
        notIncludedFound = findViewById(R.id.notIncludedFound);
        tv_totalScore = findViewById(R.id.tv_totalScore);
        tv_authentic = findViewById(R.id.tv_authentic);
        tv_avg = findViewById(R.id.tv_avg);
        tv_poor = findViewById(R.id.tv_poor);
        btn_viewHistory = findViewById(R.id.btn_viewHistory);
        Intent intent = getIntent();
        String description = intent.getStringExtra("description");
        String question = intent.getStringExtra("question");
        String mandatoryKeywordsCount = intent.getStringExtra("mandatoryKeywordsScore");
        String preferredKeywordsCount = intent.getStringExtra("preferredKeywordsCount");
        String notIncludedKeywordsCount = intent.getStringExtra("notIncludedKeywordsCount");
        String totalMandatory = intent.getStringExtra("totalMandatory");
        String totalPreferred = intent.getStringExtra("totalPreferred");
        String mandatoryKeywordsScore = intent.getStringExtra("mandatoryKeywordsScore");
        String preferredKeywordsScore = intent.getStringExtra("preferredKeywordsScore");
        String totalScore = intent.getStringExtra("totalScore");
        score = convertScoreToInt(totalScore);
        tv_question.setText(question);
        tv_description.setText(description);
        tv_mandatoryFound.setText(mandatoryKeywordsScore);
        preferredFound.setText(preferredKeywordsScore);
        notIncludedFound.setText(notIncludedKeywordsCount);
        tv_totalScore.setText(totalScore);
        if (score >= 70 && score <= 100) {
           tv_authentic.setVisibility(View.VISIBLE);
            tv_avg.setVisibility(View.GONE);
            tv_poor.setVisibility(View.GONE);
        } else if (score >= 50 && score <= 69) {
            tv_avg.setVisibility(View.VISIBLE);
            tv_authentic.setVisibility(View.GONE);
            tv_poor.setVisibility(View.GONE);
        } else if (score <= 49) {
            tv_poor.setVisibility(View.VISIBLE);
            tv_authentic.setVisibility(View.GONE);
            tv_avg.setVisibility(View.GONE);
        } else {
            tv_poor.setVisibility(View.GONE);
            tv_authentic.setVisibility(View.GONE);
            tv_avg.setVisibility(View.GONE);
        }
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_viewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DisplayHistory.class));
            }
        });
    }
    private int convertScoreToInt(String scoreString) {
        try {
            double scoreDouble = Double.parseDouble(scoreString);
            // Truncate the decimal part and convert to int
            System.out.println("VAL: "+scoreDouble);
            return (int) scoreDouble; // or use (int) Math.floor(scoreDouble);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.e("ScoreConversion", "Failed to parse score: " + scoreString);
            return 0; // Default value or some indication of failure
        }
    }
}