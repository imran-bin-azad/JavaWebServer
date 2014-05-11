package net.therap.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: imran.azad
 * Date: 5/7/14
 * Time: 9:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class ThreadPool {

    private ExecutorService executor;

    public ThreadPool(int threadLimit) {
        executor = Executors.newFixedThreadPool(threadLimit);
    }

    public void runNewThread(Runnable object) {
        executor.execute(object);
    }

    public void shutdown() {
        executor.shutdown();
        while (!executor.isTerminated()){}
        System.out.println("shutting down Thread Pool");
    }

}
