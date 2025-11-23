package com.eli.bettermb.cli;
public class Waiter {
    private volatile boolean done = false;


    public synchronized void waitUntilDone() throws InterruptedException {
        while (!done) {
            wait();                 // wait until someone calls notifyAll()
        }
    }

    public synchronized void markNotDone() {
        done = false;                // update flag
    }
    public synchronized void markDone() {
        done = true;                // update flag
        notifyAll();                // wake up waiting thread(s)
    }
}
