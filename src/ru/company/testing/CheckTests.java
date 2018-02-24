package ru.company.testing;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

import static ru.company.testing.GUI.hideElements;


public class CheckTests extends JFrame {
    private JPanel jp_mainCheckTest;
    private JPanel jp_trueAnswer;
    private JPanel jp_resultStudents;
    private JPanel jp_sourceAnswerStudents;

    private JButton btn_openTestStudents;
    private JButton btn_openTest;
    private JButton btn_saveRefactNewAnswer;
    private JButton btn_refresh;
    private JButton btn_showActualAnswerStudents;
    private JButton btn_setLevel;
    private JButton btn_setRigth;
    private JButton btn_setLeft;


    private JTable table_result;
    private JTable tableAnswer;
    private JTable table_sourceAnswerStudents;



    private JTextField tf_levelTrueAnswer;
    private JTextField tf_leftBorderLine;
    private JTextField tf_rigthBorderLine;
    private JLabel lbl_messageProcessing;


    Test test;
    File currientFileTest;
    Student currientStudent;
    List<String> trueAnswer;
    TableInfoRenderer tableInfoRenderer = new TableInfoRenderer();
    double defaultProcLevel = 0.2d; // 20% процентов значение для границ кол-ва праавильных ответов на каждый вопрос

    public CheckTests() {
        //Action open Student Tets
        btn_openTestStudents.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isExeption = true;

                JFileChooser fileopen = new JFileChooser();
                if (fileopen.showDialog(null, "Открыть файл") == JFileChooser.APPROVE_OPTION) {
                    File fileTestStudent = new File((fileopen.getSelectedFile().getAbsolutePath()));
                    try {
                        currientStudent = GUI.openObject(currientStudent, fileTestStudent);
                        isExeption =false;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }

                }

                if (!isExeption){

                    String[] answers = currientStudent.getAnswers();
                    DefaultTableModel modelTableResult = (DefaultTableModel) table_result.getModel();
                    DefaultTableModel modelTableSoureAnswer = (DefaultTableModel) table_sourceAnswerStudents.getModel();

                    List<String> rowTableResutlr = new ArrayList<>();
                    List<String> rowSourseAnswer = new ArrayList<>();
                    rowTableResutlr.add( currientStudent.getFullName());
                    rowSourseAnswer.add( currientStudent.getFullName());
                    List<String> answerList = new ArrayList<>();
                    for (String el:answers) {
                        answerList.add(el);
                    }
                    rowTableResutlr.addAll(Verification.checkAnswers(answerList, trueAnswer));
                    rowSourseAnswer.addAll(answerList);


                    rowTableResutlr.add(countTrueAnswer(rowTableResutlr));

                    modelTableResult.addRow(rowTableResutlr.toArray());
                    modelTableSoureAnswer.addRow(rowSourseAnswer.toArray());


                    modelTableResult.removeRow(0);
                    if (modelTableResult.getRowCount() > 1){
                        tableInfoRenderer.setLeftLevel((int)(Math.ceil(modelTableResult.getRowCount()*defaultProcLevel)));
                        tableInfoRenderer.setRightLevel(modelTableResult.getRowCount()- tableInfoRenderer.getLeftLevel());
                    }else {
                        tableInfoRenderer.setLeftLevel(0);
                        tableInfoRenderer.setRightLevel(1);
                    }
                    modelTableResult.insertRow(0, countTrueAnswerTheQuestion().toArray());

                    tf_leftBorderLine.setText(Integer.toString(tableInfoRenderer.getLeftLevel()));
                    tf_rigthBorderLine.setText(Integer.toString(tableInfoRenderer.getRightLevel()));

                    //отображаем панель если скрыта
                    if (!jp_resultStudents.isVisible()) { jp_resultStudents.setVisible(true); };
                }
                pack();
            }
        });


        //Action Open Test
        btn_openTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isExeption = true;

                JFileChooser fileopen = new JFileChooser();
                if (fileopen.showDialog(null, "Открыть файл") == JFileChooser.APPROVE_OPTION) {
                    currientFileTest = new File((fileopen.getSelectedFile().getAbsolutePath()));
                    try {
                        test = GUI.openObject(test, currientFileTest);
                        isExeption = false;
                    }catch (ClassCastException e1) {
                        JOptionPane.showMessageDialog(jp_mainCheckTest,
                                "Данный файл не является тестом, попробуйте открыть другой файл",
                                "Ошибка при открытие фалйа",
                                JOptionPane.WARNING_MESSAGE);
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(jp_mainCheckTest,
                                "Данный файл не является тестом или этой файл старой версии, \n попробуйте открыть другой файл",
                                "Ошибка при открытие фалйа",
                                JOptionPane.WARNING_MESSAGE);
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();

                    }
                }


                if (!isExeption){//если все открылось без ошибок

                    DefaultTableModel modelTableTrueresult = (DefaultTableModel)tableAnswer.getModel();
                    DefaultTableModel modelTableAnswerStudent = (DefaultTableModel) table_result.getModel();
                    DefaultTableModel modelTableAnswerStudentHide = (DefaultTableModel) table_sourceAnswerStudents.getModel();
                    //очистка строк
                    for (int i = modelTableTrueresult.getRowCount(); i > 0  ; i--) {
                        modelTableTrueresult.removeRow(i-1);
                    }
                    //Вставить очистку таблицы ответов студентов
                    ArrayList<Question> arrQuestions = test.getQuestionsList();
                    String[] row = new String[arrQuestions.size()+1]; //Строки

                    if (modelTableTrueresult.getColumnCount() == 0) {
                        modelTableTrueresult.addColumn("Вопросы");
                        modelTableAnswerStudent.addColumn("Студенты/Вопросы");
                        modelTableAnswerStudentHide.addColumn("Студенты/Вопросы");
                    }
                    row[0] = "Правильные ответы";
                    trueAnswer = new ArrayList<>();
                    for (int i = 0; i <arrQuestions.size() ; i++) {
                        if (modelTableTrueresult.getColumnCount() <arrQuestions.size()+1 && modelTableTrueresult.getColumnCount()<i+2) {
                            modelTableTrueresult.addColumn("№"+ (i+1));
                            modelTableAnswerStudent.addColumn("№"+ (i+1));
                            modelTableAnswerStudentHide.addColumn("№"+ (i+1));
                        }

                        row[i+1] = arrQuestions.get(i).getTrueAnswer();
                        trueAnswer.add(row[i+1]);
                    }
                    modelTableAnswerStudent.addColumn("Кол-во правильных");
                    modelTableAnswerStudent.addRow(countTrueAnswerTheQuestion().toArray());
                    modelTableTrueresult.addRow(row);

                    tf_levelTrueAnswer.setText(Integer.toString(test.getLevel()));
                    tableInfoRenderer.setLevel(test.getLevel());
                    btn_openTestStudents.setVisible(true);
                    jp_trueAnswer.setVisible(true);

                }
                pack();


            }
        });

        //Action Save new Answer in file Test
        btn_saveRefactNewAnswer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Question> questionsList = test.getQuestionsList();
                Question question;
                for (int i = 1; i < tableAnswer.getColumnCount() ; i++) {
                    question = questionsList.get(i-1);
                    question.setTrueAndswer(tableAnswer.getValueAt(0, i).toString()); // переписываем значение в вопросах
                    trueAnswer.set(i-1,tableAnswer.getValueAt(0, i).toString()); // переписываем локальную временную переменную
                }
                test.addAllQuestion(questionsList);

                try {
                    GUI.saveObject(test, currientFileTest);
                    GUI.showMessage("Данные успешно записанны в файл", lbl_messageProcessing);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });

        //Action refresh
        btn_refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Получение списка  из таблицы правильных ответов
                ArrayList<String> answerList = new ArrayList<>();
                for (int i = 1; i < tableAnswer.getColumnCount() ; i++) {
                   answerList.add(tableAnswer.getValueAt(0,i).toString());
                }

                String result;
                DefaultTableModel modelTableResult = (DefaultTableModel) table_result.getModel();
                modelTableResult.removeRow(0); // Удаление строки с подчетом правильных овтетов в столбцах
                for (int i = 0; i < table_sourceAnswerStudents.getRowCount() ; i++) {
                    int sumTrueAnswer = 0;
                    for (int j = 1; j < table_sourceAnswerStudents.getColumnCount() ; j++) {
                        if (table_sourceAnswerStudents.getValueAt(i, j) == null || "".equalsIgnoreCase((table_sourceAnswerStudents.getValueAt(i, j).toString().trim()))){
                            result = "";
                        }else if (table_sourceAnswerStudents.getValueAt(i, j).equals(answerList.get(j - 1))) {
                            result = "1";
                            sumTrueAnswer ++;
                        } else {
                            result = "0";
                        }

                        table_result.setValueAt(result, i, j);
                        table_result.setValueAt(Integer.toString(sumTrueAnswer), i, (j+1));
                    }

                }
                modelTableResult.insertRow(0, countTrueAnswerTheQuestion().toArray()); // добавление строки с подчетом правильных овтетов в столбцах
                GUI.showMessage("Данные успешно обновленны", lbl_messageProcessing);
            }
        });

        //Action show actual answer Students
        btn_showActualAnswerStudents.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jp_sourceAnswerStudents.isVisible()){
                    btn_showActualAnswerStudents.setText("Показать исходные ответы студентов");
                    jp_sourceAnswerStudents.setVisible(false);
                }else{
                    btn_showActualAnswerStudents.setText("Скрыть ответы студентов");
                    jp_sourceAnswerStudents.setVisible(true);
                }
                pack();
            }
        });

        //Action set Level for true answer
        btn_setLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GUI.isInt(tf_levelTrueAnswer.getText())){
                    tableInfoRenderer.setLevel(Integer.parseInt(tf_levelTrueAnswer.getText()));
                    GUI.showMessage("Параметр установлен", lbl_messageProcessing);
                } else {
                    JOptionPane.showMessageDialog(jp_mainCheckTest,
                            "В поле должно быть только число",
                            "Ошибка значения",
                            JOptionPane.WARNING_MESSAGE);
                }


            }
        });

        //Action set left level
        btn_setLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GUI.isInt(tf_leftBorderLine.getText())){
                    tableInfoRenderer.setLeftLevel(Integer.parseInt(tf_leftBorderLine.getText()));
                    GUI.showMessage("Параметр установлен", lbl_messageProcessing);
                }else{
                    JOptionPane.showMessageDialog(jp_mainCheckTest,
                            "В поле должно быть только число",
                            "Ошибка значения",
                            JOptionPane.WARNING_MESSAGE);
                }

            }
        });

        //Action set rigth level
        btn_setRigth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GUI.isInt(tf_rigthBorderLine.getText())){
                    tableInfoRenderer.setRightLevel(Integer.parseInt(tf_rigthBorderLine.getText()));
                    GUI.showMessage("Параметр установлен", lbl_messageProcessing);
                }else{
                    JOptionPane.showMessageDialog(jp_mainCheckTest,
                            "В поле должно быть только число",
                            "Ошибка значения",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    private String countTrueAnswer(List<String> list) {
        int i = 0;
        for (String el :list) {
            i += "1".equalsIgnoreCase(el) ? 1: 0;
        }
        return Integer.toString(i);
    }

    public List<String> countTrueAnswerTheQuestion(){
        List<String> resultRow = new  ArrayList<>();
        resultRow.add("Кол-во правильных ответов в вопросе");
        if (table_result.getRowCount() > 0){
            for (int i = 1; i < table_result.getColumnCount()-1 ; i++) {
                int tmpSum = 0;
                for (int j = 0; j < table_result.getRowCount() ; j++) {
                    String tmpString = table_result.getValueAt(j,i).toString();
                    if (GUI.isInt(tmpString)){
                        tmpSum += Integer.parseInt(tmpString) ;
                    }
                }
                resultRow.add(String.valueOf(tmpSum));
            }
        }
        return resultRow;
    }


    public void showWindows(){

        table_result.setDefaultRenderer(Object.class, tableInfoRenderer);

        setContentPane(jp_mainCheckTest);
        hideElements(btn_openTestStudents, jp_resultStudents, jp_trueAnswer,jp_sourceAnswerStudents);
        setSize(750, 400);
        pack();
        setVisible(true);
    }
}


