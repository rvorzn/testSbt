package ru.company.testing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import static ru.company.testing.GUI.hideElements;
import static ru.company.testing.GUI.showElements;

public class TestGui extends JFrame {
    private JPanel jp_welcom;
    private JPanel jp_test;
    private JPanel jp_gotoQuestion;
    private JPanel jp_blockAnswer;
    private JPanel jp_timer;
    private JPanel jp_mainTestGui;
    private JPanel jp_descriptonTest;

    private JButton btn_beginTest;
    private JButton btn_showResult;
    private JButton btn_exitPogram;
    private JButton btn_missAnswer;
    private JButton btn_openFile;
    private JButton btn_gotoQuestion;
    private JButton btn_endTest;
    private JButton btn_preQuestion;
    private JButton btn_nextQuestion;
    private JButton btn_pause;
    private JButton btn_setAnswer;

    private JLabel lbl_inputName;
    private JLabel lbl_titleTest;
    private JLabel lbl_timer;
    private JLabel lbl_currentQuewstion;


    private JTextField tf_nameStudent;
    private JTextField textField_numberQuestionGoTo;
    private JTextField tf_answer;
    private JTextPane tp_question;
    private JTextPane tp_descriptionQuestion;
    private JTextPane tp_answers;
    private JTextPane tp_descriptionTest;
    private JTextArea textArea_listQuestion;

    private Student student;
    private boolean pause =false;

    private Test test;
    int keyQuestion;
    int countPause = 3;
    int pauseTime= 0;



    public TestGui() throws HeadlessException {

        Thread timer = new Thread(){

            @Override
            public void run(){
                int i = 1;
                int min = 0;
                lbl_timer.setText("Тест закончится через: " +  Integer.toString(i) + " минут");
                while (!(i==0)){
                    while (pause) {
                        try {
                            sleep(1000);
                            pauseTime++;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        sleep(1000);
                        min += 1000;
                        if (min == 60000) {
                            i--;
                            min =0;
                            lbl_timer.setText("Тест закончится через: " +  Integer.toString(i) + " минут");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                btn_endTest.doClick();
                System.out.println("конец");
                lbl_timer.setText("Время на прохождение теста закончено");

            }
        } ;


        btn_beginTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               student = new Student(tf_nameStudent.getText());
               student.setTest(test);
               showQuestion(getQuestion(keyQuestion));

               hideElements(new JComponent[]{tf_nameStudent, btn_beginTest, tf_nameStudent, lbl_inputName, jp_descriptonTest });
               showElements(new JComponent[]{jp_blockAnswer, jp_gotoQuestion, jp_timer});

               timer.start();
            pack();
            }
        });

        //Action miss Question
        btn_missAnswer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                student.setAnswers(keyQuestion, "");

                showQuestion(getQuestion(++keyQuestion));


            }
        });

