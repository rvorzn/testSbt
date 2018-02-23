package ru.company.testing;



public class Main {

    public static void main(String[] args) {

        StartDialog startDialog = new StartDialog();
        startDialog.showWindow();
    }

    public static void startTest(Test test){
        new TestGui();
    }


    public static void  showDialogTeacher(){
        new CreateTest().showWindow();

    }
    public static void  showDialogStudet(){
       new TestGui().start();
    }

    public static void showDialogCheckTest(){
        CheckTests checkTests = new CheckTests();
        checkTests.showWindows();
    }
}
