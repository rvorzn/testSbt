package ru.company.testing;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import static ru.company.testing.GUI.hideElements;
import static ru.company.testing.GUI.isInt;
import static ru.company.testing.GUI.showElements;

public class CreateTest extends JFrame {
    private JPanel jp_mainCreateTest;
    private JPanel jp_authPanel;
    private JPanel jp_createPanel;
    private JPanel panelAnswer1;
    private JPanel panelAnswer2;
    private JPanel panelAnswer3;
    private JPanel panelAnswer4;
    private JPanel panelAnswer5;
    private JPanel panelAnswer6;
    private JPanel panelAnswer7;
    private JPanel panelAnswer8;
    private JPanel jp_dialogСhoice;
    private JPanel jp_descriptionTest;

    private JPasswordField passField_theacher;

    private JTextField tf_trueAnswer;
    private JTextField answerField1;
    private JTextField answerField2;
    private JTextField answerField3;
    private JTextField answerField4;
    private JTextField answerField5;
    private JTextField answerField6;
    private JTextField answerField7;
    private JTextField answerField8;

    private JTextArea textArea_question;
    private JTextArea description;
    private JTextPane textPane_QuestionList;
    private JTextArea ta_descriptionTest;


    private JButton btn_inputPassTheacher;
    private JButton btn_createNewTest;
    private JButton btn_removeQuewstion;
    private JButton btn_refactQuestion;
    private JButton btnAddQuestion;
    private JButton btn_addAnswer;
    private JButton btnRemoveAnswer;
    private JButton btn_openTest;
    private JButton btnSaveTest;
    private JButton btn_saveDescriptionTest;
    private JButton btn_saveRefactQuewstion;
    private JButton btn_checkTests;

    private JTextField textField_refactQuestion;
    private JTextField textField_removQuewstion;
    private JTextField tf_testTime;
    private JTextField tf_testLevel;
    private JTextField tf_testTitle;

    private JLabel lbl_Error;
    private JLabel labelHello;
    private JLabel lbl_nameTest;





    Test test = new Test("fileTest");
    int tempKey;
    public String password = "1234";

    ArrayList<Question> questionList;

