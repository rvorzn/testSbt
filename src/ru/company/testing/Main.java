package ru.company.testing;



public class Main {

    public static void main(String[] args) {
        new CreateTest().showWindow();
    }

    public static void showDialogCheckTest(){
        CheckTests checkTests = new CheckTests();
        checkTests.showWindows();
    }
}
