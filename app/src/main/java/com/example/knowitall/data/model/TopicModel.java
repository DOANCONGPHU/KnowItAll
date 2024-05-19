package com.example.knowitall.data.model;

public class TopicModel {
    private String topicName,topicImage,key;
    int setNum;


    public TopicModel(String topicName, String topicImage, String key, int setNum) {
        this.topicName = topicName;
        this.topicImage = topicImage;
        this.key = key;
        this.setNum = setNum;
    }

    public TopicModel() {
    }

    public String getTopicName() {
        return topicName;
    }

    public String getTopicImage() {
        return topicImage;
    }

    public String getKey() {
        return key;
    }

    public int getSetNum() {
        return setNum;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setTopicImage(String topicImage) {
        this.topicImage = topicImage;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setSetNum(int setNum) {
        this.setNum = setNum;
    }
}
