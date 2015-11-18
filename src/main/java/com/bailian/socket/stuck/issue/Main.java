package com.bailian.socket.stuck.issue;

import java.util.Scanner;

/**
 * Created by foy on 2015-11-17.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        int choose = 0;
        if (args == null || args.length < 1) {
            useage();
            Scanner in = new Scanner(System.in);
            choose = in.nextInt();
        }else{
            choose=Integer.parseInt(args[0]);
        }
        switch (choose) {
            case 1: {
                SocketTimeoutTestServerSide.main(args);
                break;
            }
            case 2: {
                SocketTimeoutClientSide.main(args);
                break;
            }

            default: {
                System.out.println("pending.....");
                break;
            }
        }
    }

    public static void useage() {
        System.console().printf("#############useage####################\n");
        System.console().printf("1, start Server socket\n");
        System.console().printf("2, start Client socket\n");
        System.console().printf("#######################################\n");
        System.console().printf("type number choose what u want?\n");
    }
}
