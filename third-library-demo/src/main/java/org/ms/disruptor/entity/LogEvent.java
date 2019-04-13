package org.ms.disruptor.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 声明一个event，生产者和消费者传递的数据类型
 * @author Zhenglai
 * @since 2019-04-13 22:33
 */
@Data
@NoArgsConstructor
public class LogEvent {
    private Long value;
}
