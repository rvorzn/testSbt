package ru.company.testing;

import java.io.Serializable;
import java.util.*;

public class Test implements Serializable {
    private String title;
    private String description;
    private  ArrayList<Question> questionsList = new ArrayList<>();
    private int time;
    private int level;

    // constructors
    Test(String title){
        this.title = title;
    }

    public ArrayList<Question> getQuestionsList() {
        return questionsList;
    }

    public void changeQuestion(int key, Question question){
        questionsList.set(key, question);

    }

    public String getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }

    public int getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addQuestion(Question question){
        questionsList.add(question);
    }

    public Question getQestion(int key){
        return questionsList.get(key);
    }

    public void removeQuestion(int key){
        questionsList.remove(key);
    }

    public void addAllQuestion(ArrayList<Question> listQuestion){
        questionsList = listQuestion;
    }
}
