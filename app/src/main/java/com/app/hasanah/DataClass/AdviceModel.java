package com.app.hasanah.DataClass;

public class AdviceModel {
    private  String Advice;
    private String videoUrl;

    public AdviceModel(String advice, String videoUrl) {
        Advice = advice;
        this.videoUrl = videoUrl;
    }

    public String getAdvice() {
        return Advice;
    }

    public void setAdvice(String advice) {
        Advice = advice;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
