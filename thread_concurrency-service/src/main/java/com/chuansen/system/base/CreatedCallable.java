package com.chuansen.system.base;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/9/3 16:46
 */
public class CreatedCallable {
    public static void main(String[] args) {
        ThreadC threadC=new ThreadC();
        FutureTask<String> futureTask=new FutureTask(threadC);
        new Thread(futureTask).start();
        System.out.println("这是主线程:  begin");
        try {
            System.out.println("返回结果："+ futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("这是主线程:  end");
    }
}

class ThreadC implements Callable<String> {

    public String call() throws Exception {
        Thread.sleep(500L);
        System.out.println("这是线程C");
        return "ThreadC";
    }
}

