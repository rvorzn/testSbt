package ru.company.testing.utils;

import java.util.*;

public class Verification {


    //Метод для проверки теста после окончания
//    static int countTrueAnswers(Student student) {
//        Test test = student.getTest();
//        List<Question> questionsList = test.getQuestionsList();
//        String[] answerStudents = student.getAnswers();
//
//        int trueResult = 0;
//        for (int i = 0; i < questionsList.size(); i++) {
//            String trueAnswer = questionsList.get(i).getTrueAnswer();
//            if (trueAnswer.equalsIgnoreCase(answerStudents[i])) {
//                trueResult++;
//            }
//
//        }
//        return trueResult;
//    }
//
//    static List<String> checkAnswers(List<String> listAnswers, List<String> listTrueAnswers){
//        List<String> resultList = new ArrayList<>();
//        for (int i = 0; i <listTrueAnswers.size() ; i++) {
//            if (listAnswers.get(i) != null && !"".equalsIgnoreCase(listAnswers.get(i).trim())){
//                resultList.add( (listTrueAnswers.get(i).equals(listAnswers.get(i))) ? "1" : "0");
//            }else{
//                resultList.add("");
//            }
//
//        }
//        return resultList;
//    }

    //Добавил универсальный метод, который за 1 проход возращает то что надо) оптимизация скорости, но возможно ухудшает читаемость
    //при применение этого метода

    //Метод возращающий кол-во правильных ответов и меняющий лист ответов студентов на лист правильных и неправильных овтетов
    public static int countTrueAndCheck(List<String> listAnswers, List<String> listTrueAnswers){
        int countTrue = 0;
        for (int i = 0; i <listTrueAnswers.size() ; i++) {
            if (listAnswers.get(i) != null && !"".equalsIgnoreCase(listAnswers.get(i).trim())){
                if  (listTrueAnswers.get(i).equals(listAnswers.get(i))){
                    listAnswers.set( i, "1");
                    countTrue++;
                }else{
                    listAnswers.set( i, "0");
                }
            }else{
                listAnswers.set(i, "");
            }
        }
        return  countTrue;
    }

    //метод который приводит строку к стандартку 123 где цифры распологаются по возрастанию
    public static String filterAnswer(String tmp){
        TreeSet<Integer> tmpSet = new TreeSet<>();

        tmp = tmp.trim();
        tmp = tmp.replaceAll(" ", "");
        tmp =  tmp.replaceAll(",", "");

        int i =0;
        for (char ch :tmp.toCharArray()) {
            tmpSet.add(Integer.parseInt(String.valueOf(ch)));
        }

        StringBuilder tmpSB = new StringBuilder();
        for (Integer integer:tmpSet) {
            tmpSB.append(String.valueOf(integer));
        }
        return tmpSB.toString();
    }
}





