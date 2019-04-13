package org.ms.disruptor.producer;

import com.lmax.disruptor.RingBuffer;
import org.ms.disruptor.entity.LogEvent;

import java.nio.ByteBuffer;

/**
 * LogEvent生产者
 * 
 * @author Zhenglai
 * @since 2019-04-13 22:38
 */
public class LogEventProducer {
    public LogEventProducer(RingBuffer<LogEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer byteBuffer) {
        // 获取事件队列下标位置
        long sequence = ringBuffer.next();
        try {
            // 取出空事件(Event)
            LogEvent event = ringBuffer.get(sequence);
            // 给空事件赋值
            event.setValue(byteBuffer.getLong(0));
        } finally {
            System.out.println("producer sent data...");
            //  发送数据
            ringBuffer.publish(sequence);
        }
    }

    private RingBuffer<LogEvent> ringBuffer;
}
