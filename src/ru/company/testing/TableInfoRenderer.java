package ru.company.testing;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;

public class TableInfoRenderer extends DefaultTableCellRenderer {
    final private Color WHITE = new Color(255,255, 255);
    final private Color GREEN = new Color(57, 244, 116);
    final private Color RED = new Color(236,60, 60);


    private int level= 1;
    private int rightLevel =5;
    private int leftLevel =2;



    public void setLevel(int level) {
        this.level = level;
    }

    public void setRightLevel(int rightLevel) {
        this.rightLevel = rightLevel;
    }

    public void setLeftLevel(int leftLevel) {
        this.leftLevel = leftLevel;
    }

    public int getLevel() {
        return level;
    }

    public int getRightLevel() {
        return rightLevel;
    }

    public int getLeftLevel() {
        return leftLevel;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String cellvalue;

        //защита от NullPointExeption
        if (value != null){
             cellvalue = value.toString();
        } else {
            cellvalue = "";
        }

        //Окраска в правильных и неправильных вариант
        if (column != table.getColumnCount()-1){
            if ("".equalsIgnoreCase(cellvalue.trim())){
                cell.setBackground(WHITE);
            }else if (GUI.isInt(cellvalue)){
                if (cellvalue.equals("1")) {
                    cell.setBackground(GREEN);
                } else {
                    cell.setBackground(RED);
                }
            } else{
                cell.setBackground(WHITE);

            }
            } else {
                if  (GUI.isInt(cellvalue) && Integer.parseInt(cellvalue) > level) {
                    cell.setBackground(GREEN);
                }else{
                    cell.setBackground(WHITE);
                }

            }

            //Окраска ячеек которые подсчитывают кол-во правильных ответов в каждом вопросе
            if (row == 0){
                if (GUI.isInt(cellvalue)){
                    if (Integer.parseInt(cellvalue) >= rightLevel) {
                        cell.setBackground(GREEN);
                    }else
                        if (Integer.parseInt(cellvalue) <= leftLevel){
                            cell.setBackground(RED);
                        }else{
                            cell.setBackground(WHITE);
                        }
                }else {
                    cell.setBackground(WHITE);
                }

            }

            //Условия для белых полей
            if (column == 0 ){ // 1 столбец
                cell.setBackground(WHITE);
            }


        return cell;
    }


}


