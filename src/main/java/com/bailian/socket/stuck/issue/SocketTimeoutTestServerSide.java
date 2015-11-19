package com.bailian.socket.stuck.issue;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SocketTimeoutTestServerSide {

    public static void main(String args[]) throws IOException {
        Config.checkLog(SocketTimeoutTestServerSide.class);
        ServerSocket server = new ServerSocket(Config.port(), 200,
                InetAddress.getByName(Config.ip()));
        System.out.println("server start:" + server.getInetAddress()+ ":"+ server.getLocalPort());
        while (true) {
            final Socket accept = server.accept();
            Thread t = new Thread() {
                @Override
                public void run() {
                    final String msg = "hello Client Socket, Glad to see u.\n wishing next time u come!\nEND\n";
                    outer:
                    while (true) {
                        try {
                            Scanner in = new Scanner(accept.getInputStream());
                            String s;
                            inner:
                            while (in.hasNext()) {
                                s = in.nextLine();
                                if ("bye".equalsIgnoreCase(s)) {
                                    System.out.println("client side say BYE, will try to close socket by server side!");
                                    accept.close();
                                    break outer;
                                } else if ("end".equalsIgnoreCase(s)) {
                                    System.out.println("client side say END,exit now!");
                                    break inner;
                                }
                                System.out.println(s);
                            }
                            accept.getOutputStream().write(msg.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            };
            t.start();
        }
    }
}
