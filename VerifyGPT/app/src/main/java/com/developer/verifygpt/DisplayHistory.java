package com.developer.verifygpt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.verifygpt.Adapter.HistoryAdapter;
import com.developer.verifygpt.Model.HistoryModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisplayHistory extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Integer> resultMap = new HashMap<>();
    ImageView btn_back;
    TextView tv_result;
    TextView keyTextView, valueTextView;
    ImageView back;
    RecyclerView rv_history;
    TableRow row;
    HistoryAdapter adapter;
    ArrayList<HistoryModel> reportData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_history);
        rv_history = findViewById(R.id.rv_history);
        back = findViewById(R.id.back);
        adapter = new HistoryAdapter(DisplayHistory.this, reportData);

        db.collection("GPTReport")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String id = documentSnapshot.getId(); // Get the document ID if needed
                        String description = documentSnapshot.getString("description");
                        String question = documentSnapshot.getString("question");
                        String mandatoryKeywordsCount = documentSnapshot.getString("mandatoryKeywordsScore");
                        String preferredKeywordsCount = documentSnapshot.getString("preferredKeywordsCount");
                        String notIncludedKeywordsCount = documentSnapshot.getString("notIncludedKeywordsCount");
                        String totalMandatory = documentSnapshot.getString("totalMandatory");
                        String totalPreferred = documentSnapshot.getString("totalPreferred");
                        String mandatoryKeywordsScore = documentSnapshot.getString("mandatoryKeywordsScore");
                        String preferredKeywordsScore = documentSnapshot.getString("preferredKeywordsScore");
                        String totalScore = documentSnapshot.getString("totalScore");
                        HistoryModel model = new HistoryModel( id, mandatoryKeywordsCount, preferredKeywordsCount, notIncludedKeywordsCount, totalMandatory, totalPreferred, mandatoryKeywordsScore, preferredKeywordsScore, totalScore, question, description);
                        reportData.add(model);
                        rv_history.setAdapter(adapter);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error getting documents: " + e.getMessage());
                });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_history.setLayoutManager(layoutManager);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}