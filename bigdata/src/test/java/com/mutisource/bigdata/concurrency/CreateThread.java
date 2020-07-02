package com.mutisource.bigdata.concurrency;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


/**
 * @author Jeremy
 * @create 2020 07 03 4:03
 */
@SpringBootTest
public class CreateThread {

    private void businessMethod() {
        System.out.println("________ 我是业务代码 ________");
    }

    private int deal() {
        return 1311;
    }

    @Test
    public void createByThread() {
        // 步骤一：创建线程
        Thread thread = new Thread("thread_001") {
            public void run() {
                // 业务代码
                businessMethod();
            }
        };
        // 步骤二： 启动线程，此时线程状态为就绪状态，等待cup调度器调度
        thread.start();
        System.out.println("^^^^^^ 我是main主线程 ^^^^^^^");
    }

    @Test
    public void createByRunnable() {
        // 1. 创建线程
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                businessMethod();
            }
        },"Runnable");

        // 2. 启动
        thread.start();
    }

    @Test
    public void createByRunnableLambda() {
        // 1. 创建线程 ---> lambda简化代码
        Thread thread = new Thread(this::businessMethod,"RunnableLambda");
        // 2. 启动
        thread.start();
    }

    @Test
    public void createByFutureTask() throws ExecutionException, InterruptedException {
        // futureTask
        FutureTask futureTask = new FutureTask<>(this::deal);
//        注意 Object o = futureTask.get();  此时线程尚未执行，调用get方法阻塞
//        System.out.println(o);
        // 1. 创建线程
        Thread thread = new Thread(futureTask,"future_task");
        // 2. 启动
        thread.start();

        Object o1 = futureTask.get();
        System.out.println(o1);

    }


    //


}
