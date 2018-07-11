package com.jy.study.spring.websocket.study;

public class ThreadTest {
    public static void main(String[] args) {
//        joinTest();
        interruptTest();

    }

    private static void joinTest() {
        Parent parent = new Parent();
        parent.start();
    }

    private static void interruptTest() {
        Worker worker = new Worker();
        worker.start();
        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        worker.interrupt();
        System.out.println("main exit");
    }
}

class Worker extends Thread {
    @Override
    public void run() {
        System.out.println("worker begin, Thread status:" + Thread.currentThread().getState() + ", interrupt: " + isInterrupted());
        try {
            int index = 0;
            while (!isInterrupted()) {
                System.out.println("index: " + index++);
//                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("worker exit, Thread status: " + Thread.currentThread().getState() + ", interrupt: " + isInterrupted());
    }
}



class Parent extends Thread{
    @Override
    public void run() {
        System.out.println("parent start");
        Thread sub = new Sub();
        sub.start();
        try {
            sub.join();
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        System.out.println("parent end");
    }
}

class Sub extends Thread {
    @Override
    public void run() {
        System.out.println("sub start");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sub end");
    }
}
