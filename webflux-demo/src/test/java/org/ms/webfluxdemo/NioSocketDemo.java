package org.ms.webfluxdemo;

import com.mongodb.connection.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.Key;
import java.util.Iterator;

/**
 * @author Zhenglai
 * @since 2019-04-11 00:03
 */
public class NioSocketDemo {
    private Selector selector;

    public static void main(String[] args) throws IOException {
        NioSocketDemo nioSocketDemo = new NioSocketDemo();
        nioSocketDemo.initServer(7777);
        nioSocketDemo.listenSelector();
    }

    public void initServer(int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(port));

        this.selector = Selector.open();
        serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        System.out.println(Thread.currentThread().getName()+"\tserver started...");
    }

    public void listenSelector() throws IOException {
        while (true) {
            // 等待客户端连接
            // selector模型，多路复用
            System.out.println(Thread.currentThread().getName() + "\tselecting...");
            this.selector.select();
            Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey selectionKey = keys.next();
                keys.remove();
                // process request
                handler(selectionKey);
            }
        }
    }

    private void handler(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isAcceptable()) {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            // 接受客户端发送的信息；需要给通道设置读的权限
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()) {
            // 处理读的事件
            SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int readData = socketChannel.read(buffer);
            if (readData > 0) {
                String info = new String(buffer.array(), "GBK").trim();
                System.out.println(Thread.currentThread().getName() + "\tserver received data:" + info);
            } else {
                System.out.println(Thread.currentThread().getName() + "\tclient closed...");
                selectionKey.cancel();
            }
        }
    }
}
