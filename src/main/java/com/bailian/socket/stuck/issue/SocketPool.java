package com.bailian.socket.stuck.issue;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by foy on 2015-11-17.
 */
public class SocketPool {
    private static final int DEFAULT_POOL_SIZE = 10;
    private SocketWrap[] poolledEntity;


    public SocketPool(String server, int port, int poolSize) throws IOException {
        poolledEntity = new SocketWrap[poolSize];
        for (int i = 0; i < poolSize; i++) {
            poolledEntity[i] = new SocketWrap(server, port);
        }
    }

    public SocketPool(String server, int port) throws IOException {
        this(server, port, DEFAULT_POOL_SIZE);
    }

    public void close() {
        for (int i = 0; i < poolledEntity.length; i++) {
            Socket s = poolledEntity[i];
            if (s != null && !s.isClosed()) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                poolledEntity[i] = null;
            }

        }
    }
    public String poolStatus(){
        StringBuffer sb = new StringBuffer(100);
        int usingCnt=0;
        int freeCnt=0;
        int exceptionCnt=0;
        int totalCnt=poolledEntity.length;

        for (int i = 0; i < poolledEntity.length; i++) {
            SocketWrap s = poolledEntity[i];
            if(s == null){
                exceptionCnt ++;
            }else if(s.isUsing){
                usingCnt++;
            }else {
                freeCnt++;
            }
        }
        sb.append("[ Total Pooled Socket ").append(totalCnt).append("\n");
        sb.append("currently free Socket ").append(freeCnt).append("\n");
        sb.append("currently Using Socket ").append(usingCnt).append("\n");
        sb.append("currently NULL Socket ").append(exceptionCnt).append("]\n");
        String result=sb.toString();
        System.out.println(result);
        return result;
    }
    public int poolSize() {
        return poolledEntity.length;
    }

    public Socket borrow() {
        for (int i = 0; i < poolledEntity.length; i++) {
            SocketWrap s = poolledEntity[i];
            if (s != null && !s.isUsing) {
                synchronized (s) {
                    if (!s.isUsing) {
                        s.isUsing = true;
                        return s;
                    }
                }
            }
        }
        System.out.println("can't borrow socket,just return null!");
        return null;
    }

    static class SocketWrap extends Socket {
        private volatile boolean isUsing = false;
        public SocketWrap(String server, int port) throws IOException {
            super(server, port);
        }

        @Override
        public synchronized void close() {
            isUsing = false;
        }

        public synchronized void release() throws IOException {
            super.close();
        }
    }


}
