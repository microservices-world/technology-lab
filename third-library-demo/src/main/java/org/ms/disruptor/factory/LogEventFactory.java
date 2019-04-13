package org.ms.disruptor.factory;

import com.lmax.disruptor.EventFactory;
import org.ms.disruptor.entity.LogEvent;

/**
 * 创建LogEvent对象
 * @author Zhenglai
 * @since 2019-04-13 22:35
 */
public class LogEventFactory implements EventFactory<LogEvent> {
    public LogEvent newInstance() {
        return new LogEvent();
    }
}