    JPanel[] answerArJpanel = new JPanel[]{panelAnswer1, panelAnswer2, panelAnswer3, panelAnswer4, panelAnswer5, panelAnswer6, panelAnswer7, panelAnswer8};
    JTextField[] answerArField = new JTextField[]{answerField1, answerField2, answerField3, answerField4, answerField5, answerField6, answerField7, answerField8};
    CreateTest(){

        //Action authorization teacher
        btn_inputPassTheacher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuffer passw = new StringBuffer();
                for (char ch : passField_theacher.getPassword()) {
                    passw.append(ch);
                }
                //Integer tempHash = passw.toString().hashCode();

                if (passw.toString().equals(password)){
                    jp_dialogСhoice.setVisible(true);
                    jp_authPanel.setVisible(false);

                }
                else {
                    labelHello.setText(labelHello.getText() + " (Введите пароль еще раз)");
                }
                pack();
            }
        });

        //Action add answer choice in Jpanel
        btn_addAnswer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JPanel textField: answerArJpanel) {
                    if (!textField.isVisible()){
                        textField.setVisible(true);
                        break;
                    }
                }
            }
        });

        //Action remove answer from test
        btnRemoveAnswer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = answerArJpanel.length-1; i >=0 ; i--) {
                    if (answerArJpanel[i].isVisible()){
                        answerArJpanel[i].setVisible(false);
                        break;
                    }

                }
            }
        });

        //Action addQuest in test
        btnAddQuestion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Question newQuestion = new Question();
                newQuestion.setQuestion(textArea_question.getText());
                newQuestion.setDescription(description.getText());
                for (int i = 0; i < answerArField.length ; i++) {
                    if (!"".equals(answerArField[i].getText())){
                        newQuestion.setAnswerСhoice(answerArField[i].getText());
                    }
                }
                newQuestion.setTrueAndswer(tf_trueAnswer.getText());
                test.addQuestion(newQuestion);

                clearFields();

                showListQuestion();
                System.out.println(newQuestion.toString());
                lbl_Error.setText("Вопрос удачно добавлен");

            }
        });

        //Action open test for view
        btn_openTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isExeption = false;
                JFileChooser fileopen = new JFileChooser();
                if (fileopen.showDialog(null, "Открыть файл") == JFileChooser.APPROVE_OPTION) {
                    File fileTest = new File((fileopen.getSelectedFile().getAbsolutePath()));
                    try {
                        test = GUI.openObject(test, fileTest);

                    } catch (IOException e1) {
                        e1.printStackTrace();
                        isExeption = true;
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                        isExeption = true;
                    }

                    if (!isExeption){
                        showListQuestion();
                        tf_testTitle.setText(test.getTitle());
                        ta_descriptionTest.setText(test.getDescription());
                        tf_testLevel.setText(String.valueOf(test.getLevel()));
                        tf_testTime.setText(String.valueOf(test.getTime()));
                        showElements(jp_descriptionTest, btn_saveDescriptionTest,  jp_createPanel);
                    }else{
                        //Вывод ошибки
                    }


                }

            }
        });

        //Action Save Test
        btnSaveTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                if (fc.showDialog(null, "Сохранить как") == JFileChooser.APPROVE_OPTION){
                    File fileTest = new File((fc.getSelectedFile().getAbsolutePath()));
                    try {
                        GUI.saveObject(test, fileTest);
                    }
                    catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }


            }
        });

        //Action show crate new test
        btn_createNewTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               test = new Test("Новый тест");
               showElements(jp_descriptionTest, btn_saveDescriptionTest);
            pack();
            }
        });

        //Action refatcoring Question
        btn_refactQuestion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!"".equalsIgnoreCase(textField_refactQuestion.getText().trim())  ){
                    btnAddQuestion.setVisible(false);
                    btn_saveRefactQuewstion.setVisible(true);
                    clearFields();
                    try{
                        tempKey = Integer.parseInt(textField_refactQuestion.getText());
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                    Question question =test.getQestion(tempKey-1);
                    if (question != null){
                        btnSaveTest.setVisible(false);
                        textArea_question.setText(question.getQuestion());
                        description.setText(question.getDescription());
                        tf_trueAnswer.setText(question.getTrueAnswer());
                        for (int i = 1; i <= question.getAnswerСhoice().size(); i++) {
                            answerArJpanel[i-1].setVisible(true);
                            answerArField[i-1].setText(question.getAnswerСhoice().get(i));
                        }

                    }else{
                        lbl_Error.setText("К сожалению такого вопроса с таким № нет в списке");
                    }

                }else {
                 lbl_Error.setText("Неправильно заполненно поле");
                }


            }
        });

        //Action remove Question
        btn_removeQuewstion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int key = Integer.parseInt(textField_removQuewstion.getText())-1;
                test.removeQuestion(key);

                showListQuestion();// updateViewListQuestion
            }
        });

        //Saving new version qiestion
        btn_saveRefactQuewstion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Question newQuestion = new Question();
                newQuestion.setQuestion(textArea_question.getText());
                newQuestion.setDescription(description.getText());
                for (int i = 0; i < answerArField.length ; i++) {
                    if (!"".equals(answerArField[i].getText().trim())){
                        newQuestion.setAnswerСhoice(answerArField[i].getText());
                    }
                }
                newQuestion.setTrueAndswer(tf_trueAnswer.getText());
                test.changeQuestion(tempKey-1, newQuestion);

                clearFields();
                showListQuestion();

                lbl_Error.setText("Вопрос удачно изменен добавлен");
                btn_saveRefactQuewstion.setVisible(false);
                btnAddQuestion.setVisible(true);
                btnSaveTest.setVisible(true);
            }
        });

        //Actions show window CheckTest
        btn_checkTests.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CheckTests().showWindows();
            }
        });
        //Action save or show description Test
        btn_saveDescriptionTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jp_descriptionTest.isVisible()){

                        test.setTitle(tf_testTitle.getText());
                        test.setDescription(ta_descriptionTest.getText());
                    if (!"".equalsIgnoreCase(tf_testLevel.getText().trim()) || isInt(tf_testLevel.getText())) {
                        int tmp = Integer.parseInt(tf_testLevel.getText());
                        if (tmp >= 0){
                            test.setLevel(tmp);
                        }else{
                            JOptionPane.showMessageDialog(jp_mainCreateTest,
                                    "В поле должно быть gоложительное число",
                                    "Ошибка значения",
                                    JOptionPane.WARNING_MESSAGE);
                        }


                    }else{
                        JOptionPane.showMessageDialog(jp_mainCreateTest,
                                "В поле должно быть только число ",
                                "Ошибка значения",
                                JOptionPane.WARNING_MESSAGE);
                    }
                    if (!"".equalsIgnoreCase(tf_testTime.getText().trim()) || isInt(tf_testTime.getText())){
                        int tmp = Integer.parseInt(tf_testTime.getText()) ;
                        if (tmp>0){
                            test.setTime(tmp);
                        }else{
                            JOptionPane.showMessageDialog(jp_mainCreateTest,
                                    "В поле должно быть gоложительное число и не ноль",
                                    "Ошибка значения",
                                    JOptionPane.WARNING_MESSAGE);
                        }

                    }else {
                        JOptionPane.showMessageDialog(jp_mainCreateTest,
                                "В поле должно быть только число",
                                "Ошибка значения",
                                JOptionPane.WARNING_MESSAGE);
                    }
                        btn_saveDescriptionTest.setText("Показать описание теста");
                        jp_descriptionTest.setVisible(false);
                }else{
                    btn_saveDescriptionTest.setText("Сохранить и скрыть описание теста");
                    jp_descriptionTest.setVisible(true);
                }

                if (!jp_createPanel.isVisible()){
                    jp_createPanel.setVisible(true);
                    setExtendedState(MAXIMIZED_BOTH);
                }
            }
        });
    }

    private void showListQuestion() {
        textPane_QuestionList.setText("");
        StringBuilder listQestion = new StringBuilder();

        questionList = test.getQuestionsList();
        for (int i = 0; i < questionList.size(); i++) {
            Question question =  questionList.get(i);
            listQestion.append("Вопрос №" + (i+1) + ". " + question.getQuestion()+"\n---------------------------------------------\n");
        }
        textPane_QuestionList.setText(listQestion.toString() );

    }

    private void clearFields() {
        for (JTextField field: answerArField) {
            field.setText("");
        }
        textArea_question.setText("");
        description.setText("");
        tf_trueAnswer.setText("");
    }

    public void showWindow(){
        setContentPane(jp_mainCreateTest);
        jp_dialogСhoice.setVisible(false);
        jp_createPanel.setVisible(false);
        setSize(750, 400);
        setVisible(true);
        pack();

        hideElements(jp_descriptionTest, panelAnswer3,panelAnswer4,panelAnswer5,panelAnswer6,panelAnswer7, panelAnswer8,
                btn_saveRefactQuewstion, btn_saveDescriptionTest);
    }

}





