package com.snakebattle.botrunningsystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BotPool extends Thread{
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition= lock.newCondition();

    // here I choose condition variable to handle race condition
    // so here just use queue
    private final Queue<Bot> bots = new LinkedList<>();

    // add bot to queue
    public void addBot(Integer userId, String botCode, String input){
        lock.lock();
        try{
            bots.add(new Bot(userId, botCode, input));
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    // here simply assume code is java
    // has risk of code injection!
    // to do: use docker to run other code
    private void consume(Bot bot){
        Consumer consumer = new Consumer();
        consumer.startTimeout(2000,bot);
    }

    @Override
    public void run() {
        while(true){
            lock.lock();
            if(bots.isEmpty()){
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            } else{
                Bot bot = bots.remove();
                lock.unlock();
                consume(bot);
            }
        }
    }
}
