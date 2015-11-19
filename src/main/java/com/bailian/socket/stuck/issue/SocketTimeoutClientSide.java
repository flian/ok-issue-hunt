package com.bailian.socket.stuck.issue;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class SocketTimeoutClientSide {
    public static void main(String[] args) throws InterruptedException, IOException {
        Config.checkLog(SocketTimeoutClientSide.class);
        final SocketPools pool = new SocketPools(Config.ip(), Config.port());
        //等待连接初始化完成
        Thread.sleep(3*1000L);
        int threadCount = pool.poolSize() * 2;
        for (; threadCount > 0; threadCount--) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String preFix="["+Thread.currentThread().getId()+"+"+Thread.currentThread().getName()+"]";
                    do {

                        Socket client = null;
                        do {
                            client = pool.borrow();
                            try {
                                Thread.sleep(3000L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } while (null == client);
                        //拿到连接后开始计时
                        long startTime = System.currentTimeMillis();
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
                        if(60*1000L <= howLong){
                            StringBuffer sb=new StringBuffer();
                            sb.append("#####################ERROR#####################").append("\n");
                            sb.append("start dateTime:" + new Date(startTime)).append("\n");
                            sb.append("end dateTime:" + new Date(endTime)).append("\n");
                            sb.append(preFix + "####ERROR####本次共执行超过一分钟，看起来卡了。" + howLong).append("\n");
                            sb.append("###############################################").append("\n");
                            System.err.println(sb.toString());
                        }
                        //随机休息0-5分钟？
                        try {
                            Thread.sleep(Config.randomSleep());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } while (true);

                }
            }).start();

        }

    }

}
