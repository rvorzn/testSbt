package ru.company.testing;

import java.io.Serializable;
import java.util.*;

public class Test implements Serializable {
    private String title;
    private String description;
    private  List <Question> questionsList = new ArrayList<>();
    private int time;
    private int level;

    // constructors -------------------------------
    Test(String title){
        this.title = title;
    }

    // Gets ------------------------------

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

    public List<Question> getQuestionsList() {
        return questionsList;
    }

    public void changeQuestion(int key, Question question){
        questionsList.set(key, question);
    }

    public Question getQestion(int key){
        return questionsList.get(key);
    }


    // Sets ------------------------------

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

    // metods -----------------------------

    public void addQuestion(Question question){
        questionsList.add(question);
    }

    public void removeQuestion(int key){
        questionsList.remove(key);
    }

    public void addAllQuestion(List<Question> listQuestion){
        questionsList = listQuestion;
    }
}
