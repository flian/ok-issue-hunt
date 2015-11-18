package com.bailian.socket.stuck.issue;

/**
 * Created by Administrator on 2015-11-18.
 */
public class Test {
    public static void main(String[] args) {
        while(true){
            System.out.println(SocketTimeoutClientSide.randomSleep()/1000);
        }
    }
}
