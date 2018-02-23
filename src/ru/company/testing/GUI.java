package ru.company.testing;

import javax.swing.*;
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

}

