package ru.company.testing;

import java.util.*;

public class Verification {

    static String checkAnswers(Student student){
        HashMap<Integer, Boolean> result = new HashMap<>();
        Test test = student.getTest();
        ArrayList<Question> questionsList = test.getQuestionsList();
        HashMap<Integer, String> mapAnswerStudents = student.getAnswers();
        Set<Integer> keySet = mapAnswerStudents.keySet();

        int trueResult = 0;
        for (int key: keySet) {
            String trueAnsewer = questionsList.get(key).getTrueAnswer();
            if (trueAnsewer.equals(mapAnswerStudents.get(key))){
                result.put(key, true);
                trueResult++;
            } else {
                result.put(key, false);
            }

        }
        return  trueResult + "";
    }

    static List<String> checkAnswers(List<String> listAnswers, List<String> listTrueAnswers){
        List<String> resultList = new ArrayList<>();
        for (int i = 0; i <listTrueAnswers.size() ; i++) {
            if (!"".equalsIgnoreCase(listAnswers.get(i).trim())){
                resultList.add( (listTrueAnswers.get(i).equals(listAnswers.get(i))) ? "1" : "0");
            }else{
                resultList.add("");
            }

        }
        return resultList;
    }

}




