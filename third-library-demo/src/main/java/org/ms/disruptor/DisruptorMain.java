package org.ms.disruptor;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ms.disruptor.consumer.LogEventHandler;
import org.ms.disruptor.entity.LogEvent;
import org.ms.disruptor.factory.LogEventFactory;
import org.ms.disruptor.producer.LogEventProducer;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * @author Zhenglai
 * @since 2019-04-13 22:39
 */
public class DisruptorMain {
    public static void main(String[] args) {
        // 为consumer创建处理线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        EventFactory<LogEvent> logEventFactory = new LogEventFactory();
        int ringBufferSize = 1024 * 1024; // 2^N
        Disruptor<LogEvent> logEventDisruptor = new Disruptor<LogEvent>(logEventFactory, ringBufferSize,
            executorService, ProducerType.MULTI, new YieldingWaitStrategy());
        // 注册消费者
        // 可以注册多个消费者，默认重复消费；可以配置分组均摊消费；或者按比例消费
        logEventDisruptor.handleEventsWith(new LogEventHandler());
        logEventDisruptor.start();

        RingBuffer<LogEvent> ringBuffer = logEventDisruptor.getRingBuffer();

        LogEventProducer logEventProducer = new LogEventProducer(ringBuffer);
        ByteBuffer buffer = ByteBuffer.allocate(8);
        for (int i = 0; i < 100; i++) {
            buffer.putLong(0, i);
            logEventProducer.onData(buffer);
        }
        executorService.shutdown();
        // executorService.awaitTermination(10, TimeUnit.SECONDS);
    }
}
