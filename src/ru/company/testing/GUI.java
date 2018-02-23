package ru.company.testing;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ColorModel;
import java.io.*;

public class GUI {


    static <T> void saveObject(T clz, File file) throws IOException {
        System.out.println(file.getAbsolutePath());
        try (FileOutputStream out = new FileOutputStream(file);
             ObjectOutputStream outputStream = new ObjectOutputStream(out))
        {
            outputStream.writeObject(clz);
        }
    }

    static <T> T openObject(T clz, File currientFile) throws IOException, ClassNotFoundException {
        T test = null;
            try (FileInputStream in = new FileInputStream(currientFile);
                 ObjectInputStream inputStream = new ObjectInputStream(in))
            {
                test = (T) inputStream.readObject();
            }
        return test;
    }

    public static boolean isInt(String string ) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void hideElements(JComponent... arr_comp){
        for (JComponent jcom: arr_comp) {
            jcom.setVisible(false);
        }
    }
    public static  void showElements(JComponent... arr_comp){
        for (JComponent jcom: arr_comp) {
            jcom.setVisible(true);
        }
    }


    public static void showMessage(String message,JLabel label, int timeInSeconds, Color color ){
        new Thread(new Runnable() {
            @Override
            public void run() {
                label.setForeground(color);
                label.setText(message);
                try {
                    Thread.sleep(timeInSeconds*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                label.setText("");
            }
        }).start();
    }

    public static void showMessage(String message,JLabel label, int timeInSeconds ){
        showMessage(message, label, timeInSeconds, Color.black);
    }

    public static void showMessage(String message,JLabel label){
        showMessage(message, label, 3, Color.black);
    }

    public static void showMessage(String message,JLabel label, Color color){
        showMessage(message, label, 3, color);
    }

}

