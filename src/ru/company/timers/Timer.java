package ru.company.timers;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;

public class Timer implements Runnable {
    private int time;
    private int countPause;
    private boolean isPause;

    Timer(int time){
        this.time = time;
        this.countPause = Integer.MAX_VALUE;
    }

    public Timer(int time, int countPause){
        this.time = time;
        this.countPause = countPause;
    }

    public int getTime() {
        return time;
    }

    public int getCountPause() {
        return countPause;
    }

    public boolean isPause() {
        return isPause;
    }

    public boolean pause(){
        isPause = countPause > 0 ? true : false;
        return isPause;
    }

    public void go(){
        isPause = false;
        countPause--;
    }

    public boolean stop(){
        return  interrupted();
    }

    @Override
    public void run() {
        int min = 0;
        while (!(Thread.currentThread().isInterrupted()) && !(time==0)){
            while (isPause) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                sleep(1000);
                min += 1000;
                if (min == 60000) {
                    time--;
                    min = 0;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


