package com.developer.verifygpt.Model;

import java.util.Map;

public class HistoryModel {
     public String id, mandatoryKeywordsCount, preferredKeywordsCount, notIncludedKeywordsCount, totalMandatory, totalPreferred, mandatoryKeywordsScore, preferredKeywordsScore, totalScore, question, description;

    public HistoryModel(String id, String mandatoryKeywordsCount, String preferredKeywordsCount, String notIncludedKeywordsCount, String totalScore, String question, String description) {
        this.id = id;
        this.mandatoryKeywordsCount = mandatoryKeywordsCount;
        this.preferredKeywordsCount = preferredKeywordsCount;
        this.notIncludedKeywordsCount = notIncludedKeywordsCount;
        this.totalScore = totalScore;
        this.question = question;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMandatoryKeywordsCount() {
        return mandatoryKeywordsCount;
    }

    public void setMandatoryKeywordsCount(String mandatoryKeywordsCount) {
        this.mandatoryKeywordsCount = mandatoryKeywordsCount;
    }

    public String getPreferredKeywordsCount() {
        return preferredKeywordsCount;
    }

    public void setPreferredKeywordsCount(String preferredKeywordsCount) {
        this.preferredKeywordsCount = preferredKeywordsCount;
    }

    public String getNotIncludedKeywordsCount() {
        return notIncludedKeywordsCount;
    }

    public void setNotIncludedKeywordsCount(String notIncludedKeywordsCount) {
        this.notIncludedKeywordsCount = notIncludedKeywordsCount;
    }

    public String getTotalMandatory() {
        return totalMandatory;
    }

    public void setTotalMandatory(String totalMandatory) {
        this.totalMandatory = totalMandatory;
    }

    public String getTotalPreferred() {
        return totalPreferred;
    }

    public void setTotalPreferred(String totalPreferred) {
        this.totalPreferred = totalPreferred;
    }



    public String getMandatoryKeywordsScore() {
        return mandatoryKeywordsScore;
    }

    public void setMandatoryKeywordsScore(String mandatoryKeywordsScore) {
        this.mandatoryKeywordsScore = mandatoryKeywordsScore;
    }

    public String getPreferredKeywordsScore() {
        return preferredKeywordsScore;
    }

    public void setPreferredKeywordsScore(String preferredKeywordsScore) {
        this.preferredKeywordsScore = preferredKeywordsScore;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HistoryModel() {
    }

    public HistoryModel(String id, String mandatoryKeywordsCount, String preferredKeywordsCount, String notIncludedKeywordsCount, String totalMandatory, String totalPreferred, String mandatoryKeywordsScore, String preferredKeywordsScore, String totalScore, String question, String description) {
        this.id = id;
        this.mandatoryKeywordsCount = mandatoryKeywordsCount;
        this.preferredKeywordsCount = preferredKeywordsCount;
        this.notIncludedKeywordsCount = notIncludedKeywordsCount;
        this.totalMandatory = totalMandatory;
        this.totalPreferred = totalPreferred;
        this.mandatoryKeywordsScore = mandatoryKeywordsScore;
        this.preferredKeywordsScore = preferredKeywordsScore;
        this.totalScore = totalScore;
        this.question = question;
        this.description = description;
    }


}
