package org.ms.webfluxdemo;

import org.bson.ByteBuf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Zhenglai
 * @since 2019-04-11 01:27
 */
public class FileChannelDemo {
    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("~/.gitignore");
        FileChannel fileChannel = inputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        fileChannel.read(byteBuffer);
        fileChannel.close();
        byteBuffer.flip();
    }

    public static void copyFile(String src, String dst) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(src);
             FileOutputStream outputStream = new FileOutputStream(dst)
        ) {
            FileChannel readChannel = inputStream.getChannel();
            FileChannel writeChannel = outputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                buffer.clear();
                int len = readChannel.read(buffer);
                if (len == -1) {
                    break;
                }
                buffer.flip();
                writeChannel.write(buffer);
            }
            readChannel.close();
            writeChannel.close();
        }
    }
}
