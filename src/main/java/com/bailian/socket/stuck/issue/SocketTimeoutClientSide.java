package com.bailian.socket.stuck.issue;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketTimeoutClientSide {
    public static void main(String[] args) throws InterruptedException, IOException {
        if (Config.isLogRedirect()) {
            PrintStream ps = new PrintStream(new FileOutputStream(
                    Config.cLog()));
            System.setErr(ps);
            System.setOut(ps);
        }
        final SocketPool pool = new SocketPool(Config.ip(), Config.port());
        //等待连接初始化完成
        Thread.sleep(5*1000L);
        int threadCount = pool.poolSize() * 2;
        for (; threadCount > 0; threadCount--) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String preFix="["+Thread.currentThread().getId()+"+"+Thread.currentThread().getName()+"]";
                    do {
                        long startTime = System.currentTimeMillis();
                        Socket client = null;
                        do {
                            client = pool.borrow();
                            try {
                                Thread.sleep(3000L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } while (null == client);
                        try {
                            OutputStream out = client.getOutputStream();
                            out.write((preFix+"Hello Server, I'm SocketTimeoutClientSide\nEND\n").getBytes());
                            out.flush();
                            Scanner in = new Scanner(client.getInputStream());
                            while (in.hasNext()) {
                                String s = in.nextLine();
                                System.out.println(preFix+"Server said: " + s);
                                if ("end".equalsIgnoreCase(s)) {
                                    System.out.println(preFix+"Server said END, will return Socket to pool!");
                                    //return socket to pool
                                    if (client != null) client.close();
                                    break;
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        pool.poolStatus();
                        long endTime = System.currentTimeMillis();
                        long howLong=(endTime - startTime)/(1000);
                        System.out.println(preFix+"本次共执行"+howLong+"秒钟");
                        if(60*1000L < howLong){
                            System.out.println(preFix+"####ERROR####本次共执行超过一分钟，看起来卡了。"+howLong);
                        }
                        //休息3秒？
                        try {
                            Thread.sleep( 3 * 1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } while (true);

                }
            }).start();

        }

    }
}
