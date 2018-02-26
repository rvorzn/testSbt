package ru.company.testing.test;

import ru.company.testing.Question;
import ru.company.testing.Student;
import ru.company.testing.Test;
import ru.company.testing.utils.Verification;
import ru.company.swings.GUI;
import ru.company.timers.Timer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static ru.company.swings.GUI.hideElements;
import static ru.company.swings.GUI.showElements;


public class TestGui extends JFrame {
    private JPanel jp_welcom;
    private JPanel jp_test;
    private JPanel jp_gotoQuestion;
    private JPanel jp_blockAnswer;
    private JPanel jp_timer;
    private JPanel jp_mainTestGui;
    private JPanel jp_descriptonTest;
    private JPanel jp_endTest;

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
    private JTextArea ta_listQuestion;
    private JLabel lbl_resultTest;
    private JButton btn_saveAs;

    private Student student;
    private boolean isEnd = false;

    Timer myTimer;
    private Test test;
    int keyQuestion;



    public TestGui() throws HeadlessException {

        //Action start Test
        btn_beginTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               student = new Student(tf_nameStudent.getText());
               student.setTest(test);


               if (myTimer != null){
                   new Thread(myTimer).start();
                   new Thread(new MonitorTimer()).start();
               }

                hideElements(new JComponent[]{tf_nameStudent, btn_beginTest, tf_nameStudent, lbl_inputName, jp_descriptonTest });
                showElements(new JComponent[]{jp_blockAnswer, jp_gotoQuestion, jp_timer, ta_listQuestion});
                showQuestion(getQuestion(keyQuestion));
                setExtendedState(MAXIMIZED_BOTH);
            }
        });

        //Action miss Question
        btn_missAnswer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showQuestion(getQuestion(++keyQuestion)); // показываем следующий вопрос
            }
        });

        //Action show result count True answer for Student
        btn_showResult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> trueAnswer = new ArrayList<>();
                for (Question question:test.getQuestionsList()) {
                    trueAnswer.add(question.getTrueAnswer());
                }

                lbl_resultTest.setText("Правильных ответов: " +
                        String.valueOf(Verification.countTrueAndCheck(Arrays.asList(student.getAnswers()), trueAnswer )) +
                        " из " + test.getQuestionsList().size() );

                showElements(lbl_resultTest);
                hideElements(btn_showResult);
            }
        });

        //Action exit from program befor test
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
                boolean isExceptiot = true;

                try {
                    test = GUI.openObjectDialog(test);
                    isExceptiot = false;
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }

                if (!isExceptiot){
                    myTimer =  new Timer(test.getTime(), 3);

                    lbl_titleTest.setText(test.getTitle());
                    tp_descriptionTest.setText(test.getDescription() + "\n\n\nВремя на выполнение: " +  test.getTime());
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
                myTimer.stop();

                for (int i = 0; i <student.getAnswers().length ; i++) {
                    student.getAnswers()[i] = Verification.filterAnswer(student.getAnswers()[i]);
                }

                File fileStudent = new File(student.getFullName());
                try {
                    GUI.saveObject(student, fileStudent); //автоматическое сохранение файла
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                hideElements(new JComponent[]{jp_blockAnswer, jp_gotoQuestion, jp_timer, ta_listQuestion});
                showElements(new JComponent[]{jp_endTest});
                showListQuestion();

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
                if (myTimer.isPause()) {
                    myTimer.go();
                    btn_pause.setText("Пауза (" + myTimer.getCountPause() + ")");
                    showElements(jp_blockAnswer, jp_gotoQuestion);
                    btn_pause.setVisible(myTimer.getCountPause() > 0 ? true : false);
                }else{
                    myTimer.pause();
                    btn_pause.setText("Продолжить");
                    btn_pause.setText("Тест поставлен на паузу");
                    hideElements(jp_blockAnswer, jp_gotoQuestion);
                }

            }
        });

        //Action set answer
        btn_setAnswer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String answer = tf_answer.getText();
                student.setAnswers(keyQuestion, answer);

                showQuestion(getQuestion(++keyQuestion));

            }
        });

        //Action SaveAs
        btn_saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    GUI.saveObjectDialog(student); // Сохранить туда куда хочет пользователь через диалоговое окно
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    //получение вопроса
    private  Question getQuestion(int key){
        List<Question> questionList = test.getQuestionsList();
        if (key <= (test.getQuestionsList().size()-1) ) { // если дошли до конца возращаем null
             return questionList.get(key);
        }else {
            return null;
        }
    }

    private void showQuestion (Question question){
        showListQuestion();
        if (isEnd()){
            JOptionPane.showMessageDialog(jp_mainTestGui,
                    "Поздравляем вы ответили на все вопросы и у вас осталось "+ myTimer.getTime() +" минут, вы можете завершить" +
                            "тест сами или он закончиться автоматически по истечению времени",
                    "УРА УРА УРА",
                    JOptionPane.INFORMATION_MESSAGE);
            isEnd = true;

        }

        //если дошли до последнего вопроса
        if (question != null) {

            tp_descriptionQuestion.setText(question.getDescription());
            tp_question.setText(question.getQuestion());

            String stringAnswer= "";
            for (  Map.Entry<Integer, String> answer : question.getAnswerСhoice().entrySet()) {
                stringAnswer += answer.getKey()+ ") "  +answer.getValue() +"\n";
            }

            tp_answers.setText(stringAnswer);
            //отображение кнопок управление вопросами и текущий вопрос
            btn_preQuestion.setVisible(keyQuestion == 0 ? false : true);
            btn_nextQuestion.setVisible(keyQuestion == (test.getQuestionsList().size()-1) ? false : true);
            lbl_currentQuewstion.setText("Текущий вопрос №" + (keyQuestion+1));
            //если ответ на вопрос уже есть то отображаем его
            tf_answer.setText(student.getAnswers()[keyQuestion] == null ? "" : student.getAnswers()[keyQuestion]);
            return;
        } else { // если мы дошли до последнего вопроса и есть вопросы без ответов то переходим к ним
            for (int i = 0; i < student.getAnswers().length; i++) {
                if (student.getAnswers()[i] == null || "".equalsIgnoreCase(student.getAnswers()[i].trim())) {
                    keyQuestion = i;
                    showQuestion(getQuestion(keyQuestion));
                }
            }
            keyQuestion--;

        }

    }

    // проверка, что тест можно закончить
    private  boolean isEnd(){
        if (isEnd) {return false;}
        for (String s : student.getAnswers()) {
            if (s == null || "".equalsIgnoreCase(s.trim())){ // на все вопросы даны ответы
                return false;
            }
        }
        return true;
    }


    //отображения списка вопросов и статуса ответов
    private void showListQuestion(){
        int count = test.getQuestionsList().size();
        String[] answersStudent = student.getAnswers();
        StringBuilder listQuestionAndStatus = new StringBuilder();
        for (int i = 0; i < count; i++) {
            listQuestionAndStatus.append( "Вопрос №" + (i+1) + " - " +
                    ((answersStudent[i] == null || "".equalsIgnoreCase(answersStudent[i].trim()))  ? "" : "Ответ дан") + "\n");

        }
        ta_listQuestion.setText(listQuestionAndStatus.toString());

    }


    //стартого
    public void start(){
        setContentPane(jp_mainTestGui);
        jp_welcom.setVisible(true);
        jp_test.setVisible(false);
        hideElements(new JComponent[]{jp_gotoQuestion, jp_blockAnswer, jp_endTest, jp_timer, ta_listQuestion});
        setSize(750, 400);
        setVisible(true);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    class MonitorTimer implements Runnable{
        @Override
        public void run() {
            while (myTimer.getTime() > 0){
                lbl_timer.setText("Тест закончится через: " +  myTimer.getTime() + " минут" + (myTimer.getTime() > 1 ? "" : "у") );
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            btn_endTest.doClick();
            lbl_timer.setText("Время на прохождение теста закончено");
        }
    }
}

