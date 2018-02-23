package ru.company.testing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Student implements Serializable {
    private String fullName;
    private Test test;
    private HashMap<Integer, String> answers = new HashMap<>();


    Student(String fullName){
        this.fullName = fullName;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public void setAnswers(int key, String answer) {
        answers.put(key, answer);
    }


    public Test getTest() {
        return test;
    }

    public HashMap<Integer, String> getAnswers() {
        return answers;
    }

    public String getFullName() {
        return fullName;
    }
}
