package ru.company.testing;

import java.util.*;

public class Verification {


    //Метод для проверки теста после окончания
    static int countTrueAnswers(Student student) {
        Test test = student.getTest();
        List<Question> questionsList = test.getQuestionsList();
        String[] answerStudents = student.getAnswers();

        int trueResult = 0;
        for (int i = 0; i < questionsList.size(); i++) {
            String trueAnswer = questionsList.get(i).getTrueAnswer();
            if (trueAnswer.equalsIgnoreCase(answerStudents[i])) {
                trueResult++;
            }

        }
        return trueResult;
    }

    static List<String> checkAnswers(List<String> listAnswers, List<String> listTrueAnswers){
        List<String> resultList = new ArrayList<>();
        for (int i = 0; i <listTrueAnswers.size() ; i++) {
            if (listAnswers.get(i) != null && !"".equalsIgnoreCase(listAnswers.get(i).trim())){
                resultList.add( (listTrueAnswers.get(i).equals(listAnswers.get(i))) ? "1" : "0");
            }else{
                resultList.add("");
            }

        }
        return resultList;
    }

}




