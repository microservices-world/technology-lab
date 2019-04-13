package org.ms.disruptor.consumer;

import com.lmax.disruptor.EventHandler;
import org.ms.disruptor.entity.LogEvent;

/**
 * LogEvent消费者
 * 
 * @author Zhenglai
 * @since 2019-04-13 22:36
 */
public class LogEventHandler implements EventHandler<LogEvent> {
    public void onEvent(LogEvent logEvent, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("consumer got event:" + logEvent.getValue());
    }
}
