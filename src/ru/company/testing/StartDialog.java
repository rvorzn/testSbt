package ru.company.testing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartDialog  extends  JFrame{
    private JPanel jp_mainStartDialog;

    private JButton btn_teacher;
    private JButton btn_student;



    public StartDialog() {
        //Action show window teacher administation
        btn_teacher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.showDialogTeacher();
                setVisible(false);
            }
        });

        //Action show window for beginnig test
        btn_student.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.showDialogStudet();
            }
        });

    }
    public void showWindow(){
        setContentPane(jp_mainStartDialog);
        setSize(750, 400);
        setVisible(true);
        pack();
    }
}
