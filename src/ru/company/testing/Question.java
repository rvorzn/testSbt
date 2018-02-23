package ru.company.testing;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class Question implements Serializable {
    private String question;
    private String description;
    private File image;
    private TreeMap<Integer, String>  answerСhoice = new TreeMap<>();
    private String trueAndswer;


    //sets--------------------------------------------------------------
    public void setTrueAndswer(String trueAndswer) {
        this.trueAndswer = trueAndswer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAnswerСhoice(String answerСhoice){
        int count =  this.answerСhoice.size()+1;
        this.answerСhoice.put(count, answerСhoice);
    }

    // gets ----------------------------------------------------------


    public String getQuestion() {
        return question;
    }

    public String getDescription() {
        return description;
    }

    public String getTrueAnswer() {
        return trueAndswer;
    }

    public TreeMap<Integer, String> getAnswerСhoice() {
        return answerСhoice;
    }

    public String getAnsver(){
        String stringAnser= "";
        for (  Map.Entry<Integer, String> answer : answerСhoice.entrySet()) {
            stringAnser += answer.getKey()+ ") "  +answer.getValue() +"\n";
        }
        return stringAnser;

    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", description='" + description + '\'' +
                ", image=" + image +
                ", answerСhoice=" + answerСhoice +
                ", trueAndswer='" + trueAndswer + '\'' +
                '}';
    }
}
