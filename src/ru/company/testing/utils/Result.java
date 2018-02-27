package ru.company.testing.utils;

import java.io.Serializable;

public class Result implements Serializable{
    private String[][] tableResult;

    public String[][] getTableResult() {
        return tableResult;
    }

    public void setTableResult(String[][] tableResult) {
        this.tableResult = tableResult;
    }

}
