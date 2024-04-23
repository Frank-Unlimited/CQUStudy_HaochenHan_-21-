package client;

import java.io.*;
//import java.net.InetAddress;
import java.net.Socket;

/*
 * client socket main程序
 * 首先我们需要先对该socket创建对应的消息MAP，并初始化MAP中每个消息队列
 * 之后执行main函数，创建socket，开启读写线程，开启GUI
 */
public class clientSocket {

    public static clientINFO<Integer, clientProtocol> queue;
    static {
        queue = new clientINFO<Integer, clientProtocol>();
        //发送的消息队列 id == 0
        queue.putIfAbsent(0);
        //接收的消息队列 id == 协议type
        queue.putIfAbsent(3);
        queue.putIfAbsent(4);
        queue.putIfAbsent(5);
        queue.putIfAbsent(11);
        queue.putIfAbsent(44);
        queue.putIfAbsent(99);
        queue.putIfAbsent(55);
        queue.putIfAbsent(77);
        queue.putIfAbsent(72);
        queue.putIfAbsent(166);
        queue.putIfAbsent(33);
        queue.putIfAbsent(13);
        queue.putIfAbsent(100);
    }

    public static void main(String [] args) throws IOException, ClassNotFoundException {
        // 获取客户端链接
        Socket client = new Socket("192.168.43.238", 65532); //192.168.56.1-->103.46.128.49  218.89.171.137
        System.out.println("客户端是否连接成功:" + client.isConnected());
        // 输入输出流初始化
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
        // 读写线程初始化
        ReadThread r = new ReadThread(client.getInputStream(),objectInputStream);
        WriteThread w = new WriteThread(client.getOutputStream(),objectOutputStream);
        // 读写线程启动
        r.start();
        w.start();
        // GUI启动
        clientGUI.runGUI();

        //主线程等待读写线程结束
        try {
            r.join();
            w.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("客户端关闭");
        client.close();
    }
}


