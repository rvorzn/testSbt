package ru.company.testing.utils;

import ru.company.swings.GUI;
import ru.company.testing.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

import static ru.company.swings.GUI.hideElements;



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
    private JTextField tf_rightBorderLine;
    private JLabel lbl_messageProcessing;


    Test test;
    File currientFileTest;
    Student currientStudent;
    List<String> trueAnswer;
    TableInfoRenderer tableInfoRenderer = new TableInfoRenderer();
    double defaultProcLevel = 0.2d; // 20% процентов значение для границ кол-ва праавильных ответов на каждый вопрос

    interface View{

    }

    public CheckTests() {

        //Action open Student Tets
        btn_openTestStudents.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isException = true;

                try {
                    currientStudent = GUI.openObjectDialog(currientStudent);
                    isException =false;
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
//                JFileChooser fileopen = new JFileChooser();
//                if (fileopen.showDialog(null, "Открыть файл") == JFileChooser.APPROVE_OPTION) {
//                    File fileTestStudent = new File((fileopen.getSelectedFile().getAbsolutePath()));
//                    try {
//                        currientStudent = GUI.openObject(currientStudent, fileTestStudent);
//                        isExeption =false;
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                    } catch (ClassNotFoundException e1) {
//                        e1.printStackTrace();
//                    }
//
//                }



                if (!isException){//если все открылось хорошо

                    if (!currientStudent.getTest().getTitle().equalsIgnoreCase(test.getTitle())){
                        JOptionPane.showMessageDialog(jp_mainCheckTest,
                                "Данный тест студента, не совпадает с тестом который открыт для проверки",
                                "Ошибка при открытие фалйа",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String fullNameStudent = currientStudent.getFullName();

                    //получаем мрдели 2 таблиц
                    DefaultTableModel modelTableResult = (DefaultTableModel) table_result.getModel();
                    DefaultTableModel modelTableSoureAnswer = (DefaultTableModel) table_sourceAnswerStudents.getModel();

                    for (int i = 0; i < modelTableResult.getRowCount() ; i++) {
                        String  celvalue = (String) modelTableResult.getValueAt(i, 0);
                        if (celvalue.equalsIgnoreCase(fullNameStudent)){
                            int result = JOptionPane.showConfirmDialog(jp_mainCheckTest,
                                    "Данный студент уже добавлен, хотите добавить его еще раз?",
                                    "Сообщение",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE);
                            System.out.println(result);
                            if (result == 1) {
                                return;
                            }else{
                                break;
                            }
                        }
                    }


                    //создаем коллекцию листов для заполнения строк
                    List<String> rowTableResult = new ArrayList<>();
                    List<String> rowSourceAnswer = new ArrayList<>();

                    rowTableResult.add( currientStudent.getFullName());
                    rowSourceAnswer.add( currientStudent.getFullName());

                    //полкчаем лист ответов студента
                    List<String> answerList = new ArrayList<>(Arrays.asList(currientStudent.getAnswers()));

                    //Заполняем таблицу с исходными ответами
                    rowSourceAnswer.addAll(1, answerList);
                    modelTableSoureAnswer.addRow(rowSourceAnswer.toArray());

                    //получаем лист с 0 1 для таблицы Result и заполняем листы для строк
                    int countTrueAnswer = Verification.countTrueAndCheck(answerList, trueAnswer);
                    rowTableResult.addAll(answerList);
                    rowTableResult.add(String.valueOf(countTrueAnswer));
                    modelTableResult.addRow(rowTableResult.toArray());

                    //выводим на таблицы наши строки


                    modelTableResult.removeRow(0);
                    //автоматически высчитываем границы правильных ответов для каждого вопроса
                    if (modelTableResult.getRowCount() > 1){
                        tableInfoRenderer.setLeftLevel((int)(Math.ceil(modelTableResult.getRowCount()*defaultProcLevel)));
                        tableInfoRenderer.setRightLevel(modelTableResult.getRowCount()- tableInfoRenderer.getLeftLevel());
                    }else {
                        tableInfoRenderer.setLeftLevel(0);
                        tableInfoRenderer.setRightLevel(1);
                    }
                    //вставляем строку с подсчитанныем кол-ом правильных ответов в каждом столбце
                    modelTableResult.insertRow(0, countTrueAnswerTheQuestion().toArray());

                    //отображаем расчитанные границы в соответствующих полях
                    tf_leftBorderLine.setText(Integer.toString(tableInfoRenderer.getLeftLevel()));
                    tf_rightBorderLine.setText(Integer.toString(tableInfoRenderer.getRightLevel()));

                    //отображаем панель если скрыта
                    if (!jp_resultStudents.isVisible()) { jp_resultStudents.setVisible(true); };
                }
                setExtendedState(MAXIMIZED_BOTH);
            }
        });


        //Action Open Test
        btn_openTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isException = true;

                JFileChooser fileopen = new JFileChooser();
                if (fileopen.showDialog(null, "Открыть файл") == JFileChooser.APPROVE_OPTION) {
                    currientFileTest = new File((fileopen.getSelectedFile().getAbsolutePath()));
                    try {
                        test = GUI.openObject(test, currientFileTest);
                        isException = false;
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


                if (!isException){//если все открылось без ошибок
                    //получаем модели 3 таблиц
                    DefaultTableModel modelTableTrueAnswer = (DefaultTableModel)tableAnswer.getModel();
                    DefaultTableModel modelTableResult = (DefaultTableModel) table_result.getModel();
                    DefaultTableModel modelTableSourceAnswerStudent = (DefaultTableModel) table_sourceAnswerStudents.getModel();

                    //очистка строк таблы с результатами
                    for (int i = modelTableTrueAnswer.getRowCount(); i > 0  ; i--) {
                        modelTableTrueAnswer.removeRow(i-1);
                    }

                    for (int i = modelTableResult.getRowCount(); i >0 ; i--) {
                        modelTableResult.removeRow(i-1);
                    }

                    for (int i = modelTableSourceAnswerStudent.getRowCount(); i > 0 ; i--) {
                        modelTableSourceAnswerStudent.removeRow(i-1);
                    }

                    //полечаем лист вопросов
                    List<Question> arrQuestions = test.getQuestionsList();
                    String[] row = new String[arrQuestions.size()+1]; //Строки

                    //заполняем заголовки у каждой таблицы если там еще нет ни одного столбца
                    if (modelTableTrueAnswer.getColumnCount() == 0) {
                        modelTableTrueAnswer.addColumn("Вопросы");
                        modelTableResult.addColumn("Студенты/Вопросы");
                        modelTableSourceAnswerStudent.addColumn("Студенты/Вопросы");
                    }

                    //заполняем массив для строки парвильных овтетов и кол-ки для других таблиц
                    row[0] = "Правильные ответы";
                    trueAnswer = new ArrayList<>();
                    for (int i = 0; i <arrQuestions.size() ; i++) {
                        if (modelTableTrueAnswer.getColumnCount() <arrQuestions.size()+1 && modelTableTrueAnswer.getColumnCount()<i+2) {
                            modelTableTrueAnswer.addColumn("№"+ (i+1));
                            modelTableResult.addColumn("№"+ (i+1));
                            modelTableSourceAnswerStudent.addColumn("№"+ (i+1));
                        }

                        row[i+1] = arrQuestions.get(i).getTrueAnswer();
                        trueAnswer.add(row[i+1]);
                    }
                    //добавляем колон-ку для подсчета правильных ответов в кад
                    modelTableResult.addColumn("Кол-во правильных");
                    //заполняем строчку в таблице результатов которая считает кол-во правильных ответов в каждом вопросе
                    modelTableResult.addRow(countTrueAnswerTheQuestion().toArray());
                    //заполняем таблицу правильных ответов
                    modelTableTrueAnswer.addRow(row);

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
                List<Question> questionsList = test.getQuestionsList();
                for (int i = 1; i < tableAnswer.getColumnCount() ; i++) {
                    questionsList.get(i-1).setTrueAndswer(tableAnswer.getValueAt(0, i).toString()); // переписываем значение в вопросах
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
                String result;
                DefaultTableModel modelTableResult = (DefaultTableModel) table_result.getModel();
                modelTableResult.removeRow(0); // Удаление строки с подчетом правильных овтетов в столбцах

                //Сравниеваем значение ячеек таблиц правильных ответов с исходными ответами и заполняем результирующую таблицу
                for (int i = 0; i < table_sourceAnswerStudents.getRowCount() ; i++) {
                    int sumTrueAnswer = 0;
                    for (int j = 1; j < table_sourceAnswerStudents.getColumnCount() ; j++) {
                        if (table_sourceAnswerStudents.getValueAt(i, j) == null || "".equalsIgnoreCase((table_sourceAnswerStudents.getValueAt(i, j).toString().trim()))){
                            result = "";
                        }else if (table_sourceAnswerStudents.getValueAt(i, j).equals(tableAnswer.getValueAt(0, j))) {
                            result = "1";
                            sumTrueAnswer ++;
                        } else {
                            result = "0";
                        }
                        table_result.setValueAt(result, i, j);
                    }
                    table_result.setValueAt(Integer.toString(sumTrueAnswer), i, (table_result.getColumnCount()-1));
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

        //Action set right level
        btn_setRigth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GUI.isInt(tf_rightBorderLine.getText())){
                    tableInfoRenderer.setRightLevel(Integer.parseInt(tf_rightBorderLine.getText()));
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

    //метод построение окна и его отображения
    public void showWindows(){

        table_result.setDefaultRenderer(Object.class, tableInfoRenderer);

        setContentPane(jp_mainCheckTest);
        hideElements(btn_openTestStudents, jp_resultStudents, jp_trueAnswer,jp_sourceAnswerStudents);
//        setSize(750, 400);
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }



    abstract class Viewer implements View{


    }


}




