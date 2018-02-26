package ru.company.testing;

import java.io.Serializable;


public class Student implements Serializable {
    private String fullName;
    private Test test;
    private String[] answers;

    public Student(String fullName){
        this.fullName = fullName;
    }

    public void setTest(Test test) {
        this.test = test;
        answers = new String[test.getQuestionsList().size()];
    }

    public void setAnswers(int key, String answer) {
        answers[key] = answer;
    }

    public Test getTest() {
        return test;
    }

    public String[] getAnswers() { return answers; }

    public String getFullName() {
        return fullName;
    }


}
