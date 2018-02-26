package ru.company.testing;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ColorModel;
import java.io.*;

public class GUI {


    static <T> void saveObjectDialog(T clz) throws IOException {
        JFileChooser fc = new JFileChooser();
        if (fc.showDialog(null, "Сохранить как") == JFileChooser.APPROVE_OPTION) {
            saveObject(clz, new File((fc.getSelectedFile().getAbsolutePath())));
        }

    }

    static <T> void saveObject(T clz, File file) throws IOException {

        try (FileOutputStream out = new FileOutputStream(file);
             ObjectOutputStream outputStream = new ObjectOutputStream(out))
        {
            outputStream.writeObject(clz);
        }
    }

    static <T> T openObjectDialog(T clz) throws IOException, ClassNotFoundException, ClassCastException{
        JFileChooser fileopen = new JFileChooser();
        if (fileopen.showDialog(null, "Открыть файл") == JFileChooser.APPROVE_OPTION) {
            File file = new File((fileopen.getSelectedFile().getAbsolutePath()));
            return openObject(clz, file);
        }
        return null;
    }


    static <T> T openObject(T clz, File currientFile) throws IOException, ClassNotFoundException, ClassCastException {
            try (FileInputStream in = new FileInputStream(currientFile);
                 ObjectInputStream inputStream = new ObjectInputStream(in))
            {
                return (T) inputStream.readObject();
            }
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
                label.setText("Результат операции: "  + message);
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

