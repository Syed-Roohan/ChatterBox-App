package com.developer.verifygpt.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.verifygpt.Model.HistoryModel;
import com.developer.verifygpt.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryVH> {
    Context context;
    int score;
    public HistoryAdapter(Context context, ArrayList<HistoryModel> historyModels) {
        this.context = context;
        this.historyModels = historyModels;
    }

    ArrayList<HistoryModel>  historyModels  = new ArrayList<HistoryModel>();
    @NonNull
    @Override
    public HistoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_history_layout, parent, false);
        return new HistoryVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryVH holder, int position) {
        HistoryModel model = historyModels.get(position);
        holder.tv_question.setText(historyModels.get(position).getQuestion());
        holder.tv_description.setText(historyModels.get(position).getDescription());
        holder.tv_mandatoryFound.setText(historyModels.get(position).mandatoryKeywordsScore);
        holder.preferredFound.setText(historyModels.get(position).preferredKeywordsScore);
        holder.notIncludedFound.setText(historyModels.get(position).notIncludedKeywordsCount);
        holder.tv_totalScore.setText(historyModels.get(position).totalScore);
        score = convertScoreToInt(historyModels.get(position).totalScore);
        System.out.println("SCORE "+historyModels.get(position).totalScore);
        if (score >= 70 && score <= 100) {
            holder.tv_authentic.setVisibility(View.VISIBLE);
            holder.tv_avg.setVisibility(View.GONE);
            holder.tv_poor.setVisibility(View.GONE);
        } else if (score >= 50 && score <= 69) {
            holder.tv_authentic.setVisibility(View.GONE);
            holder.tv_avg.setVisibility(View.VISIBLE);
            holder.tv_poor.setVisibility(View.GONE);
        } else if (score <= 49) {
            holder.tv_poor.setVisibility(View.VISIBLE);
            holder.tv_authentic.setVisibility(View.GONE);
            holder.tv_avg.setVisibility(View.GONE);
        } else {
            holder.tv_poor.setVisibility(View.GONE);
            holder.tv_authentic.setVisibility(View.GONE);
            holder.tv_avg.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return historyModels.size();
    }

    public class HistoryVH extends RecyclerView.ViewHolder{
        TextView tv_totalScore, tv_mandatoryFound, preferredFound, notIncludedFound,  tv_question, tv_description;
        TextView tv_authentic, tv_avg, tv_poor;
        public HistoryVH(@NonNull View itemView) {
            super(itemView);
            tv_totalScore = itemView.findViewById(R.id.tv_totalScore);
            tv_mandatoryFound = itemView.findViewById(R.id.tv_mandatoryFound);
            preferredFound = itemView.findViewById(R.id.preferredFound);
            notIncludedFound = itemView.findViewById(R.id.notIncludedFound);
            tv_question = itemView.findViewById(R.id.tv_question);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_authentic = itemView.findViewById(R.id.tv_authentic);
            tv_avg = itemView.findViewById(R.id.tv_avg);
            tv_poor = itemView.findViewById(R.id.tv_poor);

        }
    }


    // Convert dataMap to a String

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
