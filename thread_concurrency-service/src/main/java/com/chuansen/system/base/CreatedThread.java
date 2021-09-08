package com.chuansen.system.base;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/9/3 16:29
 */
public class CreatedThread {

    public static void main(String[] args) {
        ThreadA threadA=new ThreadA();
        threadA.run();
        System.out.println("这是主线程");
    }
}


class ThreadA extends Thread{
    @Override
    public void run(){
        super.run();
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("这是线程A");
    }

}

