package com.epam.rd.java.basic.practice5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Spam {

    private static final Logger LOGGER = Logger.getLogger(Spam.class.getName());
    private static final String MESSAGE_INTERRUPTED_EXCEPTION = "Interrupted exception";
    private Thread[] threads;
    private String[] messages;
    private int[] times;

    public Spam(String[] messages, int[] delays) {
        this.messages = messages;
        this.times = delays;
        this.threads = new Thread[messages.length];
    }

    public static void main(final String[] args) {

        String[] messages = new String[] { "@@@", "bbbbbbb" };
        int[] times = new int[] { 333, 222 };

        Spam spam = new Spam(messages, times);


        spam.start();
        spam.waitPrintEnter();
        spam.stop();

    }

    public void start() {

        for(int i = 0; i < messages.length; i++) {

            String message = messages[i];
            int time = times[i];

            Thread thread = new Worker(message, time);
            threads[i] = thread;
            thread.start();

        }

    }

    public void stop() {

        for(Thread thread : threads) {
            thread.interrupt();
        }

        for(Thread thread : threads) {

            try {
                thread.join();
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, MESSAGE_INTERRUPTED_EXCEPTION, e);
                Thread.currentThread().interrupt();
            }
        }

    }

    private void waitPrintEnter() {

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {

            while (!((bufferedReader.readLine()).equals("")));

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IOException exception", e);
            Thread.currentThread().interrupt();
        }

    }

    public static class Worker extends Thread {

        private final String message;
        private final int time;

        public Worker(String message, int time) {
            this.message = message;
            this.time = time;
        }

        @Override
        public void run() {

            while (!isInterrupted()) {
                threadAction();
            }
        }

        private void threadAction() {

            System.out.println(message);

            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, MESSAGE_INTERRUPTED_EXCEPTION, e);
                Thread.currentThread().interrupt();
            }
        }
    }

}
