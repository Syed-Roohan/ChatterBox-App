package com.developer.verifygpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {
    EditText edt_search, edt_description, edt_preferredKeywords, edt_notIncludedKeywords, edt_question;
    private ChipGroup chipGroup, chipGroup_preferred, chipGroup_notIncluded;
    HorizontalScrollView scrollView, scrollView_preferred, scrollView_notIncluded;
    Button btn_verify, btn_add, btn_save, btn_add_preferred, btn_add_notIncluded;
    ImageView btnClear, btnPaste;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String formattedMandatoryScore , formattedPreferredScore ,formattedTotalScore ;
    String question, description;
    TextView tv_visitReport;
    double mandatoryKeywordsCount, preferredKeywordsCount, notIncludedKeywordsCount;
    double totalMandatory, totalPreferred, totalNotIncluded;
    double mandatoryKeywordsScore, preferredKeywordsScore, totalScore;
    int count = 0;

    Map<String, Integer> keywordCounts = new HashMap<>();
    ArrayList<String> keywords = new ArrayList<>();
    ArrayList<String> preferredKeywords = new ArrayList<>();
    ArrayList<String> notIncludedKeywords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        scrollView_notIncluded = findViewById(R.id.scrollView_notIncluded);
        scrollView_preferred = findViewById(R.id.scrollView_preferred);
        btn_add_notIncluded = findViewById(R.id.btn_add_notIncluded);
        edt_notIncludedKeywords = findViewById(R.id.edt_notIncludedKeywords);
        btn_add_preferred = findViewById(R.id.btn_add_preferred);
        edt_preferredKeywords = findViewById(R.id.edt_preferredKeywords);
        edt_description = findViewById(R.id.edt_description);
        edt_search = findViewById(R.id.edt_search);
        btn_verify = findViewById(R.id.btn_verify);
        btnPaste = findViewById(R.id.btnPaste);
        btn_add = findViewById(R.id.btn_add);
        btnClear = findViewById(R.id.btnClear);
        btn_save = findViewById(R.id.btn_save);
        tv_visitReport = findViewById(R.id.tv_visitReport);
        scrollView = findViewById(R.id.scrollView);
        chipGroup = findViewById(R.id.chipGroup);
        chipGroup_preferred = findViewById(R.id.chipGroup_preferred);
        chipGroup_notIncluded = findViewById(R.id.chipGroup_notIncluded);
        edt_question = findViewById(R.id.edt_question);
// Mandaory Keys
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchedKeywords = edt_search.getText().toString();
                if (!searchedKeywords.isEmpty()) {
                    scrollView.setVisibility(View.VISIBLE);

                    if (searchedKeywords.contains(" ")) {
                        String[] words = searchedKeywords.split("\\s+");
                        for (String word : words) {
                            if (!searchedKeywords.isEmpty()) {
                                addChip(word.toLowerCase());
                                keywords.add(word.toLowerCase());
                                edt_search.setText("");
                            }
                        }
                    } else {
                        addChip(searchedKeywords.toLowerCase());
                        keywords.add(searchedKeywords.toLowerCase());
                        edt_search.setText("");
                    }
                }
            }
        });
        // Preferred Keys
        btn_add_preferred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchedPreferredKeywords = edt_preferredKeywords.getText().toString();
                if (!searchedPreferredKeywords.isEmpty()) {
                    scrollView_preferred.setVisibility(View.VISIBLE);

                    if (searchedPreferredKeywords.contains(" ")) {
                        String[] preferredWords = searchedPreferredKeywords.split("\\s+");
                        for (String word : preferredWords) {
                            if (!searchedPreferredKeywords.isEmpty()) {
                                addPreferredChip(word.toLowerCase());
                                preferredKeywords.add(word.toLowerCase());
                                edt_preferredKeywords.setText("");
                            }
                        }
                    } else {
                        addPreferredChip(searchedPreferredKeywords.toLowerCase());
                        preferredKeywords.add(searchedPreferredKeywords.toLowerCase());
                        edt_preferredKeywords.setText("");
                    }
                }
            }
        });
        // Not included Keys
        btn_add_notIncluded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchedNotIncludedKeywords = edt_notIncludedKeywords.getText().toString();
                if (!searchedNotIncludedKeywords.isEmpty()) {
                    scrollView_notIncluded.setVisibility(View.VISIBLE);

                    if (searchedNotIncludedKeywords.contains(" ")) {
                        String[] notIncludedWords = searchedNotIncludedKeywords.split("\\s+");
                        for (String word : notIncludedWords) {
                            if (!searchedNotIncludedKeywords.isEmpty()) {
                                addNotIncludedChip(word.toLowerCase());
                                notIncludedKeywords.add(word.toLowerCase());
                                edt_notIncludedKeywords.setText("");
                            }
                        }
                    } else {
                        addNotIncludedChip(searchedNotIncludedKeywords.toLowerCase());
                        notIncludedKeywords.add(searchedNotIncludedKeywords.toLowerCase());
                        edt_notIncludedKeywords.setText("");
                    }
                }
            }
        });
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GenerateReport();
                resetCounters();
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                intent.putExtra("question", edt_question.getText().toString());
                intent.putExtra("description", edt_description.getText().toString());
                intent.putExtra("mandatoryKeywordsCount", String.valueOf(mandatoryKeywordsCount));
                intent.putExtra("preferredKeywordsCount", String.valueOf(preferredKeywordsCount));
                intent.putExtra("notIncludedKeywordsCount", String.valueOf(notIncludedKeywordsCount));
                intent.putExtra("totalMandatory", String.valueOf(totalMandatory));
                intent.putExtra("totalPreferred", String.valueOf(totalPreferred));
                intent.putExtra("totalNotIncluded", String.valueOf(totalNotIncluded));
                intent.putExtra("mandatoryKeywordsScore", String.valueOf(formattedMandatoryScore));
                intent.putExtra("preferredKeywordsScore", String.valueOf(formattedPreferredScore));
                intent.putExtra("totalScore", String.valueOf(formattedTotalScore));
                if (!edt_search.getText().toString().isEmpty() | !edt_question.getText().toString().isEmpty() | !edt_preferredKeywords.getText().toString().isEmpty() | !edt_description.getText().toString().isEmpty() | !edt_notIncludedKeywords.getText().toString().isEmpty()){
                    startActivity(intent);
                }
                else{
                    edt_search.setError("Required");
                    edt_question.setError("Required");
                    edt_description.setError("Required");
                    edt_preferredKeywords.setError("Required");
                    edt_notIncludedKeywords.setError("Required");
                }
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenerateReport();
                Map<String, Object> data = new HashMap<>();
                data.put("question", edt_question.getText().toString());
                data.put("description", edt_description.getText().toString());
                data.put("mandatoryKeywordsCount", String.valueOf(mandatoryKeywordsCount));
                data.put("preferredKeywordsCount", String.valueOf(preferredKeywordsCount));
                data.put("notIncludedKeywordsCount", String.valueOf(notIncludedKeywordsCount));
                data.put("totalMandatory", String.valueOf(totalMandatory));
                data.put("totalPreferred", String.valueOf(totalPreferred));
                data.put("totalNotIncluded", String.valueOf(totalNotIncluded));
                data.put("mandatoryKeywordsScore", String.valueOf(formattedMandatoryScore));
                data.put("preferredKeywordsScore", String.valueOf(formattedPreferredScore));
                data.put("totalScore", String.valueOf(formattedTotalScore));
                db.collection("GPTReport").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DashboardActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();
                        edt_question.setText("");
                        edt_description.setText("");
                        edt_search.setText("");
                        edt_preferredKeywords.setText("");
                        edt_notIncludedKeywords.setText("");
                        chipGroup.clearCheck();
                        chipGroup_preferred.clearCheck();
                        chipGroup_notIncluded.clearCheck();
                        resetCounters();
                        startActivity(new Intent(getApplicationContext(), DisplayHistory.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DashboardActivity.this, "Data Not Saved", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_question.setText("");
                edt_description.setText("");
            }
        });
        btnPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboardManager != null && clipboardManager.hasPrimaryClip()) {
                    ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);
                    CharSequence pasteText = item.getText();
                    edt_description.setText(pasteText);
                } else {
                    Toast.makeText(DashboardActivity.this, "Clipboard is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_visitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DisplayHistory.class));
            }
        });
    }

    public void addChip(final String keyword) {
        LayoutInflater inflater = LayoutInflater.from(this);
        Chip chip = (Chip) inflater.inflate(R.layout.keyword_chip, chipGroup, false);
        chip.setText(keyword);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipGroup.removeView(view);
                keywords.remove(chip.getText().toString());
            }
        });
        chipGroup.addView(chip);
    }

    public void addPreferredChip(final String keyword) {
        LayoutInflater inflater = LayoutInflater.from(this);
        Chip preferredChip = (Chip) inflater.inflate(R.layout.keyword_chip, chipGroup, false);
        preferredChip.setText(keyword);
        preferredChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipGroup_preferred.removeView(view);
                preferredKeywords.remove(preferredChip.getText().toString());
            }
        });
        chipGroup_preferred.addView(preferredChip);
    }

    public void addNotIncludedChip(final String keyword) {
        LayoutInflater inflater = LayoutInflater.from(this);
        Chip notIncludedChip = (Chip) inflater.inflate(R.layout.keyword_chip, chipGroup, false);
        notIncludedChip.setText(keyword);
        notIncludedChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipGroup_notIncluded.removeView(view);
                notIncludedKeywords.remove(notIncludedChip.getText().toString());
            }
        });
        chipGroup_notIncluded.addView(notIncludedChip);
    }

    public int isKeyword() {
        String description = edt_description.getText().toString();
        for (String keyword : keywords) {
            if (description.contains(keyword.toLowerCase())) {
                count++;
            }
        }
        return count;
    }
    public void GenerateReport(){
        // Safety Situation
        removeUnderscores(keywords);
        removeUnderscores(preferredKeywords);
        removeUnderscores(notIncludedKeywords);
        removeDuplicates(keywords);
        removeDuplicates(preferredKeywords);
        removeDuplicates(notIncludedKeywords);
        question = edt_question.getText().toString();
        description = edt_description.getText().toString();
        String[] words = description.split(" ");
//                for (String key : keywords) {
//                    System.out.println("KEY **"+key);
//                    keywordCounts.put(key, 0);
//                }
        for (String word : words) {
            for (String key : keywords) {
                if (word.toLowerCase().equals(key.toLowerCase())) {
                    mandatoryKeywordsCount++;
//                            keywordCounts.put(key, keywordCounts.get(key) + 1);
                }

            }
        }
        for (String word : words) {
            for (String key : preferredKeywords) {
                if (word.toLowerCase().equals(key.toLowerCase())) {
//                            keywordCounts.put(key, keywordCounts.get(key) + 1);
                    preferredKeywordsCount++;
                }
            }
        }
        for (String word : words) {
            for (String key : notIncludedKeywords) {
                if (word.toLowerCase().equals(key.toLowerCase())) {
                    notIncludedKeywordsCount++;
//                            keywordCounts.put(key, keywordCounts.get(key) + 1);
                }
            }
        }
        totalMandatory = keywords.size(); // mandatory list size
        totalPreferred = preferredKeywords.size(); // mandatory list size
        totalNotIncluded = notIncludedKeywords.size(); // mandatory list size
//        mandatoryKeywordsScore = ((mandatoryKeywordsCount/totalMandatory)*70);
//        preferredKeywordsScore = ((preferredKeywordsCount/totalPreferred)*30);
//        totalScore = ((mandatoryKeywordsScore+preferredKeywordsScore)-notIncludedKeywordsCount);
        mandatoryKeywordsScore = Math.min((mandatoryKeywordsCount * 100.0 / totalMandatory), 100.0);
        preferredKeywordsScore = Math.min((preferredKeywordsCount * 100.0 / totalPreferred), 100.0);
        notIncludedKeywordsCount = Math.min(notIncludedKeywordsCount, totalMandatory); // Ensure this count doesn't exceed mandatory keywords

        // Calculate total score based on a combination of mandatory and preferred scores
        totalScore = ((mandatoryKeywordsScore * 0.7) + (preferredKeywordsScore * 0.3)) - notIncludedKeywordsCount;

        // Normalize the total score to ensure it's within 100%
        if (totalScore > 100.0) {
            totalScore = 100.0; // Cap the total score at 100%
        } else if (totalScore < 0.0) {
            totalScore = 0.0; // Ensure the total score doesn't go negative
        }
        DecimalFormat df = new DecimalFormat("#.##"); // Adjust the pattern to your required decimal places
        formattedMandatoryScore = df.format(mandatoryKeywordsScore);
        formattedPreferredScore = df.format(preferredKeywordsScore);
        formattedTotalScore = df.format(totalScore);
        Toast.makeText(this, "Mandatory: "+mandatoryKeywordsScore, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Preferred: "+preferredKeywordsScore, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "NotIncluded: "+notIncludedKeywordsCount, Toast.LENGTH_SHORT).show();


    }
    public static void removeDuplicates(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            String currentElement = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                if (currentElement.equals(list.get(j))) {
                    // Remove duplicate element
                    list.remove(j);
                    j--;
                }
            }
        }
    }
    public void resetCounters(){

        mandatoryKeywordsCount = 0;
        preferredKeywordsCount = 0;
        notIncludedKeywordsCount = 0;
        totalMandatory = 0;
        totalPreferred = 0;
        totalNotIncluded = 0;
        mandatoryKeywordsScore = 0;
        preferredKeywordsScore = 0;
        totalScore = 0;
    }
    public static void removeUnderscores(ArrayList<String> keywords) {
        for (int i = 0; i < keywords.size(); i++) {
            String originalKeyword = keywords.get(i);
            String formattedKeyword = originalKeyword.replace("_", "");
            keywords.set(i, formattedKeyword);
        }
    }
}