//        //Action write Answer
//        btnAnswer.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String answer = tf_answer.getText();
//                student.setAnswers(keyQuestion, answer);
//
//                showQuestion(getQuestion(++keyQuestion));
//
//            }
//        });
        btn_showResult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               lbl_titleTest.setText(Verification.checkAnswers(student));
            }
        });
        btn_exitPogram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //Открытие файла с тестом
        btn_openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser();
                if (fileopen.showDialog(null, "Открыть файл") == JFileChooser.APPROVE_OPTION) {
                    File fileTest = new File((fileopen.getSelectedFile().getAbsolutePath()));
                    try {
                        test = GUI.openObject(test, fileTest);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }

                    jp_welcom.setVisible(false);
                    jp_test.setVisible(true);
                }
            }
        });
        //Action goto the question
        btn_gotoQuestion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String key = textField_numberQuestionGoTo.getText();
                if (!"".equalsIgnoreCase(key.trim()) && GUI.isInt(key) && ((Integer.parseInt(key)-1) >= 0) && ((Integer.parseInt(key)-1) < test.getQuestionsList().size())){
                    keyQuestion = (Integer.parseInt(key)-1);
                    Question question = getQuestion(keyQuestion);
                    showQuestion(question);
                    textField_numberQuestionGoTo.setText("");
                }else {
                    JOptionPane.showMessageDialog(jp_mainTestGui,
                            "Поле с номером вопроса недолжно быть пустым или выходить за пределы кол-ва вопросов",
                            "Ошибка при открытие фалйа",
                            JOptionPane.WARNING_MESSAGE);
                }


            }
        });

        //Action end test
        btn_endTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(student.getAnswers().size() + " : " + test.getQuestionsList().size());
                if (student.getAnswers().size() != test.getQuestionsList().size()){
                    for (int i = 0; i < test.getQuestionsList().size(); i++) {
                        if (student.getAnswers().get(i) == null){
                            student.setAnswers(i, "");
                        }
                    }
                }
                theEndTest();
            }
        });

        //Action pre Question
        btn_preQuestion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showQuestion(test.getQestion(--keyQuestion));
            }
        });
        //Action next Question
        btn_nextQuestion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showQuestion(test.getQestion(++keyQuestion));
            }
        });

        //Action pause
        btn_pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pause) {
                    pause = false;
                    btn_pause.setText("Пауза (" + --countPause + ")");
                    showElements(jp_blockAnswer, jp_gotoQuestion);
                    btn_pause.setVisible(countPause > 0 ? true : false);
                }else{

                    pause = true;
                    btn_pause.setText("Продолжить");
                    btn_pause.setText("Тест поставлен на паузу");
                    hideElements(jp_blockAnswer, jp_gotoQuestion);
                }

            }
        });

        //Action save answer
        btn_setAnswer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String answer = tf_answer.getText();
                student.setAnswers(keyQuestion, answer);

                showQuestion(getQuestion(++keyQuestion));

            }
        });
    }





    private  Question getQuestion(int key){
        ArrayList<Question> questionList = test.getQuestionsList();
        if (key <= (test.getQuestionsList().size()-1) ) {
             return questionList.get(key);
        }else {
            return null;
        }
    }

    private void showQuestion (Question question){
            if ((question != null) || !isEnd()) {
                if (question != null) {

                    tp_descriptionQuestion.setText(question.getDescription());
                    tp_question.setText(question.getQuestion());
                    tp_answers.setText(question.getAnsver());
                    //отображение кнопок управление вопросами и текущий вопрос
                    btn_preQuestion.setVisible(keyQuestion == 0 ? false : true);
                    btn_nextQuestion.setVisible(keyQuestion == (test.getQuestionsList().size()-1) ? false : true);
                    lbl_currentQuewstion.setText("Текущий вопрос №" + (keyQuestion+1));
                    //если ответ на вопрос уже есть то отображаем его
                    tf_answer.setText(student.getAnswers().get(keyQuestion) == null ? "" : student.getAnswers().get(keyQuestion) );
                    showListQuestion();
                    return;
                } else {
                    for (int i = 0; i < student.getAnswers().size(); i++) {
                        if (student.getAnswers().get(i) == null || "".equalsIgnoreCase(student.getAnswers().get(i).trim())) {
                            keyQuestion = i;
                            showQuestion(getQuestion(keyQuestion));
                        }
                    }
                }
            }
            pack();
    }

    private  boolean isEnd(){
        if (student.getAnswers().size() == test.getQuestionsList().size()) {
            for (String s : student.getAnswers().values() ) {
                if ("".equalsIgnoreCase(s.trim())){
                    return false;
                }
            }
        }
        return false;
    }

    private void showListQuestion(){
        int count = test.getQuestionsList().size();
        HashMap<Integer, String> answersStudent = student.getAnswers();
        StringBuilder listQuestionAndStatus = new StringBuilder();
        for (int i = 0; i < count; i++) {

            listQuestionAndStatus.append( "Вопрос №" + (i+1) + " - " +
                    ((answersStudent.get(i) == null ||"".equalsIgnoreCase(answersStudent.get(i)))  ? "" : "Ответ дан") + "\n");

        }
        textArea_listQuestion.setText(listQuestionAndStatus.toString());

    }


    private void theEndTest(){
        showListQuestion();

        File file = new File(student.getFullName());
        try{
            GUI.saveObject(student, file);
        } catch (IOException e) {
            e.printStackTrace();
        }


        lbl_titleTest.setText("Спасибо что прошли тест");
        hideElements(new JComponent[]{jp_blockAnswer, jp_gotoQuestion, jp_timer});
        showElements(new JComponent[]{btn_showResult, btn_exitPogram});
    }


    public void start(){
        setContentPane(jp_mainTestGui);
        jp_welcom.setVisible(true);
        jp_test.setVisible(false);
        hideElements(new JComponent[]{jp_gotoQuestion, jp_blockAnswer, btn_exitPogram, btn_showResult, jp_timer});
        setSize(750, 400);
        setVisible(true);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}

